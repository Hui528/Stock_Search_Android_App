package com.example.myapplication;

public class PItem {
    private String ticker;
    private int shares;

    public PItem(String ticker, int shares) {
        this.ticker = ticker;
        this.shares = shares;
    }

    public String getTicker() {
        return ticker;
    }

    public int getShares() {
        return shares;
    }
}
