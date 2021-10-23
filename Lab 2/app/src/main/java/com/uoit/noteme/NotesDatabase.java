package com.uoit.noteme;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NotesDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NotesApp.db";
    public static final String NOTES_TABLE = "notes";
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
            "create table " + NOTES_TABLE+ " (" +
            NOTES_ID + " integer primary key autoincrement, " +
            NOTES_TITLE + " text not null, " +
            NOTES_SUBTITLE + " text, " +
            NOTES_BODY + " text, " +
            NOTES_COLOUR + " text" +
            ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + NOTES_TABLE);
        onCreate(db);
    }

    public boolean addNote(NotesModel note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTES_TITLE, note.getTitle());
        values.put(NOTES_SUBTITLE, note.getSubtitle());
        values.put(NOTES_BODY, note.getBody());
        values.put(NOTES_COLOUR, note.getColour());

        long insertStatus = db.insert(NOTES_TABLE, null, values);

        if (insertStatus == -1) {
            return false;
        }
        return true;
    }

}
