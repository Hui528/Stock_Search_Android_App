package com.example.myapplication;

public class FItem {
    private String ticker;
    private String name;

    public FItem(String ticker, String name) {
        this.ticker = ticker;
        this.name = name;
    }

    public String getTicker() {
        return ticker;
    }

    public String getName() {
        return name;
    }
}
