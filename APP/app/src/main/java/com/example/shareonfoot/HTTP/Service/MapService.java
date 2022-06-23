package com.example.shareonfoot.HTTP.Service;

import android.content.Context;

import com.google.gson.JsonObject;
import com.example.shareonfoot.HTTP.APIAdapter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MapService extends APIAdapter {
    /**
     * Retrofit 객체를 가져오는 메소드
     *
     * @param context
     * @return
     */
    public static MapAPI getRetrofit(Context context) {
        // 현재 서비스객체의 이름으로 Retrofit 객체를 초기화 하고 반환
        return (MapAPI) retrofit(context, MapAPI.class);
    }

    public interface MapAPI {

        @Headers({"X-Naver-Client-Id: LKHyJ0ByBr4HipAKgbvW", "X-Naver-Client-Secret: 0zrCPZlQDZ"})
        @GET("https://openapi.naver.com/v1/search/local.json?")
        Call<JsonObject> getLocalName(@Query("query") String query);

        @Headers({"X-NCP-APIGW-API-KEY-ID: z61zcmt5wp", "X-NCP-APIGW-API-KEY: O89vy2cYe04XasNZssFfJspMZqfUzW7qaZ9MwYch", "Content-Type: application/json"})
        @GET("https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving?")
        Call<JsonObject> getPathNaver(@Query("start") String start, @Query("goal") String goal, @Query("waypoints") String waypoints);

        @Headers("Content-Type: application/json; charset=utf-8")
        @POST("/find")
        Call<JsonObject> getLocate(@Body HashMap hashMap);

        @Headers("Content-Type: application/json; charset=utf-8")
        @POST("/testData")
        Call<JsonObject> getTestLocate(@Body HashMap hashMap);

        @Headers("Content-Type: application/json; charset=utf-8")
        @POST("/get-location")
        Call<JsonObject> getLocateForReady(@Body HashMap hashMap);

        @Headers("Content-Type: application/json; charset=utf-8")
        @POST("/path")
        Call<JsonObject> getPath(@Body HashMap hashMap);

        @GET("/user/test2")
        Call<JsonObject> getProcessedPath();

        @GET("/category/list/{category}")
        Call<JsonObject> getCategoryList(@Path("category") String category);
    }

}