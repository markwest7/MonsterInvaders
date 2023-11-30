package com.example.lab2;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.RectF;

import java.util.Random;

public class Player {

    private RectF mRect;
    private int mWidth;
    private int mHeight;
    private int mPosX;
    private int mPosY;
    private Bitmap mBitmap;
    private int mSound;
    private float mDirection;
    private float mSpeed;
    private boolean mAlive;

    public Player(Bitmap bitmap, int sound, int screenX, int screenY){


        mRect = new RectF();
        mBitmap = bitmap;
        mWidth = bitmap.getWidth();
        mHeight = bitmap.getHeight();
        mSound = sound;
        mPosX = screenX - (screenX/2) - (mWidth/2);
        mPosY = screenY - (mHeight * 3);

        mSpeed = 1200;

        mRect.set(mPosX, mPosY, mPosX + mWidth, mPosY + mHeight);
    }

    public void newDirection(float direction){

        mDirection = direction - (mWidth/2);

    }

    public void update(long fps){

        if (mPosX != mDirection){
            if(mPosX > mDirection){
                if((mPosX-mDirection) < (mSpeed/fps)){
                    mPosX = (int)mDirection;
                }else {
                    mPosX -= (mSpeed/fps);
                }
            }else{
                if((mDirection-mPosX) < (mSpeed/fps)){
                    mPosX = (int)mDirection;
                }else {
                    mPosX += (mSpeed/fps);
                }
            }
            mRect.set(mPosX, mPosY, mPosX + mWidth, mPosY + mHeight);
        }
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

    public boolean isAlive(){
        return mAlive;
    }

    public void setAlive(boolean alive){
        mAlive = alive;
    }
}
