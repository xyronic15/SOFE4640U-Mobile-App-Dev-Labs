package com.uoit.noteme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class EditNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        Bundle bundle = getIntent().getExtras();
        NotesModel note = (NotesModel) bundle.getSerializable("note");

        EditText titleText = (EditText) findViewById(R.id.inputNoteTitle);
        titleText.setText(note.getTitle());

        EditText subtitleText = (EditText) findViewById(R.id.inputNoteSubTitle);
        subtitleText.setText(note.getSubtitle());

        EditText noteText = (EditText) findViewById(R.id.inputNote);
        noteText.setText(note.getBody());

        ImageView deleteAction = (ImageView) findViewById(R.id.deleteButton);
        deleteAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(EditNoteActivity.this);
                alertBuilder.setMessage("Are you sure you want to delete this note?")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText titleText = (EditText) findViewById(R.id.inputNoteTitle);
                                    String titleValue = titleText.getText().toString();

                                    EditText subtitleText = (EditText) findViewById(R.id.inputNoteSubTitle);
                                    String subtitleValue = subtitleText.getText().toString();

                                    EditText noteText = (EditText) findViewById(R.id.inputNote);
                                    String noteBody = noteText.getText().toString();

                                    NotesModel notesModel = null;

                                    String colour = colourGroup(v);
                                    notesModel = new NotesModel(note.getId(), titleValue, subtitleValue, noteBody, colour);

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("note", notesModel);
                                    intent.putExtra("action", "Delete");
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alert = alertBuilder.create();
                alert.show();
            }
        });

        ImageView imageDone = findViewById(R.id.imageSave2);
        imageDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText titleText = (EditText) findViewById(R.id.inputNoteTitle);
                String titleValue = titleText.getText().toString();

                EditText subtitleText = (EditText) findViewById(R.id.inputNoteSubTitle);
                String subtitleValue = subtitleText.getText().toString();

                EditText noteText = (EditText) findViewById(R.id.inputNote);
                String noteBody = noteText.getText().toString();

                NotesModel notesModel = null;

                String colour = colourGroup(v);

                if (titleValue.matches("")) {
                    titleText.setError("Title is empty, a title is required to save");
                } else {
                    try {
                        notesModel = new NotesModel(note.getId(), titleValue, subtitleValue, noteBody, colour);
                    } catch (Exception e) {
                        Toast.makeText(EditNoteActivity.this, "Error saving note", Toast.LENGTH_SHORT).show();
                    }
                    //boolean insertStatus = notesDatabase.addNote(notesModel);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("note", notesModel);
                    intent.putExtra("action", "Edit");
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
    public String colourGroup(View view) {
        RadioButton selectedButton;
        String colour = "none";
        RadioGroup colourGroup = (RadioGroup) findViewById(R.id.colourGroup);
        int selectedColour = colourGroup.getCheckedRadioButtonId();

        if (colourGroup.getCheckedRadioButtonId() == -1) {
            return "#fafafa";
        }

        selectedButton = (RadioButton) findViewById(selectedColour);

        switch(selectedButton.getText().toString()) {
            case("Red"):
                colour = "#ff7961";
                break;
            case("Blue"):
                colour = "#6ec6ff";
                break;
            case("Green"):
                colour = "#80e27e";
                break;
            case("Yellow"):
                colour = "#ffff72";
                break;
        }

        return colour;
    }
}