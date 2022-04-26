package com.example.shareonfoot.HTTP.Session.interceptor;

import android.content.Context;

import com.example.shareonfoot.HTTP.Session.preference.CookieSharedPreferences;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * ReceivedCookiesInterceptor 클래스
 *
 * @author devetude
 */
public class ReceivedCookiesInterceptor implements Interceptor {
    // CookieSharedReferences 객체
    private CookieSharedPreferences cookieSharedPreferences;

    /**
     * 생성자
     *
     * @param context
     */
    public ReceivedCookiesInterceptor(Context context){
        // CookieSharedReferences 객체 초기화
        cookieSharedPreferences = CookieSharedPreferences.getInstanceOf(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // 가져온 chain으로 부터 리스폰스 객체를 생성
        Request original = chain.request();

        Request.Builder modify = original.newBuilder();
        modify.addHeader("Content-Type", "text/plain; charset=utf-8")
                .addHeader("Accept","application/json; charset=utf-8").build();
        modify.method(original.method(),original.body());
        Request request = modify.build();
        Response response = chain.proceed(request);

        // 리스폰스의 헤더 영역에 Set-Cookie가 설정되어있는 경우
        if (!response.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            // 쿠키 값을 읽어옴
            for (String header : response.headers("Set-Cookie")) {
                cookies.add(header);
            }

            // 쿠키 값을 CookieSharedPreferences에 저장
            cookieSharedPreferences.putHashSet(CookieSharedPreferences.COOKIE_SHARED_PREFERENCES_KEY, cookies);
        }

        // 리스폰스 객체 반환
        return response.newBuilder()
                .body(ResponseBody.create(response.body().contentType()
                        , URLDecoder.decode(response.body().string().replaceAll("%", ""),"utf-8")))
                .build();
    }
}
