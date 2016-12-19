package com.igniva.spplitt.ui.activties;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.igniva.spplitt.App;
import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.adapters.AdDetailsListAdapter;
import com.igniva.spplitt.ui.fragments.ActiveAdFragment;
import com.igniva.spplitt.ui.fragments.IncompleteAdsFragment;
import com.igniva.spplitt.ui.fragments.ViewAllActiveAdsFragment;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;

import org.json.JSONObject;

/**
 * Created by igniva-php-08 on 23/5/16.
 * ad_status=2 maen ad is closed and ad_status=1 mean ad is active
 */
public class ViewAdsDetailsActivity extends BaseActivity {
    private static final String LOG_TAG = "ViewAdsDetailsActivity";
    TextView mToolbarTvText;
    Button mBtnConnectAd;
    String mAdId;
    DataPojo dataPojo;

    int connectPosition;
    RecyclerView mRvAdDetails;
    public static boolean showoptions = true;
    public static ViewAdsDetailsActivity details;
    public static Menu menuItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ads_description);

        details = this;
        try {
            setUpLayouts();
//            setFontStyle();
            setDataInViewLayouts();
            mAdId = getIntent().getStringExtra("ad_id");
            connectPosition = getIntent().getIntExtra("ad_position", 0);

//         Webservice Call
//         Step 1: Register Callback Interface
            WebNotificationManager.registerResponseListener(responseHandlerListenerViewAdDetail);
//         Step 2: Call Webservice Method
            WebServiceClient.getAdsDescription(ViewAdsDetailsActivity.this, postAdDetailsPayload(), true, 1, responseHandlerListenerViewAdDetail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUpLayouts() {
        mToolbarTvText = (TextView) findViewById(R.id.toolbar_tv_text);
        mBtnConnectAd = (Button) findViewById(R.id.btn_connect_ad);
        mRvAdDetails = (RecyclerView) findViewById(R.id.rv_ad_deatils);
    }

    @Override
    public void setDataInViewLayouts() {
        mToolbarTvText.setText(getResources().getString(R.string.view_ad_details));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("ViewAdsDetailsActivity", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_connect_ad:
                App.getInstance().trackEvent(LOG_TAG, "Ad Connect", "Ad Connect Called");

                //         Webservice Call
                //         Step 1: Register Callback Interface
                WebNotificationManager.registerResponseListener(responseHandlerListenerViewAdDetail);
                //         Step 2: Call Webservice Method
                WebServiceClient.connectAnAd(this, connectAnAdPayload(), true, 2, responseHandlerListenerViewAdDetail);
                break;
            case R.id.toolbar_btn_back:
                App.getInstance().trackEvent(LOG_TAG, "Back Press", "View Ad Details Back Pressed");
                finish();
                break;
        }
    }


    private String connectAnAdPayload() {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(ViewAdsDetailsActivity.this, PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(ViewAdsDetailsActivity.this, PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("ad_id", mAdId);
                userData.put("ads_type", "request");
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

    private String postAdDetailsPayload() {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(ViewAdsDetailsActivity.this, PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(ViewAdsDetailsActivity.this, PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("ad_id", mAdId);
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

    ResponseHandlerListener responseHandlerListenerViewAdDetail = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            try {
                WebNotificationManager.unRegisterResponseListener(responseHandlerListenerViewAdDetail);
                if (error == null) {
                    switch (mUrlNo) {
                        case 1://to get ad desc
                            getAdsData(result);
                            break;
                        case 2:
                            connectAnAd(result);
                            break;
                        case 3:
                            cancelAnAd(result);
                            break;
                    }
                } else {
                    // TODO display error dialog
                    new Utility().showErrorDialogRequestFailed(ViewAdsDetailsActivity.this);
                }
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void cancelAnAd(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
                //Error
                new Utility().showErrorDialog(this, result);
            } else {//Success
                DataPojo dataPojo = result.getData();
                if (ViewAllActiveAdsFragment.mViewAllAds != null) {
                    ViewAllActiveAdsFragment.mViewAllAds.getPosition(connectPosition);
                    ;
                }
                if (ActiveAdFragment.mViewActiveAds != null) {
                    ActiveAdFragment.mViewActiveAds.getPosition(connectPosition);
                }
                if (IncompleteAdsFragment.mViewIncompleteAds != null) {
                    IncompleteAdsFragment.mViewIncompleteAds.getPosition(connectPosition);
                }
                this.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connectAnAd(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
                //Error
                new Utility().showErrorDialog(ViewAdsDetailsActivity.this, result);
            } else {//Success
                //Error
                showSuccessDialog(ViewAdsDetailsActivity.this, result, result.getDescription());
                DataPojo dataPojo = result.getData();
                mBtnConnectAd.setClickable(false);
                mBtnConnectAd.setEnabled(false);

                mBtnConnectAd.setBackgroundColor(this.getResources().getColor(R.color.colorGreyDark));
                mBtnConnectAd.setText(this.getResources().getString(R.string.response_pending));
                if (ViewAllActiveAdsFragment.mViewAllAds != null) {
                    ViewAllActiveAdsFragment.mViewAllAds.getPosition(connectPosition, true);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSuccessDialog(final Context mContext, ResponsePojo result, String msg) {
        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
                    R.style.CustomPopUpTheme);
            builder.setMessage(msg);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
            Utility.showToastMessageLong(mContext,
                    mContext.getResources().getString(R.string.invalid_session));
        }
    }

    private void getAdsData(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
                //Error
                new Utility().showErrorDialog(ViewAdsDetailsActivity.this, result);
            } else {//Success
                dataPojo = result.getData();
                AdDetailsListAdapter mAdsListAdapter = new AdDetailsListAdapter(ViewAdsDetailsActivity.this, dataPojo.getRequest(), dataPojo, mAdId, connectPosition);
                mRvAdDetails.setAdapter(mAdsListAdapter);
                mAdsListAdapter.notifyDataSetChanged();
                mRvAdDetails.setHasFixedSize(true);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(ViewAdsDetailsActivity.this);
                mRvAdDetails.setLayoutManager(mLayoutManager);


                if (dataPojo.getAd_status() == 1) {//active ads
                    mBtnConnectAd.setVisibility(View.VISIBLE);
                    if (PreferenceHandler.readString(ViewAdsDetailsActivity.this, PreferenceHandler.USER_ID, "").equals(dataPojo.getPosted_by_id())) {
                        mBtnConnectAd.setVisibility(View.GONE);
                        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                        setSupportActionBar(toolbar);
                        //requests will come here
                    } else {
                        if (dataPojo.is_connect()) {
                            mBtnConnectAd.setEnabled(false);
                            mBtnConnectAd.setClickable(false);
                            mBtnConnectAd.setBackgroundColor(getResources().getColor(R.color.colorGreyDark));
                            mBtnConnectAd.setText(getResources().getString(R.string.response_pending));
                        } else {
                            mBtnConnectAd.setEnabled(true);
                            mBtnConnectAd.setClickable(true);
                            mBtnConnectAd.setBackgroundColor(getResources().getColor(R.color.colorDarkGreen));
                            mBtnConnectAd.setText(getResources().getString(R.string.connect));
                        }
                        if (dataPojo.is_reject()) {
                            mBtnConnectAd.setEnabled(false);
                            mBtnConnectAd.setClickable(false);
                            mBtnConnectAd.setBackgroundColor(getResources().getColor(R.color.colorRed));
                            mBtnConnectAd.setText(getResources().getString(R.string.rejected));
                        }
                        mBtnConnectAd.setVisibility(View.VISIBLE);
                    }
                } else {//close ads
                    mBtnConnectAd.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuItem = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        if (showoptions) {
            getMenuInflater().inflate(R.menu.menu_view_details, menu);
        } else {
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setVisible(false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.mn_action_delete:
                showDeletePopup(this);
                return true;

        }
        return false;
    }

    public void showDeletePopup(final Context mContext) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
                    R.style.CustomPopUpTheme);
            builder.setCancelable(false);
            builder.setMessage(mContext.getResources().getString(R.string.delete_popup_msg));

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //         Webservice Call
                    //         Step 1, Register Callback Interface
                    WebNotificationManager.registerResponseListener(responseHandlerListenerViewAdDetail);
                    // Step 2, Call Webservice Method
                    WebServiceClient.cancelAnAd(mContext, deleteAnAdPayload(mAdId), true, 3, responseHandlerListenerViewAdDetail);

                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String deleteAnAdPayload(String mAdId) {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(this, PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(this, PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("ad_id", mAdId);
                userData.put("ads_type", "cancel");
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

}
