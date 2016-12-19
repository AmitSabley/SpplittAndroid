package com.igniva.spplitt.ui.activties;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.igniva.spplitt.App;
import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.CountriesListPojo;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.fragments.EditProfileFragment;
import com.igniva.spplitt.utils.ConnectionDetector;
import com.igniva.spplitt.utils.Permissions;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;
import com.igniva.spplitt.utils.gcm.RegistrationIntentService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;


/**
 * Created by igniva-php-08 on 3/5/16.
 */
public class SplashActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SplashActivity" ;
    //gcm
    GoogleCloudMessaging gcm;
    String regid;
    //    SharedPreferences sharedpreferences;
//    SharedPreferences.Editor editor;
    public static List<CountriesListPojo> listCountries;
    ConnectionDetector mConnectionDetector;
    LinearLayout ll_main;
    boolean val;


    private boolean isReceiverRegistered;
    private String TAG = this.getClass().getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        ll_main = (LinearLayout) findViewById(R.id.ll_main);
        val = checkInternetConnection();


        // Webservice Call to get countries list
        // Step 1, Register Callback Interface
        WebNotificationManager.registerResponseListener(responseHandlerListener);
        // Step 2, Call Webservice Method
        WebServiceClient.getCountriesList(this, "", false, 2, responseHandlerListener);


//        if (val) {
//            if (PreferenceHandler.readString(this, PreferenceHandler.IMEI_NO, "").trim().equals("")) {
////                if (Permissions.checkPermissionGetImeiNo(SplashActivity.this)) {
////                    getImeiNo();
////                }
//            }
//            if (PreferenceHandler.readString(this, PreferenceHandler.GCM_REG_ID, "").trim().equals("")) {
////                getRegId();
//            }
//        }


        //        GCM CODE

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(RegistrationIntentService.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
//                    mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
//                    mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };


        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);

        }
    }



    private boolean checkInternetConnection() {
        mConnectionDetector = new ConnectionDetector(SplashActivity.this);
        if (!mConnectionDetector.isConnectingToInternet()) {
            Snackbar snackbar = Snackbar
                    .make(ll_main, "No internet connection!", Snackbar.LENGTH_SHORT)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkInternetConnection();
                        }
                    });

            // Changing message text color
            snackbar.setActionTextColor(Color.RED);

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            return false;
        }
        return true;
    }


    public void getImeiNo() {
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String imei_no = telephonyManager.getDeviceId();
        PreferenceHandler.writeString(this, PreferenceHandler.IMEI_NO, imei_no);
//        editor.commit();
        if (PreferenceHandler.readString(this, PreferenceHandler.GCM_REG_ID, "").trim().equals("")) {

//            getRegId();

        }
    }

//    //Get gcm Registration ID of device
//    public void getRegId() {
//        new AsyncTask<Void, Void, String>() {
//            @Override
//            protected String doInBackground(Void... params) {
//                try {
//                    if (gcm == null) {
//                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
//                    }
//                    regid = gcm.register(getResources().getString(R.string.gcm_project_no));
//                    PreferenceHandler.writeString(SplashActivity.this, PreferenceHandler.GCM_REG_ID, regid);
////                    editor.commit();
//
//                } catch (IOException ex) {
//                    ex.getMessage();
//
//                }
//                return regid;
//            }
//
//            @Override
//            protected void onPostExecute(String msg) {
//            }
//        }.execute(null, null, null);
//    }

    ResponseHandlerListener responseHandlerListener = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            WebNotificationManager.unRegisterResponseListener(responseHandlerListener);
            try {
                if (error == null) {
                    switch (mUrlNo) {
                        case 1://update gcm id
                               callToNextActivity();
                            break;
                        case 2://to get countries list
                            getCountriesData(result);
                            if (!PreferenceHandler.readString(SplashActivity.this, PreferenceHandler.USER_ID, "").trim().equals("")) {
                                if (!RegistrationIntentService.GCM_TOKEN.equals("")) {
                                    if (!(PreferenceHandler.readString(SplashActivity.this, PreferenceHandler.GCM_REG_ID, "").equals(RegistrationIntentService.GCM_TOKEN))) {
                                        updateGcmToServerInBackground();
                                    } else {
                                        callToNextActivity();
                                    }
                                }
                            }else {
                                callToNextActivity();
                            }
                            break;
                    }
                } else {
                    // TODO display error dialog
                    new Utility().showErrorDialogRequestFailed(SplashActivity.this);
                }
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void callToNextActivity() {
        if (PreferenceHandler.readInteger(SplashActivity.this, PreferenceHandler.OTP_SCREEN_NO, 0) == 1) {
            startActivity(new Intent(getApplicationContext(), CreateAccountActivity.class));
            finish();
        } else if (PreferenceHandler.readInteger(SplashActivity.this, PreferenceHandler.OTP_SCREEN_NO, 0) == 2) {
            startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
            finish();
        } else if (PreferenceHandler.readInteger(SplashActivity.this, PreferenceHandler.OTP_SCREEN_NO, 0) == 3) {
            startActivity(new Intent(getApplicationContext(), EditProfileFragment.class));
            finish();
        } else if (!PreferenceHandler.readString(SplashActivity.this, PreferenceHandler.USER_ID, "").trim().equals("")) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(getApplicationContext(), LoginOptionActivity.class));
            finish();
        }
    }

    private List<CountriesListPojo> getCountriesData(ResponsePojo result) {
        List<CountriesListPojo> listCountries = new ArrayList<CountriesListPojo>();
        try {
            if (result.getStatus_code() == 400) {
                //Error
                new Utility().showErrorDialog(this, result);
            } else {//Success
                DataPojo dataPojo = result.getData();

                listCountries = dataPojo.getCountries();
                this.listCountries = listCountries;
                return listCountries;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCountries;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Permissions.MY_PERMISSIONS_REQUEST_IMEINO:
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
        Log.e("SplashActivity", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(RegistrationIntentService.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(SplashActivity.this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }



    private void updateGcmToServerInBackground() {
            // Webservice Call to get countries list
            // Step 1, Register Callback Interface
            WebNotificationManager.registerResponseListener(responseHandlerListener);
            // Step 2, Call Webservice Method
            WebServiceClient.updateGcmId(this, createGcmPayload(), false, 1, responseHandlerListener);

    }
    //saving data to json...
    private String createGcmPayload() {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(this, PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(this, PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("profile_type", "gcm");
                userData.put("gcm_id", PreferenceHandler.readString(this, PreferenceHandler.GCM_REG_ID, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }


}
