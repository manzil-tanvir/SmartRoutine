package com.example.android.timetabledemo.Session;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.android.timetabledemo.pojo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;
    String json;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";

    public SessionManagement(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public void setSession(String username) {
        User user = new User();

        DatabaseReference userDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        userDatabaseReference.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        user.setUsername(String.valueOf(dataSnapshot.child("username").getValue()));
                        user.setFullName(String.valueOf(dataSnapshot.child("fullName").getValue()));
                        user.setType(String.valueOf(dataSnapshot.child("type").getValue()));

                        json = gson.toJson(user);
                        editor.putString(SESSION_KEY, json).commit();
                    }
                }
            }
        });
    }

    public User getSession() {
        json = sharedPreferences.getString(SESSION_KEY, "");
        return gson.fromJson(json, User.class);
    }
}
