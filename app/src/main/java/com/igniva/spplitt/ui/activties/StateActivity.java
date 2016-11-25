package com.igniva.spplitt.ui.activties;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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

import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.model.StateListPojo;
import com.igniva.spplitt.ui.adapters.StateAdapter;
import com.igniva.spplitt.utils.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igniva-php-08 on 5/7/16.
 */
public class StateActivity extends BaseActivity {
    TextView mToolbarTvText;
    RecyclerView recyclerView;
    RecyclerViewFastScroller fastScroller;
    StateAdapter adapter;
    String countryId;
    String countryName;
    List<StateListPojo> listStates = new ArrayList<StateListPojo>();
    String userName;
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
        Intent in=getIntent();
        countryId=in.getStringExtra("country_id");
        countryName=in.getStringExtra("country_name");
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
        setUpLayouts();
    }


    @Override
    public void setUpLayouts() {
        mToolbarTvText = (TextView) findViewById(R.id.toolbar_tv_text);
        mToolbarTvText.setText(getResources().getString(R.string.states));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        fastScroller = (RecyclerViewFastScroller) findViewById(R.id.fastscroller);
        setCitiesList();

    }

    @Override
    public void setDataInViewLayouts() {

        adapter = new StateAdapter(this, listStates,countryId,countryName,userName,userPassword,userEmail,userGender,from);
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
        switch (v.getId()){
            case R.id.toolbar_btn_back:
                onBackPressed();
                break;
        }
    }

    private void setCitiesList() {
        try {
            // Webservice Call
            // Step 1, Register Callback Interface
            WebNotificationManager.registerResponseListener(responseHandlerListener);
            // Step 2, Call Webservice Method
            WebServiceClient.getStatesList(StateActivity.this, createStateListPayload(countryId), true, 1, responseHandlerListener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /////cities data
    //saving data to json...
    private String createStateListPayload(String countryId) {
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
    ResponseHandlerListener responseHandlerListener = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            WebNotificationManager.unRegisterResponseListener(responseHandlerListener);
            switch (mUrlNo) {
                case 1://to get countries list
                    if (error == null) {
                        getStatesData(result);
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

    private void getStatesData(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
                //Error
                new Utility().showErrorDialog(this, result);
            } else {//Success
                DataPojo dataPojo = result.getData();
                listStates = dataPojo.getStateList();
                setDataInViewLayouts();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
                            adapter = new StateAdapter(StateActivity.this, listStates,countryId,countryName,  userName, userPassword, userEmail, userGender, from);
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
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        try {
//            super.onActivityResult(requestCode, resultCode, data);
//            if (requestCode == 2) {
//                if(resultCode == Activity.RESULT_OK){
//                    Log.v("======",data+"");
//                    String result=data.getStringExtra("result");
//                    Log.v("======",result);
//                    Utility.showToastMessageShort(this,result);
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
