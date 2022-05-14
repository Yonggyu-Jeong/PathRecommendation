package com.example.shareonfoot.HTTP.Service;

import android.content.Context;

import com.example.shareonfoot.HTTP.APIAdapter;
import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class CategoryService extends APIAdapter {
    /**
     * Retrofit 객체를 가져오는 메소드
     *
     * @param context
     * @return
     */
    public static CategoryAPI getRetrofit(Context context) {
        // 현재 서비스객체의 이름으로 Retrofit 객체를 초기화 하고 반환
        return (CategoryAPI) retrofit(context, CategoryAPI.class);
    }

    public interface CategoryAPI {

        @Headers("Content-Type: application/json; charset=utf-8")
        @POST("/category/list")
        Call<JsonObject> getCategory(@Body HashMap hashMap);

    }

}