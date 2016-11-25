package com.igniva.spplitt.controller;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;

import com.igniva.spplitt.controller.ResponseHandlerListener;
import com.igniva.spplitt.controller.WebServiceClient;
import com.igniva.spplitt.model.ResponsePojo;


public class WebNotificationManager {

    private static final List<ResponseHandlerListener> responseHandlerListeners = new ArrayList<ResponseHandlerListener>();

    public static void onResponseCallReturned(
            ResponsePojo result, WebServiceClient.WebError error,
            ProgressDialog mProgressDialog, int urlNo) {


        for (int i = responseHandlerListeners.size() - 1; i >= 0; i--) {
            ResponseHandlerListener listener = responseHandlerListeners
                    .get(i);
            listener.onComplete(result, error, mProgressDialog,urlNo);
        }



    }

    // register listener for handling response
    public static void registerResponseListener(ResponseHandlerListener listener) {
        responseHandlerListeners.add(listener);

    }

    // unregister listener for handling response
    public static void unRegisterResponseListener(ResponseHandlerListener listener) {
        responseHandlerListeners.remove(listener);

    }


}
