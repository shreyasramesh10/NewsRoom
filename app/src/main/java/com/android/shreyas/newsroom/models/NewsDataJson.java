package com.android.shreyas.newsroom.models;

import com.android.shreyas.newsroom.MyUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SHREYAS on 3/18/2016.
 */

//Top Stories
public class NewsDataJson {
    public List<Map<String,?>> newsList;

    public List<Map<String, ?>> getNewsList() {
        return newsList;
    }

    public int getSize(){
        return newsList.size();
    }

    public HashMap getItem(int i){
        if (i >=0 && i < newsList.size()){
            return (HashMap) newsList.get(i);
        } else return null;
    }

    public NewsDataJson(){
        newsList = new ArrayList<Map<String,?>>();
    }

//    public void downloadNewsDataFromJson(String jsonStringUrl){
//        String description = null;
//        String url = null;
//        String title = null;
//        String imageurl = null;
//        JSONArray newsJsonArray = null;
//        JSONObject newsJsonObj = null;
//        newsList = new ArrayList<Map<String,?>>();
//        try {
//            String jsonString = MyUtility.downloadJSONusingHTTPGetRequest(jsonStringUrl);
//            JSONObject s = new JSONObject(jsonString);
//            newsJsonArray = s.getJSONArray("results");
//            for(int i = 0; i <newsJsonArray.length();i++){
//                newsJsonObj = (JSONObject) newsJsonArray.get(i);
//                if(newsJsonObj!=null){
//                    title = (String) newsJsonObj.get("title");
//                    description = (String) newsJsonObj.get("abstract");
//                    JSONArray media =(JSONArray) newsJsonObj.get("multimedia");
//                    url = (String) newsJsonObj.get("url");
//                    //String media = (String) newsJsonObj.get("multimedia");
//                    if(media.length()>0){
//                        JSONObject newsJsonObj2 =(JSONObject)media.get(2);
//                        imageurl=(String)newsJsonObj2.get("url");
//                    }
//
//                }
//                newsList.add(createNews(title,description,imageurl,url));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }

    public void downloadNewsDataFromJson(String jsonStringUrl){
        String description = null;
        String url = null;
        String title = null;
        String headline="";
        String imageurl = null;
        JSONArray newsJsonArray = null;
        JSONObject newsJsonObj = null;
        newsList = new ArrayList<Map<String,?>>();
        try {
            String jsonString = MyUtility.downloadJSONusingHTTPGetRequest(jsonStringUrl);
            JSONObject s = new JSONObject(jsonString);
            JSONObject s1 = s.getJSONObject("response");
            newsJsonArray = s1.getJSONArray("docs");
            for(int i = 0; i <newsJsonArray.length();i++){
                newsJsonObj = (JSONObject) newsJsonArray.get(i);
                if(newsJsonObj!=null){
                    JSONObject title1 =  (JSONObject)newsJsonObj.get("headline");
                    headline = (String) title1.get("main");
                    description = (String) newsJsonObj.get("snippet");
                    JSONArray media =(JSONArray) newsJsonObj.get("multimedia");
                    url = (String) newsJsonObj.get("web_url");

                    if(media.length()>0){
                        JSONObject newsJsonObj2 =(JSONObject)media.get(1);
                        imageurl="https://www.nytimes.com/" + (String)newsJsonObj2.get("url");
                    }

                }
                newsList.add(createNews(headline,description,imageurl,url));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private HashMap createNews(String title, String description, String imageurl,String url) {
        HashMap news = new HashMap();
        news.put("title", title);
        news.put("description", description);
        news.put("icon",imageurl);
        news.put("url",url);
        return news;
    }
}
