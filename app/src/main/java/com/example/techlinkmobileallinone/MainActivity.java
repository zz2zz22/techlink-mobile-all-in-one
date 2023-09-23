package com.example.techlinkmobileallinone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.DisableClickListener;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

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
            // https://github.com/javiersantos/AppUpdater
            AppUpdater appUpdater = new AppUpdater(this)
                    .setTitleOnUpdateAvailable("Có bản cập nhật mới !")
                    .setContentOnUpdateAvailable("Nội dung")
                    .setButtonUpdate("Update ?")
                    .setButtonDismiss("Maybe later")
                    .setIcon(R.drawable.techlink_logo)
                    .setDisplay(Display.DIALOG)
                    .setButtonDoNotShowAgain(null)
                    .setCancelable(false)
                    .setUpdateFrom(UpdateFrom.JSON)
                    .setUpdateJSON("https://raw.githubusercontent.com/zz2zz22/techlink-mobile-all-in-one/master/app/update-changelog.json");
            appUpdater.start();
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