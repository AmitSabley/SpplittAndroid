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
public class ViewAllActiveAdsFragment extends BaseFragment  {
    private static final String LOG_TAG = "ViewAllActiveAdsFragment";
    View mView;
    RecyclerView mRvAds;
    String mAdType = "new";
    String mAdTypeSearch = "search_new";
    String mCatId;
    boolean _areLecturesLoaded = false;
    TextView mTvNoAdsFound;
    AdsListAdapter mAdsListAdapter;
    public static  ViewAllActiveAdsFragment mViewAllAds;
    List<AdsListPojo> listAds = new ArrayList<AdsListPojo>();
    List<AdsListPojo> beforeFilterListAds = new ArrayList<AdsListPojo>();
    public static ViewAllActiveAdsFragment newInstance() {
        ViewAllActiveAdsFragment fragment = new ViewAllActiveAdsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_all_ads_active, container, false);
        try {
            Log.e("------------","-----------------");
            mViewAllAds=this;
            setUpLayouts();
            setDataInViewLayouts();
            setHasOptionsMenu(true);


       } catch (Exception e) {
            e.printStackTrace();
        }




        return mView;
    }

    private void getActiveAds(boolean b) {
        //         Webservice Call
//         Step 1, Register Callback Interface
        WebNotificationManager.registerResponseListener(responseHandlerListenerViewAD);
        // Step 2, Call Webservice Method
        WebServiceClient.getAdsList(getActivity(), postAdGetCategoriesPayload(), true, 1, responseHandlerListenerViewAD);

    }
    private String postAdGetCategoriesPayload() {
        Bundle mBundle = this.getArguments();
        if (mBundle != null) {//u are coming from categories to view ads
            mCatId = mBundle.getString("cat_id", "");
        } else {// u are coming from view event ads
            mCatId = "1";
        }
        String payload = null;
        Log.e("Response", "" + mCatId);
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
            mTvNoAdsFound=(TextView)mView.findViewById(R.id.tv_no_ads_found);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDataInViewLayouts() {

    }

//    @Override
//    public void onClick(View v) {
//        try {
//            switch (v.getId()) {
//                case R.id.btn_active_ad:
//                    mBtnActiveAd.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_active_ads_hover));
//                    mBtnCloseAd.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_closed_ads));
//                    mAdType = "new";
//                    mAdTypeSearch = "search_new";
//                    WebNotificationManager.registerResponseListener(responseHandlerListenerViewAD);
//                    WebServiceClient.getAdsList(getActivity(), postAdGetCategoriesPayload(), true, 1, responseHandlerListenerViewAD);
//
//                    break;
//                case R.id.btn_close_ad:
//                    mBtnActiveAd.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_active_ads));
//                    mBtnCloseAd.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_closed_ads_hover));
//                    mAdType = "completed";
//                    mAdTypeSearch = "search_completed";
//                    WebNotificationManager.registerResponseListener(responseHandlerListenerViewAD);
//                    WebServiceClient.getAdsList(getActivity(), postAdGetCategoriesPayload(), true, 1, responseHandlerListenerViewAD);
//                    break;
//                case R.id.btn_change_adlocation://to go at my profile
//                    PreferenceHandler.writeInteger(getActivity(), PreferenceHandler.SHOW_EDIT_PROFILE, 0);
//                    startActivity(new Intent(getActivity(), MainActivity.class));
//                    break;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currentFragmentId = Constants.FRAG_ID_VIEW_EVENT;
        Log.e("ViewAllActiveAds", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if(mAdsListAdapter!=null) {
//
//
//            mAdsListAdapter = new AdsListAdapter(getActivity(), listAds, true);
//            android.util.Log.e("val,",listAds.get(5).is_connect()+"");
//            mAdsListAdapter.notifyDataSetChanged();
//        }
    }

    ResponseHandlerListener responseHandlerListenerViewAD = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            try {
                WebNotificationManager.unRegisterResponseListener(responseHandlerListenerViewAD);
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
            if (result.getStatus_code() == 400) {
                //Error
                ErrorPojo errorPojo=result.getError();
                if(errorPojo.getError_code().equals("533")){
                        mRvAds.setAdapter(null);
                        mTvNoAdsFound.setVisibility(View.VISIBLE);
                        mRvAds.setVisibility(View.GONE);

                }else{
                    new Utility().showErrorDialog(getActivity(), result);
                }
            } else {//Success
                DataPojo dataPojo = result.getData();
                listAds = dataPojo.getAdsList();
                beforeFilterListAds.addAll(listAds);
                if (listAds.size() > 0) {
                    mTvNoAdsFound.setVisibility(View.GONE);
                    if (mAdType.equals("new")) {
                        mAdsListAdapter = new AdsListAdapter(getActivity(), listAds, true);
                    } else {
                        mAdsListAdapter = new AdsListAdapter(getActivity(), listAds, false);
                    }

                mRvAds.setAdapter(mAdsListAdapter);
                mAdsListAdapter.notifyDataSetChanged();
                }

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
                App.getInstance().trackEvent(LOG_TAG, "Search All Active Ads", "Active Ads Search");

                //Do Search here
                final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        //         Webservice Call
//         Step 1, Register Callback Interface
                        WebNotificationManager.registerResponseListener(responseHandlerListenerViewAD);
                        // Step 2, Call Webservice Method
                        WebServiceClient.getSearchAdsList(getActivity(), searchAdListPayload(query), true, 2, responseHandlerListenerViewAD);

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
                userData.put("ads_type", mAdTypeSearch);
                userData.put("cat_id", mCatId);
                userData.put("search", searchText);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        if (isVisibleToUser && !_areLecturesLoaded) {
            getActiveAds(true);
            _areLecturesLoaded = true;
        } else {
            if (listAds != null) {
                listAds.clear();
                listAds.addAll(beforeFilterListAds);
            }
            if (mAdsListAdapter != null) {
                mAdsListAdapter.notifyDataSetChanged();
            }
        }
    }



    public void getPosition(int pos, boolean isConnect)
    {
        listAds.get(pos).setIs_connect(isConnect);
        mAdsListAdapter.notifyDataSetChanged();
    }

    public void getPosition(int pos)
    {
        listAds.remove(pos);
        mAdsListAdapter.notifyDataSetChanged();
    }

    public void getPositionFlag(int pos, boolean isConnect)
    {
        listAds.get(pos).setIs_flagged(isConnect);
        mAdsListAdapter.notifyDataSetChanged();
    }
}


