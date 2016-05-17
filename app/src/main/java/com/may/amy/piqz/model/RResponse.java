package com.may.amy.piqz.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kuhnertj on 26.04.2016.
 */
public class RResponse {
    @SerializedName("children")
    private List<NewsItem> children;

    @SerializedName("after")
    private String after;
    @SerializedName("before")
    private String before;

    private String error;

    public RResponse() {
        after = "";
    }

    public List<NewsItem> getChildren() {
        return children;
    }

    public void setChildren(List<NewsItem> children) {
        this.children = children;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getError() {
        return error;
    }

    public void setError(String errorCode) {
        this.error = errorCode;
    }
}
