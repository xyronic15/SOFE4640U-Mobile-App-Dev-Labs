package com.uoit.noteme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.widget.ImageView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD_NOTE = 1;
    NotesModel notesData;
    NotesDatabase notesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notesDatabase = new NotesDatabase(this);

        ImageView imageAddNoteMain = findViewById(R.id.imageAddNoteMain);

        imageAddNoteMain.setOnClickListener((v) -> {
            startActivityForResult(new Intent(getApplicationContext(), CreateNoteActivity.class), REQUEST_CODE_ADD_NOTE);

        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_NOTE) {
            if (resultCode == Activity.RESULT_OK) {
                notesData = (NotesModel) data.getExtras().getSerializable("note");
            }
        }

        System.out.println("NEW NOTE " + notesData.toString());
        notesDatabase.addNote(notesData);

    }
}