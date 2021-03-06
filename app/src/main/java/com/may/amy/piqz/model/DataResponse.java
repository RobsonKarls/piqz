package com.may.amy.piqz.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kuhnertj on 15.04.2016.
 */
public class DataResponse {
    @SerializedName("children")
    private List<ChildrenResponse> children;

    @SerializedName("after")
    private String after;
    @SerializedName("before")
    private String before;

    public DataResponse() {
        after = "";
    }

    public List<ChildrenResponse> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenResponse> children) {
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
}
