package com.igniva.spplitt.ui.activties;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
 * Created by igniva-php-08 on 10/5/16.
 * if userid is blank then user is from forgot password else user is from create account
 * otp_type=0 means user id from create account or from forgot password ;;otp_type=1 means user is from edit profile
 */
public class OtpConfirmationActivity extends BaseActivity {
    EditText mEtOtp;
//    SharedPreferences sharedpreferences;
//    SharedPreferences.Editor editor;
    String mUserId, mMobileNo, mOtp;
    Button mBtnResendOtp;
    CountDownTimer mCdtResend;
    //    String in
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_otp_confirmation);

        //initialize shared preferneces
//        sharedpreferences = getSharedPreferences(Constants.SPPLITT_PREFERENCES, Context.MODE_PRIVATE);
//        editor = sharedpreferences.edit();

        mUserId = PreferenceHandler.readString(this,PreferenceHandler.TEMP_USER_ID, "");
        mMobileNo = PreferenceHandler.readString(this,PreferenceHandler.TEMP_MOBILE_NO, "");
        mOtp = PreferenceHandler.readString(this,PreferenceHandler.TEMP_OTP, "");
        //set Layouts
        setUpLayouts();
//        setFontStyle();
        setDataInViewLayouts();
    }

    @Override
    public void setUpLayouts() {
        mEtOtp = (EditText) findViewById(R.id.et_otp);
        mBtnResendOtp=(Button)findViewById(R.id.btn_resend_otp);
    }

    @Override
    public void setDataInViewLayouts() {

//        mEtOtp.setText(mOtp + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit_otp:
                if(mCdtResend!=null){
                    mCdtResend.cancel();
                }
                boolean val = new Validations().isValidateOtp(getApplicationContext(), mEtOtp);
                if (val) {
                    if(!mUserId.equals("")) {//user is coming from create account
                        // Webservice Call
                        // Step 1, Register Callback Interface
                        WebNotificationManager.registerResponseListener(responseHandlerListener);
                        // Step 2, Call Webservice Method
                        WebServiceClient.validateOtpPostActivation(this, createActivationOtpPayload(), true, 1,responseHandlerListener);
                    }else{//user is coming from forgotpassword
                        // Webservice Call
                        // Step 1, Register Callback Interface
                        WebNotificationManager.registerResponseListener(responseHandlerListener);
                        // Step 2, Call Webservice Method
                        WebServiceClient.validateOtpForgotPassword(this, createForgotOtpPayload(), true, 1,responseHandlerListener);
                    }
                }
                break;
            case R.id.iv_close:
                if(mCdtResend!=null){
                    mCdtResend.cancel();
                }
                Utility.removeTemperoryValueFromPreferences(this);
                this.finish();
                break;
            case R.id.btn_resend_otp:
                mBtnResendOtp.setClickable(false);
                // Webservice Call
                // Step 1, Register Callback Interface
                WebNotificationManager.registerResponseListener(responseHandlerListener);
                // Step 2, Call Webservice Method
                WebServiceClient.validateResendOtp(this, createResendOtpPayload(), true, 2,responseHandlerListener);
                setClickableFalseBtn();
                break;
        }

    }

    private String createResendOtpPayload() {
        String payload = null;
        try {

            JSONObject userData = new JSONObject();
            if(mUserId!=null) {
                userData.put("user_id", mUserId);
            }
            userData.put("mobile", mMobileNo);
            //otp_type=0 means user id from create account or from forgot password ;;otp_type=1 means user is from edit profile
            if(PreferenceHandler.readInteger(OtpConfirmationActivity.this,PreferenceHandler.OTP_SCREEN_NO, 0)==1 ||PreferenceHandler.readInteger(OtpConfirmationActivity.this,PreferenceHandler.OTP_SCREEN_NO, 0)==2) {
                userData.put("otp_type", 0);
            }else{
                userData.put("otp_type", 1);
            }
            payload = userData.toString();

            Log.e("response", "" + payload);
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }
    private void setClickableFalseBtn() {
        mCdtResend=new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                mBtnResendOtp.setText("Enable in: " + millisUntilFinished / 1000+" seconds.");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                mBtnResendOtp.setText(getResources().getString(R.string.resend_otp));
                mBtnResendOtp.setClickable(true);
            }

        }.start();
    }

    private String createForgotOtpPayload() {
        String payload = null;
        try {

            JSONObject userData = new JSONObject();
            userData.put("mobile", mMobileNo);
            userData.put("otp", mEtOtp.getText().toString().trim());
            userData.put("request_id", mOtp);
            payload = userData.toString();

            Log.e("response", "" + payload);
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }

    private String createActivationOtpPayload() {
        String payload = null;
        try {

            JSONObject userData = new JSONObject();
            userData.put("user_id", mUserId);
            userData.put("otp", mEtOtp.getText().toString().trim());
            userData.put("request_id", mOtp);
            payload = userData.toString();

            Log.e("response", "" + payload);
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }

//    @Override
//    public void setFontStyle() {
//        try {
//            int[] arrayFont = {R.id.et_otp};
//            TextView textValue;
//            for (int i = 0; i < arrayFont.length; i++) {
//                textValue = (TextView) findViewById(arrayFont[i]);
//                FontsView.changeFontStyle(getApplicationContext(), textValue);
//            }
//
//            int[] arrayFontRegular = {R.id.tv_otp_main, R.id.btn_submit_otp};
//            TextView textValueRegular;
//            for (int i = 0; i < arrayFontRegular.length; i++) {
//                textValueRegular = (TextView) findViewById(arrayFontRegular[i]);
//                FontsView.changeFontStyleRegular(getApplicationContext(), textValueRegular);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    ResponseHandlerListener responseHandlerListener = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            WebNotificationManager.unRegisterResponseListener(responseHandlerListener);
            switch (mUrlNo) {
                case 1:
                    if (error == null) {
                        // Success Case
                        otpVerify(result);
                    } else {
                        // TODO display error dialog
                        new Utility().showErrorDialogRequestFailed(getApplicationContext());
                    }
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    break;
                case 2:
                    if (error == null) {
                        // Success Case
                        resendOtp(result);
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

    private void resendOtp(ResponsePojo result) {
        try {
            if (result.getStatus_code()==400) {
                //Error
                new Utility().showErrorDialog(this, result);
            } else {
                //Success
                DataPojo dataPojo = result.getData();
                mOtp=dataPojo.getRequest_id();
                setDataInViewLayouts();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void otpVerify(ResponsePojo result) {
        try {
            if (result.getStatus_code()==400) {
                //Error
                new Utility().showErrorDialog(this, result);
            } else {
                //Success
                DataPojo dataPojo = result.getData();
                if(CreateAccountActivity.createAccount!=null) {
                    CreateAccountActivity.createAccount.finish();
                }
                if(ForgotPasswordActivity.forgotAccount!=null) {
                    ForgotPasswordActivity.forgotAccount.finish();
                }
                if(!mUserId.equals("")) {//user is coming from create account
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                }else{//user is coming from forgotpassword

                    Intent intent=new Intent(getApplicationContext(), ChangePasswordActivity.class);
                    intent.putExtra("user_id",dataPojo.getUser_id());
                    intent.putExtra("auth_token",dataPojo.getAuth_token());
                    startActivity(intent);
                }
                Utility.removeTemperoryValueFromPreferences(this);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onBackPressed() {

    }
}
