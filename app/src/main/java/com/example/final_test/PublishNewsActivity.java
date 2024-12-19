package com.example.final_test;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class PublishNewsActivity extends AppCompatActivity {
    private EditText titleInput;
    private EditText contentInput;
    private Button publishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_news);
        titleInput = findViewById(R.id.title_input);
        contentInput = findViewById(R.id.content_input);
        publishButton = findViewById(R.id.publish_button);

        publishButton.setOnClickListener(v -> publishNews());
    }

    private void publishNews() {
        String title = titleInput.getText().toString();
        String content = contentInput.getText().toString();
        String href = "";
        String imgSrc = "";
        String summarize = "";
        String detailsPinyin = PinyinUtils.toPinyin(content);

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "标题和内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_SUMMARIZE, summarize);
        values.put(DatabaseHelper.COLUMN_HREF, href);
        values.put(DatabaseHelper.COLUMN_IMG_SRC, imgSrc);
        values.put(DatabaseHelper.COLUMN_DETAILS, content);
        values.put(DatabaseHelper.COLUMN_DETAILS_PINYIN, detailsPinyin);
        values.put(DatabaseHelper.COLUMN_IS_FAVORITE, 0);
        // 将新闻插入数据库
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.insert(DatabaseHelper.TABLE_NEWS, null, values);

        // 返回到首页
        finish(); // 结束当前 Activity，返回到前一个 Activity
    }
}