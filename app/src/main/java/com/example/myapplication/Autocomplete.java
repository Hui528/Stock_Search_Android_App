package com.example.myapplication;

public class Autocomplete {
    private String displaySymbol;
    private String description;

    public Autocomplete(String displaySymbol, String description) {
        this.displaySymbol = displaySymbol;
        this.description = description;
    }

    public String getDisplaySymbol() {
        return displaySymbol;
    }

    public String getDescription() {
        return description;
    }
}
