package com.example.myapplication;

import java.time.Instant;

public class News {
    private String headline;
    private String image;
    private String url;
    private String source;
    private long datetime;
    private String summary;

    public News(String headline, String image, String url, String source, long datetime, String summary) {
        this.headline = headline;
        this.image = image;
        this.url = url;
        this.source = source;
        this.datetime = datetime;
        this.summary = summary;
    }

    public String get_headline() {
        return this.headline;
    }

    public String get_image() {
        return this.image;
    }

    public String get_url() {
        return this.url;
    }

    public String get_source() {
        return this.source;
    }

    public long get_datetime() {
        return this.datetime;
    }

    public String get_summary() {
        return this.summary;
    }

    public int get_datetime_to_today() {
        long unixTime = Instant.now().getEpochSecond();
        int diff = (int)Math.ceil(Math.abs(unixTime - get_datetime()) / (double)(60 * 60));
        return diff;
    }
}
