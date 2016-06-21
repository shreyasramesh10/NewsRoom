package com.android.shreyas.newsroom.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.shreyas.newsroom.LoginActivity;
import com.android.shreyas.newsroom.MainActivity;
import com.android.shreyas.newsroom.R;
import com.android.shreyas.newsroom.models.User;
import com.firebase.client.Firebase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Firebase ref = new Firebase("https://popping-inferno-9534.firebaseio.com");
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    User user = new User();

    public MyProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static MyProfileFragment newInstance() {
        MyProfileFragment fragment = new MyProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu.findItem(R.id.action_logout)==null){
                inflater.inflate(R.menu.menu_profile,menu);
        }
        if(ref.getAuth()!=null){
            menu.getItem(1).setVisible(false);
        }
        else{
            menu.getItem(0).setVisible(false);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_logout:
                ref.unauth();
                Intent i = getContext().getPackageManager()
                        .getLaunchIntentForPackage( getContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().finish();
                startActivity(i);
                break;
            case R.id.action_login:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("activity","My Profile");
                startActivity(intent);
                break;
            default:
                ref.unauth();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_profile,container,false);
        String providerString="Not Logged In";
        if(ref.getAuth()!=null){
            user.setUserName((String) ref.getAuth().getProviderData().get("displayName"));
            user.setEmail((String) ref.getAuth().getProviderData().get("email"));
            user.setDisplayImageURL((String) ref.getAuth().getProviderData().get("profileImageURL"));
            providerString = ref.getAuth().getProvider().toUpperCase();
        }

        CircleImageView dp = (CircleImageView) rootView.findViewById(R.id.dp_profile);
        TextView name = (TextView)rootView.findViewById(R.id.name);
        TextView email = (TextView)rootView.findViewById(R.id.email);

        TextView provider  = (TextView) rootView.findViewById(R.id.provider);
        provider.setText(providerString);
        name.setText(user.getUserName());
        email.setText(user.getEmail());
        Picasso.with(getContext()).load(user.getDisplayImageURL())
                .placeholder(R.drawable.nophotoavailable)
                .into(dp);
        ((MainActivity) getActivity()).setActionBarTitle("Profile");
        return rootView;
    }

}
