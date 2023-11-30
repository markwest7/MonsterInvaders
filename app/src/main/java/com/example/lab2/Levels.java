package com.example.lab2;

public class Levels {

    //How many seconds between monster spawns
    private int mSpawnRate;
    private int mCounter;
    private int mMonstersLeft;
    private int mMonsterSpeed;
    private int mLevel = 0;
    private int mTotalMonsters;

    public Levels(){
    }

    public void nextLevel(){
        mLevel++;
        startLevel(mLevel);
    }

    public void startLevel(int level){

        switch(level){

            case 1:
                mSpawnRate = 3000;
                mTotalMonsters = 7;
                mMonsterSpeed = 100;
                break;
            case 2:
                mSpawnRate = 1000;
                mTotalMonsters = 12;
                mMonsterSpeed = 200;
                break;
            case 3:
                mSpawnRate = 500;
                mTotalMonsters = 20;
                mMonsterSpeed = 400;
                break;
            default:
                if(mLevel<90) {
                    mSpawnRate = 500 - (mLevel * 10);
                }else{
                    mSpawnRate = 50;
                }
                mTotalMonsters += 10;
                mMonsterSpeed += 200;
                break;
        }

        mCounter = mSpawnRate;
        mMonstersLeft = mTotalMonsters;

    }

    public boolean timer(long time){
        boolean returnValue = false;

        mCounter -= time;

        if(mCounter < 1){
            returnValue = true;
            mCounter = mSpawnRate;
        }

        return returnValue;
    }

    public int getMonstersLeft() {
        return mMonstersLeft;
    }

    public void killMonster(){
        mMonstersLeft--;
    }

    public int getMonsterSpeed(){
        return mMonsterSpeed;
    }

    public int getCurrentLevel() {
        return mLevel;
    }

    public void setLevel(int level){
        mLevel = level;
    }

}
