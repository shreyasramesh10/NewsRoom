package com.android.shreyas.newsroom.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by SHREYAS on 4/19/2016.
 */
public class Stories implements Serializable {
    public Stories() {
    }

    public Stories(String story, String image, String title) {
        this.story = story;
        this.image = image;
        this.title = title;
    }

    public Stories(String story, String title) {
        this.story = story;
        this.title = title;
    }

    private String title;
    private String story;
    private String image;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @JsonIgnore
    private String key;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }
}
