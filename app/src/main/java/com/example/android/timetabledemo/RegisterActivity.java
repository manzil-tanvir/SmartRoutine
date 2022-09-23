package com.example.android.timetabledemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.android.timetabledemo.Session.SessionManagement;
import com.example.android.timetabledemo.pojo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    DatabaseReference userDatabaseReference;
    FirebaseAuth firebaseAuth;

    EditText    fullNameEditText;
    EditText    usernameEditText;
    EditText    passwordEditText;
    RadioButton registerAsStudentRadioButton;
    RadioButton registerAsTeacherRadioButton;
    Button      registerButton;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUIViews();
        initToolbar();

        // sign in activity
        signInActivity();

        fullNameEditText                = (EditText) findViewById(R.id.fullname);
        usernameEditText                = (EditText) findViewById(R.id.username);
        passwordEditText                = (EditText) findViewById(R.id.password);
        registerAsStudentRadioButton    = (RadioButton) findViewById(R.id.selectStudent);
        registerAsTeacherRadioButton    = (RadioButton) findViewById(R.id.selectTeacher);
        registerButton                  = (Button) findViewById(R.id.signupbtn);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }

        userDatabaseReference = FirebaseDatabase.getInstance().getReference("users");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName     = fullNameEditText.getText().toString();
                String username     = usernameEditText.getText().toString().toLowerCase();
                String password     = passwordEditText.getText().toString();
                String type         = "";

                if (registerAsStudentRadioButton.isChecked()) {
                    type = registerAsStudentRadioButton.getText().toString();
                } else if (registerAsTeacherRadioButton.isChecked()) {
                    type = registerAsTeacherRadioButton.getText().toString();
                }

                register(fullName, username, password, type);
            }
        });
    }

    private void setupUIViews(){
        toolbar = (Toolbar)findViewById(R.id.ToolbarMain);
    }
    private void initToolbar(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Smart Routine");
    }

    private void register (String fullName, String username, String password, String type) {
        User user = new User(fullName, username, password, type);

        if (validation(user)) {
            String dEmail = username + "@example.com";
            firebaseAuth.createUserWithEmailAndPassword(dEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        userDatabaseReference.child(username).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                fullNameEditText.setText("");
                                usernameEditText.setText("");
                                passwordEditText.setText("");
                            }
                        });

                        Toast toast = Toast.makeText(RegisterActivity.this, "You are successfully registered.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();

                        SessionManagement sessionManagement = new SessionManagement(RegisterActivity.this);
                        sessionManagement.setSession(username);

                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error occurred! try again or use another username.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast toast = Toast.makeText(RegisterActivity.this, "Re-check your input fields.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
    }

    private void signInActivity () {
        Button signInBtn = (Button) findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private boolean validation (User user) {
        if (user.getFullName().length() < 3) {
            fullNameEditText.requestFocus();
            fullNameEditText.setError("Name must be at least 3 characters long.");
            return false;
        } else if (!user.getFullName().matches("[a-zA-Z ]*$")) {
            fullNameEditText.requestFocus();
            fullNameEditText.setError("Enter only alphabetical character and space.");
            return false;
        } else if (user.getUsername().length() == 0) {
            usernameEditText.requestFocus();
            usernameEditText.setError("Username can not be empty.");
            return false;
        } else if (!(user.getUsername()).matches("^[A-Za-z]\\w{5,29}$")) {
            usernameEditText.requestFocus();
            usernameEditText.setError("Enter a valid username.");
            return false;
        } else if (user.getPassword().length() < 6) {
            passwordEditText.requestFocus();
            passwordEditText.setError("Minimum 6 characters required");
            return false;
        }

        return true;
    }
}