package com.example.shareonfoot;


public class Global {
    public static final String BASE_URL = "http://101.101.208.123/"; //api 주소
    public static final String TAG = "LOG_RETROFIT";
    public static double bitmapWidth = 500;

    private static Global instance = null;

    public static synchronized Global getInstance(){
        if(null == instance){
            instance = new Global();
        }
        return instance;
    }

    public static String getOriginalPath(String imgPath){
        return BASE_URL+imgPath;
    }

}
