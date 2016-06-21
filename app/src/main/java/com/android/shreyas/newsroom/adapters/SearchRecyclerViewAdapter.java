package com.android.shreyas.newsroom.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.shreyas.newsroom.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * Created by SHREYAS on 3/3/2016.
 */
public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {
    private List<Map<String,?>> mDataset;
    private Context context;
    private onCardClickListener mCardClickListener;
    private LruCache<String, Bitmap> imgMemoryCache;

    public SearchRecyclerViewAdapter(Context context, List<Map<String, ?>> mDataset){
        this.context = context;
        this.mDataset=mDataset;

    }
    @Override
    public SearchRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_card_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(SearchRecyclerViewAdapter.ViewHolder holder, int position) {
        Map<String, ?> news = mDataset.get(position);
        holder.vTitle.setText((String)news.get("title"));
        holder.vDescription.setText((String) news.get("description"));
        //holder.vIcon.setImageBitmap((Bitmap)news.get("icon"));

        String imgUrl = (String) news.get("icon");

        Picasso.with(context).load(imgUrl).into(holder.vIcon);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
                        mCardClickListener.onSaveIconClick(saveIcon, getAdapterPosition());
                    }
                }
            });
        }


    }

    public interface onCardClickListener{
        void onCardClick(View view, int position);
        void onCardLongClick(View view, int position);
        void onSaveIconClick(View view, int position);
    }

    public void setOnCardClickListener(final onCardClickListener mCardClickListener){
        this.mCardClickListener = mCardClickListener;
    }
}
