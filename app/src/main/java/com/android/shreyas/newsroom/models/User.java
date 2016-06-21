package com.android.shreyas.newsroom.models;

import java.io.Serializable;

/**
 * Created by SHREYAS on 4/14/2016.
 */
public class User implements Serializable {
    private String userName;
    private String displayImageURL;
    private String email;

    public User() {
    }

    public String getDisplayImageURL() {
        return displayImageURL;
    }

    public void setDisplayImageURL(String displayImageURL) {
        this.displayImageURL = displayImageURL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
