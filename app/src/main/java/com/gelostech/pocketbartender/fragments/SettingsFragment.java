package com.gelostech.pocketbartender.fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gelostech.pocketbartender.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    private View view;
    private final String font1 = "fonts/COCKB___.TTF";
    private final String font2 = "fonts/COCKTAIL.TTF";

    @BindView(R.id.settings_toolbar) Toolbar toolbar;
    @BindView(R.id.settings_title) TextView settingsTitle;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view == null){
            view = inflater.inflate(R.layout.fragment_settings, container, false);
            ButterKnife.bind(this, view);


            initViews();
        }

        return view;
    }

    private void initViews(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(null);
        settingsTitle.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), font1));

        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.preferences_holder, new PreferencesFragment())
                .commit();
    }


}
