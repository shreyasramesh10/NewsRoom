package com.android.shreyas.newsroom.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.android.shreyas.newsroom.MainActivity;
import com.android.shreyas.newsroom.R;
import com.android.shreyas.newsroom.WebViewActivity;
import com.android.shreyas.newsroom.adapters.MyDataBaseAdapter;
import com.android.shreyas.newsroom.adapters.SearchRecyclerViewAdapter;
import com.android.shreyas.newsroom.models.NewsDataJson;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FlipInBottomXAnimator;

/**
 * Created by SHREYAS on 3/3/2016.
 */
public class SearchRecyclerViewFragment extends Fragment {

    private static final String ARG_NUMBER = "argNumber";
    String query = "";

    private final String API_SEARCH = "http://api.nytimes.com/svc/search/v2/articlesearch.json?q=";
    private final String API_END = "&sort=newest&hl=true&api-key=";
    private final String API_KEY_SEARCH = "c687de4f8987dd4c85c318cb00681c0d:0:74600818";
    String url = API_SEARCH+query+API_END+API_KEY_SEARCH;
    MyDataBaseAdapter myDataBaseAdapter;
    //String url3 = "http://api.nytimes.com/svc/search/v2/articlesearch.json?q=trump&sort=newest&hl=true&api-key=c687de4f8987dd4c85c318cb00681c0d%3A0%3A74600818";
    SearchRecyclerViewAdapter searchRecyclerViewAdapter;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    NewsDataJson newsData;
    //OnEachCardSelectedListenerinSearch mListener;


    public FragmentManager getFm() {
        return fm;
    }

    public void setFm(FragmentManager fm) {
        this.fm = fm;
    }

    private FragmentManager fm ;

//    public interface OnEachCardSelectedListenerinSearch{
//        void OnEachCardSelectedinSearch(int position, HashMap<String, ?> news);
//    }

    public SearchRecyclerViewFragment() {
    }

//    public RecyclerViewFragment(FragmentManager fm){
//        this.fm = fm;
//    }

    public static SearchRecyclerViewFragment newInstance(String query){

        SearchRecyclerViewFragment recyclerViewFragment = new SearchRecyclerViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NUMBER, query);
        recyclerViewFragment.setArguments(args);
        return recyclerViewFragment;

    }

    private void itemAnimation(){
        FlipInBottomXAnimator animator = new FlipInBottomXAnimator();
        animator.setAddDuration(300);
        animator.setRemoveDuration(300);
        mRecyclerView.setItemAnimator(animator);
    }

    private void adapterAnimation(){
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(searchRecyclerViewAdapter);
        SlideInBottomAnimationAdapter slideAdapter = new SlideInBottomAnimationAdapter(alphaAdapter);
        slideAdapter.setDuration(1000);
        slideAdapter.setInterpolator(new OvershootInterpolator());
        slideAdapter.setFirstOnly(false);
        mRecyclerView.setAdapter(slideAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDataBaseAdapter = new MyDataBaseAdapter(getContext());
        newsData = new NewsDataJson();
        //content = getArguments().getString("content");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView;

        rootView = inflater.inflate(R.layout.fragment_recycler_view,container,false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);

        query = (String) getArguments().get(ARG_NUMBER);
        query = query.replaceAll(" ", "%20");
        String url = API_SEARCH+query+API_END+API_KEY_SEARCH;
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(getActivity(), newsData.getNewsList());

        if(isNetworkAvailable()){
            RetrieveMPNewsTask rnews = new RetrieveMPNewsTask(searchRecyclerViewAdapter);
            rnews.execute(url);
        }

        itemAnimation();
        adapterAnimation();
        mRecyclerView.setAdapter(searchRecyclerViewAdapter);
        searchRecyclerViewAdapter.setOnCardClickListener(new SearchRecyclerViewAdapter.onCardClickListener() {
            @Override
            public void onCardClick(View view, int position) {
                HashMap<String, ?> news = (HashMap<String, ?>) newsData.getItem(position);
/*                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(Constants.NEWS_HASHMAP, news);
                startActivity(intent);*/
                //mListener.OnEachCardSelectedinSearch(position, news);
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("news", (String) news.get("url"));
                startActivity(intent);

            }

            @Override
            public void onCardLongClick(View view, int position) {

            }

            @Override
            public void onSaveIconClick(View view, int position) {
                HashMap<String, ?> news = (HashMap<String, ?>) newsData.getItem(position);
                String image_url = "http://www.freelandlittleleague.com/assets/no-image-available-bbdbbe501d2b08a157a21431bc7b49df2c6cf6d892cc3083114229876cd7d6f4.jpg";
                image_url = (String) news.get("icon");


                long id = myDataBaseAdapter.insertData((String) news.get("url"), (String) news.get("title"), (String) news.get("description"), image_url);
                if (id < 0) {
                    //Toast.makeText(getActivity(), "Unsuccessful: " + id, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Saved for Later in Bookmarks!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ((MainActivity)getActivity()).setActionBarTitle("Search Results");
        return rootView;
    }

    private class RetrieveMPNewsTask extends AsyncTask<String, Void, NewsDataJson> {
        HashMap<String, ?> news = new HashMap<>();
        private final WeakReference<SearchRecyclerViewAdapter> myRVAdapterWeakReference;

        protected RetrieveMPNewsTask(SearchRecyclerViewAdapter adapter) {
            myRVAdapterWeakReference = new WeakReference<SearchRecyclerViewAdapter>(adapter);

        }

        @Override
        protected NewsDataJson doInBackground(String... params) {
            NewsDataJson threadMovieData = new NewsDataJson();
            for(String url: params){
                threadMovieData.downloadNewsDataFromJson(url);
            }
            return threadMovieData;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(NewsDataJson result) {
            newsData.newsList.clear();
            for(int i=0;i<result.getSize();i++){
                newsData.newsList.add(result.newsList.get(i));
            }
            if(myRVAdapterWeakReference!=null){
                final SearchRecyclerViewAdapter adapter = myRVAdapterWeakReference.get();
                if(adapter!=null){
                    adapter.notifyDataSetChanged();
                }
            }
            }

    }

    public boolean isNetworkAvailable(){
        ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info!=null && info.isConnected()){
            return true;
        }
        return false;
    }
}
