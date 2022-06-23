package com.example.shareonfoot.codi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.shareonfoot.util.Utils;

import com.example.shareonfoot.R;
import com.example.shareonfoot.home.activity_home;
import com.example.shareonfoot.util.OnBackPressedListener;
import com.example.shareonfoot.util.WorkTask;
import com.example.shareonfoot.util.infoWindowAdapter;
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.internal.LinkedTreeMap;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.Tm128;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.overlay.PolylineOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;


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
    private Marker currentMarker = null;
    private HashMap<String, Marker> markerMap = new HashMap<String, Marker>();
    private HashMap<String, LatLng> markerLatLngMap = new HashMap<String, LatLng>();
    private HashMap<String, String> markerNameMap = new HashMap<String, String>();
    private HashMap<String, Marker> stopMarkerMap = new HashMap<String, Marker>();
    private HashMap<String, LatLng> stopMarkerLatLngMap = new HashMap<String, LatLng>();
    private HashMap<String, String> stopMarkerNameMap = new HashMap<String, String>();
    private HashMap<String, PathOverlay> pathOverlayMap = new HashMap<String, PathOverlay>();

    private Marker targetMarker = null;
    private LatLng targetLatLng = null;
    private String targetName = "";
    private int pathOverlayMapCount = 0;
    private int stopMarkerCount = 0;

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

        Double lat = 37.339496586083;
        //locationSource.getLastLocation().getLatitude();
        Double lng = 126.73287520461;
                //locationSource.getLastLocation().getLongitude();

        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        InfoWindow infoWindow = new InfoWindow();

        AtomicReference<CameraUpdate> cameraUpdate = new AtomicReference<>(CameraUpdate.scrollTo(new LatLng(lat, lng)));
        naverMap.moveCamera(cameraUpdate.get());
        //getTestLocate(naverMap, infoWindow, lat.toString(), lng.toString());

        fabAdd.setOnClickListener(view -> {
            try {
                HashMap<String, Object> responseMap = new HashMap();
                String waypoint = "";
                String option = "";
                responseMap.put("start", markerLatLngMap.get("markerStart").longitude+","+markerLatLngMap.get("markerStart").latitude+",name="+markerNameMap.get("markerStart"));
                responseMap.put("goal", markerLatLngMap.get("markerGoal").longitude+","+markerLatLngMap.get("markerGoal").latitude+",name="+markerNameMap.get("markerGoal"));
                if(stopMarkerCount > 0) {
                    for(int i=0; i<stopMarkerCount+1; i++) {
                        waypoint += stopMarkerLatLngMap.get("marker"+i).longitude+","+stopMarkerLatLngMap.get("marker"+i).latitude+",name="+stopMarkerNameMap.get("marker"+i)+":";
                    }
                    responseMap.put("waypoints", waypoint.substring(0, waypoint.length()-1));
                }
                //responseMap.put("option", option); // 차후 지원할 기능
                WorkTask.GetPathLocateTask pathLocateTask = new WorkTask.GetPathLocateTask(requireContext());
                try {
                    ArrayList<LatLng> latLngArrayList = pathLocateTask.execute(responseMap).get();
                    /*
                    for(int i=0; i<pathOverlayMapCount; i++) {
                        PathOverlay path = pathOverlayMap.get(i);
                        path.setMap(null);
                    }
                    pathOverlayMapCount = 0;
                     */
                    Log.e("latLngArrayList", latLngArrayList.toString());
                    PathOverlay path = new PathOverlay();
                    path.setCoords(latLngArrayList);
                    path.setWidth(30);
                    path.setColor(Color.BLUE);

                    path.setOnClickListener(overlay -> {
                        Toast.makeText(requireContext(), "출력됨", Toast.LENGTH_SHORT).show();
                        return true;
                    });

                    pathOverlayMap.put("path"+pathOverlayMapCount++, path);
                    path.setMap(naverMap);
                    cameraUpdate.set(CameraUpdate.scrollTo(new LatLng(start_lat, start_lng)));
                    naverMap.moveCamera(cameraUpdate.get());

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
                    //setCircle(naverMap, start_lng, goal_lng, start_lat, goal_lat);
                    try {
                        getLocate(naverMap, infoWindow, user_id, user_password, start_lng.toString(), start_lat.toString(), goal_lng.toString(), goal_lat.toString());

                    } catch (Exception e) {
                        Toast.makeText(getContext(), "거리가 너무 짧습니다", Toast.LENGTH_SHORT);
                    }
                }
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
                            getLocate(naverMap, infoWindow, user_id, user_password, start_lng.toString(), start_lat.toString(), goal_lng.toString(), goal_lat.toString());
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
                Marker tempMarker = (Marker) markerMap.get("markerStart");
                tempMarker.setMap(null);
            }
            markerStart.setTag(hashMap.get("title").toString());
            markerStart.setIcon(MarkerIcons.LIGHTBLUE);
            markerStart.setCaptionTextSize(14);
            markerStart.setCaptionText(getString(R.string.marker_sub_caption_start));
            markerStart.setCaptionMinZoom(12);
            markerStart.setSubCaptionTextSize(10);
            markerStart.setSubCaptionColor(Color.GRAY);
            //markerStart.setSubCaptionText(getString(R.string.marker_sub_caption_start));
            markerStart.setSubCaptionMinZoom(13);
            markerMap.put("markerStart", markerStart);
            markerLatLngMap.put("markerStart", tm128.toLatLng());
            markerNameMap.put("markerStart", hashMap.get("title").toString());
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
                Marker tempMarker = (Marker) markerMap.get("markerGoal");
                tempMarker.setMap(null);
            }

            markerGoal.setTag(hashMap.get("title").toString());
            markerGoal.setIcon(MarkerIcons.LIGHTBLUE);
            markerGoal.setCaptionTextSize(14);
            markerGoal.setCaptionText(getString(R.string.marker_sub_caption_goal));
            markerGoal.setCaptionMinZoom(12);
            markerGoal.setSubCaptionTextSize(10);
            markerGoal.setSubCaptionColor(Color.GRAY);
            //markerGoal.setSubCaptionText(getString(R.string.marker_sub_caption_goal));
            markerGoal.setSubCaptionMinZoom(13);
            markerMap.put("markerGoal", markerGoal);
            markerLatLngMap.put("markerGoal", tm128.toLatLng());
            markerNameMap.put("markerGoal", hashMap.get("title").toString());
            markerGoal.setMap(naverMap);

            editText.setText("");
            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(start_lat, start_lng));
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
        Utils utils = new Utils();
        try {
            resultMap = mapTask.execute(obj).get();
            if(resultMap.get("check").toString().equals("ok")) {
                HashMap map = new HashMap();
                map.put("start_lng", obj[2]);
                map.put("start_lat", obj[3]);
                map.put("goal_lng", obj[4]);
                map.put("goal_lat", obj[5]);

                for (int i = 0; i < 3; i++) {
                    ArrayList<Object> dataList = null;
                    ArrayList<Object> recommendDataList = null;
                    int dataSize = 0;
                    dataSize = Integer.parseInt(String.valueOf(Math.round((Double) resultMap.get("DataSize" + i))));
                    if (dataSize < 1) {
                        continue;
                    }
                    dataList = (ArrayList<Object>) resultMap.get("resultDataList" + i);
                    setCaptionToMap(naverMap, infoWindow, dataList, dataSize);
                }

                ArrayList<Object> recommendList = utils.setRecommendPath(map, (ArrayList<Object>) resultMap.get("recommendDataList0"), (ArrayList<Object>) resultMap.get("recommendDataList1"), (ArrayList<Object>) resultMap.get("recommendDataList2"));
                try {
                    for (int j = 0; j < recommendList.size(); j++) {
                        WorkTask.GetPathNaverTask pathNaverTask = new WorkTask.GetPathNaverTask(requireContext());
                        HashMap<String, Object> recommendPathMap = (HashMap<String, Object>)recommendList.get(j);
                        ArrayList<LatLng> latLngArrayList = pathNaverTask.execute(recommendPathMap.get("start").toString(), recommendPathMap.get("goal").toString(), recommendPathMap.get("waypoints").toString()).get();
                        PathOverlay path = new PathOverlay();
                        path.setCoords(latLngArrayList);
                        path.setOnClickListener(overlay -> {
                            Toast.makeText(requireContext(), "해당 경로로 지정되었습니다.", Toast.LENGTH_SHORT).show();
                            return true;
                        });
                        path.setWidth(20);
                        if(j == 0) {
                            path.setColor(Color.YELLOW);
                        } else if(j == 1) {
                            path.setColor(Color.GREEN);
                        } else if(j == 2) {
                            path.setColor(Color.RED);
                        } else {
                            path.setColor(Color.BLUE);
                        }
                        pathOverlayMap.put("path"+pathOverlayMapCount++, path);
                        path.setMap(naverMap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for(int i=0; i<3; i++) {
                    /*
                    HashMap<String, Object> dataMap = (HashMap<String, Object>)recommendList.get(i);
                    HashMap<String, Object> latlngMap = new HashMap<String, Object>();

                    String wayPoint = (String)dataMap.get("waypoints");
                    String[] points = wayPoint.split(":");
                    for(int j=0; j<points.length; j++) {
                        String[] point = points[j].split(",");
                        latlngMap.put("lat", points[1]);
                        latlngMap.put("lng", points[1]);
                    }
                                    setCaptionToMap(naverMap, infoWindow, dataList, dataList.size(), i);
                */
                    setCaptionToMap(naverMap, infoWindow, (ArrayList<Object>) resultMap.get("recommendDataList"+i), ((ArrayList<?>) resultMap.get("recommendDataList"+i)).size(), i);
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

            String category = utils.getCategory(paramMap.get("category").toString());

            infoWindow.setAnchor(new PointF(0, 1));
            infoWindow.setOffsetX(getResources().getDimensionPixelSize(R.dimen.custom_info_window_offset_x));
            infoWindow.setOffsetY(getResources().getDimensionPixelSize(R.dimen.custom_info_window_offset_y));
            ViewGroup rootView = viewGroup.findViewById(R.id.map);
            marker.setOnClickListener(overlay -> {
                targetMarker = marker;
                targetLatLng = xy;
                targetName = paramMap.get("name").toString();
                //Log.i("marker.setOnClickListener-targetMarker", targetMarker.toString());
                //Log.i("marker.setOnClickListener-targetLatLng", xy.toString());
                infoWindowAdapter adapter = new infoWindowAdapter(getContext(), rootView, paramMap);
                View view = adapter.getContentView(infoWindow);
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(marker.getPosition());
                naverMap.moveCamera(cameraUpdate);
                infoWindow.setAdapter(adapter);
                infoWindow.open(marker);
                return true;
            });
            marker.setIcon(MarkerIcons.PINK);
            marker.setCaptionTextSize(14);
            marker.setCaptionText(paramMap.get("name").toString());
            marker.setCaptionMinZoom(12);
            marker.setSubCaptionTextSize(10);
            marker.setWidth(60);
            marker.setHeight(100);
            marker.setSubCaptionColor(Color.GRAY);
            marker.setSubCaptionText("추천 장소");
            marker.setSubCaptionMinZoom(13);
            //markerMap.put("marker-"+i, marker);
            //markerLatLngMap.put("marker-"+i, xy);
            marker.setMap(naverMap);
            infoWindow.open(marker);

            naverMap.setOnMapClickListener((point, coord) -> {
                infoWindow.setAdapter(new InfoWindowAdapter(this));
                infoWindow.setPosition(coord);
                infoWindow.open(naverMap);
            });
            naverMap.setOnMapLongClickListener((point, coord) -> {
                Boolean stopMarkerCheck = false;
                for (int j = 0; j < stopMarkerMap.size(); j++) {
                    try {
                        LatLng tempLatLng = stopMarkerLatLngMap.get("marker" + (j));
                        if ((tempLatLng.toLatLng() == targetLatLng) && (stopMarkerCount > 0)) {
                            stopMarkerMap.remove("marker" + j);
                            stopMarkerLatLngMap.remove("marker" + j);
                            stopMarkerNameMap.remove("marker" + j);
                            stopMarkerCheck = true;
                            if (stopMarkerCount > 0) {
                                stopMarkerCount--;
                            }
                            //Log.i("=========true" + stopMarkerCount, marker.getPosition() + "/" + stopMarkerMap.toString());
                            Toast.makeText(getContext(), "해당 경유지가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "경유지를 클릭 후 눌러주세요", Toast.LENGTH_SHORT).show();
                    }
                }
                if ((stopMarkerCheck == false) && (stopMarkerCount < 3)) {
                    stopMarkerMap.put("marker" + stopMarkerCount, targetMarker);
                    stopMarkerLatLngMap.put("marker" + stopMarkerCount, targetLatLng);
                    stopMarkerNameMap.put("marker"+stopMarkerCount, targetName);
                    //Log.i("=========false" + stopMarkerCount, stopMarkerMap.get("marker" + stopMarkerCount) + " / " + stopMarkerLatLngMap.get("marker" + stopMarkerCount));
                    if (stopMarkerCount < 2) {
                        stopMarkerCount++;
                    }
                    Toast.makeText(getContext(), targetName+"을(를) 경유지로 등록했습니다", Toast.LENGTH_SHORT).show();
                } else if(stopMarkerCount > 3){
                    Toast.makeText(getContext(), "경유지를 3개 이상 등록할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "경유지를 클릭 후 눌러주세요", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void setCaptionToMap(NaverMap naverMap, InfoWindow infoWindow, ArrayList<Object> dataList, int dataSize) {
        Utils utils = new Utils();
        for(int i=0; i<dataSize; i++) {
            LinkedTreeMap<String,Object> paramMap = (LinkedTreeMap<String, Object>) dataList.get(i);
            //Log.i("paramMap"+i, paramMap.toString());

            Marker marker = new Marker();
            LatLng xy = new LatLng((Double) paramMap.get("lat"), (Double) paramMap.get("lng"));
            marker.setPosition(xy);

            String category = utils.getCategory(paramMap.get("category").toString());

            infoWindow.setAnchor(new PointF(0, 1));
            infoWindow.setOffsetX(getResources().getDimensionPixelSize(R.dimen.custom_info_window_offset_x));
            infoWindow.setOffsetY(getResources().getDimensionPixelSize(R.dimen.custom_info_window_offset_y));
            ViewGroup rootView = viewGroup.findViewById(R.id.map);
            marker.setOnClickListener(overlay -> {
                targetMarker = marker;
                targetLatLng = xy;
                targetName = paramMap.get("name").toString();
                infoWindowAdapter adapter = new infoWindowAdapter(getContext(), rootView, paramMap);
                View view = adapter.getContentView(infoWindow);
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(marker.getPosition());
                naverMap.moveCamera(cameraUpdate);
                infoWindow.setAdapter(adapter);
                infoWindow.open(marker);
                return true;
            });
            marker.setIcon(MarkerIcons.GRAY);
            marker.setCaptionTextSize(14);
            marker.setCaptionText(paramMap.get("name").toString());
            marker.setCaptionMinZoom(12);
            marker.setSubCaptionTextSize(10);
            marker.setWidth(60);
            marker.setHeight(100);
            marker.setSubCaptionColor(Color.GRAY);
            marker.setSubCaptionText("추천 장소");
            marker.setSubCaptionMinZoom(13);
            markerMap.put("marker"+i, marker);
            markerLatLngMap.put("marker"+i, xy);
            marker.setMap(naverMap);
            infoWindow.open(marker);

            naverMap.setOnMapClickListener((point, coord) -> {
                infoWindow.setAdapter(new InfoWindowAdapter(this));
                infoWindow.setPosition(coord);
                infoWindow.open(naverMap);
            });

            naverMap.setOnMapLongClickListener((point, coord) -> {
                Boolean stopMarkerCheck = false;
                for(int j=0; j<stopMarkerMap.size(); j++) {
                    LatLng tempLatLng = stopMarkerLatLngMap.get("marker"+(j));
                    try {
                        if ((tempLatLng.toLatLng() == targetLatLng) && (stopMarkerCount > 0)) {
                            stopMarkerMap.remove("marker" + j);
                            stopMarkerLatLngMap.remove("marker" + j);
                            stopMarkerNameMap.remove("marker" + j);

                            stopMarkerCheck = true;
                            if (stopMarkerCount > 0) {
                                stopMarkerCount--;
                            }
                            Log.i("=========true" + stopMarkerCount, marker.getPosition() + "/" + stopMarkerMap.toString());
                            Toast.makeText(getContext(), "해당 경유지가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "경유지를 클릭 후 눌러주세요", Toast.LENGTH_SHORT).show();
                    }
                }
                if((stopMarkerCheck == false) && (stopMarkerCount < 3)) {
                    stopMarkerMap.put("marker"+stopMarkerCount, targetMarker);
                    stopMarkerLatLngMap.put("marker"+stopMarkerCount, targetLatLng);
                    stopMarkerNameMap.put("marker"+stopMarkerCount, targetName);
                    //Log.i("=========false"+stopMarkerCount, stopMarkerMap.get("marker"+stopMarkerCount) +" / "+ stopMarkerLatLngMap.get("marker"+stopMarkerCount));
                    if(stopMarkerCount < 2) {
                        stopMarkerCount++;
                    }
                    Toast.makeText(getContext(), targetName+"을(를) 경유지로 등록했습니다", Toast.LENGTH_SHORT).show();
                } else if(stopMarkerCount > 3){
                    Toast.makeText(getContext(), "경유지를 3개 이상 등록할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "경유지를 클릭 후 눌러주세요", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void setCaptionToMap(NaverMap naverMap, InfoWindow infoWindow, ArrayList<Object> dataList, int dataSize, int color) {
        Utils utils = new Utils();
        for(int i=0; i<dataSize; i++) {
            LinkedTreeMap<String,Object> paramMap = (LinkedTreeMap<String, Object>) dataList.get(i);
            //Log.i("paramMap"+i, paramMap.toString());

            Marker marker = new Marker();
            LatLng xy = new LatLng((Double) paramMap.get("lat"), (Double) paramMap.get("lng"));
            marker.setPosition(xy);

            String category = utils.getCategory(paramMap.get("category").toString());

            infoWindow.setAnchor(new PointF(0, 1));
            infoWindow.setOffsetX(getResources().getDimensionPixelSize(R.dimen.custom_info_window_offset_x));
            infoWindow.setOffsetY(getResources().getDimensionPixelSize(R.dimen.custom_info_window_offset_y));
            ViewGroup rootView = viewGroup.findViewById(R.id.map);
            marker.setOnClickListener(overlay -> {
                targetMarker = marker;
                targetLatLng = xy;
                targetName = paramMap.get("name").toString();
                infoWindowAdapter adapter = new infoWindowAdapter(getContext(), rootView, paramMap);
                View view = adapter.getContentView(infoWindow);
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(marker.getPosition());
                naverMap.moveCamera(cameraUpdate);
                infoWindow.setAdapter(adapter);
                infoWindow.open(marker);
                return true;
            });
            if(color == 0) {
                marker.setIcon(MarkerIcons.YELLOW);
            } else if(color == 1) {
                marker.setIcon(MarkerIcons.GREEN);
            } else if(color == 2) {
                marker.setIcon(MarkerIcons.RED);
            } else {
                marker.setIcon(MarkerIcons.BLUE);
            }
            marker.setCaptionTextSize(14);
            marker.setCaptionText(paramMap.get("name").toString());
            marker.setCaptionMinZoom(12);
            marker.setSubCaptionTextSize(10);
            marker.setWidth(60);
            marker.setHeight(100);
            marker.setSubCaptionColor(Color.GRAY);
            marker.setSubCaptionText("추천 장소");
            marker.setSubCaptionMinZoom(13);
            markerMap.put("marker"+i, marker);
            markerLatLngMap.put("marker"+i, xy);
            marker.setMap(naverMap);
            infoWindow.open(marker);

            naverMap.setOnMapClickListener((point, coord) -> {
                infoWindow.setAdapter(new InfoWindowAdapter(this));
                infoWindow.setPosition(coord);
                infoWindow.open(naverMap);
            });

            naverMap.setOnMapLongClickListener((point, coord) -> {
                Boolean stopMarkerCheck = false;
                for(int j=0; j<stopMarkerMap.size(); j++) {
                    LatLng tempLatLng = stopMarkerLatLngMap.get("marker"+(j));
                    try {
                        if ((tempLatLng.toLatLng() == targetLatLng) && (stopMarkerCount > 0)) {
                            stopMarkerMap.remove("marker" + j);
                            stopMarkerLatLngMap.remove("marker" + j);
                            stopMarkerNameMap.remove("marker" + j);

                            stopMarkerCheck = true;
                            if (stopMarkerCount > 0) {
                                stopMarkerCount--;
                            }
                            Log.i("=========true" + stopMarkerCount, marker.getPosition() + "/" + stopMarkerMap.toString());
                            Toast.makeText(getContext(), "해당 경유지가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "경유지를 클릭 후 눌러주세요", Toast.LENGTH_SHORT).show();
                    }
                }
                if((stopMarkerCheck == false) && (stopMarkerCount < 3)) {
                    stopMarkerMap.put("marker"+stopMarkerCount, targetMarker);
                    stopMarkerLatLngMap.put("marker"+stopMarkerCount, targetLatLng);
                    stopMarkerNameMap.put("marker"+stopMarkerCount, targetName);
                    if(stopMarkerCount < 2) {
                        stopMarkerCount++;
                    }
                    Toast.makeText(getContext(), targetName+"을(를) 경유지로 등록했습니다", Toast.LENGTH_SHORT).show();
                } else if(stopMarkerCount > 3){
                    Toast.makeText(getContext(), "경유지를 3개 이상 등록할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "경유지를 클릭 후 눌러주세요", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}

