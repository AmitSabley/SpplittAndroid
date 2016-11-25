package com.igniva.spplitt.ui.activties;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.igniva.spplitt.R;
import com.igniva.spplitt.ui.activties.BaseActivity;



/**
 * Created by igniva-php-08 on 3/5/16.
 */
public class LoginOptionActivity extends BaseActivity {
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
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                break;
            case R.id.btn_register:
                startActivity(new Intent(getApplicationContext(),CreateAccountActivity.class));
                break;
        }
    }


}
