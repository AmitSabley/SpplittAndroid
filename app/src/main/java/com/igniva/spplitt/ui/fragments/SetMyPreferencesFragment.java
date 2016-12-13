package com.igniva.spplitt.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.CategoriesListPojo;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.activties.CityActivity;
import com.igniva.spplitt.ui.activties.CountryActivity;
import com.igniva.spplitt.ui.activties.MainActivity;
import com.igniva.spplitt.ui.activties.StateActivity;
import com.igniva.spplitt.ui.views.MultiSpinner;
import com.igniva.spplitt.utils.Constants;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;
import com.igniva.spplitt.utils.Validations;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by igniva-php-08 on 18/5/16.
 */
public class SetMyPreferencesFragment extends BaseFragment implements View.OnClickListener, DateRangePickerFragment.OnDateRangeSelectedListener {
    View view;
    EditText mEtDateRange;
    MultiSpinner mMspCategories;
    TextView mTvCities;
    TextView mTvCountries;
    TextView mTvStates;
    Button mBtnSubmitSetPref;
    boolean isCategoryItemSelected;
    String startDate, endDate;
    Intent in;
    String countryId;
    String countryName;
    String stateId;
    String stateName;
    String cityId;
    static List<String> mArrayListCategories = new ArrayList<>();
    static List<String> mArrayListSelectedCategories = new ArrayList<>();
    static List<String> mArrayListCategoryId = new ArrayList<>();
    static String dateRange;
    //    List<CountriesListPojo> listCountries = new ArrayList<CountriesListPojo>();
//    ArrayList<String> mArrayListCityName = new ArrayList<>();
//    ArrayList<String> mArrayListCityId = new ArrayList<>();
//    List<CityListPojo> listCities = new ArrayList<CityListPojo>();
    boolean isDateGreater;
    boolean isCategoriesLoaded;
    boolean showStaticFields;

    public static SetMyPreferencesFragment newInstance() {
        SetMyPreferencesFragment fragment = new SetMyPreferencesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_set_my_preferences, container, false);
        setHasOptionsMenu(true);
        in = getActivity().getIntent();

        //Call from city adapter
        if (PreferenceHandler.readInteger(getActivity(), PreferenceHandler.SHOW_EDIT_PROFILE, 1) == 2) {
            showStaticFields = true;
        }
        setUpLayouts();
        setDataInViewLayouts();
        PreferenceHandler.writeInteger(getActivity(), PreferenceHandler.SHOW_EDIT_PROFILE, 1);

//         Webservice Call
        isCategoriesLoaded = false;
//         Step 1, Register Callback Interface
        WebNotificationManager.registerResponseListener(responseHandlerListenerPostAD);
//         Step 2, Call Webservice Method
        WebServiceClient.getCategoriesList(getContext(), postAdGetCategoriesPayload(), true, 1, responseHandlerListenerPostAD);

        return view;
    }

    @Override
    public void setUpLayouts() {
        mEtDateRange = (EditText) view.findViewById(R.id.tv_select_date_range);
        mEtDateRange.setOnClickListener(this);
        mMspCategories = (MultiSpinner) view.findViewById(R.id.sp_categories_pref);
        mTvCountries = (TextView) view.findViewById(R.id.tv_countries);
        mTvCountries.setOnClickListener(this);
        mTvStates = (TextView) view.findViewById(R.id.tv_states);
        mTvStates.setOnClickListener(this);
        mTvCities = (TextView) view.findViewById(R.id.tv_cities);
        mTvCities.setOnClickListener(this);
        mBtnSubmitSetPref = (Button) view.findViewById(R.id.btn_submit_set_pref);
        mBtnSubmitSetPref.setOnClickListener(this);

    }

    @Override
    public void setDataInViewLayouts() {

        if (showStaticFields) {
            if (dateRange != null) {
                mEtDateRange.setText(dateRange);
            }
            if (in != null) {
                if (in.hasExtra("countryId")) {
                    countryId = in.getStringExtra("countryId");
                    countryName = in.getStringExtra("countryName");
                    mTvCountries.setText(countryName);
                }
                if (in.hasExtra("stateId")) {
                    stateId = in.getStringExtra("stateId");
                    stateName = in.getStringExtra("stateName");
                    mTvStates.setText(stateName);
                }
                if (in.hasExtra("cityId")) {
                    cityId = in.getStringExtra("cityId");
                    mTvCities.setText(in.getStringExtra("cityName"));
                }
            }
//            if(mArrayListCategories.size()>0) {
//                Map<String, Boolean> items = new LinkedHashMap<>();
//                for (String item : mArrayListCategories) {
//                    items.put(item, Boolean.FALSE);
//                }
//                mMspCategories.setItems(items, new MultiSpinner.MultiSpinnerListener() {
//
//                    @Override
//                    public void onItemsSelected(boolean[] selected) {
//                        mArrayListSelectedCategories.clear();
//                        isCategoryItemSelected = false;
//                        // your operation with code...
//                        for (int i = 0; i < selected.length; i++) {
//                            if (selected[i]) {
//                                isCategoryItemSelected = true;
//                                mArrayListSelectedCategories.add(mArrayListCategoryId.get(i));
//                            }
//                        }
//                    }
//                });
//            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currentFragmentId = Constants.FRAG_ID_SET_PREFERENCES;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_date_range:
                mEtDateRange.setFocusable(false);
                mEtDateRange.setFocusableInTouchMode(false);
                Utility.hideKeyboard(getActivity(), mEtDateRange);
                DateRangePickerFragment dateRangePickerFragment = DateRangePickerFragment.newInstance(this, false);
                dateRangePickerFragment.show(getFragmentManager(), "datePicker");
                break;
            case R.id.btn_submit_set_pref:
                boolean val = new Validations().isValidateSetPreferences(getActivity(), mMspCategories, isCategoryItemSelected, countryId, stateId, cityId, isDateGreater, mEtDateRange);
                if (val) {
                    mBtnSubmitSetPref.setClickable(false);
//         Webservice Call
//         Step 1, Register Callback Interface
                    WebNotificationManager.registerResponseListener(responseHandlerListenerPostAD);
//         Step 2, Call Webservice Method
                    WebServiceClient.setMyPreferences(getActivity(), createSetPrefrencesPayload(), true, 4, responseHandlerListenerPostAD);
                }
                break;
            case R.id.tv_countries:
                try {
                    Log.e("======", mArrayListSelectedCategories.size() + "");
                    dateRange = mEtDateRange.getText().toString();
                    Intent in = new Intent(getActivity(), CountryActivity.class);
                    in.putExtra("from", "3");
                    startActivity(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_states:
                if (countryId != null) {
                    dateRange = mEtDateRange.getText().toString();
                    Intent in = new Intent(getActivity(), StateActivity.class);
                    in.putExtra("country_id", countryId);
                    in.putExtra("country_name", countryName);
                    in.putExtra("from", "3");
                    startActivity(in);
                } else {
                    Utility.showToastMessageShort(getActivity(), getResources().getString(R.string.msg_select_country));
                }
                break;
            case R.id.tv_cities:
                if (stateId != null) {
                    dateRange = mEtDateRange.getText().toString();
                    Intent in = new Intent(getActivity(), CityActivity.class);
                    in.putExtra("countryId", countryId);
                    in.putExtra("countryName", countryName);
                    in.putExtra("stateId", stateId);
                    in.putExtra("stateName", stateName);
                    in.putExtra("from", "3");
                    startActivity(in);
                } else {
                    Utility.showToastMessageShort(getActivity(), getResources().getString(R.string.msg_select_country_state));
                }
                break;
        }
    }

    @Override
    public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
        int startMonthDate = startMonth + 1;
        int endMonthDate = endMonth + 1;
        startDate = startDay + "-" + startMonthDate + "-" + startYear;
        endDate = endDay + "-" + endMonthDate + "-" + endYear;
        isDateGreater = CheckDates(startYear + "-" + startMonthDate + "-" + startDay, endYear + "-" + endMonthDate + "-" + endDay);
        mEtDateRange.setText(getResources().getString(R.string.from_date) + ": " + startDay + "-" + startMonthDate + "-" + startYear + " " + getResources().getString(R.string.to_date) + ": " + endDay + "-" + endMonthDate + "-" + endYear);
    }


    public static boolean CheckDates(String d1, String d2) {
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
        boolean b = false;
        try {
            if (dfDate.parse(d1).before(dfDate.parse(d2))) {
                b = true;//If start date is before end date
            } else if (dfDate.parse(d1).equals(dfDate.parse(d2))) {
                b = true;//If two dates are equal
            } else {
                b = false; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }


    ResponseHandlerListener responseHandlerListenerPostAD = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            try {
                WebNotificationManager.unRegisterResponseListener(responseHandlerListenerPostAD);
                if (error == null) {
                    switch (mUrlNo) {
                        case 1:
                            //to get categories list
                            getCategoriesData(result);
                            isCategoriesLoaded = true;
                            // Get Cities from country
//                            getCitiesFromCountry(0);
                            break;
//                        case 2://to get countries list
//                            getCountriesData(result);
//                            break;
//                        case 3://to get cities list
//                            getCitiesData(result);
//                            break;
                        case 4:
                            //to set preferences
                            mBtnSubmitSetPref.setClickable(true);
                            setPreferences(result);
                            break;

                    }
                } else {
                    new Utility().showErrorDialogRequestFailed(getActivity());
                }
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            } catch (Exception e) {
                new Utility().showErrorDialogRequestFailed(getActivity());
                e.printStackTrace();
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        }
    };

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
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }

    private void getCategoriesData(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
                //Error
                new Utility().showErrorDialog(getContext(), result);
            } else {//Success
                DataPojo dataPojo = result.getData();
                List<CategoriesListPojo> listCategories = new ArrayList<CategoriesListPojo>();
                listCategories = dataPojo.getCategories();
                if (listCategories != null) {
                    for (CategoriesListPojo categoriesListPojo : listCategories) {
                        mArrayListCategories.add(categoriesListPojo.getCategory_title());
                        mArrayListCategoryId.add(categoriesListPojo.getCategory_id());
                    }
                }

                Map<String, Boolean> items = new LinkedHashMap<>();

                if (showStaticFields) {
                    if (mArrayListSelectedCategories.size() > 0) {
                        for (int i = 0; i < mArrayListCategories.size(); i++) {
                            for (int j = 0; j < mArrayListSelectedCategories.size(); j++) {
                                String selectedVal = mArrayListSelectedCategories.get(j);
                                String val = mArrayListCategoryId.get(i);
                                if (selectedVal.equals(val)) {

                                    items.put(mArrayListCategories.get(i), Boolean.TRUE);
                                    break;
                                } else {
                                    items.put(mArrayListCategories.get(i), Boolean.FALSE);
                                }
                            }
                        }

                        mMspCategories.setItems(items, new MultiSpinner.MultiSpinnerListener() {

                            @Override
                            public void onItemsSelected(boolean[] selected) {
                                mArrayListSelectedCategories.clear();
                                isCategoryItemSelected = false;
                                // your operation with code...
                                for (int i = 0; i < selected.length; i++) {
                                    if (selected[i]) {
                                        isCategoryItemSelected = true;
                                        mArrayListSelectedCategories.add(mArrayListCategoryId.get(i));
                                    }
                                }
                            }
                        });

                    } else {
                        for (String item : mArrayListCategories) {
                            items.put(item, Boolean.FALSE);
                        }
                        mMspCategories.setItems(items, new MultiSpinner.MultiSpinnerListener() {

                            @Override
                            public void onItemsSelected(boolean[] selected) {
                                mArrayListSelectedCategories.clear();
                                isCategoryItemSelected = false;
                                // your operation with code...
                                for (int i = 0; i < selected.length; i++) {
                                    if (selected[i]) {
                                        isCategoryItemSelected = true;
                                        mArrayListSelectedCategories.add(mArrayListCategoryId.get(i));
                                    }
                                }
                            }
                        });
                    }

                } else {
                    for (String item : mArrayListCategories) {
                        items.put(item, Boolean.FALSE);
                    }
                    mMspCategories.setItems(items, new MultiSpinner.MultiSpinnerListener() {

                        @Override
                        public void onItemsSelected(boolean[] selected) {
                            mArrayListSelectedCategories.clear();
                            isCategoryItemSelected = false;
                            // your operation with code...
                            for (int i = 0; i < selected.length; i++) {
                                if (selected[i]) {
                                    isCategoryItemSelected = true;
                                    mArrayListSelectedCategories.add(mArrayListCategoryId.get(i));
                                }
                            }
                        }
                    });
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createSetPrefrencesPayload() {
        String payload = null;
//        String countryId = listCountries.get(Integer.parseInt(mSpCountries.getSelectedItem().toString())).getCountry_id();
//        String cityId = listCities.get(mArrayListCityName.indexOf(mSpCities.getSelectedItem())).getCity_id();

        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("cat_id", mArrayListSelectedCategories.toString());
                userData.put("country_id", countryId);
                userData.put("state_id", stateId);
                userData.put("city_id", cityId);
                userData.put("start_date", startDate.toString());
                userData.put("end_date", endDate.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            com.igniva.spplitt.utils.Log.e("Response Set Preferences", "" + userData);
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }

    private void setPreferences(ResponsePojo result) {
        if (result.getStatus_code() == 400) {
            //Error
            new Utility().showErrorDialog(getActivity(), result);
        } else {
            //Success
            new Utility().showSuccessDialog(getActivity(), result);
            mTvCountries.setText(getResources().getString(R.string.select_your_country));
            mTvStates.setText(getResources().getString(R.string.select_your_state));
            mTvCities.setText(getResources().getString(R.string.select_your_city));
            mEtDateRange.setText("");
            Map<String, Boolean> items = new LinkedHashMap<>();
            for (String item : mArrayListCategories) {
                items.put(item, Boolean.FALSE);
            }
            mMspCategories.setItems(items, new MultiSpinner.MultiSpinnerListener() {

                @Override
                public void onItemsSelected(boolean[] selected) {
                    mArrayListSelectedCategories.clear();
                    isCategoryItemSelected = false;
                    // your operation with code...
                    for (int i = 0; i < selected.length; i++) {
                        if (selected[i]) {
                            isCategoryItemSelected = true;
                            mArrayListSelectedCategories.add(mArrayListCategoryId.get(i));
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pref, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.mn_reset_pref:
                //         Step 1, Register Callback Interface
                WebNotificationManager.registerResponseListener(responseHandlerListenerPostAD);
//          Step 2, Call Webservice Method
                WebServiceClient.resetPref(getContext(), resetPrefPayload(), true, 4, responseHandlerListenerPostAD);

                return true;
        }
        return false;
    }

    //  Reset UserId and Token in preferences
    private String resetPrefPayload() {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
            com.igniva.spplitt.utils.Log.e("Response Reset Preferences", "" + userData);
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }
}
