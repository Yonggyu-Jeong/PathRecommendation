package com.example.shareonfoot.home.subfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.shareonfoot.closet.TabFragment_Clothes_inCloset;
import com.example.shareonfoot.util.ClothesListAdapter;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.shareonfoot.HTTP.Service.MapService;
import com.example.shareonfoot.R;
import com.example.shareonfoot.VO.ClothesVO;
import com.example.shareonfoot.home.fragment_home;
import com.example.shareonfoot.util.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;

/* 그리드 사이즈 조절 방법 :
어댑터 변경, 그리드 사이즈 변경, 페이지사이즈 변경
(small(4), medium(3), large(2)) 20p, 15p, 10p
*/

public class TabFragment_Clothes_inHome extends Fragment {

    fragment_home parentFragment;
    private static String option;

    String identifier; //프래그먼트의 종류를 알려줌
    String size;
    ArrayList<ClothesVO> clothesList = new ArrayList<ClothesVO>();

    int gridsize;
    String pagesize;
    ClothesListAdapter clothesListAdapter;
    public RelativeLayout Cloth_Info;
    public int choose;
    int page=0;
    RecyclerView rv_clothes;
    ArrayList<String> ImageUrlList = new ArrayList<String>();
    //리사이클러뷰 어댑터


    public static TabFragment_Clothes_inHome newInstance(String identifier, String size) {
        Bundle args = new Bundle();
        args.putString("identifier", identifier);  // 키값, 데이터
        args.putString("size", size);

        TabFragment_Clothes_inHome fragment = new TabFragment_Clothes_inHome();
        fragment.setArguments(args);
        return fragment;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentFragment = ((fragment_home)getParentFragment());

        Bundle args = getArguments(); // 데이터 받기
        if(args != null) {
            identifier = args.getString("identifier");
            size = args.getString("size");
            Log.e("onCreate-identifier", identifier);
            Log.e("onCreate-identifier", identifier);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        rv_clothes = (RecyclerView) view.findViewById(R.id.tab_clothes_rv);
        rv_clothes.setLayoutManager(new LinearLayoutManager(getContext())); //그리드 사이즈 설정
        rv_clothes.setAdapter(clothesListAdapter);
        rv_clothes.setNestedScrollingEnabled(true);

        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                clothesList.clear();
                init();
                clothesListAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    public void init() {
        Log.e("init-identifier", identifier);
        switch (identifier) {
            case "즐겨찾는 장소":
                option = "CH01";
                break;
            case "최근 가본 장소":
                option = "CH02";
                break;
        }
        Log.e("init-size", size);
        switch (size){
            case "small":
                gridsize = 4; //스몰 그리드 4x5
                pagesize="25"; //스몰 페이지 사이즈 25
                break;
            case "medium":
                gridsize = 3; //미디엄 그리드 3x4
                pagesize="17"; //미디엄 페이지 사이즈 17
                break;
            case "large":
                gridsize = 2; //라지 그리드 2x3
                pagesize="7"; //라지 페이지 사이즈 7
                break;
        }
        clothesList.clear();
        getLocateUserList("hello", option);
    }



    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("check",0);
        Log.e("checker-home", sharedPreferences.getString("check", "default"));
        if(!sharedPreferences.getString("check", "default").equals("signup")) {
            init();
        }
    }


    //프래그먼트 갱신
    private void refresh(){
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
    }

    public void getLocateUserList(String name, String option) {
        GetCategoryUserListTask mapTask = new GetCategoryUserListTask(requireContext());
        String result = "";
        Utils utils = new Utils();
        try {
            result = mapTask.execute(name, option).get();
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
                                if(j == 2 ) {
                                    tags += "\n";
                                } else {
                                    tags += "  ";
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
                                    image = R.drawable.foodc;
                                    break;
                                case "CS04": //카테고리 suit 조회
                                    image = R.drawable.hisc;
                                    break;
                                case "CS05": //카테고리 outer 조회
                                    image = R.drawable.naturec;
                                    break;
                                case "CS06": //카테고리 shoes 조회
                                    image = R.drawable.expc;
                                    break;
                                case "CS07": //카테고리 bag 조회
                                    image = R.drawable.themec;
                                    break;
                                case "CS08": //카테고리 bag 조회
                                    image = R.drawable.etcc;
                                    break;
                                default:
                                    image = R.drawable.naturec;
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
                    Log.e("ErrMsg", e.toString());
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public class GetCategoryUserListTask extends AsyncTask<String, Void, String> {
        public Context context;

        public GetCategoryUserListTask(Context getContext) {
            context = getContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... obj) {
            Call<JsonObject> objectCall = MapService.getRetrofit(context).getCategoryUserList(obj[0], obj[1]);
            try {
                Object result = objectCall.execute().body();
                Gson gson = new Gson();
                if(result != null) {
                    JsonObject json = gson.toJsonTree(result).getAsJsonObject();
                    return json.toString();
                }
                return "";

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
