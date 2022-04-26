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
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public class JoinService extends APIAdapter {
    /**
     * Retrofit 객체를 가져오는 메소드
     *
     * @param context
     * @return
     */
    public static JoinAPI getRetrofit(Context context) {
        // 현재 서비스객체의 이름으로 Retrofit 객체를 초기화 하고 반환
        return (JoinAPI) retrofit(context, JoinAPI.class);
    }

    public interface JoinAPI {

        //함수 선언


        /**
         * <pre>
         * 문자 전송 Service 메서드
         * </pre>
         * 1. 핸드폰 번호를 입력
         * 2. 6자리 암호를 반환
         * TODO 실패 시에도 6자리 암호를 반환함
         */
        @GET("user/join/sms/send/{phoneNum}")
        Call<JsonObject> sendSMS(@Path("phoneNum") String phoneNum);

        /**
         * <pre>
         * 문자 인증 Service 메서드
         * </pre>
         * 1. 핸드폰 번호를 입력
         * 2. 유저의 핸드폰 번호, 암호가 맞을 경우
         * 2-1. 기존에 계정이 있으면 check: user와 유저의 정보를 전달
         * 2-2. 기존 계정이 없다면 check: ok 전달
         * 3. 틀릴 경우 check: fail 전달
         */
        @GET("user/join/sms/examine/{phoneNum}&{code}")
        @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
        Call<JsonObject> examineSMS(@Path("phoneNum") String phoneNum, @Path("code") String code);

        /**
         * <pre>
         * 회원 가입 Service 메서드
         * </pre>
         * 1. 회원가입이 성공할 경우 ok 전달
         * 2. 실패할 경우 fail 전달
         * 가입했던 사용자는 기존의 데이터에 Update하며 기타 정보를 승계한다.
         * //TODO multipart로 전달되며, user 정보, info전부 필요, Badge는 갯수에 따라 자동 입력
         */
        @Multipart
        @POST("user/join/add")
        Call<JsonObject> addUser(@PartMap() LinkedHashMap<String, RequestBody> partMap, @Part List<MultipartBody.Part> img);

        /**
         * <pre>
         * 중복 아이디 확인 Service    메서드
         * </pre>
         * 1. 아이디를 입력
         * 2-1. 중복이 존재하면 fail를 반환
         * 2-2. 중복이 없을 경우 ok를 반환
         */

        @Headers("Content-Type: application/json")
        @POST("user/join/checkId")
        Call<JsonObject> checkId(@Body HashMap hashMap);

        @Headers("Content-Type: application/json")
        @POST("user/join/checkemail")
        Call<JsonObject> checkEmail(@Body HashMap hashMap);

        /**
         * <pre>
         * 유저 심사 Service 메서드
         * </pre>
         * 1. 닉네임
         * 2. 해당 User의 로그인 시간 update
         * 3. 해당 User의 정보 반환
         */
        @GET("user/join/state/{publicId}")
        Call<JsonObject> examineProfile(@Path("publicId") String publicId);

        /**
         * <pre>
         * fhrmdls 시간 업데이트 Service 메서드
         * </pre>
         */
        @GET("user/login/{publicId}")
        Call<JsonObject> login(@Path("publicId") String publicId);

        @Headers("Content-Type: application/json")
        @POST("user/login/check")
        Call<JsonObject> login_check(@Body HashMap hashMap);

        @Multipart
        @POST("user/join/re-examine")
        Call<JsonObject> reExamineUser(@PartMap() LinkedHashMap<String, RequestBody> partMap, @Part List<MultipartBody.Part> img);

        @Headers("Content-Type: application/json")
        @POST("user/check-personalcode")
        Call<JsonObject> checkInvitationCode(@Body HashMap hashMap);

    }

}