package com.android.shreyas.newsroom.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.shreyas.newsroom.R;

import java.util.HashMap;

/**
 * Created by SHREYAS on 4/24/2016.
 */
public class DetailUserStoriesFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    HashMap<String,?> result;
    public DetailUserStoriesFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            result = (HashMap<String, ?>) getArguments().getSerializable(ARG_SECTION_NUMBER);
        }
    }
    public static DetailUserStoriesFragment newInstance(HashMap<String,?> story) {
        DetailUserStoriesFragment fragment = new DetailUserStoriesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SECTION_NUMBER, story);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView;
        rootView= inflater.inflate(R.layout.fragment_detail_user_stories, container, false);


        final ImageView movie_poster = (ImageView) rootView.findViewById(R.id.icon);
        final TextView movie_title = (TextView) rootView.findViewById(R.id.title);
        final TextView movie_desc = (TextView) rootView.findViewById(R.id.descrip);


        //if (getArguments() != null) {
          //  result = (HashMap) getArguments().getSerializable(ARG_SECTION_NUMBER);
            String image = (String) result.get("image");
        if(image!=null && !image.isEmpty()){
            byte[] imageAsBytes = Base64.decode(image, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            movie_poster.setImageBitmap(bmp);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            movie_poster.setTransitionName((String) result.get("name"));
        }
        movie_title.setText((String)result.get("name"));
            movie_desc.setText((String)result.get("description"));
       // }
        return rootView;
    }

}

