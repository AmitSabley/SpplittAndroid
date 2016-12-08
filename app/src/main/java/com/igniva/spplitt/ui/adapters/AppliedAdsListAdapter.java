package com.igniva.spplitt.ui.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.AppliedlistPojo;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.model.ReviewListPojo;
import com.igniva.spplitt.ui.activties.ViewAdsDetailsActivity;
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
 * Created by igniva-php-08 on 21/6/16.
 */
public class AppliedAdsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<AppliedlistPojo> mListAds;
    Context mContext;
    String mAdsType;
    int cancelAdPosition;
    RecyclerView mRvAds;
    private DataPojo dataPojo;
    int pos = 0;
    float rating = 0;
    List<ReviewListPojo> mReviewList = new ArrayList<>();
    AlertDialog alertDialog;
    private Button mbtnRating;
    private RatingBar mratingBar_popup;
    private EditTextNonRegular met_review;


    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView mTvAdTitle;
        TextView mTvAdTime;
        TextView mTvSplittAmount;
        AppCompatRatingBar ratingBar;
        ImageView mBtnRateUser;
        TextView mTvAdTotalRequests;
        Button mBtnDetailsAd;
        CardView mCvAdsMain;
        TextView mTvAdLocation;


        public UserViewHolder(View itemView) {
            super(itemView);
            mTvAdTitle = (TextView) itemView.findViewById(R.id.tv_ad_title);
            mTvAdTime = (TextView) itemView.findViewById(R.id.tv_ad_time);
            mTvSplittAmount = (TextView) itemView.findViewById(R.id.tv_spplitt_amount);
            ratingBar = (AppCompatRatingBar) itemView.findViewById(R.id.userRating_Completed);
            mBtnRateUser = (ImageView) itemView.findViewById(R.id.ButtonRate_User);
            mTvAdTotalRequests = (TextView) itemView.findViewById(R.id.tv_ad_requests);
            mBtnDetailsAd = (Button) itemView.findViewById(R.id.btn_details_ad);
            mCvAdsMain = (CardView) itemView.findViewById(R.id.cv_category_main);
            mTvAdLocation = (TextView) itemView.findViewById(R.id.tv_ad_location);
        }
    }


    public AppliedAdsListAdapter(Context context, List<AppliedlistPojo> listAds, String ads_type,RecyclerView mRvAds) {
        this.mListAds = listAds;
        this.mContext = context;
        this.mRvAds = mRvAds;
        this.mAdsType = ads_type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_applied_ad_list, parent, false);
        UserViewHolder vhItem = new UserViewHolder(v);
        return vhItem;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            UserViewHolder userViewHolder = (UserViewHolder) holder;
            userViewHolder.mTvAdTitle.setText(mListAds.get(position).getAd_title());
            userViewHolder.mTvSplittAmount.setText(mContext.getResources().getString(R.string.spplitt_amount) + mListAds.get(position).getAd_cost());
            userViewHolder.mTvAdTotalRequests.setText(mListAds.get(position).getAd_total_request() + " Requests");
            userViewHolder.mTvAdLocation.setText(mListAds.get(position).getAd_city_name());
//            userViewHolder.mTvAdLocation.setText(mListAds.get(position).getAd_city_name() + "," + mListAds.get(position).getAd_country_name());
            java.text.DateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd");
            java.text.DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            String inputDateStr = mListAds.get(position).getAd_expiration_date();
            Date date1 = inputFormat.parse(inputDateStr);
            String outputDateStr = outputFormat.format(date1);
            String string = outputDateStr + " " + mListAds.get(position).getAd_expiration_time();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.ENGLISH);
            Date date = format.parse(string);
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
                userViewHolder.mBtnRateUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pos = position;
                        showRatingDialog(mContext, position);
                    }
                });


            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy, HH:mm");
            String newFormat = formatter.format(date);
            userViewHolder.mTvAdTime.setText(newFormat);

            if (mListAds.get(position).is_details_visible()) {
                userViewHolder.mBtnDetailsAd.setVisibility(View.VISIBLE);
            } else {
                userViewHolder.mBtnDetailsAd.setVisibility(View.GONE);
            }

            if (mAdsType.equals(mContext.getResources().getString(R.string.rejected_ads))) {
                userViewHolder.mTvAdTotalRequests.setVisibility(View.VISIBLE);
                userViewHolder.mBtnDetailsAd.setVisibility(View.GONE);
                userViewHolder.mBtnRateUser.setVisibility(View.GONE);
                userViewHolder.ratingBar.setVisibility(View.GONE);

            } else if (mAdsType.equals(mContext.getResources().getString(R.string.complete_ads))) {
                userViewHolder.mTvAdTotalRequests.setVisibility(View.VISIBLE);
            } else if (mAdsType.equals(mContext.getResources().getString(R.string.awaited_ads))) {
                userViewHolder.mTvAdTotalRequests.setVisibility(View.GONE);
                userViewHolder.mBtnDetailsAd.setVisibility(View.GONE);
                userViewHolder.mBtnRateUser.setVisibility(View.GONE);
                userViewHolder.ratingBar.setVisibility(View.GONE);
            }


            userViewHolder.mBtnDetailsAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showRequestAcceptedDialog(mContext, mListAds.get(position).getOwner_details().getOwner_email(), mListAds.get(position).getOwner_details().getOwner_username(), mListAds.get(position).getOwner_details().getOwner_mobile());
                }
            });

            userViewHolder.mCvAdsMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(mContext, ViewAdsDetailsActivity.class);
                    in.putExtra("ad_id", mListAds.get(position).getAd_id());
                    mContext.startActivity(in);
                }
            });

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


//                if(mReviewList.size()>0) {
                ReviewsListdapter mAdsListAdapter = new ReviewsListdapter(mContext, mReviewList, dataPojo);
                mRvAds.setAdapter(mAdsListAdapter);
                mAdsListAdapter.notifyDataSetChanged();
                mRvAds.setHasFixedSize(true);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                mRvAds.setLayoutManager(mLayoutManager);
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getReviewsData(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
//                Error
                new Utility().showErrorDialog(mContext, result);
            } else {
                //Success
                dataPojo = result.getData();
                alertDialog.dismiss();


                Utility.showToastMessageShort(mContext, dataPojo.getRating());
                ReviewListPojo mReviewListPojo = new ReviewListPojo();
                mReviewListPojo.setUser_id(PreferenceHandler.readString(mContext, PreferenceHandler.USER_ID, ""));
                mReviewListPojo.setUser_name(PreferenceHandler.readString(mContext, PreferenceHandler.USER_NAME, ""));
                mReviewListPojo.setUser_review(met_review.getText().toString().trim());
                mReviewListPojo.setUser_rating_value(Math.round(mratingBar_popup.getRating()) + "");
                mReviewList.add(mReviewListPojo);
                dataPojo.setOther_review(mReviewList);

                //dataPojo.notifyDataSetChanged();
                mratingBar_popup.setRating(0);
                met_review.setText("");

                mListAds.get(pos).setIs_rating(true);
                Log.e("rating ", rating + "");
                mListAds.get(pos).setRating_value(rating + "");

                notifyDataSetChanged();


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
        return position;
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
            Log.e("==========", "======" + mPhoneNo);
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
                userData.put("user_id", PreferenceHandler.readString(mContext, PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(mContext, PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("rating_value", mratingBar_popup.getRating());
                userData.put("other_user_id", mListAds.get(position).getPosted_by_id());
                userData.put("ad_id", mListAds.get(position).getAd_id());
                userData.put("review", met_review.getText().toString().trim());
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

}
