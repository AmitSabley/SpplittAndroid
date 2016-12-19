package com.igniva.spplitt.ui.activties;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.igniva.spplitt.App;
import com.igniva.spplitt.R;
import com.igniva.spplitt.ui.fragments.ViewAllAdsFragment;

/**
 * Created by igniva-php-08 on 23/5/16.
 */
public class ViewAdsActivity extends BaseActivity implements View.OnClickListener {
    private static final String LOG_TAG = "ViewAdsActivity";
    TextView mToolbarTvText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ads);
        try {
            mToolbarTvText = (TextView) findViewById(R.id.toolbar_tv_text);
            mToolbarTvText.setText(getResources().getString(R.string.view_ad));

            Intent in = getIntent();

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
    protected void onResume() {
        super.onResume();
        Log.e("ViewAdsActivity", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
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
                    App.getInstance().trackEvent(LOG_TAG, "Back Press", "View Ads Back Pressed");
                    onBackPressed();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
