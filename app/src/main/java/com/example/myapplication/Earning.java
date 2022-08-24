package com.example.myapplication;

public class Earning {
    private double actual;
    private double estimate;
    private String period;
    private double surprise;

    public Earning(double actual, double estimate, String period, double surprise) {
        this.actual = actual;
        this.estimate = estimate;
        this.period = period;
        this.surprise = surprise;
    }

    public double getActual() {
        return actual;
    }

    public double getEstimate() {
        return estimate;
    }

    public String getPeriod() {
        return period;
    }

    public double getSurprise() {
        return surprise;
    }
}
