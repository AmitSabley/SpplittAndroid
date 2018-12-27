package com.igniva.spplitt.ui.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.igniva.spplitt.App;
import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.AsyncResult;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.controller.WebServiceClientUploadImage;
import com.igniva.spplitt.model.CategoriesListPojo;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ImagePojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.activties.CityActivity;
import com.igniva.spplitt.ui.activties.CountryActivity;
import com.igniva.spplitt.ui.activties.MainActivity;
import com.igniva.spplitt.ui.activties.StateActivity;
import com.igniva.spplitt.utils.Constants;
import com.igniva.spplitt.utils.ImagePicker;
import com.igniva.spplitt.utils.Permissions;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;
import com.igniva.spplitt.utils.Validations;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by igniva-php-08 on 11/5/16.
 */
public class PostAdFragment extends BaseFragment implements View.OnClickListener, AsyncResult {
    private static final String LOG_TAG = "PostAdFragment";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    View view;
    static EditText mEtSelectDate;
    static EditText mEtSelectTime;

    List<String> mArrayListCategories = new ArrayList<>();
    List<String> mArrayListCategoryId = new ArrayList<>();
    Spinner mSpCategories;
    EditText mEtSplittCost;
    Button mBtnSubmitPostAd;
    EditText mEtAdTitle, mEtAdDesc, mEtNoOfPeople;
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
    static String mAdNoOfPeopleToSpplittWith;
    String mAdCity;
    String mAdEdit;
    Intent in;
    String countryId;
    String countryName;
    String stateId;
    String stateName;
    String cityId;
    boolean showStaticFields;
    private LinearLayout mAddPhotoBox;
    private String mImageName;
    static Bitmap myBitmap;
    private ImageView mImgPhoto1, mImgPhoto2, mImgPhoto3, mImgPhoto4, mImgCross1, mImgCross2,
            mImgCross3, mImgCross4;
    private ScrollView mScrollView;
    private RelativeLayout mRLPhoto1, mRLPhoto2, mRLPhoto3, mRLPhoto4;
    private HorizontalScrollView mHsv;
    private ArrayList<String> mImagePathList, mImageNameList;
    private ArrayList<Bitmap> mImageBitmapsList;
    private ArrayList<Uri> mImageUriList;
    private ArrayList<ImagePojo> mAdImages;
    private ArrayList<String> mIdList, mUrlList, mDeletedIds, mIdList1;
    private ArrayList<Bitmap> bitmapArrayList;

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
        mAdImages = new ArrayList<>();
        mIdList = new ArrayList<>();
        mIdList1 = new ArrayList<>();
        mUrlList = new ArrayList<>();
        mDeletedIds = new ArrayList<>();
        if (mBundle != null) {
            mAdId = mBundle.getString("ad_id");
            mAdCategoryId = mBundle.getString("ad_category_id");
            mAdTitle = mBundle.getString("ad_title");
            mAdDesc = mBundle.getString("ad_desc");
            mAdNoOfPeopleToSpplittWith = mBundle.getString("ad_no_people");
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
            try {
                mIdList = mBundle.getStringArrayList("id_list");
                mUrlList = mBundle.getStringArrayList("url_list");

                mIdList1 = new ArrayList<String>(mIdList);
                mDeletedIds = new ArrayList<String>(mIdList);
                ImagePojo imagePojo = new ImagePojo();
                if (mIdList != null) {
                    for (int i = 0; i < mIdList.size(); i++) {
                        imagePojo.setImage_id(mIdList.get(i));
                        imagePojo.setImage_url(mUrlList.get(i));
                        mAdImages.add(imagePojo);
                    }
                } else {
                    mHsv.setVisibility(View.GONE);
                }

            } catch (Exception e) {

            }


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
        mScrollView = (ScrollView) view.findViewById(R.id.sv_main);
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
        mEtNoOfPeople = (EditText) view.findViewById(R.id.et_no_of_people);
        mTvSplittCurrency = (TextView) view.findViewById(R.id.tv_splitt_cost_currency);
        mIvCurrency = (ImageView) view.findViewById(R.id.iv_currency);
        mAddPhotoBox = (LinearLayout) view.findViewById(R.id.ll_add_photo_box);
        mAddPhotoBox.setOnClickListener(this);
        mRLPhoto1 = (RelativeLayout) view.findViewById(R.id.rl_photo1);
        mRLPhoto1.setVisibility(View.GONE);
        mRLPhoto2 = (RelativeLayout) view.findViewById(R.id.rl_photo2);
        mRLPhoto2.setVisibility(View.GONE);
        mRLPhoto3 = (RelativeLayout) view.findViewById(R.id.rl_photo3);
        mRLPhoto3.setVisibility(View.GONE);
        mRLPhoto4 = (RelativeLayout) view.findViewById(R.id.rl_photo4);
        mRLPhoto4.setVisibility(View.GONE);
        mImgPhoto1 = (ImageView) view.findViewById(R.id.img_photo1);
        mImgPhoto2 = (ImageView) view.findViewById(R.id.img_photo2);
        mImgPhoto3 = (ImageView) view.findViewById(R.id.img_photo3);
        mImgPhoto4 = (ImageView) view.findViewById(R.id.img_photo4);
        mImgCross1 = (ImageView) view.findViewById(R.id.img_cross1);
        mImgCross1.setOnClickListener(this);
        mImgCross2 = (ImageView) view.findViewById(R.id.img_cross2);
        mImgCross2.setOnClickListener(this);
        mImgCross3 = (ImageView) view.findViewById(R.id.img_cross3);
        mImgCross3.setOnClickListener(this);
        mImgCross4 = (ImageView) view.findViewById(R.id.img_cross4);
        mImgCross4.setOnClickListener(this);
        mHsv = (HorizontalScrollView) view.findViewById(R.id.hsv);
        mHsv.setVisibility(View.GONE);
        mImagePathList = new ArrayList<>();
        mImageNameList = new ArrayList<>();
        mImageBitmapsList = new ArrayList<>();
        mImageUriList = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currentFragmentId = Constants.FRAG_ID_POST_ADS;
        Log.e("PostAdFragment", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }


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
                    if (mAdNoOfPeopleToSpplittWith != null) {
                        mEtNoOfPeople.setText(mAdNoOfPeopleToSpplittWith);
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
                    if (mUrlList != null) {
                        mHsv.setVisibility(View.VISIBLE);

                        if (mUrlList.size() == 4) {
                            mRLPhoto4.setVisibility(View.VISIBLE);
                            mRLPhoto3.setVisibility(View.VISIBLE);
                            mRLPhoto2.setVisibility(View.VISIBLE);
                            mRLPhoto1.setVisibility(View.VISIBLE);
                            Glide.with(getActivity())
                                    .load(mUrlList.get(3)).asBitmap()
                                    .into(mImgPhoto4);
                            Glide.with(getActivity())
                                    .load(mUrlList.get(2)).asBitmap()
                                    .into(mImgPhoto3);
                            Glide.with(getActivity())
                                    .load(mUrlList.get(1)).asBitmap()
                                    .into(mImgPhoto2);
                            Glide.with(getActivity())
                                    .load(mUrlList.get(0)).asBitmap()
                                    .into(mImgPhoto1);


                        } else if (mUrlList.size() == 3) {
                            mRLPhoto3.setVisibility(View.VISIBLE);
                            mRLPhoto2.setVisibility(View.VISIBLE);
                            mRLPhoto1.setVisibility(View.VISIBLE);

                            Glide.with(getActivity())
                                    .load(mUrlList.get(2)).asBitmap()
                                    .into(mImgPhoto3);
                            Glide.with(getActivity())
                                    .load(mUrlList.get(1)).asBitmap()
                                    .into(mImgPhoto2);
                            Glide.with(getActivity())
                                    .load(mUrlList.get(0)).asBitmap()
                                    .into(mImgPhoto1);


                        } else if (mUrlList.size() == 2) {
                            mRLPhoto2.setVisibility(View.VISIBLE);
                            mRLPhoto1.setVisibility(View.VISIBLE);

                            Glide.with(getActivity())
                                    .load(mUrlList.get(1)).asBitmap()
                                    .into(mImgPhoto2);
                            Glide.with(getActivity())
                                    .load(mUrlList.get(0)).asBitmap()
                                    .into(mImgPhoto1);

                        } else if (mUrlList.size() == 1) {
                            mRLPhoto1.setVisibility(View.VISIBLE);

                            Glide.with(getActivity())
                                    .load(mUrlList.get(0)).asBitmap()
                                    .into(mImgPhoto1);


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
                if (mAdNoOfPeopleToSpplittWith != null) {
                    mEtNoOfPeople.setText(mAdNoOfPeopleToSpplittWith);
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
        mEtNoOfPeople.setEnabled(mBoolean);
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
                boolean val = new Validations().isValidatePostAd(getActivity(), mSpCategories, mEtAdTitle, mEtAdDesc, mEtNoOfPeople, mEtSelectDate, mEtSelectTime, countryId, stateId, cityId, mEtSplittCost);
                if (val) {
                    mBtnSubmitPostAd.setClickable(false);
//                  Webservice Call
//                  Step 1: Register Callback Interface
                    WebNotificationManager.registerResponseListener(responseHandlerListenerPostAD);
                    if (mAdEdit != null) {
//                  Step 2: Call Webservice Method

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
                    mAdNoOfPeopleToSpplittWith = mEtNoOfPeople.getText().toString();
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
                    mAdNoOfPeopleToSpplittWith = mEtNoOfPeople.getText().toString();
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
                    mAdNoOfPeopleToSpplittWith = mEtNoOfPeople.getText().toString();
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
            case R.id.ll_add_photo_box:
                if (mImgPhoto1.getDrawable() != null &&
                        mImgPhoto2.getDrawable() != null &&
                        mImgPhoto3.getDrawable() != null &&
                        mImgPhoto4.getDrawable() != null) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.you_cannot_add_more_than_four_photos), Toast.LENGTH_SHORT).show();
                } else {
                    if (Permissions.checkPermissionCamera(getActivity())) {
                        App.getInstance().trackEvent(LOG_TAG, "Pick Image", "Select Image");
                        onPickImage();
                    }
                }

                break;
            case R.id.img_cross1:
                mImgPhoto1.setImageDrawable(null);
                mRLPhoto1.setVisibility(View.GONE);
                removeImageFromArrayList(0);
                checkIfAllImagesAreRemoved();
                break;
            case R.id.img_cross2:
                mImgPhoto2.setImageDrawable(null);
                mRLPhoto2.setVisibility(View.GONE);
                if (mImgPhoto1.getDrawable() == null) {
                    removeImageFromArrayList(0);

                } else {

                    removeImageFromArrayList(1);

                }

                checkIfAllImagesAreRemoved();
                break;
            case R.id.img_cross3:
                mImgPhoto3.setImageDrawable(null);
                mRLPhoto3.setVisibility(View.GONE);
                if (mImgPhoto1.getDrawable() == null &&
                        mImgPhoto2.getDrawable() != null) {
                    removeImageFromArrayList(1);

                } else if (mImgPhoto1.getDrawable() != null &&
                        mImgPhoto2.getDrawable() == null) {
                    removeImageFromArrayList(1);

                } else if (mImgPhoto1.getDrawable() == null &&
                        mImgPhoto2.getDrawable() == null) {
                    removeImageFromArrayList(0);

                } else {
                    removeImageFromArrayList(2);
                }

                checkIfAllImagesAreRemoved();
                break;
            case R.id.img_cross4:
                mImgPhoto4.setImageDrawable(null);
                mRLPhoto4.setVisibility(View.GONE);
                if (mImgPhoto1.getDrawable() == null ||
                        mImgPhoto2.getDrawable() != null ||
                        mImgPhoto3.getDrawable() != null) {
                    removeImageFromArrayList(2);

                } else if (mImgPhoto1.getDrawable() != null ||
                        mImgPhoto2.getDrawable() == null ||
                        mImgPhoto3.getDrawable() != null) {
                    removeImageFromArrayList(2);

                } else if (mImgPhoto1.getDrawable() == null ||
                        mImgPhoto2.getDrawable() != null ||
                        mImgPhoto3.getDrawable() != null) {
                    removeImageFromArrayList(2);

                } else if (mImgPhoto1.getDrawable() == null ||
                        mImgPhoto2.getDrawable() == null ||
                        mImgPhoto3.getDrawable() != null) {
                    removeImageFromArrayList(1);

                } else if (mImgPhoto1.getDrawable() != null ||
                        mImgPhoto2.getDrawable() == null ||
                        mImgPhoto3.getDrawable() == null) {
                    removeImageFromArrayList(1);

                } else if (mImgPhoto1.getDrawable() == null ||
                        mImgPhoto2.getDrawable() != null ||
                        mImgPhoto3.getDrawable() == null) {
                    removeImageFromArrayList(1);

                } else if (mImgPhoto1.getDrawable() == null &&
                        mImgPhoto2.getDrawable() == null &&
                        mImgPhoto3.getDrawable() != null) {
                    removeImageFromArrayList(0);

                } else {
                    removeImageFromArrayList(3);
                }
                checkIfAllImagesAreRemoved();
                break;
        }
    }

    private void removeImageFromArrayList(int position) {
        Log.e("zxcs", "mIdList:" + mIdList);
        Log.e("sdsa", "mDeletedIds: " + mDeletedIds.toString());
        Log.e("ssa", "mIdList1: " + mIdList1.toString());

//        if (mImageBitmapsList.size() != 0) {
//            mImageBitmapsList.remove(position);
//        }
        if (mAdImages.size() != 0) {
            // mDeletedIds.add(mAdImages.get(position).getImage_id());
            mIdList1.remove(position);
        }
        Log.e("zxcs", "1 mIdList:" + mIdList);
        Log.e("sdsa", "1 mDeletedIds: " + mDeletedIds.toString());
        Log.e("ssa", "1 mIdList1: " + mIdList1.toString());


    }

    private void checkIfAllImagesAreRemoved() {
        if (mImgPhoto1.getDrawable() == null && mImgPhoto2.getDrawable() == null
                && mImgPhoto3.getDrawable() == null && mImgPhoto4.getDrawable() == null) {
            mHsv.setVisibility(View.GONE);
        }
    }

    //  Image Picker
    public void onPickImage() {
        try {
            Intent chooseImageIntent = ImagePicker.getPickImageIntent(getActivity());
            startActivityForResult(chooseImageIntent, REQUEST_IMAGE_CAPTURE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String picturePath;

        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == 10) {
//              If(resultCode == Activity.RESULT_OK){
                Log.v("======", data + "");
                String result = data.getStringExtra("result");
                Log.v("======", result);
                Utility.showToastMessageShort(getActivity(), result);
//                }

            } else {
                Uri selectedImageURI = null;
                if (data != null) {
                    selectedImageURI = data.getData();
                }
                mImageUriList.add(selectedImageURI);

                //   picturePath = getPath(getActivity().getApplicationContext(), selectedImageURI);

                if (selectedImageURI != null) {
                    mImageName = Utility.getRealPathFromURI(getActivity(), selectedImageURI);
                    mScrollView.fullScroll(View.FOCUS_DOWN);
                    // Gallery
                    if (mImageName.toLowerCase().endsWith(".png") || mImageName.toLowerCase().endsWith(".jpeg") || mImageName.toLowerCase().endsWith(".jpg")) {
                        Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                        if (bitmap != null) {
                            setBitmapToImgViews(bitmap);
                            mImageBitmapsList.add(bitmap);

                        }
                    } else {
                        Utility.showToastMessageShort(getActivity(), getResources().getString(R.string.err_invalid_image));
                    }
                } else {
                    //Camera
                    mImageName = "photo.png";
                    Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                    if (bitmap != null) {
                        setBitmapToImgViews(bitmap);
                        mImageBitmapsList.add(bitmap);

                    }
                }
                // mImagePathList.add(picturePath);
                mImageNameList.add(mImageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    private String editPostADPayload() {
//      Get categoryid
        String categoryId = mArrayListCategoryId.get(mArrayListCategories.indexOf(mSpCategories.getSelectedItem().toString()));
        String payload = null;
//      String countryId = listCountries.get(Integer.parseInt(mSpCountries.getSelectedItem().toString())).getCountry_id();
//      String cityId = listCities.get(mArrayListCityName.indexOf(mSpCities.getSelectedItem())).getCity_id();

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
                userData.put("no_people", mEtNoOfPeople.getText().toString().trim());
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
            Log.e("Response", "" + userData);
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }

    private String rePostADPayload() {
//      Get categoryid
        String categoryId = mArrayListCategoryId.get(mArrayListCategories.indexOf(mSpCategories.getSelectedItem().toString()));
        String payload = null;
//      String countryId = listCountries.get(Integer.parseInt(mSpCountries.getSelectedItem().toString())).getCountry_id();
//      String cityId = listCities.get(mArrayListCityName.indexOf(mSpCities.getSelectedItem())).getCity_id();

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

            Log.e("Response", "" + userData);
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

    public void setBitmapToImgViews(Bitmap bitmap) {
        mScrollView.fullScroll(View.FOCUS_DOWN);
        mHsv.setVisibility(View.VISIBLE);
        if (mImgPhoto1.getDrawable() == null) {
            mRLPhoto1.setVisibility(View.VISIBLE);
            mImgPhoto1.setImageBitmap(bitmap);
        } else if (mImgPhoto2.getDrawable() == null) {
            mRLPhoto2.setVisibility(View.VISIBLE);
            mImgPhoto2.setImageBitmap(bitmap);
        } else if (mImgPhoto3.getDrawable() == null) {
            mRLPhoto3.setVisibility(View.VISIBLE);
            mImgPhoto3.setImageBitmap(bitmap);
        } else if (mImgPhoto4.getDrawable() == null) {
            mRLPhoto4.setVisibility(View.VISIBLE);
            mImgPhoto4.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onTaskResponse(Object result, int urlResponseNo) {
        switch (urlResponseNo) {
            case 5:
                //After uploading image
                Log.e("onTaskResponse", "result:" + result.toString());
                mImgPhoto1.setImageBitmap(null);
                mImgPhoto2.setImageBitmap(null);
                mImgPhoto3.setImageBitmap(null);
                mImgPhoto4.setImageBitmap(null);
                mImageBitmapsList.clear();
                bitmapArrayList.clear();
                mHsv.setVisibility(View.GONE);

                break;
        }


    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public AlertDialog onCreateDialog(Bundle savedInstanceState) {
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
        public AlertDialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            // Create a new instance of TimePickerDialog and return it
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
            LinearLayout linearLayout = new LinearLayout(getActivity());
            timePickerDialog.setCustomTitle(linearLayout);

//            timePickerDialog.setCustomTitle(null);
            return timePickerDialog;
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            if (minute < 9) {
                mEtSelectTime.setText(hourOfDay + ":0" + minute + ":00");
            } else {
                mEtSelectTime.setText(hourOfDay + ":" + minute + ":00");
            }

            mEtSelectTime.setTextColor(getActivity().getResources().getColor(R.color.colorBackground));
            mEtSelectTime.setError(null);
            mEtSelectTime.clearFocus();
        }
    }

    ResponseHandlerListener responseHandlerListenerPostAD = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            try {
                WebNotificationManager.unRegisterResponseListener(responseHandlerListenerPostAD);
                if (error == null) {
                    switch (mUrlNo) {
                        case 1:
//                          To retrieve categories list
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
                        case 4:
                            //To post an ad
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
            //URL: http://spplitt.ignivastaging.com/categories/getCategories
            //Payload: Payload:{"user_id":"19","auth_token":"Y8BCRO1WK8MU"}
            Log.e("Response", "" + userData);
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
            } else {
                //Success
                DataPojo dataPojo = result.getData();
                List<CategoriesListPojo> listCategories = new ArrayList<CategoriesListPojo>();
                listCategories = dataPojo.getCategories();
                if (listCategories != null) {
                    for (CategoriesListPojo categoriesListPojo : listCategories) {
                        mArrayListCategories.add(categoriesListPojo.getCategory_title());
                        mArrayListCategoryId.add(categoriesListPojo.getCategory_id());
                    }
                }
                ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.sppiner, mArrayListCategories);
                mSpCategories.setAdapter(adapter);
                setDataInViewLayouts();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String createPostADPayload() {
//      Get categoryid
        String categoryId = mArrayListCategoryId.get(mArrayListCategories.indexOf(mSpCategories.getSelectedItem().toString()));
        String payload = null;
//      String countryId = listCountries.get(Integer.parseInt(mSpCountries.getSelectedItem().toString())).getCountry_id();
//      String cityId = listCities.get(mArrayListCityName.indexOf(mSpCities.getSelectedItem())).getCity_id();

        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("cat_id", categoryId);
                userData.put("title", mEtAdTitle.getText().toString().trim());
                userData.put("description", mEtAdDesc.getText().toString().trim());
                userData.put("no_people", mEtNoOfPeople.getText().toString().trim());
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
            // URL: http://spplitt.ignivastaging.com/ads/postAd
            // Payload: {"user_id":"19","auth_token":"Y8BCRO1WK8MU","cat_id":"1","title":"cf","description":"Ssd","expiration_date":"13\/12\/2016","expiration_time":"6:59:00","country_id":"2","state_id":"74","city_id":"6019","cost":"2002","currency_id":"2"}
            Log.e("Response", "" + userData);
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
        } else {
            //Success
            new Utility().showSuccessDialog(getActivity(), result);
            viewsSetClickable(true);
            countryId = null;
            countryName = "";
            stateId = null;
            stateName = "";
            cityId = null;
            mAdCity = "";
            mEtAdTitle.setText("");
            mEtAdDesc.setText("");
            mEtNoOfPeople.setText("");
            mEtSelectDate.setText("");
            mEtSelectTime.setText("");
            mEtSplittCost.setText("");
            mTvCountries.setTextColor(getResources().getColor(R.color.yellow));
            mTvStates.setTextColor(getResources().getColor(R.color.yellow));
            mTvCities.setTextColor(getResources().getColor(R.color.yellow));
            mTvCountries.setText(getResources().getString(R.string.select_your_country));
            mTvStates.setText(getResources().getString(R.string.select_your_state));
            mTvCities.setText(getResources().getString(R.string.select_your_city));

            String ad_id = result.getData().getAd_id();
            // On success of PostAd, upload images
            uploadBitmapAsMultipart(ad_id);

        }
    }


    private void uploadBitmapAsMultipart(String ad_id) {
        if (mIdList1.size() != 0) {
            for (int j = 0; j < mIdList1.size(); j++) {
                if (mDeletedIds.contains(mIdList1.get(j))) {
                    mDeletedIds.remove(mIdList1.get(j));
                } else {
                }

            }
        }

        Log.e("zxcs", "2 mIdList:" + mIdList);
        Log.e("sdsa", "2 mDeletedIds: " + mDeletedIds.toString());
        Log.e("ssa", "2 mIdList1: " + mIdList1.toString());

        try {
            App.getInstance().trackEvent(LOG_TAG, "Upload Bitmap", "Bitmap on Post Ad");

            ByteArrayBody bab;
            ByteArrayOutputStream bos;
            Bitmap bm;
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("user_id", new StringBody(PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_ID, "")));
            reqEntity.addPart("auth_token", new StringBody(PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN, "")));
            reqEntity.addPart("ad_id", new StringBody(ad_id));
            String del_ids = mDeletedIds.toString();
            del_ids = del_ids.replace("[", "").replace("]", "");
            reqEntity.addPart("deleted_ids", new StringBody(del_ids));

           /* if (mImageBitmapsList.size() == 0) {

            } else {
                for (int i = 0; i < mImageBitmapsList.size(); i++) {
                    bm = mImageBitmapsList.get(i);
                    bos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 80, bos);
                    byte[] data = bos.toByteArray();
                    bab = new ByteArrayBody(data, "image/png", "image" + i + ".png");
                    reqEntity.addPart("files[" + i + "]", bab);
                }
            }*/

            bitmapArrayList = new ArrayList<>();
            if (mImgPhoto1.getDrawable() != null) {
                bitmapArrayList.add(((BitmapDrawable) mImgPhoto1.getDrawable()).getBitmap());
            }
            if (mImgPhoto2.getDrawable() != null) {
                bitmapArrayList.add(((BitmapDrawable) mImgPhoto2.getDrawable()).getBitmap());
            }
            if (mImgPhoto3.getDrawable() != null) {
                bitmapArrayList.add(((BitmapDrawable) mImgPhoto3.getDrawable()).getBitmap());
            }
            if (mImgPhoto4.getDrawable() != null) {
                bitmapArrayList.add(((BitmapDrawable) mImgPhoto4.getDrawable()).getBitmap());
            }

            Log.e("bitmapArrayList", "bitmapArrayList: " + bitmapArrayList);

            if (bitmapArrayList.size() == 0) {

            } else {
                for (int i = 0; i < bitmapArrayList.size(); i++) {
                    bm = bitmapArrayList.get(i);
                    bos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 70, bos);
                    byte[] data = bos.toByteArray();
                    bab = new ByteArrayBody(data, "image/png", "image" + i + ".png");
                    reqEntity.addPart("files[" + i + "]", bab);
                }

            }


            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream(
                    (int) reqEntity.getContentLength());
            reqEntity.writeTo(out);
            String entityContentAsString = new String(out.toByteArray());
            Log.e("multipartEntitty:", "" + entityContentAsString);

            new WebServiceClientUploadImage(getActivity(), this, WebServiceClient.HTTP_POST_AD_IMAGES, reqEntity, 5).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Permissions
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case Permissions.MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)

                {
                    onPickImage();
                } else

                {
                    Toast.makeText(getActivity(), "Please consider granting it Camera permission to use camera.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
