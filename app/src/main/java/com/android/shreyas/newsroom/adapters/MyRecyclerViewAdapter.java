package com.android.shreyas.newsroom.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.shreyas.newsroom.R;
import com.android.shreyas.newsroom.models.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by SHREYAS on 4/9/2016.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private Context myContext;
    private List<Result> myDataset;
    private onCardClickListener mCardClickListener;
    public static List<String> ImageUrls_right = new ArrayList<>();

    public MyRecyclerViewAdapter(Context context, List<Result> dataSet) {
        myContext = context;
        myDataset = dataSet;
    }
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_card_view,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewAdapter.ViewHolder holder, int position) {
        Result result = myDataset.get(position);
        holder.vTitle.setText( Html.fromHtml(result.getTitle()).toString());
        holder.vDescription.setText( Html.fromHtml(result.getAbstract()).toString());

        //holder.vIcon.setTransitionName(result.getUrl());


        if(result.getMedia().size()==0 && result.getMultimedia().size()!=0) {
            Picasso.with(myContext).load(result.getMultimedia().get(1).getUrl())
                    .placeholder(R.drawable.nophotoavailable)
                    .into(holder.vIcon);
            ImageUrls_right.add(result.getMultimedia().get(1).getUrl());
        } else if (result.getMultimedia().size()==0 && result.getMedia().size()!=0) {
            Picasso.with(myContext).load(result.getMedia().get(0).getMediaMetadata().get(1).getUrl())
                    .placeholder(R.drawable.nophotoavailable)
                    .into(holder.vIcon);
            ImageUrls_right.add(result.getMedia().get(0).getMediaMetadata().get(1).getUrl());
        } else {
            holder.vIcon.setImageResource(R.drawable.nophotoavailable);
            ImageUrls_right.add("http://www.freelandlittleleague.com/assets/no-image-available-bbdbbe501d2b08a157a21431bc7b49df2c6cf6d892cc3083114229876cd7d6f4.jpg");
        }
    }

    @Override
    public int getItemCount() {
        return myDataset.size();
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
                        mCardClickListener.onCardClick(vIcon,getAdapterPosition());
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

    public interface onCardClickListener{
        void onCardClick(View view, int position);
//        void onCardLongClick(View view, int position);
        void onSaveIconClick(View view, int position);
    }

    public void setOnCardClickListener(final onCardClickListener mCardClickListener){
        this.mCardClickListener = mCardClickListener;
    }
}
