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
    View view;
    Button buttonAdd;
    Button buttonDelete;

    public interface OnItemClickListener {
        void onItemClick(ViewGroup mParent, Context mContext, LinkedTreeMap<String, Object> mParamMap);
    }

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener nmListener = null;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.nmListener = listener;
    }

    public infoWindowAdapter(@NonNull Context context, ViewGroup parent, LinkedTreeMap<String, Object> paramMap)
    {
        super(context);
        mContext = context;
        mParent = parent;
        mParamMap = paramMap;
        view = (View) LayoutInflater.from(mContext).inflate(R.layout.info_window_view, mParent, false);
    }

    @Override
    @NonNull
    public View getContentView(@NonNull InfoWindow infoWindow)
    {
        buttonAdd = (Button) view.findViewById(R.id.btnAddForLocate);
        buttonDelete = (Button) view.findViewById(R.id.btnDeleteForLocate);

        TextView txtTitle = (TextView) view.findViewById(R.id.txttitle);
        ImageView imagePoint = (ImageView) view.findViewById(R.id.imagepoint);
        TextView category = (TextView) view.findViewById(R.id.store_category);
        TextView store_adress = (TextView) view.findViewById(R.id.store_adress);
        TextView store_rate = (TextView) view.findViewById(R.id.store_rate);
        TextView store_hashtag = (TextView) view.findViewById(R.id.store_hashtag);


     //   buttonAdd.setOnClickListener((View.OnClickListener) this);
/*
        buttonDelete.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "===============??", Toast.LENGTH_SHORT).show();
            }
        });

 */
        Utils utils = new Utils();

        String type = utils.getCategory(mParamMap.get("category").toString());
        String tags = "";
        for(int j=1; j<5; j++) {
            tags = category+"#"+utils.getTagCategory(mParamMap.get("tag"+j).toString());
            if(j<4) {
                tags += "\n";
            }
        }
        Log.i("=====", tags);

        txtTitle.setText(mParamMap.get("name").toString());
        category.setText(type);
        store_adress.setText(mParamMap.get("road").toString());
        store_rate.setText(mParamMap.get("rate").toString());
        store_hashtag.setText(tags);

        //imagePoint.setImageResource(R.drawable.ic_place_black_24dp);

        return view;
    }

}
