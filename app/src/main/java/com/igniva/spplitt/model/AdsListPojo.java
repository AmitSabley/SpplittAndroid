package com.igniva.spplitt.model;

import java.io.Serializable;

/**
 * Created by igniva-php-08 on 23/5/16.
 */
public class AdsListPojo implements Serializable {
    String ad_id;
    String category_id;
    String posted_by_id;
    String posted_by_username;
    String posted_by_first_name;
    String posted_by_last_name;
    String ad_title;
    String ad_description;
    String ad_location;
    String ad_city;
    String ad_cost;
    String ad_expiration_time;
    String ad_expiration_date;
    String ad_currency_id;
    String ad_city_id;
    String ad_status;
    String city_name;
    String ad_country_name;
    String ad_country_id;
    String ad_state_id;
    String ad_state_name;
    String ad_total_request;
    String ad_city_name;
    String ad_connected_email;
    String ad_connected_mobile;
    String ad_connected_username;
    String ad_state;
    String rating_value;
    boolean is_flagged;
    boolean is_connect;
    boolean is_rating;

    public String getAd_state() {
        return ad_state;
    }

    public String getAd_connected_username() {
        return ad_connected_username;
    }

    public String getAd_connected_email() {
        return ad_connected_email;
    }

    public String getAd_connected_mobile() {
        return ad_connected_mobile;
    }

    public String getAd_state_id() {
        return ad_state_id;
    }

    public String getAd_state_name() {
        return ad_state_name;
    }

    public void setIs_connect(boolean is_connect) {
        this.is_connect = is_connect;
    }

    public boolean is_connect() {
        return is_connect;
    }

    public void setIs_rating(boolean is_rating) {
        this.is_rating = is_rating;
    }

    public boolean getIs_Rating() {
        return is_rating;
    }

    public String getAd_city_name() {
        return ad_city_name;
    }

    public String getAd_total_request() {
        return ad_total_request;
    }

    public String getAd_country_name() {
        return ad_country_name;
    }

    public String getAd_country_id() {
        return ad_country_id;
    }

    public String getAd_city_id() {
        return ad_city_id;
    }

    public String getCity_name() {
        return city_name;
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

    public String getAd_status() {
        return ad_status;
    }

    public boolean is_flagged() {
        return is_flagged;
    }

    public void setIs_flagged(boolean is_flagged) {
        this.is_flagged = is_flagged;
    }


    public String getRating_value() {
        return rating_value;
    }

    public void setRating_value(String rating_value) {
        this.rating_value = rating_value;
    }
}
