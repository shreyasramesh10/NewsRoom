package com.android.shreyas.newsroom.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.shreyas.newsroom.MyUtility;
import com.android.shreyas.newsroom.R;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

/**
 * Created by SHREYAS on 2/14/2016.
 */
public class WeatherDetailAdapter extends RecyclerView.Adapter<WeatherDetailAdapter.ViewHolder> {
    private List<Map<String,?>> mDataSet;
    private Context mContext;
    private LruCache<String, Bitmap> imgMemoryCache;


    public WeatherDetailAdapter(Context myContext, List<Map<String, ?>> mDataSet){
        this.mContext=myContext;
        this.mDataSet=mDataSet;
    }

    @Override
    public WeatherDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_weather_detail_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(WeatherDetailAdapter.ViewHolder holder, int position) {
        Map<String, ?> movie = mDataSet.get(position);

        holder.vDate.setText((String) movie.get("date"));
        holder.vDescription.setText((String) movie.get("description"));

        holder.vMaxtemp.setText((String)movie.get("tempc_high") +
                (char) 0x00B0 + "C" + " (" + movie.get("tempf_high") + (char) 0x00B0 + "F" + ")");
        holder.vMinTemp.setText((String)movie.get("tempc_low") + (char) 0x00B0+ "C" + " (" + movie.get("tempf_low") + (char) 0x00B0+ "F"+")");

        //load image to vIcon
        String imgUrl = (String) movie.get("icon_url");
        Picasso.with(mContext).load(imgUrl).resize(120,120).into(holder.vIcon);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView vIcon;
        public TextView vDate;
        public TextView vDescription;
        public TextView vMaxtemp;
        public TextView vMinTemp;

        public ViewHolder(View itemView) {

            super(itemView);
            vIcon = (ImageView) itemView.findViewById(R.id.icon);
            vDate = (TextView) itemView.findViewById(R.id.date);
            vDescription = (TextView) itemView.findViewById(R.id.description);
            vMaxtemp = (TextView) itemView.findViewById(R.id.maxTemp);
            vMinTemp = (TextView) itemView.findViewById(R.id.minTemp);
        }
    }
}
