package com.example.shareonfoot.closet.viewHolder;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareonfoot.VO.ClothesVO;

import java.util.ArrayList;


//어댑터 : 리사이클러뷰의 아이템 뷰를 생성하는 역할을 함
//뷰 홀더 : 아이템 뷰를 저장하는 객체
//아이템 뷰 : 각각의 카드뷰 한 개
public class ListViewer_parent extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<ClothesVO> sub_listData;
    private int[] doubleClickFlag = {0, 0, 0};
    boolean sig;
    RelativeLayout drawIcon, sentView;
    TextView textView;
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
    public ListViewer_parent(Context context, ArrayList<ClothesVO> items, boolean sig, String identifier) {
        this.context = context;
        this.sub_listData = items;
        this.sig = sig;
        this.identifier = identifier;
    }

    public void removeItem(int position) {
        sub_listData.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    //뷰홀더 객체 생성하며 리턴 (아이템뷰를 위한)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        //v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_sent, parent, false);
        //drawIcon = v.findViewById(R.id.drawIcon);
        //sentView = v.findViewById(R.id.sentView);
        //textView = v.findViewById(R.id.see_card);
        //if (sig) return new ListViewHolder(v);
        //else return new HorizonViewHolder(v);
        return null;
    }

    //position 에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int spanCount = 2; // 3 columns
        int spacing = 20; // 50px
        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, spacing, context.getResources().getDisplayMetrics());
        boolean includeEdge = false;

        if (position == 0) {

        } else if (position == 1) {

        } else {

        }
    }

    //아이템 개수 반환
    @Override
    public int getItemCount() {
        return this.sub_listData.size();
    }
}
