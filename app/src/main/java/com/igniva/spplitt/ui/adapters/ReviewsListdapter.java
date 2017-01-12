package com.igniva.spplitt.ui.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ReviewListPojo;
import com.igniva.spplitt.ui.views.RoundedImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by igniva-php-08 on 6/6/16.
 */
public class ReviewsListdapter extends RecyclerView.Adapter<ReviewsListdapter.ViewHolder> implements View.OnClickListener{

    List<ReviewListPojo> mListAds;
    Context mContext;
    DataPojo dataPojo;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private int list_count;

    public ReviewsListdapter(Context context, List<ReviewListPojo> listAds, DataPojo dataPojo) {
        this.mListAds = listAds;
        this.mContext = context;
        this.dataPojo = dataPojo;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.tv_other_email:
//                Intent emailIntent = new Intent(Intent.ACTION_SEND);
//                emailIntent.setType("text/plain");
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {dataPojo.getOther_email() });
//                mContext.startActivity(emailIntent);
//                break;
//            case R.id.tv_other_phone_no:
//                Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
//                phoneIntent.setData(Uri.parse("tel:"+dataPojo.getOther_mobile()));
//                mContext.startActivity(phoneIntent);
//                break;

        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mLlRating;
        TextView mTvRating;
        AppCompatRatingBar mRbRating;
        RoundedImageView mRivReview;
        TextView mTvReviewName;
        TextView mTvReviewMsg;
        TextView mTvDateJoinedOn;
        TextView mTvGender;
        LinearLayout mLlAge;
        TextView mTvOtherAge;
        int Holderid;

        TextView mTvOtherEmail;
        TextView mTvOtherMobileNo;
        TextView mTvLocation;
        TextView mTvNoAdApplied;
        TextView mTvNoAdPosted;
        LinearLayout mLlAdsApplied;
        LinearLayout mLlAdsPosted;


        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            if (ViewType == TYPE_ITEM) {
                mLlRating = (RelativeLayout) itemView.findViewById(R.id.ll_rating);
                mTvRating = (TextView) itemView.findViewById(R.id.tv_rating);
                mRbRating = (AppCompatRatingBar) itemView.findViewById(R.id.arb_user_rating);
                mRivReview = (RoundedImageView) itemView.findViewById(R.id.riv_review_img);
                mTvReviewName = (TextView) itemView.findViewById(R.id.tv_review_name);
                mTvReviewMsg = (TextView) itemView.findViewById(R.id.tv_review_msg);

                Holderid = 1;
            }
            if (ViewType == TYPE_HEADER) {
                Holderid = 0;
                mTvOtherEmail = (TextView) itemView.findViewById(R.id.tv_other_email);
                mTvOtherMobileNo = (TextView) itemView.findViewById(R.id.tv_other_phone_no);
                mTvLocation = (TextView) itemView.findViewById(R.id.tv_other_location);
                mTvNoAdApplied = (TextView) itemView.findViewById(R.id.tv_ad_applied);
                mTvNoAdPosted = (TextView) itemView.findViewById(R.id.tv_ad_posted);
                mLlAdsApplied = (LinearLayout) itemView.findViewById(R.id.ll_ads_applied);
                mLlAdsPosted = (LinearLayout) itemView.findViewById(R.id.ll_ads_posted);
                mTvDateJoinedOn=(TextView)itemView.findViewById(R.id.tv_other_date_joined_on);
                mTvGender=(TextView)itemView.findViewById(R.id.tv_other_gender);
                mLlAge=(LinearLayout)itemView.findViewById(R.id.ll_age);
                mTvOtherAge=(TextView)itemView.findViewById(R.id.tv_other_age);
            }
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_other_profile_footer, parent, false);
            ViewHolder vhItem = new ViewHolder(v, viewType);
            return vhItem;
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_other_profile_header, parent, false);
            ViewHolder vhHeader = new ViewHolder(v, viewType);
            return vhHeader;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            //header
            if (holder.Holderid == 0) {
                if(!dataPojo.getOther_email().trim().equals("")){
                    holder.mTvOtherEmail.setText(dataPojo.getOther_email());
                    holder.mTvOtherEmail.setOnClickListener(this);
                }else{
                    holder.mTvOtherEmail.setText(mContext.getResources().getString(R.string.not_available));
                }
                if(!dataPojo.getOther_mobile().trim().equals("")){
                    holder.mTvOtherMobileNo.setText(dataPojo.getOther_mobile());
                    holder.mTvOtherMobileNo.setOnClickListener(this);
                }else{
                    holder.mTvOtherMobileNo.setText(mContext.getResources().getString(R.string.not_available));
                }


                holder.mTvLocation.setText(dataPojo.getOther_city_name() + ", " + dataPojo.getOther_country_name());
                holder.mTvNoAdApplied.setText(dataPojo.getOther_total_ads_applied() + "");
                holder.mTvNoAdPosted.setText(dataPojo.getOther_total_ads_added() + "");
                java.text.DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.text.DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String inputDateStr = dataPojo.getOther_created_date().split(" ")[0];
                Date date1 = inputFormat.parse(inputDateStr);
                String outputDateStr = outputFormat.format(date1);
                holder.mTvDateJoinedOn.setText(outputDateStr);
                if(dataPojo.getOther_gender().equals("m")) {
                    holder.mTvGender.setText("Male");
                }else if(dataPojo.getOther_gender().equals("f")) {
                    holder.mTvGender.setText("Female");
                }
                if(dataPojo.isOther_is_age()){
                    holder.mTvOtherAge.setText(dataPojo.getOther_age());
                    holder.mLlAge.setVisibility(View.VISIBLE);
                }else{
                    holder.mLlAge.setVisibility(View.GONE);
                }
            }
            //other drawer options
            else if (holder.Holderid == 1) {
                if(position==1) {
                    holder.mLlRating.setVisibility(View.VISIBLE);
                }else{
                    holder.mLlRating.setVisibility(View.GONE);
                }
                if(dataPojo.getOther_avg_rating().equals("")){//when user add rating and we had shown it statically
                    holder.mTvRating.setText("0/5");
                }else {
                    holder.mTvRating.setText(dataPojo.getOther_avg_rating() + "/5");
                }
                holder.mRbRating.setRating(Integer.parseInt(mListAds.get(position-1).getUser_rating_value()));
                holder.mTvReviewName.setText(mListAds.get(position-1).getUser_name());
                holder.mTvReviewMsg.setText(mListAds.get(position-1).getUser_review());
                Glide.with(mContext)
                        .load(WebServiceClient.BASE_URL+mListAds.get(position-1).getUser_image()).asBitmap()
                        .into(holder.mRivReview);
            }



        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public int getItemCount() {//return array.size
        try {
            list_count = mListAds.size() + 1;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return list_count;

    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


}

