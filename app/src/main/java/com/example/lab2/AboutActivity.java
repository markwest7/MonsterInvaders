package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.widget.ImageView;

public class AboutActivity extends AppCompatActivity {

    //Objects for drawing
    private Canvas mCanvas;
    private Paint mPaint;
    private Bitmap mBitmap;

    ImageView aboutView;
    Point size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the screen resolution
        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        aboutView = new ImageView(this);
        setContentView(aboutView);

        mBitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);

        mCanvas = new Canvas (mBitmap);
        mPaint = new Paint();

        draw();
    }

    public void draw(){

        aboutView.setImageBitmap(mBitmap);

        mCanvas.drawColor(Color.argb(255,255,255,255));

        mPaint.setColor(Color.argb(255,0,0,0));
        mPaint.setTextSize((float) (size.x*0.1));
        mCanvas.drawText("About the Game", (float) (size.x*0.05), (float) (size.y*0.1),mPaint);

        mPaint.setTextSize((float) (size.x*0.07));
        mCanvas.drawText("Here are the rules", (float) (size.x*0.05), (float) (size.y*0.3),mPaint);

        mPaint.setTextSize((float) (size.x*0.05));
        mCanvas.drawText("Shoot the monsters to earn points ", (float) (size.x*0.05), (float) (size.y*0.4),mPaint);
        mCanvas.drawText("and progress to the next level.", (float) (size.x*0.05), (float) (size.y*0.45),mPaint);

    }
}