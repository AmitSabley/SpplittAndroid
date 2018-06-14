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
import com.igniva.spplitt.model.CityListPojo;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.adapters.CityAdapter;
import com.igniva.spplitt.utils.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by igniva-php-08 on 4/7/16.
 */
public class CityActivity extends BaseActivity {
    private static final String LOG_TAG = "CityActivity";
    TextView mToolbarTvText;
    RecyclerView recyclerView;
    RecyclerViewFastScroller fastScroller;
    CityAdapter adapter;
    String mCountryId;
    String mCountryName;
    String stateId;
    String stateName;
    List<CityListPojo> listCities = new ArrayList<CityListPojo>();
    String userName;

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("CityActivity", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String userPassword;
    String userEmail;
    int userGender;
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recycler_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent in = getIntent();
        mCountryId = in.getStringExtra("countryId");
        mCountryName = in.getStringExtra("countryName");
        stateId = in.getStringExtra("stateId");
        stateName = in.getStringExtra("stateName");

        if (in.hasExtra("userName")) {
            userName = in.getStringExtra("userName");
        }
        if (in.hasExtra("userPassword")) {
            userPassword = in.getStringExtra("userPassword");
        }
        if (in.hasExtra("userEmail")) {
            userEmail = in.getStringExtra("userEmail");
        }
        if (in.hasExtra("userGender")) {
            userGender = in.getIntExtra("userGender", 0);
        }
        if (in.hasExtra("from")) {
            from = in.getStringExtra("from");
        }
        setUpLayouts();
    }


    @Override
    public void setUpLayouts() {
        mToolbarTvText = (TextView) findViewById(R.id.toolbar_tv_text);
        mToolbarTvText.setText(getResources().getString(R.string.cities));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        fastScroller = (RecyclerViewFastScroller) findViewById(R.id.fastscroller);
        setCitiesList();

    }

    @Override
    public void setDataInViewLayouts() {

        adapter = new CityAdapter(this, listCities, mCountryId, mCountryName, stateId, stateName, userName, userPassword, userEmail, userGender, from);
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
                        //not initialized, or no items shown, so hide fast-scroller
                        fastScroller.setVisibility(View.GONE);
                    return;
                }
                final int lastVisibleItemPosition = findLastVisibleItemPosition();
                int itemsShown = lastVisibleItemPosition - firstVisibleItemPosition + 1;
                //if all items are shown, hide the fast-scroller
                fastScroller.setVisibility(adapter.getItemCount() > itemsShown ? View.VISIBLE : View.GONE);
            }
        });
        fastScroller.setRecyclerView(recyclerView);
        fastScroller.setViewsToUse(R.layout.recycler_view_fast_scroller__fast_scroller, R.id.fastscroller_bubble, R.id.fastscroller_handle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_btn_back:
                App.getInstance().trackEvent(LOG_TAG, "Back Press", "Ad Details Back Pressed");
                onBackPressed();
                break;
        }
    }

    private void setCitiesList() {
        try {
//           Webservice Call
//          Step 1: Register Callback Interface
            WebNotificationManager.registerResponseListener(responseHandlerListener);
//          Step 2: Call Webservice Method
            WebServiceClient.getCitiesList(CityActivity.this, createCityListPayload(stateId), true, 1, responseHandlerListener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //     Cities data
//     Saving data to json...
    private String createCityListPayload(String countryId) {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            userData.put("state_id", countryId);
            // Payload: {"state_id":"74"}
            Log.e("Response", "" + userData);
            payload = userData.toString();
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
                    //To retrieve countries list
                    if (error == null) {
                        getCitiesData(result);
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

    private void getCitiesData(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
                //Error
                new Utility().showErrorDialog(this, result);
            } else {
                //Success
                DataPojo dataPojo = result.getData();
                listCities = dataPojo.getCityList();
                setDataInViewLayouts();
//                mArrayListCityName.clear();
//                mArrayListCityId.clear();
//                for (CityListPojo cityListPojo : listCities) {
//                    mArrayListCityName.add(cityListPojo.getCity_name());
//                    mArrayListCityId.add(cityListPojo.getCity_id());
//                }

//                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mArrayListCityName);
//                mSpCities.setAdapter(adapter);
//                adapter.notifyDataSetChanged();

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
                        if (!newText.toString().trim().equals("")) {
                            adapter.getFilter().filter(newText);
                        } else {
                            adapter = new CityAdapter(CityActivity.this, listCities, mCountryId, mCountryName, stateId, stateName, userName, userPassword, userEmail, userGender, from);
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
