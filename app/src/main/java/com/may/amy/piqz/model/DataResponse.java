package com.may.amy.piqz.model;

import java.util.List;

/**
 * Created by kuhnertj on 15.04.2016.
 */
public class DataResponse {
    private List<NewsItem> children;
    private String after, before;

    public DataResponse() {
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
}
