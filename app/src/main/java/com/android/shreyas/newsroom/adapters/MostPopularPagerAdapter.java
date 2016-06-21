package com.android.shreyas.newsroom.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.shreyas.newsroom.fragments.RecyclerViewFragment;

/**
 * Created by SHREYAS on 2/9/2016.
 */
public class MostPopularPagerAdapter extends FragmentStatePagerAdapter {

    Context context;

    public MostPopularPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        RecyclerViewFragment  f;
        if(position ==0 ){
            f = RecyclerViewFragment.newInstance("mostshared");
        }
        else if(position==1){
            f = RecyclerViewFragment.newInstance("mostemailed");
        }
        else{
            f = RecyclerViewFragment.newInstance("mostviewed");
        }
        return f;
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0)
            return "Most Shared";
        if(position==1)
            return "Most Emailed";
        else
            return "Most Viewed";
    }

}
