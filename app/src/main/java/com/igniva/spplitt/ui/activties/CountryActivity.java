package com.igniva.spplitt.ui.activties;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.igniva.spplitt.App;
import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.CountriesListPojo;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.adapters.CountryAdapter;
import com.igniva.spplitt.utils.Utility;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by igniva-php-08 on 4/7/16.
 */
public class CountryActivity extends BaseActivity {
    private static final String LOG_TAG = "CountryActivity";
    TextView mToolbarTvText;
    RecyclerView recyclerView;
    RecyclerViewFastScroller fastScroller;
    CountryAdapter adapter;
    List<CountriesListPojo> listCountries = new ArrayList<CountriesListPojo>();
    String userName;
    String userPassword;
    String userEmail;
    int userGender;

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("CountryActivity", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String from;
    //1=create account;2=edit profile;3=set prefernces;4=post ad;5=dashboard
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recycler_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpLayouts();

    }


    @Override
    public void setUpLayouts() {
        mToolbarTvText = (TextView) findViewById(R.id.toolbar_tv_text);
        mToolbarTvText.setText(getResources().getString(R.string.countries));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        fastScroller = (RecyclerViewFastScroller) findViewById(R.id.fastscroller);
        setCountriesList();

    }

    @Override
    public void setDataInViewLayouts() {
        Intent in = getIntent();

        if(in.hasExtra("userName")) {
            userName=in.getStringExtra("userName");
        }
        if(in.hasExtra("userPassword")) {
            userPassword=in.getStringExtra("userPassword");
        }
        if(in.hasExtra("userEmail")) {
            userEmail=in.getStringExtra("userEmail");
        }
        if(in.hasExtra("userGender")) {
            userGender=in.getIntExtra("userGender",0);
        }
        if(in.hasExtra("from")) {
            from=in.getStringExtra("from");
        }
        adapter = new CountryAdapter(this, listCountries,userName,userPassword,userEmail,userGender,from);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutChildren(final RecyclerView.Recycler recycler, final RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                //TODO if the items are filtered, considered hiding the fast scroller here
                final int firstVisibleItemPosition = findFirstVisibleItemPosition();
                if (firstVisibleItemPosition != 0) {
                    // this avoids trying to handle un-needed calls
                    if (firstVisibleItemPosition == -1)
                    // not initialized, or no items shown, so hide fast-scroller
                        fastScroller.setVisibility(View.GONE);
                    return;
                }
                final int lastVisibleItemPosition = findLastVisibleItemPosition();
                int itemsShown = lastVisibleItemPosition - firstVisibleItemPosition + 1;
                //If all items are shown, hide the fast-scroller
                fastScroller.setVisibility(adapter.getItemCount() > itemsShown ? View.VISIBLE : View.GONE);
            }
        });
        fastScroller.setRecyclerView(recyclerView);
        fastScroller.setViewsToUse(R.layout.recycler_view_fast_scroller__fast_scroller, R.id.fastscroller_bubble, R.id.fastscroller_handle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_btn_back:
                App.getInstance().trackEvent(LOG_TAG, "Back Press", "Ad Details Back Pressed");
                onBackPressed();
                break;
        }

    }
    private void setCountriesList() {
        try {
            if (SplashActivity.listCountries == null) {
//               Webservice Call
//              Step 1: Register Callback Interface
                WebNotificationManager.registerResponseListener(responseHandlerListener);
//              Step 2: Call Webservice Method
                WebServiceClient.getCountriesList(this, "", true, 1, responseHandlerListener);
            } else {
                listCountries = SplashActivity.listCountries;

                setDataInViewLayouts();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseHandlerListener responseHandlerListener = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            WebNotificationManager.unRegisterResponseListener(responseHandlerListener);
            switch (mUrlNo) {
                case 1:
                    //to get countries list
                    if (error == null) {
                        getCountriesData(result);
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
    private void getCountriesData(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
                //Error
                new Utility().showErrorDialog(this, result);
            } else {
                //Success
                DataPojo dataPojo = result.getData();
                listCountries = dataPojo.getCountries();
                setDataInViewLayouts();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.getItem(0);
        Drawable icon = item.getIcon();
        Utility.applyTint(icon);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_search:
                final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if(!newText.toString().trim().equals("")) {
                            adapter.getFilter().filter(newText);
                        }else{
                            adapter = new CountryAdapter(CountryActivity.this, listCountries,  userName, userPassword, userEmail, userGender, from);
                            recyclerView.setAdapter(adapter);
                        }
                        return false;
                    }
                });
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
