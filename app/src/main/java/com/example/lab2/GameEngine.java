package com.example.lab2;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class GameEngine extends SurfaceView implements Runnable{

    //Objects for drawing
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private Paint mPaint;

    //Holds the resolution of the screen
    private int mScreenX;
    private int mScreenY;

    //Here is the Thread and two control variables
    private Thread mGameThread = null;
    //This volatile variable can be accessed from inside and outside the thread
    private volatile boolean mPlaying;
    private boolean mPaused = true;

    //Keep track of the frame rate
    private long mFPS;
    //The number of milliseconds in a second
    private final int MILLIS_IN_SECOND = 1000;

    private int mScore;
    private int mTimelimit;

    //Arraylist that will hold references to instances of the Monster class
    ArrayList<Monster> monsterList;

    Player player;

    private Bitmap mBitmap;
    private Bitmap mBitmap2;

    private Levels mLevel;

    private int mWait;
    private String mText = "";
    private long mTimeThisFrame=0;

    //List holding references for all the bullets
    ArrayList<Bullet> bulletList;

    //Variables for sound
    private SoundPool mSoundPlayer;
    private int mSoundMonster1;
    private int mSoundMonster2;
    private MediaPlayer mediaPlayer;

    //Buttons
    private RectF mButtonRect;

    public GameEngine(Context context, int x, int y, boolean playMusic) {
        super(context);

        mScreenX = x;
        mScreenY = y;

        mHolder = getHolder();
        mPaint = new Paint();

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        mSoundPlayer = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();

        try{
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("monster1.ogg");
            mSoundMonster1 = mSoundPlayer.load(descriptor, 0);

            descriptor = assetManager.openFd("monster2.ogg");
            mSoundMonster2 = mSoundPlayer.load(descriptor, 0);

            descriptor = assetManager.openFd("Music.mp3");

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(),descriptor.getStartOffset(),descriptor.getLength());
            mediaPlayer.prepare();

        }catch(IOException e){
            Log.e("error", "failed to load sound files");
        }

        monsterList = new ArrayList<>();
        mBitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.monster2a);

        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.rymdskepp);
        player = new Player(mBitmap, mSoundMonster2, mScreenX, mScreenY);

        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.monster2);
        monsterList.add(new Monster(mBitmap, mBitmap2, mSoundMonster2, mScreenX, mScreenY));

        bulletList = new ArrayList<>();

        if(playMusic){
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        float screenX = mScreenX;
        float screenY = mScreenY;
        mButtonRect = new RectF((float)(0), (float)(screenY*0.9), (float)(screenX*0.2), (float)(screenY));

        mLevel = new Levels();

        mPaused = true;

    }

    @Override
    public void run() {
        while(mPlaying){

            long frameStartTime = System.currentTimeMillis();

            if (!mPaused){
                update();
            } else {
                if (mWait>0) {
                    mWait -= mTimeThisFrame;
                } else{
                    if (!player.isAlive()){
                        newGame();
                    }
                }
            }

            draw();

            mTimeThisFrame = System.currentTimeMillis() - frameStartTime;
            if(mTimeThisFrame >=1) {
                mFPS = MILLIS_IN_SECOND / mTimeThisFrame;
            }

        }
    }

    public void pause(){
        mPlaying = false;
        try{
            mGameThread.join();
        } catch (InterruptedException e){
            //Error
        }
        mediaPlayer.pause();
    }

    public void resume(){
        mPlaying = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }

    public void newGame(){
        synchronized (bulletList){
            bulletList = new ArrayList<>();
        }
        synchronized (monsterList){
            monsterList = new ArrayList<>();
        }
        newLevel();
        player.setAlive(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){

        if(mWait<1){
            mPaused=false;
        }

        if(mButtonRect.contains(motionEvent.getX(),motionEvent.getY())){
            RectF rect = player.getRect();
            synchronized (bulletList) {
                bulletList.add(new Bullet((int) rect.left + (int) ((rect.right - rect.left) / 2), (int) rect.top));
            }
        }else{
            player.newDirection(motionEvent.getX());
        }

        return true;
    }

    private void update(){
        player.update(mFPS);

        synchronized (bulletList) {
            for (Bullet bullet : bulletList) {
                bullet.update(mFPS);
            }
        }

        synchronized (monsterList) {
            for (Monster monster : monsterList) {
                monster.update(mFPS, mTimeThisFrame);
            }
        }

        checkCollisions();

        if(mLevel.timer(mTimeThisFrame)){
            newEnemy();
        }

        //draw();
    }

    private void newEnemy(){
        synchronized (monsterList) {
            monsterList.add(new Monster(mBitmap, mBitmap2, mSoundMonster1, mScreenX, mScreenY));
        }
    }

    private void checkCollisions(){


        synchronized (bulletList) {
            for (Bullet bullet : bulletList) {
                if (!bullet.isHit()) {
                    RectF bulletRect = bullet.getRect();
                    synchronized (monsterList) {
                        for (Monster monster : monsterList) {
                            if (monster.isAlive()) {
                                RectF monsterRect = monster.getRect();
                                if (monsterRect.intersect(bulletRect)) {
                                    //Hit
                                    mSoundPlayer.play(mSoundMonster1, 1,1,1,0,1);
                                    monster.kill();
                                    bullet.hit();
                                    mScore += 1;
                                    mLevel.killMonster();
                                    if (mLevel.getMonstersLeft() < 1) {
                                        newLevel();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        synchronized (bulletList) {
            for (Iterator<Bullet> bulletIterator = bulletList.iterator(); bulletIterator.hasNext(); ) {
                Bullet bullet = bulletIterator.next();
                if (bullet.isHit()) {
                    bulletIterator.remove();
                }
            }
        }

        synchronized (monsterList) {
            for (Iterator<Monster> monsterIterator = monsterList.iterator(); monsterIterator.hasNext(); ) {
                Monster monster = monsterIterator.next();
                if (!monster.isAlive()) {
                    monsterIterator.remove();
                }
            }
        }

        RectF playerRect = player.getRect();
        for(Monster monster : monsterList){
            RectF monsterRect = monster.getRect();
            if (monsterRect.intersect(playerRect)) {
                gameOver();
            }
        }

    }

    private void gameOver(){
        mPaused = true;
        mText = "You died. Score: " + mScore;
        player.setAlive(false);
        mWait = 5000;
        mLevel.setLevel(0);
        mScore = 0;
        synchronized (bulletList){
            bulletList = new ArrayList<>();
        }
        synchronized (monsterList){
            monsterList = new ArrayList<>();
        }
    }

    private void newLevel(){
        mLevel.nextLevel();
        mPaused = true;
        mText = "Get ready for level " + mLevel.getCurrentLevel();
        mWait = 2000;
        synchronized (bulletList){
            bulletList = new ArrayList<>();
        }
        synchronized (monsterList){
            monsterList = new ArrayList<>();
        }
    }

    private void draw(){
        if(mHolder.getSurface().isValid()){
            mCanvas = mHolder.lockCanvas();
            mCanvas.drawColor(Color.argb(255,100,100,55));
            mPaint.setColor(Color.argb(255,255,0,0));

            if (mPaused){
                mPaint.setTextSize(mScreenY/30);
                mCanvas.drawText(mText, 100,mScreenY/2,mPaint);
                if(mWait<1){
                    mText = "Click to start";
                }
            }

            //Draw monsters
            synchronized (monsterList) {
                for (Monster monster : monsterList) {
                    RectF rect = monster.getRect();
                    //mCanvas.drawRect(rect.left,rect.top,rect.right,rect.bottom,mPaint);
                    mCanvas.drawBitmap(monster.getBitmap(), rect.left, rect.top, mPaint);
                }
            }

            //Draw the bullets
            mPaint.setColor(Color.argb(255,100,0,0));
            synchronized (bulletList) {
                for (Bullet bullet : bulletList) {
                    RectF rect = bullet.getRect();
                    mCanvas.drawRect(rect, mPaint);
                }
            }

            //Draw the players ship
            RectF rect = player.getRect();
            mCanvas.drawBitmap(player.getBitmap(),rect.left,rect.top,mPaint);

            //Draw buttons
            mCanvas.drawRect(mButtonRect, mPaint);

            //Draw score
            String score = "" + mScore;
            mPaint.setTextSize(mScreenY/20);
            mCanvas.drawText(score, 10,50,mPaint);

            mHolder.unlockCanvasAndPost(mCanvas);
        }
    }

}
