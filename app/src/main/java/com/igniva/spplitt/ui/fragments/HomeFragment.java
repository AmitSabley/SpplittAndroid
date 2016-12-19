package com.igniva.spplitt.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igniva.spplitt.App;
import com.igniva.spplitt.R;


/**
 * Created by igniva-php-08 on 17/5/16.
 */
public class HomeFragment extends BaseFragment {
    private static final String LOG_TAG = "HomeFragment" ;
    View view;

    @Override
    public void onResume() {
        super.onResume();
        Log.e("HomeFragment", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        return view;
    }

    @Override
    public void setUpLayouts() {

    }

    @Override
    public void setDataInViewLayouts() {

    }


}
