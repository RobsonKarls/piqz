package com.may.amy.piqz.model;

/**
 * Created by kuhnertj on 15.04.2016.
 */
public class NewsItem {
    private String author, title, thumbnail, url;
    private int numComments;
    private long created;

    public NewsItem(String author, String title, int numComments, long created, String thumbnail, String url) {
        this.author = author;
        this.title = title;
        this.thumbnail = thumbnail;
        this.url = url;
        this.numComments = numComments;
        this.created = created;
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
