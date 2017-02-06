package com.igniva.spplitt.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.igniva.spplitt.R;
import com.igniva.spplitt.ui.views.MultiSpinner;
import com.igniva.spplitt.ui.views.RoundedImageView;

/**
 * Common Utility to Validate any type of user input
 */
public class Validations {

    /**
     * Validating form
     *
     * @param applicationContext
     * @param mSvMmain
     * @param myBitmap
     * @param mRivUserImage
     * @param mEtUsername
     * @param mEtPassword
     * @param mEtEmail
     * @param mCbTerms
     */

    public boolean isValidate(Context applicationContext, ScrollView mSvMmain, Bitmap myBitmap, RoundedImageView mRivUserImage, EditText mEtUsername, EditText mEtPassword, EditText mEtEmail, String mCountryId, String mStateId, String mCityId, CheckBox mCbTerms) {
        if (!validateUsername(applicationContext, mEtUsername)) {
            return false;
        }
        if (!validatePassword(applicationContext, mEtPassword)) {
            return false;
        }
        if (!validateEmailOnly(applicationContext, mEtEmail)) {
            return false;
        }
        if (!validateCountryId(applicationContext, mCountryId)) {
            return false;
        }
        if (!validateStateId(applicationContext, mStateId)) {
            return false;
        }
        if (!validateCityId(applicationContext, mCityId)) {
            return false;
        }
        if (!validateImage(applicationContext, mRivUserImage, myBitmap, mSvMmain)) {
            return false;
        }
        if (!validateTerms(applicationContext, mCbTerms)) {
            return false;
        }
        return true;
    }

    /**
     * Validating form for login
     *
     * @param applicationContext
     * @param mEtEmail
     * @param mEtPassword
     */
    public boolean isValidateLogin(Context applicationContext, EditText mEtEmail, EditText mEtPassword) {
        if (!validateEmailOnly(applicationContext, mEtEmail)) {
            return false;
        }
        if (!validatePassword1(applicationContext, mEtPassword)) {
            return false;
        }
        return true;
    }

    /**
     * Validating form for forgot password
     *
     * @param applicationContext
     * @param mEtEmail
     */
    public boolean isValidateForgotPassword(Context applicationContext, EditText mEtEmail) {
        if (!validateEmailOnly(applicationContext, mEtEmail)) {
            return false;
        }
        return true;
    }

    /**
     * Validating form for change password
     *
     * @param applicationContext
     * @param mEtOldPassword
     * @param mEtNewPassword
     */
    public boolean isValidateChangePassword(Context applicationContext, EditText mEtOldPassword, EditText mEtNewPassword) {
        if (!validateOldPassword(applicationContext, mEtOldPassword)) {
            return false;
        }
        if (!validatePassword(applicationContext, mEtNewPassword)) {
            return false;
        }
        return true;
    }

    /**
     * Validating form for reset password
     *
     * @param applicationContext
     * @param mEtNewPassword
     */
    public boolean isValidateResetPassword(Context applicationContext, EditText mEtNewPassword) {
        if (!validateNewPassword(applicationContext, mEtNewPassword)) {
            return false;
        }
        return true;
    }

    /**
     * Validating form for enter otp
     *
     * @param applicationContext
     * @param mEtOtp
     */
    public boolean isValidateOtp(Context applicationContext, EditText mEtOtp) {
        if (!validateOtp(applicationContext, mEtOtp)) {
            return false;
        }
        return true;
    }

    /**
     * Validating form for validate email id
     *
     * @param applicationContext
     * @param mEtNewEmail
     * @param mEtOldPassword
     */
    public boolean isValidateUpdateEmail(Context applicationContext, EditText mEtNewEmail, EditText mEtOldPassword) {
        if (!validateEmailOnly(applicationContext, mEtNewEmail)) {
            return false;
        }
        if (!validatePassword1(applicationContext, mEtOldPassword)) {
            return false;
        }
        return true;
    }

    /**
     * Validating form for validate mobile no
     *
     * @param applicationContext
     * @param mEtNewMobileNo
     * @param mEtOldPassword
     */
    public boolean isValidateUpdateMobileNo(Context applicationContext, EditText mEtNewMobileNo, EditText mEtOldPassword) {

        if (!validateMobileNo(applicationContext, mEtNewMobileNo)) {
            return false;
        }
        if (!validatePassword1(applicationContext, mEtOldPassword)) {
            return false;
        }
        return true;
    }

    /**
     * Validating form for validate edit profile
     *
     * @param applicationContext
     * @param mSvmain
     * @param myBitmap
     */
    public boolean isValidateEditProfile(Context applicationContext, ScrollView mSvmain, Bitmap myBitmap, EditText mEtUsername, EditText mEtAge, String mCountryId, String mStateId, String mCityId) {
//        if (!validateImage(applicationContext, mRivUserImage, myBitmap, mSvmain)) {
//            return false;
//        }
        if (!validateUsername(applicationContext, mEtUsername)) {
            return false;
        }
        if (!validateAge(applicationContext, mEtAge)) {
            return false;
        }
        if (!validateCountryId(applicationContext, mCountryId)) {
            return false;
        }
        if (!validateStateId(applicationContext, mStateId)) {
            return false;
        }
        if (!validateCityId(applicationContext, mCityId)) {
            return false;
        }
        return true;
    }

    /**
     * Validating form for validate post ad
     *
     * @param applicationContext
     * @param mSpCategories
     * @param mEtAdTitle
     * @param mEtAdDesc
     * @param mEtSelectDate
     * @param mEtSelectTime
     * @param mCountryId
     * @param mEtSplittCost
     */
    public boolean isValidatePostAd(Context applicationContext, Spinner mSpCategories, EditText mEtAdTitle, EditText mEtAdDesc, EditText mEtSelectDate, EditText mEtSelectTime, String mCountryId, String mStateId, String mCityId, EditText mEtSplittCost) {
        if (!validateCategory(applicationContext, mSpCategories)) {
            return false;
        }
        if (!validateAdTitle(applicationContext, mEtAdTitle)) {
            return false;
        }
        if (!validateDesc(applicationContext, mEtAdDesc)) {
            return false;
        }
        if (!validateDate(applicationContext, mEtSelectDate)) {
            return false;
        }
        if (!validateTime(applicationContext, mEtSelectTime)) {
            return false;
        }
        if (!validateCountryId(applicationContext, mCountryId)) {
            return false;
        }
        if (!validateStateId(applicationContext, mStateId)) {
            return false;
        }
        if (!validateCityId(applicationContext, mCityId)) {
            return false;
        }
        if (!validateCost(applicationContext, mEtSplittCost)) {
            return false;
        }

        return true;
    }

    /**
     * Validating form for validate post ad
     *
     * @param applicationContext
     * @param mEtDesc
     */
    public boolean isValidateContactUs(Context applicationContext, EditText mEtDesc) {
        if (!validateContactDesc(applicationContext, mEtDesc)) {
            return false;
        }
        return true;
    }


    public boolean isValidateSetPreferences(FragmentActivity applicationContext, MultiSpinner mMspCategories, boolean isItemSelected, String mCountryId, String mStateId, String mCityId, boolean isDateGreater, EditText mBtnDateRange) {
        if (!validateMultiCategory(applicationContext, mMspCategories, isItemSelected)) {
            return false;
        }
        if (!validateCountryId(applicationContext, mCountryId)) {
            return false;
        }
        if (!validateStateId(applicationContext, mStateId)) {
            return false;
        }
        if (!validateCityId(applicationContext, mCityId)) {
            return false;
        }
        if (!validateDateGreater(applicationContext, isDateGreater, mBtnDateRange)) {
            return false;
        }
        if (!validateDate(applicationContext, mBtnDateRange)) {
            return false;
        }


        return true;
    }

    public boolean isValidateReviews(Context applicationContext, RatingBar mRatingBar, EditText mEtReviews) {
        if (!validateRating(applicationContext, mRatingBar)) {
            return false;
        }
        if (!validateReviews(applicationContext, mEtReviews)) {
            return false;
        }
        return true;
    }

    public boolean isValidateTextDialog(Context applicationContext, EditText mEtReportAbuseMsg) {

        if (!validateText(applicationContext, mEtReportAbuseMsg)) {

            return false;
        }
        return true;
    }


    private boolean validateRating(Context applicationContext, RatingBar mRatingBar) {
        if (mRatingBar.getRating() == 0) {
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_rating));
            requestFocus(applicationContext, mRatingBar);
            return false;
        }
        return true;
    }


    private boolean validateMultiCategory(FragmentActivity applicationContext, MultiSpinner mMspCategories, boolean isItemSelected) {
        if (!isItemSelected) {
            TextView errorText = (TextView) mMspCategories.getSelectedView();
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_category));
            requestFocus(applicationContext, mMspCategories);
            return false;
        }
        return true;
    }

    private boolean validateAge(Context applicationContext, EditText mEtAge) {
        if (mEtAge.getText().toString().trim().isEmpty()) {
//            mEtAge.setError(applicationContext.getString(R.string.err_msg_age));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_age));
            requestFocus(applicationContext, mEtAge);
            return false;
        }
        return true;
    }

    private boolean validateOtp(Context applicationContext, EditText mEtOtp) {
        if (mEtOtp.getText().toString().trim().isEmpty()) {
//            mEtOtp.setError(applicationContext.getString(R.string.err_msg_otp));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_otp));
            requestFocus(applicationContext, mEtOtp);
            return false;
        }
        return true;
    }

    private boolean validateImage(Context applicationContext, RoundedImageView mRivUserImage, Bitmap myBitmap, ScrollView mSvMmain) {
        if (myBitmap == null) {
            mRivUserImage.setBorderColor(Color.RED);
            mSvMmain.fullScroll(ScrollView.FOCUS_UP);
            Utility.showToastMessageShort(applicationContext, applicationContext.getResources().getString(R.string.err_img));
            return false;
        }
        return true;
    }

    private boolean validateCity(Context applicationContext, AutoCompleteTextView mActvCity) {
        if (mActvCity.getText().toString().trim().isEmpty()) {
//            mActvCity.setError(applicationContext.getString(R.string.err_msg_city));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_city));
            requestFocus(applicationContext, mActvCity);
            return false;
        }
        return true;
    }

    private boolean validateCity1(Context applicationContext, Spinner mActvCity) {
        if (mActvCity.getSelectedItem() != null) {
            if (mActvCity.getSelectedItem().toString().trim().isEmpty()) {
//            mActvCity.setError(applicationContext.getString(R.string.err_msg_city));
                Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_city));
                requestFocus(applicationContext, mActvCity);
                return false;
            }
        } else {

//            mActvCity.setError(applicationContext.getString(R.string.err_msg_city));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_city));
            requestFocus(applicationContext, mActvCity);
            return false;

        }
        return true;
    }

    private boolean validateCountry(Context applicationContext, Spinner mActvCountry) {
        if (mActvCountry.getSelectedItem().toString().trim().isEmpty()) {
            TextView errorText = (TextView) mActvCountry.getSelectedView();
//            errorText.setError(applicationContext.getString(R.string.err_msg_country));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_country));
            requestFocus(applicationContext, mActvCountry);
            return false;
        }
        return true;
    }

    private boolean validateUsername(Context applicationContext, EditText mEtUsername) {
        if (mEtUsername.getText().toString().trim().isEmpty()) {
//            mEtUsername.setError(applicationContext.getString(R.string.err_msg_username));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_username));
            requestFocus(applicationContext, mEtUsername);
            return false;
        }
        return true;
    }

    private boolean validateTerms(Context applicationContext, CheckBox mCbTerms) {
        if (!mCbTerms.isChecked()) {
//            mEtUsername.setError(applicationContext.getString(R.string.err_msg_username));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_terms));
            requestFocus(applicationContext, mCbTerms);
            return false;
        }
        return true;
    }

    private boolean validateEmail(Context applicationContext, View view) {
        String email = ((EditText) view).getText().toString().trim();
        if (!email.isEmpty()) {
            if (email.contains("[a-zA-Z]+") || email.contains("@")) {
                if (email.isEmpty() || !isValidEmail(email)) {
//                    ((EditText) view).setError(applicationContext.getString(R.string.err_msg_email));
                    Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_email));
                    requestFocus(applicationContext, ((EditText) view));
                    return false;
                }
            } else if (email.matches("^[0-9]*$")) {
                return validateMobileNo(applicationContext, view);
            } else {
//                ((EditText) view).setError(applicationContext.getString(R.string.err_valid_data));
                Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_valid_data));
                requestFocus(applicationContext, ((EditText) view));
                return false;
            }
        } else {
//            ((EditText) view).setError(applicationContext.getString(R.string.err_msg_email_mobileno));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_email_mobileno));
            requestFocus(applicationContext, ((EditText) view));
            return false;
        }
        return true;
    }

    private boolean validateEmailOnly(Context applicationContext, View view) {
        String email = ((EditText) view).getText().toString().trim();

        if (email.contains("[a-zA-Z]+") || email.contains("@")) {
            if (email.isEmpty() || !isValidEmail(email)) {
//                    ((EditText) view).setError(applicationContext.getString(R.string.err_msg_email));
                Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_email));
                requestFocus(applicationContext, ((EditText) view));
                return false;
            }
        } else {
//                ((EditText) view).setError(applicationContext.getString(R.string.err_valid_data));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_email));
            requestFocus(applicationContext, ((EditText) view));
            return false;
        }

        return true;
    }

    private boolean validatePassword(Context applicationContext, View view) {
        if (((EditText) view).getText().toString().trim().isEmpty()) {
//            ((EditText) view).setError(applicationContext.getString(R.string.err_msg_password));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_password));
            requestFocus(applicationContext, ((EditText) view));
            return false;
        }
        if (!isLegalPassword(((EditText) view).getText().toString().trim())) {
//            ((EditText) view).setError(applicationContext.getString(R.string.err_msg_password_not_alphanumeric));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_password_not_alphanumeric));
            requestFocus(applicationContext, ((EditText) view));
            return false;
        }
        return true;
    }

    public static boolean isLegalPassword(String pass) {
        if (!pass.matches(".*[a-z].*")) return false;
        if (!pass.matches(".*\\d.*")) return false;
        return true;
    }

    private boolean validatePassword1(Context applicationContext, View view) {
        if (((EditText) view).getText().toString().trim().isEmpty()) {
//            ((EditText) view).setError(applicationContext.getString(R.string.err_msg_password));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_password));
            requestFocus(applicationContext, ((EditText) view));
            return false;
        }
        return true;
    }

    private boolean validateMobileNo(Context applicationContext, View view) {
        if (((EditText) view).getText().toString().trim().isEmpty() || !isValidMobile(((EditText) view).getText().toString().trim())) {
//            ((EditText) view).setError(applicationContext.getString(R.string.err_msg_mobileno));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_mobileno));
            requestFocus(applicationContext, ((EditText) view));
            return false;
        } else if (((EditText) view).getText().toString().trim().length() < 10) {
//            ((EditText) view).setError(applicationContext.getString(R.string.err_msg_mobileno_size));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_mobileno_size));
            requestFocus(applicationContext, ((EditText) view));
            return false;
        } else if (((EditText) view).getText().toString().trim().length() > 12) {
//            ((EditText) view).setError(applicationContext.getString(R.string.err_msg_mobileno_size));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_mobileno_size));
            requestFocus(applicationContext, ((EditText) view));
            return false;
        }

        return true;
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(Context applicationContext, View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private boolean validateNewPassword(Context applicationContext, EditText mEtNewPassword) {
        if (mEtNewPassword.getText().toString().trim().isEmpty()) {
//            mEtNewPassword.setError(applicationContext.getString(R.string.err_msg_new_password));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_new_password));
            requestFocus(applicationContext, mEtNewPassword);
            return false;
        }
        return true;
    }

    private boolean validateOldPassword(Context applicationContext, EditText mEtOldPassword) {
        if (mEtOldPassword.getText().toString().trim().isEmpty()) {
//            mEtOldPassword.setError(applicationContext.getString(R.string.err_msg_old_password));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_old_password));
            requestFocus(applicationContext, mEtOldPassword);
            return false;
        }
        return true;
    }

    private boolean validateCategory(Context applicationContext, Spinner mSpCategories) {
        if (mSpCategories.getSelectedItem() != null) {
            if (mSpCategories.getSelectedItem().toString().trim().isEmpty()) {
                TextView errorText = (TextView) mSpCategories.getSelectedView();
//            errorText.setError(applicationContext.getString(R.string.err_msg_category));
                Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_category));
                requestFocus(applicationContext, mSpCategories);
                return false;
            }
        } else {
            TextView errorText = (TextView) mSpCategories.getSelectedView();
//            errorText.setError(applicationContext.getString(R.string.err_msg_category));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_category));
            requestFocus(applicationContext, mSpCategories);
        }
        return true;
    }

    private boolean validateAdTitle(Context applicationContext, EditText mEtAdTitle) {
        if (mEtAdTitle.getText().toString().trim().isEmpty()) {
//            mEtAdTitle.setError(applicationContext.getString(R.string.err_msg_adTitle));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_adTitle));
            requestFocus(applicationContext, mEtAdTitle);
            return false;
        }
        return true;
    }

    private boolean validateDesc(Context applicationContext, EditText mEtAdDesc) {
        if (mEtAdDesc.getText().toString().trim().isEmpty()) {
//            mEtAdDesc.setError(applicationContext.getString(R.string.err_msg_adDesc));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_adDesc));
            requestFocus(applicationContext, mEtAdDesc);
            return false;
        }
        return true;
    }

    private boolean validateContactDesc(Context applicationContext, EditText mEtDesc) {
        if (mEtDesc.getText().toString().trim().isEmpty()) {
//            mEtAdDesc.setError(applicationContext.getString(R.string.err_msg_adDesc));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_contactUs));
            requestFocus(applicationContext, mEtDesc);
            return false;
        }
        return true;
    }

    private boolean validateReviews(Context applicationContext, EditText mEtReviews) {
        if (mEtReviews.getText().toString().trim().isEmpty()) {
//            mEtAdDesc.setError(applicationContext.getString(R.string.err_msg_adDesc));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_reviews));
            requestFocus(applicationContext, mEtReviews);
            return false;
        }
        return true;
    }

    private boolean validateText(Context applicationContext, EditText mEtReportAbuseMsg) {
        if (mEtReportAbuseMsg.getText().toString().trim().isEmpty()) {
//            mEtAdDesc.setError(applicationContext.getString(R.string.err_msg_adDesc));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_reviews));
            //requestFocus(applicationContext, mEtReportAbuseMsg);
            return false;
        }
        return true;
    }

    private boolean validateDate(Context applicationContext, EditText mEtSelectDate) {
        if (mEtSelectDate.getText().toString().trim().isEmpty()) {
//            mEtSelectDate.setError(applicationContext.getString(R.string.err_msg_adDate));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_adDate));
            requestFocus(applicationContext, mEtSelectDate);
            Utility.hideKeyboard(applicationContext, mEtSelectDate);
//            mEtSelectDate.setFocusable(false);
//            mEtSelectDate.setFocusableInTouchMode(false);
            return false;
        }
        return true;
    }

    private boolean validateDateGreater(Context applicationContext, boolean isDateGreater, EditText mEtSelectDate) {
        if (!isDateGreater) {
//            mEtSelectDate.setError(applicationContext.getString(R.string.err_msg_adDate));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_adDate_greater));
            requestFocus(applicationContext, mEtSelectDate);
            Utility.hideKeyboard(applicationContext, mEtSelectDate);
//            mEtSelectDate.setFocusable(false);
//            mEtSelectDate.setFocusableInTouchMode(false);
            return false;
        }
        return true;
    }

    private boolean validateCost(Context applicationContext, EditText mEtSplittCost) {
        if (mEtSplittCost.getText().toString().trim().isEmpty()) {
//            mEtSplittCost.setError(applicationContext.getString(R.string.err_msg_adCost));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_adCost));
            requestFocus(applicationContext, mEtSplittCost);
            return false;
        } else if (Long.parseLong(mEtSplittCost.getText().toString().trim()) <= 0) {
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_adCost_lesser));
            requestFocus(applicationContext, mEtSplittCost);
            return false;
        }
        return true;
    }

    private boolean validateLocation(Context applicationContext, EditText mEtAdLocation) {
        if (mEtAdLocation.getText().toString().trim().isEmpty()) {
//            mEtAdLocation.setError(applicationContext.getString(R.string.err_msg_adLocation));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_adLocation));
            requestFocus(applicationContext, mEtAdLocation);
            return false;
        }
        return true;
    }

    private boolean validateTime(Context applicationContext, EditText mEtSelectTime) {
        if (mEtSelectTime.getText().toString().trim().isEmpty()) {
            mEtSelectTime.setFocusable(true);
//            mEtSelectTime.setError(applicationContext.getString(R.string.err_msg_adTime));
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_adTime));
            requestFocus(applicationContext, mEtSelectTime);
            Utility.hideKeyboard(applicationContext, mEtSelectTime);
//            mEtSelectTime.setFocusable(false);
//            mEtSelectTime.setFocusableInTouchMode(false);
            return false;
        }
        return true;
    }

    private boolean validateCountryId(Context applicationContext, String mCountryId) {
        if (mCountryId == null) {
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_country));
            return false;
        } else if (mCountryId.isEmpty()) {
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_country));
            return false;
        } else
            return true;
    }

    private boolean validateStateId(Context applicationContext, String mStateId) {
        if (mStateId == null) {
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_state));
            return false;
        } else if (mStateId.trim().isEmpty()) {
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_state));
            return false;
        }
        return true;
    }

    private boolean validateCityId(Context applicationContext, String mCityId) {
        if (mCityId == null) {
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_city));
            return false;
        } else if (mCityId.trim().isEmpty()) {
            Utility.showToastMessageLong(applicationContext, applicationContext.getResources().getString(R.string.err_msg_city));
            return false;
        }
        return true;
    }

}
