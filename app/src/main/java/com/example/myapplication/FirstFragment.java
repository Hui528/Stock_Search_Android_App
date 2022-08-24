package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

public class FirstFragment extends Fragment {
    private String ticker;
    private long current_timestamp;

    public FirstFragment(String ticker, long current_timestamp) {
        this.ticker = ticker;
        this.current_timestamp = current_timestamp;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_first, container, false);
        WebView webView = (WebView) root.findViewById(R.id.hc_1);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.clearCache(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/hourlyData.html"); // for first chart
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                first charts: for hourly data
                String dataUrl = "https://hw8-backend-server.wl.r.appspot.com/hourlyData/" + ticker + "/" + current_timestamp;
                view.loadUrl("javascript:fetchHourlyData('" + ticker + "', '" + dataUrl + "')");
            }
        });
        return root;
    }
}
