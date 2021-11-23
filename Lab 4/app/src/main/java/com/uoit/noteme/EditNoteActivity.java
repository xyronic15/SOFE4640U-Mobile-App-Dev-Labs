package com.uoit.noteme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class EditNoteActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 1;
    private static final int SELECT_IMG_CODE = 2;
    private static final int CAMERA_PERMISSION_CODE = 3;
    private static final int CAPTURE_IMG_CODE = 4;
    private ImageView noteImg;
    private TextView textWebURL;
    private LinearLayout layoutWebURL;

    private AlertDialog dialogAddURL;

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

        noteImg = findViewById(R.id.noteImg);
        byte[] imgBytes = note.getImg();
        if (imgBytes.length > 0){
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgBytes, 0,imgBytes.length);
            noteImg.setImageBitmap(bitmap);
            noteImg.setVisibility(View.VISIBLE);
        }

        textWebURL = findViewById(R.id.textWebURL);
        layoutWebURL = findViewById(R.id.layoutWebURL);
        String notesURL = note.getNoteURL();
        if (!notesURL.isEmpty()){
            textWebURL.setText(notesURL);
            layoutWebURL.setVisibility(View.VISIBLE);
        }

        //set the onclicklistener for addURLLayout
        findViewById(R.id.addUrlLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWebURLDialog();
            }
        });

        findViewById(R.id.addImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            EditNoteActivity.this,
                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION_CODE
                    );
                } else {
                    selectImage();
                }
            }
        });

        findViewById(R.id.takeImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(
                            EditNoteActivity.this,
                            new String[] {Manifest.permission.CAMERA},
                            CAMERA_PERMISSION_CODE
                    );
                } else {
                    captureImage();
                }
            }
        });
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

                                    String notesURL = textWebURL.getText().toString();

                                    NotesModel notesModel = null;

                                    String colour = colourGroup(v);
                                    notesModel = new NotesModel(note.getId(), titleValue, subtitleValue, noteBody, colour, img, notesURL);

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

                String notesURL = textWebURL.getText().toString();

                NotesModel notesModel = null;

                String colour = colourGroup(v);

                if (titleValue.matches("")) {
                    titleText.setError("Title is empty, a title is required to save");
                } else {
                    try {
                        notesModel = new NotesModel(note.getId(), titleValue, subtitleValue, noteBody, colour, img, notesURL);
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

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_IMG_CODE);
    }

    private void captureImage(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMG_CODE);
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
        } else if (requestCode == CAMERA_PERMISSION_CODE && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                captureImage();
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
        } else if (requestCode == CAPTURE_IMG_CODE && resultCode == RESULT_OK){
            if (data != null){
                try {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    noteImg.setImageBitmap(bitmap);
                    noteImg.setVisibility(View.VISIBLE);
                } catch (Exception e){
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public void getWebURLDialog(){
        if (dialogAddURL == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(EditNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_add_url,
                    (ViewGroup) findViewById(R.id.layoutAddURLContainer)
            );
            builder.setView(view);

            dialogAddURL = builder.create();
            if (dialogAddURL.getWindow() != null){
                dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText inputURL = view.findViewById(R.id.inputURL);
            inputURL.requestFocus();

            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inputURL.getText().toString().isEmpty()){
                        Toast.makeText(EditNoteActivity.this, "Please enter an URL", Toast.LENGTH_SHORT).show();
                    }else if (!Patterns.WEB_URL.matcher(inputURL.getText().toString()).matches()){
                        Toast.makeText(EditNoteActivity.this, "Enter a valid URL", Toast.LENGTH_SHORT).show();
                    }else {
                        textWebURL.setText(inputURL.getText().toString());
                        layoutWebURL.setVisibility(View.VISIBLE);
                        dialogAddURL.dismiss();
                    }
                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddURL.dismiss();
                }
            });
        }

        dialogAddURL.show();
    }
}