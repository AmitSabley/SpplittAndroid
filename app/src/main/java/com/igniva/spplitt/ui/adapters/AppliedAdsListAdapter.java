package com.igniva.spplitt.ui.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.AppliedlistPojo;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.activties.MainActivity;
import com.igniva.spplitt.ui.activties.ViewAdsDetailsActivity;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by igniva-php-08 on 21/6/16.
 */
public class AppliedAdsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<AppliedlistPojo> mListAds;
    Context mContext;
    int cancelAdPosition;
    RecyclerView mRvAds;

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView mTvAdTitle;
        TextView mTvAdTime;
        TextView mTvSplittAmount;
        TextView mTvAdTotalRequests;
        Button mBtnDetailsAd;
        CardView mCvAdsMain;
        TextView mTvAdLocation;

        public UserViewHolder(View itemView) {
            super(itemView);
            mTvAdTitle = (TextView) itemView.findViewById(R.id.tv_ad_title);
            mTvAdTime = (TextView) itemView.findViewById(R.id.tv_ad_time);
            mTvSplittAmount = (TextView) itemView.findViewById(R.id.tv_spplitt_amount);

            mTvAdTotalRequests = (TextView) itemView.findViewById(R.id.tv_ad_requests);
            mBtnDetailsAd = (Button) itemView.findViewById(R.id.btn_details_ad);
            mCvAdsMain = (CardView) itemView.findViewById(R.id.cv_category_main);
            mTvAdLocation = (TextView) itemView.findViewById(R.id.tv_ad_location);
        }
    }


    public AppliedAdsListAdapter(Context context, List<AppliedlistPojo> listAds, RecyclerView mRvAds) {
        this.mListAds = listAds;
        this.mContext = context;
        this.mRvAds = mRvAds;
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

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy, HH:mm");
            String newFormat = formatter.format(date);
            userViewHolder.mTvAdTime.setText(newFormat);

            if (mListAds.get(position).is_details_visible()) {
                userViewHolder.mBtnDetailsAd.setVisibility(View.VISIBLE);
            } else {
                userViewHolder.mBtnDetailsAd.setVisibility(View.GONE);
            }

            userViewHolder.mBtnDetailsAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showRequestAcceptedDialog(mContext,mListAds.get(position).getOwner_details().getOwner_email(),mListAds.get(position).getOwner_details().getOwner_username(),mListAds.get(position).getOwner_details().getOwner_mobile());
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
            tvOtherName.setText(mContext.getResources().getString(R.string.requested) +" "+ mName+" " + mContext.getResources().getString(R.string.requested_request));
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
            Log.e("==========","======"+mPhoneNo);
            tvOtherPhoneNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!mPhoneNo.equals("") || !mPhoneNo.equals("Not available")) {
                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                        phoneIntent.setData(Uri.parse("tel:" + mPhoneNo));
                        mContext.startActivity(phoneIntent);
                    }
                }
            });

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

}
