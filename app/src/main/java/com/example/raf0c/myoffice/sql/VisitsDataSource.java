package com.example.raf0c.myoffice.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.raf0c.myoffice.model.Visits;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raf0c on 27/09/15.
 */
public class VisitsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_OFFICE, MySQLiteHelper.COLUMN_ENTRY,
                                    MySQLiteHelper.COLUMN_EXIT, MySQLiteHelper.COLUMN_DURATION};

    public VisitsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Visits insertVisit(String office, Long entry, Long exit, Long duration) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_OFFICE, office);
        values.put(MySQLiteHelper.COLUMN_ENTRY,entry);
        values.put(MySQLiteHelper.COLUMN_EXIT,exit);
        values.put(MySQLiteHelper.COLUMN_DURATION, duration);

        long insertId = database.insert(MySQLiteHelper.TABLE_VISITS, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_VISITS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Visits newVisit = cursorToVisit(cursor);
        cursor.close();
        return newVisit;
    }

    public void deleteVisit(Visits visits) {
        long id = visits.getId();
        System.out.println("Visit deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_VISITS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Visits> getAllVisits() {
        List<Visits> visits = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_VISITS, allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            Visits visit = cursorToVisit(cursor);
            visits.add(visit);
            cursor.moveToNext();

        }

        // make sure to close the cursor
        cursor.close();
        return visits;
    }

    private Visits cursorToVisit(Cursor cursor) {
        Visits visit = new Visits(cursor.getLong(0),cursor.getString(1),cursor.getLong(2),cursor.getLong(3),cursor.getLong(4));
        return visit;
    }


}

