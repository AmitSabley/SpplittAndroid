package com.igniva.spplitt.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
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
        view=inflater.inflate(R.layout.fragment_contact_us,container,false);
//        view = inflater.inflate(R.layout.fragment_home, container, false);
//        NavigationView navigationView = (NavigationView) MainActivity.main.findViewById(R.id.nav_view);
//        ((MainActivity)getActivity()).onNavigationItemSelected(navigationView.getMenu().getItem(4));
        return view;
    }
    @Override
    public void setUpLayouts() {

    }

    @Override
    public void setDataInViewLayouts() {

    }


}
