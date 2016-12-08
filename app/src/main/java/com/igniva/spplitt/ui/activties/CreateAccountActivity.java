package com.igniva.spplitt.ui.activties;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.AsyncResult;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.controller.WebServiceClientUploadImage;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.views.RoundedImageView;
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
 * Created by igniva-php-08 on 3/5/16.
 */
public class CreateAccountActivity extends BaseActivity implements AsyncResult {
    TextView mToolbarTvText;
    TextView mTvCities;
    TextView mTvCountries;
    EditText mEtUsername, mEtPassword, mEtEmail;
    RadioGroup mRgGender;
    ScrollView mSvMmain;
    RadioButton mRbSelected;
    Button mBtnRegister;
    static String mImageName;
    RoundedImageView mRivUserImage;
    static Bitmap myBitmap;
    TextView mTvStates;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static CreateAccountActivity createAccount;
    String countryId;
    String countryName;
    String stateId;
    String stateName;
    String cityId;
    Intent in;
    boolean showStaticFields;
    static CheckBox mCbTerms;
    TextView mTvTerms;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            //coming from city adapter
            if(PreferenceHandler.readInteger(this, PreferenceHandler.SHOW_EDIT_PROFILE, 1)==10){
                showStaticFields=true;
            }
            PreferenceHandler.writeInteger(this, PreferenceHandler.SHOW_EDIT_PROFILE, 1);
            createAccount = this;

            //to open ot screen
            if (PreferenceHandler.readInteger(this, PreferenceHandler.OTP_SCREEN_NO, 0) == 1) {
                startActivity(new Intent(getApplicationContext(), OtpConfirmationActivity.class));
            }
            in = getIntent();
            setUpLayouts();
            setDataInViewLayouts();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    ResponseHandlerListener responseHandlerListener = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            WebNotificationManager.unRegisterResponseListener(responseHandlerListener);
            switch (mUrlNo) {
                case 3://for creating account of a user
                    mBtnRegister.setClickable(true);
                    if (error == null) {
                        // Succes Case
                        createUserAccount(result);
                    } else {
                        // TODO display error dialog
                        new Utility().showErrorDialogRequestFailed(CreateAccountActivity.this);
                    }
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    break;
            }
        }
    };

    @Override
    public void setUpLayouts() {
        mSvMmain = (ScrollView) findViewById(R.id.sv_main);
        mToolbarTvText = (TextView) findViewById(R.id.toolbar_tv_text);
        mRivUserImage = (RoundedImageView) findViewById(R.id.riv_userImage);
        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mEtEmail = (EditText) findViewById(R.id.et_email);
        mRgGender = (RadioGroup) findViewById(R.id.rg_gender);
        mTvCities = (TextView) findViewById(R.id.tv_cities);
        mTvStates = (TextView) findViewById(R.id.tv_states);
        mTvCountries = (TextView) findViewById(R.id.tv_countries);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mCbTerms=(CheckBox)findViewById(R.id.cb_terms);
        mTvTerms=(TextView)findViewById(R.id.tv_terms);
        Utility.hideKeyboard(getApplicationContext(), mEtUsername);
    }




    @Override
    public void setDataInViewLayouts() {
        mToolbarTvText.setText(getResources().getString(R.string.register));
        String redString = getResources().getString(R.string.check_terms);
        mTvTerms.setText(Html.fromHtml(redString));
        if(showStaticFields){
            if(myBitmap!=null){
                mRivUserImage.setImageBitmap(myBitmap);
            }

        if (in != null) {

            if(in.hasExtra("countryId")) {
                countryId = in.getStringExtra("countryId");
                countryName=in.getStringExtra("countryName");
                mTvCountries.setText(countryName);
            }
            if(in.hasExtra("stateId")) {
                stateId = in.getStringExtra("stateId");
                stateName=in.getStringExtra("stateName");
                mTvStates.setText(stateName);
            }
            if(in.hasExtra("cityId")) {
                cityId = in.getStringExtra("cityId");
                mTvCities.setText(in.getStringExtra("cityName"));
            }
            if(in.hasExtra("userName")) {
                mEtUsername.setText(in.getStringExtra("userName"));
            }
            if(in.hasExtra("userPassword")) {
                mEtPassword.setText(in.getStringExtra("userPassword"));
            }
            if(in.hasExtra("userEmail")) {
                mEtEmail.setText(in.getStringExtra("userEmail"));
            }
            if(in.hasExtra("userGender")) {
                mRbSelected = (RadioButton) findViewById(in.getIntExtra("userGender", 0));
                mRbSelected.setChecked(true);
            }
        }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.riv_userImage:
                if (Permissions.checkPermissionCamera(CreateAccountActivity.this)) {
                    onPickImage();
                }
                break;
            case R.id.tv_login_here:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
            case R.id.tv_track_location:
//                updateLocation();
                break;
            case R.id.toolbar_btn_back:
                onBackPressed();
                break;
            case R.id.btn_register:
                boolean val = new Validations().isValidate(getApplicationContext(), mSvMmain, myBitmap, mRivUserImage, mEtUsername, mEtPassword, mEtEmail, countryId, stateId,cityId,mCbTerms);
                if (val) {
                    mBtnRegister.setClickable(false);
                    // Step 1, Register Callback Interface
                    WebNotificationManager.registerResponseListener(responseHandlerListener);
                    uploadBitmapAsMultipart();
                }
                break;
            case R.id.tv_countries:
                try {
                    Intent in = new Intent(getApplicationContext(), CountryActivity.class);

                    if (!mEtUsername.getText().toString().trim().equals("")) {
                        in.putExtra("userName", mEtUsername.getText().toString());
                    }
                    if (!mEtPassword.getText().toString().trim().equals("")) {
                        in.putExtra("userPassword", mEtPassword.getText().toString());
                    }
                    if (!mEtEmail.getText().toString().trim().equals("")) {
                        in.putExtra("userEmail", mEtEmail.getText().toString());
                    }
                    in.putExtra("userGender", mRgGender.getCheckedRadioButtonId());
                    in.putExtra("from", "1");
                    startActivity(in);
//                    startActivityForResult(in, 2);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.tv_states:
                if(countryId!=null){
                    Intent in=new Intent(CreateAccountActivity.this,StateActivity.class);
                    in.putExtra("userName",mEtUsername.getText().toString());
                    in.putExtra("userPassword",mEtPassword.getText().toString());
                    in.putExtra("userEmail",mEtEmail.getText().toString());
                    in.putExtra("userGender",mRgGender.getCheckedRadioButtonId());
                    in.putExtra("country_id",countryId);
                    in.putExtra("country_name",countryName);
                    in.putExtra("from", "1");
                    startActivity(in);
                }else{
                    Utility.showToastMessageShort(this,getResources().getString(R.string.msg_select_country));
                }
                break;
            case R.id.tv_cities:
                if(stateId!=null){
                    Intent in=new Intent(CreateAccountActivity.this,CityActivity.class);
                    in.putExtra("userName",mEtUsername.getText().toString());
                    in.putExtra("userPassword",mEtPassword.getText().toString());
                    in.putExtra("userEmail",mEtEmail.getText().toString());
                    in.putExtra("userGender",mRgGender.getCheckedRadioButtonId());
                    in.putExtra("countryId",countryId);
                    in.putExtra("countryName",countryName);
                    in.putExtra("stateId",stateId);
                    in.putExtra("stateName",stateName);
                    in.putExtra("from", "1");
                    startActivity(in);
                }else{
                    Utility.showToastMessageShort(this,getResources().getString(R.string.msg_select_country_state));
                }
                break;
            case R.id.tv_terms:
                Intent in=new Intent(CreateAccountActivity.this,TermsOfUseActivity.class);
                startActivity(in);
                break;
        }
    }


    //Image Picker
    public void onPickImage() {
        try {
            Intent chooseImageIntent = ImagePicker.getPickImageIntent(getApplicationContext());
            startActivityForResult(chooseImageIntent, REQUEST_IMAGE_CAPTURE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == 10) {
//                if(resultCode == Activity.RESULT_OK){
                    Log.v("======",data+"");
                    String result=data.getStringExtra("result");
                    Log.v("======",result);
                    Utility.showToastMessageShort(this,result);
//                }

            }else {
                Uri selectedImageURI = null;
                if (data != null) {
                    selectedImageURI = data.getData();
                }
                if (selectedImageURI != null) {
                    mImageName = Utility.getRealPathFromURI(getApplicationContext(), selectedImageURI);

                    if (mImageName.toLowerCase().endsWith(".png") || mImageName.toLowerCase().endsWith(".jpeg") || mImageName.toLowerCase().endsWith(".jpg")) {
                        Bitmap bitmap = ImagePicker.getImageFromResult(getApplicationContext(), resultCode, data);
                        if (bitmap != null) {
                            myBitmap = bitmap;
                            mRivUserImage.setImageBitmap(bitmap);
                            mRivUserImage.setBorderColor(Color.WHITE);

                        }
                    } else {
                        Utility.showToastMessageShort(CreateAccountActivity.this, getResources().getString(R.string.err_invalid_image));
                    }
                } else {
                    mImageName = "user.jpg";
                    Bitmap bitmap = ImagePicker.getImageFromResult(getApplicationContext(), resultCode, data);
                    if (bitmap != null) {
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void uploadBitmapAsMultipart() {
        try {
            String filename = mImageName.toLowerCase();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            if (filename.endsWith(".png"))
                myBitmap.compress(Bitmap.CompressFormat.PNG, 80, bos);
            else if (filename.endsWith(".jpeg") || filename.endsWith(".jpg"))
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            else
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);


            ContentBody contentPart = new ByteArrayBody(bos.toByteArray(), filename);
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("fileToUpload", contentPart);
            new WebServiceClientUploadImage(CreateAccountActivity.this, this, WebServiceClient.MULTIPART_URL, reqEntity, 3).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //saving data to json...
    private String createSignUpPayload(String pictureUrl) {
        String payload = null;
        try {
             //get gender
            int selectedId = mRgGender.getCheckedRadioButtonId();
            mRbSelected = (RadioButton) findViewById(selectedId);
            String mGender = "";
            if (mRbSelected.getText().toString().trim().equals("Male")) {
                mGender = "m";
            } else {
                mGender = "f";
            }

            //get registration_by
            String registration_by = "";
            String email = "";
            String mobileno = "";
            registration_by = "email";
            email = mEtEmail.getText().toString().trim();
            JSONObject userData = new JSONObject();
            try {
                userData.put("email", email);
                userData.put("first_name", "");
                userData.put("last_name", "");
                userData.put("username", mEtUsername.getText().toString().trim());
                userData.put("password", mEtPassword.getText().toString().trim());
                userData.put("picture", pictureUrl);
                userData.put("gender", mGender);
                userData.put("registration_type", "Normal");
                userData.put("registration_by", registration_by);
                userData.put("country", countryId);
                userData.put("state", stateId);
                userData.put("latitude", "");
                userData.put("longitude", "");
                userData.put("city", cityId);
                userData.put("age", "");
                userData.put("postal_code", "");
                userData.put("device_type", "android");
                userData.put("device_id", PreferenceHandler.readString(this, PreferenceHandler.IMEI_NO, ""));
                userData.put("gcm_id", PreferenceHandler.readString(this, PreferenceHandler.GCM_REG_ID, ""));
                userData.put("mobile", mobileno);
                userData.put("currencyId", countryId);

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.e("response", "" + userData);
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
            e.printStackTrace();
        }
        return payload;
    }



    private void createUserAccount(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
                //Error
                new Utility().showErrorDialog(this, result);
            } else {//Success
                DataPojo dataPojo = result.getData();
//                if (dataPojo.getRegistration_by().equals("email")) {
                new Utility().showCreateAccountDialog(this, result);
//                } else {
//                    PreferenceHandler.writeInteger(this, PreferenceHandler.OTP_SCREEN_NO, 1);
//                    PreferenceHandler.writeString(this, PreferenceHandler.TEMP_MOBILE_NO, mEtEmail.getText().toString());
//                    PreferenceHandler.writeString(this, PreferenceHandler.TEMP_USER_ID, dataPojo.getUser_id());
//                    PreferenceHandler.writeString(this, PreferenceHandler.TEMP_OTP, dataPojo.getRequest_id());
//                    Intent intent = new Intent(getApplicationContext(), OtpConfirmationActivity.class);
//                    startActivity(intent);
//                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskResponse(Object result, int urlResponseNo) {
        switch (urlResponseNo) {
            case 3://after uploading image
                signUpUser(result.toString());
                break;

        }

    }

    private void signUpUser(String result) {
        // Step 2, Call Webservice Method
        WebServiceClient.signUpUser(this, createSignUpPayload(result), true, 3, responseHandlerListener);
    }

    /////cities data
    //saving data to json...
    private String createCityListPayload(String countryId) {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            userData.put("country_id", countryId);

            Log.e("rseponse", "" + userData);
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
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
                    Toast.makeText(CreateAccountActivity.this, "Please consider granting it storage permission",
                            Toast.LENGTH_LONG).show();

                }
            }

            case Permissions.MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Please consider granting it Contacts permission to display your Contacts.",
                            Toast.LENGTH_LONG).show();
                }
                break;

            case Permissions.MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)

                {
                    onPickImage();
                } else

                {
                    Toast.makeText(CreateAccountActivity.this, "Please consider granting it Camera permission to use camera.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
