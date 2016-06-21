
package com.android.shreyas.newsroom.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Multimedium implements Serializable {

    @SerializedName("url")
    @Expose
    private String url;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Multimedium() {
    }

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }
}
