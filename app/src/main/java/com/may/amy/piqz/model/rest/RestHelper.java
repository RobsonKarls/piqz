package com.may.amy.piqz.model.rest;

import com.may.amy.piqz.model.NewsResponse;
import com.may.amy.piqz.util.PrivateConstants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Class for API calls to reddit with Retrofit
 */
public interface RestHelper {
    @GET("/r/{subreddit}/hot.json")
    Call<NewsResponse> listHot(@Header("Authorization") String token,
                               @Header("Accept") String accept,
                               @Path("subreddit") String subreddit,
                               @Query("after") String after,
                               @Query("limit") String limit);

    @GET("/r/{subreddit}/hot.json")
    Call<NewsResponse> listHot(@Path("subreddit") String subreddit,
                               @Query("after") String after,
                               @Query("limit") String limit);

    @GET(PrivateConstants.URL_USER + PrivateConstants.URL_MULTI + ".json")
    Call<NewsResponse> getMulti();

    @GET(PrivateConstants.URL_USER + PrivateConstants.URL_MULTI + ".json")
    Call<NewsResponse> getMulti(@Query("after") String after, @Query("limit") String limit);
}
