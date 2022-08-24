package com.example.myapplication;

public class Trends {
    private String period;
    private int buy;
    private int hold;
    private int sell;
    private int strongBuy;
    private int strongSell;

    public Trends(String period, int buy, int hold, int sell, int strongBuy, int strongSell) {
        this.period = period;
        this.buy = buy;
        this.hold = hold;
        this.sell = sell;
        this.strongBuy = strongBuy;
        this.strongSell = strongSell;
    }

    public String getPeriod() {
        return this.period;
    }

    public int getBuy() {
        return this.buy;
    }

    public int getHold() {
        return this.hold;
    }

    public int getSell() {
        return this.sell;
    }

    public int getStrongBuy() {
        return this.strongBuy;
    }

    public int getStrongSell() {
        return this.strongSell;
    }
}
