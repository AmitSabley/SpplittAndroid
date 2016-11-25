package com.igniva.spplitt.ui.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.AdsListPojo;
import com.igniva.spplitt.model.AdsPostedPojo;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.activties.OtherProfileActivity;
import com.igniva.spplitt.ui.activties.ViewAdsDetailsActivity;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by igniva-php-08 on 6/6/16.
 */
public class AdsAppliedListdapter extends RecyclerView.Adapter<AdsAppliedListdapter.ViewHolder> {

    List<AdsPostedPojo> mListAds;
    Context mContext;
    Button mBttnFlagAd;
    boolean mIsCloseAds;

    public AdsAppliedListdapter(Context context, List<AdsPostedPojo> listAds, boolean isCloseAds) {
        this.mListAds = listAds;
        this.mContext = context;
        this.mIsCloseAds = isCloseAds;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvAdTitle;
        TextView mTvAdTime;
        TextView mTvSplittAmount;
        TextView mTvAdOwnerName;
        TextView mTvAdLocation;


        public ViewHolder(View itemView) {
            super(itemView);
            mTvAdTitle = (TextView) itemView.findViewById(R.id.tv_ad_title);
            mTvAdTime = (TextView) itemView.findViewById(R.id.tv_ad_time);
            mTvSplittAmount = (TextView) itemView.findViewById(R.id.tv_spplitt_amount);
            mTvAdOwnerName = (TextView) itemView.findViewById(R.id.tv_ad_owner_name);
            mTvAdLocation = (TextView) itemView.findViewById(R.id.tv_ad_location);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ad_applied_list, parent, false);
        ViewHolder vhItem = new ViewHolder(v);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {

            holder.mTvAdTitle.setText(mListAds.get(position).getAd_title());
            holder.mTvSplittAmount.setText(mContext.getResources().getString(R.string.spplitt_amount)+ mListAds.get(position).getAd_cost());
            holder.mTvAdOwnerName.setText(Html.fromHtml(mListAds.get(position).getPosted_by_username()));
//            holder.mTvAdLocation.setText(mListAds.get(position).getAd_city_name()+","+mListAds.get(position).getAd_country_name());
//            String[] date=mListAds.get(position).getExpiration_date().split("/");
            DateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd");
            DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            String inputDateStr = mListAds.get(position).getAd_expiration_date();
            Date date1 = inputFormat.parse(inputDateStr);
            String outputDateStr = outputFormat.format(date1);
            String string = outputDateStr + " " + mListAds.get(position).getAd_expiration_time();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.ENGLISH);
            Date date = format.parse(string);

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
            String newFormat = formatter.format(date);

            holder.mTvAdTime.setText(newFormat);
            holder.mTvAdLocation.setText(mListAds.get(position).getAd_city_name() + " , " + mListAds.get(position).getAd_country_name());


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public int getItemCount() {//return array.size
        return mListAds.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}

