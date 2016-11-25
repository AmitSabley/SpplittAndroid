package com.igniva.spplitt.utils;

public class Log {

	public static boolean isDebug = true;
	private static final String TAG = "APP_FRAMEWORK";

	// print debug message
	public static void d(String TAG, String message) {
		if (isDebug)
			android.util.Log.d(TAG, ((message != null) ? message : ""));
	}

	// print error message
	public static void e(String TAG, String message) {
		if (isDebug)
			android.util.Log.e(TAG, ((message != null) ? message : ""));
	}

	// verbose message
	public static void v(String TAG, String message) {
		if (isDebug)
			android.util.Log.e(TAG, ((message != null) ? message : ""));
	}

	public static void d(Exception e) {

		e.printStackTrace();

	}

	public static void d(String s) {
		d(TAG, s);
	}

	public static void d(String tag, Exception e) {
		d(e);
	}

	// TODO: log everything coming in here
	public static void e(Exception e) {

		e.printStackTrace();

	}

	// TODO: log everything coming in here
	public static void e(String s) {
		e(TAG, s);
	}

	// TODO: log everything coming in here
	public static void e(String tag, Exception e) {
		e(e);
	}

	// TODO: log everything coming in here
	public static void w(String tag, String s) {

		android.util.Log.w(tag, s);

	}

	// TODO: log everything coming in here
	public static void w(Exception e) {

		e.printStackTrace();

	}

	// TODO: log everything coming in here
	public static void w(String s) {
		w(TAG, s);
	}

	// TODO: log everything coming in here
	public static void w(String tag, Exception e) {
		w(e);
	}

	// TODO: log everything coming in here
	public static void i(String tag, String s) {
		android.util.Log.i(tag, s);
	}
}
