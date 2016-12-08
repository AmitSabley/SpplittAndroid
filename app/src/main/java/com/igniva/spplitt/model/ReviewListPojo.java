package com.igniva.spplitt.model;

/**
 * Created by igniva-php-08 on 3/6/16.
 */
public class ReviewListPojo {
    String user_id;
    String user_image;
    String user_name;
    String user_review;
    String user_rating_value;
    String other_user_id;


    public String getUser_rating_value() {
        return user_rating_value;
    }

    public void setUser_rating_value(String user_rating_value) {
        this.user_rating_value = user_rating_value;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setOther_User_id(String other_user_id){
        this.other_user_id = other_user_id;
    }

    public void setUser_review(String user_review) {
        this.user_review = user_review;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_review() {
        return user_review;
    }

    public String getOther_user_id() {
        return other_user_id;
    }
}
