package com.android.shreyas.newsroom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.shreyas.newsroom.R;

import java.util.List;
import java.util.Map;

public class SettingsAdapter extends BaseAdapter {


    private final Context context;
    private final List<Map<String,?>> newsList;


    public SettingsAdapter(Context context, List<Map<String, ?>> newsList)
    {
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public int getCount() {
        return newsList.size();    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(position%2==0)
            return 0;
        else
            return 1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        long viewType = getItemId(position);
        if(convertView == null){
            holder = new ViewHolder();
            if(viewType==0)
                convertView = LayoutInflater.from(context).inflate(R.layout.fragment_settings, parent, false);
            else
                convertView = LayoutInflater.from(context).inflate(R.layout.fragment_settings_new, parent, false);

            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.body = (TextView) convertView.findViewById(R.id.body);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }


        Map<String,?> entry = newsList.get(position);
        holder.title.setText((String)entry.get("name"));
        holder.body.setText((String)entry.get("description"));

        return convertView;
    }

    static class ViewHolder{

        TextView title;
        TextView body;
    }

}
