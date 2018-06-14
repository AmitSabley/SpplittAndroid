package com.igniva.spplitt.ui.fragments;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.igniva.spplitt.App;
import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.AppliedlistPojo;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ErrorPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.activties.MainActivity;
import com.igniva.spplitt.ui.adapters.AppliedAdsListAdapter;
import com.igniva.spplitt.utils.Constants;
import com.igniva.spplitt.utils.Log;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igniva-php-08 on 21/6/16.
 */
public class AwaitedAdFragment extends BaseFragment implements View.OnClickListener {
    private static final String LOG_TAG = "AwaitedAdFragment";
    View mView;
    RecyclerView mRvAds;
    boolean _areLecturesLoaded = false;
    TextView mTvNoAdsFound;
    List<AppliedlistPojo> allAdsList = new ArrayList<AppliedlistPojo>();
    List<AppliedlistPojo> tempAdDataList = new ArrayList<AppliedlistPojo>();
    List<AppliedlistPojo> searchResultAdsList = new ArrayList<AppliedlistPojo>();
    List<AppliedlistPojo> tempListAds = new ArrayList<AppliedlistPojo>();
    AppliedAdsListAdapter mUserAdapter;
    SearchView mSearchView;
    boolean isSearch;
    boolean isLoadMore;
    ProgressBar mProgressBar;
    public static boolean isDataLoaded;
    //    call type 1= all ads,calltype=2 only searched ad result
    private int mCallType = 1;

    public static AwaitedAdFragment newInstance() {
        AwaitedAdFragment fragment = new AwaitedAdFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_ads_awaited, container, false);
        try {
            setUpLayouts();
            setDataInViewLayouts();
            setHasOptionsMenu(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mView;
    }

    private void getActiveAds(boolean showProgress) {
        try {
            mCallType = 1;
//            Webservice Call
//            Step 1, Register Callback Interface
            WebNotificationManager.registerResponseListener(responseHandlerListenerViewAD);
//            Step 2, Call Webservice Method
            WebServiceClient.getMyAppliedAdsList(getActivity(), awaitedAdsPayload(), showProgress, 1, responseHandlerListenerViewAD);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setUpLayouts() {
        try {
            mRvAds = (RecyclerView) mView.findViewById(R.id.rv_applied_ads);
            mTvNoAdsFound = (TextView) mView.findViewById(R.id.tv_no_ads_found);
            mTvNoAdsFound.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDataInViewLayouts() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currentFragmentId = Constants.FRAG_ID_MY_ADS_ACTIVE;
        Log.e("AwaitedAdFragment", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String awaitedAdsPayload() {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("ads_type", "awaited");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //URL: http://spplitt.ignivastaging.com/ads/getBuyerAppliedAds
            //Payload: {"user_id":"19","auth_token":"2CUWSLXOU1QY","ads_type":"awaited"}
            Log.e("Response Awaited Ads", "" + userData);
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }

    ResponseHandlerListener responseHandlerListenerViewAD = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            try {
                WebNotificationManager.unRegisterResponseListener(responseHandlerListenerViewAD);
                if (error == null) {
                    switch (mUrlNo) {
                        case 1:
                            //To get categories list
                            getAdsData(result);
                            break;
                        case 2:
                            //To search ad list
                            getAdsData(result);
                            break;
                    }
                } else {
                    // TODO display error dialog
                    new Utility().showErrorDialogRequestFailed(getActivity());
                }
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                new Utility().showErrorDialogRequestFailed(getActivity());
            }
        }
    };

    private void getAdsData(ResponsePojo result) {
        try {
            isSearch = false;
            if (result.getStatus_code() == 400) {
                ErrorPojo errorPojo = result.getError();
                if (errorPojo.getError_code().equals("533")) {
                    mRvAds.setAdapter(null);
                    mTvNoAdsFound.setVisibility(View.VISIBLE);
                    mRvAds.setVisibility(View.GONE);

                } else {
                    new Utility().showErrorDialog(getActivity(), result);
                }
            } else {//Success
                DataPojo dataPojo = result.getData();

                if (mCallType == 1) {
                    allAdsList = dataPojo.getAppliedlist();
                    if (allAdsList.size() > 0) {
                        tempAdDataList.addAll(allAdsList);
                        mRvAds.setVisibility(View.VISIBLE);
                        mTvNoAdsFound.setVisibility(View.GONE);
                        mUserAdapter = new AppliedAdsListAdapter(getActivity(), allAdsList, getResources().getString(R.string.awaited_ads), mRvAds, tempAdDataList);

                    }
                } else {
                    searchResultAdsList.clear();
                    searchResultAdsList = dataPojo.getAppliedlist();
                    if (searchResultAdsList.size() > 0) {
                        mRvAds.setVisibility(View.VISIBLE);
                        mTvNoAdsFound.setVisibility(View.GONE);
                        mUserAdapter = new AppliedAdsListAdapter(getActivity(), searchResultAdsList, getResources().getString(R.string.awaited_ads), mRvAds, tempAdDataList);

                    }
                }

                mRvAds.setAdapter(mUserAdapter);
                mUserAdapter.notifyDataSetChanged();
                mRvAds.setHasFixedSize(true);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                mRvAds.setLayoutManager(mLayoutManager);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_my_ads, menu);
        MenuItem item = menu.getItem(0);
        Drawable icon = item.getIcon();
        Utility.applyTint(icon);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_search:
                App.getInstance().trackEvent(LOG_TAG, "Search Awaited Ads", "Search Awaited Ads");
                //do Search here
                mSearchView = (SearchView) MenuItemCompat.getActionView(item);
                SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
                mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
                mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Utility.hideKeyboard(getActivity(), mSearchView);
                        mSearchView.clearFocus();
                        mCallType = 2;
//                        Webservice Call
//                        Step 1, Register Callback Interface
                        WebNotificationManager.registerResponseListener(responseHandlerListenerViewAD);
                        // Step 2, Call Webservice Method
                        WebServiceClient.getSearchAppliedAdsList(getActivity(), searchAdListPayload(query), true, 2, responseHandlerListenerViewAD);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });

                final EditText mEtSearchView = (EditText) mSearchView.findViewById(R.id.search_src_text);
                // Get the search close button image view
                ImageView closeButton = (ImageView) mSearchView.findViewById(R.id.search_close_btn);

                // Set on click listener
                closeButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        isSearch = true;
                        mEtSearchView.setText("");
                        if (allAdsList.size() > 0) {
                            mRvAds.setVisibility(View.VISIBLE);
                            mTvNoAdsFound.setVisibility(View.GONE);
                            mUserAdapter = new AppliedAdsListAdapter(getActivity(), allAdsList, getResources().getString(R.string.awaited_ads), mRvAds, tempAdDataList);
                            mRvAds.setAdapter(mUserAdapter);

                        }

                    }
                });

                MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {

                        if (allAdsList.size() > 0) {
                            mRvAds.setVisibility(View.VISIBLE);
                            mTvNoAdsFound.setVisibility(View.GONE);
                            mUserAdapter = new AppliedAdsListAdapter(getActivity(), allAdsList, getResources().getString(R.string.awaited_ads), mRvAds, tempAdDataList);
                            mRvAds.setAdapter(mUserAdapter);
                            mUserAdapter.notifyDataSetChanged();

                        }
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }
                });


                return true;
        }
        return false;
    }


    private String searchAdListPayload(String searchText) {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("ads_type", "awaited_search");
                userData.put("search", searchText);
            } catch (Exception e) {
                e.printStackTrace();
            }
            android.util.Log.e("Awaited Response", "" + userData);
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !_areLecturesLoaded) {
            getActiveAds(true);
            _areLecturesLoaded = true;
        } else {
            if (tempAdDataList.size() > 0) {
                mUserAdapter = new AppliedAdsListAdapter(getActivity(), tempAdDataList, getResources().getString(R.string.awaited_ads), mRvAds, tempAdDataList);
                mRvAds.setAdapter(mUserAdapter);
                mUserAdapter.notifyDataSetChanged();
                mTvNoAdsFound.setVisibility(View.GONE);
                mRvAds.setVisibility(View.VISIBLE);
            }
        }
    }


}