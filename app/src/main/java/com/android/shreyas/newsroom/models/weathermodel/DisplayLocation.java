
package com.android.shreyas.newsroom.models.weathermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DisplayLocation {

    @SerializedName("full")
    @Expose
    private String full;
    
    /**
     * No args constructor for use in serialization
     * 
     */
    public DisplayLocation() {
    }


    /**
     * 
     * @return
     *     The full
     */
    public String getFull() {
        return full;
    }

    /**
     * 
     * @param full
     *     The full
     */
    public void setFull(String full) {
        this.full = full;
    }

}
