package com.igniva.spplitt.ui.fragments;

import android.support.v4.app.Fragment;

import com.igniva.spplitt.App;
import com.igniva.spplitt.utils.Log;

/**
 * Created by igniva-php-08 on 10/5/16.
 */
public abstract class BaseFragment extends Fragment {

    private static final String LOG_TAG = "BaseFragment";

    public abstract void setUpLayouts();

    @Override
    public void onResume() {
        super.onResume();
        Log.e("BaseFragment", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public abstract void setDataInViewLayouts();
}
