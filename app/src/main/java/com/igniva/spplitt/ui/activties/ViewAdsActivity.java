package com.igniva.spplitt.ui.activties;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.AdsListPojo;
import com.igniva.spplitt.model.CategoriesListPojo;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ResponsePojo;


import com.igniva.spplitt.ui.fragments.ViewAllActiveAdsFragment;
import com.igniva.spplitt.ui.fragments.ViewAllAdsFragment;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igniva-php-08 on 23/5/16.
 */
public class ViewAdsActivity extends BaseActivity implements View.OnClickListener {
    TextView mToolbarTvText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ads);
        try {
            mToolbarTvText = (TextView) findViewById(R.id.toolbar_tv_text);
            mToolbarTvText.setText(getResources().getString(R.string.view_ad));

            Intent in=getIntent();

            Fragment fragment = ViewAllAdsFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("cat_id", in.getStringExtra("cat_id"));
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_main_content_view_ads, fragment);
            fragmentTransaction.commitAllowingStateLoss();

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setUpLayouts() {

    }

    @Override
    public void setDataInViewLayouts() {
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.toolbar_btn_back:
                    onBackPressed();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
