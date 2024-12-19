package com.example.final_test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    NewsViewModel newsViewModel;
    String url = "https://www.gz.chinanews.com.cn/jjgz/zwrs/index.shtml";
    public static final String KEY_DATA = "key_data";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_main_news);

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
//        //登录测试
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putBoolean("is_logged_in", false); // 重置登录状态
//        editor.apply();

        boolean isLoggedIn = preferences.getBoolean("is_logged_in", false);
        if (!isLoggedIn) {
            startActivity(new Intent(this, LoginActivity.class));
            finish(); // 结束当前 Activity
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);

        // Observe the news list
//        newsViewModel.getNewsList().observe(this, newsItems -> {
//            if (newsItems != null && !newsItems.isEmpty()) {
//                // Load HomeFragment only when news items are available
//                loadFragment(new HomeFragment());
//            } else {
//                // Optionally handle the case where there are no news items
//                Toast.makeText(MainActivity.this, "没有新闻可显示", Toast.LENGTH_SHORT).show();
//            }
//        });
        loadFragment(new HomeFragment());
        gainWebPage(url);
        // Load the default fragment
//        loadFragment(new HomeFragment()); // This can be removed if you want to wait for data

        // Set up bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    loadFragment(new HomeFragment());
                    return true;
                case R.id.nav_search:
                    loadFragment(new SearchFragment());
                    return true;
                case R.id.nav_favorites:
                    loadFragment(new FavoritesFragment());
                    return true;
                case R.id.nav_me:
                    loadFragment(new MyFragment());
                    return true;
            }
            return false;
        });

        // Start loading news data


        MutableLiveData<String> erroMessage = newsViewModel.getErrMessage();
        erroMessage.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    private void gainWebPage(String url) {
        new NewsItemGetThread(newsViewModel, url, this).start();
    }

    public static NewsItem getIntentBookItem(Intent intent) {
        Bundle b = intent.getExtras();
        return (NewsItem) b.getSerializable(KEY_DATA);
    }

    private void loadNewsFromDatabase() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.getAllNews();

        while (cursor.moveToNext()) {
            int titleIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE);
            int summarizeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SUMMARIZE);
            int hrefIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_HREF);
            int imgSrcIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMG_SRC);

            if (titleIndex >= 0 && summarizeIndex >= 0 && hrefIndex >= 0 && imgSrcIndex >= 0) {
                String title = cursor.getString(titleIndex);
                String summarize = cursor.getString(summarizeIndex);
                String href = cursor.getString(hrefIndex);
                String imgSrc = cursor.getString(imgSrcIndex);

                // Print news item details to console
                System.out.println("Title: " + title);
                System.out.println("Summarize: " + summarize);
                System.out.println("Href: " + href);
                System.out.println("ImgSrc: " + imgSrc);
            }
        }
        cursor.close();
    }
}
