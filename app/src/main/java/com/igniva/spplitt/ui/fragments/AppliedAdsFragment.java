package com.igniva.spplitt.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.igniva.spplitt.R;
import com.igniva.spplitt.ui.activties.MainActivity;
import com.igniva.spplitt.ui.activties.ViewAdsActivity;
import com.igniva.spplitt.utils.Constants;

/**
 * Created by igniva-php-08 on 17/6/16.
 */
public class AppliedAdsFragment extends BaseFragment{
    View mView;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;

    public static AppliedAdsFragment newInstance() {
        AppliedAdsFragment fragment = new AppliedAdsFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_applied_ads, container, false);
        try {
            setHasOptionsMenu(true);

            setUpLayouts();
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewPager.setOffscreenPageLimit(3);

        return mView;
    }
    @Override
    public void setUpLayouts() {

        tabLayout = (TabLayout) mView.findViewById(R.id.tabs);
        viewPager = (ViewPager) mView.findViewById(R.id.viewpager);
        MyAdapter adapter = new MyAdapter(getChildFragmentManager(), 3);
        viewPager.setAdapter(adapter);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

    @Override
    public void setDataInViewLayouts() {

    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currentFragmentId = Constants.FRAG_ID_APPLIED_ADS;
    }


    class MyAdapter extends FragmentPagerAdapter {
        int int_items;

        public MyAdapter(FragmentManager fm, int int_items) {
            super(fm);
            this.int_items = int_items;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = AwaitedAdFragment.newInstance();
                    break;
                case 1:
                    fragment = RejectedAdsFragment.newInstance();
                    break;
                case 2:
                    fragment = CompletedAdsFragment.newInstance();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            try {
                switch (position) {
                    case 0:
                        return getResources().getString(R.string.awaited_ads);
                    case 1:
                        return getResources().getString(R.string.rejected_ads);
                    case 2:
                        return getResources().getString(R.string.completed_ads);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

