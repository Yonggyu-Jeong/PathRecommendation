package com.example.shareonfoot.closet;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareonfoot.VO.ClothesVO;

public class TabPagerAdapter_closet extends FragmentStatePagerAdapter {

    Fragment fragment;

    // Count number of tabs
    private int tabCount;

    public TabPagerAdapter_closet(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        // Returning the current tabs
        Log.e("getItem", "" + position);
        switch (position) {
            case 0:
                return TabFragment_Clothes_inCloset.newInstance("private", "모두");
            case 1:
                return TabFragment_Clothes_inCloset.newInstance("private", "음식점");
            case 2:
                return TabFragment_Clothes_inCloset.newInstance("private", "역사관광지");
            case 3:
                return TabFragment_Clothes_inCloset.newInstance("private", "자연관광지");
            case 4:
                return TabFragment_Clothes_inCloset.newInstance("private", "체험관광지");
            case 5:
                return TabFragment_Clothes_inCloset.newInstance("private", "테마관광지");
            case 6:
                return TabFragment_Clothes_inCloset.newInstance("private", "기타관광지");
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    private OnItemClickListener itemClickListener;

    //인터페이스 선언
    public interface OnItemClickListener {
        //클릭시 동작할 함수
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    //이후 내부에 선언한 ViewHolder에서 클릭 시 동작 호출
    public class ViewHolder extends RecyclerView.ViewHolder {
        View binding;

        public ViewHolder(View binding) {
            super(binding.getRootView());
            this.binding = binding;

            //아이템 클릭 시 (전체)
            binding.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //존재하는 포지션인지 확인
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        //동작 호출 (onItemClick 함수 호출)
                        if (itemClickListener != null) {
                            itemClickListener.onItemClick(v, pos);
                        }
                    }
                }
            });
        }

    }
}
