package com.igniva.spplitt.ui.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.CategoriesListPojo;
import com.igniva.spplitt.model.CityListPojo;
import com.igniva.spplitt.model.CountriesListPojo;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.activties.CityActivity;
import com.igniva.spplitt.ui.activties.CountryActivity;
import com.igniva.spplitt.ui.activties.MainActivity;
import com.igniva.spplitt.ui.activties.SplashActivity;
import com.igniva.spplitt.ui.activties.StateActivity;
import com.igniva.spplitt.ui.adapters.CountryListAdapter;
import com.igniva.spplitt.utils.Constants;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;
import com.igniva.spplitt.utils.Validations;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by igniva-php-08 on 11/5/16.
 */
public class PostAdFragment extends BaseFragment implements View.OnClickListener {
    View view;
    static EditText mEtSelectDate;
    static EditText mEtSelectTime;

    List<String> mArrayListCategories = new ArrayList<>();
    List<String> mArrayListCategoryId = new ArrayList<>();
    Spinner mSpCategories;
    EditText mEtSplittCost;
    Button mBtnSubmitPostAd;
    EditText mEtAdTitle;
    EditText mEtAdDesc;
    TextView mTvSplittCurrency;
    TextView mTvCities;
    TextView mTvCountries;
    TextView mTvStates;
    boolean isCategoriesLoaded;
    ImageView mIvCurrency;

    Bundle mBundle;
    String mAdId;
    static String mAdCategoryId;
    static String mAdTitle;
    static String mAdDesc;
    static String mAdDate;
    static String mAdTime;
    static String mAdCurrency;
    String mAdCity;
    String mAdEdit;
    Intent in;
    String countryId;
    String countryName;
    String stateId;
    String stateName;
    String cityId;
    boolean showStaticFields;

    public static PostAdFragment newInstance() {
        PostAdFragment fragment = new PostAdFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post_ad, container, false);
        in = getActivity().getIntent();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //coming from city adapter
        if (PreferenceHandler.readInteger(getActivity(), PreferenceHandler.SHOW_EDIT_PROFILE, 1) == 3) {
            showStaticFields = true;
        }
        PreferenceHandler.writeInteger(getActivity(), PreferenceHandler.SHOW_EDIT_PROFILE, 1);
        mBundle = getArguments();
        if (mBundle != null) {
            mAdId = mBundle.getString("ad_id");
            mAdCategoryId = mBundle.getString("ad_category_id");
            mAdTitle = mBundle.getString("ad_title");
            mAdDesc = mBundle.getString("ad_desc");
            mAdDate = mBundle.getString("ad_date");
            mAdTime = mBundle.getString("ad_time");
            countryId = mBundle.getString("ad_country");
            stateId = mBundle.getString("ad_state");
            cityId = mBundle.getString("ad_city");
            countryName = mBundle.getString("ad_country_name");
            stateName = mBundle.getString("ad_state_name");
            mAdCity = mBundle.getString("ad_city_name");
            mAdCurrency = mBundle.getString("ad_currency");
            mAdEdit = mBundle.getString("ad_edit");
        }

//         Webservice Call
        isCategoriesLoaded = false;
//         Step 1, Register Callback Interface
        WebNotificationManager.registerResponseListener(responseHandlerListenerPostAD);
        // Step 2, Call Webservice Method
        WebServiceClient.getCategoriesList(getContext(), postAdGetCategoriesPayload(), true, 1, responseHandlerListenerPostAD);
        setUpLayouts();

        return view;
    }


    @Override
    public void setUpLayouts() {
        mTvCountries = (TextView) view.findViewById(R.id.tv_countries);
        mTvCountries.setOnClickListener(this);
        mTvStates = (TextView) view.findViewById(R.id.tv_states);
        mTvStates.setOnClickListener(this);
        mTvCities = (TextView) view.findViewById(R.id.tv_cities);
        mTvCities.setOnClickListener(this);
        mEtSelectDate = (EditText) view.findViewById(R.id.tv_select_date);
        mEtSelectDate.setOnClickListener(this);
        mEtSelectTime = (EditText) view.findViewById(R.id.btn_time);
        mEtSelectTime.setOnClickListener(this);
        mSpCategories = (Spinner) view.findViewById(R.id.sp_categories);
        mEtSplittCost = (EditText) view.findViewById(R.id.et_splitt_cost);
        mBtnSubmitPostAd = (Button) view.findViewById(R.id.btn_submit_post_ad);
        mBtnSubmitPostAd.setOnClickListener(this);
        mEtAdTitle = (EditText) view.findViewById(R.id.et_ad_title);
        mEtAdDesc = (EditText) view.findViewById(R.id.et_ad_desc);
        mTvSplittCurrency = (TextView) view.findViewById(R.id.tv_splitt_cost_currency);
        mIvCurrency = (ImageView) view.findViewById(R.id.iv_currency);


    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currentFragmentId = Constants.FRAG_ID_POST_ADS;


    }

    @Override
    public void setDataInViewLayouts() {
        try {
            mTvSplittCurrency.setText(PreferenceHandler.readString(getActivity(), PreferenceHandler.CURRENCY_CODE, ""));
            if (mBundle != null) {
                if (!mBundle.isEmpty()) {

                    if (mAdCategoryId != null) {
//                        Log.e("========",""+mArrayListCategoryId.indexOf(mAdCategoryId)+"===="+mAdCategoryId);
                        mSpCategories.setSelection(mArrayListCategoryId.indexOf(mAdCategoryId));
                    }
                    if (mAdTitle != null) {
                        mEtAdTitle.setText(mAdTitle);
                    }
                    if (mAdDesc != null) {
                        mEtAdDesc.setText(mAdDesc);
                    }
                    if (mAdDate != null) {
                        mEtSelectDate.setText(mAdDate);
                    }
                    if (mAdTime != null) {
                        mEtSelectTime.setText(mAdTime);
                    }
                    if (countryName != null) {
                        mTvCountries.setText(countryName);
                    }
                    if (stateName != null) {
                        mTvStates.setText(stateName);
                    }
                    if (mAdCity != null) {
                        mTvCities.setText(mAdCity);

                    }
                    if (mAdCurrency != null) {
                        mEtSplittCost.setText(mAdCurrency);
                    }
                    if (mAdEdit != null) {
                        if (mAdEdit.equals("edit")) {
                            viewsSetClickable(true);
                        } else if (mAdEdit.equals("repost")) {
                            viewsSetClickable(false);
                        } else {
                            viewsSetClickable(true);
                        }
                    }
                    mBundle.clear();

                }
            }
            if (showStaticFields) {
                if (mAdCategoryId != null) {
//                    Log.e("========",""+mArrayListCategoryId.indexOf(mAdCategoryId)+"===="+mAdCategoryId);
//                    mSpCategories.setSelection(Integer.parseInt(mAdCategoryId) - 1);
                    mSpCategories.setSelection(mArrayListCategoryId.indexOf(mAdCategoryId));
                }
                if (mAdTitle != null) {
                    mEtAdTitle.setText(mAdTitle);
                }
                if (mAdDesc != null) {
                    mEtAdDesc.setText(mAdDesc);
                }
                if (mAdDate != null) {
                    mEtSelectDate.setText(mAdDate);
                }
                if (mAdTime != null) {
                    mEtSelectTime.setText(mAdTime);
                }
                if (mAdCurrency != null) {
                    mEtSplittCost.setText(mAdCurrency);
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewsSetClickable(boolean mBoolean) {
        mSpCategories.setEnabled(mBoolean);
        mSpCategories.setClickable(mBoolean);
        mEtAdTitle.setEnabled(mBoolean);
        mEtAdDesc.setEnabled(mBoolean);
        mTvCountries.setEnabled(mBoolean);
        mTvCountries.setClickable(mBoolean);
        mTvStates.setEnabled(mBoolean);
        mTvStates.setClickable(mBoolean);
        mTvCities.setEnabled(mBoolean);
        mTvCities.setClickable(mBoolean);
        mEtSplittCost.setEnabled(mBoolean);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_date:
                mEtSelectDate.setFocusable(false);
                mEtSelectTime.setFocusable(false);
                mEtSelectDate.setFocusableInTouchMode(false);
                mEtSelectTime.setFocusableInTouchMode(false);
                Utility.hideKeyboard(getActivity(), mEtSelectDate);
                Utility.hideKeyboard(getActivity(), mEtSelectTime);
                showDatePickerDialog();
                break;
            case R.id.btn_time:
                mEtSelectDate.setFocusable(false);
                mEtSelectTime.setFocusable(false);
                mEtSelectDate.setFocusableInTouchMode(false);
                mEtSelectTime.setFocusableInTouchMode(false);
                Utility.hideKeyboard(getActivity(), mEtSelectDate);
                Utility.hideKeyboard(getActivity(), mEtSelectTime);
                showTimePickerDialog(v);
                break;
            case R.id.btn_submit_post_ad:
                boolean val = new Validations().isValidatePostAd(getActivity(), mSpCategories, mEtAdTitle, mEtAdDesc, mEtSelectDate, mEtSelectTime, countryId, stateId, cityId, mEtSplittCost);
                if (val) {
                    // Step 2, Call Webservice Method
                    mBtnSubmitPostAd.setClickable(false);
                    //         Webservice Call
//         Step 1, Register Callback Interface
                    WebNotificationManager.registerResponseListener(responseHandlerListenerPostAD);
                    if (mAdEdit != null) {
                        if (mAdEdit.equals("edit")) {
                            WebServiceClient.editAd(getActivity(), editPostADPayload(), true, 4, responseHandlerListenerPostAD);
                        } else if (mAdEdit.equals("repost")) {
                            WebServiceClient.repostAd(getActivity(), rePostADPayload(), true, 4, responseHandlerListenerPostAD);
                        } else {
                            WebServiceClient.postAd(getActivity(), createPostADPayload(), true, 4, responseHandlerListenerPostAD);
                        }
                    } else {
                        WebServiceClient.postAd(getActivity(), createPostADPayload(), true, 4, responseHandlerListenerPostAD);
                    }
                }
                break;
            case R.id.tv_countries:
                try {
                    mAdCategoryId = mArrayListCategoryId.get(mArrayListCategories.indexOf(mSpCategories.getSelectedItem().toString()));
                    mAdTitle = mEtAdTitle.getText().toString();
                    mAdDesc = mEtAdDesc.getText().toString();
                    mAdDate = mEtSelectDate.getText().toString();
                    mAdTime = mEtSelectTime.getText().toString();
                    mAdCurrency = mEtSplittCost.getText().toString();
                    Intent in = new Intent(getActivity(), CountryActivity.class);
                    in.putExtra("from", "4");
                    startActivity(in);
//                    startActivityForResult(in, 2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_states:
                if (countryId != null) {
                    mAdCategoryId = mArrayListCategoryId.get(mArrayListCategories.indexOf(mSpCategories.getSelectedItem().toString()));
                    mAdTitle = mEtAdTitle.getText().toString();
                    mAdDesc = mEtAdDesc.getText().toString();
                    mAdDate = mEtSelectDate.getText().toString();
                    mAdTime = mEtSelectTime.getText().toString();
                    mAdCurrency = mEtSplittCost.getText().toString();
                    Intent in = new Intent(getActivity(), StateActivity.class);
                    in.putExtra("country_id", countryId);
                    in.putExtra("country_name", countryName);
                    in.putExtra("from", "4");
                    startActivity(in);
                } else {
                    Utility.showToastMessageShort(getActivity(), getResources().getString(R.string.msg_select_country));
                }
                break;
            case R.id.tv_cities:
                if (stateId != null) {
                    mAdCategoryId = mArrayListCategoryId.get(mArrayListCategories.indexOf(mSpCategories.getSelectedItem().toString()));
                    mAdTitle = mEtAdTitle.getText().toString();
                    mAdDesc = mEtAdDesc.getText().toString();
                    mAdDate = mEtSelectDate.getText().toString();
                    mAdTime = mEtSelectTime.getText().toString();
                    mAdCurrency = mEtSplittCost.getText().toString();
                    Intent in = new Intent(getActivity(), CityActivity.class);
                    in.putExtra("countryId", countryId);
                    in.putExtra("countryName", countryName);
                    in.putExtra("stateId", stateId);
                    in.putExtra("stateName", stateName);
                    in.putExtra("from", "4");
                    startActivity(in);
                } else {
                    Utility.showToastMessageShort(getActivity(), getResources().getString(R.string.msg_select_country_state));
                }
                break;

        }

    }

    private String editPostADPayload() {
        //get categoryid
        String categoryId = mArrayListCategoryId.get(mArrayListCategories.indexOf(mSpCategories.getSelectedItem().toString()));
        String payload = null;
//        String countryId = listCountries.get(Integer.parseInt(mSpCountries.getSelectedItem().toString())).getCountry_id();
//        String cityId = listCities.get(mArrayListCityName.indexOf(mSpCities.getSelectedItem())).getCity_id();

        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("ads_type", "edit");
                userData.put("ad_id", mAdId);
                userData.put("cat_id", categoryId);
                userData.put("title", mEtAdTitle.getText().toString().trim());
                userData.put("description", mEtAdDesc.getText().toString().trim());
                userData.put("expiration_date", mEtSelectDate.getText().toString().trim());
                userData.put("expiration_time", mEtSelectTime.getText().toString().trim());
                userData.put("country_id", countryId);
                userData.put("state_id", stateId);
                userData.put("city_id", cityId);
                userData.put("cost", mEtSplittCost.getText().toString().trim());
                userData.put("currency_id", PreferenceHandler.readString(getActivity(), PreferenceHandler.CURRENCY_ID, ""));
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

    private String rePostADPayload() {
        //get categoryid
        String categoryId = mArrayListCategoryId.get(mArrayListCategories.indexOf(mSpCategories.getSelectedItem().toString()));
        String payload = null;
//        String countryId = listCountries.get(Integer.parseInt(mSpCountries.getSelectedItem().toString())).getCountry_id();
//        String cityId = listCities.get(mArrayListCityName.indexOf(mSpCities.getSelectedItem())).getCity_id();

        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("ads_type", "repost");
                userData.put("ad_id", mAdId);
                userData.put("update_date", mEtSelectDate.getText().toString().trim());
                userData.put("update_time", mEtSelectTime.getText().toString().trim());
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

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");

    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datepicker = new DatePickerDialog(getActivity(), this, year, month, day);
            datepicker.getDatePicker().setMinDate(c.getTimeInMillis());
            // Create a new instance of DatePickerDialog and return it
            return datepicker;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            int mon = month + 1;
            mEtSelectDate.setText(day + "/" + mon + "/" + year);
            mEtSelectDate.setTextColor(Color.BLACK);
            mEtSelectDate.setError(null);
            mEtSelectDate.clearFocus();
            mEtSelectDate.invalidate();
        }
    }


    public void showTimePickerDialog(View v) {
//        final Calendar mcurrentTime = Calendar.getInstance();
//        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//        final int minute = mcurrentTime.get(Calendar.MINUTE);
//        RangeTimePickerDialog newFragment = new RangeTimePickerDialog(getActivity(),new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int hourOfDay, int selectedMinute) {
//
//                if (minute < 9) {
//                    mEtSelectTime.setText(hourOfDay + ":0" + minute + ":00");
//                } else {
//                    mEtSelectTime.setText(hourOfDay + ":" + minute + ":00");
//                }
//                mEtSelectTime.setTextColor(Color.BLACK);
//                mEtSelectTime.setError(null);
//                mEtSelectTime.clearFocus();
//
//            }
//        }, hour, minute, true);//true = 24 hour time
//        newFragment.setMin(hour, minute);
//        newFragment.show();
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            if (minute < 9) {
                mEtSelectTime.setText(hourOfDay + ":0" + minute + ":00");
            } else {
                mEtSelectTime.setText(hourOfDay + ":" + minute + ":00");
            }
            mEtSelectTime.setTextColor(Color.BLACK);
            mEtSelectTime.setError(null);
            mEtSelectTime.clearFocus();
//            mEtSelectTime.setE(true);
        }
    }

    ResponseHandlerListener responseHandlerListenerPostAD = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            try {
                WebNotificationManager.unRegisterResponseListener(responseHandlerListenerPostAD);
                if (error == null) {
                    switch (mUrlNo) {
                        case 1://to get categories list
                            getCategoriesData(result);
                            isCategoriesLoaded = true;
//                            if (mBundle != null) {
//                                if (!mBundle.isEmpty()) {
//                                    String countryId = listCountries.get(Integer.parseInt(mAdCountry)).getCountry_id();
//                                    getCitiesFromCountry(Integer.parseInt(countryId) - 2);
//                                }else {
//                                    getCitiesFromCountry(0);
//                                }
//                            } else {
//                                getCitiesFromCountry(0);
//                            }
                            break;
//                        case 2://to get countries list
//                            getCountriesData(result);
//                            break;
//                        case 3://to get cities list
//                            getCitiesData(result);
//                            break;
                        case 4://to post an ad
                            mBtnSubmitPostAd.setClickable(true);
                            postAdData(result);
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


//    private void getCountriesData(ResponsePojo result) {
//        try {
//            if (result.getStatus_code() == 400) {
//                //Error
//                new Utility().showErrorDialog(getContext(), result);
//            } else {//Success
//                DataPojo dataPojo = result.getData();
//                List<CountriesListPojo> listCountries = new ArrayList<CountriesListPojo>();
//                listCountries = dataPojo.getCountries();
//                CountryListAdapter adapter = new CountryListAdapter(getActivity(), listCountries);
//                mSpCountries.setAdapter(adapter);
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void getCitiesData(ResponsePojo result) {
//        try {
//            if (result.getStatus_code() == 400) {
//                //Error
//                new Utility().showErrorDialog(getActivity(), result);
//            } else {//Success
//                DataPojo dataPojo = result.getData();
//                listCities = dataPojo.getCityList();
//                mArrayListCityName.clear();
//                mArrayListCityId.clear();
//                if (listCities != null) {
//                    for (CityListPojo cityListPojo : listCities) {
//                        mArrayListCityName.add(cityListPojo.getCity_name());
//                        mArrayListCityId.add(cityListPojo.getCity_id());
//                    }
//                }
//                ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, mArrayListCityName);
//                mSpCities.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//
//                // to set data
//                setDataInViewLayouts();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


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
            Log.e("rseponse", "" + userData);
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
//                mArrayListCategories.clear();
//                mArrayListCategoryId.clear();
                DataPojo dataPojo = result.getData();
                List<CategoriesListPojo> listCategories = new ArrayList<CategoriesListPojo>();
                listCategories = dataPojo.getCategories();
                if (listCategories != null) {
                    for (CategoriesListPojo categoriesListPojo : listCategories) {
                        mArrayListCategories.add(categoriesListPojo.getCategory_title());
                        mArrayListCategoryId.add(categoriesListPojo.getCategory_id());
                    }
                }
                ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, mArrayListCategories);
                mSpCategories.setAdapter(adapter);
                setDataInViewLayouts();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createPostADPayload() {
        //get categoryid
        String categoryId = mArrayListCategoryId.get(mArrayListCategories.indexOf(mSpCategories.getSelectedItem().toString()));
        String payload = null;
//        String countryId = listCountries.get(Integer.parseInt(mSpCountries.getSelectedItem().toString())).getCountry_id();
//        String cityId = listCities.get(mArrayListCityName.indexOf(mSpCities.getSelectedItem())).getCity_id();

        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("cat_id", categoryId);
                userData.put("title", mEtAdTitle.getText().toString().trim());
                userData.put("description", mEtAdDesc.getText().toString().trim());
                userData.put("expiration_date", mEtSelectDate.getText().toString().trim());
                userData.put("expiration_time", mEtSelectTime.getText().toString().trim());
                userData.put("country_id", countryId);
                userData.put("state_id", stateId);
                userData.put("city_id", cityId);
                userData.put("cost", mEtSplittCost.getText().toString().trim());
                userData.put("currency_id", PreferenceHandler.readString(getActivity(), PreferenceHandler.CURRENCY_ID, ""));
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

    private void postAdData(ResponsePojo result) {
        if (result.getStatus_code() == 400) {
            //Error
            new Utility().showErrorDialog(getActivity(), result);
        } else {//Success
            new Utility().showSuccessDialog(getActivity(), result);
            viewsSetClickable(true);
//            in.removeExtra("key");
            countryId = null;
            countryName = "";
            stateId=null;
            stateName="";
            cityId=null;
            mAdCity="";
            mEtAdTitle.setText("");
            mEtAdDesc.setText("");
            mEtSelectDate.setText("");
            mEtSelectTime.setText("");
            mEtSplittCost.setText("");
            mTvCountries.setText(getResources().getString(R.string.select_your_country));
            mTvStates.setText(getResources().getString(R.string.select_your_state));
            mTvCities.setText(getResources().getString(R.string.select_your_city));

        }
    }

//    /**
//     * @param position position of country
//     */
//    void getCitiesFromCountry(int position) {
//        // listCountries.get(position).getCountry_id();
//        // Webservice Call
//        // Step 1, Register Callback Interface
//        WebNotificationManager.registerResponseListener(responseHandlerListenerPostAD);
//        // Step 2, Call Webservice Method
//        WebServiceClient.getCitiesList(getActivity(), createCityListPayload(listCountries.get(position).getCountry_id()), true, 3, responseHandlerListenerPostAD);
//
//    }

}
