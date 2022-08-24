package com.example.myapplication;

public class Description {
    private String ticker;
    private String company_name;
    private String exchange_code;
    private String ipo_start_date;
    private String industry;
    private String weburl;
    private String logo;

    public Description(String ticker, String company_name, String exchange_code, String ipo_start_date, String industry, String weburl, String logo) {
        this.ticker = ticker;
        this.company_name = company_name;
        this.exchange_code = exchange_code;
        this.ipo_start_date = ipo_start_date;
        this.industry = industry;
        this.weburl = weburl;
        this.logo = logo;
    }

    public String getTicker() {
        return ticker;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getExchange_code() {
        return exchange_code;
    }

    public String getIpo_start_date() {
        return ipo_start_date;
    }

    public String getIndustry() {
        return industry;
    }

    public String getWeburl() {
        return weburl;
    }

    public String getLogo() {
        return logo;
    }
}
