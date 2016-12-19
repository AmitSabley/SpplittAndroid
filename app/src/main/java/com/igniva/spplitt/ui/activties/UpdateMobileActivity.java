package com.igniva.spplitt.ui.activties;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
 * Created by igniva-php-08 on 12/5/16.
 */
public class UpdateMobileActivity extends BaseActivity {
    private static final String LOG_TAG = "UpdateMobileActivity";
    //    SharedPreferences sharedpreferences;
//    SharedPreferences.Editor editor;
    EditText mEtNewMobileNo, mEtOldPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_update_mobile);

        //initialize shared preferneces
//        sharedpreferences = getSharedPreferences(Constants.SPPLITT_PREFERENCES, Context.MODE_PRIVATE);
//        editor = sharedpreferences.edit();
        //set Layouts
        setUpLayouts();
//        setFontStyle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("UpdateMobileActivity", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseHandlerListener responseHandlerListener = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            WebNotificationManager.unRegisterResponseListener(responseHandlerListener);
            switch (mUrlNo) {
                case 1:
                    if (error == null) {
                        updateMobileData(result);
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

    private void updateMobileData(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
                //Error
                new Utility().showErrorDialog(this, result);
            } else {
                //Success
                DataPojo dataPojo = result.getData();
                PreferenceHandler.writeString(this, PreferenceHandler.MOBILE_NO, mEtNewMobileNo.getText().toString());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
//                PreferenceHandler.writeInteger(this,PreferenceHandler.OTP_SCREEN_NO,3);
//                PreferenceHandler.writeString(this,PreferenceHandler.TEMP_MOBILE_NO,mEtNewMobileNo.getText().toString());
//                PreferenceHandler.writeString(this,PreferenceHandler.TEMP_USER_ID,dataPojo.getUser_id());
//                PreferenceHandler.writeString(this,PreferenceHandler.TEMP_OTP,dataPojo.getRequest_id());
//                Intent intent=new Intent(getApplicationContext(), OtpConfirmationActivity.class);
//                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUpLayouts() {
        mEtNewMobileNo = (EditText) findViewById(R.id.et_update_mobileno);
        mEtOldPassword = (EditText) findViewById(R.id.et_update_password);
    }

    @Override
    public void setDataInViewLayouts() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update_mobileno:
                boolean val = new Validations().isValidateUpdateMobileNo(getApplicationContext(), mEtNewMobileNo, mEtOldPassword);
                if (val) {
                    App.getInstance().trackEvent(LOG_TAG, "Update Mobile", "Mobile Update Called");
                    // Webservice Call
                    // Step 1, Register Callback Interface
                    WebNotificationManager.registerResponseListener(responseHandlerListener);
                    // Step 2, Call Webservice Method
                    WebServiceClient.updateMobileNo(this, createUpdateMobileNoPayload(), true, 1, responseHandlerListener);
                }
                break;
        }
    }

    private String createUpdateMobileNoPayload() {
        String payload = null;
        try {

            JSONObject userData = new JSONObject();
            userData.put("user_id", PreferenceHandler.readString(this, PreferenceHandler.USER_ID, ""));
            userData.put("auth_token", PreferenceHandler.readString(this, PreferenceHandler.AUTH_TOKEN, ""));
            userData.put("profile_type", "mobile");
            userData.put("mobile", mEtNewMobileNo.getText().toString().trim());
            userData.put("password", mEtOldPassword.getText().toString().trim());
            payload = userData.toString();
            Log.e("response", "" + payload);
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }

//    @Override
//    public void setFontStyle() {
//        int[] arrayFont = {R.id.et_update_mobileno, R.id.et_update_password};
//        TextView textValue;
//        for (int i = 0; i < arrayFont.length; i++) {
//            textValue = (TextView) findViewById(arrayFont[i]);
//            FontsView.changeFontStyle(getApplicationContext(), textValue);
//        }
//
//        int[] arrayFontRegular = {R.id.tv_reset_password, R.id.btn_update_mobileno};
//        TextView textValueRegular;
//        for (int i = 0; i < arrayFontRegular.length; i++) {
//            textValueRegular = (TextView) findViewById(arrayFontRegular[i]);
//            FontsView.changeFontStyleRegular(getApplicationContext(), textValueRegular);
//        }
//    }

}
