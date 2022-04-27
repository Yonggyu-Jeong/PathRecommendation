package com.example.shareonfoot.util;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.shareonfoot.HTTP.Service.MapService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;

public class WorkTask {
    public static class GetLocateNameTask extends AsyncTask<String, Void, HashMap> {

        public Context context;
        private HashMap hashMap;

        public GetLocateNameTask(Context getContext) {
            context = getContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HashMap doInBackground(String... obj) {
            Call<JsonObject> objectCall = MapService.getRetrofit(context).getLocalName(obj[0]);
            hashMap = new HashMap();
            try {
                Object result = objectCall.execute().body();
                Gson gson = new Gson();
                JsonObject json = gson.toJsonTree(result).getAsJsonObject();
                JsonArray items = json.getAsJsonArray("items");

                hashMap.put("title", items.get(0).getAsJsonObject().get("title"));
                hashMap.put("category", items.get(0).getAsJsonObject().get("category"));
                hashMap.put("mapx", items.get(0).getAsJsonObject().get("mapx").getAsDouble());
                hashMap.put("mapy", items.get(0).getAsJsonObject().get("mapy").getAsDouble());
                hashMap.put("check", "ok");
                return hashMap;

            } catch (IOException e) {
                e.printStackTrace();
                hashMap.put("check", "fail");
                return hashMap;
            }
        }

        @Override
        protected void onPostExecute(HashMap s) {
            super.onPostExecute(s);
            if (s.equals("fail")) {
                Toast.makeText(context, "네트워크 연결상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }


       public static class GetLocateTask extends AsyncTask<String, Void, String> {

        public Context context;

        public GetLocateTask(Context getContext) {
            context = getContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... obj) {
            HashMap hashMap = new HashMap();
            HashMap hashMap1 = new HashMap();
            hashMap1.put("publicId", obj[0]);
            hashMap1.put("userId", obj[1]);

            HashMap hashMap2 = new HashMap();
            hashMap2.put("lat", obj[2]);
            hashMap2.put("lng", obj[3]);

            HashMap hashMap3 = new HashMap();
            hashMap3.put("lat", obj[4]);
            hashMap3.put("lng", obj[5]);

            hashMap.put("user", hashMap1);
            hashMap.put("start", hashMap2);
            hashMap.put("goal", hashMap3);

            Call<JsonObject> objectCall = MapService.getRetrofit(context).getLocate(hashMap);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("fail")) {
                Toast.makeText(context, "네트워크 연결상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}