package com.igniva.spplitt.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.igniva.spplitt.R;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.utils.Log;
import com.igniva.spplitt.utils.Utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class WebServiceClient {


    // Global variables
    Context mContext;
    private static final String LOG_TAG = "WebServiceClient";
    private static String url;
    private static String payload;
    private static HttpMethod method;
    private static ProgressDialog progressDialog;
    private static int urlNo;
    private static ResponseHandlerListener responseHandlerListener;

    private enum HttpMethod {
        HTTP_GET, HTTP_POST, HTTP_PUT
    }


    public enum WebError {
        INVALID_CREDENTIALS, UNAUTHORIZED, PASSWORD_FORMAT_ERROR,
        CURRENT_PASSWORD_INVALID, INVALID_JSON, INCOMPLETE_JSON,
        NO_CREDENTIALS_STORED, CONNECTION_ERROR, EMAIL_NOT_FOUND_FOR_RESET_PASSWORD,
        SAVE_FILE_ERROR, UNKNOWN, USER_ALREADY_EXISTS, NOT_FOUND
    }

    private final static String CONTENT_TYPE = "application/json";

    /**
     * Webservice Urls
     */

    public static final String HTTP_PROTOCOL = "http://";
//    public static final String HTTP_HOST_IP = "api.spplitt.com";
//    public final static String BASE_URL="http://api.spplitt.com/";

    public static final String HTTP_HOST_IP = "spplitt.ignivastaging.com";
    public final static String BASE_URL = "http://spplitt.ignivastaging.com/";


    public static final String HTTP_LOGIN = HTTP_PROTOCOL + HTTP_HOST_IP + "/users/login?";
    public static final String HTTP_REGISTR = HTTP_PROTOCOL + HTTP_HOST_IP + "/users/register";
    public static final String HTTP_FORGOT_PASSWORD = HTTP_PROTOCOL + HTTP_HOST_IP + "/users/forgotPassword";
    public static final String HTTP_VALIDATE_OTP = HTTP_PROTOCOL + HTTP_HOST_IP + "/users/otp";
    public static final String HTTP_GET_COUNTRIES = HTTP_PROTOCOL + HTTP_HOST_IP + "/countries/getCountries";
    public static final String HTTP_UPDATE_EMAIL = HTTP_PROTOCOL + HTTP_HOST_IP + "/users/getProfile";
    public static final String HTTP_GET_CATEGORIES = HTTP_PROTOCOL + HTTP_HOST_IP + "/categories/getCategories";
    public static final String HTTP_POST_AD = HTTP_PROTOCOL + HTTP_HOST_IP + "/ads/postAd";
    public static final String HTTP_OTP_POST_ACTIVATION = HTTP_PROTOCOL + HTTP_HOST_IP + "/users/postactivation";
    public static final String HTTP_OTP_FORGOT_PASSWORD = HTTP_PROTOCOL + HTTP_HOST_IP + "/users/otp";
    private static final String HTTP_CHANGE_PASSWORD_RESET = HTTP_PROTOCOL + HTTP_HOST_IP + "/users/mresetPassword";
    private static final String HTTP_RESEND_OTP = HTTP_PROTOCOL + HTTP_HOST_IP + "/users/resendOTP";
    private static final String HTTP_GET_ADS_LIST = HTTP_PROTOCOL + HTTP_HOST_IP + "/ads/getAds";
    private static final String HTTP_FLAG_AD = HTTP_PROTOCOL + HTTP_HOST_IP + "/ads/flagAd";
    private static final String HTTP_GET_ADS_DESC = HTTP_PROTOCOL + HTTP_HOST_IP + "/ads/adDetails";
    private static final String HTTP_SEARCH_ADS = HTTP_PROTOCOL + HTTP_HOST_IP + "/ads/getAds";
    public static final String MULTIPART_URL = "http://api.spplitt.com/upload.php";
    public static String API_BASE = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
    private static final String HTTP_CITY_LIST = HTTP_PROTOCOL + HTTP_HOST_IP + "/countries/getCity";
    private static final String HTTP_OTHER_PROFILE = HTTP_PROTOCOL + HTTP_HOST_IP + "/users/getProfile";
    private static final String HTTP_MY_ADS = HTTP_PROTOCOL + HTTP_HOST_IP + "/ads/getMyAds";
    private static final String HTTP_CANCEL_MY_ADS = HTTP_PROTOCOL + HTTP_HOST_IP + "/ads/AdsActions";
    private static final String HTTP_UPDATE_GCM_ID = HTTP_PROTOCOL + HTTP_HOST_IP + "/users/getProfile";
    private static final String HTTP_CONNECT_AD = HTTP_PROTOCOL + HTTP_HOST_IP + "/ads/AdConnection";
    private static final String HTTP_SET_PREFERENCES = HTTP_PROTOCOL + HTTP_HOST_IP + "/notifications/saveNotification";
    private static final String HTTP_SEARCH_MY_ADS = HTTP_PROTOCOL + HTTP_HOST_IP + "/ads/getMyAds";
    private static final String HTTP_CHANGE_PASSWORD = HTTP_PROTOCOL + HTTP_HOST_IP + "/users/changePassword";
    private static final String HTTP_GET_NOTIFICATIONS = HTTP_PROTOCOL + HTTP_HOST_IP + "/notifications/getUserNotification";
    private static final String HTTP_ACCEPT_AD = HTTP_PROTOCOL + HTTP_HOST_IP + "/ads/AdConnection";
    private static final String HTTP_APPLIED_AD = HTTP_PROTOCOL + HTTP_HOST_IP + "/ads/getBuyerAppliedAds";
    private static final String HTTP_CONTACT_US = HTTP_PROTOCOL + HTTP_HOST_IP + "/users/ContactUs";
    private static final String HTTP_RATING = HTTP_PROTOCOL + HTTP_HOST_IP + "/ratings/saveRatings";
    private static final String HTTP_STATES = HTTP_PROTOCOL + HTTP_HOST_IP + "/countries/getStates";
    private static final String HTTP_RESET_PREF = HTTP_PROTOCOL + HTTP_HOST_IP + "/Notifications/resetNotification";
    private static final String HTTP_SAVE_LOCATION = HTTP_PROTOCOL + HTTP_HOST_IP + "/ads/savePreferredLocation";

    public WebServiceClient(Context context) {
        mContext = context;
    }

    public static void getLogin(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_LOGIN;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }


    public static void signUpUser(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_REGISTR;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void forgotPassword(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_FORGOT_PASSWORD;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void validateOtp(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_VALIDATE_OTP;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void getCountriesList(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_GET_COUNTRIES;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url + payload, "", method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;

    }

    public static void updateEmail(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_UPDATE_EMAIL;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void updateMobileNo(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_UPDATE_EMAIL;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void editProfileUser(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_UPDATE_EMAIL;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void getCategoriesList(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_GET_CATEGORIES;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void postAd(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_POST_AD;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void validateOtpPostActivation(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_OTP_POST_ACTIVATION;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void validateOtpForgotPassword(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_OTP_FORGOT_PASSWORD;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void changePasswordWhenForgot(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_CHANGE_PASSWORD_RESET;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void validateResendOtp(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_RESEND_OTP;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void getAdsList(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_GET_ADS_LIST;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void flagAnAd(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_FLAG_AD;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void getAdsDescription(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_GET_ADS_DESC;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void getSearchAdsList(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_SEARCH_ADS;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void getCitiesList(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_CITY_LIST;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void getOthersProfile(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_OTHER_PROFILE;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void getMyAdsList(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_MY_ADS;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void cancelAnAd(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_CANCEL_MY_ADS;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void editAd(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_CANCEL_MY_ADS;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void repostAd(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_CANCEL_MY_ADS;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;
    }

    public static void updateGcmId(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_UPDATE_GCM_ID;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;

    }

    public static void connectAnAd(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_CONNECT_AD;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;

    }

    public static void setMyPreferences(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_SET_PREFERENCES;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;

    }


    public static void getSearchMyAdsList(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_SEARCH_MY_ADS;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;

    }

    public static void changePassword(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_CHANGE_PASSWORD;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;

    }

    public static void getNotificationsList(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_GET_NOTIFICATIONS;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;

    }

    public static void acceptAnAd(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_ACCEPT_AD;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;

    }

    public static void getMyAppliedAdsList(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_APPLIED_AD;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;

    }

    public static void getSearchAppliedAdsList(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_APPLIED_AD;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;

    }

    public static void contactUs(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_CONTACT_US;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;

    }

    public static void addRating(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_RATING;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;

    }


    public static void addRating_MyAds(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_RATING;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;

    }

    public static void getStatesList(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_STATES;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;

    }

    public static void resetPref(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_RESET_PREF;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;

    }

    public static void saveLocationPayload(final Context context, String payload, boolean showprogress, int urlno, ResponseHandlerListener responseHandlerListenerLogin) {
        url = HTTP_SAVE_LOCATION;
        method = HttpMethod.HTTP_POST;
        checkNetworkState(url, payload, method, context, showprogress);
        urlNo = urlno;
        responseHandlerListener = responseHandlerListenerLogin;

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
            CallWebserviceTask ca = new CallWebserviceTask(url, _payload, method, context, showProgress);
            ca.execute();
        } else {
            // open dialog here
            new Utility().showNoInternetDialog((Activity) context);

        }
    }

    /**
     * General Async task to call Webservices
     */
    private static class CallWebserviceTask extends
            AsyncTask<Void, Void, Object[]> {
        private final String mUrl;
        private final String mPayload;
        private final HttpMethod mMethod;
        private Context mContext;
        private boolean mShowProgress;

        public CallWebserviceTask(String url, String _payload,
                                  HttpMethod method, final Context context, boolean showProgress) {
            Log.e(LOG_TAG, url);
            mContext = context;
            mUrl = url;
            mPayload = _payload;
            mMethod = method;
            mContext = context;
            mShowProgress = showProgress;
            Log.e("payload", mPayload);
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
            WebError error = null;
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
                    Log.e("=====" + responsePojo);
                    // output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
                } else {
                    Log.d(LOG_TAG, "Success code is " + successCode);
                    error = WebError.UNKNOWN;
                }
            } catch (Exception e) {
                error = WebError.UNKNOWN;
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
                    } else if (((ResponsePojo) result[0]).getStatus_code() == 600) {
                        // Show Logout Dialog when user has logged in from another device
                        WebNotificationManager.unRegisterResponseListener(responseHandlerListener);
                        progressDialog.dismiss();
                        new Utility().showSetLocationDialog(mContext, (ResponsePojo) result[0]);
                    } else {
                        WebNotificationManager.onResponseCallReturned(
                                (ResponsePojo) result[0], (WebError) result[1],
                                progressDialog, urlNo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    WebNotificationManager.onResponseCallReturned(
                            (ResponsePojo) result[0], (WebError) result[1],
                            progressDialog, urlNo);
                }
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}