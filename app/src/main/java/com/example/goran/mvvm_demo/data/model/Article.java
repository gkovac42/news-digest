package com.example.goran.mvvm_demo.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "article_table")
public class Article {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @SerializedName("title")
    @Expose
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
    @ColumnInfo(name = "published_at")
    private String publishedAt;

    public Article() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getPublishedAtFormatted() {
        try {
            String date = publishedAt.substring(0, 10);
            String time = publishedAt.substring(11, 16);

            return date + " at " + time;

        } catch (Exception e) {
            return "publication date unknown";
        }
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}
