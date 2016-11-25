package com.igniva.spplitt.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.igniva.spplitt.R;
import com.igniva.spplitt.ui.activties.MainActivity;
import com.igniva.spplitt.utils.Constants;

/**
 * Created by igniva-php-08 on 18/5/16.
 */
public class AboutSpplittFragment extends BaseFragment {
    View mView;
    WebView mWvTerms;
    ProgressBar mPbBar;
    public static AboutSpplittFragment newInstance() {
        AboutSpplittFragment fragment = new AboutSpplittFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_about_spplitt, container, false);
        setUpLayouts();
        return mView;
    }
    @Override
    public void setUpLayouts() {
        mWvTerms=(WebView)mView.findViewById(R.id.wv_about);
        mPbBar=(ProgressBar)mView.findViewById(R.id.pb_about);
        mWvTerms.setWebViewClient(new MyBrowser(mPbBar));
        mWvTerms.getSettings().setLoadsImagesAutomatically(true);
        mWvTerms.getSettings().setJavaScriptEnabled(true);
        mWvTerms.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWvTerms.loadUrl(getResources().getString(R.string.about_url));
    }

    @Override
    public void setDataInViewLayouts() {

    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currentFragmentId = Constants.FRAG_ID_ABOUT_US;
    }

    private class MyBrowser extends WebViewClient {
        private ProgressBar progressBar;
        public MyBrowser(ProgressBar progressBar) {
            this.progressBar=progressBar;
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }
}
