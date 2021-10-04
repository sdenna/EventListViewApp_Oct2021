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
import java.util.Calendar;

public class EditEventActivity extends AppCompatActivity {

    private String dateSelected = "No date chosen";
    private String keyToUpdate;
    private int dateMonth;
    private int dateDay;
    private int dateYear;
    private FirestoreHelper dbHelper;
    private EditText eventNameET;
    private CalendarView calendarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        dbHelper = new FirestoreHelper();

        Intent intent = getIntent();
        Event event = intent.getParcelableExtra("event");

        String eventNameToUpdate = event.getEventName();
        String eventDateToUpdate = event.getEventDate();
        keyToUpdate = event.getKey();

        eventNameET = findViewById(R.id.eventName);
        calendarView =  findViewById(R.id.eventCalendarDate);

        eventNameET.setText(eventNameToUpdate);

        // This allows us to parse out the date to get the month, day, and year
        String parts[] = eventDateToUpdate.split("/");

        int month = Integer.parseInt(parts[0]);
        int day = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        dateSelected = (month + 1) + "/" + day + "/" + year;

        // Sets the month day, year on the calendar view so we can display the date
        // they chose and avoid having to error check their entry when they enter a new date

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        long milliTime = calendar.getTimeInMillis();
        calendarView.setDate(milliTime);

     //   SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                dateSelected = (month + 1) + "/" + day + "/" + year;
                dateYear = year;
                dateMonth = month + 1;
                dateDay = day;
                closeKeyboard();
            }
        });
    }

    public void updateEventData(View v) {
        EditText eventNameET = (EditText) findViewById(R.id.eventName);
        String eventName = eventNameET.getText().toString();

        // verify there is a name and date
        if (eventName.length() == 0 ) {
            Toast.makeText(EditEventActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
        }
        else if (dateSelected.equals("No date chosen")) {
            Toast.makeText(EditEventActivity.this, "Please select Date", Toast.LENGTH_SHORT).show();
        }
        else {
            Event updatedEvent = new Event(eventName, dateSelected, dateYear, dateMonth, dateDay, keyToUpdate);
            eventNameET.setText("");    // clears out text
            Toast.makeText(EditEventActivity.this, updatedEvent.getEventDate() + " " + updatedEvent.getEventName(), Toast.LENGTH_SHORT).show();
            dbHelper.updateEvent(updatedEvent);
        }

    }

    public void deleteEventData(View v) {
       // call the firestore helper method delete and pass it the key for the document you wish to delete
        Log.i("Denna", "trying to delete " + keyToUpdate);
        dbHelper.deleteEvent(keyToUpdate);
    }
    public void showData(View v) {
        Intent intent = new Intent(EditEventActivity.this, DisplayEventsActivity.class);
        ArrayList<Event> eventsToShow = dbHelper.getEventsArrayList();
        intent.putExtra("events", eventsToShow);
        startActivity(intent);
    }

    // do I need this???
    public void onHome(View v) {

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