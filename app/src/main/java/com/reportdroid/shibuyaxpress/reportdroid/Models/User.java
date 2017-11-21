package com.reportdroid.shibuyaxpress.reportdroid.Models;

/**
 * Created by paulf on 13-Nov-17.
 */

public class User {
    private String username;
    private String password;
    private Integer id;
    private String email;
    private String image;

    private static User _INSTANCE=null;

    public static User getInstance(){
        if(_INSTANCE==null){
            _INSTANCE=new User();
        }
        return _INSTANCE;
    }

    private User() {
    }

    public String getProfile() {
        return image;
    }

    public void setProfile(String profile) {
        this.image = profile;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
