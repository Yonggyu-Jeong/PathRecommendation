package com.example.shareonfoot.closet;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.shareonfoot.HTTP.Service.MapService;
import com.example.shareonfoot.R;
import com.example.shareonfoot.VO.ClothesVO;
import com.example.shareonfoot.util.ClothesListAdapter;
import com.example.shareonfoot.util.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;

/* 그리드 사이즈 조절 방법 :
어댑터 변경, 그리드 사이즈 변경, 페이지사이즈 변경ㅆ
(small(4), medium(3), large(2)) 20p, 15p, 10p
*/

public class TabFragment_Clothes_inCloset extends Fragment {

    private static String option;
    fragment_closet parentFragment;
    String identifier; //프래그먼트의 종류를 알려줌
    Integer pos = 10;
    String location;
    public RelativeLayout Cloth_Info;
    private static final int UPDATE_INTERVAL_MS = 120000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 60000; // 0.5초
    Integer page = 0;
    RecyclerView rv_clothes;
    ArrayList<ClothesVO> clothesList = new ArrayList<ClothesVO>();
    Location mCurrentLocatiion;
    private LocationRequest locationRequest;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient; // Deprecated된 FusedLocationApi를 대체
    private LatLng mDefaultLocation = new LatLng(37.375280717973304, 126.63289979777781);
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    LatLng latLng = null;
    private boolean mLocationPermissionGranted;
    //리사이클러뷰 어댑터
    ClothesListAdapter clothesListAdapter;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public static String ErrMag = "ErrMag";
    public String err;
    private int choose = 0;

    public static TabFragment_Clothes_inCloset newInstance(String location, String identifier) {
        Bundle args = new Bundle();
        args.putString("location", location);
        args.putString("identifier", identifier);  // 키값, 데이터

        TabFragment_Clothes_inCloset fragment = new TabFragment_Clothes_inCloset();
        fragment.setArguments(args);
        return fragment;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentFragment = ((fragment_closet) getParentFragment());

        Bundle args = getArguments(); // 데이터 받기
        if (args != null) {
            location = args.getString("location");
            identifier = args.getString("identifier");
        }

       /* SharedPreferences sharedPreferences = getContext().getSharedPreferences("tab",0);
        choose = sharedPreferences.getInt("pos", choose);
        Log.e("sharedPreferences", ""+choose);
        if(choose == 0) {
            option = "CS99";
        } else if(choose == 1) {
            option = "CS01";
        } else if(choose == 2) {
            option = "CS04";
        } else if(choose == 3) {
            option = "CS05";
        } else if(choose == 4) {
            option = "CS06";
        } else if(choose == 5) {
            option = "CS07";
        } else if(choose == 6) {
            option = "CS08";
        } else {
            option = "CS99";
        }
        Log.e("option", option);

        clothesListAdapter = new ClothesListAdapter(getContext(), clothesList, R.layout.fragment_recyclerview);*/

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String coordinates[] = {page.toString(), ""};
        //리사이클러 뷰 설정하기
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        rv_clothes = (RecyclerView) view.findViewById(R.id.tab_clothes_rv);
        rv_clothes.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_clothes.setNestedScrollingEnabled(true);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //스크롤이 최상단이면 데이터를 갱신한다
                clothesList.clear();
                page = 0;
                String coordinates[] = {page.toString()};
                getLocateList(option);
                clothesListAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
       /* String result = getLocateList(option);
        Log.e("result 작동 테스트", result);
        clothesListAdapter.notifyDataSetChanged();*/
        // mSwipeRefreshLayout.setRefreshing(false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    public void init() {
        Log.e("init-identifier", identifier);
        switch (identifier) {
            case "모두":
                option = "CS99";
                break;
            case "음식점":
                option = "CS01";
                break;
            case "역사관광지":
                option = "CS04";
                break;
            case "자연관광지":
                option = "CS05";
                break;
            case "체험관광지":
                option = "CS06";
                break;
            case "테마관광지":
                option = "CS07";
                break;
            case "기타관광지":
                option = "CS08";
                break;
        }
        clothesList.clear();
        getLocateList(option);
    }

    //프래그먼트 갱신
    private void refresh() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
    }


    public void getLocateList(String name) {
        GetCategoryListTask mapTask = new GetCategoryListTask(requireContext());
        String result;
        Utils utils = new Utils();
        try {
            result = mapTask.execute(name).get();
            if(!result.equals("fail")){
                int i = 0;
                Integer image = 0;
                ArrayList<String> jidx = new ArrayList();
                ArrayList<String> jname = new ArrayList();
                ArrayList<String> jcategory = new ArrayList();
                ArrayList<String> jstar = new ArrayList();
                ArrayList<String> jadress = new ArrayList();
                ArrayList<String> jreview = new ArrayList();
                ArrayList<Integer> jimage = new ArrayList();

                try {
                    JSONArray jarray = new JSONObject(result).getJSONArray("result");
                    if (jarray != null) {
                        final int numberOfItemsInResp = jarray.length();

                        for (i = 0; i < numberOfItemsInResp; i++) {
                            JSONObject jsonObject = jarray.getJSONObject(i);
                            String idx = jsonObject.getString("locate_id");
                            String star = jsonObject.getString("rate");
                            String review = "";
                            String tags = "";
                            for(int j=1; j<5; j++) {
                                tags += "#"+utils.getTagCategory(jsonObject.getString("tag"+j).toString());
                                if(j!=4) {
                                    tags += "\n";
                                }
                            }
                            if (!jsonObject.isNull("info")) {
                                review = jsonObject.getString("info");
                            }
                            String adress = jsonObject.getString("area");
                            String name1 = jsonObject.getString("name");
                            String category = jsonObject.getString("category");
                            jidx.add(idx);
                            jname.add(name1);
                            jcategory.add(tags);
                            jstar.add(star);
                            jadress.add(adress);
                            jreview.add(review);

                            switch (category) {
                                case "CS01": //모든 옷 조회
                                    image = R.drawable.all;
                                    break;
                                case "CS02": //카테고리 top 조회
                                    image = R.drawable.desert;
                                    break;
                                case "CS03": //카테고리 bottom 조회
                                    image = R.drawable.food1;
                                    break;
                                case "CS04": //카테고리 suit 조회
                                    image = R.drawable.sports;
                                    break;
                                case "CS05": //카테고리 outer 조회
                                    image = R.drawable.movie;
                                    break;
                                case "CS06": //카테고리 shoes 조회
                                    image = R.drawable.soju;
                                    break;
                                case "CS07": //카테고리 bag 조회
                                    image = R.drawable.play;
                                    break;
                                case "CS08": //카테고리 bag 조회
                                    image = R.drawable.icon_footer_bus;
                                    break;
                                default:
                                    image = R.drawable.desert;
                                    break;
                            }
                            jimage.add(image);
                            ClothesVO data = new ClothesVO();

                            data.setidx(idx);
                            data.setname(name1+"  ("+star+")");
                            data.setcategory(tags);
                            //data.setstar(star);
                            data.setadress(adress);
                            data.setreview(review);
                            data.setimage(image);
                            clothesList.add(data);
                        }
                        clothesListAdapter = new ClothesListAdapter(getContext(), clothesList, R.layout.fragment_recyclerview);
                        rv_clothes.setAdapter(clothesListAdapter);
                        clothesListAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "가까운 곳 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(ErrMag, e.toString());
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class GetCategoryListTask extends AsyncTask<String, Void, String> {
        public Context context;

        public GetCategoryListTask(Context getContext) {
            context = getContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... obj) {
            Call<JsonObject> objectCall = MapService.getRetrofit(context).getCategoryList(obj[0]);
            try {
                Object result = objectCall.execute().body();
                Gson gson = new Gson();
                JsonObject json = gson.toJsonTree(result).getAsJsonObject();
                return json.toString();

            } catch (IOException e) {
                e.printStackTrace();
                return "fail";
            }
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }
}