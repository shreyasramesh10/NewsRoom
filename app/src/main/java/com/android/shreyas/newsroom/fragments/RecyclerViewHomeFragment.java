package com.android.shreyas.newsroom.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.shreyas.newsroom.DetailViewActivity;
import com.android.shreyas.newsroom.MainActivity;
import com.android.shreyas.newsroom.R;
import com.android.shreyas.newsroom.adapters.MyDataBaseAdapter;
import com.android.shreyas.newsroom.adapters.MyRecyclerViewHomeAdapter;
import com.android.shreyas.newsroom.apimanager.GetNewsApi;
import com.android.shreyas.newsroom.models.NewsData;
import com.android.shreyas.newsroom.models.Result;
import com.android.shreyas.newsroom.models.weathermodel.Weather;
import com.android.shreyas.newsroom.models.weathermodel.WeatherData;
import com.android.shreyas.newsroom.models.weathermodel.WeatherDataJson;
import com.baoyz.widget.PullRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
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
 * Use the {@link RecyclerViewHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecyclerViewHomeFragment extends Fragment {
    protected GetNewsApi getNewsApi;
//    protected GetNewsApi getWeatherApi;
//    protected GetNewsApi getWeatherApiNew;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    public MyRecyclerViewHomeAdapter myRecyclerViewHomeAdapter;

    PullRefreshLayout layout = null;

    MyDataBaseAdapter myDataBaseAdapter;
    WeatherDataJson weatherDataJson;

    Call<NewsData> newsDataCall;
    NewsData newsData = new NewsData();


    List<Result> resultsList = newsData.getResults();

    public String stringLatitude;
    public String stringLongitude;

    private static final String ARG_LAT = "latitude";
    private static final String ARG_LON = "longitude";

    public interface OnEachCardSelectedonHomeListener{
        void OnEachCardSelectedonHome();
        void OnSearch(String query);
    }
    public interface onRefreshListenerCustom{
        void onRefreshCallFromHome(PullRefreshLayout layout);
    }
    onRefreshListenerCustom mRefreshListener;
    OnEachCardSelectedonHomeListener mListener;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    private void itemAnimation(){
        FlipInBottomXAnimator animator = new FlipInBottomXAnimator();
        animator.setAddDuration(300);
        animator.setRemoveDuration(300);
        recyclerView.setItemAnimator(animator);
    }

    private void adapterAnimation(){
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(myRecyclerViewHomeAdapter);
        SlideInBottomAnimationAdapter slideAdapter = new SlideInBottomAnimationAdapter(alphaAdapter);
        slideAdapter.setDuration(1000);
        slideAdapter.setInterpolator(new OvershootInterpolator());
        slideAdapter.setFirstOnly(false);
        recyclerView.setAdapter(slideAdapter);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mRefreshListener = (onRefreshListenerCustom)getContext();
            mListener =  (OnEachCardSelectedonHomeListener)getContext();
        }
        catch(ClassCastException exception){
            throw new ClassCastException(context.toString()
                    + " must implement the Listener");
        }

    }

    public RecyclerViewHomeFragment() {
        // Required empty public constructor
    }

    public static RecyclerViewHomeFragment newInstance(String stringLatitude,String stringLongitude) {
        RecyclerViewHomeFragment fragment = new RecyclerViewHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LAT, stringLatitude);
        args.putString(ARG_LON, stringLongitude);
        fragment.setArguments(args);
        return fragment;
    }

    public static RecyclerViewHomeFragment newInstance() {
        RecyclerViewHomeFragment fragment = new RecyclerViewHomeFragment();
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu.findItem(R.id.action_search)==null)
            inflater.inflate(R.menu.menu_search_task,menu);

        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if(search!=null){
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    mListener.OnSearch(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        weatherDataJson = new WeatherDataJson();
        setRetainInstance(true);
        myDataBaseAdapter = new MyDataBaseAdapter(getContext());
        if (getArguments() != null) {
            stringLatitude = getArguments().getString(ARG_LAT);
            stringLongitude = getArguments().getString(ARG_LON);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_recycler_view,container,false);

        final SharedPreferences mSettings = getActivity().getSharedPreferences("Settings", 0);
///////////////////news////////////////////////
        recyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        myRecyclerViewHomeAdapter = new MyRecyclerViewHomeAdapter
                (getActivity(), resultsList,weatherDataJson.getWeatherList(),mSettings);

        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.nytimes.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        getNewsApi =retrofit.create(GetNewsApi.class);

        newsDataCall = getNewsApi.GetTopStories("home.json", "9c88747a6be8832be915c61a06281c2f:5:74600818");
        //to check the resulting url
        String s = getNewsApi.GetTopStories("home.json", "9c88747a6be8832be915c61a06281c2f:5:74600818").request().url().toString();
        ((MainActivity) getActivity()).setActionBarTitle("Home");

        newsDataCall.enqueue(new Callback<NewsData>() {

            @Override
            public void onResponse(Call<NewsData> call, Response<NewsData> response) {
                final WeakReference<MyRecyclerViewHomeAdapter> adapterWeakReference = new WeakReference<>(myRecyclerViewHomeAdapter);

                newsData = response.body();
                List<Result> resultsListLocal = new ArrayList<>();
                if(newsData!=null){
                    resultsListLocal = newsData.getResults();
                }


                resultsList.clear();
                for(int i=0;i<resultsListLocal.size();i++){
                    resultsList.add(resultsListLocal.get(i));
                }
                if(adapterWeakReference!=null){
                    final MyRecyclerViewHomeAdapter adapter = adapterWeakReference.get();
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
///////////////////news///////////////////

        layout = (PullRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.setRefreshing(true);
                mRefreshListener.onRefreshCallFromHome(layout);
            }
        });
        layout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        layout.setRefreshing(false);
        itemAnimation();
        adapterAnimation();

        recyclerView.setAdapter(myRecyclerViewHomeAdapter);
        myRecyclerViewHomeAdapter.setOnCardClickListener(new MyRecyclerViewHomeAdapter.onCardClickListener() {
            @Override
            public void onCardClick(View view, int position) {
                if (position == 0) {
                    mListener.OnEachCardSelectedonHome();
                } else {
                    Result result = resultsList.get(position - 1);
                    Intent intent = new Intent(getActivity(), DetailViewActivity.class);
                    intent.putExtra("result", result);
                    startActivity(intent);
                }

            }

            @Override
            public void onSaveIconClick(View view, int position) {
                if (position == 0) {
                    PopupMenu popupmenu = new PopupMenu(getActivity(), view);
                    MenuInflater inflater = popupmenu.getMenuInflater();
                    inflater.inflate(R.menu.menu_popup, popupmenu.getMenu());
                    popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_refresh:
                                    String weather_url = "http://api.wunderground.com/api/027d1740ac4b4e92/conditions/q/"+ Weather.lat+","+Weather.lon+".json";
                                    MyDownloadWeatherData myDownloadMovieDataCard = new MyDownloadWeatherData(myRecyclerViewHomeAdapter);
                                    myDownloadMovieDataCard.execute(weather_url);
                                    return true;
                                case R.id.action_metric:
                                    SharedPreferences.Editor editor = mSettings.edit();
                                    editor.putString("units", "metric");
                                    editor.commit();
                                    String weather_url2 = "http://api.wunderground.com/api/027d1740ac4b4e92/conditions/q/"+ Weather.lat+","+Weather.lon+".json";
                                    MyDownloadWeatherData myDownloadMovieDataCardMetric = new MyDownloadWeatherData(myRecyclerViewHomeAdapter);
                                    myDownloadMovieDataCardMetric.execute(weather_url2);
                                    return true;
                                case R.id.action_imperial:
                                    SharedPreferences.Editor editor2 = mSettings.edit();
                                    editor2.putString("units", "imperial");
                                    editor2.commit();
                                    String weather_url3 = "http://api.wunderground.com/api/027d1740ac4b4e92/conditions/q/"+ Weather.lat+","+Weather.lon+".json";
                                    MyDownloadWeatherData myDownloadMovieDataCardImperial = new MyDownloadWeatherData(myRecyclerViewHomeAdapter);
                                    myDownloadMovieDataCardImperial.execute(weather_url3);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupmenu.show();
                }
                if (position > 0) {
                    Result result = resultsList.get(position - 1);
                    String image_url = "http://www.freelandlittleleague.com/assets/no-image-available-bbdbbe501d2b08a157a21431bc7b49df2c6cf6d892cc3083114229876cd7d6f4.jpg";
                    if (result.getMedia().size() == 0 && result.getMultimedia().size() != 0) {
                        image_url = result.getMultimedia().get(1).getUrl();
                    }
                    if (result.getMultimedia().size() == 0 && result.getMedia().size() != 0) {
                        image_url = result.getMedia().get(0).getMediaMetadata().get(1).getUrl();
                    }


                    long id = myDataBaseAdapter.insertData(result.getUrl(), result.getTitle(), result.getAbstract(), image_url);
                    if (id < 0) {
                       //Toast.makeText(getActivity(), "Unsuccessful: " + id, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Saved for Later in Bookmarks!", Toast.LENGTH_SHORT).show();
                    }

                    result.setIsSaved(true);
                    ((ImageView) view).setImageResource(R.drawable.bookmarksave);
                }

            }
        });
        String weather_url = "http://api.wunderground.com/api/027d1740ac4b4e92/conditions/q/"+ Weather.lat+","+Weather.lon+".json";
        if(isNetworkAvailable()) {
        MyDownloadWeatherData myDownloadMovieDataCard = new MyDownloadWeatherData(myRecyclerViewHomeAdapter);
        myDownloadMovieDataCard.execute(weather_url);
        }
        return rootView;
    }

    public boolean isNetworkAvailable(){
        ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info!=null && info.isConnected()){
            return true;
        }
        return false;
    }
    private class MyDownloadWeatherData extends AsyncTask<String, Void, WeatherDataJson> {
        private final WeakReference<MyRecyclerViewHomeAdapter> adapterWeakReference;
        public MyDownloadWeatherData(MyRecyclerViewHomeAdapter adapter) {
            adapterWeakReference = new WeakReference<MyRecyclerViewHomeAdapter>(adapter);
        }

        @Override
        protected WeatherDataJson doInBackground(String... params) {

            WeatherDataJson threadData = new WeatherDataJson();
            for(String url: params){
                threadData.downloadWeatherConditionsFromJson(url);
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
                final MyRecyclerViewHomeAdapter adapter = adapterWeakReference.get();
                if(adapter!=null){
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

}
