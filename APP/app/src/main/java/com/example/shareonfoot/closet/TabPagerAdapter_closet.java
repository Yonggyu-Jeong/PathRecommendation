package com.example.shareonfoot.closet;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

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
        switch (position) {
            case 0:
                return TabFragment_Clothes_inCloset.newInstance("private","share");
            case 1:
                return TabFragment_Clothes_inCloset.newInstance("private","음식점");
            case 2:
                return TabFragment_Clothes_inCloset.newInstance("private","역사관광지");
            case 3:
                return TabFragment_Clothes_inCloset.newInstance("private","자연관광지");
            case 4:
                return TabFragment_Clothes_inCloset.newInstance("private","체험관광지");
            case 5:
                return TabFragment_Clothes_inCloset.newInstance("private","테마관광지");
            case 6:
                return TabFragment_Clothes_inCloset.newInstance("private","기타관광지");
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
}
