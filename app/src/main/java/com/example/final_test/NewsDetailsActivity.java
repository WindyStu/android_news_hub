package com.example.final_test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide; // 使用 Glide 加载图像

public class NewsDetailsActivity extends AppCompatActivity {
    private TextView newsTitle;
    private TextView newsContent;
    private ImageView newsImage;
    private ImageView commentsIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        newsTitle = findViewById(R.id.news_title_view);
        newsContent = findViewById(R.id.news_content);
        newsImage = findViewById(R.id.news_image);
        commentsIcon = findViewById(R.id.detail_comment);



        // 获取传递的数据
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String imageUrl = intent.getStringExtra("imgUrl");

        // 设置数据到视图
        if (title != null) {
            newsTitle.setText(title);
        }
        if (content != null) {
            newsContent.setText(content);
        }
        if (imageUrl != null) {
            // 使用 Glide 加载图像
            Glide.with(this).load(imageUrl).into(newsImage);
        }

        commentsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comment_intent = new Intent(NewsDetailsActivity.this, CommentActivity.class);
                comment_intent.putExtra("news_title", title); // 假设 getId() 方法返回新闻的唯一标识
                startActivity(comment_intent);
            }
        });
    }
}
