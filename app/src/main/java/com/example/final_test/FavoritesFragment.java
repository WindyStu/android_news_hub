package com.example.final_test;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    private NewsViewModel newsViewModel;
    private String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        ListView lv = view.findViewById(R.id.favorites_lv);
        SharedPreferences preferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        username = preferences.getString("username", "用户");

        newsViewModel = new ViewModelProvider(requireActivity()).get(NewsViewModel.class);
        loadFavoritesFromDatabase(lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsItem item = (NewsItem) parent.getItemAtPosition(position);
                // Handle item click, e.g., open detail view
            }
        });

        return view;
    }

    private void loadFavoritesFromDatabase(ListView lv) {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        Cursor cursor = dbHelper.getAllFavorites(username); // Create this method in DatabaseHelper

        List<NewsItem> favoritesList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int titleIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE);
            int summarizeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SUMMARIZE);
            int hrefIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_HREF);
            int imgSrcIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMG_SRC);
            int detailsIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DETAILS);

            if (titleIndex >= 0 && summarizeIndex >= 0 && hrefIndex >= 0 && imgSrcIndex >= 0 && detailsIndex >= 0) {
                String title = cursor.getString(titleIndex);
                String summarize = cursor.getString(summarizeIndex);
                String href = cursor.getString(hrefIndex);
                String imgSrc = cursor.getString(imgSrcIndex);
                String details = cursor.getString(detailsIndex);

                favoritesList.add(new NewsItem(title, summarize, href, imgSrc, details, true));
            }
        }
        cursor.close();

        if (!favoritesList.isEmpty()) {
            NewsItemAdapter adapter = new NewsItemAdapter(getContext(), favoritesList);
            lv.setAdapter(adapter);
        } else {
            Toast.makeText(getContext(), "没有收藏的新闻", Toast.LENGTH_SHORT).show();
        }
    }
} 