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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.igniva.spplitt.ui.adapters.MyAdsListAdapter;
import com.igniva.spplitt.utils.Constants;
import com.igniva.spplitt.utils.Log;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;




public class IncompleteAdsFragment extends BaseFragment implements View.OnClickListener {
    private static final String LOG_TAG = "IncompleteAdsFragment";
    View mView;
    RecyclerView mRvAdsIncomplete;
    String mCatId;
    private boolean isLoading;
    boolean _areLecturesLoaded = false;
    List<AdsListPojo> listAds = new ArrayList<AdsListPojo>();
    List<AdsListPojo> beforeFilterListAds = new ArrayList<AdsListPojo>();
    List<AdsListPojo> tempListAds = new ArrayList<AdsListPojo>();
    TextView mTvNoAdsFound;
    LinearLayout mLlLoading;
    int mPageNo=1;
    private int lastVisibleItem, totalItemCount;
    private int visibleThreshold = 5;
    int mPosition;
    MyAdsListAdapter  mUserAdapter;
    boolean isLoadMore;
    ProgressBar mProgressBar;
    SearchView searchView;
    public static boolean isDataLoaded;
    boolean isSearch;
    public static  IncompleteAdsFragment mViewIncompleteAds;
    public static IncompleteAdsFragment newInstance() {
        IncompleteAdsFragment fragment = new IncompleteAdsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_ads_active, container, false);
        try {
            mViewIncompleteAds=this;
            setUpLayouts();
            setDataInViewLayouts();
            setHasOptionsMenu(true);

            Bundle mBundle = this.getArguments();
            if (mBundle != null) {
                //Call from categories to view ads
                mCatId = mBundle.getString("cat_id", "");
            } else {
                //Call from view event ads
                mCatId = "1";
            }
//            getActiveAds();
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
            // Step 2, Call Webservice Method
            WebServiceClient.getMyAdsList(getActivity(), activeAdsPayload(), showProgress, 1, responseHandlerListenerViewAD);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setUpLayouts() {
        try {
            mRvAdsIncomplete = (RecyclerView) mView.findViewById(R.id.rv_ads);
            mLlLoading=(LinearLayout)mView.findViewById(R.id.ll_loading_more);
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
        Log.e("IncompleteAdsFragment", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String activeAdsPayload() {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("ads_type", "incomplete");
                userData.put("cat_id", "");
                userData.put("page_no",mPageNo );
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
            isSearch=false;
            if(searchView!=null) {
                Utility.hideKeyboard(getActivity(), searchView);
            }
            if (result.getStatus_code() == 400) {
                mRvAdsIncomplete.setAdapter(null);
                mTvNoAdsFound.setVisibility(View.VISIBLE);
                mRvAdsIncomplete.setVisibility(View.GONE);
                //Error
                ErrorPojo errorPojo = result.getError();
                if (!errorPojo.getError_code().equals("533")) {
                    new Utility().showErrorDialog(getActivity(), result);
                }
            } else {//Success
                if(tempListAds.size()>0){
                    tempListAds.clear();
                }
                DataPojo dataPojo = result.getData();
                tempListAds.addAll(dataPojo.getAdsList());

                if (tempListAds.size() > 0) {
                    mTvNoAdsFound.setVisibility(View.GONE);
                    mRvAdsIncomplete.setVisibility(View.VISIBLE);
                    mUserAdapter = new MyAdsListAdapter(getActivity(), tempListAds, getResources().getString(R.string.incomplete_ads),beforeFilterListAds);
                    mRvAdsIncomplete.setAdapter(mUserAdapter);
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
                isDataLoaded=true;
                mUserAdapter.notifyDataSetChanged();
                if(mProgressBar!=null) {
                    if (mProgressBar.getVisibility() == View.VISIBLE) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                }
                //Error

                ErrorPojo errorPojo=result.getError();
                if(errorPojo.getError_code().equals("533")){

                    if(mPageNo>1){

                        if(mLlLoading.getVisibility()==View.VISIBLE){
                            mLlLoading.setVisibility(View.GONE);

                        }
                    }else {

                        mRvAdsIncomplete.setAdapter(null);
                        mTvNoAdsFound.setVisibility(View.VISIBLE);
                        mRvAdsIncomplete.setVisibility(View.GONE);
                    }
                }else{
                    new Utility().showErrorDialog(getActivity(), result);
                }
            } else {//Success
                if(listAds.size()>0) {
                    listAds.remove(listAds.size() - 1);
                    mUserAdapter.notifyItemRemoved(listAds.size());
                }
                DataPojo dataPojo = result.getData();
                mPosition=dataPojo.getTotal_page();
                listAds.addAll(dataPojo.getAdsList());
                beforeFilterListAds=listAds;
               //searchResultAdsList;
                if (listAds.size() > 0) {
//                    if(!isSearch) {
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
//                            mUserAdapter.setLoaded();
                            isLoading = false;
                        }
                        mTvNoAdsFound.setVisibility(View.GONE);
                        mRvAdsIncomplete.setVisibility(View.VISIBLE);
//                    }else{
//                        setDataToList();
//                        mTvNoAdsFound.setVisibility(View.GONE);
//                        mRvAdsIncomplete.setVisibility(View.VISIBLE);
//                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataToList() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRvAdsIncomplete.setLayoutManager(mLayoutManager);
        mUserAdapter = new MyAdsListAdapter(getActivity(), listAds, getResources().getString(R.string.incomplete_ads),beforeFilterListAds);
        mRvAdsIncomplete.setAdapter(mUserAdapter);
        mRvAdsIncomplete.setHasFixedSize(true);
        if (mLlLoading.getVisibility() == View.VISIBLE) {
            mUserAdapter.notifyDataSetChanged();
            isLoading = false;
            mLlLoading.setVisibility(View.GONE);
        }


        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRvAdsIncomplete.getLayoutManager();
        mRvAdsIncomplete.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && (totalItemCount <= (lastVisibleItem + visibleThreshold))) {
                    isDataLoaded = false;
                    listAds.add(null);
                    mUserAdapter.notifyItemInserted(listAds.size() - 1);
                    if (mPageNo < mPosition) {
                        mPageNo++;
                        getActiveAds(false);
                    } else {
                        isDataLoaded = true;
                    }
                    isLoading = true;
                }

            }
        });
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
                App.getInstance().trackEvent(LOG_TAG, "Search InComplete Ads", "Search InComplete Ads");

                //do Search here
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
                        WebNotificationManager.registerResponseListener(responseHandlerListenerViewAD);
                        // Step 2, Call Webservice Method
                        WebServiceClient.getSearchMyAdsList(getActivity(), searchAdListPayload(query), true, 2, responseHandlerListenerViewAD);

                        return false;
                    }


                    @Override
                    public boolean onQueryTextChange(String newText) {
                            return false;
                    }
                });


                final EditText mEtSearchView = (EditText) searchView.findViewById(R.id.search_src_text);
                // Get the search close button image view
                ImageView closeButton = (ImageView)searchView.findViewById(R.id.search_close_btn);

                // Set on click listener
                closeButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        isSearch=true;
                        mEtSearchView.setText("");
                        if(beforeFilterListAds.size()>0) {
                            mRvAdsIncomplete.setVisibility(View.VISIBLE);
                            mTvNoAdsFound.setVisibility(View.GONE);
                            mUserAdapter = new MyAdsListAdapter(getActivity(), beforeFilterListAds, getResources().getString(R.string.incomplete_ads),beforeFilterListAds);
                            mRvAdsIncomplete.setAdapter(mUserAdapter);
                            mUserAdapter.notifyDataSetChanged();
                        }
                    }
                });

                MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {

                        if(beforeFilterListAds.size()>0) {
                            mRvAdsIncomplete.setVisibility(View.VISIBLE);
                            mTvNoAdsFound.setVisibility(View.GONE);
                            mUserAdapter = new MyAdsListAdapter(getActivity(), beforeFilterListAds, getResources().getString(R.string.incomplete_ads),beforeFilterListAds);
                            mRvAdsIncomplete.setAdapter(mUserAdapter);
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
                userData.put("ads_type", "search_incomplete");
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
        }else {

            if (beforeFilterListAds.size()>0) {
                mUserAdapter = new MyAdsListAdapter(getActivity(), beforeFilterListAds, getResources().getString(R.string.incomplete_ads),beforeFilterListAds);
                mRvAdsIncomplete.setAdapter(mUserAdapter);
                mUserAdapter.notifyDataSetChanged();
                mTvNoAdsFound.setVisibility(View.GONE);
                mRvAdsIncomplete.setVisibility(View.VISIBLE);
            }
        }
    }
    public void getPosition(int pos)
    {
        listAds.remove(pos);
        mUserAdapter.notifyDataSetChanged();
    }



}