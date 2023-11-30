package com.example.lab2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class GameActivity extends Activity {

    private GameEngine mGameEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the screen resolution
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Intent intent = getIntent();
        Boolean playMusic = intent.getBooleanExtra(MenuActivity.EXTRA_MUSIC, true);

        mGameEngine = new GameEngine(this, size.x, size.y, playMusic);
        setContentView(mGameEngine);
    }

    @Override
    //Start the main game thread when the game is launched
    protected void onResume() {
        super.onResume();

        mGameEngine.resume();
    }

    @Override
    //Stop the thread when the player quits
    protected void onPause() {
        super.onPause();

        mGameEngine.pause();
    }
}