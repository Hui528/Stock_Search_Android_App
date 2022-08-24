package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

public class SecondFragment extends Fragment {
    private String ticker;

    public SecondFragment(String ticker) {
        this.ticker = ticker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_second, container, false);
        WebView webView = (WebView) root.findViewById(R.id.hc_2);
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
                String dataUrl = "https://hw8-backend-server.wl.r.appspot.com/hisData/" + ticker;
                view.loadUrl("javascript:fetchHisData('" + ticker + "', '" + dataUrl + "')");
            }
        });
        return root;
    }
}
