package com.example.final_test;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "news.db";
    private static final int DATABASE_VERSION = 2;

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

    private static final String TABLE_USERS_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT" +
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
                    COLUMN_DETAILS + " TEXT" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE_FAVORITES_CREATE);
        db.execSQL(TABLE_USERS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    public Cursor getAllNews() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NEWS, null, null, null, null, null, null);
    }

    public Cursor getAllFavorites() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_FAVORITES, null, null, null, null, null, null);
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
} 