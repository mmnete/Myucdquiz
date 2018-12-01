package com.companywesbite.myucdquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/***
 *
 *
 *
 * Team: Flashcards Pro
 * Date: 12/09/2018
 * Name:  SplashScreenActivity
 * Functionality: Shows splashscreen until app is loaded then starts the mainactivity
 *
 *
 */

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}