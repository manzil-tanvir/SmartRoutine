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
import android.widget.Toast;

import com.example.android.timetabledemo.Session.SessionManagement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    EditText    usernameEditText;
    EditText    passwordEditText;
    Button      loginButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUIViews();
        initToolbar();

        // create account activity
        createAccountActivity();

        usernameEditText    = (EditText) findViewById(R.id.username);
        passwordEditText    = (EditText) findViewById(R.id.password);
        loginButton         = (Button) findViewById(R.id.loginbtn);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().toLowerCase();
                String password = passwordEditText.getText().toString();

                login(username, password);
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

    private void login(String username, String password) {
        if (validation(username, password)) {
            String dEmail = username + "@example.com";
            firebaseAuth.signInWithEmailAndPassword(dEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        Toast toast = Toast.makeText(LoginActivity.this, "You are successfully logged in.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();

                        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
                        sessionManagement.setSession(username);

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast toast = Toast.makeText(LoginActivity.this, "Username or password is incorrect.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                    }
                }
            });
        } else {
            Toast toast = Toast.makeText(LoginActivity.this, "Re-check your input fields.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
    }

    private void createAccountActivity () {
        Button createAccountBtn = (Button) findViewById(R.id.createAccountBtn);
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private boolean validation (String username, String password) {
        if (username.length() == 0) {
            usernameEditText.requestFocus();
            usernameEditText.setError("Username can not be empty.");
            return false;
        } else if (!username.matches("^[A-Za-z]\\w{5,29}$")) {
            usernameEditText.requestFocus();
            usernameEditText.setError("Enter a valid username.");
            return false;
        } else if (password.length() == 0) {
            passwordEditText.requestFocus();
            passwordEditText.setError("Password can not be empty.");
            return false;
        }

        return true;
    }
}

