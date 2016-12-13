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
import android.widget.ProgressBar;
import android.widget.TextView;

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
    View mView;
    RecyclerView mRvAds;
    boolean _areLecturesLoaded = false;
    TextView mTvNoAdsFound;
    List<AppliedlistPojo> listAds = new ArrayList<AppliedlistPojo>();
    List<AppliedlistPojo> beforeFilterListAds = new ArrayList<AppliedlistPojo>();
    AppliedAdsListAdapter mUserAdapter;
    SearchView mSearchView;
    boolean isLoadMore;
    ProgressBar mProgressBar;
    public static boolean isDataLoaded;

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
                            getSearchAdsData(result);
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


    private void getSearchAdsData(ResponsePojo result) {
        try {
            if (mSearchView != null) {
                Utility.hideKeyboard(getActivity(), mSearchView);
            }
            if (result.getStatus_code() == 400) {
                mRvAds.setAdapter(null);
                mTvNoAdsFound.setVisibility(View.VISIBLE);
                mRvAds.setVisibility(View.GONE);
                //Error
                ErrorPojo errorPojo = result.getError();
                if (!errorPojo.getError_code().equals("533")) {
                    new Utility().showErrorDialog(getActivity(), result);
                }
            } else {//Success
                if (listAds.size() > 0) {
                    listAds.clear();
                }
                DataPojo dataPojo = result.getData();
                listAds.addAll(dataPojo.getAppliedlist());

                if (listAds.size() > 0) {
                    mTvNoAdsFound.setVisibility(View.GONE);
                    mRvAds.setVisibility(View.VISIBLE);
                    mRvAds.setAdapter(mUserAdapter);
                    mUserAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAdsData(ResponsePojo result) {
        try {

            if (result.getStatus_code() == 400) {
                isDataLoaded = true;
                if (mProgressBar != null) {
                    if (mProgressBar.getVisibility() == View.VISIBLE) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                }
                //Error
                ErrorPojo errorPojo = result.getError();
                if (errorPojo.getError_code().equals("533")) {
                    mRvAds.setAdapter(null);
                    mTvNoAdsFound.setVisibility(View.VISIBLE);
                    mRvAds.setVisibility(View.GONE);

                } else {
                    new Utility().showErrorDialog(getActivity(), result);
                }
            } else {//Success
                if (listAds.size() > 0) {
                    listAds.remove(listAds.size() - 1);
                    mUserAdapter.notifyItemRemoved(listAds.size());
                }
                DataPojo dataPojo = result.getData();
                listAds.addAll(dataPojo.getAppliedlist());
                beforeFilterListAds.addAll(listAds);
                if (listAds.size() > 0) {
                    if (!isLoadMore) {
                        setDataToList();
                        isLoadMore = true;
                    } else {
                        isDataLoaded = true;
                        if (mProgressBar != null) {
                            if (mProgressBar.getVisibility() == View.VISIBLE) {
                                mProgressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                        mUserAdapter.notifyDataSetChanged();
                    }
                    mTvNoAdsFound.setVisibility(View.GONE);
                    mRvAds.setVisibility(View.VISIBLE);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataToList() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRvAds.setLayoutManager(mLayoutManager);
        mUserAdapter = new AppliedAdsListAdapter(getActivity(), listAds, getResources().getString(R.string.awaited_ads), mRvAds);
        mRvAds.setAdapter(mUserAdapter);
        mRvAds.setHasFixedSize(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_my_ads, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_search:
                //do sth here
                mSearchView = (SearchView) MenuItemCompat.getActionView(item);
                SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
                mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
                mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Utility.hideKeyboard(getActivity(), mSearchView);
                        mSearchView.clearFocus();
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
            android.util.Log.e("rseponse", "" + userData);
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
            if (listAds != null) {
                listAds.clear();
                listAds.addAll(beforeFilterListAds);
            }
            if (mUserAdapter != null) {
                mUserAdapter.notifyDataSetChanged();
            }
        }
    }


}