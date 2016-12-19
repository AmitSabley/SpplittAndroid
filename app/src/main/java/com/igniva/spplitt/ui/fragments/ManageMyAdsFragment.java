package com.igniva.spplitt.ui.fragments;

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
import android.view.View;
import android.view.ViewGroup;

import com.igniva.spplitt.App;
import com.igniva.spplitt.R;
import com.igniva.spplitt.ui.activties.MainActivity;
import com.igniva.spplitt.utils.Constants;
import com.igniva.spplitt.utils.Log;

/**
 * Created by igniva-php-08 on 18/5/16.
 */
public class ManageMyAdsFragment extends BaseFragment implements View.OnClickListener {
    private static final String LOG_TAG = "ManageMyAdsFragment";
    View mView;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    FloatingActionButton mBtnPostAd;

    public static ManageMyAdsFragment newInstance() {
        ManageMyAdsFragment fragment = new ManageMyAdsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_manage_my_ads, container, false);
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
    public void onResume() {
        super.onResume();
        MainActivity.currentFragmentId = Constants.FRAG_ID_MANAGE_MY_ADS;
        Log.e("ManageMyAdsFragment", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUpLayouts() {
        mBtnPostAd = (FloatingActionButton) mView.findViewById(R.id.btn_post_ad);
        mBtnPostAd.setOnClickListener(this);
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
    public void onClick(View view) {
        NavigationView navigationView = (NavigationView) MainActivity.main.findViewById(R.id.nav_view);
        switch (view.getId()) {
            case R.id.btn_post_ad:
                //to go at post ad
                ((MainActivity) getActivity()).onNavigationItemSelected(navigationView.getMenu().getItem(4));
                break;
        }
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
                    App.getInstance().trackEvent(LOG_TAG, "ActiveAdFragment Call", "AwaitedAdFragment Display");
                    fragment = ActiveAdFragment.newInstance();
                    break;
                case 1:
                    App.getInstance().trackEvent(LOG_TAG, "CompleteAdsFragment Call", "AwaitedAdFragment Display");
                    fragment = CompleteAdsFragment.newInstance();
                    break;
                case 2:
                    App.getInstance().trackEvent(LOG_TAG, "IncompleteAdsFragment Call", "AwaitedAdFragment Display");
                    fragment = IncompleteAdsFragment.newInstance();
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
                        return getResources().getString(R.string.active_ads);
                    case 1:
                        return getResources().getString(R.string.complete_ads);
                    case 2:
                        return getResources().getString(R.string.incomplete_ads);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
