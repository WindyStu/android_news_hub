package com.example.final_test;

public class Comment {
    private String username;
    private String timestamp; // 评论时间
    private String content; // 评论内容

    public Comment(String username, String timestamp, String content) {
        this.username = username;
        this.timestamp = timestamp;
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }
}
