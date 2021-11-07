package com.uoit.noteme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.Toolbar;

public class CreateNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //NotesDatabase notesDatabase = new NotesDatabase(this);

        setContentView(R.layout.activity_create_note);

        final int STORAGE_PERMISSION_CODE = 1;

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        ImageView imageDone = findViewById(R.id.imageSave);

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
                        notesModel = new NotesModel(-1, titleValue, subtitleValue, noteBody, colour);
                    } catch (Exception e) {
                        Toast.makeText(CreateNoteActivity.this, "Error saving note", Toast.LENGTH_SHORT).show();
                    }
                    //boolean insertStatus = notesDatabase.addNote(notesModel);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("note", notesModel);
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

    private void selectImage() {

    }
}