package com.example.shareonfoot.codi;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Retrofit;

import com.example.shareonfoot.HTTP.APIAdapter;
import com.example.shareonfoot.HTTP.Service.MapService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.shareonfoot.util.Utils;

import com.example.shareonfoot.R;
import com.example.shareonfoot.home.activity_home;
import com.example.shareonfoot.util.OnBackPressedListener;
import com.example.shareonfoot.util.WorkTask;
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.Tm128;
import com.naver.maps.geometry.Utmk;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Align;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PolylineOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


import static android.app.Activity.RESULT_OK;

public class fragment_codi extends Fragment implements OnBackPressedListener, OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private MapView mapView;
    private FusedLocationSource locationSource;

    ViewGroup viewGroup;
    Toast toast;
    long backKeyPressedTime;

    int MAKE_CODI = 120;
    int WEATHER_CODI = 191;
    int RECO_CODI = 255;

    Activity activity;

    DrawerLayout drawer;
    public RelativeLayout Cloth_Info;
    public RelativeLayout Cloth_Info_edit;
    public ImageView iv_image;
    public ImageView iv_edit_image;
    public TextView theme;
    public TextView tv_category;
    public TextView tv_detailcategory;
    public TextView tv_season;
    public TextView tv_brand;
    public TextView tv_date;
    public String weekDay;
    public ImageView iv_heart;
    public ImageView iv_modify;
    public ImageView iv_delete;
    public ImageView iv_save;
    public TextView tv_cloNo;
    public TextView tv_cloFavorite;
    public TextView tv_edit_category;
    public TextView tv_edit_season;
    public TextView tv_edit_date;
    public TextView tv_edit_name;
    public TextView tv_edit_detailcategory;
    public TextView tv_edit_brand;
    public TextView weekday;
    public EditText editText;
    public static String ErrMag = "ErrMag";
    public String err;
    //tring[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private FloatingActionMenu fam;
    private FloatingActionButton fabMake, fabRecommend, fabAdd;
    private Boolean checkStart = false;
    private Boolean checkGoal = false;
    private Double start_lng = 0.0;
    private Double start_lat = 0.0;
    private Double goal_lng = 0.0;
    private Double goal_lat = 0.0;
    private String user_id = "hello";
    private String user_password = "1";
    private static List<LatLng> COORDS = null;
    private static final int UPDATE_INTERVAL_MS = 300000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 300000; // 0.5초
    private Marker currentMarker = null;
    private HashMap<String, Marker> Markermap = new HashMap<String, Marker>();


    private static class InfoWindowAdapter extends InfoWindow.ViewAdapter {
        @NonNull
        private final fragment_codi context;
        private View rootView;
        private ImageView icon;
        private TextView text;

        public InfoWindowAdapter(@NonNull fragment_codi fragment_codi) {
            this.context = fragment_codi;
        }

        @NonNull
        @Override
        public View getView(@NonNull InfoWindow infoWindow) {
            if (rootView == null) {
                rootView = View.inflate(context.getContext(), R.layout.view_custom_info_window, null);
                icon = rootView.findViewById(R.id.icon);
                text = rootView.findViewById(R.id.text);
            }

            if (infoWindow.getMarker() != null) {
                icon.setImageResource(R.drawable.ic_place_black_24dp);
                text.setText((String) infoWindow.getMarker().getTag());
            } else {
                icon.setImageResource(R.drawable.ic_my_location_black_24dp);
                text.setText(context.getString(
                        R.string.format_coord, infoWindow.getPosition().latitude, infoWindow.getPosition().longitude));
            }

            return rootView;
        }
    }

    public fragment_codi() {
    }

    public static fragment_codi newInstance() {

        Bundle args = new Bundle();

        fragment_codi fragment = new fragment_codi();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.frag_codi, container, false);
        toast = Toast.makeText(getContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT);
        mapView = (MapView) viewGroup.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);


        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return viewGroup;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            activity = (Activity) context;
            ((activity_home) activity).setOnBackPressedListener(this);
        }

    }


    @Override
    public void onStart() {
        super.onStart();

        drawer = getView().findViewById(R.id.final_drawer_layout);

        Cloth_Info = (RelativeLayout) getView().findViewById(R.id.cloth_info);
        Cloth_Info.setVisibility(View.GONE);
        Cloth_Info_edit = (RelativeLayout) getView().findViewById(R.id.cloth_info_edit);
        Cloth_Info_edit.setVisibility(View.GONE);

        iv_image = (ImageView) getView().findViewById(R.id.iv_codi_image);
        iv_edit_image = (ImageView) getView().findViewById(R.id.iv_edit_image);
        tv_category = (TextView) getView().findViewById(R.id.tv_info_catergory);
        tv_detailcategory = (TextView) getView().findViewById(R.id.tv_info_detailcategory);
        tv_season = (TextView) getView().findViewById(R.id.tv_info_season);
        tv_brand = (TextView) getView().findViewById(R.id.tv_info_brand);
        tv_date = (TextView) getView().findViewById(R.id.tv_info_date);

        iv_heart = (ImageView) getView().findViewById(R.id.iv_heart);
        iv_modify = (ImageView) getView().findViewById(R.id.iv_modify);
        iv_delete = (ImageView) getView().findViewById(R.id.iv_delete);
        iv_save = (ImageView) getView().findViewById(R.id.iv_save);

        tv_cloNo = (TextView) getView().findViewById(R.id.tv_clothes_no);
        tv_cloFavorite = (TextView) getView().findViewById(R.id.tv_clothes_favorite);
        tv_edit_name = (TextView) getView().findViewById(R.id.tv_info_color);
        tv_edit_detailcategory = (TextView) getView().findViewById(R.id.tv_edit_detailcategory);
        tv_edit_brand = (TextView) getView().findViewById(R.id.tv_edit_brand);
        weekday = (TextView) getView().findViewById(R.id.tabLayout);

        tv_edit_category = (TextView) getView().findViewById(R.id.tv_edit_catergory);
        tv_edit_season = (TextView) getView().findViewById(R.id.tv_edit_season);
        tv_edit_date = (TextView) getView().findViewById(R.id.tv_edit_date);
        theme = (TextView) getView().findViewById(R.id.day_theme);

        editText = (EditText) getView().findViewById(R.id.editText);

        final String[] Category = {""};

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());

        weekday.setText(weekDay);
        switch (day_return(weekDay)) {
            case 1:
                theme.setText("오늘은 카페랑 디저트!!");

                break;
            case 2:
                theme.setText("오늘은 맛집이야!!");

                break;
            case 3:
                theme.setText("오늘은 운동하러 가자!!");

                break;
            case 4:
                theme.setText("오늘은 마음의 양식!!");

                break;
            case 5:
                theme.setText("오늘은 불금!!");

                break;
            case 6:
                theme.setText("오늘은 놀러 가자!!");

                break;
            case 7:
                theme.setText("오늘은 전부 다!!");

                break;
        }


        //플로팅 액션 버튼 설정

        fabAdd = (FloatingActionButton) getView().findViewById(R.id.fab_add_photo);
        fabMake = (FloatingActionButton) getView().findViewById(R.id.fab_make_codi);
        fabRecommend = (FloatingActionButton) getView().findViewById(R.id.fab_recommend_codi);
        fam = (FloatingActionMenu) getView().findViewById(R.id.fab_menu);

        //handling menu status (open or close)
        fam.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    //Toast.makeText(getContext(), "Menu is opened", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getContext(), "Menu is closed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        BtnOnClickListener onClickListener = new BtnOnClickListener();

        fam.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fam.isOpened()) {
                    fam.close(true);
                } else {
                    fam.open(true);
                }
            }
        });

        fam.setIconAnimationOpenInterpolator(new CycleInterpolator(-0.5f));
        fam.setIconAnimationCloseInterpolator(new CycleInterpolator(-0.75f));
        fam.setClosedOnTouchOutside(true);
        fam.getMenuIconView().setColorFilter(Color.parseColor("#000000"));

    }


    public int day_return(String day) {
        if (day.equals("월요일")) return 1;
        else if (day.equals("화요일")) return 2;
        else if (day.equals("수요일")) return 3;
        else if (day.equals("목요일")) return 4;
        else if (day.equals("금요일")) return 5;
        else if (day.equals("토요일")) return 6;
        else return 7;
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            tv_edit_date.setText(year + "년" + monthOfYear + "월" + dayOfMonth + "일");
            Toast.makeText(getContext(), year + "년" + monthOfYear + "월" + dayOfMonth + "일", Toast.LENGTH_SHORT).show();
        }
    };

    //뒤로 가기 버튼이 눌렸을 경우 드로워(메뉴)를 닫는다.
    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fam.isOpened()) {
            fam.close(true);
        } else if (Cloth_Info_edit.getVisibility() == View.VISIBLE) {
            Cloth_Info_edit.setVisibility(View.GONE);
        } else if (Cloth_Info.getVisibility() == View.VISIBLE) {
            Cloth_Info.setVisibility(View.GONE);
        } else if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast.show();
            return;
        } else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }

    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMap.setLocationSource(locationSource);

        Double lat = 37.361927530;
        //locationSource.getLastLocation().getLatitude();
        Double lng = 126.738518735;
                //locationSource.getLastLocation().getLongitude();

        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        InfoWindow infoWindow = new InfoWindow();

        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(lat, lng));
        naverMap.moveCamera(cameraUpdate);
        getTestLocate(naverMap, infoWindow, lat.toString(), lng.toString());

        fabAdd.setOnClickListener(view -> {
            try {
                getLocate(naverMap, infoWindow, user_id, user_password, start_lng.toString(), start_lat.toString(), goal_lng.toString(), goal_lat.toString());
            } catch (Exception e) {
                Toast.makeText(getContext(), "거리가 너무 짧습니다", Toast.LENGTH_SHORT);
            }
            fam.close(true);
        });

        // 출발지
        fabMake.setOnClickListener(view -> {
            if(editText.getText() != null) {
                getLocateNameforStart(editText.getText().toString(), naverMap, infoWindow);
                checkStart = true;
                if(checkStart && checkGoal) {
                    setCircle(naverMap, start_lng, goal_lng, start_lat, goal_lat);
                    try {
                        getLocate(naverMap, infoWindow, user_id, user_password, start_lng.toString(), start_lat.toString(), goal_lng.toString(), goal_lat.toString());
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "거리가 너무 짧습니다", Toast.LENGTH_SHORT);
                    }                }
                fam.close(true);
            }
        });

        //목적지
        fabRecommend.setOnClickListener(view -> {
            if(editText.getText() != null) {
                getLocateNameforGoal(editText.getText().toString(), naverMap, infoWindow);
                checkGoal = true;
                if(checkStart && checkGoal) {
                    if(checkStart && checkGoal) {
                        setCircle(naverMap, start_lng, goal_lng, start_lat, goal_lat);
                        try {
                            //getLocate(naverMap, infoWindow, user_id, user_password, start_lng.toString(), start_lat.toString(), goal_lng.toString(), goal_lat.toString());
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "거리가 너무 짧습니다", Toast.LENGTH_SHORT);
                        }
                    }
                }
                fam.close(true);
            }
        });
    }

    //클릭 리스너
    class BtnOnClickListener implements Button.OnClickListener {
        String res="";

        @Override
        public void onClick(View view) {
            Intent intent;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MAKE_CODI && resultCode == RESULT_OK)
            ((activity_home)activity).refresh_codi(fragment_codi.this);
        else if(requestCode == WEATHER_CODI && resultCode == RESULT_OK)
            ((activity_home)activity).refresh_codi(fragment_codi.this);
        else if(requestCode == RECO_CODI && resultCode == RESULT_OK)
            ((activity_home)activity).refresh_codi(fragment_codi.this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }

    String getCurrentAddress(LatLng latlng) {
        // 위치 정보와 지역으로부터 주소 문자열을 구한다.
        List<Address> addressList = null ;
        Geocoder geocoder = new Geocoder( getActivity(), Locale.getDefault());

        // 지오코더를 이용하여 주소 리스트를 구한다.
        try {
            addressList = geocoder.getFromLocation(latlng.latitude,latlng.longitude,1);
        } catch (IOException e) {
            Toast. makeText( getActivity(), "위치로부터 주소를 인식할 수 없습니다. 네트워크가 연결되어 있는지 확인해 주세요.", Toast.LENGTH_SHORT ).show();
            e.printStackTrace();
            return "주소 인식 불가" ;
        }

        if (addressList.size() < 1) { // 주소 리스트가 비어있는지 비어 있으면
            return "해당 위치에 주소 없음" ;
        }

        // 주소를 담는 문자열을 생성하고 리턴
        Address address = addressList.get(0);
        StringBuilder addressStringBuilder = new StringBuilder();
        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            addressStringBuilder.append(address.getAddressLine(i));
            if (i < address.getMaxAddressLineIndex())
                addressStringBuilder.append("\n");
        }

        return addressStringBuilder.toString();
    }

    /*
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                Location location = locationList.get(locationList.size() - 1);

                LatLng currentPosition
                        = new LatLng(location.getLatitude(), location.getLongitude());

                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                        + " 경도:" + String.valueOf(location.getLongitude());

                Log.d("TAG", "Time :" + CurrentTime() + " onLocationResult : " + markerSnippet);

                //현재 위치에 마커 생성하고 이동
                setCurrentLocation(location, markerTitle, markerSnippet);
                mCurrentLocatiion = location;

            }
        }

    };
*/
    private String CurrentTime(){
        Date today = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss a");
        return time.format(today);
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    public void getLocateNameforStart(String name, NaverMap naverMap, InfoWindow infoWindow) {
        WorkTask.GetLocateNameTask mapTask = new WorkTask.GetLocateNameTask(requireContext());
        HashMap hashMap = new HashMap();
        try {
            hashMap = mapTask.execute(name).get();

            Marker markerStart = new Marker();
            Tm128 tm128 = new Tm128((Double)hashMap.get("mapx"), (Double)hashMap.get("mapy"));
            markerStart.setPosition(tm128.toLatLng());
            markerStart.setOnClickListener(overlay -> {
                infoWindow.open(markerStart);
                return true;
            });
            start_lat = tm128.toLatLng().latitude;
            start_lng = tm128.toLatLng().longitude;


            if(checkStart == true) {
                Marker tempMarker = (Marker) Markermap.get("markerStart");
                tempMarker.setMap(null);
            }
            markerStart.setTag("출발지");
            markerStart.setIcon(MarkerIcons.RED);
            markerStart.setCaptionTextSize(14);
            markerStart.setCaptionText(getString(R.string.marker_sub_caption_start));
            markerStart.setCaptionMinZoom(12);
            markerStart.setSubCaptionTextSize(10);
            markerStart.setSubCaptionColor(Color.GRAY);
            markerStart.setSubCaptionText(getString(R.string.marker_sub_caption_start));
            markerStart.setSubCaptionMinZoom(13);
            Markermap.put("markerStart", markerStart);
            markerStart.setMap(naverMap);

            editText.setText("");
            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(start_lat, start_lng));
            naverMap.moveCamera(cameraUpdate);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getLocateNameforGoal(String name, NaverMap naverMap, InfoWindow infoWindow) {
        WorkTask.GetLocateNameTask mapTask = new WorkTask.GetLocateNameTask(requireContext());
        HashMap hashMap = new HashMap();

        try {
            hashMap = mapTask.execute(name).get();

            Marker markerGoal = new Marker();
            Tm128 tm128 = new Tm128((Double)hashMap.get("mapx"), (Double)hashMap.get("mapy"));
            markerGoal.setPosition(tm128.toLatLng());

            markerGoal.setOnClickListener(overlay -> {
                infoWindow.open(markerGoal);
                return true;
            });
            goal_lat = tm128.toLatLng().latitude;
            goal_lng = tm128.toLatLng().longitude;

            if(checkGoal == true) {
                Marker tempMarker = (Marker) Markermap.get("markerGoal");
                tempMarker.setMap(null);
            }

            markerGoal.setTag("도착지");
            markerGoal.setIcon(MarkerIcons.LIGHTBLUE);
            markerGoal.setCaptionTextSize(14);
            markerGoal.setCaptionText(getString(R.string.marker_sub_caption_goal));
            markerGoal.setCaptionMinZoom(12);
            markerGoal.setSubCaptionTextSize(10);
            markerGoal.setSubCaptionColor(Color.GRAY);
            markerGoal.setSubCaptionText(getString(R.string.marker_sub_caption_goal));
            markerGoal.setSubCaptionMinZoom(13);
            Markermap.put("markerGoal", markerGoal);
            markerGoal.setMap(naverMap);

            editText.setText("");
            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(goal_lat, goal_lng));
            naverMap.moveCamera(cameraUpdate);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getTestLocate(NaverMap naverMap, InfoWindow infoWindow, String... obj) {
        WorkTask.GetLocateForReadyTask mapTask = new WorkTask.GetLocateForReadyTask(requireContext());
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        try {
            HashMap<String, Object> executetMap = mapTask.execute(obj).get();
            if(executetMap.get("check").toString().equals("ok")) {
                setCaptionToMap(naverMap, infoWindow, executetMap);
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getLocate(NaverMap naverMap, InfoWindow infoWindow, String... obj) {
        WorkTask.GetLocateTask mapTask = new WorkTask.GetLocateTask(requireContext());
        HashMap<String, Object> resultMap = null;
        try {
            resultMap = mapTask.execute(obj).get();
            if(resultMap.get("check").toString().equals("ok")) {
                for(int i=0; i<3; i++) {
                    ArrayList<Object> dataList = null;
                    int dataSize = 0;
                    dataList = (ArrayList<Object>)resultMap.get("resultDataList"+i);
                    dataSize = Integer.parseInt(String.valueOf(Math.round((Double) resultMap.get("DataSize"+i))));
                    Log.i("dataList"+i, dataList.toString());
                    Log.i("dataSize"+i, String.valueOf(dataSize));
                    setCaptionToMap(naverMap, infoWindow, dataList, dataSize);
                }

            } else {
                Toast.makeText(getContext(), "추천 목적지의 수가 적습니다. 다른 장소를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setCircle(NaverMap naverMap, Double lng_start, Double lng_goal, Double lat_start, Double lat_goal) {

        COORDS = Arrays.asList( new LatLng(start_lat, start_lng), new LatLng(goal_lat, goal_lng));
        HashMap hashMap = getRangeCircle(start_lng, goal_lng, start_lat, goal_lat);
        HashMap temMap = new HashMap();
        PolylineOverlay polylineOverlay1 = new PolylineOverlay();
        polylineOverlay1.setWidth(3);
        polylineOverlay1.setCoords(COORDS);
        polylineOverlay1.setMap(naverMap);

        temMap = (HashMap) hashMap.get("first");
        CircleOverlay circleOverlay1 = new CircleOverlay();
        circleOverlay1.setCenter(new LatLng((Double)temMap.get("lat"), (Double)temMap.get("lng")));
        circleOverlay1.setRadius(500);
        circleOverlay1.setColor(ColorUtils.setAlphaComponent(Color.YELLOW, 31));
        circleOverlay1.setOutlineColor(Color.YELLOW);
        circleOverlay1.setOutlineWidth(getResources().getDimensionPixelSize(R.dimen.overlay_line_width));
        circleOverlay1.setMap(naverMap);

        temMap = (HashMap) hashMap.get("second");
        CircleOverlay circleOverlay2 = new CircleOverlay();
        circleOverlay2.setCenter(new LatLng((Double)temMap.get("lat"), (Double)temMap.get("lng")));
        circleOverlay2.setRadius(500);
        circleOverlay2.setColor(ColorUtils.setAlphaComponent(Color.GREEN, 31));
        circleOverlay2.setOutlineColor(Color.GREEN);
        circleOverlay2.setOutlineWidth(getResources().getDimensionPixelSize(R.dimen.overlay_line_width));
        circleOverlay2.setMap(naverMap);

        temMap = (HashMap) hashMap.get("third");
        CircleOverlay circleOverlay3 = new CircleOverlay();
        circleOverlay3.setCenter(new LatLng((Double)temMap.get("lat"), (Double)temMap.get("lng")));
        circleOverlay3.setRadius(500);
        circleOverlay3.setColor(ColorUtils.setAlphaComponent(Color.BLUE, 31));
        circleOverlay3.setOutlineColor(Color.BLUE);
        circleOverlay3.setOutlineWidth(getResources().getDimensionPixelSize(R.dimen.overlay_line_width));
        circleOverlay3.setMap(naverMap);
    }

    public HashMap getRangeCircle(Double lng_start, Double lng_goal, Double lat_start, Double lat_goal) {
        HashMap hashMap = new HashMap();
        HashMap firstMap = new HashMap();
        HashMap secondMap = new HashMap();
        HashMap thirdMap = new HashMap();

        double distance = 0.0;
        double lng = lng_start - lng_goal;
        double lat = lat_start - lat_goal;
        double lng_standard2 = 0;
        double lat_standard2 = 0;

        distance = Math.sqrt((lng * lng) + (lat * lat));
        distance = distance / 3;

        firstMap.put("lat", lat_start - (lat/3)*0.6);
        firstMap.put("lng", lng_start - (lng/3)*0.6);

        secondMap.put("lat", lat_start - (lat/3)*1.7);
        secondMap.put("lng", lng_start - (lng/3)*1.7);

        thirdMap.put("lat", lat_start - (lat/3)*3);
        thirdMap.put("lng", lng_start - (lng/3)*3);

        hashMap.put("first", firstMap);
        hashMap.put("second", secondMap);
        hashMap.put("third", thirdMap);

        return hashMap;
    }

    public void setCaptionToMap(NaverMap naverMap, InfoWindow infoWindow, HashMap<String, Object> map) {
        Utils utils = new Utils();

        for(int i = 0; i<Integer.parseInt(String.valueOf(Math.round((Double) map.get("result-size")))); i++) {
            LinkedTreeMap<String,Object> paramMap = (LinkedTreeMap<String, Object>) map.get("result_" + (i + 1));

            Marker marker = new Marker();
            LatLng xy = new LatLng((Double) paramMap.get("lat"), (Double) paramMap.get("lng"));
            marker.setPosition(xy);

            String category = "";
            category = utils.getCategory(paramMap.get("category").toString())+"\n";
            for(int j=1; j<5; j++) {
                category = category+"#"+utils.getTagCategory(paramMap.get("tag"+j).toString());
                if(j<4) {
                    category += "\n";
                }
            }

            infoWindow.setAnchor(new PointF(0, 1));
            infoWindow.setOffsetX(getResources().getDimensionPixelSize(R.dimen.custom_info_window_offset_x));
            infoWindow.setOffsetY(getResources().getDimensionPixelSize(R.dimen.custom_info_window_offset_y));
            infoWindow.setAdapter(new InfoWindowAdapter(this));
            marker.setOnClickListener(overlay -> {
                infoWindow.open(marker);
                return true;
            });

            marker.setTag(category);
            marker.setIcon(MarkerIcons.PINK);
            marker.setCaptionTextSize(14);
            marker.setCaptionText(paramMap.get("name").toString());
            marker.setCaptionMinZoom(12);
            marker.setSubCaptionTextSize(10);
            marker.setSubCaptionColor(Color.GRAY);
            marker.setSubCaptionText("추천 장소");
            marker.setSubCaptionMinZoom(13);
            Markermap.put("marker-"+i, marker);
            marker.setMap(naverMap);
            infoWindow.open(marker);

            naverMap.setOnMapClickListener((point, coord) -> {
                infoWindow.setPosition(coord);
                infoWindow.open(naverMap);
            });
        }
    }

    public void setCaptionToMap(NaverMap naverMap, InfoWindow infoWindow, ArrayList<Object> dataList, int dataSize) {
        Utils utils = new Utils();
        for(int i=0; i<dataSize; i++) {
            LinkedTreeMap<String,Object> paramMap = (LinkedTreeMap<String, Object>) dataList.get(i);
            Log.i("paramMap"+i, paramMap.toString());

            Marker marker = new Marker();
            LatLng xy = new LatLng((Double) paramMap.get("lat"), (Double) paramMap.get("lng"));
            marker.setPosition(xy);

            String category = "";
            category = utils.getCategory(paramMap.get("category").toString())+"\n";
            for(int j=1; j<5; j++) {
                category = category+"#"+utils.getTagCategory(paramMap.get("tag"+j).toString());
                if(j<4) {
                    category += "\n";
                }
            }

            infoWindow.setAnchor(new PointF(0, 1));
            infoWindow.setOffsetX(getResources().getDimensionPixelSize(R.dimen.custom_info_window_offset_x));
            infoWindow.setOffsetY(getResources().getDimensionPixelSize(R.dimen.custom_info_window_offset_y));
            infoWindow.setAdapter(new InfoWindowAdapter(this));
            marker.setOnClickListener(overlay -> {
                infoWindow.open(marker);
                return true;
            });

            marker.setTag(category);

            if(i==0) {
                marker.setIcon(MarkerIcons.YELLOW);
            } else if(i==1) {
                marker.setIcon(MarkerIcons.GREEN);
            } else {
                marker.setIcon(MarkerIcons.BLUE);
            }

            marker.setCaptionTextSize(14);
            marker.setCaptionText(paramMap.get("name").toString());
            marker.setCaptionMinZoom(12);
            marker.setSubCaptionTextSize(10);
            marker.setSubCaptionColor(Color.GRAY);
            marker.setSubCaptionText("추천 장소");
            marker.setSubCaptionMinZoom(13);
            Markermap.put("marker-"+i, marker);
            marker.setMap(naverMap);
            infoWindow.open(marker);

            naverMap.setOnMapClickListener((point, coord) -> {
                infoWindow.setPosition(coord);
                infoWindow.open(naverMap);
            });

        }
    }
}


    /*
        class NearestTask extends AsyncTask<String, String, String> {
        String sendMsg, receiveMsg;
        StringBuffer Buffer = new StringBuffer();
        URL url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String[] strings) {
            CookieHandler.setDefault( new CookieManager( null, CookiePolicy.ACCEPT_ALL ) );
            String get_json = "",tmp;
            URL url;

            try {
                url = new URL("http://49.50.172.215:8080/shareonfoot/data.jsp");
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    err = ioException.toString();
                    Log.i(ErrMag, "1");
                }
                conn.setRequestMethod("POST"); // URL 요청에 대한 메소드 설정 : POST.
                conn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                conn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

                // 서버에서 읽어오기 위한 스트림 객체
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "lat=" + strings[0]+ "&lng=" + strings[1] + "&day=" + strings[2];
                Log.i("day",strings[2]);
                wr.write(sendMsg);
                wr.flush();
                wr.close();

                if (conn.getResponseCode() == conn.HTTP_OK) {

                    // 서버에서 읽어오기 위한 스트림 객체
                    InputStreamReader isr = new InputStreamReader(
                            conn.getInputStream());
                    // 줄단위로 읽어오기 위해 BufferReader로 감싼다.
                    BufferedReader br = new BufferedReader(isr);
                    // 반복문 돌면서읽어오기
                    while (true) {
                        String line = br.readLine();
                        if (line == null) {

                            break;
                        }
                        Buffer.append(line);
                    }
                    br.close();
                    conn.disconnect();
                }
                get_json = Buffer.toString();
                Log.i("msg", "get_json: " + get_json);

            } catch (MalformedURLException e) {
                e.printStackTrace();
                err = e.toString();
                Log.i(ErrMag, "5");
            } catch (IOException e) {
                e.printStackTrace();
                err = e.toString();
                Log.i(ErrMag, err);
            }
            return get_json;
        }


        public void onPostExecute(String result) {
            super.onPostExecute(result);
            List<Location> list = new ArrayList<>();
            int i=0;
            Log.d("onPostExecute:  ", " <<<<<onPostExecute>>>> ");
            try {
                JSONArray jarray = new JSONObject(result).getJSONArray("store_data");
                if(jarray!=null){
                    while (jarray != null) {
                        JSONObject jsonObject = jarray.getJSONObject(i);
                        String name = jsonObject.getString("store_name");
                        double lng =  Double.parseDouble(jsonObject.getString("store_lng"));
                        double lat = Double.parseDouble(jsonObject.getString("store_lat"));
                        double dst = Double.parseDouble(jsonObject.getString("store_dst"));
                        //Toast.makeText(getContext(), String.valueOf(dst), Toast.LENGTH_SHORT).show();
                        LatLng position=new LatLng(lat,lng);
                        /*
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(position)
                                .title(name)
                                .snippet(String.format("나와의 거리 %.2fkm", dst));
                        switch (i){
                            case 0:
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.m1));
                                break;
                            case 1:
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.m2));
                                break;
                            case 2:
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.m3));
                                break;
                            case 3:
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.m4));
                                break;
                            case 4:
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.m5));
                                break;
                            default:
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2));
                                break;
                        }
                        mMap.addMarker(markerOptions);
                        polylineOptions = new PolylineOptions();
                        polylineOptions.color(Color.rgb(135,206,235))
                                .width(10).
                                jointType(JointType.ROUND);;
                        // 맵셋팅
                        arrayPoints.add(position);
                        polylineOptions.addAll(arrayPoints);

                        mMap.addPolyline(polylineOptions);
                        // null을 가끔 못 읽어오는 때가 있다고 하기에 써봄
                        //String Start = jsonObject.optString("START_TIME", "text on no value");
                        //String Stop = jsonObject.optString("STOP_TIME", "text on no value");
                        //String REG = jsonObject.optString("REG_TIME", "text on no value");
                        Log.i("qw", name + "/" + lng+ "/" + lat);

                        i++;


}
                } else {
                        Toast.makeText(getContext(), "가까운 곳 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        } catch (Exception e) {
                        Log.e(ErrMag, "7");
                        }
                        }
                        }
     */
