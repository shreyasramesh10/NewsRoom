
package com.android.shreyas.newsroom.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewsData  implements Serializable {

    @SerializedName("num_results")
    @Expose
    private Integer numResults;
    @SerializedName("results")
    @Expose
    private List<Result> results = new ArrayList<Result>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public NewsData() {
    }

    /**
     * 
     * @return
     *     The numResults
     */
    public Integer getNumResults() {
        return numResults;
    }

    /**
     * 
     * @param numResults
     *     The num_results
     */
    public void setNumResults(Integer numResults) {
        this.numResults = numResults;
    }

    /**
     * 
     * @return
     *     The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     * 
     * @param results
     *     The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

}
