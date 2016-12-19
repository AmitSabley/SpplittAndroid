package com.igniva.spplitt.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;
import com.igniva.spplitt.utils.Validations;

import org.json.JSONObject;

/**
 * Created by igniva-php-08 on 18/5/16.
 */
public class ContactUsFragment extends BaseFragment implements View.OnClickListener {
    private static final String LOG_TAG = "ContactUsFragment";
    View mView;
    EditText mTvUsername;
    EditText mTvEmail;
    EditText mEtDesc;
    Button mBtnSubmit;

    public static ContactUsFragment newInstance() {
        ContactUsFragment fragment = new ContactUsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_contact_us, container, false);
        setUpLayouts();
        setDataInViewLayouts();
        return mView;
    }

    @Override
    public void setUpLayouts() {
        mTvUsername = (EditText) mView.findViewById(R.id.tv_username);
        mTvEmail = (EditText) mView.findViewById(R.id.tv_email_address);
        mEtDesc = (EditText) mView.findViewById(R.id.et_ad_desc);
        mBtnSubmit = (Button) mView.findViewById(R.id.btn_submit_contact_us);
        mBtnSubmit.setOnClickListener(this);
    }

    @Override
    public void setDataInViewLayouts() {
        mTvUsername.setText(PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_NAME, ""));
        mTvEmail.setText(PreferenceHandler.readString(getActivity(), PreferenceHandler.EMAIL, ""));
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currentFragmentId = Constants.FRAG_ID_CONTACT_US;
        Log.e("ContactUsFragment", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_contact_us:
                boolean val = new Validations().isValidateContactUs(getActivity(), mEtDesc);
                if (val) {
                    App.getInstance().trackEvent(LOG_TAG, "Contact Us Details Submit", "Contact Us Details Submit");

//                   Step 2. Call Webservice Method
                    mBtnSubmit.setClickable(false);
//                   Webservice Call
//                  Step 1. Register Callback Interface
                    WebNotificationManager.registerResponseListener(responseHandlerListenerContactus);
                    WebServiceClient.contactUs(getActivity(), createContactUsPayload(), true, 1, responseHandlerListenerContactus);
                }
                break;
        }
    }

    // Create Contact payload
    private String createContactUsPayload() {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {

                userData.put("user_id", PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("username", PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_NAME, ""));
                userData.put("description", mEtDesc.getText().toString().trim());
                userData.put("email", PreferenceHandler.readString(getActivity(), PreferenceHandler.EMAIL, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
//Url: http://spplitt.ignivastaging.com/users/ContactUs
//Payload: {"user_id":"19","auth_token":"2CUWSLXOU1QY","username":"harry","description":"xxzaXSXaDX","email":"harry@mailinator.com"}
            Log.e("Response Contact Us", "" + userData);
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }

    ResponseHandlerListener responseHandlerListenerContactus = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            try {
                WebNotificationManager.unRegisterResponseListener(responseHandlerListenerContactus);
                if (error == null) {
                    switch (mUrlNo) {
                        case 1:
                            //to get categories list
                            mBtnSubmit.setClickable(true);
                            getContactUsData(result);
                            break;
                    }
                } else {
                    new Utility().showErrorDialogRequestFailed(getActivity());
                }
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            } catch (Exception e) {
                new Utility().showErrorDialogRequestFailed(getActivity());
                e.printStackTrace();
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        }
    };

    private void getContactUsData(ResponsePojo result) {
        if (result.getStatus_code() == 400) {
            //Error
            new Utility().showErrorDialog(getActivity(), result);
        } else {
            //Success
            new Utility().showSuccessDialog(getActivity(), result);
            mEtDesc.setText("");
        }
    }
}
