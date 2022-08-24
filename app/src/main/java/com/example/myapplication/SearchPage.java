package com.example.myapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.VolleyError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.highsoft.highcharts.common.hichartsclasses.HIChart;
import com.highsoft.highcharts.common.hichartsclasses.HIColumn;
import com.highsoft.highcharts.common.hichartsclasses.HICondition;
import com.highsoft.highcharts.common.hichartsclasses.HILabel;
import com.highsoft.highcharts.common.hichartsclasses.HILine;
import com.highsoft.highcharts.common.hichartsclasses.HIOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIPlotOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIResponsive;
import com.highsoft.highcharts.common.hichartsclasses.HIRules;
import com.highsoft.highcharts.common.hichartsclasses.HISeries;
import com.highsoft.highcharts.common.hichartsclasses.HITitle;
import com.squareup.picasso.Picasso;
import com.highsoft.highcharts.core.*;
import com.highsoft.highcharts.common.*;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class SearchPage extends AppCompatActivity {
    private final String HOST = "https://hw8-backend-server.wl.r.appspot.com";
    private String autocompleteUrl = HOST + "/autocomplete/";
    private String descriptionUrl = HOST + "/description/";
    private String hourlyDataUrl = HOST + "/hourlyData/";
    private String hisDataUrl = HOST + "/hisData/";
    private String latestPriceUrl = HOST + "/latestPrice/";
    private String newsUrl = HOST + "/news/";
    private String socialUrl = HOST + "/social/";
    private String trendsUrl = HOST + "/trends/";
    private String earningUrl = HOST + "/earning/";
    private String peersUrl = HOST + "/peers/";

    private LinearLayout progressbar_search_layout;
    private LinearLayout show_search_layout;

    private NestedScrollView nestedScrollView;
    private AtomicInteger requestsCounter;
    private ViewPager2 viewPager;

    private boolean containTicker;

    public String ticker = "AAPL";
    public RequestQueue requestQueue;
    public ArrayList<Autocomplete> autocompleteItems;
    public Description description;
    public long latest;
    public HistoricalData hourlyData;
    public HistoricalData hisData;
    public LatestPrice latestPrice;
    public ArrayList<News> newsArray;
    public Social social;
    public ArrayList<Trends> trendsArray;
    public ArrayList<Earning> earningArray;
    public ArrayList<String> peersArray;

    private double[] hourly_data;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private RecyclerView peersRecyclerView;
    private PeersAdapter peersAdapter;

//    private HIChartView chartView;
//    private HIOptions options;

//    public SearchPage(String ticker) {
//        this.ticker = ticker;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.search_result);

        progressbar_search_layout = findViewById(R.id.progressbar_search_layout);
        show_search_layout = findViewById(R.id.show_search_layout);
        progressbar_search_layout.setVisibility(View.VISIBLE);
        show_search_layout.setVisibility(View.GONE);


        Intent intent = getIntent();
        if (!intent.hasExtra("ticker")) {
            throw new RuntimeException("do not have extra ticker");
        }
        ticker = intent.getStringExtra("ticker");

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        View v = getLayoutInflater().inflate(R.layout.activity_main,null);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);



//        nestedScrollView = (NestedScrollView) findViewById(R.id.search_details);
//        setPagerAdapter();
        handleIntent(getIntent());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void handleIntent(Intent intent) {
//        ticker = intent.getStringExtra(SearchManager.QUERY).toUpperCase();
        requestQueue = Volley.newRequestQueue(this);
        requestsCounter = new AtomicInteger(3);
//        setPagerAdapter();
        displayHeader();
        fetchDescription();
        fetchLatestPrice();
        fetchPeersArray();
        fetchSocial();
        displayTrendsData();
        displayEarningData();
        fetchNewsArray();
//        setPeerClick();
//        displayHisData();
//        fetchHourlyData();
//        setPagerAdapter();


//        requestQueue.start();
//        displaySummary();

//        requestQueue.addRequestEventListener(req -> {
//            requestsCounter.decrementAndGet();
//            if(requestsCounter.get() == 0) {
//                displaySummary();
//            }
//        });
//        requestQueue.addRequestEventListener(new RequestQueue.RequestEventListener() {
//            @Override
//            public void onRequestEvent(Request<?> request, int event) {
//
//            }
//        });

    }

    private void displaySearchPage() {
        progressbar_search_layout.setVisibility(View.GONE);
        show_search_layout.setVisibility(View.VISIBLE);
    }

    private void fetchAutocomplete() {
        autocompleteItems = new ArrayList<>();
        System.out.println("autocomplete entered");
        Log.i("enter", "autocomplete");
        JsonArrayRequest AutocompleteData = new JsonArrayRequest(
                Request.Method.GET,
                this.autocompleteUrl + this.ticker,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                String displaySymbol = item.getString("displaySymbol");
                                String description = item.getString("description");
                                autocompleteItems.add(new Autocomplete(displaySymbol, description));
                            }
                            System.out.println("autocomplete finished");
                            System.out.println(autocompleteItems.get(0).getDescription());
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError statesError) {
                        Log.i("Error", "getAutocomplete() statesError: " + statesError.toString());
                    }
                }
        );
        this.requestQueue.add(AutocompleteData);
    }


    private void fetchDescription() {
        JsonObjectRequest DescriptionData = new JsonObjectRequest(
                Request.Method.GET,
                this.descriptionUrl + this.ticker,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String ticker = response.getString("ticker");
                            String company_name = response.getString("company_name");
                            String exchange_code = response.getString("exchange_code");
                            String ipo_start_date = response.getString("ipo_start_date");
                            String industry = response.getString("industry");
                            String weburl = response.getString("weburl");
                            String logo = response.getString("logo");
                            description = new Description(ticker, company_name, exchange_code, ipo_start_date, industry, weburl, logo);
                            displaySummary();
                            displayAbout();
//                            displaySearchPage();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError statesError) {
                        Log.i("Error", "getDescription() statesError: " + statesError.toString());
                    }
                }
        );
        this.requestQueue.add(DescriptionData);
    }

    private void fetchHourlyData() {
        long latest = latestPrice.get_current_timestamp();
        JsonObjectRequest HourlyData = new JsonObjectRequest(
                Request.Method.GET,
                this.hourlyDataUrl + ticker + "/" + latest,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray close_prices = response.getJSONArray("close_prices");
                            JSONArray high_prices = response.getJSONArray("high_prices");
                            JSONArray low_prices = response.getJSONArray("low_prices");
                            JSONArray open_prices = response.getJSONArray("open_prices");
                            String status = response.getString("status");
                            JSONArray timestamp = response.getJSONArray("timestamp");
                            JSONArray volume = response.getJSONArray("volume");
                            int length = timestamp.length();
                            double close_prices_array[] = new double[length];
                            double high_prices_array[] = new double[length];
                            double low_prices_array[] = new double[length];
                            double open_prices_array[] = new double[length];
                            long timestamp_array[] = new long[length];
                            long volume_array[] = new long[length];

                            for(int i = 0; i < length; i++) {
                                close_prices_array[i] = close_prices.getDouble(i);
                                high_prices_array[i] = high_prices.getDouble(i);
                                low_prices_array[i] = low_prices.getDouble(i);
                                open_prices_array[i] = open_prices.getDouble(i);
                                timestamp_array[i] = timestamp.getLong(i);
                                volume_array[i] = volume.getLong(i);
                            }
                            hourlyData = new HistoricalData(close_prices_array, high_prices_array, low_prices_array, open_prices_array, status, timestamp_array, volume_array);
//                            hourly_data = close_prices_array;
//                            displayHourlyData();
//                            displayHisData();
//                            setPagerAdapter();

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError statesError) {
                        Log.i("Error", "getHourlyData() statesError: " + statesError.toString());
                    }
                }
        );
        this.requestQueue.add(HourlyData);
    }

    private void fetchHisData() {
        JsonObjectRequest HisData = new JsonObjectRequest(
                Request.Method.GET,
                this.hisDataUrl + this.ticker,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray close_prices = response.getJSONArray("close_prices");
                            JSONArray high_prices = response.getJSONArray("high_prices");
                            JSONArray low_prices = response.getJSONArray("low_prices");
                            JSONArray open_prices = response.getJSONArray("open_prices");
                            String status = response.getString("status");
                            JSONArray timestamp = response.getJSONArray("timestamp");
                            JSONArray volume = response.getJSONArray("volume");
                            int length = timestamp.length();
                            double close_prices_array[] = new double[length];
                            double high_prices_array[] = new double[length];
                            double low_prices_array[] = new double[length];
                            double open_prices_array[] = new double[length];
                            long timestamp_array[] = new long[length];
                            long volume_array[] = new long[length];

                            for(int i = 0; i < length; i++) {
                                close_prices_array[i] = close_prices.getDouble(i);
                                high_prices_array[i] = high_prices.getDouble(i);
                                low_prices_array[i] = low_prices.getDouble(i);
                                open_prices_array[i] = open_prices.getDouble(i);
                                timestamp_array[i] = timestamp.getLong(i);
                                volume_array[i] = volume.getLong(i);
                            }
                            hisData = new HistoricalData(close_prices_array, high_prices_array, low_prices_array, open_prices_array, status, timestamp_array, volume_array);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError statesError) {
                        Log.i("Error", "getHisData() statesError: " + statesError.toString());
                    }
                }
        );
        this.requestQueue.add(HisData);
    }

    private void fetchLatestPrice() {
        JsonObjectRequest LatestPriceData = new JsonObjectRequest(
                Request.Method.GET,
                this.latestPriceUrl + this.ticker,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String ticker = response.getString("ticker");
                            double last_price = response.getDouble("last_price");
                            double change = response.getDouble("change");
                            double change_percentage = response.getDouble("change_percentage");
                            long current_timestamp = response.getLong("current_timestamp");
                            double high_price = response.getDouble("high_price");
                            double low_price = response.getDouble("low_price");
                            double open_price = response.getDouble("open_price");
                            double prev_close = response.getDouble("prev_close");
                            latestPrice = new LatestPrice(ticker, last_price, change, change_percentage, current_timestamp, high_price, low_price, open_price, prev_close);
                            displayPrice();
                            displayStats();
                            displayPortfolio();
                            displaySearchPage();
                            setPagerAdapter();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError statesError) {
                        Log.i("Error", "getLatestPrice() statesError: " + statesError.toString());
                    }
                }
        );
        this.requestQueue.add(LatestPriceData);
    }

    private void fetchNewsArray() {
        newsArray = new ArrayList<>();
        JsonArrayRequest NewsArrayData = new JsonArrayRequest(
                Request.Method.GET,
                this.newsUrl + this.ticker,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                String headline = item.getString("headline");
                                String image = item.getString("image");
                                String url = item.getString("url");
                                String source = item.getString("source");
                                long datetime = item.getLong("datetime");
                                String summary = item.getString("summary");
                                News news = new News(headline, image, url, source, datetime, summary);
                                newsArray.add(news);
                            }
                            displayNews();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError statesError) {
                        Log.i("Error", "getNewsArray() statesError: " + statesError.toString());
                    }
                }
        );
        this.requestQueue.add(NewsArrayData);
    }

    private void fetchSocial() {
        JsonObjectRequest SocialData = new JsonObjectRequest(
                Request.Method.GET,
                this.socialUrl + this.ticker,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int reddit_mention = response.getJSONObject("reddit").getInt("mention");
                            int reddit_positiveMention = response.getJSONObject("reddit").getInt("positiveMention");
                            int reddit_negativeMention = response.getJSONObject("reddit").getInt("negativeMention");
                            int twitter_mention = response.getJSONObject("twitter").getInt("mention");
                            int twitter_positiveMention = response.getJSONObject("twitter").getInt("positiveMention");
                            int twitter_negativeMention = response.getJSONObject("twitter").getInt("negativeMention");
                            social = new Social(reddit_mention, reddit_positiveMention, reddit_negativeMention, twitter_mention, twitter_positiveMention, twitter_negativeMention);
                            displayInsights();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError statesError) {
                        Log.i("Error", "getSocial() statesError: " + statesError.toString());
                    }
                }
        );
        this.requestQueue.add(SocialData);
    }

    private void fetchTrendsArray() {
        trendsArray = new ArrayList<>();
        JsonArrayRequest TrendsArrayData = new JsonArrayRequest(
                Request.Method.GET,
                this.trendsUrl + this.ticker,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                String period = item.getString("period");
                                int buy = item.getInt("buy");
                                int hold = item.getInt("hold");
                                int sell = item.getInt("sell");
                                int strongBuy = item.getInt("strongBuy");
                                int strongSell = item.getInt("strongSell");
                                trendsArray.add(new Trends(period, buy, hold, sell, strongBuy, strongSell));
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError statesError) {
                        Log.i("Error", "getAutocomplete() statesError: " + statesError.toString());
                    }
                }
        );
        this.requestQueue.add(TrendsArrayData);
    }

    private void fetchEarningArray() {
        earningArray = new ArrayList<>();
        JsonArrayRequest EarningArrayData = new JsonArrayRequest(
                Request.Method.GET,
                this.earningUrl + this.ticker,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                double actual = item.getDouble("actual");
                                double estimate = item.getDouble("estimate");
                                String period = item.getString("period");
                                double surprise = item.getDouble("surprise");
                                earningArray.add(new Earning(actual, estimate, period, surprise));
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError statesError) {
                        Log.i("Error", "getEarningArray() statesError: " + statesError.toString());
                    }
                }
        );
        this.requestQueue.add(EarningArrayData);
    }

    private void fetchPeersArray() {
        peersArray = new ArrayList<>();
        JsonObjectRequest PeersArrayData = new JsonObjectRequest(
                Request.Method.GET,
                this.peersUrl + this.ticker,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray peers = response.getJSONArray("peers");
                            if (peers != null) {
                                for(int i = 0; i < peers.length(); i++) {
                                    peersArray.add(peers.getString(i));
                                }
                            }
                            displayPeers();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError statesError) {
                        Log.i("Error", "getPeersArray() statesError: " + statesError.toString());
                    }
                }
        );
        this.requestQueue.add(PeersArrayData);
    }

    public ArrayList<Autocomplete> getAutocompleteItems() {
        ArrayList<Autocomplete> autocompleteList = new ArrayList<>();
        autocompleteList.add(new Autocomplete("AAA", "aaa"));
        autocompleteList.add(new Autocomplete("AA", "aa"));
        if(this.autocompleteItems != null) {
            return this.autocompleteItems;
        }
        return autocompleteList;
    }

    private void displaySummary() {
        View summaryView = (View) findViewById(R.id.summary);
        ImageView logoView = (ImageView) summaryView.findViewById(R.id.logo);
        TextView tickerTextView = (TextView) summaryView.findViewById(R.id.ticker);
        TextView company_nameTextView = (TextView) summaryView.findViewById(R.id.company_name);
        TextView nameTextView = findViewById(R.id.table_company);


        Picasso.get().load(description.getLogo()).into(logoView);
        tickerTextView.setText(description.getTicker());
        company_nameTextView.setText(description.getCompany_name());
        nameTextView.setText(description.getCompany_name());

    }

    private void displayPrice() {
//        View summaryView = (View) findViewById(R.id.summary);
        TextView current_priceTextView = findViewById(R.id.current_price);
        TextView change_nameTextView = findViewById(R.id.change_and_percent);
        ImageView change_icon_down = findViewById(R.id.change_icon_down);
        ImageView change_icon_up = findViewById(R.id.change_icon_up);

//          current_priceTextView.setText("$");
        current_priceTextView.setText("$" + String.format("%.2f", latestPrice.get_last_price()));
//        change_nameTextView.setText("$$$$" + latestPrice.get_change() + " (" + latestPrice.get_change_percentage() + "%)");
        change_nameTextView.setText("$" + String.format("%.2f", latestPrice.get_change()) + " (" + String.format("%.2f", latestPrice.get_change_percentage()) + "%)");
        if(latestPrice.get_change() > 0) {
            change_nameTextView.setTextColor(Color.parseColor("#39D800"));
            change_icon_down.setVisibility(View.GONE);
            change_icon_up.setVisibility(View.VISIBLE);
        }
        if(latestPrice.get_change() < 0) {
            change_nameTextView.setTextColor(Color.parseColor("#E60505"));
            change_icon_down.setVisibility(View.VISIBLE);
            change_icon_up.setVisibility(View.GONE);
        }
    }

    private void displayHourlyData() {
//        HIChartView chartView = findViewById(R.id.hc);
//
//        HIOptions options = new HIOptions();
//
//        HIChart chart = new HIChart();
//        chart.setType("line");
//        options.setChart(chart);
//
//        HITitle title = new HITitle();
//        title.setText(ticker + "Hourly Price Variation");
//        options.setTitle(title);
//
//        HILine line1 = new HILine();
//        System.out.println("hourlyData.get_close_prices() data: " + hourlyData.get_close_prices()[0]);
//        line1.setData(new ArrayList<>(Arrays.asList(49.9, 71.5, 106.4, 129.2, 144, 176, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4)));
//
//        options.setSeries(new ArrayList<>(Arrays.asList(line1)));
//
//        chartView.setOptions(options);
        WebView webView = (WebView) findViewById(R.id.hc_1);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.clearCache(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/hourlyData.html"); // for first chart
//        webView.loadUrl("file:///android_asset/hisDataCharts.html"); // for second chart
//        webView.loadUrl("file:///android_asset/trendsCharts.html"); // for third(trends) chart
//        webView.loadUrl("file:///android_asset/earningCharts.html"); // for third(trends) chart
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                first charts: for hourly data
                String dataUrl = hourlyDataUrl + ticker + "/" + latestPrice.get_current_timestamp();
                view.loadUrl("javascript:fetchHourlyData('" + ticker + "', '" + dataUrl + "')");

//                if(latestPrice.get_change() > 0) {
//                    view.loadUrl("javascript:fetchHourlyData('" + ticker + "', '" + "#39D80" + "', '" + dataUrl + "')");
//                }
//                else {
//                    view.loadUrl("javascript:fetchHourlyData('" + ticker + "', '" + "#E60505" + "', '" + dataUrl + "')");
//                }

//                second charts: for historical data
//                String dataUrl = hisDataUrl + ticker;
//                view.loadUrl("javascript:fetchHisData('" + ticker + "', '" + dataUrl + "')");

//                third charts: for trends data
//                String dataUrl = trendsUrl + ticker;
//                view.loadUrl("javascript:fetchTrendsData('" + ticker + "', '" + dataUrl + "')");

//                forth charts: for earning data
//                String dataUrl = earningUrl + ticker;
//                view.loadUrl("javascript:fetchEarningData('" + ticker + "', '" + dataUrl + "')");
            }
        });
    }

    private void displayHisData() {
        WebView webView = (WebView) findViewById(R.id.hc_2);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.clearCache(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/hisDataCharts.html"); // for second chart
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

//                second charts: for historical data
                String dataUrl = hisDataUrl + ticker;
                view.loadUrl("javascript:fetchHisData('" + ticker + "', '" + dataUrl + "')");
            }
        });
    }

    private void setPagerAdapter() {
        LinearLayout viewpager1 = findViewById(R.id.viewpager1);
        viewPager = viewpager1.findViewById(R.id.pager2);
        viewPager.setAdapter(new MyFragmentPagerAdapter(this, ticker, latestPrice.get_current_timestamp()));

        TabLayout sampleTabLayout = findViewById(R.id.sampleTabLayout);
        new TabLayoutMediator(
                sampleTabLayout,
                viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                        tab.setText("Tab");
                        if(position == 0) {
                            tab.setIcon(R.drawable.chart_line);
                        }
                        if(position == 1) {
                            tab.setIcon(R.drawable.clock_time_three);
                        }
                    }
                }
        ).attach();
    }

    private void displayTrendsData() {
        WebView webView = (WebView) findViewById(R.id.hc_trends);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.clearCache(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/trendsCharts.html"); // for third(trends) chart
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                third charts: for trends data
                String dataUrl = trendsUrl + ticker;
                view.loadUrl("javascript:fetchTrendsData('" + ticker + "', '" + dataUrl + "')");
            }
        });
    }

    private void displayEarningData() {
        WebView webView = (WebView) findViewById(R.id.hc_earning);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.clearCache(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/earningCharts.html");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                forth charts: for earning data
                String dataUrl = earningUrl + ticker;
                view.loadUrl("javascript:fetchEarningData('" + ticker + "', '" + dataUrl + "')");
            }
        });
    }

    private void displayNews() {
        newsRecyclerView = findViewById(R.id.news_recyclerView);
        newsAdapter = new NewsAdapter(newsArray);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        newsRecyclerView.setLayoutManager(layoutManager);
        newsRecyclerView.setAdapter(newsAdapter);
    }

    private void displayStats() {
        TextView opTextView = findViewById(R.id.open_price);
        TextView lpTextView = findViewById(R.id.low_price);
        TextView hpTextView = findViewById(R.id.high_price);
        TextView pcTextView = findViewById(R.id.prev_close);
        opTextView.setText("$" + String.format("%.2f", latestPrice.get_open_price()));
        lpTextView.setText("$" + String.format("%.2f", latestPrice.get_low_price()));
        hpTextView.setText("$" + String.format("%.2f", latestPrice.get_high_price()));
        pcTextView.setText("$" + String.format("%.2f", latestPrice.get_prev_close()));
    }

    private void displayAbout() {
        TextView ipoTextView = findViewById(R.id.start_date);
        TextView industryTextView = findViewById(R.id.industry);
        TextView webTextView = findViewById(R.id.webpage);

        ipoTextView.setText(description.getIpo_start_date());
        industryTextView.setText(description.getIndustry());
//        String text = "<a href='" + description.getWeburl() + "'> Google </a>";
        webTextView.setText(description.getWeburl());
//        webTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void displayPeers() {
        peersRecyclerView = findViewById(R.id.peers_recyclerView);
        peersAdapter = new PeersAdapter(this.getApplicationContext(), peersArray);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        peersRecyclerView.setLayoutManager(layoutManager);
        peersRecyclerView.setAdapter(peersAdapter);

    }

//    private void setPeerClick() {
//        TextView peerTV = findViewById(R.id.peer_item);
//        peerTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String str = peerTV.getText().toString();
//                str = str.replace(str.substring(str.length()-1), "");
//                Intent intent = new Intent(view.getContext(), MainActivity.class);
//                intent.putExtra("ticker", str);
//                view.getContext().startActivity(intent);
//                System.out.println("clicked peer from SP");
//            }
//        });
//    }

    private void displayInsights() {
//        TextView nameTextView = findViewById(R.id.table_company);
        TextView trTextView = findViewById(R.id.total_reddit);
        TextView prTextView = findViewById(R.id.positive_reddit);
        TextView nrTextView = findViewById(R.id.negative_reddit);
        TextView ttTextView = findViewById(R.id.total_twitter);
        TextView ptTextView = findViewById(R.id.positive_twitter);
        TextView ntTextView = findViewById(R.id.negative_twitter);

//        nameTextView.setText(description.getCompany_name());
        trTextView.setText(social.get_reddit_mention() + "");
        prTextView.setText(social.get_reddit_positiveMention() + "");
        nrTextView.setText(social.get_reddit_negativeMention() + "");
        ttTextView.setText(social.get_twitter_mention() + "");
        ptTextView.setText(social.get_twitter_positiveMention() + "");
        ntTextView.setText(social.get_twitter_negativeMention() + "");
    }

    private void displayPortfolio() {
        TextView sharesTextView = findViewById(R.id.shares);
        TextView avgTextView = findViewById(R.id.avg);
        TextView totalTextView = findViewById(R.id.total);
        TextView changeTextView = findViewById(R.id.change);
        TextView marketValueTextView = findViewById(R.id.market);
        Button tradeBtn = findViewById(R.id.trade_button);

        SharedPreferences sp = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String[] tradeInfo = sp.getString(ticker, "0, 0.00").split(",");
        int shares = Integer.parseInt(tradeInfo[0]);
        double totalCost = Double.parseDouble(tradeInfo[1]);
        sharesTextView.setText(shares + "");
        totalTextView.setText("$" + String.format("%.2f", totalCost));
        if(shares == 0) {
            avgTextView.setText("$0.00");

        }
        else {
            double avg = totalCost / shares;
            avgTextView.setText("$" + String.format("%.2f", avg));
        }
        double marketValue = latestPrice.get_last_price() * shares;
        marketValueTextView.setText("$" + String.format("%.2f", marketValue));
        double change = marketValue - totalCost;
        changeTextView.setText("$" + String.format("%.2f", change));

//        double leftMoney = Double.parseDouble(sp.getString("leftMoney", "25000.00"));


        tradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TradeDialog tradeDialog = new TradeDialog(description.getCompany_name(), description.getTicker(), latestPrice.get_last_price(), sharesTextView, avgTextView, totalTextView, changeTextView, marketValueTextView, view.getContext());
//                Toast.makeText(view.getContext(), "tradeBtn clicked", Toast.LENGTH_SHORT).show();
                tradeDialog.show(((AppCompatActivity) view.getContext()).getSupportFragmentManager(), "TradeDialog");
            }
        });
    }

    private void displayHeader() {
        ImageView headerBackView = findViewById(R.id.header_back);
        TextView header_tv = findViewById(R.id.header_ticker);
        ImageView headerStarView = findViewById(R.id.header_star);
        SharedPreferences sp = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sp.edit();
        String[] favList = sp.getString("favorites", "").split(",");
        ArrayList<String> favArray = new ArrayList<>(Arrays.asList(favList));

        if(favArray.contains(ticker)) {
            headerStarView.setImageResource(R.drawable.star);
            containTicker = true;
        }
        else {
            containTicker = false;
        }
        header_tv.setText(ticker);
        headerBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        headerStarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(containTicker) {
                    Toast.makeText(SearchPage.this.getApplicationContext(), ticker + " is removed from favorites", Toast.LENGTH_SHORT).show();
                    headerStarView.setImageResource(R.drawable.star_outline);
                    String[] update_favList = sp.getString("favorites", "").split(",");
//                    ArrayList<String> update_favArray = new ArrayList<>(Arrays.asList(update_favList));
                    String new_favList = "";
                    for(int i = 0; i < update_favList.length; i++) {
                        if(!update_favList[i].equals(ticker))
                        new_favList += update_favList[i] + ",";
                    }
                    myEdit.putString("favorites", new_favList);
                    myEdit.apply();
                }
                else {
                    Toast.makeText(SearchPage.this.getApplicationContext(), ticker + " is added to favorites", Toast.LENGTH_SHORT).show();
                    headerStarView.setImageResource(R.drawable.star);
                    String crt_fav_list = sp.getString("favorites", "");
                    myEdit.putString("favorites", crt_fav_list + ticker + ",");
                    myEdit.apply();

                }
                containTicker = !containTicker;
            }
        });
    }

}
