package com.example.android.timetabledemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.android.timetabledemo.Session.SessionManagement;
import com.example.android.timetabledemo.pojo.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ListScheduleActivity extends AppCompatActivity {

    DatabaseReference scheduleDatabaseReference;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_schedule);
        setupUIViews();
        initToolbar();
        setAuthUser();

        scheduleDatabaseReference = FirebaseDatabase.getInstance().getReference().child("schedules");
    }

    private void setAuthUser () {
        TextView authFullNameTextView = (TextView) findViewById(R.id.authFullName);
        TextView authUsernameTextView = (TextView) findViewById(R.id.authUsername);

        SessionManagement sessionManagement = new SessionManagement(ListScheduleActivity.this);
        User user = sessionManagement.getSession();

        authFullNameTextView.setText("NAME: " + user.getFullName());
        authUsernameTextView.setText("USERNAME: " + user.getUsername());
    }

    private void setupUIViews(){
        toolbar = (Toolbar)findViewById(R.id.ToolbarListSchedule);
    }
    private void initToolbar(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Schedule List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.credits:
                Toast.makeText(ListScheduleActivity.this, "Under Construction.", Toast.LENGTH_LONG).show();
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ListScheduleActivity.this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}