package com.example.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "allinformation";
    public static final String TABLE_INFORMATIONONBUYS = "informationonbuys";

    public static final String KEY_ID = "_id";
    public static final String KEY_INSTR= "instr";
    public static final String KEY_VID = "type_of_tool";
    public static final String KEY_CENA ="price";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_INFORMATIONONBUYS + "(" + KEY_ID
                + " integer primary key," + KEY_INSTR + " text," + KEY_VID + " text,"+ KEY_CENA + " text" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_INFORMATIONONBUYS);

        onCreate(db);

    }
}