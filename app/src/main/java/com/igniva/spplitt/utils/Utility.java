package com.igniva.spplitt.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.igniva.spplitt.R;
import com.igniva.spplitt.model.ErrorPojo;
import com.igniva.spplitt.model.ResponsePojo;
import com.igniva.spplitt.ui.activties.LoginActivity;
import com.igniva.spplitt.ui.activties.LoginOptionActivity;
import com.igniva.spplitt.ui.activties.MainActivity;
import com.igniva.spplitt.ui.activties.UpdateEmailActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.json.JSONObject;


public class Utility {


	/**
	 * @param context
	 * @return Returns true if there is network connectivity
	 */
	public static boolean isInternetConnection(Context context) {
		boolean HaveConnectedWifi = false;
		boolean HaveConnectedMobile = false;

		try {

			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni != null) {
				if (ni.getType() == ConnectivityManager.TYPE_WIFI)
					if (ni.isConnectedOrConnecting())
						HaveConnectedWifi = true;
				if (ni.getType() == ConnectivityManager.TYPE_MOBILE)
					if (ni.isConnectedOrConnecting())
						HaveConnectedMobile = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return HaveConnectedWifi || HaveConnectedMobile;
	}

	/**
	 * Display Toast Message
	 **/
	public static void showToastMessageShort(Context context, String message) {
		Toast.makeText(context, message,
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * Display Toast Message
	 **/
	public static void showToastMessageLong(Context context, String message) {
		try {
			Toast.makeText(context, message,
					Toast.LENGTH_LONG).show();
		}catch (Exception e){e.printStackTrace();}
	}

	/**
	 * Display Toast Message
	 **/
	public static void showToastMessageLong(Activity context, String message) {
		try {
		Toast.makeText(context, message,
				Toast.LENGTH_LONG).show();
		}catch (Exception e){e.printStackTrace();}
	}
	public static String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = {MediaStore.Images.Media.DATA};
			cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * Get IP address from first non-localhost interface
	 * 
	 * @param
	 *
	 * @return address or empty string
	 */
//	public static String getIPAddress(boolean useIPv4) {
//		try {
//			List<NetworkInterface> interfaces = Collections
//					.list(NetworkInterface.getNetworkInterfaces());
//			for (NetworkInterface intf : interfaces) {
//				List<InetAddress> addrs = Collections.list(intf
//						.getInetAddresses());
//				for (InetAddress addr : addrs) {
//					if (!addr.isLoopbackAddress()) {
//						String sAddr = addr.getHostAddress();
//						// boolean isIPv4 =
//						// InetAddressUtils.isIPv4Address(sAddr);
//						boolean isIPv4 = sAddr.indexOf(':') < 0;
//
//						if (useIPv4) {
//							if (isIPv4)
//								return sAddr;
//						} else {
//							if (!isIPv4) {
//								int delim = sAddr.indexOf('%'); // drop ip6 zone
//																// suffix
//								return delim < 0 ? sAddr.toUpperCase() : sAddr
//										.substring(0, delim).toUpperCase();
//							}
//						}
//					}
//				}
//			}
//		} catch (Exception ex) {
//		} // for now eat exceptions
//		return "";
//	}

	public void showNoInternetDialog(final Context mContext) {
		try {

			AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
					R.style.CustomPopUpTheme);
			builder.setTitle(mContext.getResources().getString(R.string.no_internet_title));
			builder.setMessage(mContext.getResources().getString(R.string.no_internet));
			builder.setPositiveButton("OK", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					dialog.dismiss();
					//((Activity) mContext).finish();

				}
			});

			builder.show();
		} catch (Exception e) {
			showToastMessageLong(mContext,
					mContext.getResources().getString(R.string.no_internet));
		}
	}

//    public void showInvalidSessionDialog(final Context mContext) {
//        try {
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
//                    R.style.CustomPopUpTheme);
//            builder.setTitle(mContext.getResources().getString(R.string.invalid_session));
//			builder.setCancelable(false);
//            builder.setMessage(mContext.getResources().getString(R.string.logout_device));
//            builder.setPositiveButton("OK", new OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    dialog.dismiss();
//                    // TODO redirect to login screen
//                    ((Activity) mContext).finish();
//
//                }
//            });
//
//            builder.show();
//        } catch (Exception e) {
//			e.printStackTrace();
//            showToastMessageLong(mContext,
//                    mContext.getResources().getString(R.string.invalid_session));
//        }
//    }

	public void showErrorDialog(final Context mContext, ResponsePojo result) {
		try {

			AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
					R.style.CustomPopUpTheme);
//			builder.setTitle(result.getDescription());
			ErrorPojo errorPojo=result.getError();
			builder.setMessage(errorPojo.getError_msg()+"");
//			builder.setMessage(result.getDescription()+"");
			builder.setPositiveButton("OK", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			builder.show();
		} catch (Exception e) {
			e.printStackTrace();
			showToastMessageLong(mContext,
					mContext.getResources().getString(R.string.invalid_session));
		}
	}

	public void showCreateAccountDialog(final Activity mContext, ResponsePojo result) {
		try {

			AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
					R.style.CustomPopUpTheme);
			builder.setMessage(result.getDescription());

			builder.setPositiveButton("OK", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					mContext.startActivity(new Intent(mContext, LoginActivity.class));
					mContext.finish();
					dialog.dismiss();
				}
			});

			builder.show();
		} catch (Exception e) {
			e.printStackTrace();
			showToastMessageLong(mContext,
					mContext.getResources().getString(R.string.invalid_session));
		}
	}

	public void showSuccessDialog(final Context mContext, ResponsePojo result) {
		try {

			AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
					R.style.CustomPopUpTheme);
			builder.setMessage(result.getDescription());

			builder.setPositiveButton("OK", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			builder.show();
		} catch (Exception e) {
			e.printStackTrace();
			showToastMessageLong(mContext,
					mContext.getResources().getString(R.string.invalid_session));
		}
	}

	public static void clearSharedPreferneces(Context mContext) {
		String imei_no=PreferenceHandler.readString(mContext,PreferenceHandler.IMEI_NO,"");
		String gcm_id=PreferenceHandler.readString(mContext,PreferenceHandler.GCM_REG_ID,"");
		int show_edit_profile=PreferenceHandler.readInteger(mContext,PreferenceHandler.SHOW_EDIT_PROFILE,0);
		PreferenceHandler.getEditor(mContext).clear().commit();
		PreferenceHandler.writeString(mContext,PreferenceHandler.IMEI_NO,imei_no);
		PreferenceHandler.writeString(mContext,PreferenceHandler.GCM_REG_ID,gcm_id);
		PreferenceHandler.writeInteger(mContext,PreferenceHandler.SHOW_EDIT_PROFILE,show_edit_profile);
//		sharedpreferences.edit().putInt(Constants.OTP_SCREEN_NO,0);
//		editor.commit();
	}

	public void showCreateAccountDialogUpdate(final Activity mContext, ResponsePojo result) {
		try {

			AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
					R.style.CustomPopUpTheme);
			builder.setMessage(result.getDescription());
			builder.setCancelable(false);
			builder.setPositiveButton("OK", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					mContext.startActivity(new Intent(mContext, LoginActivity.class));
					if(MainActivity.main!=null) {
						MainActivity.main.finish();
					}
//					Intent intent = new Intent(mContext, LoginActivity.class);
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					mContext.startActivity(intent);
					mContext.finish();
					dialog.dismiss();
				}
			});

			builder.show();
		} catch (Exception e) {
			e.printStackTrace();
			showToastMessageLong(mContext,
					mContext.getResources().getString(R.string.invalid_session));
		}
	}


	public static void removeTemperoryValueFromPreferences(Context mContext) {
		PreferenceHandler.getEditor(mContext).remove(PreferenceHandler.TEMP_MOBILE_NO).commit();
		PreferenceHandler.getEditor(mContext).remove(PreferenceHandler.TEMP_USER_ID).commit();
		PreferenceHandler.getEditor(mContext).remove(PreferenceHandler.TEMP_OTP).commit();
		PreferenceHandler.getEditor(mContext).remove(PreferenceHandler.OTP_SCREEN_NO).commit();
//		editor.commit();
	}


	public void showErrorDialogRequestFailed(final Context mContext) {//when unable to hit web service
		try {

			AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
					R.style.CustomPopUpTheme);
			builder.setTitle(mContext.getResources().getString(R.string.unknown_error));
			builder.setMessage(mContext.getResources().getString(R.string.unknown_error_message));
			builder.setPositiveButton("OK", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			builder.show();
		} catch (Exception e) {
			e.printStackTrace();
			showToastMessageLong(mContext,
					mContext.getResources().getString(R.string.unknown_error));
		}
	}
	public void showInvalidSessionDialogLogout(final Context mContext, ResponsePojo result) {
		try {

			AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
					R.style.CustomPopUpTheme);
//			builder.setTitle(mContext.getResources().getString(R.string.invalid_session));
			builder.setCancelable(false);
			ErrorPojo errorPojo=result.getError();
			builder.setMessage(errorPojo.getError_msg()+"");
//			builder.setMessage(mContext.getResources().getString(R.string.logout_device));
			builder.setPositiveButton("OK", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Utility.clearSharedPreferneces(mContext);
					mContext.startActivity(new Intent(mContext, LoginOptionActivity.class));
					if(MainActivity.main!=null) {
						MainActivity.main.finish();
					}
					dialog.dismiss();
					((Activity) mContext).finish();

				}
			});

			builder.show();
		} catch (Exception e) {
			e.printStackTrace();
			showToastMessageLong(mContext,
					mContext.getResources().getString(R.string.invalid_session));
		}
	}
	public static void hideKeyboard(Context applicationContext, View view) {
		InputMethodManager imm = (InputMethodManager) applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
