package com.igniva.spplitt.ui.activties;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.igniva.spplitt.R;

/**
 * Created by igniva-php-08 on 11/7/16.
 */
public class TermsOfUseActivity extends BaseActivity{
    TextView mToolbarTvText;
    WebView mWvTerms;
    ProgressBar mPbBar;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        setUpLayouts();
        setDataInViewLayouts();
    }

    @Override
    public void setUpLayouts() {
        mToolbarTvText = (TextView) findViewById(R.id.toolbar_tv_text);
        mWvTerms=(WebView)findViewById(R.id.wv_terms);
        mPbBar=(ProgressBar)findViewById(R.id.pb_terms);
    }

    @Override
    public void setDataInViewLayouts() {
        mToolbarTvText.setText(getResources().getString(R.string.terms));
        mWvTerms.setWebViewClient(new MyBrowser(mPbBar));
        mWvTerms.getSettings().setLoadsImagesAutomatically(true);
        mWvTerms.getSettings().setJavaScriptEnabled(true);
        mWvTerms.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWvTerms.loadUrl(getResources().getString(R.string.terms_url));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_disagree:
                CreateAccountActivity.mCbTerms.setChecked(false);
                finish();
                break;
            case R.id.btn_agree:
                CreateAccountActivity.mCbTerms.setChecked(true);
                finish();
                break;

        }
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
