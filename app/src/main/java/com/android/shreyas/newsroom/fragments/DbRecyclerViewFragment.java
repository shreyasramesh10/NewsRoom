package com.android.shreyas.newsroom.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.android.shreyas.newsroom.DbDetailViewActivity;
import com.android.shreyas.newsroom.DetailViewActivity;
import com.android.shreyas.newsroom.MainActivity;
import com.android.shreyas.newsroom.R;
import com.android.shreyas.newsroom.SimpleItemTouchHelperCallback;
import com.android.shreyas.newsroom.WebViewActivity;
import com.android.shreyas.newsroom.adapters.DbRecyclerViewAdapter;
import com.android.shreyas.newsroom.adapters.MyDataBaseAdapter;
import com.android.shreyas.newsroom.models.NewsDb;
import com.baoyz.widget.PullRefreshLayout;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FlipInBottomXAnimator;

public class DbRecyclerViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DbRecyclerViewAdapter mRecyclerViewAdapter;
    PullRefreshLayout layout = null;
    ArrayList<NewsDb> newsDbArrayList;
    MyDataBaseAdapter dataBaseAdapter;// = new MyDataBaseAdapter(getContext());
    Cursor cursor;// = dataBaseAdapter.getAllDataCursor();
    public DbRecyclerViewFragment() {
        // Required empty public constructor
    }

    public static DbRecyclerViewFragment newInstance() {
        DbRecyclerViewFragment fragment = new DbRecyclerViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //newsDbArrayList = new ArrayList<>();
        dataBaseAdapter = new MyDataBaseAdapter(getContext());
        cursor = dataBaseAdapter.getAllDataCursor();
        //setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView;
        newsDbArrayList = new ArrayList<>();
        rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        //SQLiteDatabase db = dataBaseAdapter.helper.getReadableDatabase();

        //Cursor cursor = dataBaseAdapter.getInformation(db);

        cursor.moveToFirst();
        int cursorcount = cursor.getCount();
        if(cursorcount>0){
            do {
                NewsDb news = new NewsDb(cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4));
                newsDbArrayList.add(news);
            } while (cursor.moveToNext());
        }

        mRecyclerViewAdapter = new DbRecyclerViewAdapter(getActivity(), newsDbArrayList,dataBaseAdapter);

        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        layout = (PullRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerViewAdapter.notifyItemRangeChanged(0, mRecyclerViewAdapter.getItemCount());
                layout.setRefreshing(false);
                Toast.makeText(getContext(), "Refreshed", Toast.LENGTH_LONG).show();
            }
        });
        layout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        itemAnimation();
        adapterAnimation();

        mRecyclerViewAdapter.setOnCardClickListener(new DbRecyclerViewAdapter.onCardClickListener() {
            @Override
            public void onCardClick(View view, int position) {
                NewsDb newsDb = newsDbArrayList.get(position);
                Intent intent = new Intent(getActivity(), DbDetailViewActivity.class);
                intent.putExtra("result", newsDb);
                startActivity(intent);
            }

            @Override
            public void onSaveIconClick(View view, int position) {
                //delete from db
                NewsDb newsDb = newsDbArrayList.get(position);
                String url = newsDb.getUrl();
                newsDbArrayList.remove(position);
                //newsDbArrayList.remove(newsDb);
                mRecyclerViewAdapter.notifyDataSetChanged();
                int count = dataBaseAdapter.deleteRow(url);
                Toast.makeText(getActivity(), "Bookmark Removed ", Toast.LENGTH_SHORT).show();
            }
        });
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mRecyclerViewAdapter);
        ItemTouchHelper d = new ItemTouchHelper(callback);
        ((MainActivity) getActivity()).setActionBarTitle("Bookmarks");
        d.attachToRecyclerView(mRecyclerView);

        return rootView;
    }

    private void itemAnimation(){
        FlipInBottomXAnimator animator = new FlipInBottomXAnimator();
        animator.setAddDuration(300);
        animator.setRemoveDuration(300);
        mRecyclerView.setItemAnimator(animator);
    }

    private void adapterAnimation(){
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mRecyclerViewAdapter);
        SlideInBottomAnimationAdapter slideAdapter = new SlideInBottomAnimationAdapter(alphaAdapter);
        slideAdapter.setDuration(1000);
        slideAdapter.setInterpolator(new OvershootInterpolator());
        slideAdapter.setFirstOnly(false);
        mRecyclerView.setAdapter(slideAdapter);
    }

}
