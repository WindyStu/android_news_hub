package com.example.final_test;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "news.db";
    private static final int DATABASE_VERSION = 11;

    public static final String TABLE_NEWS = "news";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_SUMMARIZE = "summarize";
    public static final String COLUMN_HREF = "href";
    public static final String COLUMN_IMG_SRC = "imgSrc";
    public static final String COLUMN_DETAILS_PINYIN = "details_pinyin";
    public static final String COLUMN_DETAILS = "details";
    public static final String COLUMN_IS_FAVORITE = "isFavorite";
    public static final String TABLE_FAVORITES = "favorites";
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE = "role";
    public static final String TABLE_COMMENTS = "comments";
    private static final String COLUMN_COMMENT_ID = "comment_id";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    private static final String TABLE_COMMENTS_CREATE =
            "CREATE TABLE " + TABLE_COMMENTS + " (" +
                    COLUMN_COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_CONTENT + " TEXT NOT NULL, " +
                    COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP " +
                    ");";

    private static final String TABLE_USERS_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT," +
                    COLUMN_ROLE + " TEXT"+
                    ");";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NEWS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_SUMMARIZE + " TEXT, " +
                    COLUMN_HREF + " TEXT, " +
                    COLUMN_IMG_SRC + " TEXT, " +
                    COLUMN_DETAILS + " TEXT, " +
                    COLUMN_DETAILS_PINYIN + " TEXT, " +
                    COLUMN_IS_FAVORITE + " INTEGER DEFAULT 0" +
                    ");";

    private static final String TABLE_FAVORITES_CREATE =
            "CREATE TABLE " + TABLE_FAVORITES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_SUMMARIZE + " TEXT, " +
                    COLUMN_HREF + " TEXT, " +
                    COLUMN_IMG_SRC + " TEXT, " +
                    COLUMN_DETAILS + " TEXT, " +
                    COLUMN_USERNAME + " TEXT" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE_FAVORITES_CREATE);
        db.execSQL(TABLE_USERS_CREATE);
        db.execSQL(TABLE_COMMENTS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void addComment(String newsId, String userId, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", newsId); // 新闻
        values.put("username", userId);
        values.put("content", content); // 评论内容
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        values.put("timestamp", timestamp);

        long newRowId = db.insert(TABLE_COMMENTS, null, values);
        if (newRowId == -1) {
            Log.e("DatabaseHelper", "Failed to insert comment");
        } else {
            Log.d("DatabaseHelper", "Comment added with ID: " + newRowId);
        }
    }

    public Cursor getCommentsByNewsId(String newsId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_COMMENTS, null, "title = ?", new String[]{newsId}, null, null, null);
    }


    public Cursor getAllNews() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NEWS, null, null, null, null, null, null);
    }

    public void addNews(String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);

        long newRowId = db.insert("news", null, values); // 假设你的新闻表名为 news
        if (newRowId == -1) {
            Log.e("DatabaseHelper", "Failed to insert news");
        } else {
            Log.d("DatabaseHelper", "News added with ID: " + newRowId);
        }
    }

//    public Cursor getAllFavorites() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.query(TABLE_FAVORITES, null, null, null, null, null, null);
//    }

    public Cursor getAllFavorites(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USERNAME + " LIKE ?";
        return db.query(TABLE_FAVORITES, null, selection, new String[]{username}, null, null, null);
    }

    public Cursor searchNewsByDetails(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_DETAILS + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%"};
        return db.query(TABLE_NEWS, null, selection, selectionArgs, null, null, null);
    }

    public Cursor searchNewsByPinyin(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String pinyinQuery = PinyinUtils.toPinyin(query);
        String selection = COLUMN_DETAILS_PINYIN + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + pinyinQuery + "%"};
        return db.query(TABLE_NEWS, null, selection, selectionArgs, null, null, null);
    }

    public String getRoleByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String role = null;

        // 查询语句
        String query = "SELECT role FROM " + TABLE_USERS + " WHERE username = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                Integer role_index = cursor.getColumnIndex("role");
                role = cursor.getString(role_index);
            }
            cursor.close();
        }
        return role; // 返回角色
    }
} 