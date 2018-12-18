package com.igniva.spplitt.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;

import com.igniva.spplitt.R;
import com.igniva.spplitt.utils.Log;

import org.apache.http.entity.mime.MultipartEntity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;

/**
 * Created by igniva-php-08 on 25/5/16.
 */
public class WebServiceClientUploadImage extends
        AsyncTask<String, Integer, String> {
    String mUrl;
    MultipartEntity mReqEntity;
    Context mContext;
    ProgressDialog progressDialog;
    StringBuilder builder = new StringBuilder();
    AsyncResult mCallBack;
    int urlNo;

    public WebServiceClientUploadImage(Context mContext, AsyncResult callBack, String urlString, MultipartEntity reqEntity, int urlNo) {
        this.mUrl = urlString;
        this.mReqEntity = reqEntity;
        this.mContext = mContext;
        this.mCallBack = callBack;
        this.urlNo = urlNo;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            progressDialog = ProgressDialog.show(mContext, "", mContext
                            .getResources().getString(R.string.please_wait), true,
                    false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected String doInBackground(String... voids) {
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            URL url = new URL(mUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Content-length", mReqEntity.getContentLength() + "");
            conn.addRequestProperty(mReqEntity.getContentType().getName(), mReqEntity.getContentType().getValue());
            OutputStream os = conn.getOutputStream();
            mReqEntity.writeTo(conn.getOutputStream());
            os.close();
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = null;
                builder = new StringBuilder();
                try {
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.toString();
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            mCallBack.onTaskResponse(result, urlNo);
            System.out.println(result);
            progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
