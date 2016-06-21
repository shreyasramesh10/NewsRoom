package com.android.shreyas.newsroom.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.shreyas.newsroom.R;
import com.android.shreyas.newsroom.SimpleItemTouchHelperCallback;
import com.android.shreyas.newsroom.models.NewsDb;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by SHREYAS on 2/14/2016.
 */
public class DbRecyclerViewAdapter extends RecyclerView.Adapter<DbRecyclerViewAdapter.ViewHolder> implements SimpleItemTouchHelperCallback.ItemTouchHelperAdapter {
    private ArrayList<NewsDb> mDataSet;
    private Context mContext;
    private onCardClickListener mCardClickListener;
    MyDataBaseAdapter dataBaseAdapter;

    public DbRecyclerViewAdapter(Context myContext, ArrayList<NewsDb> mDataSet, MyDataBaseAdapter dataBaseAdapter){
        this.mContext=myContext;
        this.mDataSet=mDataSet;
        this.dataBaseAdapter = dataBaseAdapter;
    }

    @Override
    public DbRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_card_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(DbRecyclerViewAdapter.ViewHolder holder, int position) {
        NewsDb newsDb  = mDataSet.get(position);

        holder.vTitle.setText( Html.fromHtml(newsDb.getTitle()).toString());
        holder.vDescription.setText(Html.fromHtml(newsDb.getDescription()).toString());
        Picasso.with(mContext).load(newsDb.getImage_url())
                .placeholder(R.drawable.nophotoavailable)
                .error(R.drawable.nophotoavailable)
                .into(holder.vIcon);

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemDismiss(int position) {
        NewsDb newsDb = mDataSet.get(position);
        String url = newsDb.getUrl();
        mDataSet.remove(position);
        dataBaseAdapter.deleteRow(url);
        notifyItemRemoved(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView vIcon;
        public TextView vTitle;
        public TextView vDescription;
        public ImageView saveIcon;

        public ViewHolder(View itemView) {

            super(itemView);

            vTitle = (TextView) itemView.findViewById(R.id.newsTitleCard);
            vDescription = (TextView) itemView.findViewById(R.id.descriptionCard);
            vIcon = (ImageView) itemView.findViewById(R.id.newsIconCard);
            saveIcon =(ImageView) itemView.findViewById(R.id.selectionCard);

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
                        mCardClickListener.onSaveIconClick(v,getAdapterPosition());
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
