package com.uoit.noteme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.widget.Button;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListAdapter.OnNoteListener {

    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int REQUEST_CODE_EDIT_NOTE = 2;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;

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

        this.imageView = (ImageView)this.findViewById(R.id.imageView1);
        Button photoButton = (Button) this.findViewById(R.id.button1);
        photoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
    }


        imageAddNoteMain.setOnClickListener((v) -> {
            startActivityForResult(new Intent(getApplicationContext(), CreateNoteActivity.class), REQUEST_CODE_ADD_NOTE);
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

    @Override
   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
       super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       if (requestCode == MY_CAMERA_PERMISSION_CODE)
       {
           if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
           {
               Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
               Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
               startActivityForResult(cameraIntent, CAMERA_REQUEST);
           }
           else
           {
               Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
           }
       }
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

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }

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
}
