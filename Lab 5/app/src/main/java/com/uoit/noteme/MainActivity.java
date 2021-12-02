package com.uoit.noteme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListAdapter.OnNoteListener {

    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int REQUEST_CODE_EDIT_NOTE = 2;
    public static final String EXPORT_FILENAME = "notes.json";
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

        ImageView imageExportNotes = findViewById(R.id.imageExportNotes);

        imageExportNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    exportAllNotes();
                    Toast.makeText(MainActivity.this, "JSON exported", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, "Could not export", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "Could not export", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
//                exportAllNotes();
            }
        });

        //Get all the note models
        notes = notesDatabase.getAllNotes();

        recyclerView = findViewById(R.id.notesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        listAdapter = new ListAdapter(MainActivity.this, notes, this);
        recyclerView.setAdapter(listAdapter);

        //searchbar f
        EditText searchBar = findViewById(R.id.inputSearch);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                searchNotes(searchBar.getText().toString());
            }
        });
    }

    private void searchNotes(String noteTitle){

        ArrayList<NotesModel> searchedNotes = new ArrayList<>();
        notes = notesDatabase.getAllNotes();
        if (!noteTitle.isEmpty()){
            for (NotesModel n : notes){
                if (n.getTitle().toLowerCase().contains(noteTitle.toLowerCase())){
                    searchedNotes.add(n);
                }
            }
            notes = searchedNotes;
            listAdapter.updateAdapter(notes);
        }
        else {
            notes = notesDatabase.getAllNotes();
            listAdapter.updateAdapter(notes);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_NOTE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
                notesData = (NotesModel) data.getExtras().getSerializable("note");
                System.out.println("NEW NOTE " + notesData.toString());
                notesDatabase.addNote(notesData);
            }
        } else if (requestCode == REQUEST_CODE_EDIT_NOTE) {
            if (resultCode == Activity.RESULT_OK) {
                notesData = (NotesModel) data.getExtras().getSerializable("note");
                System.out.println("Edited Note: " + notesData.toString());

                String action = (String) data.getExtras().get("action");

                if (action.equalsIgnoreCase("edit")) {
                    notesDatabase.editNote(notesData);
                } else if (action.equalsIgnoreCase("delete")) {
                    notesDatabase.deleteNote(notesData);
                }
            }
        }

        //updates notes arraylist and update list adapter
        notes = notesDatabase.getAllNotes();
        listAdapter.updateAdapter(notes);
    }

    @Override
    public void onNoteClick(int position) {
        NotesModel note = notes.get(position);
        System.out.println(note.toString());
        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE);
    }

    private void exportAllNotes() throws IOException, JSONException {
        ArrayList<NotesModel> notesToExport = notesDatabase.getAllNotes();

        JSONArray arrayToExport = new JSONArray();
        for (NotesModel note: notesToExport) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("title", note.getTitle());
                obj.put("subtitle", note.getSubtitle());
                obj.put("body", note.getBody());
                obj.put("img", note.getImg());
                obj.put("url", note.getNoteURL());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            arrayToExport.put(obj);
        }

//        FileOutputStream outputStream = getApplicationContext().openFileOutput(EXPORT_FILENAME, Context.MODE_PRIVATE);
//        outputStream.write(arrayToExport.toString(4).getBytes());
//        outputStream.close();

        File file = new File(getApplicationContext().getFilesDir(), EXPORT_FILENAME);
        try {
            FileWriter writer = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(arrayToExport.toString(4));
//            Toast.makeText(MainActivity.this, "JSON exported", Toast.LENGTH_SHORT).show();
            bufferedWriter.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}