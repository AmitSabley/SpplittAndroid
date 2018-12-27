package com.igniva.spplitt.model;

import java.util.List;

/**
 * Created by igniva-andriod-11 on 4/5/16.
 */
public class DataPojo {
    String user_id, username, email, mobile, gender, age, country_name, currency_code, state, picture, auth_token, country_code, currency_id;
    String currency_image;
    String city_id;
    String city_name;
    String request_id;
    String is_age_public;
    String registration_by;
    String ad_id;
    String no_people;
    String category_id;
    String posted_by_id;
    String posted_by_username;
    String posted_by_first_name;
    String posted_by_last_name;
    String ad_title;
    String ad_description;
    String ad_location;
    String ad_city_name;
    String ad_city_id;
    String ad_country_id;
    String ad_country_name;
    String ad_city;
    String ad_cost;
    int ad_status;
    String ad_expiration_time;
    String ad_expiration_date;
    String ad_currency_id;
    boolean is_flagged;
    boolean is_connect;
    String other_user_id;
    String other_email;
    String other_username;
    String other_mobile;
    String other_country_name;
    String other_city;
    String other_picture;
    String other_city_name;
    String other_avg_rating;
    String other_created_date;
    String other_gender;
    String save_prefernces;
    String preferred_country_name;
    String preferred_state_name;
    String preferred_city_name;
    int other_total_ads_added;
    int other_total_ads_applied;
    List<ReviewListPojo> other_review;
    List<CityListPojo> cityList;
    List<CountriesListPojo> Countries;
    List<CategoriesListPojo> Categories;
    List<AdsListPojo> AdsList;
    List<ConnectionRequestsListPojo> Request;
    List<AdsPostedPojo> AdsPostedList;
    List<NotificationListPojo> notification;
    List<AppliedlistPojo> Appliedlist;
    List<StateListPojo> stateList;
    List<ImagePojo> image;
    boolean other_is_age;
    String other_age;
    String total_recode;
    String other_user_email;
    String other_user_mobile;
    String other_user_picture;
    int total_page;
    String Rating;
    String country_id;
    String state_name;
    boolean is_reject;

    public List<ImagePojo> getImage() {
        return image;
    }

    public void setImage(List<ImagePojo> image) {
        this.image = image;
    }

    public String getNo_people() {
        return no_people;
    }

    public void setNo_people(String no_people) {
        this.no_people = no_people;
    }

    public String getPreferred_country_name() {
        return preferred_country_name;
    }

    public String getPreferred_state_name() {
        return preferred_state_name;
    }

    public String getPreferred_city_name() {
        return preferred_city_name;
    }

    public boolean is_reject() {
        return is_reject;
    }

    public String getCountry_id() {
        return country_id;
    }

    public String getState_name() {
        return state_name;
    }

    public List<StateListPojo> getStateList() {
        return stateList;
    }

    public String getSave_prefernces() {
        return save_prefernces;
    }

    public void setOther_review(List<ReviewListPojo> other_review) {
        this.other_review = other_review;
    }

    public String getRating() {
        return Rating;
    }

    public int getTotal_page() {
        return total_page;
    }

    public List<AppliedlistPojo> getAppliedlist() {
        return Appliedlist;
    }

    public String getOther_user_picture() {
        return other_user_picture;
    }

    public String getOther_user_email() {
        return other_user_email;
    }

    public String getOther_user_mobile() {
        return other_user_mobile;
    }

    public String getTotal_recode() {
        return total_recode;
    }

    public List<NotificationListPojo> getNotification() {
        return notification;
    }

    public boolean isOther_is_age() {
        return other_is_age;
    }

    public String getOther_age() {
        return other_age;
    }

    public String getAd_city_name() {
        return ad_city_name;
    }

    public String getAd_city_id() {
        return ad_city_id;
    }

    public String getAd_country_id() {
        return ad_country_id;
    }

    public String getAd_country_name() {
        return ad_country_name;
    }

    public boolean is_connect() {
        return is_connect;
    }

    public String getOther_gender() {
        return other_gender;
    }

    public String getOther_created_date() {
        return other_created_date;
    }

    public String getOther_city_name() {
        return other_city_name;
    }

    public List<AdsPostedPojo> getAdsPostedList() {
        return AdsPostedList;
    }

    public String getOther_user_id() {
        return other_user_id;
    }

    public String getOther_email() {
        return other_email;
    }

    public String getOther_username() {
        return other_username;
    }

    public String getOther_mobile() {
        return other_mobile;
    }

    public String getOther_country_name() {
        return other_country_name;
    }

    public String getOther_city() {
        return other_city;
    }

    public String getOther_picture() {
        return other_picture;
    }

    public String getOther_avg_rating() {
        return other_avg_rating;
    }

    public void setOther_avg_rating(String other_avg_rating) {
        this.other_avg_rating = other_avg_rating;
    }

    public int getOther_total_ads_added() {
        return other_total_ads_added;
    }

    public int getOther_total_ads_applied() {
        return other_total_ads_applied;
    }

    public List<ReviewListPojo> getOther_review() {
        return other_review;
    }

    public String getCurrency_image() {
        return currency_image;
    }

    public String getCity_id() {
        return city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getRequest_id() {
        return request_id;
    }

    public List<CityListPojo> getCityList() {
        return cityList;
    }

    public int getAd_status() {
        return ad_status;
    }

    public String getAd_id() {
        return ad_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getPosted_by_id() {
        return posted_by_id;
    }

    public String getPosted_by_username() {
        return posted_by_username;
    }

    public String getPosted_by_first_name() {
        return posted_by_first_name;
    }

    public String getPosted_by_last_name() {
        return posted_by_last_name;
    }

    public String getAd_title() {
        return ad_title;
    }

    public String getAd_description() {
        return ad_description;
    }

    public String getAd_location() {
        return ad_location;
    }

    public String getAd_city() {
        return ad_city;
    }

    public String getAd_cost() {
        return ad_cost;
    }

    public String getAd_expiration_time() {
        return ad_expiration_time;
    }

    public String getAd_expiration_date() {
        return ad_expiration_date;
    }

    public String getAd_currency_id() {
        return ad_currency_id;
    }


    public boolean is_flagged() {
        return is_flagged;
    }

    public List<ConnectionRequestsListPojo> getRequest() {
        return Request;
    }

    public List<AdsListPojo> getAdsList() {
        return AdsList;
    }

    public List<CountriesListPojo> getCountries() {
        return Countries;
    }

    public List<CategoriesListPojo> getCategories() {
        return Categories;
    }

    public String getUser_id() {
        return user_id;
    }

//    public String getOtp() {
//        return otp;
//    }

    public String getRegistration_by() {
        return registration_by;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getGender() {
        return gender;
    }

    public String getCurrency_id() {
        return currency_id;
    }

    public String getAge() {
        return age;
    }

    public String getCountry_name() {
        return country_name;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public String getIs_age_public() {
        return is_age_public;
    }

    public String getState() {
        return state;
    }


    public String getPicture() {
        return picture;
    }


    public String getCountry_code() {
        return country_code;
    }

}
