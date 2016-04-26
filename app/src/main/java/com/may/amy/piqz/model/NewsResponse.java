package com.may.amy.piqz.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kuhnertj on 15.04.2016.
 */
public class NewsResponse {
    @SerializedName("data")
    DataResponse data;

    public DataResponse getData() {
        return data;
    }

    public void setData(DataResponse data) {
        this.data = data;
    }
}
