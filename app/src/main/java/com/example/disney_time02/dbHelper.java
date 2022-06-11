package com.example.disney_time02;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dbHelper extends SQLiteOpenHelper {
   private static final String DATABASE_NAME = "userDB";
   public static final String TABLE_NAME = "users";
   private static final int DATABASE_VERSION = 1;
   public static final String USERNAME = "username";
   public static final String PASSWORD = "password";
   private String SQLCREATE = "create table " + TABLE_NAME +
           "(" + USERNAME + " text, " + PASSWORD + " text);";
   private String SQLDELETE = "drop table if exists " + TABLE_NAME;
   public dbHelper(@Nullable Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      db.execSQL(SQLCREATE);
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL(SQLDELETE);
      onCreate(db);
   }
}
