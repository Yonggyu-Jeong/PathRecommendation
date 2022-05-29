package com.example.shareonfoot.util;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.shareonfoot.R;
import com.google.gson.internal.LinkedTreeMap;
import com.naver.maps.map.overlay.InfoWindow;

public class infoWindowAdapter extends InfoWindow.DefaultViewAdapter {
    private final Context mContext;
    private final ViewGroup mParent;
    public LinkedTreeMap<String, Object> mParamMap;

    public infoWindowAdapter(@NonNull Context context, ViewGroup parent, LinkedTreeMap<String, Object> paramMap)
    {
        super(context);
        mContext = context;
        mParent = parent;
        mParamMap = paramMap;
    }

    @Override
    @NonNull
    public View getContentView(@NonNull InfoWindow infoWindow)
    {
        View view = (View) LayoutInflater.from(mContext).inflate(R.layout.info_window_view, mParent, false);

        Button buttonAdd = (Button) view.findViewById(R.id.btnAddForLocate);
        Button buttonDelete = (Button) view.findViewById(R.id.btnDeleteForLocate);

        TextView title = (TextView) view.findViewById(R.id.txttitle);
        ImageView imagePoint = (ImageView) view.findViewById(R.id.imagepoint);
        TextView category = (TextView) view.findViewById(R.id.store_category);
        TextView store_adress = (TextView) view.findViewById(R.id.store_adress);
        TextView store_rate = (TextView) view.findViewById(R.id.store_rate);
        TextView store_hashtag = (TextView) view.findViewById(R.id.store_hashtag);

        Utils utils = new Utils();
        String type = utils.getCategory(mParamMap.get("category").toString());
        String tags = "";
        for(int j=1; j<5; j++) {
            tags = "#"+utils.getTagCategory(mParamMap.get("tag"+j).toString()+"\n");
        }
        title.setText(mParamMap.get("name").toString());
        category.setText(type);
        store_adress.setText(mParamMap.get("road").toString());
        if(mParamMap.containsKey("rate")) {
            store_rate.setText(mParamMap.get("rate").toString());
        }
        store_hashtag.setText(tags);

        //imagePoint.setImageResource(R.drawable.ic_place_black_24dp);

        return view;
    }

}
