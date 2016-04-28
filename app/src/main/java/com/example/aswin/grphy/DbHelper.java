package com.example.aswin.grphy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SandStorm";
    public static final Integer DATABASE_VERSION = 1;

    public static final String ROW_ID = "_id";
    public static final String XAXIS = "xaxis";
    public static final String YAXIS = "yaxis";


    public static final String URL_TAG_NAME = "url_tag_name";
    public static final String URL_VALUE = "url_value";

    public static final String TABLE_GRAPH_ACTIVITY = "graph_activity";
    public static final String TABLE_LIST_ACTIVITY = "list_activity";

    public static final String CREATE_TABLE_GRAPH_ACTIVITY = "CREATE TABLE "+ TABLE_GRAPH_ACTIVITY
            +"( "+ ROW_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + XAXIS + " TEXT NOT NULL, "
            + YAXIS + " TEXT NOT NULL);";

    public static final String CREATE_TABLE_LIST_ACTIVITY = "CREATE TABLE "+ TABLE_LIST_ACTIVITY
            +"( "+ ROW_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + URL_TAG_NAME + " TEXT NOT NULL, "
            + URL_VALUE + " TEXT NOT NULL);";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_GRAPH_ACTIVITY);
        db.execSQL(CREATE_TABLE_LIST_ACTIVITY);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_GRAPH_ACTIVITY);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_LIST_ACTIVITY);
        onCreate(db);
    }
}
