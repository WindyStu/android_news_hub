package com.example.final_test;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);

        MutableLiveData<List<NewsItem>> NewsList = newsViewModel.getNewsList();
        ListView lv =findViewById(R.id.news_lv);
        gainWebPage(url);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsItem item =(NewsItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, NewsItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_DATA, item);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        NewsList.observe(this, new Observer<List<NewsItem>>() {
            @Override
            public void onChanged(List<NewsItem> newsItems) {
                NewsItemAdapter adapter = new NewsItemAdapter(MainActivity.this, newsItems);
                lv.setAdapter(adapter);
            }
        });

        MutableLiveData<String > erroMessage = newsViewModel.getErrMessage();
        erroMessage.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
        loadNewsFromDatabase();
    }

    private void gainWebPage(String url) {
        new NewsItemGetThread(newsViewModel, url, this).start();
    }

    public static NewsItem getIntentBookItem(Intent intent) {
        Bundle b = intent.getExtras();
        return (NewsItem) b.getSerializable(KEY_DATA);
    }

    private void loadNewsFromDatabase() {
        /**
         * 数据库测试
         * @param none
         * @return void
         */

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.getAllNews();

        while (cursor.moveToNext()) {
            int titleIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE);
            int summarizeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SUMMARIZE);
            int hrefIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_HREF);
            int imgSrcIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMG_SRC);
            int details = cursor.getColumnIndex(DatabaseHelper.COLUMN_DETAILS);

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
                System.out.println("Details："+details);
            }
        }
        cursor.close();
    }
}
