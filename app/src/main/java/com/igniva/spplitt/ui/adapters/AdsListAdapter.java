package com.igniva.spplitt.ui.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.AdsListPojo;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.model.ReviewListPojo;
import com.igniva.spplitt.ui.activties.OtherProfileActivity;
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
 * Created by igniva-php-08 on 23/5/16.
 */
public class AdsListAdapter extends RecyclerView.Adapter<AdsListAdapter.ViewHolder> {

    List<AdsListPojo> mListAds;
    Context mContext;
    RecyclerView mRvAds;
    Button mBttnFlagAd;
    Button mBttnConnectAd;
    boolean mIsCloseAds;
    int connectPosition;
    int flagPosition;
    int pos = 0;
    private Button mbtnRating;
    private RatingBar mratingBar_popup;
    private EditTextNonRegular met_review;
    private DataPojo dataPojo;
    float rating = 0;
    List<ReviewListPojo> mReviewList = new ArrayList<>();
    AlertDialog alertDialog;


    public AdsListAdapter(Context context, List<AdsListPojo> listAds, boolean isCloseAds) {
        this.mListAds = listAds;
        this.mContext = context;
        this.mIsCloseAds = isCloseAds;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatRatingBar ratingBar;
        TextView mTvAdTitle;
        TextView mTvAdTime;
        TextView mTvSplittAmount;
        TextView mTvAdOwnerName;
        //        TextView mTvAdDetails;
        ImageView mBtnRateUser;
        TextView mTvAdLocation;
        Button mBtnFlagAd;
        Button mBtnConnectAd;
        LinearLayout mCvmain;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvAdTitle = (TextView) itemView.findViewById(R.id.tv_ad_title);
            mTvAdTime = (TextView) itemView.findViewById(R.id.tv_ad_time);
            mTvSplittAmount = (TextView) itemView.findViewById(R.id.tv_spplitt_amount);
            mTvAdOwnerName = (TextView) itemView.findViewById(R.id.tv_ad_owner_name);
            ratingBar = (AppCompatRatingBar) itemView.findViewById(R.id.userRating_Completed);
            mBtnRateUser = (ImageView) itemView.findViewById(R.id.ButtonRate_User);

//            mTvAdDetails = (TextView) itemView.findViewById(R.id.tv_ads_details);
            mBtnFlagAd = (Button) itemView.findViewById(R.id.btn_flag_ad);
            mBtnConnectAd = (Button) itemView.findViewById(R.id.btn_connect_ad);
            mCvmain = (LinearLayout) itemView.findViewById(R.id.cv_category_main);
            mTvAdLocation = (TextView) itemView.findViewById(R.id.tv_ad_location);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ad_list, parent, false);
        ViewHolder vhItem = new ViewHolder(v);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {

            holder.mTvAdTitle.setText(mListAds.get(position).getAd_title());
            holder.mTvSplittAmount.setText(mContext.getResources().getString(R.string.spplitt_amount) + mListAds.get(position).getAd_cost());
            holder.mTvAdLocation.setText(mListAds.get(position).getAd_city_name());
            holder.mTvAdOwnerName.setText(Html.fromHtml("<u>" + mListAds.get(position).getPosted_by_username() + "</u>"));
//            String[] date=mListAds.get(position).getExpiration_date().split("/");
            java.text.DateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd");
            java.text.DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            String inputDateStr = mListAds.get(position).getAd_expiration_date();
            Date date1 = inputFormat.parse(inputDateStr);
            String outputDateStr = outputFormat.format(date1);
            String string = outputDateStr + " " + mListAds.get(position).getAd_expiration_time();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.ENGLISH);
            Date date = format.parse(string);

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy, HH:mm");
            String newFormat = formatter.format(date);

            holder.mTvAdTime.setText(newFormat);

            //to change color of specific selected flag at on scroll
            if (mBttnFlagAd != null) {
                holder.mBtnFlagAd.setClickable(false);
                mBttnFlagAd.setCompoundDrawablesWithIntrinsicBounds(null, mContext.getResources().getDrawable(R.mipmap.inactive), null, null);
            }
            boolean isRating = mListAds.get(position).getIs_Rating();
            String rating;
            if (isRating) {
                holder.ratingBar.setVisibility(View.GONE);
                holder.mBtnRateUser.setVisibility(View.GONE);
                rating = mListAds.get(position).getRating_value();
                float frating = Float.parseFloat(rating);
                holder.ratingBar.setRating(frating);

            } else

            {
                holder.mBtnRateUser.setVisibility(View.GONE);
                holder.mBtnRateUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pos = position;
                        // showRatingDialog(mContext, position);
                    }
                });


            }
            if (mIsCloseAds) {//close or active ads
                holder.mBtnFlagAd.setVisibility(View.VISIBLE);
                holder.mBtnConnectAd.setVisibility(View.VISIBLE);
                if (PreferenceHandler.readString(mContext, PreferenceHandler.USER_ID, "").equals(mListAds.get(position).getPosted_by_id())) {
                    //to show connect and flag button (my own adds)
                    holder.mBtnConnectAd.setVisibility(View.GONE);
                    holder.mBtnFlagAd.setVisibility(View.GONE);
                } else {//other adds
                    if (mListAds.get(position).is_connect()) {
                        holder.mBtnConnectAd.setEnabled(false);
                        holder.mBtnConnectAd.setClickable(false);
                        holder.mBtnConnectAd.setBackgroundColor(mContext.getResources().getColor(R.color.colorGreyDark));
                        holder.mBtnConnectAd.setText(mContext.getResources().getString(R.string.response_pending));
                    } else {
                        holder.mBtnConnectAd.setEnabled(true);
                        holder.mBtnConnectAd.setClickable(true);
                        holder.mBtnConnectAd.setBackgroundColor(mContext.getResources().getColor(R.color.colorDarkGreen));
                        holder.mBtnConnectAd.setText(mContext.getResources().getString(R.string.connect));
                    }
                    holder.mBtnConnectAd.setVisibility(View.VISIBLE);
                    holder.mBtnFlagAd.setVisibility(View.VISIBLE);
                    if (mListAds.get(position).is_flagged()) {
                        //ad is flagged or not
                        holder.mBtnFlagAd.setClickable(false);
                        holder.mBtnFlagAd.setCompoundDrawablesWithIntrinsicBounds(null, mContext.getResources().getDrawable(R.mipmap.active), null, null);
                    } else {
                        holder.mBtnFlagAd.setClickable(true);
                        holder.mBtnFlagAd.setCompoundDrawablesWithIntrinsicBounds(null, mContext.getResources().getDrawable(R.mipmap.inactive), null, null);
                        holder.mBtnFlagAd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mBttnFlagAd = holder.mBtnFlagAd;
                                connectPosition = position;
                                showCreateAccountDialog(mContext, mListAds.get(position).getAd_id(), mListAds.get(position).getCategory_id());
                            }
                        });
                    }
                }
            } else {
                holder.mBtnFlagAd.setVisibility(View.GONE);
                holder.mBtnConnectAd.setVisibility(View.GONE);
            }

            holder.mBtnConnectAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBttnConnectAd = holder.mBtnConnectAd;
                    connectPosition = position;
                    //         Webservice Call
                    //         Step 1, Register Callback Interface
                    WebNotificationManager.registerResponseListener(responseHandlerListenerFlagAd);
                    // Step 2, Call Webservice Method
                    WebServiceClient.connectAnAd(mContext, connectAnAdPayload(mListAds.get(position).getAd_id()), true, 2, responseHandlerListenerFlagAd);

                }
            });
            holder.mCvmain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Gson gson = new Gson();
                    String json = gson.toJson(mListAds);
                    AdsListPojo mAdsListPojo = mListAds.get(position);
                    Intent in = new Intent(mContext, ViewAdsDetailsActivity.class);
                    in.putExtra("ad_id", mListAds.get(position).getAd_id());
                    in.putExtra("ad_position", position);
                    in.putExtra("ad_list", json);
                    in.putExtra("ad_object", mAdsListPojo);
                    mContext.startActivity(in);
                }
            });
            holder.mTvAdOwnerName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(mContext, OtherProfileActivity.class);
                    in.putExtra("other_user_id", mListAds.get(position).getPosted_by_id());
                    mContext.startActivity(in);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

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
            android.util.Log.e("Response Review", "" + userData);
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
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
    public int getItemCount() {//return array.size
        return mListAds.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public void showCreateAccountDialog(final Context mContext, final String mAdId, final String mCategoryId) {
        try {
            LayoutInflater li = LayoutInflater.from(mContext);
            View promptsView = li.inflate(R.layout.dialog_flag_ad, null);
             AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
                    R.style.CustomPopUpTheme);
            // set prompts.xml to alertdialog builder

            final EditText mEtReportAbuseMsg = (EditText) promptsView
                    .findViewById(R.id.et_report_abuse_message);
            builder.setCancelable(true);

                builder.setPositiveButton("OK", new AlertDialog.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

            builder.setNegativeButton("CANCEL", new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.setView(promptsView);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean val = (mEtReportAbuseMsg.getText().toString().trim().isEmpty());
                    // if EditText is empty disable closing on possitive button
                    if (!val)
                    {
                        // Webservice Call
                        // Step 1, Register Callback Interface
                        WebNotificationManager.registerResponseListener(responseHandlerListenerFlagAd);
                        // Step 2, Call Webservice Method
                        WebServiceClient.flagAnAd(mContext, flagAnAdPayload(mAdId, mEtReportAbuseMsg.getText().toString(), mCategoryId), true, 2, responseHandlerListenerFlagAd);
                        alertDialog.dismiss();
                    }
                    else{
                        Log.e("Enter your text", "Enter your text");
                        Utility.showToastMessageLong(mContext, "Enter your Text");
                    }
                }

            });


            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String connectAnAdPayload(String mAdId) {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(mContext, PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(mContext, PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("ad_id", mAdId);
                userData.put("ads_type", "request");
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

    private String flagAnAdPayload(String mAdId, String mEtReportAbuseMsg, String mCategoryId) {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(mContext, PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(mContext, PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("ad_id", mAdId);
                userData.put("text_message", mEtReportAbuseMsg);
                userData.put("cat_id", mCategoryId);
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

    ResponseHandlerListener responseHandlerListenerFlagAd = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            try {
                WebNotificationManager.unRegisterResponseListener(responseHandlerListenerFlagAd);
                if (error == null) {
                    switch (mUrlNo) {
                        case 1://to flag an ad
                            flagAnAd(result);
                            break;
                        case 2:
                            connectAnAd(result);
                            break;
                    }
                } else {
                    // TODO display error dialog
                    new Utility().showErrorDialogRequestFailed(mContext);
                }
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void connectAnAd(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
                //Error
                new Utility().showErrorDialog(mContext, result);
            } else {//Success
                //Error
                showSuccessDialog(mContext, result, result.getDescription());
                DataPojo dataPojo = result.getData();
                mBttnConnectAd.setClickable(false);
                mBttnConnectAd.setEnabled(false);
                mListAds.get(connectPosition).setIs_connect(true);

                mBttnConnectAd.setBackgroundColor(mContext.getResources().getColor(R.color.colorGreyDark));
                mBttnConnectAd.setText(mContext.getResources().getString(R.string.response_pending));


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void flagAnAd(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
                //Error
                new Utility().showErrorDialog(mContext, result);
            } else {//Success
                //Error
                showSuccessDialog(mContext, result, mContext.getString(R.string.success_msg_flag));
                DataPojo dataPojo = result.getData();
                mBttnFlagAd.setClickable(false);
                mListAds.get(connectPosition).setIs_flagged(true);

                mBttnFlagAd.setCompoundDrawablesWithIntrinsicBounds(null, mContext.getResources().getDrawable(R.mipmap.active), null, null);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSuccessDialog(final Context mContext, ResponsePojo result, String msg) {
        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
                    R.style.CustomPopUpTheme);
            builder.setMessage(msg);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
            Utility.showToastMessageLong(mContext,
                    mContext.getResources().getString(R.string.invalid_session));
        }
    }
}

