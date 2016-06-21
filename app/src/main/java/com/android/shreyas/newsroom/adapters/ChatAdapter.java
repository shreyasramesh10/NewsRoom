package com.android.shreyas.newsroom.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.shreyas.newsroom.R;
import com.firebase.client.Firebase;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * Created by bssan_000 on 6/16/2015.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    Firebase mref = new Firebase("https://popping-inferno-9534.firebaseio.com");
    private List<Map<String,?>> mDataSet;
    private Context mContext ;


    //OnItemClickListener myItemClickListener;


    //Constructor
    public ChatAdapter(Context myContext, List<Map<String, ?>> myDataSet)  {
        mContext = myContext;
        mDataSet = myDataSet;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView vIcon;
        TextView name;
        TextView description;

        public ViewHolder(View v) {
            super(v);
            vIcon = (ImageView) v.findViewById(R.id.icon);
            name = (TextView) v.findViewById(R.id.nameCard);
            description = (TextView) v.findViewById(R.id.msgCard);
//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (myItemClickListener != null) {
//                        myItemClickListener.onItemClick(v, getAdapterPosition());
//                    }
//                }
//            });
//            v.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    if (myItemClickListener != null) {
//                        myItemClickListener.onItemLongClick(v, getAdapterPosition());
//                    }
//                    return true;
//                }
//            });
//            if(menuIcon != null) {
//                menuIcon.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(myItemClickListener !=null) {
//                            myItemClickListener.onOverflowMenuClick(v,getAdapterPosition());
//                        }
//                    }
//                });
//            }

        }

        public void bindMovieData(Map<String,?> chat) {
            if(name !=null) name.setText((String) chat.get("name"));

            if(description !=null) description.setText((String) chat.get("message"));
            if(vIcon!=null)
            Picasso.with(mContext).load((String) chat.get("url"))
                        .into(vIcon);
        }
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card_view,parent,false);
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card_view2,parent,false);
        }


        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {

        Map<String,?> chat =  mDataSet.get(position);
        String author = (String)chat.get("name");
        if(mref.getAuth().getProviderData().get("displayName").equals(author)){
            return 0;
        }
        else
            return 1;
        }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Map<String,?> chat =  mDataSet.get(position);
        holder.bindMovieData(chat);
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

//    public interface OnItemClickListener {
//        public void onItemClick(View view, int position);
//        public void onItemLongClick(View view, int position);
//        public void onOverflowMenuClick(View view, int position);
//
//    }
//
//    public void setOnItemClickListenerRRR(final OnItemClickListener mItemClickListener) {
//        this.myItemClickListener = mItemClickListener;
//    }

}
