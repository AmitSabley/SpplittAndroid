package com.igniva.spplitt.ui.activties;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.igniva.spplitt.App;
import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;
import com.igniva.spplitt.utils.Validations;

import org.json.JSONObject;


/**
 * Created by Jigyasa Saluja on 6/5/16.
 */
public class ForgotPasswordActivity extends BaseActivity {
    private static final String LOG_TAG = "ForgotPasswordActivity";
    EditText mEtEmail;
    TextView mToolbarTvText;

    //    SharedPreferences sharedpreferences;
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("ForgotPasswordActivity", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    SharedPreferences.Editor editor;
    public static ForgotPasswordActivity forgotAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

//       Initialize Shared Preferences
//       sharedpreferences = getSharedPreferences(Constants.SPPLITT_PREFERENCES, Context.MODE_PRIVATE);
//       editor = sharedpreferences.edit();
        forgotAccount = this;
//       TO Call OTP screen
        if (PreferenceHandler.readInteger(this, PreferenceHandler.OTP_SCREEN_NO, 0) == 2) {
            startActivity(new Intent(getApplicationContext(), OtpConfirmationActivity.class));
        }
//      Set Layouts
        setUpLayouts();
        setDataInViewLayouts();
    }

    ResponseHandlerListener responseHandlerListenerForgot = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            WebNotificationManager.unRegisterResponseListener(responseHandlerListenerForgot);
            switch (mUrlNo) {
                case 1:
                    if (error == null) {

                        getForgotPassword(result);
                    } else {
                        // TODO display error dialog
                        new Utility().showErrorDialogRequestFailed(getApplicationContext());
                    }
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    break;
            }
        }
    };

    /**
     * Layout Setup
     */
    @Override
    public void setUpLayouts() {
        mEtEmail = (EditText) findViewById(R.id.et_forgot_email);
        mToolbarTvText = (TextView) findViewById(R.id.toolbar_tv_text);
    }


    /**
     * Set Data in Views
     */
    @Override
    public void setDataInViewLayouts() {
        mToolbarTvText.setText(getResources().getString(R.string.forgot_password));
    }


    /**
     * OnClick Listeners
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_btn_back:
                App.getInstance().trackEvent(LOG_TAG, "Back Press", "Ad Details Back Pressed");

                onBackPressed();
                break;
            case R.id.btn_forgot_password:
                boolean val = new Validations().isValidateForgotPassword(getApplicationContext(), mEtEmail);
                if (val) {
                    App.getInstance().trackEvent(LOG_TAG, "Forgot Password", "Forgot Password Clicked");

//                  Webservice Call
//                  Step 1: Register Callback Interface
                    WebNotificationManager.registerResponseListener(responseHandlerListenerForgot);
//                  Step 2: Call Webservice Method
                    WebServiceClient.forgotPassword(this, createForgotPasswordPayload(), true, 1, responseHandlerListenerForgot);
                }
                break;
        }
    }

    /**
     * Saving Data to JSON
     */
    private String createForgotPasswordPayload() {
        String payload = null;
        try {
//          Get registration_by
            String registration_by = "", forgot_type = "";
//            if (mEtEmail.getText().toString().trim().contains("@")) {
            registration_by = "email";
            forgot_type = "Email";
//            } else {
//                registration_by = "mobile";
//                forgot_type = "Mobile";
//            }
            JSONObject userData = new JSONObject();
            try {
                userData.put(registration_by, mEtEmail.getText().toString().trim());
                userData.put("forgot_type", forgot_type);
            } catch (Exception e) {
                e.printStackTrace();
            }
            payload = userData.toString();

            Log.e("Response", "" + payload);
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }


    /**
     * Forgot Password Response
     *
     * @param result
     */
    private void getForgotPassword(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
                //Error
                new Utility().showErrorDialog(this, result);
            } else {
                //Success
                DataPojo dataPojo = result.getData();
//                if (mEtEmail.getText().toString().trim().contains("@")) {
                new Utility().showCreateAccountDialog(this, result);
//                } else {
//                    PreferenceHandler.writeInteger(this,PreferenceHandler.OTP_SCREEN_NO,2);
//                    PreferenceHandler.writeString(this,PreferenceHandler.TEMP_MOBILE_NO,mEtEmail.getText().toString());
//                    PreferenceHandler.writeString(this,PreferenceHandler.TEMP_OTP,dataPojo.getRequest_id());
//                    Intent intent=new Intent(getApplicationContext(), OtpConfirmationActivity.class);
//                    startActivity(intent);
//                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
