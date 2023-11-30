package com.example.lab2;



import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.RectF;



import java.io.IOException;
import java.util.Random;

public class Monster {

    private RectF mRect;
    private int mWidth;
    private int mHeight;
    private Random mRandom;
    private int mPosX;
    private int mPosY;
    private Bitmap mBitmap;
    private Bitmap mBitmap1;
    private Bitmap mBitmap2;
    private int mBitTimer=500;
    private int mWhatBitmap=1;
    private int mSound;
    private int mSpeed;
    private boolean mAlive = true;


    public Monster(Bitmap bitmap, Bitmap bitmap2, int sound, int screenX, int screenY){

        mRect = new RectF();
        mRandom = new Random();
        mBitmap = bitmap;
        mBitmap1 = bitmap;
        mBitmap2 = bitmap2;
        mWidth = bitmap.getWidth();
        mHeight = bitmap.getHeight();
        newPosition(screenX, screenY);
        mSound = sound;
        mSpeed = 200;
    }

    public void update(long fps, long timeThisFrame){
        mPosY += (mSpeed/fps);
        mRect.set(mPosX, mPosY, mPosX +mWidth, mPosY +mHeight);

        mBitTimer -= timeThisFrame;

        if (mBitTimer<0){
            if (mWhatBitmap==1){
                mBitmap=mBitmap2;
                mWhatBitmap=2;
                mBitTimer=500;
            } else{
                mBitmap=mBitmap1;
                mWhatBitmap=1;
                mBitTimer=500;
            }
        }
    }

    /*
    Sets a new position for the instance making sure it stays on screen
     */
    public void newPosition(int screenX, int screenY){
        mPosX = mRandom.nextInt(screenX - mWidth);
        mPosY = 0;


        mRect.set(mPosX, mPosY, mPosX + mWidth, mPosY + mHeight);
    }

    public RectF getRect(){
        return mRect;
    }

    public Bitmap getBitmap(){
        return mBitmap;
    }

    public int getSound(){
        return mSound;
    }

    public Point getPoint(){
        Point position = new Point(mPosX,mPosY);
        return position;
    }

    public boolean isAlive(){
        return mAlive;
    }

    public void kill(){
        mAlive = false;
    }

}
