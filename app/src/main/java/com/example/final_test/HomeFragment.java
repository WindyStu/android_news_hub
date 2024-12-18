package com.example.final_test;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private NewsViewModel newsViewModel;
    private ListView lv;
    private ProgressBar progressBar;
    public static final String KEY_DATA = "key_data";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        lv = view.findViewById(R.id.news_lv);
        progressBar = view.findViewById(R.id.progress_bar);

        newsViewModel = new ViewModelProvider(requireActivity()).get(NewsViewModel.class);

        newsViewModel.getNewsList().observe(getViewLifecycleOwner(), newsItems -> {
            if (newsItems != null && !newsItems.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                NewsItemAdapter adapter = new NewsItemAdapter(getContext(), newsItems);
                lv.setVisibility(View.VISIBLE);
                lv.setAdapter(adapter);
            } else {
                // Optionally handle the case where there are no news items
                lv.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "没有新闻可显示", Toast.LENGTH_SHORT).show();
            }
        });
//        loadNewsFromDatabase();
//        不知道为什么没有监听到点击事件
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                NewsItem item = (NewsItem) parent.getItemAtPosition(position);
//                Intent intent = new Intent(getActivity(), NewsItemActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(KEY_DATA, item);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });

        return view;
    }

//    private void loadNewsFromDatabase() {
//        progressBar.setVisibility(View.VISIBLE);
//        lv.setVisibility(View.GONE);
//        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
//        Cursor cursor = dbHelper.getAllNews();
//
//        List<NewsItem> newsItems = new ArrayList<>();
//        while (cursor.moveToNext()) {
//            int titleIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE);
//            int summarizeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SUMMARIZE);
//            int hrefIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_HREF);
//            int imgSrcIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMG_SRC);
//            int detailsIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DETAILS);
//            int isFavoriteIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IS_FAVORITE);
//
//            if (titleIndex >= 0 && summarizeIndex >= 0 && hrefIndex >= 0 && imgSrcIndex >= 0 && detailsIndex >= 0 && isFavoriteIndex >= 0) {
//                String title = cursor.getString(titleIndex);
//                String summarize = cursor.getString(summarizeIndex);
//                String href = cursor.getString(hrefIndex);
//                String imgSrc = cursor.getString(imgSrcIndex);
//                String details = cursor.getString(detailsIndex);
//                boolean isFavorite = cursor.getInt(isFavoriteIndex) == 1;
//
//                newsItems.add(new NewsItem(title, summarize, href, imgSrc, details, isFavorite));
//            }
//        }
//        cursor.close();
//        progressBar.setVisibility(View.GONE);
//        if (!newsItems.isEmpty()) {
//            NewsItemAdapter adapter = new NewsItemAdapter(getContext(), newsItems);
//            lv.setVisibility(View.VISIBLE);
//            lv.setAdapter(adapter);
//        } else {
//            Toast.makeText(getContext(), "没有新闻可显示", Toast.LENGTH_SHORT).show();
//        }
//    }
} 