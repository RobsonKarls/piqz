package com.may.amy.piqz.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kuhnertj on 15.04.2016.
 */
public class NewsItem {

    @SerializedName("author")
    private String author;

    @SerializedName("title")
    private String title;

    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("url")
    private String url;

    @SerializedName("num_comments")
    private int numComments;

    @SerializedName("created_utc")
    private long created;

    @SerializedName("score")
    private int score;

    @SerializedName("domain")
    private String domain;

    @SerializedName("selftext_html")
    private String htmlSelftext;

    @SerializedName("post_hint")
    private String postHint;

    @SerializedName("over_18")
    private boolean nsfw;

    private int postType;

    public NewsItem() {
        postType = 0;
    }

    public int getPostType() {
        return postType;
    }

    public boolean isNsfw() {
        return nsfw;
    }

    public void setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public String getPostHint() {
        return postHint;
    }

    public void setPostHint(String postHint) {
        this.postHint = postHint;
    }

    public String getHtmlSelftext() {
        return htmlSelftext;
    }

    public void setHtmlSelftext(String htmlSelftext) {
        this.htmlSelftext = htmlSelftext;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
