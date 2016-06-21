package com.android.shreyas.newsroom.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.shreyas.newsroom.DividerItemDecoration;
import com.android.shreyas.newsroom.MainActivity;
import com.android.shreyas.newsroom.R;
import com.android.shreyas.newsroom.adapters.WeatherDetailAdapter;
import com.android.shreyas.newsroom.models.weathermodel.Weather;
import com.android.shreyas.newsroom.models.weathermodel.WeatherDataJson;
import com.baoyz.widget.PullRefreshLayout;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by SHREYAS on 2/14/2016.
 */
public class WeatherDetailFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    WeatherDataJson weatherDataJson =new WeatherDataJson();

    PullRefreshLayout layout;
    private WeatherDetailAdapter mRecyclerViewAdapter;
    private static final String ARG_SECTION_NUMBER = "section_number";


    public WeatherDetailFragment() {
        // Required empty public constructor
    }

    public static WeatherDetailFragment newInstance(HashMap<String, ?> sectionNumber) {
        WeatherDetailFragment fragment = new WeatherDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public static WeatherDetailFragment newInstance() {
        WeatherDetailFragment fragment = new WeatherDetailFragment();
        return fragment;
    }

    public interface onRefreshListenerWeather{
        void onRefreshCallWeather(PullRefreshLayout layout);
    }
    onRefreshListenerWeather mRefreshListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        weatherDataJson = new WeatherDataJson();
    }
    public boolean isNetworkAvailable(){
        ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info!=null && info.isConnected()){
            return true;
        }
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mRefreshListener = (onRefreshListenerWeather)getContext();
        }
        catch(ClassCastException exception){
            throw new ClassCastException(context.toString()
                    + " must implement the Listener");
        }

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        final View rootView;
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        weatherDataJson = new WeatherDataJson();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerViewAdapter = new WeatherDetailAdapter(getActivity(), weatherDataJson.getWeatherList());


        String movieUrl = "http://api.wunderground.com/api/027d1740ac4b4e92/forecast10day/q/"+ Weather.lat+","+Weather.lon+".json";
        if(isNetworkAvailable()) {
            MyDownloadWeatherDataCard myDownloadMovieDataCard = new MyDownloadWeatherDataCard(mRecyclerViewAdapter);
            myDownloadMovieDataCard.execute(movieUrl);
        }

        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider));
//        itemAnimation();
//        adapterAnimation();

        layout = (PullRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.setRefreshing(true);
                mRefreshListener.onRefreshCallWeather(layout);
            }
        });
        layout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        layout.setRefreshing(false);
        ((MainActivity) getActivity()).setActionBarTitle("10 Day Forecast");
        return rootView;
    }
//    private void itemAnimation(){
//        FlipInBottomXAnimator animator = new FlipInBottomXAnimator();
//        animator.setAddDuration(300);
//        animator.setRemoveDuration(300);
//        mRecyclerView.setItemAnimator(animator);
//    }
//
//    private void adapterAnimation(){
//        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mRecyclerViewAdapter);
//        SlideInBottomAnimationAdapter slideAdapter = new SlideInBottomAnimationAdapter(alphaAdapter);
//        slideAdapter.setDuration(1000);
//        slideAdapter.setInterpolator(new OvershootInterpolator());
//        slideAdapter.setFirstOnly(false);
//        mRecyclerView.setAdapter(slideAdapter);
//    }


    private class MyDownloadWeatherDataCard extends AsyncTask<String, Void, WeatherDataJson> {
        private final WeakReference<WeatherDetailAdapter> adapterWeakReference;
        public MyDownloadWeatherDataCard(WeatherDetailAdapter adapter) {
            adapterWeakReference = new WeakReference<WeatherDetailAdapter>(adapter);
        }

        @Override
        protected WeatherDataJson doInBackground(String... params) {

            WeatherDataJson threadData = new WeatherDataJson();
            for(String url: params){
                threadData.downloadWeatherDataFromJson(url);
            }
            return threadData;
        }

        @Override
        protected void onPostExecute(WeatherDataJson s) {
            weatherDataJson.weatherList.clear();
            for(int i=0;i<s.getSize();i++){
                weatherDataJson.weatherList.add(s.weatherList.get(i));
            }
            if(adapterWeakReference!=null){
                final WeatherDetailAdapter adapter = adapterWeakReference.get();
                if(adapter!=null){
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
