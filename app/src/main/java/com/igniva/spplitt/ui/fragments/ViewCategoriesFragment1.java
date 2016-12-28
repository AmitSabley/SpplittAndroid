package com.igniva.spplitt.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.igniva.spplitt.App;
import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.CategoriesListPojo;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.activties.CountryActivity;
import com.igniva.spplitt.ui.activties.MainActivity;
import com.igniva.spplitt.ui.activties.ViewAdsActivity;
import com.igniva.spplitt.ui.adapters.CategoryListAdapter;
import com.igniva.spplitt.utils.Constants;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igniva-php-08 on 18/5/16.
 */
public class ViewCategoriesFragment1 extends BaseFragment implements View.OnClickListener {
    private static final String LOG_TAG = "ViewCategoriesFragment1";
    View mView;
    RecyclerView mRvCategories;
    TextView mTvLocation;
    Button mBtnChangeLocation;
    FloatingActionButton mBtnPostAd;
    Button mBtnViewAllAd;
    Intent in;
    String countryId;
    String countryName;
    String stateId;
    String stateName;
    String cityId;
    String cityName;

    public static ViewCategoriesFragment1 newInstance() {
        ViewCategoriesFragment1 fragment = new ViewCategoriesFragment1();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_view_categories_one, container, false);
        in = getActivity().getIntent();
        if(PreferenceHandler.readInteger(getActivity(), PreferenceHandler. SHOW_EDIT_PROFILE, 0) == 6) {
            if (in != null) {
                if (in.hasExtra("countryId")) {
                    countryId = in.getStringExtra("countryId");
                    countryName = in.getStringExtra("countryName");

                }
                if (in.hasExtra("stateId")) {
                    stateId = in.getStringExtra("stateId");
                    stateName = in.getStringExtra("stateName");

                }
                if (in.hasExtra("cityId")) {
                    cityId = in.getStringExtra("cityId");
                    cityName = in.getStringExtra("cityName");
                }
            }
        }
        setHasOptionsMenu(true);
        setUpLayouts();
//        setFontStyle();
        setDataInViewLayouts();
//         Webservice Call
//         Step 1: Register Callback Interface
        WebNotificationManager.registerResponseListener(responseHandlerListenerViewAD);
//         Step 2: Call Webservice Method
        WebServiceClient.getCategoriesList(getContext(), postAdGetCategoriesPayload(), true, 1, responseHandlerListenerViewAD);

        return mView;
    }

    @Override
    public void setUpLayouts() {
        mRvCategories = (RecyclerView) mView.findViewById(R.id.rv_categories);
        mTvLocation = (TextView) mView.findViewById(R.id.tv_my_location);
        mBtnChangeLocation = (Button) mView.findViewById(R.id.btn_change_location);
        mBtnChangeLocation.setOnClickListener(this);
        mBtnPostAd = (FloatingActionButton) mView.findViewById(R.id.btn_post_ad);
        mBtnPostAd.setOnClickListener(this);
//        mBtnViewAllAd = (Button) mView.findViewById(R.id.btn_view_al_ad);
//        mBtnViewAllAd.setOnClickListener(this);
    }

    @Override
    public void setDataInViewLayouts() {
        if(PreferenceHandler.readInteger(getActivity(), PreferenceHandler. SHOW_EDIT_PROFILE, 0) == 6 && cityId!=null && cityName!=null) {
//            PreferenceHandler.writeInteger(getActivity(), PreferenceHandler.SHOW_EDIT_PROFILE, 0);
            mBtnChangeLocation.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.tick, 0);
            mTvLocation.setText(cityName + "," + countryName);
        } else {
            mBtnChangeLocation.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_edit, 0);
            mTvLocation.setText(PreferenceHandler.readString(getActivity(), PreferenceHandler.AD_CITY_NAME, "") + "," + PreferenceHandler.readString(getActivity(), PreferenceHandler.AD_COUNTRY_NAME, ""));
//            PreferenceHandler.writeInteger(getActivity(), PreferenceHandler.SHOW_EDIT_PROFILE, 0);
        }
    }


    private String postAdGetCategoriesPayload() {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN, ""));
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
                switch (mUrlNo) {
                    case 1:
                        //to get categories list
                        if (error == null) {
                            getCategoriesData(result);
                        } else {
                            //
                            new Utility().showErrorDialogRequestFailed(getActivity());
                        }
                        if (mProgressDialog != null && mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        break;
                    case 2:
                        //to save user location
                        mBtnChangeLocation.setClickable(true);
                        if (error == null) {
                            Log.e("====result",result+"");

                            mBtnChangeLocation.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_edit, 0);
                            PreferenceHandler.writeString(getActivity(), PreferenceHandler.AD_COUNTRY_NAME, countryName);
                            PreferenceHandler.writeString(getActivity(), PreferenceHandler.AD_STATE_NAME, stateName);
                            PreferenceHandler.writeString(getActivity(), PreferenceHandler.AD_CITY_NAME, cityName);
                            countryId="";
                            countryName="";
                            stateId="";
                            stateName="";
                            cityId="";
                            cityName="";
                            new Utility().showSuccessDialog(getActivity(), result);
                        } else {
                            mBtnChangeLocation.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.tick, 0);
                            new Utility().showErrorDialogRequestFailed(getActivity());
                        }
                        if (mProgressDialog != null && mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
                new Utility().showErrorDialogRequestFailed(getActivity());
            }
        }
    };

    private void getCategoriesData(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
                //Error
                new Utility().showErrorDialog(getContext(), result);
            } else {
                //Success
                DataPojo dataPojo = result.getData();
                List<CategoriesListPojo> listCategories = new ArrayList<CategoriesListPojo>();
                listCategories = dataPojo.getCategories();
                if (listCategories != null) {
                    if (listCategories.size() > 0) {
                        mRvCategories.setAdapter(new CategoryListAdapter(getActivity(), listCategories));
                        mRvCategories.setHasFixedSize(true);
                        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                        mRvCategories.setLayoutManager(mLayoutManager);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        NavigationView navigationView = (NavigationView) MainActivity.main.findViewById(R.id.nav_view);
        switch (view.getId()) {
            case R.id.btn_change_location://to go at my profile
                mBtnChangeLocation.setClickable(false);
                App.getInstance().trackEvent(LOG_TAG, "Change Location and Go to My Profile", "My Profile Call to Change Location");
//                if(PreferenceHandler.readInteger(getActivity(), PreferenceHandler. SHOW_EDIT_PROFILE, 0) == 6) {
                if (cityId!=null && !cityId.equals("") ) {
                    saveLocation();
                } else {


                    Intent in1 = new Intent(getActivity(), CountryActivity.class);
                    in1.putExtra("from", "5");
                    startActivity(in1);

                }
// ((MainActivity) getActivity()).onNavigationItemSelected(navigationView.getMenu().getItem(1));
                break;
            case R.id.btn_post_ad:
                App.getInstance().trackEvent(LOG_TAG, "Post Ads", "Post Ads");
                //to go at post ad
                ((MainActivity) getActivity()).onNavigationItemSelected(navigationView.getMenu().getItem(4));
                break;
            case R.id.btn_view_al_ad:
                App.getInstance().trackEvent(LOG_TAG, "View All Ads From Categories", "View All Ads From Categories");

                //to view all ads
                Intent in = new Intent(getActivity(), ViewAdsActivity.class);
                getActivity().startActivity(in);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBtnChangeLocation.setClickable(true);
        MainActivity.currentFragmentId = Constants.FRAG_ID_CATEGORIES;
        Log.e("ViewCategoriesFragment", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.mn_al_ads:
                Intent in = new Intent(getActivity(), ViewAdsActivity.class);
                getActivity().startActivity(in);
                return true;
        }
        return false;
    }

    void saveLocation() {
        try {
            // Webservice Call
            // Step 1: Register Callback Interface
            WebNotificationManager.registerResponseListener(responseHandlerListenerViewAD);
            // Step 2: Call Webservice Method
            WebServiceClient.saveLocationPayload(getActivity(), createSaveLocationPayload(), true, 2, responseHandlerListenerViewAD);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //saving data to json...
    private String createSaveLocationPayload() {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            userData.put("user_id", PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_ID, ""));
            userData.put("auth_token", PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN, ""));
            userData.put("country_id", countryId);
            userData.put("state_id", stateId);
            userData.put("city_id", cityId);
            Log.e("Response", "" + userData);
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }
}
