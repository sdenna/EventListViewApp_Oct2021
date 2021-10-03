package com.example.eventlistviewapp_firebase_master2021;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class EditEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        // adds arrow to go back to home
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void updateEventData(View v) {

    }

    public void deleteEventData(View v) {

    }
    public void onRetrieve(View v) {

    }
    public void onHome(View v) {

    }
}