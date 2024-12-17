package com.example.final_test;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private NewsViewModel newsViewModel;
    private ListView lv;
    private EditText searchInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        lv = view.findViewById(R.id.search_lv);
        searchInput = view.findViewById(R.id.search_input);

        newsViewModel = new ViewModelProvider(requireActivity()).get(NewsViewModel.class);

        // Add text change listener to the search input
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchNews(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void searchNews(String query) {
        // Convert query to pinyin
        String pinyinQuery = PinyinUtils.toPinyin(query);

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        Cursor cursor = dbHelper.searchNewsByPinyin(pinyinQuery);

        List<NewsItem> searchResults = new ArrayList<>();
        while (cursor.moveToNext()) {
            int titleIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE);
            int summarizeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SUMMARIZE);
            int hrefIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_HREF);
            int imgSrcIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMG_SRC);
            int detailsIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DETAILS);

            String title = titleIndex >= 0 ? cursor.getString(titleIndex) : "";
            String summarize = summarizeIndex >= 0 ? cursor.getString(summarizeIndex) : "";
            String href = hrefIndex >= 0 ? cursor.getString(hrefIndex) : "";
            String imgSrc = imgSrcIndex >= 0 ? cursor.getString(imgSrcIndex) : "";
            String details = detailsIndex >= 0 ? cursor.getString(detailsIndex) : "";


            searchResults.add(new NewsItem(title, summarize, href, imgSrc, details, false));
        }
        cursor.close();

        if (!searchResults.isEmpty()) {
            NewsItemAdapter adapter = new NewsItemAdapter(getContext(), searchResults);
            lv.setAdapter(adapter);
        } else {
            Toast.makeText(getContext(), "没有找到相关新闻", Toast.LENGTH_SHORT).show();
        }
    }
} 