package com.example.android.timetabledemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.android.timetabledemo.Session.SessionManagement;
import com.example.android.timetabledemo.pojo.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MyScheduleActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedule);

        setupUIViews();
        initToolbar();
        setAuthUser();

        addScheduleActivity();
        listScheduleActivity();
    }

    private void addScheduleActivity() {
        Button addScheduleBtnButton      = (Button) findViewById(R.id.addScheduleBtn);
        addScheduleBtnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyScheduleActivity.this, AddScheduleActivity.class));
            }
        });
    }

    private void listScheduleActivity() {
        Button listScheduleBtnButton     = (Button) findViewById(R.id.listScheduleBtn);
        listScheduleBtnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyScheduleActivity.this, ListScheduleActivity.class));
            }
        });
    }

    private void setAuthUser () {
        TextView authFullNameTextView = (TextView) findViewById(R.id.authFullName);
        TextView authUsernameTextView = (TextView) findViewById(R.id.authUsername);

        SessionManagement sessionManagement = new SessionManagement(MyScheduleActivity.this);
        User user = sessionManagement.getSession();

        authFullNameTextView.setText("NAME: " + user.getFullName());
        authUsernameTextView.setText("USERNAME: " + user.getUsername());
    }

    private void setupUIViews(){
        toolbar = (Toolbar)findViewById(R.id.ToolbarMySchedule);
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("My Schedule");
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
                Toast.makeText(MyScheduleActivity.this, "Under Construction.", Toast.LENGTH_LONG).show();
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MyScheduleActivity.this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
