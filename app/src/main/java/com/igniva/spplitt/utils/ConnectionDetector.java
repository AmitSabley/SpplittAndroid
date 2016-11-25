package com.igniva.spplitt.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.igniva.spplitt.R;


public class ConnectionDetector {

    private Context _context;
    boolean value;

    public ConnectionDetector(Context context) {
        this._context = context;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null) {
                Log.i("Connection Message1", "--" + info[0].getDetailedState() + "--");
                for (int i = 0; i < info.length; i++) {
                    Log.i("Connection Message", "--" + info[i].getDetailedState() + "");
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;

                    }
                }
            }
        }
        return false;
    }

//    public boolean checkInternetConnection(View view) {
//
//        Snackbar snackbar = Snackbar
//                .make(view, "No internet connection!", Snackbar.LENGTH_SHORT)
//                .setAction("RETRY", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        value = true;
//                        //validations();
//                    }
//                });
//
//        snackbar.setActionTextColor(Color.RED);
//        View sbView = snackbar.getView();
//        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextColor(Color.YELLOW);
//        snackbar.show();
//        return value;
//    }
//
//    public void checkInternetConnectionDialog(Activity activity, String message) {
//
//        final Dialog commentDialog = new Dialog(activity);
//        commentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        commentDialog.setContentView(R.layout.dialog_internet_conn);
//        TextView itemName = (TextView) commentDialog.findViewById(R.id.message);
//        itemName.setText(message + " !!");
//        TextView dismissButton = (TextView) commentDialog
//                .findViewById(R.id.dismiss);
////        FontsView.changeFontStyle(activity, itemName);
////        FontsView.changeFontStyle(activity, dismissButton);
//        dismissButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                commentDialog.dismiss();
//            }
//        });
//        commentDialog.show();
//        commentDialog.setCancelable(false);
//
//    }
}
