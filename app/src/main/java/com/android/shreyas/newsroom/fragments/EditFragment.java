package com.android.shreyas.newsroom.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.shreyas.newsroom.R;
import com.android.shreyas.newsroom.UserStoriesActivity;
import com.android.shreyas.newsroom.models.Stories;
import com.firebase.client.Firebase;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    EditText title;
    EditText body;
    // TODO: Rename and change types of parameters
    HashMap<String,?> story;


    public EditFragment() {
        // Required empty public constructor
    }

    public static EditFragment newInstance(HashMap<String,?> story) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, story);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            story = (HashMap<String, ?>) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_edit,container,false);
        title = (EditText) rootView.findViewById(R.id.titleEdit);
        body = (EditText) rootView.findViewById(R.id.bodyEdit);
        title.setText((String)story.get("name"));
        body.setText((String) story.get("description"));
        Button upload = (Button) rootView.findViewById(R.id.upload);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase listNameRef = new Firebase("https://popping-inferno-9534.firebaseio.com/stories");
                listNameRef = listNameRef.child(listNameRef.getAuth().getUid());
                String image = (String) story.get("image");
                if(image!=null && !image.isEmpty()){
                    listNameRef.child((String) story.get("key"))
                            .setValue(new Stories(body.getText().toString(), (String)story.get("image"),title.getText().toString()));
                    Intent intent = new Intent(getActivity(),UserStoriesActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                else {
                    listNameRef.child((String) story.get("key"))
                            .setValue(new Stories(body.getText().toString(), title.getText().toString()));
                    Intent intent = new Intent(getActivity(), UserStoriesActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        return rootView;
    }

}
