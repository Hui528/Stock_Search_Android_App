package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;

public class NewsDialog extends DialogFragment{
    private News news;

    public NewsDialog(News news) {
        this.news = news;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_news, container, false);

        TextView srcTextView = view.findViewById(R.id.dialog_source);
        srcTextView.setText(news.get_source());
        TextView dateTextView = view.findViewById(R.id.datetime);
        long unixtime = news.get_datetime();
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMM dd, yyyy");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-7"));
        String date = sdf.format(unixtime*1000L);
        dateTextView.setText(date);
        TextView titleTextView = view.findViewById(R.id.dialog_title);
        titleTextView.setText(news.get_headline());
        TextView summaryTextView = view.findViewById(R.id.dialog_summary);
        summaryTextView.setText(news.get_summary());

        ImageView chromeImgView = view.findViewById(R.id.dialog_share_chrome);
        ImageView twitterImgView = view.findViewById(R.id.dialog_share_twitter);
        ImageView facebookImgView = view.findViewById(R.id.dialog_share_facebook);

        chromeImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri url = Uri.parse(news.get_url());
                Intent intent = new Intent(Intent.ACTION_VIEW, url);
                startActivity(intent);
            }
        });

        twitterImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String twitterUrl = "https://twitter.com/intent/tweet?text=" + news.get_headline() + "&url=" + news.get_url();
                Uri url = Uri.parse(twitterUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, url);
                startActivity(intent);
            }
        });

        facebookImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String facebookUrl = "https://www.facebook.com/sharer/sharer.php?u=" + news.get_url() + "&amp;src=sdkpreparse";
                Uri url = Uri.parse(facebookUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, url);
                startActivity(intent);
            }
        });
        return view;
    }

}
