package com.android.shreyas.newsroom.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.shreyas.newsroom.R;
import com.android.shreyas.newsroom.models.Result;
import com.android.shreyas.newsroom.models.weathermodel.CurrentObservation;
import com.android.shreyas.newsroom.models.weathermodel.DisplayLocation;
import com.android.shreyas.newsroom.models.weathermodel.Main;
import com.android.shreyas.newsroom.models.weathermodel.Weather;
import com.android.shreyas.newsroom.models.weathermodel.WeatherData;
import com.android.shreyas.newsroom.models.weathermodel.Wind;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by SHREYAS on 4/9/2016.
 */
public class MyRecyclerViewHomeAdapter extends RecyclerView.Adapter<MyRecyclerViewHomeAdapter.MainViewHolder> {
    private Context myContext;
    private List<Result> myDataset;
    private List<Map<String,?>> myWeatherSet;
    private onCardClickListener mCardClickListener;
    private static final int TYPE_PROFILE = 1;
    private static final int TYPE_OPTION_MENU = 2;
    public static List<String> ImageUrls_left = new ArrayList<>();
    SharedPreferences mSettings;


    public MyRecyclerViewHomeAdapter(Context context, List<Result> dataSet, List<Map<String, ?>> mDataSet, SharedPreferences mSettings) {
        myContext = context;
        myDataset = dataSet;
        myWeatherSet = mDataSet;
        this.mSettings = mSettings;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0? TYPE_PROFILE : TYPE_OPTION_MENU);
    }

    @Override
    public MyRecyclerViewHomeAdapter.MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_PROFILE:
                return new WeatherViewHolder(LayoutInflater.from(myContext).inflate(R.layout.fragment_weather_card, parent, false));
            case TYPE_OPTION_MENU:
                return new ViewHolder(LayoutInflater.from(myContext).inflate(R.layout.fragment_card_view, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewHomeAdapter.MainViewHolder holder, int position) {

        if(holder.getItemViewType() == TYPE_PROFILE){
            WeatherViewHolder mholder = (WeatherViewHolder) holder;
            if(myWeatherSet.size()>0){
                Map<String, ?> movie = myWeatherSet.get(position);
                final String units = mSettings.getString("units", "imperial");

                mholder.vLocation.setText((String)movie.get("location"));

                mholder.vDescription.setText((String) movie.get("description"));
                Picasso.with(myContext)
                        .load((String)movie.get("icon_url"))
                        .resize(300, 300)
                        .centerCrop()
                        .into(mholder.vIcon);
                mholder.vUpdated.setText((String)movie.get("date"));

                if(units.equals("metric")){
                    mholder.vTemp.setText((String) movie.get("temp_c") +(char) 0x00B0 + "C" );
                    mholder.vFeelsLike.setText(myContext.getResources().getString(R.string.feels_likec,(String) movie.get("feelslike_c")));
                    mholder.vWindSpeed.setText(myContext.getResources().getString(R.string.wind_metric,(String) movie.get("wind_metric")));

                }
                if(units.equals("imperial")) {
                    mholder.vTemp.setText((String) movie.get("temp_f")+(char) 0x00B0 + "F");

                    mholder.vFeelsLike.setText(myContext.getResources().getString(R.string.feels_like,(String) movie.get("feelslike_f")));
                    mholder.vWindSpeed.setText(myContext.getResources().getString(R.string.wind_imperial,(String) movie.get("wind_imperial")));
                }
            }
            else{
                //Snackbar.make(holder.itemView, "GPS Location Off, Turn on and Refresh", Snackbar.LENGTH_SHORT).show();
                //Toast.makeText(myContext,"GPS Location Off, Turn on and Refresh",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            ViewHolder holder1 = (ViewHolder) holder;
            Result result = myDataset.get(position-1);
            holder1.vTitle.setText(Html.fromHtml(result.getTitle()).toString());
            holder1.vDescription.setText(Html.fromHtml(result.getAbstract()).toString());

            if(result.getMedia().size()==0 && result.getMultimedia().size()!=0) {
                Picasso.with(myContext).load(result.getMultimedia().get(1).getUrl())
                        .placeholder(R.drawable.nophotoavailable)
                        .into(holder1.vIcon);
                ImageUrls_left.add(result.getMultimedia().get(1).getUrl());
            } else if (result.getMultimedia().size()==0 && result.getMedia().size()!=0) {
                Picasso.with(myContext).load(result.getMedia().get(0).getMediaMetadata().get(1).getUrl())
                        .placeholder(R.drawable.nophotoavailable)
                        .into(holder1.vIcon);
                ImageUrls_left.add(result.getMedia().get(0).getMediaMetadata().get(1).getUrl());
            } else {
                holder1.vIcon.setImageResource(R.drawable.nophotoavailable);
                ImageUrls_left.add("http://www.freelandlittleleague.com/assets/no-image-available-bbdbbe501d2b08a157a21431bc7b49df2c6cf6d892cc3083114229876cd7d6f4.jpg");
            }
        }

    }



    @Override
    public int getItemCount() {
        return myDataset.size()+1;
    }

    public class ViewHolder extends MainViewHolder {
        public TextView vTitle;
        public TextView vDescription;
        public ImageView vIcon;
        public ImageView saveIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            vTitle = (TextView) itemView.findViewById(R.id.newsTitleCard);
            vDescription = (TextView) itemView.findViewById(R.id.descriptionCard);
            vIcon = (ImageView) itemView.findViewById(R.id.newsIconCard);
            saveIcon = (ImageView) itemView.findViewById(R.id.selectionCard);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCardClickListener!=null){
                        mCardClickListener.onCardClick(v,getAdapterPosition());
                    }
                }
            });

            saveIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCardClickListener!=null){
                        mCardClickListener.onSaveIconClick(saveIcon,getAdapterPosition());
                    }
                }
            });
        }
    }

    public class WeatherViewHolder extends MainViewHolder {
        public TextView vLocation;
        public TextView vDescription;
        public TextView vTemp;
        public TextView vWindSpeed;
        public ImageView vIcon;
        public ImageView moreOptionsIcon;
        public TextView vFeelsLike;
        public TextView vUpdated;
        public WeatherViewHolder(View itemView) {
            super(itemView);
            vLocation = (TextView) itemView.findViewById(R.id.locName);
            vTemp = (TextView) itemView.findViewById(R.id.temp);
            vWindSpeed = (TextView) itemView.findViewById(R.id.windSpeed);
            vDescription = (TextView) itemView.findViewById(R.id.descriptionWeather);
            vIcon = (ImageView) itemView.findViewById(R.id.iconWeather);
            moreOptionsIcon = (ImageView) itemView.findViewById(R.id.moreOptions);
            vFeelsLike = (TextView) itemView.findViewById(R.id.feelsLikeTemp);
            vUpdated = (TextView) itemView.findViewById(R.id.updated);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCardClickListener!=null){
                        mCardClickListener.onCardClick(v,getAdapterPosition());
                    }
                }
            });

            moreOptionsIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCardClickListener!=null){
                        mCardClickListener.onSaveIconClick(v,getAdapterPosition());
                    }
                }
            });
        }
    }


    public interface onCardClickListener{
        void onCardClick(View view, int position);
        void onSaveIconClick(View view, int position);
    }

    public void setOnCardClickListener(final onCardClickListener mCardClickListener){
        this.mCardClickListener = mCardClickListener;
    }

    public class MainViewHolder extends RecyclerView.ViewHolder{

        public MainViewHolder(View itemView) {
            super(itemView);
        }
    }
}
