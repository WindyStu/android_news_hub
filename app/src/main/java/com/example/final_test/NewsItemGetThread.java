package com.example.final_test;

import androidx.lifecycle.MutableLiveData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class NewsItemGetThread extends Thread{
    private NewsViewModel viewModel;
    private String url;
    private Context context;

    public NewsItemGetThread(NewsViewModel viewModel, String url, Context context) {
        this.viewModel = viewModel;
        this.url = url;
        this.context = context;
    }

    @Override
    public void run() {
        MutableLiveData<List<NewsItem>> newList = viewModel.getNewsList();
        List<NewsItem> list = newList.getValue();
        list.clear();
        MutableLiveData<String> errorMessage = viewModel.getErrMessage();

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .timeout(10000000).get();
//            Document doc = Jsoup.connect(url).timeout(10000).get();
            Elements newHTML = doc.select("div.listClass");

            for (Element news : newHTML) {
                String title = news.select("h3.h4 a").first().text();
                String href = news.select("h3.h4 a").attr("href");
                String imgSrc = news.select("a.thumb img").attr("src");
                String summarize = news.select("div.post--content p").first().text();

                String details = fetchDetailsFromHref(href);
                String detailsPinyin = PinyinUtils.toPinyin(details);

                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_TITLE, title);
                values.put(DatabaseHelper.COLUMN_SUMMARIZE, summarize);
                values.put(DatabaseHelper.COLUMN_HREF, href);
                values.put(DatabaseHelper.COLUMN_IMG_SRC, imgSrc);
                values.put(DatabaseHelper.COLUMN_DETAILS, details);
                values.put(DatabaseHelper.COLUMN_DETAILS_PINYIN, detailsPinyin);
                values.put(DatabaseHelper.COLUMN_IS_FAVORITE, 0);

                db.insert(DatabaseHelper.TABLE_NEWS, null, values);

                list.add(new NewsItem(title, summarize, href, imgSrc, details, false));
            }
            newList.postValue(list);
        } catch (IOException e) {
            e.printStackTrace();
            errorMessage.postValue(e.toString());
        } finally {
            db.close();
        }
    }

    private String fetchDetailsFromHref(String href) {
        try {
            Document detailDoc = Jsoup.connect(href).timeout(10000).get();
            Elements paragraphs = detailDoc.select("div.article p");
            StringBuilder details = new StringBuilder();
            for (Element paragraph : paragraphs) {
                details.append(paragraph.text()).append("\n");
            }
            return details.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
