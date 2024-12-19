package com.example.final_test;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        NewsItem newsItem = list.get(position);
        String title = newsItem.getTitle();
        String summarize = newsItem.getSummarize();
        String imgSrc = newsItem.getImgSrc();
        TextView tle = v.findViewById(R.id.news_title);
        TextView sum = v.findViewById(R.id.news_summarize);
        ImageView iv = v.findViewById(R.id.row_view_news_img);

        tle.setText(title);
        sum.setText(summarize);
        if (!imgSrc.isEmpty())
            Glide.with(context).load(Uri.parse(imgSrc)).into(iv);
        else
            iv.setVisibility(View.GONE);

        // Update UI based on favorite status
        if (newsItem.isFavorite())
            v.setBackgroundColor(context.getResources().getColor(R.color.light_gray));

        // Set long click listener for the entire news item view
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Toggle favorite status
                toggleFavorite(newsItem);
                // Update UI immediately after toggling
                notifyDataSetChanged();
                return true; // Return true to indicate the event was handled
            }
        });

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, NewsItemActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(HomeFragment.KEY_DATA, newsItem);
//                intent.putExtras(bundle);
//                context.startActivity(intent);
                Intent intent = new Intent(context, NewsDetailsActivity.class);
                intent.putExtra("title", newsItem.getTitle());
                intent.putExtra("content", newsItem.getDetails());
                intent.putExtra("imgUrl", newsItem.getImgSrc());
                context.startActivity(intent);
            }
        });

        return v;
    }

    private void toggleFavorite(NewsItem newsItem) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Check current favorite status
        boolean isCurrentlyFavorite = newsItem.isFavorite();

        // Toggle the favorite status
        boolean newFavoriteStatus = !isCurrentlyFavorite;
        newsItem.setFavorite(newFavoriteStatus);

        SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String username = preferences.getString("username", "用户");

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, newsItem.getTitle());
        values.put(DatabaseHelper.COLUMN_SUMMARIZE, newsItem.getSummarize());
        values.put(DatabaseHelper.COLUMN_HREF, newsItem.getHref());
        values.put(DatabaseHelper.COLUMN_IMG_SRC, newsItem.getImgSrc());
        values.put(DatabaseHelper.COLUMN_DETAILS, newsItem.getDetails());
        values.put(DatabaseHelper.COLUMN_USERNAME, username);

        if (newFavoriteStatus) {
            // Insert into favorites table
            db.insert(DatabaseHelper.TABLE_FAVORITES, null, values);
            Toast.makeText(context, "已收藏", Toast.LENGTH_SHORT).show();
        } else {
            // Remove from favorites table
            db.delete(DatabaseHelper.TABLE_FAVORITES, DatabaseHelper.COLUMN_HREF + " = ?", new String[]{newsItem.getHref()});
            Toast.makeText(context, "取消收藏", Toast.LENGTH_SHORT).show();
        }

        // Update the news table
        values.clear();
        values.put(DatabaseHelper.COLUMN_IS_FAVORITE, newFavoriteStatus ? 1 : 0);
        db.update(DatabaseHelper.TABLE_NEWS, values, DatabaseHelper.COLUMN_HREF + " = ?", new String[]{newsItem.getHref()});

        db.close();

        // Log the action
        System.out.println("Toggled favorite for: " + newsItem.getTitle() + " to " + newsItem.isFavorite());
    }
}
