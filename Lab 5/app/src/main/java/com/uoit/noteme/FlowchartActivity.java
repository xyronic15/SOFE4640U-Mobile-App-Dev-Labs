package com.uoit.noteme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import static com.uoit.noteme.DrawDisplay.currentCol;

import java.io.ByteArrayOutputStream;

public class FlowchartActivity extends AppCompatActivity {

    public static Path path;
    public static Paint brush;
    public static Paint canvasPaint;
    public static Canvas drawCanvas;
    public static Bitmap canvasBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowchart);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        ConstraintLayout drawingLayout = findViewById(R.id.drawingLayout);

        findViewById(R.id.imageSave).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Bitmap bmp = createBitmapFromView(getApplicationContext(), drawingLayout);
                Bitmap bmp = canvasBitmap;
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                byte[] byteArray = out.toByteArray();
                Intent intent = new Intent();
                intent.putExtra("drawing", byteArray);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void pencil(View view) {
        brush.setColor(Color.BLACK);
        currentColor(brush.getColor());
    }

    public void currentColor(int c){
        currentCol = c;
        path = new Path();
    }

    private Bitmap createBitmapFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
}