package com.example.myapplication;

public class LatestPrice {
    private String ticker;
    private double last_price;
    private double change;
    private double change_percentage;
    private long current_timestamp;
    private double high_price;
    private double low_price;
    private double open_price;
    private double prev_close;

    public LatestPrice(String ticker, double last_price, double change, double change_percentage, long current_timestamp, double high_price, double low_price, double open_price, double prev_close) {
        this.ticker = ticker;
        this.last_price = last_price;
        this.change = change;
        this.change_percentage = change_percentage;
        this.current_timestamp = current_timestamp;
        this.high_price = high_price;
        this.low_price = low_price;
        this.open_price = open_price;
        this.prev_close = prev_close;
    }

    public String get_ticker() {
        return this.ticker;
    }

    public double get_last_price() {
        return this.last_price;
    }

    public double get_change() {
        return this.change;
    }

    public double get_change_percentage() {
        return this.change_percentage;
    }

    public long get_current_timestamp() {
        return this.current_timestamp;
    }

    public double get_high_price() {
        return this.high_price;
    }

    public double get_low_price() {
        return this.low_price;
    }

    public double get_open_price() {
        return this.open_price;
    }

    public double get_prev_close() {
        return this.prev_close;
    }
}
