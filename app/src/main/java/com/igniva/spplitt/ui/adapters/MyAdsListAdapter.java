package com.igniva.spplitt.ui.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.AdsListPojo;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ImagePojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.model.ReviewListPojo;
import com.igniva.spplitt.ui.activties.MainActivity;
import com.igniva.spplitt.ui.activties.ViewAdsDetailsActivity;
import com.igniva.spplitt.ui.fragments.ActiveAdFragment;
import com.igniva.spplitt.ui.fragments.CompleteAdsFragment;
import com.igniva.spplitt.ui.fragments.IncompleteAdsFragment;
import com.igniva.spplitt.ui.views.EditTextNonRegular;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;
import com.igniva.spplitt.utils.Validations;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by igniva-php-08 on 23/5/16.
 */
public class MyAdsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<AdsListPojo> mListAds;
    List<AdsListPojo> mTempListAds;
    Context mContext;
    String mAdsType;
    int cancelAdPosition;
    RecyclerView mRvAds;
    AlertDialog alertDialog;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<ReviewListPojo> mReviewList = new ArrayList<>();

    private Button mbtnRating;
    private RatingBar mratingBar_popup;
    private EditTextNonRegular met_review;
    private DataPojo dataPojo;
    int pos = 0;
    float rating = 0;

    String adIdPos;
    private int adIdPosition = 0;

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView mTvAdTitle;
        TextView mTvAdTime;
        TextView mTvSplittAmount;
        TextView mTvAdOwnerName;
        AppCompatRatingBar ratingBar;
        TextView mTvAdTotalRequests;
        Button mBtnEditAd;
        ImageView mBtnRateUser;
        Button mBtnDeleteAd;
        Button mBtnRepostAd;
        Button mBtnDetailsAd;
        LinearLayout mCvAdsMain;
        LinearLayout mLlBtn;
        TextView mTvAdLocation;

        public UserViewHolder(View itemView) {
            super(itemView);
            mTvAdTitle = (TextView) itemView.findViewById(R.id.tv_ad_title);
            mTvAdTime = (TextView) itemView.findViewById(R.id.tv_ad_time);
            mTvSplittAmount = (TextView) itemView.findViewById(R.id.tv_spplitt_amount);
            mTvAdOwnerName = (TextView) itemView.findViewById(R.id.tv_ad_owner_name);
            ratingBar = (AppCompatRatingBar) itemView.findViewById(R.id.userRating_Completed);
            mTvAdTotalRequests = (TextView) itemView.findViewById(R.id.tv_ad_requests);
            mBtnEditAd = (Button) itemView.findViewById(R.id.btn_edit_ad);
            mBtnDeleteAd = (Button) itemView.findViewById(R.id.btn_delete_ad);
            mBtnRateUser = (ImageView) itemView.findViewById(R.id.ButtonRate_User);
            mBtnRepostAd = (Button) itemView.findViewById(R.id.btn_repost_ad);
            mCvAdsMain = (LinearLayout) itemView.findViewById(R.id.cv_category_main);
            mLlBtn = (LinearLayout) itemView.findViewById(R.id.ll_btn);
            mTvAdLocation = (TextView) itemView.findViewById(R.id.tv_ad_location);
            mBtnDetailsAd = (Button) itemView.findViewById(R.id.btn_details_ad);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    public MyAdsListAdapter(Context context, List<AdsListPojo> listAds, String ads_type, List<AdsListPojo> templistAds) {
        this.mListAds = listAds;
        this.mContext = context;
        this.mAdsType = ads_type;
        this.mTempListAds = templistAds;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_my_ad_list, parent, false);
            UserViewHolder vhItem = new UserViewHolder(v);
            return vhItem;
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
            LoadingViewHolder vhfooter = new LoadingViewHolder(view);
            return vhfooter;
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            if (holder instanceof UserViewHolder) {
                UserViewHolder userViewHolder = (UserViewHolder) holder;
                userViewHolder.mTvAdTitle.setText(mListAds.get(position).getAd_title());
                userViewHolder.mTvSplittAmount.setText(mContext.getResources().getString(R.string.spplitt_amount) + mListAds.get(position).getAd_cost());
                userViewHolder.mTvAdOwnerName.setText(Html.fromHtml("<u>" + mListAds.get(position).getPosted_by_username() + "</u>"));
                if (Integer.parseInt(mListAds.get(position).getAd_total_request()) <= 1) {
                    userViewHolder.mTvAdTotalRequests.setText(mListAds.get(position).getAd_total_request() + " Request");
                } else {
                    userViewHolder.mTvAdTotalRequests.setText(mListAds.get(position).getAd_total_request() + " Requests");
                }

                userViewHolder.mTvAdLocation.setText(mListAds.get(position).getAd_city_name());
//                userViewHolder.mTvAdLocation.setText(mListAds.get(position).getAd_city_name() + "," + mListAds.get(position).getAd_country_name());
                java.text.DateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd");
                java.text.DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                String inputDateStr = mListAds.get(position).getAd_expiration_date();
                Date date1 = inputFormat.parse(inputDateStr);
                boolean isRating = mListAds.get(position).getIs_Rating();
                String rating;
                if (isRating) {
                    userViewHolder.ratingBar.setVisibility(View.VISIBLE);
                    userViewHolder.mBtnRateUser.setVisibility(View.GONE);
                    rating = mListAds.get(position).getRating_value();
                    float frating = Float.parseFloat(rating);
                    userViewHolder.ratingBar.setRating(frating);

                } else

                {
                    userViewHolder.mBtnRateUser.setVisibility(View.VISIBLE);
                    userViewHolder.ratingBar.setVisibility(View.GONE);
                    userViewHolder.mBtnRateUser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pos = position;
                            adIdPos = mListAds.get(position).getAd_id();
                            showRatingDialog(mContext, position);
                        }
                    });


                }
                String outputDateStr = outputFormat.format(date1);
                String string = outputDateStr + " " + mListAds.get(position).getAd_expiration_time();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.ENGLISH);
                Date date = format.parse(string);

                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy, HH:mm");
                String newFormat = formatter.format(date);
                userViewHolder.mTvAdTime.setText(newFormat);
                if (mAdsType.equals(mContext.getResources().getString(R.string.active_ads))) {
                    userViewHolder.mLlBtn.setWeightSum(3);
                    userViewHolder.mTvAdTotalRequests.setVisibility(View.VISIBLE);
                    userViewHolder.mBtnEditAd.setVisibility(View.VISIBLE);
                    userViewHolder.mBtnRepostAd.setVisibility(View.GONE);
                    userViewHolder.mBtnDeleteAd.setVisibility(View.VISIBLE);
                    userViewHolder.mBtnDetailsAd.setVisibility(View.GONE);
                    userViewHolder.mBtnRateUser.setVisibility(View.GONE);
                    userViewHolder.ratingBar.setVisibility(View.GONE);

                } else if (mAdsType.equals(mContext.getResources().getString(R.string.complete_ads))) {
                    userViewHolder.mLlBtn.setWeightSum(1);
                    userViewHolder.mTvAdTotalRequests.setVisibility(View.VISIBLE);
                    userViewHolder.mBtnEditAd.setVisibility(View.GONE);
                    userViewHolder.mBtnRepostAd.setVisibility(View.VISIBLE);
                    userViewHolder.mBtnDeleteAd.setVisibility(View.GONE);
                    userViewHolder.mBtnDetailsAd.setVisibility(View.VISIBLE);
                  /*  userViewHolder.mBtnRateUser.setVisibility(View.GONE);
                    userViewHolder.ratingBar.setVisibility(View.GONE);*/
                } else if (mAdsType.equals(mContext.getResources().getString(R.string.incomplete_ads))) {
                    userViewHolder.mLlBtn.setWeightSum(1);
                    userViewHolder.mTvAdTotalRequests.setVisibility(View.GONE);
                    userViewHolder.mBtnEditAd.setVisibility(View.GONE);
                    userViewHolder.mBtnRepostAd.setVisibility(View.VISIBLE);
                    userViewHolder.mBtnDeleteAd.setVisibility(View.GONE);
                    userViewHolder.mBtnDetailsAd.setVisibility(View.GONE);
                    userViewHolder.mBtnRateUser.setVisibility(View.GONE);
                    userViewHolder.ratingBar.setVisibility(View.GONE);
                }

                userViewHolder.mBtnEditAd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.main.addValue(createBundle(position, "edit"));
                        NavigationView navigationView = (NavigationView) MainActivity.main.findViewById(R.id.nav_view);
                        ((MainActivity) mContext).onNavigationItemSelected(navigationView.getMenu().getItem(4));
                    }
                });
                userViewHolder.mBtnDeleteAd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelAdPosition = position;
                        showDeletePopup(mContext);
                    }
                });
                userViewHolder.mTvAdOwnerName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                    Intent in = new Intent(mContext, OtherProfileActivity.class);
//                    in.putExtra("other_user_id", mListAds.get(position).getPosted_by_id());
//                    mContext.startActivity(in);
                        NavigationView navigationView = (NavigationView) MainActivity.main.findViewById(R.id.nav_view);
                        ((MainActivity) mContext).onNavigationItemSelected(navigationView.getMenu().getItem(1));
                    }
                });
                userViewHolder.mBtnRepostAd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.main.addValue(createBundle(position, "repost"));
                        NavigationView navigationView = (NavigationView) MainActivity.main.findViewById(R.id.nav_view);
                        ((MainActivity) mContext).onNavigationItemSelected(navigationView.getMenu().getItem(4));
                    }
                });
                userViewHolder.mCvAdsMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(mContext, ViewAdsDetailsActivity.class);
                        in.putExtra("ad_id", mListAds.get(position).getAd_id());
                        in.putExtra("ad_position", position);
                        mContext.startActivity(in);
                    }
                });
                userViewHolder.mBtnDetailsAd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("Position:", +position + "");
                        Log.e("Connected Email", mListAds.get(position).getAd_connected_email() + "jsdjskj");
                        showRequestAcceptedDialog(mContext, mListAds.get(position).getAd_connected_email(), mListAds.get(position).getAd_connected_username(), mListAds.get(position).getAd_connected_mobile());
                    }
                });
            } else if (holder instanceof LoadingViewHolder) {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
                if (mAdsType.equals(mContext.getResources().getString(R.string.active_ads))) {
                    if (ActiveAdFragment.isDataLoaded) {
                        loadingViewHolder.progressBar.setVisibility(View.GONE);
                    }
                } else if (mAdsType.equals(mContext.getResources().getString(R.string.complete_ads))) {
                    if (CompleteAdsFragment.isDataLoaded) {
                        loadingViewHolder.progressBar.setVisibility(View.GONE);
                    }
                } else if (mAdsType.equals(mContext.getResources().getString(R.string.incomplete_ads))) {
                    if (IncompleteAdsFragment.isDataLoaded) {
                        loadingViewHolder.progressBar.setVisibility(View.GONE);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private void getOthersProfileData(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
//                Error
                new Utility().showErrorDialog(mContext, result);
            } else {
                //Success
                dataPojo = result.getData();


                if (mReviewList.size() > 0) {
                    ReviewsListdapter mAdsListAdapter = new ReviewsListdapter(mContext, mReviewList, dataPojo);
                    mRvAds.setAdapter(mAdsListAdapter);
                    mAdsListAdapter.notifyDataSetChanged();
                    mRvAds.setHasFixedSize(true);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                    mRvAds.setLayoutManager(mLayoutManager);
                } else {
                    new Utility().showErrorDialogRequestFailed(mContext);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getReviewsData(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 200) {
                //Success
                dataPojo = result.getData();
                alertDialog.dismiss();


                Utility.showToastMessageShort(mContext, dataPojo.getRating());
                ReviewListPojo mReviewListPojo = new ReviewListPojo();
                mReviewListPojo.setUser_id(PreferenceHandler.readString(mContext, PreferenceHandler.USER_ID, ""));
                mReviewListPojo.setUser_name(PreferenceHandler.readString(mContext, PreferenceHandler.USER_NAME, ""));
                mReviewListPojo.setUser_review(met_review.getText().toString().trim());
                mReviewListPojo.setOther_User_id(mListAds.get(pos).getAd_connected_id());
                mReviewListPojo.setUser_rating_value(Math.round(mratingBar_popup.getRating()) + "");
                mReviewList.add(mReviewListPojo);
                dataPojo.setOther_review(mReviewList);

                //dataPojo.notifyDataSetChanged();
                mratingBar_popup.setRating(0);
                met_review.setText("");
                mListAds.get(pos).setIs_rating(true);
                mListAds.get(pos).setRating_value(rating + "");
                notifyDataSetChanged();

                for (int i = 0; i < mTempListAds.size(); i++) {
                    if (mTempListAds.get(i).getAd_id().equalsIgnoreCase(adIdPos)) {
                        adIdPosition = i;
                        break;
                    }
                }

                if (mTempListAds != null) {
                    if (mTempListAds.size() > 0) {
                        mTempListAds.get(adIdPosition).setIs_rating(true);
                        mTempListAds.get(adIdPosition).setRating_value(rating + "");
                    }
                }


            } else {
                //Error
                new Utility().showErrorDialog(mContext, result);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    ResponseHandlerListener responseHandlerListenerLogin = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            WebNotificationManager.unRegisterResponseListener(responseHandlerListenerLogin);
            if (error == null) {
                switch (mUrlNo) {
                    case 1:
                        getOthersProfileData(result);
                        break;
                    case 2:
                        getReviewsData(result);

                        break;
                }
            } else {
                // TODO display error dialog
                new Utility().showErrorDialogRequestFailed(mContext);
            }
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    };

    @Override
    public int getItemCount() {
        return mListAds == null ? 0 : mListAds.size();
    }


    @Override
    public int getItemViewType(int position) {
        return mListAds.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void showRequestAcceptedDialog(final Context mContext, final String mEmail, String mName, final String mPhoneNo) {
        try {
            LayoutInflater li = LayoutInflater.from(mContext);
            final View promptsView = li.inflate(R.layout.dialog_request_accepted, null);
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
                    R.style.CustomPopUpTheme);
            TextView tvOtherName = (TextView) promptsView.findViewById(R.id.tv_header_accepted);
            tvOtherName.setText(mContext.getResources().getString(R.string.requested) + " " + mName + " " + mContext.getResources().getString(R.string.requested_request));
            TextView tvOtherEmail = (TextView) promptsView.findViewById(R.id.tv_other_email);
            tvOtherEmail.setText(mEmail);
            tvOtherEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mEmail});
                    mContext.startActivity(emailIntent);
                }
            });
            TextView tvOtherPhoneNo = (TextView) promptsView.findViewById(R.id.tv_other_phone_no);
            tvOtherPhoneNo.setText(mPhoneNo);
            if (!mPhoneNo.equals("Not available")) {
                tvOtherPhoneNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                        phoneIntent.setData(Uri.parse("tel:" + mPhoneNo));
                        mContext.startActivity(phoneIntent);
                    }


                });
            } else
                tvOtherPhoneNo.setClickable(false);

            builder.setView(promptsView);
            final AlertDialog alertDialog = builder.create();
            Button ibClose = (Button) promptsView.findViewById(R.id.ib_close);
            ibClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Utility.showToastMessageLong(mContext,
                    mContext.getResources().getString(R.string.invalid_session));
        }
    }


    public void showRatingDialog(final Context mContext, final int position) {
        try {
            LayoutInflater li = LayoutInflater.from(mContext);
            final View promptsView = li.inflate(R.layout.layout_rating_popup, null);
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
                    R.style.CustomPopUpTheme);
            mbtnRating = (Button) promptsView.findViewById(R.id.btn_rating_submit);
            mratingBar_popup = (RatingBar) promptsView.findViewById(R.id.ratingBar_popup);
            met_review = (EditTextNonRegular) promptsView.findViewById(R.id.et_review);
            mbtnRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    rating = mratingBar_popup.getRating();
                    addReviews(position);

                   /* if(met_review.getText().equals(""))
                    {
                        Snackbar.make(promptsView,"Please Enter Review", Snackbar.LENGTH_LONG);
                    }
                    else {
                       String mReview =  met_review.getText().toString();
                        mratingBar_popup.getRating();
                    }*/
                }
            });

            builder.setView(promptsView);
            alertDialog = builder.create();

            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Utility.showToastMessageLong(mContext,
                    mContext.getResources().getString(R.string.invalid_session));
        }
    }


    private void addReviews(int position) {
        boolean val = new Validations().isValidateReviews(mContext, mratingBar_popup, met_review);
        if (val) {
            // Webservice Call
            // Step 1, Register Callback Interface
            WebNotificationManager.registerResponseListener(responseHandlerListenerLogin);
            // Step 2, Call Webservice Method
            WebServiceClient.addRating_MyAds(mContext, createReviewsPayload(position), true, 2, responseHandlerListenerLogin);
        }
    }


    private String createReviewsPayload(int position) {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", mListAds.get(position).getPosted_by_id());
                userData.put("auth_token", PreferenceHandler.readString(mContext, PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("rating_value", mratingBar_popup.getRating());
                userData.put("other_user_id", mListAds.get(position).getAd_connected_id());
                userData.put("ad_id", mListAds.get(position).getAd_id());
                userData.put("review", met_review.getText().toString().trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
            android.util.Log.e("My Ads Response:", "" + userData);
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }

    public void showDeletePopup(final Context mContext) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
                    R.style.CustomPopUpTheme);
            builder.setCancelable(false);
            builder.setMessage(mContext.getResources().getString(R.string.delete_popup_msg));

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //         Webservice Call
                    //         Step 1, Register Callback Interface
                    WebNotificationManager.registerResponseListener(responseHandlerListenerDeleteAd);
                    // Step 2, Call Webservice Method
                    WebServiceClient.cancelAnAd(mContext, deleteAnAdPayload(mListAds.get(cancelAdPosition).getAd_id()), true, 1, responseHandlerListenerDeleteAd);

                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bundle createBundle(int position, String adEditType) {
        Bundle mBundle = new Bundle();
        mBundle.putString("ad_id", mListAds.get(position).getAd_id());
        mBundle.putString("ad_connected_username", mListAds.get(position).getAd_connected_username());
        mBundle.putString("ad_connected_mobile", mListAds.get(position).getAd_connected_mobile());
        mBundle.putString("ad_connected_email", mListAds.get(position).getAd_connected_email());
        mBundle.putString("ad_category_id", mListAds.get(position).getCategory_id());
        mBundle.putString("ad_title", mListAds.get(position).getAd_title());
        mBundle.putString("ad_desc", mListAds.get(position).getAd_description());
        mBundle.putString("ad_no_people", mListAds.get(position).getAd_no_people());
        mBundle.putString("ad_date", mListAds.get(position).getAd_expiration_date());
        mBundle.putString("ad_time", mListAds.get(position).getAd_expiration_time());
        mBundle.putString("ad_country", mListAds.get(position).getAd_country_id());
        mBundle.putString("ad_state", mListAds.get(position).getAd_state());
        mBundle.putString("ad_city", mListAds.get(position).getAd_city_id());
        mBundle.putString("ad_country_name", mListAds.get(position).getAd_country_name());
        mBundle.putString("ad_state_name", mListAds.get(position).getAd_state_name());
        mBundle.putString("ad_city_name", mListAds.get(position).getAd_city_name());
        mBundle.putString("ad_currency", mListAds.get(position).getAd_cost());
        mBundle.putBoolean("is_rating", mListAds.get(position).getIs_Rating());
        mBundle.putString("rating_value", mListAds.get(position).getRating_value());
        mBundle.putString("ad_edit", adEditType);

        //Images
        List<ImagePojo> imagesList = new ArrayList<>();
        Log.e("dsvds", "mListAds :" + mListAds.get(position).getImage());
        imagesList = mListAds.get(position).getImage();
        if (imagesList.size() > 0) {
            ArrayList<String> idList = new ArrayList<>();
            ArrayList<String> urlList = new ArrayList<>();
            for (int i = 0; i < imagesList.size(); i++) {
                idList.add(imagesList.get(i).getImage_id());
                urlList.add(imagesList.get(i).getImage_url());
            }
            mBundle.putStringArrayList("id_list", idList);
            mBundle.putStringArrayList("url_list", urlList);

        } else {
            mBundle.putStringArrayList("id_list", null);
            mBundle.putStringArrayList("url_list", null);

        }
        return mBundle;
    }

    private String deleteAnAdPayload(String mAdId) {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(mContext, PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(mContext, PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("ad_id", mAdId);
                userData.put("ads_type", "cancel");
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

    ResponseHandlerListener responseHandlerListenerDeleteAd = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            try {
                WebNotificationManager.unRegisterResponseListener(responseHandlerListenerDeleteAd);
                switch (mUrlNo) {
                    case 1://to flag an ad
                        if (error == null) {
                            cancelAnAd(result);
                        } else {
                            // TODO display error dialog
                            new Utility().showErrorDialogRequestFailed(mContext);
                        }
                        if (mProgressDialog != null && mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        break;


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void cancelAnAd(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
                //Error
                new Utility().showErrorDialog(mContext, result);
            } else {//Success
                DataPojo dataPojo = result.getData();

                mListAds.remove(cancelAdPosition);
                notifyItemRemoved(cancelAdPosition);
                notifyItemRangeChanged(cancelAdPosition, mListAds.size());
                notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
