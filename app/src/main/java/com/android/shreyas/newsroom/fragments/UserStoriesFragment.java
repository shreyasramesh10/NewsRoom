package com.android.shreyas.newsroom.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.android.shreyas.newsroom.CameraActivity;
import com.android.shreyas.newsroom.LoginActivity;
import com.android.shreyas.newsroom.MainActivity;
import com.android.shreyas.newsroom.R;
import com.android.shreyas.newsroom.UserStoriesActivity;
import com.android.shreyas.newsroom.adapters.UserStoriesAdapter;
import com.android.shreyas.newsroom.models.Stories;
import com.baoyz.widget.PullRefreshLayout;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.github.fabtransitionactivity.SheetLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FlipInBottomXAnimator;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserStoriesFragment extends Fragment implements SheetLayout.OnFabAnimationEndListener {

    private static final int REQUEST_CODE = 1;
    private static final String ARG_OPTION = "argument_option";
    private OnListItemSelectedListener mListener;
    int position;
    PullRefreshLayout layout=null;
    SheetLayout mSheetLayout;
    FloatingActionButton mFab;
    Firebase listNameRef;
    private static final String TAG = "myApp";
    RecyclerView myRecyclerView;
    LinearLayoutManager mLayoutManager;

    UserStoriesAdapter myRecyclerViewAdapter;
    List<Map<String, ?>> moviesList;
    Firebase ref = new Firebase("https://popping-inferno-9534.firebaseio.com");

    @Override
    public void onFabAnimationEnd() {
        if(ref.getAuth()!=null){
            Intent intent = new Intent(getActivity(), CameraActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }
        else{
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("activity","User Stories");
            startActivityForResult(intent, REQUEST_CODE);
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            mSheetLayout.contractFab();
        }
    }

    public interface OnListItemSelectedListener {
        public void onListItemSelected(View view, HashMap<String, ?> movie);
        void editItem(HashMap<String,?> story);
    }

    public UserStoriesFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            mListener = (OnListItemSelectedListener)context;
//
//        } catch(ClassCastException e) {
//            throw new ClassCastException("activity must implement onFragmentInteraction Listener");
//        }
//
//    }

    public static UserStoriesFragment newInstance() {
        UserStoriesFragment fragment = new UserStoriesFragment();
        return fragment;

    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_stories_recycler_view, container, false);
        moviesList = new ArrayList<Map<String,?>>();
        try {
            mListener = (OnListItemSelectedListener)getContext();

        } catch(ClassCastException e) {
            throw new ClassCastException("activity must implement onFragmentInteraction Listener");
        }
        myRecyclerView  = (RecyclerView) rootView.findViewById(R.id.cardList);
        myRecyclerView.setHasFixedSize(true);
        mSheetLayout = (SheetLayout) rootView.findViewById(R.id.bottom_sheet);
        mFab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        mSheetLayout.setFab(mFab);
        mSheetLayout.setFabAnimationEndListener(this);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSheetLayout.expandFab();
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        //mLayoutManager = new LinearLayoutManager(getActivity());
        myRecyclerView.setLayoutManager(mLayoutManager);


        myRecyclerViewAdapter = new UserStoriesAdapter(getActivity(), moviesList);
        myRecyclerView.setAdapter(myRecyclerViewAdapter);
        layout = (PullRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myRecyclerViewAdapter.notifyItemRangeChanged(0, myRecyclerViewAdapter.getItemCount());
                layout.setRefreshing(false);
                Toast.makeText(getContext(), "Refreshed", Toast.LENGTH_LONG).show();
            }
        });
        layout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        
        myRecyclerViewAdapter.setOnItemClickListener(new UserStoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                HashMap<String, ?> movie = (HashMap<String, ?>) moviesList.get(position);
                mListener.onListItemSelected(view, movie);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onOverflowMenuClick(View view, final int position) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                MenuInflater inflator = popupMenu.getMenuInflater();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_edit:
                                HashMap<String, String> movie = (HashMap<String, String>) moviesList.get(position);
                                mListener.editItem(movie);
                                return true;
                            case R.id.action_delete:
                                UserStoriesFragment.this.position = position;
                                HashMap<String, ?> movie1 = (HashMap<String, ?>) moviesList.get(position);
                                listNameRef.child((String) movie1.get("key")).setValue(null);
                                return true;
                            default:
                                return false;

                        }
                    }
                });
                inflator.inflate(R.menu.menu_popup_stories, popupMenu.getMenu());
                popupMenu.show();
            }
        });

        itemAnimation();
        adapterAnimation();

        listNameRef = new Firebase("https://popping-inferno-9534.firebaseio.com/stories");
        listNameRef = listNameRef.child(listNameRef.getAuth().getUid());
        listNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                Stories movie = snapshot.getValue(Stories.class);
                movie.setKey(snapshot.getKey());
                moviesList.add(position, createMovie(movie.getTitle(), movie.getStory(), movie.getImage(),movie.getKey()));
                myRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                moviesList.remove(position);
                myRecyclerViewAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        ((UserStoriesActivity) getActivity()).setActionBarTitle("Notes");
        return rootView;
    }

    private void itemAnimation(){
        FlipInBottomXAnimator animator = new FlipInBottomXAnimator();
        animator.setAddDuration(300);
        animator.setRemoveDuration(300);
        myRecyclerView.setItemAnimator(animator);
    }

    private void adapterAnimation(){
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(myRecyclerViewAdapter);
        SlideInBottomAnimationAdapter slideAdapter = new SlideInBottomAnimationAdapter(alphaAdapter);
        slideAdapter.setDuration(1000);
        slideAdapter.setInterpolator(new OvershootInterpolator());
        slideAdapter.setFirstOnly(false);
        myRecyclerView.setAdapter(slideAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //setRetainInstance(true);

    }

    private HashMap createMovie(String name,String description, String image, String key) {
        HashMap movie = new HashMap();

        movie.put("name", name);
        movie.put("description", description);
        movie.put("image", image);
        movie.put("key",key);
        return movie;
    }


}
