package com.android.shreyas.newsroom.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.shreyas.newsroom.DetailViewActivity;
import com.android.shreyas.newsroom.MainActivity;
import com.android.shreyas.newsroom.R;
import com.android.shreyas.newsroom.WebViewActivity;
import com.android.shreyas.newsroom.adapters.MyDataBaseAdapter;
import com.android.shreyas.newsroom.adapters.MyRecyclerViewAdapter;
import com.android.shreyas.newsroom.apimanager.GetNewsApi;
import com.android.shreyas.newsroom.models.NewsData;
import com.android.shreyas.newsroom.models.Result;
import com.baoyz.widget.PullRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FlipInBottomXAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecyclerViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecyclerViewFragment extends Fragment {
    protected GetNewsApi getNewsApi;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    MyDataBaseAdapter myDataBaseAdapter;
    ImageView saveIcon;
    Call<NewsData> newsDataCall;
    NewsData newsData = new NewsData();
    List<Result> resultsList = newsData.getResults();
    PullRefreshLayout layout = null;

    private static final String ARG_PARAM = "content";
    private final String API_KEY = "9c88747a6be8832be915c61a06281c2f:5:74600818";

    // TODO: Rename and change types of parameters
    private String mParam;

    public RecyclerViewFragment() {
        // Required empty public constructor
    }

    public static RecyclerViewFragment newInstance(String param1) {
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public static RecyclerViewFragment newInstance() {
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        myDataBaseAdapter = new MyDataBaseAdapter(getContext());
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_recycler_view,container,false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(), resultsList);

        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.nytimes.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        getNewsApi =retrofit.create(GetNewsApi.class);

        switch (mParam){
            case "home":
                newsDataCall = getNewsApi.GetTopStories("home.json", API_KEY);
                ((MainActivity) getActivity()).setActionBarTitle("Home");
                break;
            case "sports":
                newsDataCall = getNewsApi.GetTopStories("sports.json",API_KEY);
                break;
            case "politics":
                newsDataCall = getNewsApi.GetTopStories("politics.json",API_KEY);
                break;
            case "technology":
                newsDataCall = getNewsApi.GetTopStories("technology.json",API_KEY);
                break;
            case "world":
                newsDataCall = getNewsApi.GetTopStories("world.json",API_KEY);
                break;
            case "national":
                newsDataCall = getNewsApi.GetTopStories("national.json",API_KEY);
                break;
            case "nyregion":
                newsDataCall = getNewsApi.GetTopStories("nyregion.json",API_KEY);
                break;
            case "science":
                newsDataCall = getNewsApi.GetTopStories("science.json",API_KEY);
                break;
            case "fashion":
                newsDataCall = getNewsApi.GetTopStories("fashion.json",API_KEY);
                break;
            case "opinion":
                newsDataCall = getNewsApi.GetTopStories("opinion.json",API_KEY);
                break;
            case "mostshared":
                newsDataCall = getNewsApi.GetMostPopular("mostshared", API_KEY);
                break;
            case "mostemailed":
                newsDataCall = getNewsApi.GetMostPopular("mostemailed", API_KEY);
                break;
            case "mostviewed":
                newsDataCall = getNewsApi.GetMostPopular("mostviewed", API_KEY);
                break;
            default:
                newsDataCall = getNewsApi.GetTopStories("home.json",API_KEY);
                break;
        }

        newsDataCall.enqueue(new Callback<NewsData>() {

            @Override
            public void onResponse(Call<NewsData> call, Response<NewsData> response) {
                final WeakReference<MyRecyclerViewAdapter> adapterWeakReference = new WeakReference<>(myRecyclerViewAdapter);

                newsData = response.body();
                List<Result> resultsListLocal = newsData.getResults();

                resultsList.clear();
                for(int i=0;i<resultsListLocal.size();i++){
                    resultsList.add(resultsListLocal.get(i));
                }
                if(adapterWeakReference!=null){
                    final MyRecyclerViewAdapter adapter = adapterWeakReference.get();
                    if(adapter!=null){
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsData> call, Throwable t) {
                Log.d("Retrofit", "Failed " + t.getMessage());
            }
        });

        layout = (PullRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myRecyclerViewAdapter.notifyItemRangeChanged(0, myRecyclerViewAdapter.getItemCount());
                layout.setRefreshing(false);
                Toast.makeText(getContext(),"Refreshed",Toast.LENGTH_LONG).show();
            }
        });
        layout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);

        itemAnimation();
        adapterAnimation();

        recyclerView.setAdapter(myRecyclerViewAdapter);
        myRecyclerViewAdapter.setOnCardClickListener(new MyRecyclerViewAdapter.onCardClickListener() {
            @Override
            public void onCardClick(View view, int position) {
                Result result = resultsList.get(position);

                Intent intent = new Intent(getActivity(), DetailViewActivity.class);
                intent.putExtra("result", result);
                startActivity(intent);
            }

            @Override
            public void onSaveIconClick(View view, int position) {
                Result result = resultsList.get(position);
                String image_url = "http://www.freelandlittleleague.com/assets/no-image-available-bbdbbe501d2b08a157a21431bc7b49df2c6cf6d892cc3083114229876cd7d6f4.jpg";
                if (result.getMedia().size() == 0 && result.getMultimedia().size() != 0) {
                    image_url = result.getMultimedia().get(1).getUrl();
                }
                if (result.getMultimedia().size() == 0 && result.getMedia().size() != 0) {
                    image_url = result.getMedia().get(0).getMediaMetadata().get(1).getUrl();
                }


                long id = myDataBaseAdapter.insertData(result.getUrl(), result.getTitle(), result.getAbstract(),image_url);
                if (id < 0) {
                    //Toast.makeText(getActivity(), "Unsuccessful: " + id, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Saved for Later in Bookmarks!", Toast.LENGTH_SHORT).show();
                }

                result.setIsSaved(true);
                ((ImageView)view).setImageResource(R.drawable.bookmarksave);
            }
        });


        return rootView;
    }

    private void itemAnimation(){
        FlipInBottomXAnimator animator = new FlipInBottomXAnimator();
        animator.setAddDuration(300);
        animator.setRemoveDuration(300);
        recyclerView.setItemAnimator(animator);
    }

    private void adapterAnimation(){
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(myRecyclerViewAdapter);
        SlideInBottomAnimationAdapter slideAdapter = new SlideInBottomAnimationAdapter(alphaAdapter);
        slideAdapter.setDuration(1000);
        slideAdapter.setInterpolator(new OvershootInterpolator());
        slideAdapter.setFirstOnly(false);
        recyclerView.setAdapter(slideAdapter);
    }
}
