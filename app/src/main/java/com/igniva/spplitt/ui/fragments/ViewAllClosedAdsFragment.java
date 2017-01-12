package com.igniva.spplitt.ui.fragments;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
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
import android.widget.TextView;

import com.igniva.spplitt.App;
import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.AdsListPojo;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ErrorPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.activties.MainActivity;
import com.igniva.spplitt.ui.adapters.AdsListAdapter;
import com.igniva.spplitt.utils.Constants;
import com.igniva.spplitt.utils.Log;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igniva-php-08 on 14/6/16.
 */
public class ViewAllClosedAdsFragment extends BaseFragment {
    private static final String LOG_TAG = "ViewAllClosedAdsFragment";
    View mView;
    boolean isSearch;
    RecyclerView mRvAds;
    String mAdType = "completed";
    String mAdTypeSearch = "search_completed";
    String mCatId;
    boolean _areLecturesLoaded = false;
    TextView mTvNoAdsFound;
    AdsListAdapter mAdsListAdapter;

    private int mCallType = 1;
    List<AdsListPojo> allAdsList = new ArrayList<AdsListPojo>();
    List<AdsListPojo> searchResultAdsList = new ArrayList<AdsListPojo>();
//    List<AdsListPojo> tempListAds = new ArrayList<AdsListPojo>();

    public static ViewAllClosedAdsFragment newInstance() {
        ViewAllClosedAdsFragment fragment = new ViewAllClosedAdsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_all_ads_active, container, false);
        try {
            setUpLayouts();
            setDataInViewLayouts();
            setHasOptionsMenu(true);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return mView;
    }

    private String postAdGetCategoriesPayload() {
        Bundle mBundle = this.getArguments();
        if (mBundle != null) {//u are coming from categories to view ads
            mCatId = mBundle.getString("cat_id", "");
        } else {// u are coming from view event ads
            mCatId = "1";
        }
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("ads_type", mAdType);
                userData.put("cat_id", mCatId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("Response", "" + userData);
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }


    @Override
    public void setUpLayouts() {
        try {
            mRvAds = (RecyclerView) mView.findViewById(R.id.rv_ads);
            mTvNoAdsFound = (TextView) mView.findViewById(R.id.tv_no_ads_found);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDataInViewLayouts() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currentFragmentId = Constants.FRAG_ID_VIEW_EVENT;
        Log.e("ViewAllClosedAdsFragment", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseHandlerListener responseHandlerListenerViewClosedAD = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            try {
                WebNotificationManager.unRegisterResponseListener(responseHandlerListenerViewClosedAD);
                if (error == null) {
                    switch (mUrlNo) {
                        case 1://to get categories list
                            getAdsData(result);
                            break;
                        case 2:  //to search ad list
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
//                    allAdsList = dataPojo.getAdsList();
                    if (dataPojo.getAdsList().size() > 0) {
                        allAdsList.addAll(dataPojo.getAdsList());
                        mRvAds.setVisibility(View.VISIBLE);
                        mTvNoAdsFound.setVisibility(View.GONE);
                        if (mAdType.equals("completed")) {
                            mAdsListAdapter = new AdsListAdapter(getActivity(), allAdsList, false,allAdsList);
                        } else {
                            mAdsListAdapter = new AdsListAdapter(getActivity(), allAdsList, true,allAdsList);
                        }
                    }
                } else {
                    searchResultAdsList.clear();
                    searchResultAdsList = dataPojo.getAdsList();
                    if (searchResultAdsList.size() > 0) {
                        mRvAds.setVisibility(View.VISIBLE);
                        mTvNoAdsFound.setVisibility(View.GONE);
                        if (mAdType.equals("completed")) {
                            mAdsListAdapter = new AdsListAdapter(getActivity(), searchResultAdsList, false,allAdsList);
                        } else {
                            mAdsListAdapter = new AdsListAdapter(getActivity(), searchResultAdsList, true,allAdsList);
                        }
                    }
                }
//                searchResultAdsList.addAll(allAdsList);

//                tempListAds=searchResultAdsList;//searchResultAdsList;
//                if (allAdsList.size() > 0) {
//                    mRvAds.setVisibility(View.VISIBLE);
//                    mTvNoAdsFound.setVisibility(View.GONE);
//                    if (mAdType.equals("completed")) {
//                        mAdsListAdapter = new AdsListAdapter(getActivity(), allAdsList, false);
//                    } else {
//                        mAdsListAdapter = new AdsListAdapter(getActivity(), allAdsList, true);
//                    }
//
                mRvAds.setAdapter(mAdsListAdapter);
                mAdsListAdapter.notifyDataSetChanged();
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
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_search:
                App.getInstance().trackEvent(LOG_TAG, "Search Closed Ads", "Closed Ads Search");

                //Do Search here
                final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Utility.hideKeyboard(getActivity(), searchView);
                        searchView.clearFocus();
                        //         Webservice Call
                        //         Step 1, Register Callback Interface
                        mCallType = 2;
                        WebNotificationManager.registerResponseListener(responseHandlerListenerViewClosedAD);
                        // Step 2, Call Webservice Method
                        WebServiceClient.getSearchAdsList(getActivity(), searchAdListPayload(query), true, 2, responseHandlerListenerViewClosedAD);

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        /*Utility.hideKeyboard(getActivity(), searchView);
                        searchView.clearFocus();
                        //         Webservice Call
//         Step 1, Register Callback Interface
                        WebNotificationManager.registerResponseListener(responseHandlerListenerViewClosedAD);
                        // Step 2, Call Webservice Method
                        WebServiceClient.getSearchAdsList(getActivity(), searchAdListPayload(newText), true, 2, responseHandlerListenerViewClosedAD);
*/
                        return false;
                    }
                });

                final EditText mEtSearchView = (EditText) searchView.findViewById(R.id.search_src_text);
                // Get the search close button image view
                ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);

                // Set on click listener
                closeButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                        getActiveAds(true);
                        isSearch = true;
                        mEtSearchView.setText("");
//                        getActiveAds(true);
                        if (allAdsList.size() > 0) {
                            mRvAds.setVisibility(View.VISIBLE);
                            mTvNoAdsFound.setVisibility(View.GONE);
//                            allAdsList=tempListAds;

                            if (mAdType.equals("completed")) {
                                mAdsListAdapter = new AdsListAdapter(getActivity(), allAdsList, false,allAdsList);
                            } else {
                                mAdsListAdapter = new AdsListAdapter(getActivity(), allAdsList, true,allAdsList);
                            }

                            mRvAds.setAdapter(mAdsListAdapter);
//                            mAdsListAdapter.notifyDataSetChanged();
                        }
//                        Toast.makeText(getActivity(), tempListAds.size()+"==", Toast.LENGTH_LONG).show();
                    }
                });


                MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {

                        if (allAdsList.size() > 0) {
                            mRvAds.setVisibility(View.VISIBLE);
                            mTvNoAdsFound.setVisibility(View.GONE);
//                            allAdsList=tempListAds;
                            if (mAdType.equals("completed")) {
                                mAdsListAdapter = new AdsListAdapter(getActivity(), allAdsList, false,allAdsList);
                            } else {
                                mAdsListAdapter = new AdsListAdapter(getActivity(), allAdsList, true,allAdsList);
                            }

                            mRvAds.setAdapter(mAdsListAdapter);
                            mAdsListAdapter.notifyDataSetChanged();
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
                userData.put("ads_type", mAdTypeSearch);
                userData.put("cat_id", mCatId);
                userData.put("search", searchText);
            } catch (Exception e) {
                e.printStackTrace();
            }
            android.util.Log.e("Response Expired Ads", "" + userData);
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
            getClosedAds(true);
            _areLecturesLoaded = true;
        } else {
            if (allAdsList.size()>0) {
                mAdsListAdapter = new AdsListAdapter(getActivity(), allAdsList, false,allAdsList);
                mRvAds.setAdapter(mAdsListAdapter);
                mAdsListAdapter.notifyDataSetChanged();
                mTvNoAdsFound.setVisibility(View.GONE);
                mRvAds.setVisibility(View.VISIBLE);
            }


        }
    }

    private void getClosedAds(boolean b) {
        //         Webservice Call
//         Step 1, Register Callback Interface
        mCallType = 1;
        WebNotificationManager.registerResponseListener(responseHandlerListenerViewClosedAD);
        // Step 2, Call Webservice Method
        WebServiceClient.getAdsList(getActivity(), postAdGetCategoriesPayload(), true, 1, responseHandlerListenerViewClosedAD);

    }
}


