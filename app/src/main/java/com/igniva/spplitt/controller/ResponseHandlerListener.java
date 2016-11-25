package com.igniva.spplitt.controller;

import java.util.ArrayList;

import android.app.ProgressDialog;
import com.igniva.spplitt.controller.WebServiceClient.WebError;
import com.igniva.spplitt.model.ResponsePojo;


public interface ResponseHandlerListener {

	/**
	 * Called when the method completes
	 * 
	 * @param result
	 *            The result obtained from the web service in the form of
	 *            ResponseWrapper class - ResponsePojo
	 * @param error
	 *            null if no error, or of the ErrorPojo enum values if error
	 *            occurred
	 * @param mProgressDialog
	 */
	public void onComplete(ResponsePojo result, WebError error,
						   ProgressDialog mProgressDialog,int mUrlNo);

}
