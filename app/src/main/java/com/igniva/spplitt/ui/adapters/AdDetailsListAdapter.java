package com.igniva.spplitt.ui.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.ConnectionRequestsListPojo;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.activties.OtherProfileActivity;
import com.igniva.spplitt.ui.activties.ViewAdsDetailsActivity;
import com.igniva.spplitt.ui.fragments.ActiveAdFragment;
import com.igniva.spplitt.ui.fragments.IncompleteAdsFragment;
import com.igniva.spplitt.ui.fragments.ViewAllActiveAdsFragment;
import com.igniva.spplitt.ui.views.RoundedImageView;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by igniva-php-08 on 6/6/16.
 */
public class AdDetailsListAdapter extends RecyclerView.Adapter<AdDetailsListAdapter.ViewHolder> {

    List<ConnectionRequestsListPojo> mListRequestAds;
    Context mContext;
    DataPojo dataPojo;
    String mAdId;
    Button mBtnFlagAnAd;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    int clickPos;
    int list_count;
    ViewHolder mViewHolder;
    int connectPosition;

    public AdDetailsListAdapter(Context context, List<ConnectionRequestsListPojo> mListRequestAds, DataPojo dataPojo, String mAdId, int connectPosition) {

        this.mListRequestAds = mListRequestAds;
        this.mContext = context;
        this.dataPojo = dataPojo;
        this.mAdId = mAdId;
        this.connectPosition = connectPosition;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mViewAdTitle;
        TextView mViewAdSpplittCost;
        TextView mViewAdTime;
        TextView mViewAdLocation;
        TextView mViewAdUser;
        TextView mViewAdDesc;
        Button mBtnViewAdFlagAd;
        TextView mTvNoOfRequest;
        int Holderid;

        RoundedImageView mRivConnectorName;
        TextView mTvConnectorName;
        TextView mTvConnectorTime;
        Button mBtnAcceptAd;
        Button mBtnRejectAd;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            if (ViewType == TYPE_ITEM) {
                mRivConnectorName = (RoundedImageView) itemView.findViewById(R.id.riv_connecters_img);
                mTvConnectorName = (TextView) itemView.findViewById(R.id.tv_connector_name);
                mTvConnectorTime = (TextView) itemView.findViewById(R.id.tv_connector_time);
                mBtnAcceptAd = (Button) itemView.findViewById(R.id.btn_accept_ad);
                mBtnRejectAd = (Button) itemView.findViewById(R.id.btn_reject_ad);
                Holderid = 1;
            }
            if (ViewType == TYPE_HEADER) {
                Holderid = 0;
                mViewAdTitle = (TextView) itemView.findViewById(R.id.tv_viewad_title);
                mViewAdSpplittCost = (TextView) itemView.findViewById(R.id.tv_viewad_splitt_cost);
                mViewAdTime = (TextView) itemView.findViewById(R.id.tv_viewad_spplitt_time);
                mViewAdLocation = (TextView) itemView.findViewById(R.id.tv_viewad_owner_location);
                mViewAdUser = (TextView) itemView.findViewById(R.id.tv_viewad_user);
                mViewAdDesc = (TextView) itemView.findViewById(R.id.tv_viewad_desc);
                mTvNoOfRequest = (TextView) itemView.findViewById(R.id.tv_no_of_requests);
                mBtnViewAdFlagAd = (Button) itemView.findViewById(R.id.btn_flag_view_ad);
            }
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ad_details_footer, parent, false);
            ViewHolder vhItem = new ViewHolder(v, viewType);
            return vhItem;
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ad_details_header, parent, false);
            ViewHolder vhHeader = new ViewHolder(v, viewType);
            return vhHeader;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            mViewHolder = holder;
            //header
            if (holder.Holderid == 0) {
                holder.mViewAdTitle.setText(dataPojo.getAd_title());
                holder.mViewAdSpplittCost.setText(mContext.getResources().getString(R.string.spplitt_amount) + dataPojo.getAd_cost());

                java.text.DateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd");
                java.text.DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                String inputDateStr = dataPojo.getAd_expiration_date();
                Date date1 = inputFormat.parse(inputDateStr);
                String outputDateStr = outputFormat.format(date1);
                String string = outputDateStr + " " + dataPojo.getAd_expiration_time();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.ENGLISH);
                Date date = format.parse(string);

                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy, HH:mm");
                String newFormat = formatter.format(date);

                holder.mViewAdTime.setText(newFormat);
//                holder.mViewAdLocation.setText(dataPojo.getAd_city_name() + " ," + dataPojo.getAd_country_name());
                holder.mViewAdLocation.setText(dataPojo.getAd_city_name());
                holder.mViewAdUser.setText(" " + dataPojo.getPosted_by_username());
                holder.mViewAdDesc.setText(dataPojo.getAd_description());
                holder.mViewAdUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(mContext, OtherProfileActivity.class);
                        in.putExtra("other_user_id", dataPojo.getPosted_by_id());
                        mContext.startActivity(in);
                    }
                });


                if (dataPojo.getAd_status() == 1) {//active ads
                    holder.mBtnViewAdFlagAd.setVisibility(View.VISIBLE);
                    //to check ads are mine or other
                    if (PreferenceHandler.readString(mContext, PreferenceHandler.USER_ID, "").equals(dataPojo.getPosted_by_id())) {
                        holder.mBtnViewAdFlagAd.setVisibility(View.GONE);

                        //requests will come here
                    } else {//other

                        holder.mBtnViewAdFlagAd.setVisibility(View.VISIBLE);
                        if (dataPojo.is_flagged()) {
                            //ad is flagged or not
                            holder.mBtnViewAdFlagAd.setClickable(false);
                            holder.mBtnViewAdFlagAd.setFocusableInTouchMode(false);
                            holder.mBtnViewAdFlagAd.setEnabled(false);
                            holder.mBtnViewAdFlagAd.setCompoundDrawablesWithIntrinsicBounds(null, mContext.getResources().getDrawable(R.mipmap.active), null, null);
                        } else {
                            holder.mBtnViewAdFlagAd.setClickable(true);
                            holder.mBtnViewAdFlagAd.setCompoundDrawablesWithIntrinsicBounds(null, mContext.getResources().getDrawable(R.mipmap.inactive), null, null);
                            holder.mBtnViewAdFlagAd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mBtnFlagAnAd = holder.mBtnViewAdFlagAd;
                                    showCreateAccountDialog(mContext, mAdId, "");
                                }
                            });
                        }
                    }
                } else {//close ads
                    holder.mBtnViewAdFlagAd.setVisibility(View.GONE);
                }
                //to check ads are mine or other
                if (PreferenceHandler.readString(mContext, PreferenceHandler.USER_ID, "").equals(dataPojo.getPosted_by_id())) {
                    holder.mTvNoOfRequest.setVisibility(View.VISIBLE);
                    if(mListRequestAds.size()<=1) {
                        holder.mTvNoOfRequest.setText(mListRequestAds.size() + " Request");
                    }else{
                        holder.mTvNoOfRequest.setText(mListRequestAds.size() + " Requests");
                    }
                } else {//other
                    holder.mTvNoOfRequest.setVisibility(View.GONE);
                }
            }
            //footer
            else if (holder.Holderid == 1) {
                holder.mTvConnectorName.setText(mListRequestAds.get(position - 1).getConnected_by_name());


                java.text.DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.text.DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                String inputDateStr = mListRequestAds.get(position - 1).getConnected_by_time().split(" ")[0];
                Date date1 = inputFormat.parse(inputDateStr);
                String outputDateStr = outputFormat.format(date1);
                String string = outputDateStr + " " + mListRequestAds.get(position - 1).getConnected_by_time().split(" ")[1];
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH);
                Date date = format.parse(string);

                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy, HH:mm");
                String newFormat = formatter.format(date);
                holder.mTvConnectorTime.setText(newFormat);
                Glide.with(mContext)
                        .load(WebServiceClient.BASE_URL + mListRequestAds.get(position - 1).getConnected_by_photo()).asBitmap()
                        .into(holder.mRivConnectorName);
                if (dataPojo.getAd_status() == 5) {
                    mViewHolder.mBtnAcceptAd.setVisibility(View.GONE);
                    mViewHolder.mBtnRejectAd.setVisibility(View.GONE);
                } else {
                    if (mListRequestAds.get(position - 1).getConnectivity().equals("request")) {
                        if (mListRequestAds.get(position - 1).is_accept()) {
                            if (ViewAdsDetailsActivity.menuItem != null) {
                                ViewAdsDetailsActivity.showoptions = false;
                                ViewAdsDetailsActivity.details.onCreateOptionsMenu(ViewAdsDetailsActivity.menuItem);
                            }
                            mViewHolder.mBtnAcceptAd.setVisibility(View.VISIBLE);
                            mViewHolder.mBtnAcceptAd.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_check_circle_green, 0, 0, 0);
                            mViewHolder.mBtnAcceptAd.setClickable(false);
                            mViewHolder.mBtnAcceptAd.setFocusableInTouchMode(false);
                            mViewHolder.mBtnAcceptAd.setEnabled(false);
                        } else {
                            ViewAdsDetailsActivity.showoptions = true;
                            mViewHolder.mBtnAcceptAd.setVisibility(View.VISIBLE);
                            mViewHolder.mBtnAcceptAd.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_check_circle_grey, 0, 0, 0);
                            mViewHolder.mBtnAcceptAd.setClickable(true);
                        }
                        if (mListRequestAds.get(position - 1).is_reject()) {
                            if (ViewAdsDetailsActivity.menuItem != null) {
                                ViewAdsDetailsActivity.showoptions = false;
                                ViewAdsDetailsActivity.details.onCreateOptionsMenu(ViewAdsDetailsActivity.menuItem);
                            }
                            mViewHolder.mBtnRejectAd.setVisibility(View.VISIBLE);
                            mViewHolder.mBtnRejectAd.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_cancel_black_red, 0, 0, 0);
                            mViewHolder.mBtnRejectAd.setClickable(false);
                            mViewHolder.mBtnRejectAd.setFocusableInTouchMode(false);
                            mViewHolder.mBtnRejectAd.setEnabled(false);
                        } else {
                            ViewAdsDetailsActivity.showoptions = true;
                            mViewHolder.mBtnRejectAd.setVisibility(View.VISIBLE);
                            mViewHolder.mBtnRejectAd.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_cancel_black_grey, 0, 0, 0);
                            mViewHolder.mBtnRejectAd.setClickable(true);
                        }
                    } else if (mListRequestAds.get(position - 1).getConnectivity().equals("confirm")) {
                        if (mListRequestAds.get(position - 1).is_accept()) {
                            if (ViewAdsDetailsActivity.menuItem != null) {
                                ViewAdsDetailsActivity.showoptions = false;
                                ViewAdsDetailsActivity.details.onCreateOptionsMenu(ViewAdsDetailsActivity.menuItem);
                            }
                            mViewHolder.mBtnAcceptAd.setVisibility(View.VISIBLE);
                            mViewHolder.mBtnRejectAd.setVisibility(View.GONE);
                            mViewHolder.mBtnAcceptAd.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_check_circle_green, 0, 0, 0);
                            mViewHolder.mBtnAcceptAd.setClickable(false);
                            mViewHolder.mBtnAcceptAd.setFocusableInTouchMode(false);
                            mViewHolder.mBtnAcceptAd.setEnabled(false);
                        } else {
                            ViewAdsDetailsActivity.showoptions = true;
                            mViewHolder.mBtnAcceptAd.setVisibility(View.VISIBLE);
                            mViewHolder.mBtnRejectAd.setVisibility(View.GONE);
                        }

                    } else if (mListRequestAds.get(position - 1).getConnectivity().equals("reject")) {
                        if (ViewAdsDetailsActivity.menuItem != null) {
                            ViewAdsDetailsActivity.showoptions = false;
                            ViewAdsDetailsActivity.details.onCreateOptionsMenu(ViewAdsDetailsActivity.menuItem);
                        }
                        if (mListRequestAds.get(position - 1).is_accept()) {
                            mViewHolder.mBtnAcceptAd.setVisibility(View.VISIBLE);
                            mViewHolder.mBtnAcceptAd.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_check_circle_green, 0, 0, 0);
                            mViewHolder.mBtnAcceptAd.setClickable(false);
                            mViewHolder.mBtnAcceptAd.setFocusableInTouchMode(false);
                            mViewHolder.mBtnAcceptAd.setEnabled(false);
                        } else {
                            mViewHolder.mBtnAcceptAd.setVisibility(View.GONE);
                            mViewHolder.mBtnRejectAd.setVisibility(View.VISIBLE);
                            mViewHolder.mBtnRejectAd.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_cancel_black_red, 0, 0, 0);
                            mViewHolder.mBtnRejectAd.setClickable(false);
                            mViewHolder.mBtnRejectAd.setFocusableInTouchMode(false);
                            mViewHolder.mBtnRejectAd.setEnabled(false);
                        }

                    }
                }
                holder.mRivConnectorName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(mContext, OtherProfileActivity.class);
                        in.putExtra("other_user_id", mListRequestAds.get(position - 1).getConnected_by_id());
                        mContext.startActivity(in);
                    }
                });
                holder.mTvConnectorName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(mContext, OtherProfileActivity.class);
                        in.putExtra("other_user_id", mListRequestAds.get(position - 1).getConnected_by_id());
                        mContext.startActivity(in);
                    }
                });
                holder.mBtnAcceptAd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickPos = position;
                        showSuccessDialog(mContext, mListRequestAds.get(position - 1).getConnected_by_id(), mContext.getResources().getString(R.string.accept_msg), "confirm", 1);
                    }
                });
                holder.mBtnRejectAd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickPos = position;
                        showSuccessDialog(mContext, mListRequestAds.get(position - 1).getConnected_by_id(), mContext.getResources().getString(R.string.reject_msg), "rejected", 3);
                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public int getItemCount() {
        try {
            list_count = mListRequestAds.size() + 1;

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

    public void showSuccessDialog(final Context mContext, final String connected_by_id, String message, final String confirm, final int urlNo) {
        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
                    R.style.CustomPopUpTheme);
            builder.setMessage(message);

            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    showRequestAcceptedDialog(mContext);
                    //         Webservice Call
                    //         Step 1, Register Callback Interface
                    WebNotificationManager.registerResponseListener(responseHandlerListenerViewAdDetail);
                    // Step 2, Call Webservice Method
                    WebServiceClient.acceptAnAd(mContext, acceptAnAdPayload(confirm, mAdId, connected_by_id), true, urlNo, responseHandlerListenerViewAdDetail);

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

    private String acceptAnAdPayload(String mAdType, String mAdId, String mOtherUserId) {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(mContext, PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(mContext, PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("ads_type", mAdType);
                userData.put("ad_id", mAdId);
                userData.put("other_user_id", mOtherUserId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            payload = userData.toString();
            Log.e("Response", payload);
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }

    public void showRequestAcceptedDialog(final Context mContext, final DataPojo dataPojo) {
        try {
            LayoutInflater li = LayoutInflater.from(mContext);
            final View promptsView = li.inflate(R.layout.dialog_request_accepted, null);
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
                    R.style.CustomPopUpTheme);
            TextView tvOtherName = (TextView) promptsView.findViewById(R.id.tv_header_accepted);
            tvOtherName.setText(mContext.getResources().getString(R.string.requested) + " " + dataPojo.getOther_username() + " " + mContext.getResources().getString(R.string.requested_request));
            TextView tvOtherEmail = (TextView) promptsView.findViewById(R.id.tv_other_email);
            tvOtherEmail.setText(dataPojo.getOther_user_email());
            tvOtherEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{dataPojo.getOther_user_email()});
                    mContext.startActivity(emailIntent);
                }
            });
            TextView tvOtherPhoneNo = (TextView) promptsView.findViewById(R.id.tv_other_phone_no);
            tvOtherPhoneNo.setText(dataPojo.getOther_user_mobile());

            if (!dataPojo.getOther_user_mobile().equals("Not available")) {
                tvOtherPhoneNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                        phoneIntent.setData(Uri.parse("tel:" + dataPojo.getOther_user_mobile()));
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

    private boolean isPositionHeader(int position) {
        return position == 0;
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

            builder.setPositiveButton("OK", new AlertDialog.OnClickListener() {
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
                    if (!val) {
                        // Webservice Call
                        // Step 1, Register Callback Interface
                        WebNotificationManager.registerResponseListener(responseHandlerListenerViewAdDetail);
                        // Step 2, Call Webservice Method
                        WebServiceClient.flagAnAd(mContext, flagAnAdPayload(mAdId, mEtReportAbuseMsg.getText().toString(), mCategoryId), true, 2, responseHandlerListenerViewAdDetail);
                        alertDialog.dismiss();
                    } else {
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

//    public void showCreateAccountDialogUpdate(final Context mContext, final String mAdId, final String mCategoryId) {
//        try {
//            LayoutInflater li = LayoutInflater.from(mContext);
//            View promptsView = li.inflate(R.layout.dialog_flag_ad, null);
//            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
//                    R.style.CustomPopUpTheme);
//            // set prompts.xml to alertdialog builder
//            builder.setView(promptsView);
//
//            final EditText mEtReportAbuseMsg = (EditText) promptsView
//                    .findViewById(R.id.et_report_abuse_message);
//
//            builder.setCancelable(true);
//            mEtReportAbuseMsg.requestFocus();
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    boolean val = new Validations().isValidateTextDialog(mContext,mEtReportAbuseMsg);
//                    if (val) {
//                        // Webservice Call
//                        // Step 1, Register Callback Interface
//                        WebNotificationManager.registerResponseListener(responseHandlerListenerViewAdDetail);
//                        // Step 2, Call Webservice Method
//                        WebServiceClient.flagAnAd(mContext, flagAnAdPayload(mAdId, mEtReportAbuseMsg.getText().toString(), mCategoryId), true, 2, responseHandlerListenerViewAdDetail);
//                    }
//                }
//            });
//            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//
//            builder.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


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
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }

    private void flagAnAd(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
                //Error
                new Utility().showErrorDialog(mContext, result);
            } else {//Success
                DataPojo dataPojo = result.getData();
                mBtnFlagAnAd.setClickable(false);
                mBtnFlagAnAd.setFocusableInTouchMode(false);
                mBtnFlagAnAd.setEnabled(false);
                mBtnFlagAnAd.setCompoundDrawablesWithIntrinsicBounds(null, mContext.getResources().getDrawable(R.mipmap.active), null, null);
                if (ViewAllActiveAdsFragment.mViewAllAds != null) {
                    ViewAllActiveAdsFragment.mViewAllAds.getPositionFlag(connectPosition, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ResponseHandlerListener responseHandlerListenerViewAdDetail = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            try {
                WebNotificationManager.unRegisterResponseListener(responseHandlerListenerViewAdDetail);
                if (error == null) {
                    switch (mUrlNo) {
                        case 1:
                            acceptAnAd(result);
                            break;
                        case 2://to flag an add
                            flagAnAd(result);
                            break;
                        case 3:
                            rejectAnAd(result);

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

    private void acceptAnAd(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
                //Error
                new Utility().showErrorDialog(mContext, result);
            } else {//Success

                DataPojo dataPojo = result.getData();
                showRequestAcceptedDialog(mContext, dataPojo);
                for (int i = 0; i < mListRequestAds.size(); i++) {
                    mListRequestAds.get(i).setIs_accept(false);
                    mListRequestAds.get(i).setConnectivity("reject");
                }
                mListRequestAds.get(clickPos - 1).setIs_accept(true);
                mListRequestAds.get(clickPos - 1).setConnectivity("confirm");
                notifyDataSetChanged();
                if (ActiveAdFragment.mViewActiveAds != null) {
                    ActiveAdFragment.mViewActiveAds.getPosition(connectPosition);
                } else if (IncompleteAdsFragment.mViewIncompleteAds != null) {
                    IncompleteAdsFragment.mViewIncompleteAds.getPosition(connectPosition);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rejectAnAd(ResponsePojo result) {
        try {
            if (result.getStatus_code() == 400) {
                //Error
                new Utility().showErrorDialog(mContext, result);
            } else {//Success

                DataPojo dataPojo = result.getData();
                mListRequestAds.get(clickPos - 1).setIs_accept(false);
                mListRequestAds.get(clickPos - 1).setConnectivity("reject");
                notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

