package com.igniva.spplitt.ui.activties;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by igniva-php-08 on 3/5/16.
 */
public abstract class BaseActivity extends AppCompatActivity {
    //while using appcontactActivity translucent toolbar does not come
    public abstract void setUpLayouts();
    public abstract void setDataInViewLayouts();
    public abstract void onClick(View v);
}
