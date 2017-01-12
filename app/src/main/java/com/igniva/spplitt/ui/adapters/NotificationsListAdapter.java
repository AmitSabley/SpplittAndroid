package com.igniva.spplitt.ui.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.AdsListPojo;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.NotificationListPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.activties.MainActivity;
import com.igniva.spplitt.ui.activties.OtherProfileActivity;
import com.igniva.spplitt.ui.activties.ViewAdsDetailsActivity;

import com.igniva.spplitt.ui.fragments.ActiveAdFragment;
import com.igniva.spplitt.ui.fragments.CompleteAdsFragment;
import com.igniva.spplitt.ui.fragments.IncompleteAdsFragment;
import com.igniva.spplitt.ui.fragments.NotificationsFragment;
import com.igniva.spplitt.ui.fragments.OnLoadMoreListener;

import com.igniva.spplitt.ui.views.RoundedImageView;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.igniva.spplitt.ui.fragments.NotificationsFragment.ca;


/**
 * Created by igniva-php-08 on 17/6/16.
 */
public class NotificationsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<NotificationListPojo> mListAds;
    Context mContext;
    int cancelAdPosition;
    RecyclerView mRvAds;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 8;
    private int lastVisibleItem, totalItemCount;

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView mTvUserName;
        TextView mTvNotificationMsg;
        TextView mTvNotificationTime;
        RoundedImageView mRivUserImage;
        LinearLayout mLlMain;

        public UserViewHolder(View itemView) {
            super(itemView);
            mTvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            mTvNotificationMsg = (TextView) itemView.findViewById(R.id.tv_notification_msg);
            mTvNotificationTime = (TextView) itemView.findViewById(R.id.tv_notification_time);
            mRivUserImage = (RoundedImageView) itemView.findViewById(R.id.riv_user_img);
            mLlMain=(LinearLayout)itemView.findViewById(R.id.ll_main);

        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        LinearLayout mCvMain;
        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            mCvMain=(LinearLayout)itemView.findViewById(R.id.cv_category_main);
        }
    }

    public NotificationsListAdapter(Context context, List<NotificationListPojo> listAds, RecyclerView mRvAds) {
        this.mListAds = listAds;
        this.mContext = context;
        this.mRvAds = mRvAds;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRvAds.getLayoutManager();
        mRvAds.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                       @Override
                                       public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                           super.onScrolled(recyclerView, dx, dy);
                                           totalItemCount = linearLayoutManager.getItemCount();
                                           lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                                           if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                               if (mOnLoadMoreListener != null) {
                                                   mOnLoadMoreListener.onLoadMore();
                                               }
                                               isLoading = true;
                                           }
                                       }
                                   }

        );
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_my_notification_list, parent, false);
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
                userViewHolder.mTvUserName.setText(mListAds.get(position).getNotification_user_name());
                userViewHolder.mTvNotificationMsg.setText(mListAds.get(position).getNotification_message());

                Glide.with(mContext)
                        .load(WebServiceClient.BASE_URL+mListAds.get(position).getNotification_user_pic()).asBitmap()
                        .into(userViewHolder.mRivUserImage);

                CharSequence time=DateUtils.getRelativeTimeSpanString(getDateInMillis(mListAds.get(position).getNotification_created()),System.currentTimeMillis(),DateUtils.SECOND_IN_MILLIS,
                        DateUtils.FORMAT_ABBREV_RELATIVE);
                userViewHolder.mTvNotificationTime.setText(time);
                userViewHolder.mLlMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NotificationsFragment.cancelAsyncTask();
                        Intent in = new Intent(mContext, ViewAdsDetailsActivity.class);
                        in.putExtra("ad_id", mListAds.get(position).getNotification_ad_id());
                        in.putExtra("ad_position", position);
                        mContext.startActivity(in);
                    }
                });
                userViewHolder.mTvUserName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NotificationsFragment.cancelAsyncTask();
                        Intent in = new Intent(mContext, OtherProfileActivity.class);
                        in.putExtra("other_user_id", mListAds.get(position).getNotification_user_id());
                        mContext.startActivity(in);
                    }
                });
                userViewHolder.mRivUserImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NotificationsFragment.cancelAsyncTask();
                        Intent in = new Intent(mContext, OtherProfileActivity.class);
                        in.putExtra("other_user_id", mListAds.get(position).getNotification_user_id());
                        mContext.startActivity(in);
                    }
                });
            } else if (holder instanceof LoadingViewHolder) {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
                if(NotificationsFragment.isDataLoaded){
                    loadingViewHolder.mCvMain.setVisibility(View.GONE);
                    loadingViewHolder.progressBar.setVisibility(View.GONE);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public int getItemCount() {
        return mListAds == null ? 0 : mListAds.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemViewType(int position) {
        return mListAds.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public  long getDateInMillis(String srcDate) {
        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss");

        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
