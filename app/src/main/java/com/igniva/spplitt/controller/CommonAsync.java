package com.igniva.spplitt.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.igniva.spplitt.controller.AsyncResult;

import java.io.DataOutputStream;
import java.io.IOException;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;


public class CommonAsync extends AsyncTask<String, Integer, String> {
    String appURL;
    ProgressDialog progressDialog;

    Context mContext;
    HashMap<String, String> commonDataFetch;
    AsyncResult<String> callback;
    StringBuilder jsonResults;
    int urlNo;

    public CommonAsync(Context c, String appURL,
                       AsyncResult callBack,
                       HashMap<String, String> commonDataFetch, int urlNo) {

        try {
            this.mContext = c;
            this.appURL = appURL;
            this.callback = callBack;
            progressDialog = new ProgressDialog(mContext);
            this.commonDataFetch = commonDataFetch;
            this.urlNo = urlNo;
        } catch (Exception e) {
        }

    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection httpUrlConnection = null;
        jsonResults = new StringBuilder();
        try {
            StringBuilder urlBuilder = new StringBuilder();
            if(commonDataFetch!=null) {
                Iterator<String> paramIterator = commonDataFetch.keySet().iterator();
                while (paramIterator.hasNext()) {
                    String key = paramIterator.next();
                    String value = commonDataFetch.get(key);
                    urlBuilder.append(URLEncoder.encode(key, "UTF-8"));
                    urlBuilder.append("=").append(
                            URLEncoder.encode(value, "UTF-8"));
                    urlBuilder.append("&");
                }
            }
            URL url = new URL(appURL);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestMethod("POST");
            DataOutputStream wr = new DataOutputStream(
                    httpUrlConnection.getOutputStream());
            wr.writeBytes(urlBuilder.toString());
            wr.flush();
            wr.close();


            InputStreamReader in = new InputStreamReader(httpUrlConnection.getInputStream());
            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            Log.e("JSON RESULT", jsonResults + "");
        } catch (MalformedURLException e) {
            Log.e("Error Message", "Error processing API", e);
        } catch (IOException e) {
            Log.e("Error Message", "Error connecting to API", e);
        } catch (Exception e) {
            Log.e("Error Message", "Error processing API", e);
        } finally {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
        }
        return jsonResults.toString();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCancelable(false);
        } catch (Exception e) {
        }

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
        progressDialog.dismiss();

            callback.onTaskResponse(result, urlNo);
        } catch (Exception e) {

        }
    }
}
