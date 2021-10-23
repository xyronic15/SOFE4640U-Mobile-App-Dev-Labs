package com.uoit.noteme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NotesDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NotesApp.db";
    public static final String NOTES_TABLE_NAME = "notes";
    public static final String NOTES_ID = "id";
    public static final String NOTES_TITLE = "title";
    public static final String NOTES_SUBTITLE = "subtitle";
    public static final String NOTES_BODY = "body";
    public static final String NOTES_COLOUR = "colour";

    public NotesDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "create table notes" +
            "id integer primary key id"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
