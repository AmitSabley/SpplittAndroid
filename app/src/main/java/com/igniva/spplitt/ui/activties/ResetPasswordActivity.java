package com.igniva.spplitt.ui.activties;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.igniva.spplitt.R;
import com.igniva.spplitt.utils.Validations;

import java.util.LinkedHashMap;


/**
 * Created by igniva-php-08 on 6/5/16.
 */
public class ResetPasswordActivity extends BaseActivity  {
    TextView mToolbarTvText;
    EditText mEtNewPassword;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        //set Layouts
        setUpLayouts();
//        setFontStyle();
        setDataInViewLayouts();
    }

    @Override
    public void setUpLayouts() {
        mToolbarTvText=(TextView)findViewById(R.id.toolbar_tv_text);
        mEtNewPassword=(EditText)findViewById(R.id.et_new_password);
    }

    @Override
    public void setDataInViewLayouts() {
        mToolbarTvText.setText(getResources().getString(R.string.change_password));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_btn_back:
                onBackPressed();
                break;
            case R.id.btn_change_password:
                boolean val = new Validations().isValidateResetPassword(getApplicationContext(),mEtNewPassword);
                if (val) {
                    addDataToJson();
                }
                break;
        }

    }

//    @Override
//    public void setFontStyle() {
//        int[] arrayFont = { R.id.tv_reset_password_text,R.id.et_new_password};
//        TextView textValue;
//        for (int i = 0; i < arrayFont.length; i++) {
//            textValue = (TextView) findViewById(arrayFont[i]);
//            FontsView.changeFontStyle(getApplicationContext(), textValue);
//        }
//
//        int[] arrayFontRegular={R.id.tv_reset_password,R.id.btn_change_password,R.id.toolbar_tv_text};
//        TextView textValueRegular;
//        for (int i = 0; i < arrayFontRegular.length; i++) {
//            textValueRegular = (TextView) findViewById(arrayFontRegular[i]);
//            FontsView.changeFontStyleRegular(getApplicationContext(), textValueRegular);
//        }
//    }
    //saving data to json...
    private void addDataToJson() {
        //get registration_by

        LinkedHashMap userData = new LinkedHashMap();
        try {
//            userData.put("user_id",mEtEmail.getText().toString().trim());
//            userData.put("auth_token",mEtEmail.getText().toString().trim());
            userData.put("new_password", mEtNewPassword.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("response",""+userData);
//        executeAsyncClass(userData);
    }
    //initializing async class
//    public void executeAsyncClass(LinkedHashMap userData) {
//        //URL+MethodName+&
//        String URL = Constants.RESET_PASSWORD_URL + "&";
//        CommonAsync asyncClass = new CommonAsync(getApplicationContext(), URL, this, userData,1);
//        asyncClass.execute();
//    }
//    @Override
//    public void onTaskResponse(Object result, int urlResponseNo) {
//        switch (urlResponseNo){
//            case 1:
//                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
//                break;
//        }
//    }
}
