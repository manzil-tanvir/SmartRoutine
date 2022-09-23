package com.example.android.timetabledemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.android.timetabledemo.Session.SessionManagement;
import com.example.android.timetabledemo.pojo.Schedule;
import com.example.android.timetabledemo.pojo.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AddScheduleActivity extends AppCompatActivity {

    DatabaseReference scheduleDatabaseReference;

    EditText    courseEditText;
    EditText    semesterEditText;
    EditText    weekDayEditText;
    EditText    startTimeEditText;
    EditText    endTimeEditText;
    EditText    roomNoEditText;
    Button      addScheduleSubmitButton;

    private Toolbar toolbar;

    private final String[] WEEK_DAYS = new String[] {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday"};
    private final String[] SEMESTERS = new String[] {"1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        setupUIViews();
        initToolbar();
        setAuthUser();

        courseEditText              = (EditText) findViewById(R.id.course);
        semesterEditText            = (EditText) findViewById(R.id.semester);
        weekDayEditText             = (EditText) findViewById(R.id.weekDay);
        startTimeEditText           = (EditText) findViewById(R.id.startTime);
        endTimeEditText             = (EditText) findViewById(R.id.endTime);
        roomNoEditText              = (EditText) findViewById(R.id.roomNo);
        addScheduleSubmitButton     = (Button) findViewById(R.id.addScheduleSubmitBtn);

        scheduleDatabaseReference = FirebaseDatabase.getInstance().getReference().child("schedules");

        addScheduleSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String course       = semesterEditText.getText().toString();
                String semester     = semesterEditText.getText().toString();
                String weekDay      = weekDayEditText.getText().toString();
                String startTime    = startTimeEditText.getText().toString();
                String endTime      = endTimeEditText.getText().toString();
                String roomNo       = roomNoEditText.getText().toString();

                addSchedule(course, semester, weekDay, startTime, endTime, roomNo);
            }
        });
    }

    private void addSchedule (String course, String semester, String weekDay, String startTime, String endTime, String roomNo) {
        Schedule schedule = new Schedule(course, semester, weekDay, startTime, endTime, roomNo);

        if (validation(schedule)) {
            scheduleDatabaseReference.push().setValue(schedule);
            Toast toast = Toast.makeText(AddScheduleActivity.this, "Your schedule have been placed.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();

            startActivity(new Intent(AddScheduleActivity.this, ListScheduleActivity.class));
            finish();
        } else {
            Toast toast = Toast.makeText(AddScheduleActivity.this, "Re-check your input fields.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
    }

    private boolean validation (Schedule schedule) {
        if (schedule.getCourse().length() == 0) {
            courseEditText.requestFocus();
            courseEditText.setError("Course can not be empty.");
            return false;
        } else if (schedule.getSemester().length() == 0) {
            semesterEditText.requestFocus();
            semesterEditText.setError("Semester can not be empty.");
            return false;
        } else if (!stringArrayContains(schedule.getSemester(), this.SEMESTERS)) {
            semesterEditText.requestFocus();
            semesterEditText.setError("Semester should be 1st, 2nd, 3rd, 4th, 5th, 6th, 7th or 8th.");
            return false;
        } else if (schedule.getWeekDay().length() == 0) {
            weekDayEditText.requestFocus();
            weekDayEditText.setError("Week day can not be empty.");
            return false;
        } else if (!stringArrayContains(schedule.getWeekDay(), this.WEEK_DAYS)) {
            weekDayEditText.requestFocus();
            weekDayEditText.setError("Week day should be Saturday, Sunday, Monday, Tuesday or Wednesday.");
            return false;
        } else if (schedule.getStartTime().length() == 0) {
            startTimeEditText.requestFocus();
            startTimeEditText.setError("Start time can not be empty.");
            return false;
        } else if (schedule.getEndTime().length() == 0) {
            endTimeEditText.requestFocus();
            endTimeEditText.setError("End time can not be empty.");
            return false;
        } else if (schedule.getRoomNo().length() == 0) {
            roomNoEditText.requestFocus();
            roomNoEditText.setError("Room no can not be empty.");
            return false;
        }

        return true;
    }

    private boolean stringArrayContains(String value, String[] data) {
        for (String datum : data)
            if (datum.equalsIgnoreCase(value))
                return true;

        return false;
    }

    private void setAuthUser () {
        TextView authFullNameTextView = (TextView) findViewById(R.id.authFullName);
        TextView authUsernameTextView = (TextView) findViewById(R.id.authUsername);

        SessionManagement sessionManagement = new SessionManagement(AddScheduleActivity.this);
        User user = sessionManagement.getSession();

        authFullNameTextView.setText("NAME: " + user.getFullName());
        authUsernameTextView.setText("USERNAME: " + user.getUsername());
    }

    private void setupUIViews(){
        toolbar = (Toolbar)findViewById(R.id.ToolbarAddSchedule);
    }
    private void initToolbar(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Schedule");
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
                Toast.makeText(AddScheduleActivity.this, "Under Construction.", Toast.LENGTH_LONG).show();
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AddScheduleActivity.this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}