package com.uoit.noteme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.Toolbar;

public class CreateNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

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

                String colour = colourGroup(v);

                if (titleValue.matches("")) {
                    titleText.setError("Title is empty, a title is required to save");
                } else {
                    NotesModel notesModel = new NotesModel(1, titleValue, subtitleValue, noteBody, colour);
                    System.out.println(notesModel.toString());
                }
            }
        });
    }

    public String colourGroup(View view) {
        RadioGroup colourGroup = (RadioGroup) findViewById(R.id.colourGroup);
        int selectedColour = colourGroup.getCheckedRadioButtonId();

        RadioButton selectedButton = (RadioButton) findViewById(selectedColour);

        return selectedButton.getText().toString();

    }
}