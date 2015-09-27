package com.example.raf0c.myoffice.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by raf0c on 27/09/15.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {


    public static final String TABLE_VISITS = "visits";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_OFFICE = "office";
    public static final String COLUMN_ENTRY = "entry";
    public static final String COLUMN_EXIT = "exit";
    public static final String COLUMN_DURATION = "duration";

    private static final String DATABASE_NAME = "visits.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table " + TABLE_VISITS
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_OFFICE + " text not null, "
            + COLUMN_ENTRY + " long not null, "
            + COLUMN_EXIT + " long, "
            + COLUMN_DURATION + " long );";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISITS);
        onCreate(db);
    }
}
