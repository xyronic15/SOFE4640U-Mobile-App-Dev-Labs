package com.uoit.noteme;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.session.PlaybackState;

import java.util.ArrayList;
import java.util.List;

public class NotesDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NotesApp.db";
    public static final String NOTES_TABLE = "notes";
    public static final String NOTES_ID = "id";
    public static final String NOTES_TITLE = "title";
    public static final String NOTES_SUBTITLE = "subtitle";
    public static final String NOTES_BODY = "body";
    public static final String NOTES_COLOUR = "colour";
    public static final String NOTES_IMG = "image";
    public static final String NOTES_DRAW = "drawing";
    public static final String NOTES_LINK = "link";

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
            NOTES_COLOUR + " text, " +
            NOTES_IMG + " blob, " +
            NOTES_DRAW + " blob, " +
            NOTES_LINK + " text" +
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
        byte[] imgBytes = note.getImg();
        values.put(NOTES_IMG, imgBytes);
        byte[] drawingBytes = note.getDrawing();
        values.put(NOTES_DRAW, drawingBytes);
        values.put(NOTES_LINK, note.getNoteURL());

        long insertStatus = db.insert(NOTES_TABLE, null, values);

        if (insertStatus == -1) {
            return false;
        }
        return true;
    }

    public boolean editNote(NotesModel note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTES_TITLE, note.getTitle());
        values.put(NOTES_SUBTITLE, note.getSubtitle());
        values.put(NOTES_BODY, note.getBody());
        values.put(NOTES_COLOUR, note.getColour());
        byte[] imgBytes = note.getImg();
        values.put(NOTES_IMG, imgBytes);
        byte[] drawingBytes = note.getDrawing();
        values.put(NOTES_DRAW, drawingBytes);
        values.put(NOTES_LINK, note.getNoteURL());

        System.out.println("ID IS - " + note.getId());

        long updateStatus = db.update(NOTES_TABLE, values, NOTES_ID + "=?", new String[] {Integer.toString(note.getId())});

        if (updateStatus == -1) {
            return false;
        }
        return true;
    }

    public boolean deleteNote(NotesModel note) {
        SQLiteDatabase db = this.getWritableDatabase();

        long deleteStatus = db.delete(NOTES_TABLE, NOTES_ID + "=?", new String[] {Integer.toString(note.getId())});

        if (deleteStatus == -1) {
            return false;
        }
        return true;
    }

    public ArrayList<NotesModel> getAllNotes() {
        ArrayList<NotesModel> notes = new ArrayList<>();

        String query = "select * from " + NOTES_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String subtitle = cursor.getString(2);
                String body = cursor.getString(3);
                String colour = cursor.getString(4);
                byte[] img = cursor.getBlob(5);
                byte[] drawing = cursor.getBlob(6);
                String link = cursor.getString(7);

                NotesModel note = new NotesModel(id, title, subtitle, body, colour, img, drawing, link);
                notes.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notes;
    }

    public List<NotesModel> searchNotes(String searchTitle) {
        List<NotesModel> notes = new ArrayList<>();

        String query = "select * from " + NOTES_TABLE + " where " + NOTES_TITLE + "=\"" + searchTitle + "\"";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String subtitle = cursor.getString(2);
                String body = cursor.getString(3);
                String colour = cursor.getString(4);
                byte[] img = cursor.getBlob(5);
                byte[] drawing = cursor.getBlob(6);
                String link = cursor.getString(7);

                NotesModel note = new NotesModel(id, title, subtitle, body, colour, img, drawing, link);
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }

}
