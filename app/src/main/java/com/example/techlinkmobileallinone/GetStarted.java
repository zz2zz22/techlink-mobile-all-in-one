package com.example.techlinkmobileallinone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class GetStarted extends AppCompatActivity {
    Button startButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(view -> {
            Intent i = new Intent(GetStarted.this, LoginActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.static_animation, R.anim.zoom_out);
            finish();
        });
    }
}