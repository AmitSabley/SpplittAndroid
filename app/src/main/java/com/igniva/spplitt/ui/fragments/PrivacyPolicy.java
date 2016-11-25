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
 * Created by igniva-php-08 on 11/7/16.
 */
public class PrivacyPolicy extends BaseFragment {
    View mView;
    WebView mWvTerms;
    ProgressBar mPbBar;
    public static PrivacyPolicy newInstance() {
        PrivacyPolicy fragment = new PrivacyPolicy();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        setUpLayouts();
        return mView;
    }
    @Override
    public void setUpLayouts() {
        mWvTerms=(WebView)mView.findViewById(R.id.wv_privacy);
        mPbBar=(ProgressBar)mView.findViewById(R.id.pb_privacy);
        mWvTerms.setWebViewClient(new MyBrowser(mPbBar));
        mWvTerms.getSettings().setLoadsImagesAutomatically(true);
        mWvTerms.getSettings().setJavaScriptEnabled(true);
        mWvTerms.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWvTerms.loadUrl(getResources().getString(R.string.privacy_url));
    }

    @Override
    public void setDataInViewLayouts() {

    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currentFragmentId = Constants.FRAG_ID_PRIVACY_POLICY;
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
