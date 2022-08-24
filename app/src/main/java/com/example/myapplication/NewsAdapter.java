package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private ArrayList<News> newsList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View newsView;
        private ImageView imageView;
        private TextView sourceView;
        private TextView timelineView;
        private TextView titleView;

        public ViewHolder(View newsView) {
            super(newsView);
            this.newsView = newsView;
            this.imageView = newsView.findViewById(R.id.news_img);
            this.sourceView = newsView.findViewById(R.id.source);
            this.timelineView = newsView.findViewById(R.id.timeline);
            this.titleView = newsView.findViewById(R.id.news_title);
        }

        public View getNewsView() {
            return newsView;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getSourceView() {
            return sourceView;
        }

        public TextView getTimelineView() {
            return timelineView;
        }

        public TextView getTitleView() {
            return titleView;
        }
    }

    public NewsAdapter(ArrayList<News> newsList) {
        this.newsList = newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View newsView;
        if(viewType == 0) {
            newsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_first, parent, false);
        }
        else {
            newsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_others, parent, false);
        }
        ViewHolder newsViewHolder = new ViewHolder(newsView);
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    News news = newsList.get(position);
    holder.getSourceView().setText(news.get_source());
    holder.getTimelineView().setText(news.get_datetime_to_today() + " hours ago");
    holder.getTitleView().setText(news.get_headline());
    Picasso.get().load(news.get_image()).into(holder.getImageView());
    Context newsContext = holder.newsView.getContext();

    holder.newsView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            NewsDialog newsDialog = NewsDialog.createDialogObject(news);
            NewsDialog newsDialog = new NewsDialog(news);
            newsDialog.show(((AppCompatActivity) newsContext).getSupportFragmentManager(), "NewsDialog");
        }
    });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
