package com.igniva.spplitt.ui.fragments;

import android.support.v4.app.Fragment;

/**
 * Created by igniva-php-08 on 10/5/16.
 */
public abstract class BaseFragment extends Fragment {

    private static final String LOG_TAG = "BaseFragment";

    public abstract void setUpLayouts();

    public abstract void setDataInViewLayouts();
}
