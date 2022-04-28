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

        /*
        @Headers("X-Naver-Client-Id: LKHyJ0ByBr4HipAKgbvW")
        @Headers("X-Naver-Client-Secret") String clientSecret = "0zrCPZlQDZ",
        @Query("query") String query,
        @Query("display") int display=1,
        @Query("start") int start = 1
        @GET("https://openapi.naver.com/v1/search/local.json/{}")
        Call<JsonObject> sendSMS(@Path("phoneNum") String phoneNum);
*/

        @Headers({"X-Naver-Client-Id: LKHyJ0ByBr4HipAKgbvW", "X-Naver-Client-Secret: 0zrCPZlQDZ"})
        @GET("https://openapi.naver.com/v1/search/local.json?")
        Call<JsonObject> getLocalName(@Query("query") String query);

        @Headers("Content-Type: application/json; charset=utf-8")
        @POST("/locate/find")
        Call<JsonObject> getLocate(@Body HashMap hashMap);

        @Headers("Content-Type: application/json; charset=utf-8")
        @POST("/testData")
        Call<JsonObject> getTestLocate(@Body HashMap hashMap);

        @Headers("Content-Type: application/json")
        @POST("user/join/checkemail")
        Call<JsonObject> checkEmail(@Body HashMap hashMap);


    }

}