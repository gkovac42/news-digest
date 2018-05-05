package com.example.goran.mvvm_demo.data.remote.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "article_table")
public class Article {

    @SerializedName("author")
    @Expose
    @Ignore
    private Object author;
    @SerializedName("title")
    @Expose
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "title")
    private String title;
    @SerializedName("description")
    @Expose
    @ColumnInfo(name = "description")
    private String description;
    @SerializedName("url")
    @Expose
    @ColumnInfo(name = "url")
    private String url;
    @SerializedName("urlToImage")
    @Expose
    @ColumnInfo(name = "url_to_image")
    private String urlToImage;
    @SerializedName("publishedAt")
    @Expose
    @Ignore
    private String publishedAt;


    public Object getAuthor() {
        return author;
    }

    public void setAuthor(Object author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}
