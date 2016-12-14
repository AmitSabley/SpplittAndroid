package com.igniva.spplitt.ui.activties;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.igniva.spplitt.utils.Utility;
import com.igniva.spplitt.utils.Validations;

import org.json.JSONObject;


/**
 * Created by Jigyasa Saluja on 6/5/16.
 */

public class ChangePasswordActivity extends BaseActivity {
    TextView mToolbarTvText;
    EditText mEtNewPassword;
    Intent in;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        in = getIntent();
//         set Layouts
        setUpLayouts();
//        setFontStyle();
        setDataInViewLayouts();
    }


    /**
     * Layout Setup
     */
    @Override
    public void setUpLayouts() {
        mToolbarTvText = (TextView) findViewById(R.id.toolbar_tv_text);
        mEtNewPassword = (EditText) findViewById(R.id.et_new_password);
    }

    /**
     * Set Data in Views
     */
    @Override
    public void setDataInViewLayouts() {
        mToolbarTvText.setText(getResources().getString(R.string.change_password));
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
                onBackPressed();
                break;
            case R.id.btn_change_password:
                boolean val = new Validations().isValidateResetPassword(getApplicationContext(), mEtNewPassword);
                if (val) {
 //                 Webservice Call
//                  Step 1: Register Callback Interface
                    WebNotificationManager.registerResponseListener(responseHandlerListener);
//                  Step 2: Call Webservice Method
                    WebServiceClient.changePasswordWhenForgot(this, createChangePasswordPayload(), true, 1,responseHandlerListener);
                }
                break;
        }

    }


    /**
     * Change PassWord Payload to be sent
     */
    private String createChangePasswordPayload() {
        String payload = null;
        try {

            JSONObject userData = new JSONObject();
            userData.put("user_id", in.getStringExtra("user_id"));
            userData.put("auth_token", in.getStringExtra("auth_token"));
            userData.put("new_password", mEtNewPassword.getText().toString().trim());
            payload = userData.toString();

            Log.e("response", "" + payload);
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }


    ResponseHandlerListener responseHandlerListener = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            WebNotificationManager.unRegisterResponseListener(responseHandlerListener);
            switch (mUrlNo) {
                case 1:
                    if (error == null) {
                        // Success Case
                        changePassword(result);
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
     * Change PassWord Response
     *
     * @param result
     */
    private void changePassword(ResponsePojo result) {
        try {
            if (result.getStatus_code()==400) {
                //Error
                new Utility().showErrorDialog(this, result);
            } else {
                //Success
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
