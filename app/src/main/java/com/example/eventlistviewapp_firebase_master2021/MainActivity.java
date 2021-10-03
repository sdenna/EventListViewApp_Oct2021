package com.example.eventlistviewapp_firebase_master2021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addEventButtonPressed(View v) {

    }

    public void showData(View v) {
        Intent intent = new Intent(MainActivity.this, DisplayEventsActivity.class);
        startActivity(intent);
    }
}
