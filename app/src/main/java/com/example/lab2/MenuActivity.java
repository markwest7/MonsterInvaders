package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

public class MenuActivity extends Activity {

    public static final String EXTRA_MUSIC = "com.example.lab2.MUSIC";
    private CheckBox checkBoxMusic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkBoxMusic = findViewById(R.id.checkBox);
    }

    //Start game when user clicks the play button
    public void playGame(View view){
        boolean playMusic;

        if(checkBoxMusic.isChecked()){
            playMusic = true;
        } else {
            playMusic = false;
        }

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_MUSIC, playMusic);
        startActivity(intent);
    }

    //Go to about view when user clicks the about the game button
    public void aboutGame(View view){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

}