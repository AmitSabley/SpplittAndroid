package com.igniva.spplitt.ui.activties;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
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

import java.util.LinkedHashMap;


/**
 * Created by igniva-php-08 on 6/5/16.
 */
public class ForgotPasswordActivity extends BaseActivity {
    EditText mEtEmail;
    TextView mToolbarTvText;
//    SharedPreferences sharedpreferences;
//    SharedPreferences.Editor editor;
    public static  ForgotPasswordActivity forgotAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //initialize shared preferneces
//        sharedpreferences = getSharedPreferences(Constants.SPPLITT_PREFERENCES, Context.MODE_PRIVATE);
//        editor = sharedpreferences.edit();
        forgotAccount=this;
        //to open ot screen
        if(PreferenceHandler.readInteger(this,PreferenceHandler.OTP_SCREEN_NO, 0)==2) {
            startActivity(new Intent(getApplicationContext(), OtpConfirmationActivity.class));
        }
        //set Layouts
        setUpLayouts();
//        setFontStyle();
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

    @Override
    public void setUpLayouts() {
        mEtEmail = (EditText) findViewById(R.id.et_forgot_email);
        mToolbarTvText = (TextView) findViewById(R.id.toolbar_tv_text);
    }

    @Override
    public void setDataInViewLayouts() {
        mToolbarTvText.setText(getResources().getString(R.string.forgot_password));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_btn_back:
                onBackPressed();
                break;
            case R.id.btn_forgot_password:
                boolean val = new Validations().isValidateForgotPassword(getApplicationContext(), mEtEmail);
                if (val) {
                    // Webservice Call
                    // Step 1, Register Callback Interface
                    WebNotificationManager.registerResponseListener(responseHandlerListenerForgot);
                    WebServiceClient.forgotPassword(this, createForgotPasswordPayload(), true, 1,responseHandlerListenerForgot);
                }
                break;
        }
    }

//    @Override
//    public void setFontStyle() {
//        int[] arrayFont = {R.id.tv_reset_password, R.id.tv_reset_password_text, R.id.et_forgot_email};
//        TextView textValue;
//        for (int i = 0; i < arrayFont.length; i++) {
//            textValue = (TextView) findViewById(arrayFont[i]);
//            FontsView.changeFontStyle(getApplicationContext(), textValue);
//        }
//
//        int[] arrayFontRegular = {R.id.tv_reset_password, R.id.btn_forgot_password, R.id.toolbar_tv_text};
//        TextView textValueRegular;
//        for (int i = 0; i < arrayFontRegular.length; i++) {
//            textValueRegular = (TextView) findViewById(arrayFontRegular[i]);
//            FontsView.changeFontStyleRegular(getApplicationContext(), textValueRegular);
//        }
//    }

    //saving data to json...
    private String createForgotPasswordPayload() {
        String payload = null;
        try {
            //get registration_by
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

            Log.e("rseponse", "" + payload);
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }


    private void getForgotPassword(ResponsePojo result) {
        try {
            if (result.getStatus_code()==400) {
                //Error
                new Utility().showErrorDialog(this, result);
            } else {//Success
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
