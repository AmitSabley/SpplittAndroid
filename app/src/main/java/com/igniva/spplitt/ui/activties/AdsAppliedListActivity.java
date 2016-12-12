package com.igniva.spplitt.ui.activties;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.AdsPostedPojo;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.adapters.AdsAppliedListdapter;
import com.igniva.spplitt.utils.Log;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igniva-php-08 on 6/6/16.
 */
public class AdsAppliedListActivity extends BaseActivity{
    String mOtherUserId;
    String mAdType;
    RecyclerView mRvAds;
    TextView mToolbarTvText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_applied);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent in=getIntent();
        mOtherUserId=in.getStringExtra("other_user_id");
        mAdType=in.getStringExtra("ads_type");

//         Webservice Call
//         Step 1: Register Callback Interface
        WebNotificationManager.registerResponseListener(responseHandlerListenerViewAD);
//         Step 2: Call Webservice Method
        WebServiceClient.getAdsList(this, postAdGetCategoriesPayload(), true, 1, responseHandlerListenerViewAD);

        setUpLayouts();
        setDataInViewLayouts();
    }
    @Override
    public void setUpLayouts() {
        mRvAds=(RecyclerView)findViewById(R.id.rv_ads_applied);
        mToolbarTvText = (TextView) findViewById(R.id.toolbar_tv_text);
    }

    @Override
    public void setDataInViewLayouts() {

        String upperString = mAdType.substring(0,1).toUpperCase() + mAdType.substring(1);
        mToolbarTvText.setText(upperString+" Ads");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_btn_back:
                onBackPressed();
                break;

        }
    }
    private String postAdGetCategoriesPayload() {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(this, PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(this, PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("ads_type", mAdType);
                userData.put("other_user_id", mOtherUserId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("Response", "" + userData);
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }
    ResponseHandlerListener responseHandlerListenerViewAD = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            try {
                WebNotificationManager.unRegisterResponseListener(responseHandlerListenerViewAD);
                if (error == null) {
                    switch (mUrlNo) {
                        case 1:
                            //to get ads list
                            getAdsData(result);
                            break;
                    }
                } else {
                    // TODO display error dialog
                    new Utility().showErrorDialogRequestFailed(AdsAppliedListActivity.this);
                }
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                new Utility().showErrorDialogRequestFailed(AdsAppliedListActivity.this);
            }
        }
    };
    private void getAdsData(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
                mRvAds.setAdapter(null);
                //Error
                new Utility().showErrorDialog(AdsAppliedListActivity.this, result);
            } else {
                //Success
                DataPojo dataPojo = result.getData();
                List<AdsPostedPojo> listAds = new ArrayList<AdsPostedPojo>();
                listAds = dataPojo.getAdsPostedList();
                if(listAds.size()>0) {
                    AdsAppliedListdapter mAdsListAdapter = new AdsAppliedListdapter(AdsAppliedListActivity.this, listAds, false);


                    mRvAds.setAdapter(mAdsListAdapter);
                    mAdsListAdapter.notifyDataSetChanged();


                    mRvAds.setHasFixedSize(true);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(AdsAppliedListActivity.this);
                    mRvAds.setLayoutManager(mLayoutManager);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
