package com.example.final_test;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private ListView commentsListView;
    private EditText commentInput;
    private Button submitCommentButton;
    private List<Comment> comments;
    private String newsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        commentsListView = findViewById(R.id.comments_list_view);
        commentInput = findViewById(R.id.comment_input);
        submitCommentButton = findViewById(R.id.submit_comment_button);

        // 获取新闻 ID
        newsTitle = getIntent().getStringExtra("news_title");

        // 初始化评论列表
        comments = new ArrayList<>();
        loadComments(); // 加载评论

        submitCommentButton.setOnClickListener(v -> submitComment());
    }

    private void loadComments() {
        // 从数据库加载评论
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.getCommentsByNewsId(newsTitle);

        List<Comment> commentsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Integer username_idx = cursor.getColumnIndex("username");
            Integer commentText_idx = cursor.getColumnIndex("content");
            Integer time_idx = cursor.getColumnIndex("timestamp");

            String username = cursor.getString(username_idx); // 假设你有 username 列
            String commentText = cursor.getString(commentText_idx);
            String timestamp = cursor.getString(time_idx);
            commentsList.add(new Comment(username, timestamp, commentText));
        }
        cursor.close();

        if (commentsList.isEmpty()) {
            // 列表为空
            commentsListView.setVisibility(View.GONE);
            Toast.makeText(this, "空空如也~", Toast.LENGTH_SHORT).show();
        } else {
            // 列表不为空，可以处理评论
            // 例如，更新 ListView 或 RecyclerView
            commentsListView.setVisibility(View.VISIBLE);
            CommentAdapter adapter = new CommentAdapter(this, commentsList);
            commentsListView.setAdapter(adapter);
        }

    }

    private void submitComment() {
        String commentText = commentInput.getText().toString();
        if (commentText.isEmpty()) {
            Toast.makeText(this, "评论不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取当前用户的 ID
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String username = preferences.getString("username", "用户");

        // 将评论保存到数据库
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.addComment(newsTitle, username, commentText); // 添加评论到数据库

        // 清空输入框
        commentInput.setText("");
        // 重新加载评论
        loadComments();
    }
}