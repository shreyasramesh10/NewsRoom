package com.android.shreyas.newsroom.models.weathermodel;

import com.android.shreyas.newsroom.MyUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherDataJson {

    public List<Map<String,?>> weatherList;
    public List<Map<String, ?>> getWeatherList() {
        return weatherList;
    }

    public int getSize(){
        return weatherList.size();
    }

    public HashMap getItem(int i){
        if (i >=0 && i < weatherList.size()){
            return (HashMap) weatherList.get(i);
        } else return null;
    }

	public WeatherDataJson(){
        weatherList = new ArrayList<Map<String,?>>();
	}

    public void downloadWeatherDataFromJson(String jsonStringUrl){
		String date="";
        String description="";
		String icon_url="";

        String tempc_high="";
        String tempf_high="";
        String tempc_low="";
        String tempf_low="";

        JSONObject movieJsonObj;
        weatherList = new ArrayList<Map<String,?>>();
        try {
            String jsonString = MyUtility.downloadJSONusingHTTPGetRequest(jsonStringUrl);
            JSONObject weatherJsonObject = new JSONObject(jsonString);

                movieJsonObj = (JSONObject) weatherJsonObject.get("forecast");
            JSONObject simple = (JSONObject) movieJsonObj.get("simpleforecast");
            JSONArray forecastday = (JSONArray) simple.getJSONArray("forecastday");
            for(int i=0;i<forecastday.length();i++){
                JSONObject object = (JSONObject)forecastday.get(i);
                JSONObject dateobject = (JSONObject) object.get("date");
                date = dateobject.getString("pretty");
                JSONObject highTemp = (JSONObject) object.get("high");
                tempc_high = highTemp.getString("celsius");
                tempf_high = highTemp.getString("fahrenheit");
                JSONObject lowTemp = (JSONObject) object.get("low");
                tempc_low = lowTemp.getString("celsius");
                tempf_low = lowTemp.getString("fahrenheit");
                description = (String) object.get("conditions");
                icon_url = (String) object.get("icon_url");
                weatherList.add(createMovie(date, description, tempc_high, tempf_high, tempc_low,tempf_low,icon_url));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void downloadWeatherConditionsFromJson(String jsonStringUrl){
        String date="";
        String description="";
        String icon_url="";

        String location="";
        String temp_string="";
        String temp_c="";
        String temp_f="";
        String feels_like_string="";
        String feels_like_c="";
        String feels_like_f="";
        String wind_metric="";
        String wind_imperial="";
        JSONObject movieJsonObj;
        weatherList = new ArrayList<Map<String,?>>();
        try {
            String jsonString = MyUtility.downloadJSONusingHTTPGetRequest(jsonStringUrl);
            JSONObject weatherJsonObject = new JSONObject(jsonString);

            movieJsonObj = (JSONObject) weatherJsonObject.get("current_observation");
            date = (String) movieJsonObj.get("observation_time");
            description = movieJsonObj.getString("weather");
            temp_string = movieJsonObj.getString("temperature_string");
            temp_c = movieJsonObj.getString("temp_c");
            temp_f = movieJsonObj.getString("temp_f");
            wind_metric = movieJsonObj.getString("wind_kph");
            wind_imperial = movieJsonObj.getString("wind_mph");
            feels_like_string = movieJsonObj.getString("feelslike_string");
            feels_like_c = movieJsonObj.getString("feelslike_c");
            feels_like_f = movieJsonObj.getString("feelslike_f");
            JSONObject loc = movieJsonObj.getJSONObject("display_location");
            location = loc.getString("full");
            icon_url = movieJsonObj.getString("icon_url");
            weatherList.add(createConditions(date,description,location,temp_c,
                    temp_f,temp_string,feels_like_c,feels_like_f,feels_like_string,icon_url,wind_metric,wind_imperial));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private HashMap createMovie(String date, String description, String tempc_high, String tempf_high, String tempc_low, String tempf_low, String icon_url) {
        HashMap weather = new HashMap();
        weather.put("date", date);
        weather.put("description", description);
        weather.put("tempc_high",tempc_high);
        weather.put("tempf_high",tempf_high);
        weather.put("tempc_low",tempc_low);
        weather.put("tempf_low", tempf_low);
        weather.put("icon_url", icon_url);
        return weather;
    }

    private HashMap createConditions(String date, String description,String location,
                                     String temp_c, String temp_f,String temp_string,
                                     String feelslike_c, String feelslike_f,String feelslike_string,
                                     String icon_url,String wind_metric,String wind_imperial) {
        HashMap weather = new HashMap();
        weather.put("date", date);
        weather.put("description", description);
        weather.put("location",location);
        weather.put("temp_c",temp_c);
        weather.put("temp_f",temp_f);
        weather.put("temp_string", temp_string);
        weather.put("feelslike_c", feelslike_c);
        weather.put("feelslike_f", feelslike_f);
        weather.put("feelslike_string", feelslike_string);
        weather.put("icon_url", icon_url);
        weather.put("wind_metric", wind_metric);
        weather.put("wind_imperial", wind_imperial);
        return weather;
    }

}
