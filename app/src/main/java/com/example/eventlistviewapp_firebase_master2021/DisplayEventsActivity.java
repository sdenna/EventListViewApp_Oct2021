package com.example.eventlistviewapp_firebase_master2021;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DisplayEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_events);

        // adds arrow to go back to home
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}