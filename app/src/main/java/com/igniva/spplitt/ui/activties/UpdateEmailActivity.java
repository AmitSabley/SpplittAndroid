package com.igniva.spplitt.ui.activties;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;
import com.igniva.spplitt.utils.Validations;

import org.json.JSONObject;

/**
 * Created by igniva-php-08 on 12/5/16.
 */
public class UpdateEmailActivity extends BaseActivity {
//    SharedPreferences sharedpreferences;
//    SharedPreferences.Editor editor;
    EditText mEtNewEmail,mEtOldPassword;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_update_email);

        //initialize shared preferneces
//        sharedpreferences = getSharedPreferences(Constants.SPPLITT_PREFERENCES, Context.MODE_PRIVATE);
//        editor = sharedpreferences.edit();


        //set Layouts
        setUpLayouts();
//        setFontStyle();
    }

    ResponseHandlerListener responseHandlerListener=new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            WebNotificationManager.unRegisterResponseListener(responseHandlerListener);
            switch (mUrlNo) {
                case 1://success
                    if (error==null)
                    {
                        getLoginData(result);
                    }else{
                        // TODO display error dialog
                        new Utility().showErrorDialogRequestFailed(getApplicationContext());
                    }
                    if(mProgressDialog!=null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();


                    break;
            }
        }
    };
    private void getLoginData(ResponsePojo result) {
        try {
            if (result.getStatus_code()==400) {
                //Error
                new Utility().showErrorDialog(this, result);
            } else {
                //Success
                Utility.clearSharedPreferneces(this);
                new Utility().showCreateAccountDialogUpdate(UpdateEmailActivity.this,result);
            }
        }catch(Exception e){e.printStackTrace();}
    }

    @Override
    public void setUpLayouts() {
        mEtNewEmail=(EditText)findViewById(R.id.et_update_email);
        mEtOldPassword=(EditText)findViewById(R.id.et_update_password);
    }

    @Override
    public void setDataInViewLayouts() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_update_email:
                boolean val = new Validations().isValidateUpdateEmail(getApplicationContext(), mEtNewEmail, mEtOldPassword);
                if (val) {
                    // Webservice Call
                    // Step 1, Register Callback Interface
                    WebNotificationManager.registerResponseListener(responseHandlerListener);
                    // Step 2, Call Webservice Method
                    WebServiceClient.updateEmail(this, createUpdateEmailPayload(), true, 1,responseHandlerListener);
                }
                break;
        }
    }

    private String createUpdateEmailPayload() {
        String payload = null;
        try {

            JSONObject userData = new JSONObject();
            userData.put("user_id", PreferenceHandler.readString(this,PreferenceHandler.USER_ID,""));
            userData.put("auth_token",PreferenceHandler.readString(this,PreferenceHandler.AUTH_TOKEN,""));
            userData.put("profile_type", "email");
            userData.put("email", mEtNewEmail.getText().toString().trim());
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
//        int[] arrayFont = {R.id.et_update_email, R.id.et_update_password};
//        TextView textValue;
//        for (int i = 0; i < arrayFont.length; i++) {
//            textValue = (TextView) findViewById(arrayFont[i]);
//            FontsView.changeFontStyle(getApplicationContext(), textValue);
//        }
//
//        int[] arrayFontRegular = {R.id.tv_reset_password, R.id.btn_update_email};
//        TextView textValueRegular;
//        for (int i = 0; i < arrayFontRegular.length; i++) {
//            textValueRegular = (TextView) findViewById(arrayFontRegular[i]);
//            FontsView.changeFontStyleRegular(getApplicationContext(), textValueRegular);
//        }
//    }


}
