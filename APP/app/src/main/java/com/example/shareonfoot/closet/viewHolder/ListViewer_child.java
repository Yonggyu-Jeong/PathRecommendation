package com.example.shareonfoot.closet.viewHolder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.shareonfoot.R;
import com.example.shareonfoot.VO.ClothesVO;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;


//어댑터 : 리사이클러뷰의 아이템 뷰를 생성하는 역할을 함
//뷰 홀더 : 아이템 뷰를 저장하는 객체
//아이템 뷰 : 각각의 카드뷰 한 개
public class ListViewer_child extends RecyclerView.Adapter<ListViewer_child.ItemViewHolder> {

    Context mContext;
    ArrayList<ClothesVO> listData;
    LayoutInflater inflater;
    String[] emoticon = {"😊", "😋", "😍", "😘", "🤩", "🤗", "🙃", "😁", "😄", "😆"};
    boolean match;
    String identifier;
    public interface OnItemClickListener {
        void onItemClick(View v, int position, ImageView iv_Clothes, ClothesVO cloInfo);
    }

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener nmListener = null;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.nmListener = listener;
    }

    //생성자에서 데이터 리스트 객체를 전달받음.
    public ListViewer_child(Context context, ArrayList<ClothesVO> items, boolean match,String identifier) {
        this.mContext = context;
        this.listData = items;
        this.inflater = LayoutInflater.from(context);
        this.match = match;
        this.identifier=identifier;
    }

    //뷰홀더 객체 생성하며 리턴 (아이템뷰를 위한)
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (!match) {
         //   parent = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_horizontal, parent, false);
        } else {
          //  parent = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_matching_user, parent, false);
        }
        return new ItemViewHolder(parent);
    }

    //position 에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    //아이템 개수 반환
    @Override
    public int getItemCount() {
        return this.listData.size();
    }

    public void addItem(ClothesVO data) {
        listData.add(data);
    }


    public void removeIthem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView2;
        private TextView textView3;
        private TextView textView5;
        private TextView textView6;
        private ImageView imageView;
        private RelativeLayout itemview;

        ItemViewHolder(View itemView) {
            super(itemView);

            textView2 = (TextView)itemView.findViewById(R.id.title);
            textView3 = (TextView)itemView.findViewById(R.id.category);
            textView5 = (TextView)itemView.findViewById(R.id.adress);
            textView6 = (TextView)itemView.findViewById(R.id.review);
            imageView = (ImageView)itemView.findViewById(R.id.image);
            //itemview = imageView.findViewById(R.id.itemView);

            if (!match) {
                //location = itemView.findViewById(R.id.location);
                //character = itemView.findViewById(R.id.character);
            }

            itemView.setOnClickListener(v -> {
                //int pos = getAbsoluteAdapterPosition();
                //if (pos != RecyclerView.NO_POSITION) {
                    // 리스너 객체의 메서드 호출.
                    //tab_Hist();
                //}
            });
        }

        void onBind(ClothesVO data) {
        }
    }
}