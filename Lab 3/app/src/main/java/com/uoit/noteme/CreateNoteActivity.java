package com.uoit.noteme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class CreateNoteActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 1;
    private static final int SELECT_IMG_CODE = 2;
    private ImageView noteImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //NotesDatabase notesDatabase = new NotesDatabase(this);

        setContentView(R.layout.activity_create_note);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        noteImg = findViewById(R.id.noteImg);

        findViewById(R.id.addImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            CreateNoteActivity.this,
                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION_CODE
                    );
                } else {
                    selectImage();
                }
            }
        });

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

                BitmapDrawable imgDrawable = ((BitmapDrawable) noteImg.getDrawable());
                byte[] img;
                if (imgDrawable != null){
                    Bitmap imgBitmap = imgDrawable.getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imgBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
                    img = stream.toByteArray();
                } else {
                    img = new byte[0];
                }


                NotesModel notesModel = null;

                String colour = colourGroup(v);

                if (titleValue.matches("")) {
                    titleText.setError("Title is empty, a title is required to save");
                } else {
                    try {
                        notesModel = new NotesModel(-1, titleValue, subtitleValue, noteBody, colour, img);
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
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_IMG_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage();
            } else{
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMG_CODE && resultCode == RESULT_OK){
            if (data != null){
                Uri selectedImgUri = data.getData();
                if (selectedImgUri != null){
                    try {

                        InputStream inStream = getContentResolver().openInputStream(selectedImgUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inStream);
                        noteImg.setImageBitmap(bitmap);
                        noteImg.setVisibility(View.VISIBLE);
                    }catch (Exception e){
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}