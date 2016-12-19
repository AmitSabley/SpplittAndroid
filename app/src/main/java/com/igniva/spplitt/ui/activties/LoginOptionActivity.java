package com.igniva.spplitt.ui.activties;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.igniva.spplitt.App;
import com.igniva.spplitt.R;



/**
 * Created by igniva-php-08 on 3/5/16.
 */
public class LoginOptionActivity extends BaseActivity {
    private static final String LOG_TAG = "LoginOptionActivity";

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("LoginOptionActivity", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_option);
    }

    @Override
    public void setUpLayouts() {
    }

    @Override
    public void setDataInViewLayouts() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                App.getInstance().trackEvent(LOG_TAG, "Login User Call", "Login User Called");
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                break;
            case R.id.btn_register:
                App.getInstance().trackEvent(LOG_TAG, "Register User Call", "Create Account Called");
                startActivity(new Intent(getApplicationContext(),CreateAccountActivity.class));
                break;
        }
    }


}
