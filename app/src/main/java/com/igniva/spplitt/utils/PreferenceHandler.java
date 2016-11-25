package com.igniva.spplitt.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Common PrefrenceConnector class for storing preference values.
 * 
 */
public class PreferenceHandler {

	public static final String PREF_NAME = "APPFRAMEWORK_PREFERENCES";
	public static final int MODE = Context.MODE_PRIVATE;


	//	public static final String PREF_KEY_LOGIN = "PREF_KEY_LOGIN";
//	public static final String PREF_KEY_USER_ID = "PREF_KEY_USER_ID";
//	public static final String PREF_KEY_USER_NAME = "PREF_KEY_USER_NAME";
//	public static final String PREF_KEY_USER_EMAIL = "PREF_KEY_USER_EMAIL";
//	public static final String PREF_KEY_USER_MOBILE = "PREF_KEY_USER_MOBILE";
//
//	public static final String SPPLITT_PREFERENCES="SpplittPreferences";
	public static  String IMEI_NO="imei_no";
	public static  String GCM_REG_ID="gcm_regId";
	public static  String OTP_SCREEN_NO="otp_screen";// if 0 then otp screen will not comes ;  if 1 then after registration otp will comes  ;if 2 then after forgot password otp will comes
	public static  String SHOW_EDIT_PROFILE="show_edit_profile";// if 0 then show edit profile first after login and if 1 then dont open
	public static  String USER_ID="user_id";
	public static  String USER_NAME="user_name";
	public static  String EMAIL="email";
	public static  String MOBILE_NO="mobile_no";
	public static  String GENDER="gender";
	public static  String PICTURE="picture";
	public static  String COUNTRY="country";
	public static  String COUNTRY_NAME="country_name";
	public static  String CITY="city";
	public static  String AGE="age";
	public static  String CURRENCY_CODE="currency_code";
	public static  String IS_AGE_PUBLIC ="is_age_public" ;
	public static  String AUTH_TOKEN = "auth_token";
	public static  String CURRENCY_ID="currency_id";
	public static  String CITY_NAME = "city_name";
	public static String STATE="state";
	public static String STATE_NAME="state_name";
	//create account
	public static String TEMP_MOBILE_NO="temp_mobile_no";
	public static String TEMP_USER_ID="temp_user_id";
	public static String TEMP_OTP="temp_otp";
	public static String AD_CITY_NAME = "ad_city_name";
	public static String AD_STATE_NAME = "ad_state_name";
	public static String AD_COUNTRY_NAME = "ad_country_name";

	public static void writeBoolean(Context context, String key, boolean value) {
		getEditor(context).putBoolean(key, value).commit();
	}

	public static boolean readBoolean(Context context, String key,
			boolean defValue) {
		return getPreferences(context).getBoolean(key, defValue);
	}

	public static void writeInteger(Context context, String key, int value) {
		getEditor(context).putInt(key, value).commit();
	}

	public static int readInteger(Context context, String key, int defValue) {
		return getPreferences(context).getInt(key, defValue);
	}

	public static void writeString(Context context, String key, String value) {
		getEditor(context).putString(key, value).commit();
	}

	public static String readString(Context context, String key, String defValue) {
		return getPreferences(context).getString(key, defValue);
	}

	public static void writeFloat(Context context, String key, float value) {
		getEditor(context).putFloat(key, value).commit();
	}

	public static float readFloat(Context context, String key, float defValue) {
		return getPreferences(context).getFloat(key, defValue);
	}

	public static void writeLong(Context context, String key, long value) {
		getEditor(context).putLong(key, value).commit();
	}

	public static long readLong(Context context, String key, long defValue) {
		return getPreferences(context).getLong(key, defValue);
	}

	public static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(PREF_NAME, MODE);
	}

	public static Editor getEditor(Context context) {
		return getPreferences(context).edit();
	}



}
