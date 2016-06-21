package com.android.shreyas.newsroom.models;

import java.io.Serializable;

/**
 * Created by SHREYAS on 3/20/2016.
 */
public class ChatMessage implements Serializable {
    String message;
    String name;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    String imageUrl;

    public ChatMessage() {
    }

    public ChatMessage(String name, String message, String imageUrl) {
        this.message = message;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }
}
