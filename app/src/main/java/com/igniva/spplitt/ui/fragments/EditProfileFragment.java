package com.igniva.spplitt.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.AsyncResult;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.controller.WebServiceClientUploadImage;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.activties.CityActivity;
import com.igniva.spplitt.ui.activties.CountryActivity;
import com.igniva.spplitt.ui.activties.MainActivity;
import com.igniva.spplitt.ui.activties.StateActivity;
import com.igniva.spplitt.ui.activties.UpdateEmailActivity;
import com.igniva.spplitt.ui.activties.UpdateMobileActivity;
import com.igniva.spplitt.ui.views.RoundedImageView;
import com.igniva.spplitt.utils.Constants;
import com.igniva.spplitt.utils.ImagePicker;
import com.igniva.spplitt.utils.Permissions;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;
import com.igniva.spplitt.utils.Validations;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by igniva-php-08 on 10/5/16.
 */
public class EditProfileFragment extends BaseFragment implements View.OnClickListener, AsyncResult {
    View view;
    RoundedImageView mRivUserImage;
    static Bitmap myBitmap;
    String mImageName;
    EditText mEtUsername;
    EditText mEtEmail;
    EditText mEtMobileNo;
    EditText mEtAge;
    ToggleButton mTbAge;
    TextView mTvCities;
    TextView mTvCountries;
    TextView mTvStates;
    ImageButton mBtnChangeEmail;
    ImageButton mBtnChangeMobileNo;
    ScrollView mSvmain;
    Button mBtnSubmitprofile;
    String countryId;
    String countryName;
    String stateId;
    String stateName;
    String cityId;
    Intent in;
    static String age;
    static boolean isAge;
    boolean showStaticFields;
    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        try {
            in=getActivity().getIntent();
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
//          Call from city adapter
            if(PreferenceHandler.readInteger(getActivity(), PreferenceHandler.SHOW_EDIT_PROFILE, 1)==0){
                showStaticFields=true;
            }
            PreferenceHandler.writeInteger(getActivity(), PreferenceHandler.SHOW_EDIT_PROFILE, 1);
//            if (PreferenceHandler.readInteger(getActivity(), PreferenceHandler.OTP_SCREEN_NO, 0) == 3) {
//                startActivity(new Intent(getActivity(), OtpConfirmationActivity.class));
//            }
            setUpLayouts();
            setDataInViewLayouts();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    ResponseHandlerListener responseHandlerListener = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            try {
                WebNotificationManager.unRegisterResponseListener(responseHandlerListener);
                switch (mUrlNo) {
                    case 3:
                        //For creating account of a user
                        mBtnSubmitprofile.setClickable(true);
                        if (error == null) {
                            // Success Case
                            updateUserAccount(result);
                        } else {
                            // TODO display error dialog
                            new Utility().showErrorDialogRequestFailed(getActivity());
                        }
                        if (mProgressDialog != null && mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void setUpLayouts() {
        mRivUserImage = (RoundedImageView) view.findViewById(R.id.riv_userImage_edit);
        mRivUserImage.setOnClickListener(this);
        mEtUsername = (EditText) view.findViewById(R.id.et_username_edit);
        mTvCountries = (TextView) view.findViewById(R.id.tv_countries);
        mTvCountries.setOnClickListener(this);
        mTvStates = (TextView) view.findViewById(R.id.tv_states);
        mTvStates.setOnClickListener(this);
        mTvCities = (TextView) view.findViewById(R.id.tv_cities);
        mTvCities.setOnClickListener(this);
        mEtEmail = (EditText) view.findViewById(R.id.et_email_edit);
        mEtMobileNo = (EditText) view.findViewById(R.id.et_mobile_no_edit);
        mEtAge = (EditText) view.findViewById(R.id.et_age_edit);
        mTbAge = (ToggleButton) view.findViewById(R.id.tb_age);
        mBtnChangeEmail = (ImageButton) view.findViewById(R.id.btn_change_email_edit);
        mBtnChangeEmail.setOnClickListener(this);
        mBtnChangeMobileNo = (ImageButton) view.findViewById(R.id.btn_edit_mobileno);
        mBtnChangeMobileNo.setOnClickListener(this);
        mSvmain = (ScrollView) view.findViewById(R.id.sv_main);
        mBtnSubmitprofile = (Button) view.findViewById(R.id.btn_submit_profile_edit);
        mBtnSubmitprofile.setOnClickListener(this);

    }

    @Override
    public void setDataInViewLayouts() {
        try {

            if(!PreferenceHandler.readString(getActivity(), PreferenceHandler.PICTURE, "").equals("")) {
                try {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            try {
                                if(myBitmap==null) {
                                    myBitmap = Glide.
                                            with(getActivity()).
                                            load(WebServiceClient.BASE_URL + PreferenceHandler.readString(getActivity(), PreferenceHandler.PICTURE, "")).
                                            asBitmap().
                                            into(200, 200). // Width and height
                                            get();
                                }else{
                                    Glide.
                                            with(getActivity()).
                                            load(WebServiceClient.BASE_URL + PreferenceHandler.readString(getActivity(), PreferenceHandler.PICTURE, "")).
                                            asBitmap().
                                            into(200, 200). // Width and height
                                            get();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    new Thread(runnable).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Glide.with(getContext())
                        .load(WebServiceClient.BASE_URL + PreferenceHandler.readString(getActivity(), PreferenceHandler.PICTURE, "")).centerCrop().override(200, 200).fitCenter()
                        .into(mRivUserImage);
            }     isAge=true;
            mEtEmail.setText(PreferenceHandler.readString(getActivity(), PreferenceHandler.EMAIL, ""));
            mEtMobileNo.setText(PreferenceHandler.readString(getActivity(), PreferenceHandler.MOBILE_NO, ""));
            mEtUsername.setText(PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_NAME, ""));
            mEtAge.setText(PreferenceHandler.readString(getActivity(), PreferenceHandler.AGE, ""));
            mTvCountries.setText(PreferenceHandler.readString(getActivity(), PreferenceHandler.COUNTRY_NAME, ""));
            mTvStates.setText(PreferenceHandler.readString(getActivity(), PreferenceHandler.STATE_NAME, ""));
            mTvCities.setText(PreferenceHandler.readString(getActivity(), PreferenceHandler.CITY_NAME, ""));
            if (PreferenceHandler.readString(getActivity(), PreferenceHandler.IS_AGE_PUBLIC, "").equals("1")) {
                isAge=true;
                mTbAge.setChecked(true);
            } else {
                mTbAge.setChecked(false);
            }

            countryId=PreferenceHandler.readString(getActivity(), PreferenceHandler.COUNTRY, "");
            stateId=PreferenceHandler.readString(getActivity(), PreferenceHandler.STATE, "");
            cityId=PreferenceHandler.readString(getActivity(), PreferenceHandler.CITY, "");
            countryName=PreferenceHandler.readString(getActivity(), PreferenceHandler.COUNTRY_NAME, "");
            stateName=PreferenceHandler.readString(getActivity(), PreferenceHandler.STATE_NAME, "");
            if(showStaticFields) {
                if (in != null) {
                    if (in.hasExtra("countryId")) {
                        countryId = in.getStringExtra("countryId");
                        countryName = in.getStringExtra("countryName");
                        mTvCountries.setText(countryName);
                    }
                    if (in.hasExtra("stateId")) {
                        stateId = in.getStringExtra("stateId");
                        stateName = in.getStringExtra("stateName");
                        mTvStates.setText(stateName);
                    }
                    if (in.hasExtra("cityId")) {
                        cityId = in.getStringExtra("cityId");
                        mTvCities.setText(in.getStringExtra("cityName"));
                    }
                    if (in.hasExtra("userName")) {
                        mEtUsername.setText(in.getStringExtra("userName"));
                    }
                    if (in.hasExtra("userEmail")) {
                        mEtEmail.setText(in.getStringExtra("userEmail"));
                    }
                }
                if (myBitmap != null) {
                    mRivUserImage.setImageBitmap(myBitmap);
                    Bitmap bitmap = myBitmap;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    Glide.with(getContext())
                            .load(bitmapdata).centerCrop().override(200, 200).fitCenter()
                            .into(mRivUserImage);
                }
                if (age != null) {
                    mEtAge.setText(age);
                }
                if (isAge) {
                    mTbAge.setChecked(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.riv_userImage_edit:
                if (Permissions.checkPermissionCamera(getActivity())) {
                    onPickImage();
                }
                break;
            case R.id.btn_change_email_edit:
                mBtnChangeEmail.setClickable(false);//enabled in on resume
                mBtnChangeMobileNo.setClickable(false);
                startActivity(new Intent(getContext(), UpdateEmailActivity.class));
                break;
            case R.id.btn_edit_mobileno://enabled in on resume
                mBtnChangeEmail.setClickable(false);
                mBtnChangeMobileNo.setClickable(false);
                startActivity(new Intent(getContext(), UpdateMobileActivity.class));
                break;
            case R.id.btn_submit_profile_edit:
                boolean val = new Validations().isValidateEditProfile(getContext(), mSvmain, myBitmap, mEtUsername, mEtAge, countryId, stateId,cityId);
                if (val) {
                    mBtnSubmitprofile.setClickable(false);
                    // Step 1: Register Callback Interface
                    WebNotificationManager.registerResponseListener(responseHandlerListener);
                    if (mImageName == null) {
                    // Step 2: Call Webservice Method
                        WebServiceClient.editProfileUser(getActivity(), editProfilePayload(PreferenceHandler.readString(getActivity(), PreferenceHandler.PICTURE, "")), true, 3, responseHandlerListener);
                    } else {
                        uploadBitmapAsMultipart();
                    }
                }
                break;
            case R.id.tv_countries:
                try {
                    age=mEtAge.getText().toString();
                    isAge=mTbAge.isChecked();
                    Intent in = new Intent(getActivity(), CountryActivity.class);

                    if (!mEtUsername.getText().toString().trim().equals("")) {
                        in.putExtra("userName", mEtUsername.getText().toString());
                    }
                    if (!mEtEmail.getText().toString().trim().equals("")) {
                        in.putExtra("userEmail", mEtEmail.getText().toString());
                    }
                    in.putExtra("from", "2");
                    startActivity(in);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.tv_states:
                if(countryId!=null){
                    age=mEtAge.getText().toString();
                    isAge=mTbAge.isChecked();
                    Intent in=new Intent(getActivity(),StateActivity.class);
                    in.putExtra("userName",mEtUsername.getText().toString());
                    in.putExtra("userEmail",mEtEmail.getText().toString());
                    in.putExtra("country_id",countryId);
                    in.putExtra("country_name",countryName);
                    in.putExtra("from", "2");
                    startActivity(in);
                }else{
                    Utility.showToastMessageShort(getActivity(),getResources().getString(R.string.msg_select_country));
                }
                break;
            case R.id.tv_cities:
                if(stateId!=null){
                    age=mEtAge.getText().toString();
                    isAge=mTbAge.isChecked();
                    Intent in=new Intent(getActivity(),CityActivity.class);
                    in.putExtra("userName",mEtUsername.getText().toString());
                    in.putExtra("userEmail",mEtEmail.getText().toString());
                    in.putExtra("countryId",countryId);
                    in.putExtra("countryName",countryName);
                    in.putExtra("stateId",stateId);
                    in.putExtra("stateName",stateName);
                    in.putExtra("from", "2");
                    startActivity(in);
                }else{
                    Utility.showToastMessageShort(getActivity(),getResources().getString(R.string.msg_select_country_state));
                }
                break;
        }

    }

    //Image Picker
    public void onPickImage() {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(getContext());
        startActivityForResult(chooseImageIntent, 234);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == 0) {
                myBitmap = myBitmap;
            } else {
                Uri selectedImageURI = null;
                if (data != null) {
                    selectedImageURI = data.getData();
                }
                if (selectedImageURI != null) {
                    mImageName = Utility.getRealPathFromURI(getActivity(), selectedImageURI);

                    Log.e("imagename", mImageName);
                    if (mImageName.toLowerCase().endsWith(".png") || mImageName.toLowerCase().endsWith(".jpeg") || mImageName.toLowerCase().endsWith(".jpg")) {
                        Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                        if (bitmap != null) {
                            myBitmap = bitmap;
                            mRivUserImage.setImageBitmap(bitmap);
                            mRivUserImage.setBorderColor(Color.WHITE);

                        }
                    } else {
                        Utility.showToastMessageShort(getActivity(), getResources().getString(R.string.err_invalid_image));
                    }
                } else {
                    mImageName = "user.jpg";
                    Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                    if (bitmap != null) {
//                    Log.e("bitmap",bitmap.get)
                        myBitmap = bitmap;
                        mRivUserImage.setImageBitmap(bitmap);
                        mRivUserImage.setBorderColor(Color.WHITE);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onTaskResponse(Object result, int urlResponseNo) {
        switch (urlResponseNo) {
            case 3:
                signUpUser(result.toString());
                break;
        }
    }

    private void signUpUser(String result) {
        // Step 2, Call Webservice Method
        WebServiceClient.editProfileUser(getActivity(), editProfilePayload(result), true, 3, responseHandlerListener);
    }



    private void uploadBitmapAsMultipart() {
        try {
//            progressDialog = ProgressDialog.show(getActivity(), "", getActivity()
//                            .getResources().getString(R.string.please_wait), true,
//                    false);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            String filename = "";
            if (mImageName != null) {
                filename = mImageName.toLowerCase();
                if (filename.endsWith(".png")) {
                    myBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                } else if (filename.endsWith(".jpeg") || filename.endsWith(".jpg")) {
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                } else {
                    filename = "user.jpg";
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                }
            }
            ContentBody contentPart = new ByteArrayBody(bos.toByteArray(), filename);

            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("fileToUpload", contentPart);
            new WebServiceClientUploadImage(getActivity(), this, WebServiceClient.MULTIPART_URL, reqEntity, 3).execute();
//            String pictureUrl = Common.multipost(WebServiceClient.MULTIPART_URL, reqEntity, bos.toByteArray());
//            progressDialog.dismiss();
//            // Step 2, Call Webservice Method
//            WebServiceClient.editProfileUser(getActivity(), editProfilePayload(pictureUrl), true, 3,responseHandlerListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //saving data to json...
    private String editProfilePayload(String pictureUrl) {
        String payload = null;
        try {
            String isAge = "0";
            if (mTbAge.isChecked()) {
                isAge = "1";
            } else {
                isAge = "0";
            }
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("profile_type", "update");
                userData.put("first_name", "");
                userData.put("last_name", "");
                userData.put("city", cityId);
                userData.put("state_id",stateId);
                userData.put("country", countryId);
                userData.put("postal_code", "");
                userData.put("is_age_public", isAge);
                userData.put("age", mEtAge.getText().toString());
                userData.put("gender", PreferenceHandler.readString(getActivity(), PreferenceHandler.GENDER, ""));
                userData.put("username", mEtUsername.getText().toString().trim());
                userData.put("picture", pictureUrl);

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.v("rseponse", "" + userData);
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }

    private void updateUserAccount(ResponsePojo result) {
        if (result.getStatus_code() == 400) {
            //Error
            new Utility().showErrorDialog(getActivity(), result);
        } else {//Success
//            new Utility().showSuccessDialog(getContext(), result);
            DataPojo dataPojo = result.getData();
            PreferenceHandler.writeString(getActivity(), PreferenceHandler.USER_ID, dataPojo.getUser_id());
            PreferenceHandler.writeString(getActivity(), PreferenceHandler.USER_NAME, dataPojo.getUsername());
//            editor.putString(Constants.EMAIL, dataPojo.getEmail());
//            editor.putString(Constants.MOBILE_NO, dataPojo.getMobile());
            PreferenceHandler.writeString(getActivity(), PreferenceHandler.GENDER, dataPojo.getGender());
            PreferenceHandler.writeString(getActivity(), PreferenceHandler.AGE, dataPojo.getAge());
            PreferenceHandler.writeString(getActivity(), PreferenceHandler.COUNTRY, dataPojo.getCurrency_id());
            PreferenceHandler.writeString(getActivity(), PreferenceHandler.COUNTRY_NAME, dataPojo.getCountry_name());
            PreferenceHandler.writeString(getActivity(), PreferenceHandler.STATE, dataPojo.getState());
            PreferenceHandler.writeString(getActivity(), PreferenceHandler.STATE_NAME, dataPojo.getState_name());
            PreferenceHandler.writeString(getActivity(), PreferenceHandler.CURRENCY_CODE, dataPojo.getCurrency_code());
            PreferenceHandler.writeString(getActivity(), PreferenceHandler.CITY, dataPojo.getCity_id());
            PreferenceHandler.writeString(getActivity(), PreferenceHandler.CITY_NAME, dataPojo.getCity_name());
            PreferenceHandler.writeString(getActivity(), PreferenceHandler.IS_AGE_PUBLIC, dataPojo.getIs_age_public());
            PreferenceHandler.writeString(getActivity(), PreferenceHandler.PICTURE, dataPojo.getPicture());
//            editor.putString(Constants.AUTH_TOKEN, dataPojo.getAuth_token());
            PreferenceHandler.writeString(getActivity(), PreferenceHandler.CURRENCY_ID, dataPojo.getCurrency_id());

            //navigate to home page
            NavigationView navigationView = (NavigationView) MainActivity.main.findViewById(R.id.nav_view);
            ((MainActivity) getActivity()).onNavigationItemSelected(navigationView.getMenu().getItem(0));
            countryId= dataPojo.getCurrency_id();
            stateId=dataPojo.getState();
            cityId=dataPojo.getCity_id();
            countryName=dataPojo.getCountry_name();
            stateName=dataPojo.getState_name();


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currentFragmentId = Constants.FRAG_ID_MY_PROFILE;
        mBtnChangeEmail.setClickable(true);
        mBtnChangeMobileNo.setClickable(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Permissions.MY_PERMISSIONS_REQUEST_ALL:
                break;

            case Permissions.MY_PERMISSIONS_REQUEST_WRITE_READ: {
                if (grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                    //TODO Ask for permission
                    Toast.makeText(getActivity(), "Please consider granting it storage permission",
                            Toast.LENGTH_LONG).show();

                }
            }

            case Permissions.MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getActivity(), "Please consider granting it Contacts permission to display your Contacts.",
                            Toast.LENGTH_LONG).show();
                }
                break;

            case Permissions.MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)

                {
                    onPickImage();
                } else

                {
                    Toast.makeText(getActivity(), "Please consider granting it Camera permission to use camera.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
