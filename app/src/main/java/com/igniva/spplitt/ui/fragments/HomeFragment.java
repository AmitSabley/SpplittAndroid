package com.igniva.spplitt.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igniva.spplitt.R;


/**
 * Created by igniva-php-08 on 17/5/16.
 */
public class HomeFragment extends BaseFragment {
    View view;

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
