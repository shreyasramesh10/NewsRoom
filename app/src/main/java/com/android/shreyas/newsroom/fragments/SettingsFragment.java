package com.android.shreyas.newsroom.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.shreyas.newsroom.R;
import com.android.shreyas.newsroom.adapters.SettingsAdapter;
import com.android.shreyas.newsroom.models.AcknowledgmentData;

import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private FlipView flipView;
    private SettingsAdapter settingsAdapter;
    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings2,container,false);
        flipView = (FlipView) rootView.findViewById(R.id.flip_view);
        settingsAdapter = new SettingsAdapter(getActivity(),(new AcknowledgmentData().getMoviesList()));
        flipView.setAdapter(settingsAdapter);
        flipView.peakNext(false);
        flipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
        return rootView;
    }

}
