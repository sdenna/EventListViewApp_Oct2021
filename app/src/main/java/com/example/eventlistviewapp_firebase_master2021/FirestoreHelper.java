package com.example.eventlistviewapp_firebase_master2021;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FirestoreHelper {
    private FirebaseFirestore db;
    private ArrayList<Event> eventsArrayList= new ArrayList<>();

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
    }

    /* You can add custom objects with Firestore as long as there is a public constructor
    that takes no arguments AND a public getter for each property.

     https://firebase.google.com/docs/firestore/manage-data/add-data?authuser=0
    */
    public void addEvent(Event event) {
        // use .add when you don't have a unique ID number for each document.  This will generate
        // one for you.  If you did have a unique ID number, then you would use set.
       db.collection("events")
                .add(event)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    // documentReference contains a reference to the newly created Document if done successfully
                    public void onSuccess(DocumentReference documentReference) {
                        db.collection("events").document(documentReference.getId())
                                .update("key", documentReference.getId());
                        Log.i("Denna", "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Denna", "Error adding document", e);
                    }
                });

    }



    public ArrayList<Event> getEventsArrayList() { return eventsArrayList; }


}
