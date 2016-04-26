package com.may.amy.piqz.model.rest;

import com.may.amy.piqz.model.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by kuhnertj on 15.04.2016.
 */
public interface RestHelper {
    @GET("/r/{subreddit}/hot.json")
    Call<NewsResponse> listHot(@Header("Authorization") String token,
                               @Header("Accept") String accept,
                               @Path("subreddit") String subreddit,
                               @Query("after") String after,
                               @Query("limit") String limit);


}
