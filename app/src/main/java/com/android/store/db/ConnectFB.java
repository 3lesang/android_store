package com.android.store.db;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConnectFB {
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public void ConnectFB() {

    }
    public final DatabaseReference getDBRef() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("DBProduct");
        return myRef;
    }
}
