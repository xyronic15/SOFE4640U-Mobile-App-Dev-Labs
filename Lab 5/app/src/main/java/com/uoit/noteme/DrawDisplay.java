package com.uoit.noteme;

import static com.uoit.noteme.FlowchartActivity.brush;
import static com.uoit.noteme.FlowchartActivity.canvasBitmap;
import static com.uoit.noteme.FlowchartActivity.canvasPaint;
import static com.uoit.noteme.FlowchartActivity.drawCanvas;
import static com.uoit.noteme.FlowchartActivity.path;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DrawDisplay extends View {

//    public static ArrayList<Path> pathList = new ArrayList<>();
//    public static ArrayList<Integer> colorList = new ArrayList<>();
//    public ViewGroup.LayoutParams params;
    public static int currentCol = Color.BLACK;

//    public DrawDisplay(Context context) {
//        super(context);
//    }

    public DrawDisplay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
//
//    public DrawDisplay(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//    }

    public void init(){
//        brush.setAntiAlias(true);
//        brush.setColor(Color.BLACK);
//        brush.setStyle(Paint.Style.STROKE);
//        brush.setStrokeCap(Paint.Cap.ROUND);
//        brush.setStrokeJoin(Paint.Join.ROUND);
//        brush.setStrokeWidth(10f);
//
//        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        path = new Path();
        brush = new Paint();
        brush.setColor(Color.BLACK);
        brush.setAntiAlias(true);
        brush.setStrokeWidth(10f);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                path.moveTo(x, y);
//                invalidate();
//                return true;
//            case MotionEvent.ACTION_MOVE:
//                path.lineTo(x, y);
//                pathList.add(path);
//                colorList.add(currentCol);
//                invalidate();
//                return true;
//            default:
//                return false;
//        }

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                path.lineTo(x, y);
                drawCanvas.drawPath(path, brush);
                path.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        for (int i = 0; i < pathList.size(); i++) {
//            brush.setColor(colorList.get(i));
//            canvas.drawPath(pathList.get(i), brush);
//            invalidate();
//        }

        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(path, brush);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        drawCanvas = new Canvas(canvasBitmap);
    }
}
