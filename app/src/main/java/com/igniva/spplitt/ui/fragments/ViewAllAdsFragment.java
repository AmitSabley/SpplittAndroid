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

import com.igniva.spplitt.R;
import com.igniva.spplitt.ui.activties.MainActivity;
import com.igniva.spplitt.utils.Constants;

/**
 * Created by igniva-php-08 on 14/6/16.
 */
public class ViewAllAdsFragment extends BaseFragment{
    View mView;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    String mCatId;
    public static ViewAllAdsFragment newInstance() {
        ViewAllAdsFragment fragment = new ViewAllAdsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_all_ads, container, false);
        try {
            setHasOptionsMenu(true);
            Bundle mBundle = this.getArguments();
            if (mBundle != null) {//u are coming from categories to view ads
                mCatId = mBundle.getString("cat_id", "");
            } else {// u are coming from view event ads
                mCatId = "1";
            }
            setUpLayouts();
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewPager.setOffscreenPageLimit(2);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currentFragmentId = Constants.FRAG_ID_VIEW_EVENT;
    }

    @Override
    public void setUpLayouts() {
        tabLayout = (TabLayout) mView.findViewById(R.id.tabs);
        viewPager = (ViewPager) mView.findViewById(R.id.viewpager);
        MyAdapter adapter = new MyAdapter(getChildFragmentManager(), 2);
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



    class MyAdapter extends FragmentPagerAdapter {
        int int_items;

        public MyAdapter(FragmentManager fm, int int_items) {
            super(fm);
            this.int_items = int_items;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    fragment = ViewAllActiveAdsFragment.newInstance();
                    bundle.putString("cat_id", mCatId);
                    fragment.setArguments(bundle);
                    break;
                case 1:
                    fragment = ViewAllClosedAdsFragment.newInstance();
                    bundle.putString("cat_id", mCatId);
                    fragment.setArguments(bundle);
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
                        return getResources().getString(R.string.closed_ads);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
