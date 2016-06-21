
package com.android.shreyas.newsroom.models.weathermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class WeatherDataNew {

    @SerializedName("current_observation")
    @Expose
    private CurrentObservation currentObservation;

    /**
     * No args constructor for use in serialization
     * 
     */
    public WeatherDataNew() {
    }

    /**
     * 
     * @return
     *     The currentObservation
     */
    public CurrentObservation getCurrentObservation() {
        return currentObservation;
    }

    /**
     * 
     * @param currentObservation
     *     The current_observation
     */
    public void setCurrentObservation(CurrentObservation currentObservation) {
        this.currentObservation = currentObservation;
    }

}
