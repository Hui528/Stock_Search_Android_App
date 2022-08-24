package com.example.myapplication;

public class HistoricalData {
    private double close_prices[];
    private double high_prices[];
    private double low_prices[];
    private double open_prices[];
    private String status;
    private long timestamp[];
    private long volume[];

    public HistoricalData(double close_prices[], double high_prices[], double low_prices[], double open_prices[], String status, long timestamp[], long volume[]) {
        this.close_prices = close_prices;
        this.high_prices = high_prices;
        this.low_prices = low_prices;
        this.open_prices = open_prices;
        this.status = status;
        this.timestamp = timestamp;
        this.volume = volume;
    }

    public double[] get_close_prices() {
        return this.close_prices;
    }

    public double[] get_high_prices() {
        return this.high_prices;
    }

    public double[] get_low_prices() {
        return this.low_prices;
    }

    public double[] get_open_prices() {
        return this.open_prices;
    }

    public String get_status() {
        return this.status;
    }

    public long[] get_timestamp() {
        return this.timestamp;
    }

    public long[] get_volume() {
        return this.volume;
    }
}
