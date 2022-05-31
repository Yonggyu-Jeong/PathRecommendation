package com.example.shareonfoot.util;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.shareonfoot.HTTP.Service.CategoryService;
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
                String title = items.get(0).getAsJsonObject().get("title").toString();
                title = title.replaceAll("<b>", "").replaceAll("</b>", "");
                hashMap.put("title", title);
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


       public static class GetLocateTask extends AsyncTask<String, Void,  HashMap<String, Object>> {

        public Context context;

        public GetLocateTask(Context getContext) {
            context = getContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected  HashMap<String, Object> doInBackground(String... obj) {
            HashMap map = new HashMap();
            HashMap<String, Object> resultMap = new HashMap<String, Object>();
            HashMap mapUser = new HashMap();
            HashMap mapStart = new HashMap();
            HashMap mapGoal = new HashMap();

            mapUser.put("id", obj[0]);
            mapUser.put("password", obj[1]);
            mapStart.put("lng", obj[2]);
            mapStart.put("lat", obj[3]);
            mapGoal.put("lng", obj[4]);
            mapGoal.put("lat", obj[5]);

            map.put("user", mapUser);
            map.put("start", mapStart);
            map.put("goal", mapGoal);

            Call<JsonObject> objectCall = MapService.getRetrofit(context).getLocate(map);
            try {
                Object result = objectCall.execute().body();
                Log.e("SELECT 목록 = ", result.toString());
                //JsonObject json = gson.toJsonTree(result).getAsJsonObject();
                resultMap = new Gson().fromJson(result.toString(), new TypeToken<HashMap<String, Object>>() {}.getType());
                resultMap.put("check", "ok");
                return resultMap;

            } catch (IOException e) {
                e.printStackTrace();
                resultMap.put("check", "fail");
                return resultMap;
            }
        }

        @Override
        protected void onPostExecute( HashMap<String, Object> s) {
            super.onPostExecute(s);
            if (s.equals("fail")) {
                Toast.makeText(context, "네트워크 연결상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class GetTestLocateTask extends AsyncTask<String, Void, String> {

        public Context context;

        public GetTestLocateTask(Context getContext) {
            context = getContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... obj) {
            HashMap map = new HashMap();
            HashMap mapUser = new HashMap();
            HashMap mapStart = new HashMap();
            HashMap mapGoal = new HashMap();

            mapUser.put("id", obj[0]);
            mapUser.put("password", obj[1]);
            mapStart.put("lng", obj[2]);
            mapStart.put("lat", obj[3]);
            mapGoal.put("lng", obj[4]);
            mapGoal.put("lat", obj[5]);

            map.put("user", mapUser);
            map.put("start", mapStart);
            map.put("goal", mapGoal);

            Call<JsonObject> objectCall = MapService.getRetrofit(context).getTestLocate(map);
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


    public static class GetCategoryListTask extends AsyncTask<String, Void, String> {

        public Context context;

        public GetCategoryListTask(Context getContext) {
            context = getContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... obj) {
            HashMap map = new HashMap();
            map.put("category", obj);

            Call<JsonObject> objectCall = CategoryService.getRetrofit(context).getCategory(map);
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

    public static class GetLocateForReadyTask extends AsyncTask<String, Void, HashMap<String, Object>> {

        public Context context;

        public GetLocateForReadyTask(Context getContext) {
            context = getContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HashMap<String, Object> doInBackground(String... obj) {
            HashMap<String, Object> map = new HashMap<String, Object>();

            map.put("lat", obj[0]);
            map.put("lng", obj[1]);

            Call<JsonObject> objectCall = MapService.getRetrofit(context).getLocateForReady(map);
            try {
                Object result = objectCall.execute().body();
                Gson gson = new Gson();

                map = new Gson().fromJson(result.toString(), new TypeToken<HashMap<String, Object>>() {}.getType());
                map.put("check", "ok");

                return map;

            } catch (IOException e) {
                map.put("check", "fail");
                e.printStackTrace();
                return map;
            }
        }

        @Override
        protected void onPostExecute(HashMap<String, Object>  s) {
            super.onPostExecute(s);
            if (s.equals("fail")) {
                Toast.makeText(context, "네트워크 연결상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public static class GetPathLocateTask extends AsyncTask<HashMap<String, Object>, Void, HashMap<String, Object>> {
        public Context context;
        private HashMap resultMap;

        public GetPathLocateTask(Context getContext) {
            context = getContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HashMap doInBackground(HashMap<String, Object>... obj) {
            resultMap = new HashMap();

            Call<JsonObject> objectCall = MapService.getRetrofit(context).getPath(obj[0]);
            try {
                Object result = objectCall.execute().body();
                Gson gson = new Gson();
                Log.i("GetPathLocateTask-doInBackground >>>>> ", result.toString());
                JsonObject json = gson.toJsonTree(result).getAsJsonObject();
                //JsonArray items = json.getAsJsonArray("items");

                resultMap.put("check", "ok");
                resultMap.put("result", json.toString());
                return resultMap;

            } catch (IOException e) {
                e.printStackTrace();
                resultMap.put("check", "fail");
                return resultMap;
            }
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> s) {
            super.onPostExecute(s);
            if (s.equals("fail")) {
                Toast.makeText(context, "네트워크 연결상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class GetPathNaverTask extends AsyncTask<String, Void, HashMap<String, Object>> {
        public Context context;
        private HashMap resultMap;

        public GetPathNaverTask(Context getContext) {
            context = getContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HashMap doInBackground(String... obj) {
            resultMap = new HashMap();
            Log.i("responseTask", "start="+obj[0]+"///goal="+obj[1]+"///waypoint+"+obj[2]);
            Call<JsonObject> objectCall = MapService.getRetrofit(context).getPathNaver(obj[0], obj[1], obj[2]);
            try {
                Object result = objectCall.execute().body();
                Gson gson = new Gson();
                Log.i("GetPathLocateTask-doInBackground >>>>> ", result.toString());
                JsonObject json = gson.toJsonTree(result).getAsJsonObject();
                //JsonArray items = json.getAsJsonArray("items");

                resultMap.put("check", "ok");
                resultMap.put("result", json.toString());
                return resultMap;

            } catch (IOException e) {
                e.printStackTrace();
                resultMap.put("check", "fail");
                return resultMap;
            }
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> s) {
            super.onPostExecute(s);
            if (s.equals("fail")) {
                Toast.makeText(context, "네트워크 연결상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}