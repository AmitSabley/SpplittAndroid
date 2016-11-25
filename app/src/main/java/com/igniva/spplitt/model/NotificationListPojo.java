package com.igniva.spplitt.model;

/**
 * Created by igniva-php-08 on 17/6/16.
 */
public class NotificationListPojo {
    String notification_id;
    String notification_user_id;
    String notification_title;
    String notification_user_name;
    String notification_user_pic;
    String notification_ad_id;
    String notification_message;
    String notification_path;
    String notification_code;
    String notification_status;
    String notification_created;

    public String getNotification_ad_id() {
        return notification_ad_id;
    }

    public String getNotification_user_name() {
        return notification_user_name;
    }

    public String getNotification_user_pic() {
        return notification_user_pic;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public String getNotification_user_id() {
        return notification_user_id;
    }

    public String getNotification_title() {
        return notification_title;
    }

    public String getNotification_message() {
        return notification_message;
    }

    public String getNotification_path() {
        return notification_path;
    }

    public String getNotification_code() {
        return notification_code;
    }

    public String getNotification_status() {
        return notification_status;
    }

    public String getNotification_created() {
        return notification_created;
    }
}
