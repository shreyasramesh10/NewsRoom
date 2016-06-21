package com.android.shreyas.newsroom.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.shreyas.newsroom.MainActivity;
import com.android.shreyas.newsroom.R;
import com.android.shreyas.newsroom.adapters.CircularAdapter;
import com.android.shreyas.newsroom.adapters.MyRecyclerViewAdapter;
import com.android.shreyas.newsroom.adapters.MyRecyclerViewHomeAdapter;
import com.jpardogo.listbuddies.lib.views.ListBuddiesLayout;



public class ListBuddiesFragment extends Fragment {
    int mMarginDefault;
    int[] mScrollConfig;
    private CircularAdapter mAdapterLeft;
    private CircularAdapter mAdapterRight;

    public ListBuddiesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMarginDefault = getResources().getDimensionPixelSize(com.jpardogo.listbuddies.lib.R.dimen.default_margin_between_lists);
        mScrollConfig = getResources().getIntArray(R.attr.scrollFaster);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v;
        v = inflater.inflate(R.layout.fragment_list_buddies, container, false);
        ((MainActivity) getActivity()).setActionBarTitle("News In Images");

        ListBuddiesLayout listBuddiesLayout = (ListBuddiesLayout) v.findViewById(R.id.listbuddies);
        mAdapterLeft = new CircularAdapter(getActivity(), getResources().getDimensionPixelSize(R.dimen.item_height_small), MyRecyclerViewHomeAdapter.ImageUrls_left);
        mAdapterRight = new CircularAdapter(getActivity(), getResources().getDimensionPixelSize(R.dimen.item_height_tall), MyRecyclerViewAdapter.ImageUrls_right);
        listBuddiesLayout.setAdapters(mAdapterLeft, mAdapterRight);
        return v;
    }


}
