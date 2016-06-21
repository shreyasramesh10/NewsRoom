package com.android.shreyas.newsroom.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.android.shreyas.newsroom.fragments.RecyclerViewFragment;
import com.android.shreyas.newsroom.fragments.ViewPagerFragment;

import java.util.HashMap;

/**
 * Created by SHREYAS on 2/9/2016.
 */
public class TopStoriesPagerAdapter extends FragmentStatePagerAdapter {

    Context context;

    public TopStoriesPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        RecyclerViewFragment  f;
        switch (position){
            case 0:
                f = RecyclerViewFragment.newInstance("world");
                break;
            case 1:
                f = RecyclerViewFragment.newInstance("national");
                break;
            case 2:
                f = RecyclerViewFragment.newInstance("nyregion");
                break;
            case 3:
                f = RecyclerViewFragment.newInstance("sports");
                break;
            case 4:
                f = RecyclerViewFragment.newInstance("politics");
                break;
            case 5:
                f = RecyclerViewFragment.newInstance("technology");
                break;
            default:
                f = RecyclerViewFragment.newInstance("world");
                break;
        }
        return f;
    }

    @Override
    public int getCount() {
        return 6;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "WORLD";

            case 1:
                return "NATIONAL";

            case 2:
                return "NY REGION";

            case 3:
                return "SPORTS";

            case 4:
                return "POLITICS";

            case 5:
                return "TECHNOLOGY";

            default:
                return "WORLD";

        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
