package com.android.shreyas.newsroom.apimanager;

import com.android.shreyas.newsroom.models.NewsData;
import com.android.shreyas.newsroom.models.weathermodel.WeatherData;
import com.android.shreyas.newsroom.models.weathermodel.WeatherDataNew;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by SHREYAS on 4/9/2016.
 */
public interface GetNewsApi {

    @GET("/svc/topstories/v2/{section}")
    Call<NewsData> GetTopStories(@Path("section") String section, @Query("api-key") String api_key);

    @GET("/svc/mostpopular/v2/{category}/all-sections/7.json")
    Call<NewsData> GetMostPopular(@Path("category") String category, @Query("api-key") String api_key);

    @GET("/data/2.5/weather")
    Call<WeatherData> getWeatherFromApiGeo(@Query("lat") String lat, @Query("lon") String lon , @Query("appid") String key);

    @GET("/data/2.5/weather")
    Call<WeatherData> getWeatherFromApiGeoUnits(@Query("lat") String lat, @Query("lon") String lon ,@Query("units") String units, @Query("appid") String key);


    @GET("/api/027d1740ac4b4e92/conditions/q/{lat},{lon}.json")
    Call<WeatherDataNew> getWeatherFromUnderGround(@Path("lat") String lat, @Path("lon") String lon);
    //Call<NewsSearchData> GetArticleSearch();getWeatherFromUnderGround
}