
package com.android.shreyas.newsroom.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Medium implements Serializable{

    @SerializedName("media-metadata")
    @Expose
    private List<MediaMetadatum> mediaMetadata = new ArrayList<MediaMetadatum>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Medium() {
    }

    /**
     * 
     * @return
     *     The mediaMetadata
     */
    public List<MediaMetadatum> getMediaMetadata() {
        return mediaMetadata;
    }

    /**
     * 
     * @param mediaMetadata
     *     The media-metadata
     */
    public void setMediaMetadata(List<MediaMetadatum> mediaMetadata) {
        this.mediaMetadata = mediaMetadata;
    }

}
