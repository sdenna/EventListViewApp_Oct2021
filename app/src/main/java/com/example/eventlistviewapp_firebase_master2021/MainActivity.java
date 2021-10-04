package com.example.eventlistviewapp_firebase_master2021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String dateSelected = "No date chosen";
    private int dateMonth;
    private int dateDay;
    private int dateYear;
    private FirestoreHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new FirestoreHelper();

        //  Video to learn basic access to CalendarView Data
        //  https://www.youtube.com/watch?v=WNBE_3ZizaA

        CalendarView calendarView = findViewById(R.id.eventCalendarDate);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
                                                 @Override
                                                 public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                                                     dateSelected =  (month + 1) + "/" + day + "/" + year;
                                                     dateYear = year;
                                                     dateMonth = month + 1;
                                                     dateDay = day;
                                                     closeKeyboard();
                                                 }
                                             }
        );
        
    }

    public void addEventButtonPressed(View v) {
        EditText eventNameET = (EditText) findViewById(R.id.eventName);
        String eventName = eventNameET.getText().toString();

        // verify there is a name and date
        if (eventName.length() == 0 ) {
            Toast.makeText(MainActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
        }
        else if (dateSelected.equals("No date chosen")) {
            Toast.makeText(MainActivity.this, "Please select Date", Toast.LENGTH_SHORT).show();
        }
        else {
            Event newEvent = new Event(eventName, dateSelected, dateYear, dateMonth, dateDay);
            eventNameET.setText("");    // clears out text
            Toast.makeText(MainActivity.this, newEvent.getEventDate() + " " + newEvent.getEventName(), Toast.LENGTH_SHORT).show();
            dbHelper.addEvent(newEvent);
        }
    }

    /**
     * This method is called to retrieve the data from firestore.  Currently it is simply
     * calling the helper method to get an array list with all events, and then the array list
     * will be used to display the info when the next page loads.
     */

    public void showData(View v) {
        Intent intent = new Intent(MainActivity.this, DisplayEventsActivity.class);
        ArrayList<Event> eventsToShow = dbHelper.getEventsArrayList();
        intent.putExtra("events", eventsToShow);
        startActivity(intent);
    }

    /**
     * This method will be called to minimize the on screen keyboard in the Activity
     * When we get the current view, it is the view that has focus, which is the keyboard
     *
     * Source:  https://www.youtube.com/watch?v=CW5Xekqfx3I
     */
    private void closeKeyboard() {
        View view = this.getCurrentFocus();     // view will refer to the keyboard
        if (view != null ){                     // if there is a view that has focus
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
