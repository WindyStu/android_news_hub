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
    public static final String COLUMN_DETAILS = "details";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NEWS + " (" +
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
        db.execSQL("DELETE FROM " + TABLE_NEWS);
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        onCreate(db);
    }

    public Cursor getAllNews() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NEWS, null, null, null, null, null, null);
    }
} 