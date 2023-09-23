package com.example.techlinkmobileallinone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    public static int SPLASH_TIMER = 2000;

    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(() -> {
            onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
            boolean isSkipOnboarding = onBoardingScreen.getBoolean("skipOnboarding", true);
            /*SharedPreferences.Editor editor = onBoardingScreen.edit();
            editor.putBoolean("skipOnboarding", false);
            editor.commit();
            */

            if(isSkipOnboarding)
            {
                startActivity(new Intent(MainActivity.this, NavigationActivity.class));
            }
            else{
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
            overridePendingTransition(R.anim.static_animation, R.anim.zoom_out);
            finish();
        }, SPLASH_TIMER);
    }
}