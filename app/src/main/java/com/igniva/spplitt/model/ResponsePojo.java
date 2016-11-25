package com.igniva.spplitt.model;

import java.util.List;

/**
 * Wrapper class -  to wrap ant response from webservice
 */
public class ResponsePojo {

    private int status_code;
    private String description;
    private DataPojo data;
    private ErrorPojo error;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus(int status_code) {
        this.status_code = status_code;
    }

    public DataPojo getData() {
        return data;
    }

    public void setData(DataPojo data) {
        this.data = data;
    }

    public ErrorPojo getError() {
        return error;
    }

    public void setError(ErrorPojo error) {
        this.error = error;
    }

}
