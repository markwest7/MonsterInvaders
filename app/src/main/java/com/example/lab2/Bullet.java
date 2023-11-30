package com.example.lab2;

import android.graphics.Point;
import android.graphics.RectF;

public class Bullet {

    private RectF mRect;
    private float mSpeed = 500;
    private int mPosX;
    private int mPosY;
    private int mSize = 10;
    private boolean mHit = false;

    public Bullet(int x, int y){

        mPosX = x;
        mPosY = y;
        mRect = new RectF(x, y, x+mSize,y+mSize);
    }

    public void update(long fps){
        mPosY -= (mSpeed/fps);
        mRect.set(mPosX, mPosY, mPosX +mSize, mPosY +mSize);
    }

    public RectF getRect(){
        return mRect;
    }

    public Point getPoint(){
        Point position = new Point(mPosX,mPosY);
        return position;
    }

    public boolean isHit(){
        return mHit;
    }

    public void hit(){
        mHit = true;
    }
}
