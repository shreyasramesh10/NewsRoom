package com.android.shreyas.newsroom.models;

import java.io.Serializable;

/**
 * Created by SHREYAS on 4/10/2016.
 */
public class NewsDb implements Serializable {
    private String url;
    private String title;
    private String description;
    private String image_url;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    public NewsDb(String url, String title, String description, String image_url) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.image_url = image_url;
    }
}
