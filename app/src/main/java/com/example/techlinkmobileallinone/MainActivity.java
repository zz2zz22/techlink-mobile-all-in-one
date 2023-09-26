package com.example.techlinkmobileallinone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {

    public static int SPLASH_TIMER = 2000;
    private int CAMERA_PERMISSION_REQUEST_CODE = 0;
    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
        }
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