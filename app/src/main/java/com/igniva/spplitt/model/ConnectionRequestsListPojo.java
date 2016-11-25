package com.igniva.spplitt.model;

/**
 * Created by igniva-php-08 on 26/5/16.
 */
public class ConnectionRequestsListPojo {
    String connectivity;
    String connected_by_id;
    String connected_by_name;
    String connected_by_photo;
    String connected_by_time;
    boolean is_accept;
    boolean is_reject;

    public boolean is_accept() {
        return is_accept;
    }

    public void setIs_accept(boolean is_accept) {
        this.is_accept = is_accept;
    }

    public void setIs_reject(boolean is_reject) {
        this.is_reject = is_reject;
    }

    public boolean is_reject() {
        return is_reject;
    }

    public String getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(String connectivity) {
        this.connectivity = connectivity;
    }

    public String getConnected_by_id() {
        return connected_by_id;
    }

    public String getConnected_by_name() {
        return connected_by_name;
    }

    public String getConnected_by_photo() {
        return connected_by_photo;
    }

    public String getConnected_by_time() {
        return connected_by_time;
    }
}
