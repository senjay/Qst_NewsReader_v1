package com.example.qst.qst_newsreader_v1.Sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//**
// * Author: 钱苏涛.
// * Date:2018/12/27.
// * Description:
//**
public class Sqlhelp extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="NEWS";
    public static final String KEY_ID="_id";
    public static final String KEY_UNIQUEID="uniqueid";
    public static final String KEY_DATE="date";
    public static final String KEY_TITLE="title";
    public static final String KEY_URL="url";
    public static final String KEY_PICURL="picurl";
    public static final String KEY_CONTENT="content";
    public static final String KEY_CATEGORY="category";
    public static final String KEY_AUTHOR="author";
    SQLiteDatabase mydb;
    public Sqlhelp(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql=String.format("create table if not exists %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT, %s TEXT, %s TEXT,%s TEXT,%s TEXT, %s TEXT, %s TEXT, %s TEXT)"
                ,DATABASE_NAME,KEY_ID,KEY_UNIQUEID,KEY_TITLE,KEY_DATE,KEY_CATEGORY,KEY_AUTHOR,KEY_URL,KEY_PICURL,KEY_CONTENT);
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}