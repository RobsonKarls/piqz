package com.may.amy.piqz.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kuhnertj on 26.04.2016.
 */
public class ChildrenResponse {
    @SerializedName("data")
    private NewsItem data;

    public NewsItem getData() {
        return data;
    }

    public void setData(NewsItem data) {
        this.data = data;
    }
}
