package com.reportdroid.shibuyaxpress.reportdroid.Models;

/**
 * Created by paulf on 17-Nov-17.
 */

public class ResponseUser {
    private String status;
    private User data;

    public ResponseUser() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseUser{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
