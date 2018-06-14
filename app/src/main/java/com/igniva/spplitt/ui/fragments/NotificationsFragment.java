package com.igniva.spplitt.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.igniva.spplitt.App;
import com.igniva.spplitt.R;
import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebNotificationManager;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.DataPojo;
import com.igniva.spplitt.model.ErrorPojo;
import com.igniva.spplitt.model.NotificationListPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.activties.MainActivity;
import com.igniva.spplitt.ui.adapters.NotificationsListAdapter;
import com.igniva.spplitt.utils.Constants;
import com.igniva.spplitt.utils.Log;
import com.igniva.spplitt.utils.PreferenceHandler;
import com.igniva.spplitt.utils.Utility;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class NotificationsFragment extends BaseFragment implements View.OnClickListener {
    private static final String LOG_TAG = "NotificationsFragment" ;
    View mView;
    RecyclerView mRvAds;
    String mCatId;
    boolean _areLecturesLoaded = false;
    TextView mTvNoAdsFound;
    List<NotificationListPojo> mListAds = new ArrayList<NotificationListPojo>();
    LinearLayout mLlLoading;
    int mPageNo = 1;
    int mPosition;
    NotificationsListAdapter mUserAdapter;
    SearchView searchView;
    boolean isLoadMore;
    ProgressBar mProgressBar;
    public static boolean isDataLoaded;

    private enum HttpMethod {
        HTTP_GET, HTTP_POST, HTTP_PUT
    }
    private static ProgressDialog progressDialog;
    private static final String HTTP_GET_NOTIFICATIONS =  WebServiceClient.HTTP_PROTOCOL + WebServiceClient.HTTP_HOST_IP + "/notifications/getUserNotification";
    private static String url;
    private static HttpMethod method;
    private static int urlNo;
    private static ResponseHandlerListener responseHandlerListener;
    public static CallWebserviceTask ca;

    public static NotificationsFragment newInstance() {
        NotificationsFragment fragment = new NotificationsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_notifications, container, false);
        try {
            PreferenceHandler.writeInteger(getActivity(), PreferenceHandler.SHOW_EDIT_PROFILE, 1);
            setUpLayouts();
            setDataInViewLayouts();
            getActiveAds(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mView;
    }

    private void getActiveAds(boolean showProgress) {
        try {
//            Webservice Call
//         Step 1, Register Callback Interface
            WebNotificationManager.registerResponseListener(responseHandlerListenerViewAD);
//         Step 2, Call Webservice Method
            getNotificationsList(getContext(), getNotificationsPayload(), showProgress, 1, responseHandlerListenerViewAD);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private String getNotificationsPayload() {
        String payload = null;
        try {
            JSONObject userData = new JSONObject();
            try {
                userData.put("user_id", PreferenceHandler.readString(getActivity(), PreferenceHandler.USER_ID, ""));
                userData.put("auth_token", PreferenceHandler.readString(getActivity(), PreferenceHandler.AUTH_TOKEN, ""));
                userData.put("page_no", mPageNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //URL: http://spplitt.ignivastaging.com/notifications/getUserNotification
            //Payload: {"user_id":"19","auth_token":"Y8BCRO1WK8MU","page_no":1}
            Log.e("Response", "" + userData);
            payload = userData.toString();
        } catch (Exception e) {
            payload = null;
        }
        return payload;
    }
    @Override
    public void setUpLayouts() {
        try {
            mRvAds = (RecyclerView) mView.findViewById(R.id.rv_notifications);
            mLlLoading = (LinearLayout) mView.findViewById(R.id.ll_loading_more);
            mTvNoAdsFound = (TextView) mView.findViewById(R.id.tv_no_notifications_found);
            mTvNoAdsFound.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDataInViewLayouts() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currentFragmentId = Constants.FRAG_ID_NOTIFICATIONS;
        Log.e("NotificationsFragment", "OnResume Called");
        try {
            App.getInstance().trackScreenView(LOG_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    ResponseHandlerListener responseHandlerListenerViewAD = new ResponseHandlerListener() {
        @Override
        public void onComplete(ResponsePojo result, WebServiceClient.WebError error, ProgressDialog mProgressDialog, int mUrlNo) {
            try {
                WebNotificationManager.unRegisterResponseListener(responseHandlerListenerViewAD);
                if (error == null) {
                    switch (mUrlNo) {
                        case 1:
                            //to get categories list
                            getAdsData(result);
                            break;
                    }
                } else {
                    // TODO display error dialog
                    new Utility().showErrorDialogRequestFailed(getActivity());
                }
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                new Utility().showErrorDialogRequestFailed(getActivity());
            }
        }
    };




    private void getAdsData(ResponsePojo result) {
        try {

            if (result.getStatus_code() == 400) {
                isDataLoaded = true;
                if (mProgressBar != null) {
                    if (mProgressBar.getVisibility() == View.VISIBLE) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                }
                //Error
                ErrorPojo errorPojo = result.getError();
                if (errorPojo.getError_code().equals("540")) {

                    if (mPageNo > 1) {

                        if (mLlLoading.getVisibility() == View.VISIBLE) {
                            mLlLoading.setVisibility(View.GONE);

                        }
                    } else {
                        mRvAds.setAdapter(null);
                        mTvNoAdsFound.setVisibility(View.VISIBLE);
                        mRvAds.setVisibility(View.GONE);
                    }
                } else {
                    new Utility().showErrorDialog(getActivity(), result);
                }
            } else {//Success
                if (mListAds.size() > 0) {
                    mListAds.remove(mListAds.size() - 1);

                    mUserAdapter.notifyItemRemoved(mListAds.size());
                }
                DataPojo dataPojo = result.getData();
                mPosition=dataPojo.getTotal_page();
                mListAds.addAll(dataPojo.getNotification());

//                // Use Set to remove duplicacy
//                Set set = new HashSet(mListAds);
//                mListAds = new ArrayList(set);

                if (mListAds.size() > 0) {
                    if (!isLoadMore) {
                        setDataToList();
                        isLoadMore = true;
                    } else {
                        isDataLoaded = true;
                        if (mProgressBar != null) {
                            if (mProgressBar.getVisibility() == View.VISIBLE) {
                                mProgressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                        mUserAdapter.notifyDataSetChanged();
                        mUserAdapter.setLoaded();
                    }
                    mTvNoAdsFound.setVisibility(View.GONE);
                    mRvAds.setVisibility(View.VISIBLE);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataToList() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRvAds.setLayoutManager(mLayoutManager);
        mUserAdapter = new NotificationsListAdapter(getActivity(), mListAds, mRvAds);

//        final MyAdsListAdapter mAdsListAdapter = new MyAdsListAdapter(getActivity(), allAdsList, getResources().getString(R.string.active_ads),mRvAds);
        mRvAds.setAdapter(mUserAdapter);
        mRvAds.setHasFixedSize(true);
        if (mLlLoading.getVisibility() == View.VISIBLE) {
            mUserAdapter.notifyDataSetChanged();
            mUserAdapter.setLoaded();
            mLlLoading.setVisibility(View.GONE);
        }
        mUserAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                isDataLoaded = false;

                mListAds.add(null);
                mUserAdapter.notifyItemInserted(mListAds.size() - 1);
                if(mPageNo<mPosition) {
                    mPageNo++;
                    getActiveAds(false);
                }else{
                    isDataLoaded = true;
                }
//                mLlLoading.setVisibility(View.VISIBLE);

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_my_ads, menu);
        MenuItem item = menu.getItem(0);
        Drawable icon = item.getIcon();
        Utility.applyTint(icon);
        super.onCreateOptionsMenu(menu, inflater);
    }


    public static void getNotificationsList(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_GET_NOTIFICATIONS;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener=responseHandlerListenerLogin;

    }

    /**
     * Check Available Network connection and make http call only if network is
     * available else show no network available
     *
     * @param url      , the url to be called
     * @param _payload ,the data to be send while making http call
     * @param method   , the requested method
     * @param context  , the context of calling class
     */
    private static void checkNetworkState(String url, String _payload,
                                          HttpMethod method, Context context, boolean showProgress) {
        if (Utility.isInternetConnection(context)) {
            ca=  new CallWebserviceTask(url, _payload, method, context, showProgress);

            ca.execute();
        } else {
            // open dialog here
            new Utility().showNoInternetDialog((Activity) context);

        }
    }
    private static class CallWebserviceTask extends
            AsyncTask<Void, Void, Object[]> {
        private final String mUrl;
        private final String mPayload;
        private final HttpMethod mMethod;
        private Context mContext;
        private boolean mShowProgress;

        public CallWebserviceTask(String url, String _payload,
                                  HttpMethod method, final Context context, boolean showProgress) {
            Log.e(LOG_TAG,url);
            mContext = context;
            mUrl = url;
            mPayload = _payload;
            mMethod = method;
            mContext = context;
            mShowProgress = showProgress;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mShowProgress) {
                progressDialog = ProgressDialog.show(mContext, "", mContext
                                .getResources().getString(R.string.please_wait), true,
                        false);
            }
            // progressDialog.setCancelable(true);
        }

        @Override
        protected Object[] doInBackground(Void... vParams) {
//            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            ResponsePojo responsePojo = null;
            WebServiceClient.WebError error = null;
            String method = "";

            URL url = null;
            try {
                url = new URL(mUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setReadTimeout(50000);
                connection.setConnectTimeout(50000);
                if (mMethod.equals(HttpMethod.HTTP_GET)) {
                    connection.setRequestMethod("GET");
                } else if (mMethod.equals(HttpMethod.HTTP_POST)) {
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                }

                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(mPayload); //Writes out the string to the underlying output stream as a sequence of bytes
                dStream.flush(); // Flushes the data output stream.
                dStream.close(); // Closing the output stream.


                int successCode = connection.getResponseCode();

                if (successCode == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    StringBuilder responseOutput = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        responseOutput.append(line);
                    }
                    br.close();
                    Log.d(LOG_TAG, responseOutput.toString());

                    Gson gson = new Gson();
                    responsePojo = gson.fromJson(responseOutput.toString(), ResponsePojo.class);
                    Log.e("====="+responsePojo);
                    // output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
                } else {
                    Log.d(LOG_TAG, "Success code is " + successCode);
                    error = WebServiceClient.WebError.UNKNOWN;
                }
            } catch (Exception e) {
                error = WebServiceClient.WebError.UNKNOWN;
                e.printStackTrace();
            }

            return new Object[]{responsePojo, error};
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onPostExecute(Object[] result) {
            try {
//                if (isResponseHandled) {
                // TODO handle Authtoken Expiration
                try {
                    if (((ResponsePojo) result[0]).getStatus_code() == 1000) {
                        // Show Logout Dialog when user has logged in from another device
                        WebNotificationManager.unRegisterResponseListener(responseHandlerListener);
                        progressDialog.dismiss();
                        new Utility().showInvalidSessionDialogLogout(mContext, (ResponsePojo) result[0]);
                    } else {
                        WebNotificationManager.onResponseCallReturned(
                                (ResponsePojo) result[0], (WebServiceClient.WebError) result[1],
                                progressDialog, urlNo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    WebNotificationManager.onResponseCallReturned(
                            (ResponsePojo) result[0], (WebServiceClient.WebError) result[1],
                            progressDialog, urlNo);
                }
//                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public static void cancelAsyncTask(){
        if(ca.getStatus()==AsyncTask.Status.PENDING){
            ca.cancel(true);
        }else  if(ca.getStatus()==AsyncTask.Status.RUNNING){
            ca.cancel(true);
        }
    }
}