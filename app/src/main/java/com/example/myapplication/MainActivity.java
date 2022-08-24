package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.highsoft.highcharts.core.*;
import com.highsoft.highcharts.common.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    private LinearLayout main_details_layout;
    private LinearLayout progressbar_layout;

    private SearchPage searchPage;
    private ArrayList<Autocomplete> autocompleteList = new ArrayList<>();
    public RequestQueue requestQueue;
    private RecyclerView pRecyclerview;
    private RecyclerView fRecyclerview;
    private PSectionItemAdapter pSectionItemAdapter;
    private FSectionItemAdapter fSectionItemAdapter;
    private AutocompleteAdapter adapter;
    private Toolbar toolbar;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        fetchAutocomplete("AAPL");
        adapter = new AutocompleteAdapter(this,
                R.layout.autocomplete_item, autocompleteList);
//        searchView.setAdapter(adapter);
        setCashBalance();
        setDate();
        setFooter();



//        requestQueue = Volley.newRequestQueue(this);
//
//        AutoCompleteTextView tickerText = findViewById(R.id.autoComplete);
//
//
////        AutocompleteAdapter adapter = new AutocompleteAdapter(this,
////                R.layout.autocomplete_item, autocompleteList);
////        tickerText.setAdapter(adapter);
////        tickerText.addTextChangedListener(new TextWatcher() {
////
////            @Override
////            public void onTextChanged(CharSequence s, int start, int before, int count) {
////                String input = tickerText.getText().toString();
////                adapter.clear();
////                System.out.println("clear all adapter data");
////                fetchAutocomplete(input);
////                for(Autocomplete item : autocompleteList) {
////                    adapter.add(item);
////                }
////                adapter.getFilter().filter(tickerText.getText(), null);
////            }
////
////            @Override
////            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
////
////            }
////
////            @Override
////            public void afterTextChanged(Editable s) {
////
////            }
////        });
////        System.out.println(input);
////        searchPage = new SearchPage(input);
//        fetchAutocomplete("AAPL");
////        ArrayList<Autocomplete> autocompleteList = searchPage.getAutocompleteItems();
////        ArrayList<Autocomplete> autocompleteList = new ArrayList<>();
////        autocompleteList.add(new Autocomplete("AAA", "aaa"));
////        autocompleteList.add(new Autocomplete("AA", "aa"));
//        AutocompleteAdapter adapter = new AutocompleteAdapter(this,
//                R.layout.autocomplete_item, autocompleteList);
////        ListView listView = findViewById(R.id.autocomplete_list);
////        listView.setAdapter(adapter);
//        tickerText.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        main_details_layout = findViewById(R.id.main_details);
        progressbar_layout = findViewById(R.id.progressbar_layout);
        progressbar_layout.setVisibility(View.VISIBLE);
        main_details_layout.setVisibility(View.GONE);

        requestQueue = Volley.newRequestQueue(this);

        setCashBalance();

        SharedPreferences sp = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String[] portfolioInfo = sp.getString("portfolioList", "").split(",");
        ArrayList<PItem> portfolioArray = new ArrayList<>();
        for(int i = 0; i < portfolioInfo.length; i++) {
            String crt_ticker = portfolioInfo[i];
            String[] tradeInfo = sp.getString(crt_ticker, "0, 0.00").split(",");
            int shares = Integer.parseInt(tradeInfo[0]);
            if(shares != 0) {
                portfolioArray.add(new PItem(crt_ticker, shares));
            }
        }

        if(!sp.getString("portfolioList", "").equals("")) {
            pRecyclerview = findViewById(R.id.recyclerview);
            pSectionItemAdapter = new PSectionItemAdapter(portfolioArray, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            pRecyclerview.setLayoutManager(layoutManager);
            pRecyclerview.setAdapter(pSectionItemAdapter);

            enableMoved();
        }


        String[] favoritesInfo = sp.getString("favorites", "").split(",");
        ArrayList<String> favoritesArray = new ArrayList<>(Arrays.asList(favoritesInfo));

        if(!sp.getString("favorites", "").equals("")) {
            fRecyclerview = findViewById(R.id.fav_recyclerview);
            fSectionItemAdapter = new FSectionItemAdapter(favoritesArray, this);
            LinearLayoutManager layoutManager_2 = new LinearLayoutManager(this);
            fRecyclerview.setLayoutManager(layoutManager_2);
            fRecyclerview.setAdapter(fSectionItemAdapter);

            enableSwipeToDeleteAndUndo();
        }

        showMainDetails();

    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar, menu);
//        searchView = (android.widget.AutoCompleteTextView) menu.findItem(R.id.menu_search).getActionView();
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(false);

        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        autocompleteList.add(new Autocomplete("AAPL", "APPLE INC"));
        autocompleteList.add(new Autocomplete("TSLA", "TESLA INC"));
//        autocompleteList.add(new Autocomplete("DELL", "DELL TECHNOLOGIES -C"));
        autocompleteList.add(new Autocomplete("TWTR", "TWITTER INC"));


//        fetchAutocomplete("AAPL");
        adapter = new AutocompleteAdapter(this, R.layout.autocomplete_item, autocompleteList);
        searchAutoComplete.setAdapter(adapter);
        searchAutoComplete.setThreshold(1);

        AtomicBoolean itemClicked = new AtomicBoolean(false);


        searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (itemClicked.get()) {
                    itemClicked.set(false);
                } else {
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchAutoComplete.setOnItemClickListener((adapterView, view, itemIndex, id) -> {
            String formattedSearchOption = ((Autocomplete) adapterView.getItemAtPosition(itemIndex)).getDisplaySymbol();
            System.out.println("formattedSearchOption: " + formattedSearchOption);
            itemClicked.set(true);
            searchAutoComplete.setText(formattedSearchOption);
            Intent intent = new Intent(this, SearchPage.class);
            intent.putExtra("ticker", formattedSearchOption);
            startActivity(intent);
        });

////        fetchAutocomplete("AAPL");
////        AutocompleteAdapter adapter = new AutocompleteAdapter(this,
////                R.layout.autocomplete_item, autocompleteList);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                System.out.println("onQueryTextSubmit finished");
////                Intent intent = new Intent(MainActivity.this.getApplicationContext(), SearchPage.class);
////                intent.putExtra("ticker", query);
////                startActivity(intent);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                System.out.println("onQueryTextChange executed: " + s);
////                adapter = new AutocompleteAdapter(MainActivity.this.getApplicationContext(), R.layout.autocomplete_item, autocompleteList);
////                searchAutoComplete.setAdapter(adapter);
////                fetchAutocomplete(s);
//
//                SharedPreferences sp = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//                SharedPreferences.Editor myEdit = sp.edit();
//                myEdit.putString("search_ticker", searchAutoComplete.getText().toString());
//                myEdit.commit();
//
//                System.out.println("onQueryTextChange finished");
//                return true;
//            }
//        });


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(this, SearchPage.class);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(componentName);
        searchView.setSearchableInfo(searchableInfo);

//        SharedPreferences sp = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//        SharedPreferences.Editor myEdit = sp.edit();
//        myEdit.putString("search_ticker", searchAutoComplete.getText().toString());
//        myEdit.commit();

//        Intent intent = new Intent(this, SearchPage.class);
//        intent.putExtra("ticker", "TSLA");
//        startActivity(intent);
//        System.out.println(searchAutoComplete.getText().toString());
        return true;
    }

    private void fetchAutocomplete(String input) {
        autocompleteList = new ArrayList<>();
//        adapter.clear();
        String HOST = "https://hw8-backend-server.wl.r.appspot.com";
        String autocompleteUrl = HOST + "/autocomplete/";
        System.out.println("autocomplete entered");
        Log.i("enter", "autocomplete");
        JsonArrayRequest AutocompleteData = new JsonArrayRequest(
                Request.Method.GET,
                autocompleteUrl + input,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                String displaySymbol = item.getString("displaySymbol");
                                String description = item.getString("description");
                                autocompleteList.add(new Autocomplete(displaySymbol, description));
//                                adapter.setNotifyOnChange(true);
//                                adapter.add(new Autocomplete(displaySymbol, description));
                            }
                            adapter = new AutocompleteAdapter(MainActivity.this.getApplicationContext(), R.layout.autocomplete_item, autocompleteList);
//                            searchAutoComplete.setAdapter(adapter);
//                            adapter.getFilter().filter(, null);
                            showMainDetails();


                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        System.out.println("autocomplete finished");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError statesError) {
                        Log.i("Error", "getAutocomplete() statesError: " + statesError.toString());
                    }
                }
        );
        System.out.println("end of fetchAutocomplete");
        requestQueue.add(AutocompleteData);
    }

    private void setDate() {
        TextView date_tv = findViewById(R.id.main_date);
        long unixtime = Instant.now().getEpochSecond();
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMMM yyyy");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-7"));
        String date = sdf.format(unixtime*1000L);
        date_tv.setText(date);
    }

//    private void updateFavItems() {
//        SharedPreferences sp = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//        String[] favList = sp.getString("favorites", "").split(",");
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//
//            }
//        });
//    }

    private void setFooter() {
        TextView footer_tv = findViewById(R.id.footer);
        footer_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.finnhub.io/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeDeleteCallback swipeToDeleteCallback = new SwipeDeleteCallback(this, fSectionItemAdapter) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final String item = fSectionItemAdapter.getfItemList().get(position);
                fSectionItemAdapter.removeItem(position);

                ArrayList<String> updatedFavList = fSectionItemAdapter.getfItemList();
                updateFavSp(updatedFavList);
            }

            @Override
            public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
                ArrayList<String> updatedFavList = fSectionItemAdapter.getfItemList();
                updateFavSp(updatedFavList);
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(fRecyclerview);
    }



    private void updateFavSp(ArrayList<String> updatedFavList) {
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();
        String updated = "";
        for(String item : updatedFavList) {
            updated = updated + item + ",";
        }
        myEdit.putString("favorites", updated);
        myEdit.apply();
    }

    private void enableMoved() {
        PortfolioMoveCallback portfolioMoveCallback = new PortfolioMoveCallback(this, pSectionItemAdapter) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }
            @Override
            public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
                ArrayList<PItem> updatedPortList = pSectionItemAdapter.getpItemList();
                updatePortSp(updatedPortList);
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(portfolioMoveCallback);
        itemTouchhelper.attachToRecyclerView(pRecyclerview);
    }

    private void updatePortSp(ArrayList<PItem> updatedPortList) {
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();
        String updated = "";
        for(PItem item : updatedPortList) {
            updated = updated + item.getTicker() + ",";
        }
        myEdit.putString("portfolioList", updated);
        myEdit.apply();
    }

    private void showMainDetails() {
        progressbar_layout.setVisibility(View.GONE);
        main_details_layout.setVisibility(View.VISIBLE);
    }

    private void setCashBalance() {
        TextView cb_tv = findViewById(R.id.cash_balance);
        TextView nw_tv = findViewById(R.id.net_worth);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        double leftMoney = Double.parseDouble(sh.getString("leftMoney", "25000.00"));
        String lm = String.format("%.2f", leftMoney);
        cb_tv.setText("$" + lm);
        nw_tv.setText("$" + lm);
    }
}