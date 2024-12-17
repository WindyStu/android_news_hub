package com.example.final_test;

import java.io.Serializable;

public class NewsItem implements Serializable {
    private String title;
    private String summarize;
    private String href;
    private String imgSrc;
    private String details;
    private boolean isFavorite;

    public String[] getTextList(){
        return new String[]{
            title,summarize,href,imgSrc
        };
    }

    @Override
    public String toString() {
        return "NewsItem{" +
                "title='" + title + '\'' +
                ", summarize='" + summarize + '\'' +
                ", href='" + href + '\'' +
                ", imgSrc='" + imgSrc + '\'' +
                '}';
    }

    public NewsItem(String title, String summarize, String href, String imgSrc, String details, boolean isFavorite) {
        this.title = title;
        this.summarize = summarize;
        this.href = href;
        this.imgSrc = imgSrc;
        this.details = details;
        this.isFavorite = isFavorite;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummarize(String summarize) {
        this.summarize = summarize;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getTitle() {
        return title;
    }

    public String getSummarize() {
        return summarize;
    }

    public String getHref() {
        return href;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
