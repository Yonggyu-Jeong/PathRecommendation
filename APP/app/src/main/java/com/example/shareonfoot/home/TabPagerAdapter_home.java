package com.example.shareonfoot.home;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.shareonfoot.home.subfragment.TabFragment_Clothes_inHome;
import com.example.shareonfoot.codi.TabFragment_Codi_large;

public class TabPagerAdapter_home extends FragmentStatePagerAdapter {
    // Count number of tabs
    private int tabCount;

    public TabPagerAdapter_home(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        Log.e("getItem", ""+position);
        // Returning the current tabs
        switch (position) {
            case 0:
                return TabFragment_Clothes_inHome.newInstance("즐겨찾는 장소","medium");
            case 1:
                return TabFragment_Clothes_inHome.newInstance("최근 가본 장소", "medium");
            /*
            case 0:
                return TabFragment_Clothes_inHome.newInstance("favorite","medium");
            case 1:
                return TabFragment_Codi_large.newInstance("favorite");
*/
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

