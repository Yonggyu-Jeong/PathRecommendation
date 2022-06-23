package com.example.shareonfoot.closet;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.shareonfoot.HTTP.Service.CategoryService;
import com.example.shareonfoot.HTTP.Service.MapService;
import com.example.shareonfoot.R;
import com.example.shareonfoot.VO.ClothesVO;
import com.example.shareonfoot.util.ClothesListAdapter;
import com.example.shareonfoot.util.WorkTask;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;

/* 그리드 사이즈 조절 방법 :
어댑터 변경, 그리드 사이즈 변경, 페이지사이즈 변경ㅆ
(small(4), medium(3), large(2)) 20p, 15p, 10p
*/

public class   TabFragment_Clothes_inCloset extends Fragment {

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
    public static String ErrMag = "ErrMag";
    public String err;

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
        clothesListAdapter = new ClothesListAdapter(getContext(), clothesList, R.layout.fragment_recyclerview);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String coordinates[] = {page.toString(), ""};
        //Log.i("qe",String.valueOf(page));
        //현재 페이지수와 함께 웹서버에 옷 데이터 요청
        String json = getLocateList("CS01");
        //리사이클러 뷰 설정하기
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        rv_clothes = (RecyclerView) view.findViewById(R.id.tab_clothes_rv);
        rv_clothes.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_clothes.setAdapter(clothesListAdapter);
        rv_clothes.setNestedScrollingEnabled(true);
        rv_clothes.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (!rv_clothes.canScrollVertically(-1)) {
                    //스크롤이 최상단이면 데이터를 갱신한다
                    /*clothesList.clear();
                    page=0;
                    new networkTask().execute(Integer.toString(page));
                    clothesListAdapter.notifyDataSetChanged();
                    rv_clothes.setAdapter(clothesListAdapter);
                    //Log.e("test","데이터 갱신");*/
                } else if (!rv_clothes.canScrollVertically(1)) {
                    String coordinates[] = {(++page).toString()};
                    String json = getLocateList("CS01");
                    clothesListAdapter.notifyDataSetChanged();
                    rv_clothes.setAdapter(clothesListAdapter);

                    Log.e("test", "페이지 수 증가");
                } else {
                }
            }
        });

        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //스크롤이 최상단이면 데이터를 갱신한다
                clothesList.clear();
                page = 0;
                String coordinates[] = {page.toString()};
                String json = getLocateList("CS01");
                clothesListAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //프래그먼트 갱신
    private void refresh() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
    }


    public String getLocateList(String name) {
        GetCategoryListTask mapTask = new GetCategoryListTask(requireContext());
        String result = null;
        try {
            result = mapTask.execute(name).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
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
                        String star = "2.5";
                        String review = jsonObject.getString("info");
                        String adress = jsonObject.getString("area");
                        String name = jsonObject.getString("name");
                        String category = jsonObject.getString("category");
                        jidx.add(idx);
                        jname.add(name);
                        jname.add(category);
                        jname.add(star);
                        jname.add(adress);
                        jname.add(review);

                        switch (pos) {
                            case 0: //모든 옷 조회
                                image = R.drawable.all;
                                break;
                            case 1: //카테고리 top 조회
                                image = R.drawable.desert;
                                break;
                            case 2: //카테고리 bottom 조회
                                image = R.drawable.food1;
                                break;
                            case 3: //카테고리 suit 조회
                                image = R.drawable.sports;
                                break;
                            case 4: //카테고리 outer 조회
                                image = R.drawable.movie;
                                break;
                            case 5: //카테고리 shoes 조회
                                image = R.drawable.soju;
                                break;
                            case 6: //카테고리 bag 조회
                                image = R.drawable.play;
                                break;
                            default:
                                image = R.drawable.desert;
                                break;
                        }
                        jimage.add(image);
                        ClothesVO data = new ClothesVO();

                        data.setidx(idx);
                        data.setname(name);
                        data.setcategory(category);
                        data.setstar(star);
                        data.setadress(adress);
                        data.setreview(review);
                        data.setimage(image);
                        clothesListAdapter.addItem(data);
                        clothesListAdapter.notifyDataSetChanged();
                    }
                    //clothesList.clear();
                } else {
                    Toast.makeText(getContext(), "가까운 곳 없습니다.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e(ErrMag, e.toString());
            }
        }
    }


}
