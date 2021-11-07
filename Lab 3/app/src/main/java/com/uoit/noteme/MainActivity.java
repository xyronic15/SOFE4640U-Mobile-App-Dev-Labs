package com.uoit.noteme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListAdapter.OnNoteListener {

    public static final int REQUEST_CODE_ADD_NOTE = 1;
    NotesModel notesData;
    ArrayList<NotesModel> notes;
    NotesDatabase notesDatabase;
    ListAdapter listAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notesDatabase = new NotesDatabase(this);

        ImageView imageAddNoteMain = findViewById(R.id.imageAddNoteMain);

        imageAddNoteMain.setOnClickListener((v) -> {
            startActivityForResult(new Intent(getApplicationContext(), CreateNoteActivity.class), REQUEST_CODE_ADD_NOTE);

        });

        //Get all the notemodels
        notes = notesDatabase.getAllNotes();

        recyclerView = findViewById(R.id.notesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        listAdapter = new ListAdapter(MainActivity.this, notes, this);
        recyclerView.setAdapter(listAdapter);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_NOTE) {
            if (resultCode == Activity.RESULT_OK) {
                notesData = (NotesModel) data.getExtras().getSerializable("note");
                System.out.println("NEW NOTE " + notesData.toString());
                notesDatabase.addNote(notesData);

                //updates notes arraylist and update list adapter
                notes = notesDatabase.getAllNotes();
                listAdapter.updateAdapter(notes);
            }
        }

    }

    @Override
    public void onNoteClick(int position) {
        System.out.println("Clicked: " + position);
    }
}