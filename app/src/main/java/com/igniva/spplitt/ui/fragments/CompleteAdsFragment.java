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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.AdsListPojo;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ErrorPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.activties.MainActivity;
import com.igniva.spplitt.ui.adapters.MyAdsListAdapter;
import com.igniva.spplitt.utils.Constants;
import com.igniva.spplitt.utils.Log;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CompleteAdsFragment extends BaseFragment implements View.OnClickListener {
    View mView;
    RecyclerView mRvAds;
    String mCatId;
    List<AdsListPojo> completelistAds = new ArrayList<AdsListPojo>();
    List<AdsListPojo> beforeFilterCompletelistAds = new ArrayList<AdsListPojo>();
    boolean _areLecturesLoaded = false;
    TextView mTvNoAdsFound;
    LinearLayout mLlLoading;
    int mPageNo = 1;
    int mPosition;
    MyAdsListAdapter mUserAdapter;
    boolean isLoadMore;
    ProgressBar mProgressBar;
    SearchView searchView;
    public static boolean isDataLoaded;

    public static CompleteAdsFragment newInstance() {
        CompleteAdsFragment fragment = new CompleteAdsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_ads_active, container, false);
        try {
            setUpLayouts();
            setDataInViewLayouts();
            setHasOptionsMenu(true);

            Bundle mBundle = this.getArguments();
            if (mBundle != null) {
                // Call from categories to view ads
                mCatId = mBundle.getString("cat_id", "");
            } else {
                // Call from view event ads
                mCatId = "1";
            }
            //  getActiveAds();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mView;
    }

    private void getActiveAds(boolean showProgress) {
        try {

//            Webservice Call
//            Step 1, Register Callback Interface
            WebNotificationManager.registerResponseListener(responseHandlerListenerCompleteAd);
//            Step 2, Call Webservice Method
            WebServiceClient.getMyAdsList(getActivity(), activeAdsPayload(), showProgress, 1, responseHandlerListenerCompleteAd);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setUpLayouts() {
        try {
            mRvAds = (RecyclerView) mView.findViewById(R.id.rv_ads);
            mLlLoading = (LinearLayout) mView.findViewById(R.id.ll_loading_more);
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

    private String activeAdsPayload() {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("ads_type", "completed");
                userData.put("cat_id", "");
                userData.put("page_no", mPageNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("rseponse", "" + userData);
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }

    ResponseHandlerListener responseHandlerListenerCompleteAd = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            try {
                WebNotificationManager.unRegisterResponseListener(responseHandlerListenerCompleteAd);
                if (error == null) {
                    switch (mUrlNo) {
                        case 1:
                            //to get categories list
                            getAdsData(result);
                            break;
                        case 2:
                            //to search ad list
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
            if (searchView != null) {
                Utility.hideKeyboard(getActivity(), searchView);
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
            } else {
                //Success
                if (completelistAds.size() > 0) {
                    completelistAds.clear();
                }
                DataPojo dataPojo = result.getData();
                completelistAds.addAll(dataPojo.getAdsList());

                if (completelistAds.size() > 0) {
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
                //Error
                ErrorPojo errorPojo = result.getError();
                if (errorPojo.getError_code().equals("533")) {

                    if (mPageNo > 1) {

                        if (mLlLoading.getVisibility() == View.VISIBLE) {
                            mLlLoading.setVisibility(View.GONE);

                        }
                    } else {
                        mRvAds.setAdapter(null);
                        mTvNoAdsFound.setVisibility(View.VISIBLE);
                        mRvAds.setVisibility(View.GONE);
                    }
                } else {
                    new Utility().showErrorDialog(getActivity(), result);
                }
            } else {//Success
                if (completelistAds.size() > 0) {
                    completelistAds.remove(completelistAds.size() - 1);
                    mUserAdapter.notifyItemRemoved(completelistAds.size());
                }
                DataPojo dataPojo = result.getData();
                mPosition = dataPojo.getTotal_page();
                completelistAds.addAll(dataPojo.getAdsList());
                beforeFilterCompletelistAds.addAll(completelistAds);
                if (completelistAds.size() > 0) {
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
                        mUserAdapter.setLoaded();
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
        mUserAdapter = new MyAdsListAdapter(getActivity(), completelistAds, getResources().getString(R.string.complete_ads), mRvAds);
        mRvAds.setAdapter(mUserAdapter);
        mRvAds.setHasFixedSize(true);
        if (mLlLoading.getVisibility() == View.VISIBLE) {
            mUserAdapter.notifyDataSetChanged();
            mUserAdapter.setLoaded();
            mLlLoading.setVisibility(View.GONE);
        }
        mUserAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                isDataLoaded = false;

                completelistAds.add(null);
                mUserAdapter.notifyItemInserted(completelistAds.size() - 1);
                if (mPageNo < mPosition) {
                    mPageNo++;
                    getActiveAds(false);
                } else {
                    isDataLoaded = true;
                }
            }
        });
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
                searchView = (SearchView) MenuItemCompat.getActionView(item);
                SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Utility.hideKeyboard(getActivity(), searchView);
                        searchView.clearFocus();
//                        Webservice Call
//                        Step 1, Register Callback Interface
                        WebNotificationManager.registerResponseListener(responseHandlerListenerCompleteAd);
//                        Step 2, Call Webservice Method
                        WebServiceClient.getSearchMyAdsList(getActivity(), searchAdListPayload(query), true, 2, responseHandlerListenerCompleteAd);

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
                userData.put("ads_type", "search_completed");
                userData.put("search", searchText);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //URL: http://spplitt.ignivastaging.com/ads/getMyAds
            //Payload: {"user_id":"19","auth_token":"2CUWSLXOU1QY","ads_type":"completed","cat_id":"","page_no":1}
            android.util.Log.e("Response", "" + userData);
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e("=====", "complee");
        if (isVisibleToUser && !_areLecturesLoaded) {
            getActiveAds(true);
            _areLecturesLoaded = true;
        } else {
            if (completelistAds != null) {
                completelistAds.clear();
                completelistAds.addAll(beforeFilterCompletelistAds);
            }
            if (mUserAdapter != null) {
                mUserAdapter.notifyDataSetChanged();
            }
        }
    }
}