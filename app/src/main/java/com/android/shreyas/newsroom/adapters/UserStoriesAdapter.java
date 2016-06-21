package com.android.shreyas.newsroom.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.shreyas.newsroom.R;

import java.util.List;
import java.util.Map;

public class UserStoriesAdapter extends RecyclerView.Adapter<UserStoriesAdapter.ViewHolder> {

    private List<Map<String,?>> mDataSet;
    private Context mContext ;


    OnItemClickListener myItemClickListener;


    //Constructor
    public UserStoriesAdapter(Context myContext, List<Map<String, ?>> myDataSet)  {
        mContext = myContext;
        mDataSet = myDataSet;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView vIcon;
        public TextView name;
        public TextView description;
        public ImageView menuIcon;

        public ViewHolder(View v) {
            super(v);
            vIcon = (ImageView) v.findViewById(R.id.newsIconCard);
            name = (TextView) v.findViewById(R.id.newsTitleCard);
            description = (TextView) v.findViewById(R.id.descriptionCard);
            menuIcon = (ImageView) v.findViewById(R.id.selectionCard);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myItemClickListener != null) {
                        myItemClickListener.onItemClick(vIcon, getAdapterPosition());
                    }
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (myItemClickListener != null) {
                        myItemClickListener.onItemLongClick(v, getAdapterPosition());
                    }
                    return true;
                }
            });
            if(menuIcon != null) {
                menuIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(myItemClickListener !=null) {
                            myItemClickListener.onOverflowMenuClick(v,getAdapterPosition());
                        }
                    }
                });
            }

        }

        public void bindMovieData(Map<String,?> movie) {
            if(name !=null) name.setText((String) movie.get("name"));

            if(description !=null) description.setText((String) movie.get("description"));


            String image = (String) movie.get("image");
            if(image != null && !image.isEmpty()) {
                if (vIcon != null) {
                    byte[] imageAsBytes = Base64.decode(image, Base64.DEFAULT);
                    Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                    vIcon.setImageBitmap(bmp);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        vIcon.setTransitionName((String) movie.get("name"));
                    }
                }
            }
        }
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_card_stories,parent,false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Map<String,?> movie =  mDataSet.get(position);
        holder.bindMovieData(movie);
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
        public void onOverflowMenuClick(View view, int position);

    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.myItemClickListener = mItemClickListener;
    }

}
