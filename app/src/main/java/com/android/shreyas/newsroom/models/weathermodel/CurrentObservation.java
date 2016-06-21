
package com.android.shreyas.newsroom.models.weathermodel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CurrentObservation {


    @SerializedName("display_location")
    @Expose
    private DisplayLocation displayLocation;



    @SerializedName("observation_time")
    @Expose
    private String observationTime;
  
    @SerializedName("weather")
    @Expose
    private String weather;
    @SerializedName("temperature_string")
    @Expose
    private String temperatureString;
    @SerializedName("temp_f")
    @Expose
    private Double tempF;
    @SerializedName("temp_c")
    @Expose
    private Double tempC;
    @SerializedName("relative_humidity")
    @Expose
    private String relativeHumidity;
    @SerializedName("wind_string")
    @Expose
    private String windString;
   
    @SerializedName("feelslike_string")
    @Expose
    private String feelslikeString;
    @SerializedName("feelslike_f")
    @Expose
    private String feelslikeF;
    @SerializedName("feelslike_c")
    @Expose
    private String feelslikeC;
    
    @SerializedName("icon_url")
    @Expose
    private String iconUrl;
    @SerializedName("forecast_url")
    @Expose
    private String forecastUrl;
    
    /**
     * No args constructor for use in serialization
     * 
     */
    public CurrentObservation() {
    }

    

    /**
     * 
     * @return
     *     The displayLocation
     */
    public DisplayLocation getDisplayLocation() {
        return displayLocation;
    }

    /**
     * 
     * @param displayLocation
     *     The display_location
     */
    public void setDisplayLocation(DisplayLocation displayLocation) {
        this.displayLocation = displayLocation;
    }

 
    /**
     * 
     * @return
     *     The observationTime
     */
    public String getObservationTime() {
        return observationTime;
    }

    /**
     * 
     * @param observationTime
     *     The observation_time
     */
    public void setObservationTime(String observationTime) {
        this.observationTime = observationTime;
    }


    /**
     * 
     * @return
     *     The weather
     */
    public String getWeather() {
        return weather;
    }

    /**
     * 
     * @param weather
     *     The weather
     */
    public void setWeather(String weather) {
        this.weather = weather;
    }

    /**
     * 
     * @return
     *     The temperatureString
     */
    public String getTemperatureString() {
        return temperatureString;
    }

    /**
     * 
     * @param temperatureString
     *     The temperature_string
     */
    public void setTemperatureString(String temperatureString) {
        this.temperatureString = temperatureString;
    }

    /**
     * 
     * @return
     *     The tempF
     */
    public Double getTempF() {
        return tempF;
    }

    /**
     * 
     * @param tempF
     *     The temp_f
     */
    public void setTempF(Double tempF) {
        this.tempF = tempF;
    }

    /**
     * 
     * @return
     *     The tempC
     */
    public Double getTempC() {
        return tempC;
    }

    /**
     * 
     * @param tempC
     *     The temp_c
     */
    public void setTempC(Double tempC) {
        this.tempC = tempC;
    }

    /**
     * 
     * @return
     *     The relativeHumidity
     */
    public String getRelativeHumidity() {
        return relativeHumidity;
    }

    /**
     * 
     * @param relativeHumidity
     *     The relative_humidity
     */
    public void setRelativeHumidity(String relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    /**
     * 
     * @return
     *     The windString
     */
    public String getWindString() {
        return windString;
    }

    /**
     * 
     * @param windString
     *     The wind_string
     */
    public void setWindString(String windString) {
        this.windString = windString;
    }


    /**
     * 
     * @return
     *     The feelslikeString
     */
    public String getFeelslikeString() {
        return feelslikeString;
    }

    /**
     * 
     * @param feelslikeString
     *     The feelslike_string
     */
    public void setFeelslikeString(String feelslikeString) {
        this.feelslikeString = feelslikeString;
    }

    /**
     * 
     * @return
     *     The feelslikeF
     */
    public String getFeelslikeF() {
        return feelslikeF;
    }

    /**
     * 
     * @param feelslikeF
     *     The feelslike_f
     */
    public void setFeelslikeF(String feelslikeF) {
        this.feelslikeF = feelslikeF;
    }

    /**
     * 
     * @return
     *     The feelslikeC
     */
    public String getFeelslikeC() {
        return feelslikeC;
    }

    /**
     * 
     * @param feelslikeC
     *     The feelslike_c
     */
    public void setFeelslikeC(String feelslikeC) {
        this.feelslikeC = feelslikeC;
    }

 
    /**
     * 
     * @return
     *     The iconUrl
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * 
     * @param iconUrl
     *     The icon_url
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
