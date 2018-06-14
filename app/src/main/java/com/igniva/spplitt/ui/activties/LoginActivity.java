package com.igniva.spplitt.ui.activties;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
 * Created by igniva-php-08 on 3/5/16.
 */
public class LoginActivity extends BaseActivity {
    private static final String LOG_TAG = "LoginActivity";
    EditText mEtEmail, mEtPassword;
    TextView mToolbarTvText;
    Button mBtnLogin;
//    SharedPreferences sharedpreferences;
//    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //set Layouts
        setUpLayouts();
//        setFontStyle();
        setDataInViewLayouts();
    }

    ResponseHandlerListener responseHandlerListenerLogin = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            WebNotificationManager.unRegisterResponseListener(responseHandlerListenerLogin);
            switch (mUrlNo) {
                case 1:
                    if (error == null) {
                        getLoginData(result);
                    } else {
                        // TODO display error dialog
                        new Utility().showErrorDialogRequestFailed(LoginActivity.this);
                    }
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("LoginActivity", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUpLayouts() {
        mEtEmail = (EditText) findViewById(R.id.et_email);
        mToolbarTvText = (TextView) findViewById(R.id.toolbar_tv_text);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(loginListener);
    }

    @Override
    public void setDataInViewLayouts() {
        mToolbarTvText.setText(getResources().getString(R.string.login));
    }


    View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean val = new Validations().isValidateLogin(getApplicationContext(), mEtEmail, mEtPassword);
            if (val) {
                WebNotificationManager.registerResponseListener(responseHandlerListenerLogin);
                WebServiceClient.getLogin(LoginActivity.this, createLoginPayload(), true, 1, responseHandlerListenerLogin);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.toolbar_btn_back:
                App.getInstance().trackEvent(LOG_TAG, "Back Press", "Ad Details Back Pressed");
                onBackPressed();
                break;
            case R.id.tv_register_here_login:
                App.getInstance().trackEvent(LOG_TAG, "Register User Call", "Create Account");
                startActivity(new Intent(getApplicationContext(), CreateAccountActivity.class));
                break;
            case R.id.tv_forgot_pwd:
                App.getInstance().trackEvent(LOG_TAG, "Forgot Password Call", "Forgot Password");
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
                break;
        }
    }

    //saving data to json...
    private String createLoginPayload() {
        String payload = null;
        try {
            //get registration_by
            String registration_by = "";
//            if (mEtEmail.getText().toString().trim().contains("@")) {
            registration_by = "email";
//            } else {
//                registration_by = "mobile";
//            }
            JSONObject userData = new JSONObject();
            userData.put(registration_by, mEtEmail.getText().toString().trim());
            userData.put("password", mEtPassword.getText().toString().trim());
            userData.put("device_type", "android");
            userData.put("device_id", PreferenceHandler.readString(this, PreferenceHandler.IMEI_NO, ""));
            userData.put("gcm_id", PreferenceHandler.readString(this, PreferenceHandler.GCM_REG_ID, ""));
            payload = userData.toString();
            Log.e("Response", "" + payload);
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }


    private void getLoginData(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
//                Error
                new Utility().showErrorDialog(this, result);
            } else {
                //Success
                DataPojo dataPojo = result.getData();
                PreferenceHandler.writeString(this, PreferenceHandler.USER_ID, dataPojo.getUser_id());
                PreferenceHandler.writeString(this, PreferenceHandler.USER_NAME, dataPojo.getUsername());
                PreferenceHandler.writeString(this, PreferenceHandler.EMAIL, dataPojo.getEmail());
                PreferenceHandler.writeString(this, PreferenceHandler.MOBILE_NO, dataPojo.getMobile());
                PreferenceHandler.writeString(this, PreferenceHandler.GENDER, dataPojo.getGender());
                PreferenceHandler.writeString(this, PreferenceHandler.AGE, dataPojo.getAge());
                PreferenceHandler.writeString(this, PreferenceHandler.COUNTRY, dataPojo.getCountry_id());
                PreferenceHandler.writeString(this, PreferenceHandler.COUNTRY_NAME, dataPojo.getCountry_name());
                PreferenceHandler.writeString(this, PreferenceHandler.STATE, dataPojo.getState());
                PreferenceHandler.writeString(this, PreferenceHandler.STATE_NAME, dataPojo.getState_name());
                PreferenceHandler.writeString(this, PreferenceHandler.CURRENCY_CODE, dataPojo.getCurrency_code());
                PreferenceHandler.writeString(this, PreferenceHandler.CITY, dataPojo.getCity_id());
                PreferenceHandler.writeString(this, PreferenceHandler.CITY_NAME, dataPojo.getCity_name());
                PreferenceHandler.writeString(this, PreferenceHandler.IS_AGE_PUBLIC, dataPojo.getIs_age_public());
                PreferenceHandler.writeString(this, PreferenceHandler.PICTURE, dataPojo.getPicture());
                PreferenceHandler.writeString(this, PreferenceHandler.AUTH_TOKEN, dataPojo.getAuth_token());
                PreferenceHandler.writeString(this, PreferenceHandler.CURRENCY_ID, dataPojo.getCurrency_id());
                PreferenceHandler.writeString(this, PreferenceHandler.AD_COUNTRY_NAME, dataPojo.getPreferred_country_name());
                PreferenceHandler.writeString(this, PreferenceHandler.AD_STATE_NAME, dataPojo.getPreferred_state_name());
                PreferenceHandler.writeString(this, PreferenceHandler.AD_CITY_NAME, dataPojo.getPreferred_city_name());
                           
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
           
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
