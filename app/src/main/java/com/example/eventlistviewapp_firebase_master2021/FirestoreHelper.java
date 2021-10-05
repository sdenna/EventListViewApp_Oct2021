package com.example.eventlistviewapp_firebase_master2021;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class FirestoreHelper {
    private final FirebaseFirestore db;         // ref to entire database
    private CollectionReference eventsRef;      // ref to events collection only

                                                // arraylist of all Events in database
    private ArrayList<Event> eventsArrayList= new ArrayList<>();

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        // This listener will listen for whenever the data is updated.  When that occurs, the arraylist
        // is cleared out and it is refreshed with the updated data.

        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                // clear out the array list so that none of the events are duplicated in the display
                eventsArrayList.clear();

                // this for each loop will get each Document Snapshot from the query, and one at a time,
                // convert them to an object of the Event class and then add them to the array list

                for (QueryDocumentSnapshot doc: value) {
                    Event event = doc.toObject(Event.class);
                    eventsArrayList.add(event);
                }
            }
        });
    }

    /* You can add custom objects with Firestore as long as there is a public constructor
    that takes no arguments AND a public getter for each property.  Because we included these
    fields in the Event class, we can simply add an Event object and we don't have to use a Map object

     https://firebase.google.com/docs/firestore/manage-data/add-data?authuser=0
    */
    public void addEvent(Event event) {
        // use .add when you don't have a unique ID number for each document.  This will generate
        // one for you.  If you did have a unique ID number, then you would use set.
       db.collection("events")
                .add(event) // adds an event without a key defined yet
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    // documentReference contains a reference to the newly created Document if done successfully
                    public void onSuccess(DocumentReference documentReference) {
                        db.collection("events").document(documentReference.getId())
                                .update("key", documentReference.getId());  // sets the DocID key for the Event that was just added
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Denna", "Error adding document", e);
                    }
                });

    }

    public void deleteEvent(String key) {
        db.collection("events").document(key)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("Denna", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Denna", "Error deleting document", e);
                    }
                });

    }

    // set will override an existing Event object with this key
    // if one isn't available, then it will add the object.
    public void updateEvent(Event event) {
        db.collection("events").document(event.getKey())
                .set(event);
    }

    public ArrayList<Event> getEventsArrayList() {
        Collections.sort(eventsArrayList);
        return eventsArrayList;
    }
}
