package com.igniva.spplitt.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.igniva.spplitt.App;
import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.activties.MainActivity;
import com.igniva.spplitt.utils.Constants;
import com.igniva.spplitt.utils.Log;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;
import com.igniva.spplitt.utils.Validations;

import org.json.JSONObject;

/**
 * Created by igniva-php-08 on 18/5/16.
 */
public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener {
    private static final String LOG_TAG = "ChangePasswordFragment" ;
    EditText mEtOldPassword, mEtNewPassword;
    Button mBtnChangePassword;
    View view;

    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_password, container, false);
        setUpLayouts();
        return view;
    }

    @Override
    public void setUpLayouts() {
        mEtNewPassword = (EditText) view.findViewById(R.id.et_new_password);
        mEtOldPassword = (EditText) view.findViewById(R.id.et_old_password);
        mBtnChangePassword = (Button) view.findViewById(R.id.btn_change_password);
        mBtnChangePassword.setOnClickListener(this);
    }

    @Override
    public void setDataInViewLayouts() {
        mEtNewPassword.setText("");
        mEtOldPassword.setText("");
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btn_change_password:
                    boolean val = new Validations().isValidateChangePassword(getActivity(), mEtOldPassword, mEtNewPassword);
                    if (val) {
                        App.getInstance().trackEvent(LOG_TAG, "Change Password", "Change Password Called");

//                        Webservice Call
//                        Step 1, Register Callback Interface
                        WebNotificationManager.registerResponseListener(responseHandlerListenerChangePassword);
//                        Step 2, Call Webservice Method
                        WebServiceClient.changePassword(getActivity(), createChangePasswordPayload(), true, 1, responseHandlerListenerChangePassword);


                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String createChangePasswordPayload() {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("old_password", mEtOldPassword.getText().toString().trim());
                userData.put("new_password", mEtNewPassword.getText().toString().trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
            com.igniva.spplitt.utils.Log.e("Response", "" + userData);
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }

    ResponseHandlerListener responseHandlerListenerChangePassword = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            try {
                WebNotificationManager.unRegisterResponseListener(responseHandlerListenerChangePassword);
                if (error == null) {
                    switch (mUrlNo) {
                        case 1:
                            //To get categories list
                            getChangePasswordData(result);
                            break;
                    }
                } else {
                    // TODO display error dialog
                    new Utility().showErrorDialogRequestFailed(getActivity());
                }
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                new Utility().showErrorDialogRequestFailed(getActivity());
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currentFragmentId = Constants.FRAG_ID_CHANGE_PASSWORD;
        Log.e("ChangePasswordFragment", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getChangePasswordData(ResponsePojo result) {
        try {

            if (result.getStatus_code() == 400) {
                // Error
                new Utility().showErrorDialog(getActivity(), result);
            } else {
                // Success
                new Utility().showSuccessDialog(getActivity(), result);
                setDataInViewLayouts();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
