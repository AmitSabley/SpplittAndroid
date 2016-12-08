package com.igniva.spplitt.model;

/**
 * Created by igniva-php-08 on 21/6/16.
 */
public class AppliedlistPojo {
    String ad_id;
    String category_id;
    String posted_by_id;
    String posted_by_username;
    String posted_by_first_name;
    String posted_by_last_name;
    String ad_title;
    String ad_description;
    String ad_country_id;
    String ad_country_name;
    String ad_currency_id;
    String ad_currency_code;
    String ad_currency_image;
    String ad_city_id;
    String ad_city_name;
    String ad_cost;
    String ad_total_request;
    String ad_expiration_time;
    String ad_expiration_date;
    String ad_status;
    boolean is_details_visible;
    boolean is_rating;
    String rating_value;

//    String owner_details;
//    String owner_username;
//    String owner_email;
//    String owner_picture;
//    String owner_mobile;

    OwnerDetails owner_details;
//    public String getOwner_details() {
//        return owner_details;
//    }

    public OwnerDetails getOwner_details() {
        return owner_details;
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

    public String getAd_country_id() {
        return ad_country_id;
    }

    public String getAd_country_name() {
        return ad_country_name;
    }

    public String getAd_currency_id() {
        return ad_currency_id;
    }

    public String getAd_currency_code() {
        return ad_currency_code;
    }

    public String getAd_currency_image() {
        return ad_currency_image;
    }

    public String getAd_city_id() {
        return ad_city_id;
    }

    public String getAd_city_name() {
        return ad_city_name;
    }

    public String getAd_cost() {
        return ad_cost;
    }

    public String getAd_total_request() {
        return ad_total_request;
    }

    public String getAd_expiration_time() {
        return ad_expiration_time;
    }
    public void setIs_rating(boolean is_rating) {
        this.is_rating = is_rating;
    }

    public boolean getIs_Rating() {
        return is_rating;
    }

    public String getRating_value() {
        return rating_value;
    }

    public void setRating_value(String rating_value) {
        this.rating_value = rating_value;
    }
    public String getAd_expiration_date() {
        return ad_expiration_date;
    }

    public String getAd_status() {
        return ad_status;
    }

    public boolean is_details_visible() {
        return is_details_visible;
    }
}
