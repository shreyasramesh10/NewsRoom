package com.android.shreyas.newsroom.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.android.shreyas.newsroom.MainActivity;
import com.android.shreyas.newsroom.R;
import com.android.shreyas.newsroom.UserStoriesActivity;
import com.android.shreyas.newsroom.adapters.MostPopularPagerAdapter;
import com.android.shreyas.newsroom.adapters.TopStoriesPagerAdapter;

public class ViewPagerFragment extends Fragment {
    TopStoriesPagerAdapter topStoriesPagerAdapter;
    MostPopularPagerAdapter mostPopularPagerAdapter;
    ViewPager viewPager;
    private static final String ARG_PARAM = "param";

    // TODO: Rename and change types of parameters
    private String mParam = "TopStories";


    public ViewPagerFragment() {
        // Required empty public constructor
    }


    public static ViewPagerFragment newInstance(String param) {
        ViewPagerFragment fragment = new ViewPagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    public static ViewPagerFragment newInstance() {
        ViewPagerFragment fragment = new ViewPagerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
            if(mParam.equals("topstories")){
                topStoriesPagerAdapter = new TopStoriesPagerAdapter(getChildFragmentManager(),getContext());

            }
            else{
                mostPopularPagerAdapter = new MostPopularPagerAdapter(getChildFragmentManager(),getContext());

            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_viewpager,container,false);
        viewPager = (ViewPager) rootView.findViewById(R.id.pager);

        FragmentManager fm = getChildFragmentManager();
        if(mParam.equals("topstories")){
            //topStoriesPagerAdapter = new TopStoriesPagerAdapter(fm,getContext());
            viewPager.setAdapter(topStoriesPagerAdapter);
            ((MainActivity) getActivity()).setActionBarTitle("Top Stories");
        }
        else{
            //mostPopularPagerAdapter = new MostPopularPagerAdapter(fm,getContext());
            viewPager.setAdapter(mostPopularPagerAdapter);
            ((MainActivity) getActivity()).setActionBarTitle("Most Popular");
        }

        viewPager.setPageTransformer(true, new RotateDownTransformer());
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return rootView;
    }

}
