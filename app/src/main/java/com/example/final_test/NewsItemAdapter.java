package com.example.final_test;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsItemAdapter extends ArrayAdapter<NewsItem> {
    private Context context;
    private List<NewsItem> list;

    public NewsItemAdapter(@NonNull Context context, List<NewsItem> list) {
        super(context, android.R.layout.simple_list_item_1, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.row_view_for_news, parent, false);
        }
        NewsItem newsItem =list.get(position);
        String title = newsItem.getTitle();
        String summarize = newsItem.getSummarize();
        String imgSrc = newsItem.getImgSrc();
        TextView tle = v.findViewById(R.id.news_title);
        TextView sum = v.findViewById(R.id.news_summarize);
        ImageView iv = v.findViewById(R.id.row_view_news_img);
        tle.setText(title);
        sum.setText(summarize);
        if(!imgSrc.isEmpty())
            Glide.with(context).load(Uri.parse(imgSrc)).into(iv);
        else
            iv.setVisibility(View.GONE);

        return v;
    }
}
