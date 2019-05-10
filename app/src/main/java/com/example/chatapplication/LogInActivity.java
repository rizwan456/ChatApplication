package com.example.chatapplication;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.chatapplication.databinding.ActivityLogInBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
    ActivityLogInBinding logInBinding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logInBinding = DataBindingUtil.setContentView(this, R.layout.activity_log_in);
        setUp();
    }

    private void setUp() {
        auth = FirebaseAuth.getInstance();
        logInBinding.btnLogin.setOnClickListener(v -> {
            logInBinding.progressBar.setVisibility(View.VISIBLE);
            if (logInBinding.password.getText().toString().isEmpty()
                    || logInBinding.email.getText().toString().isEmpty()) {
                Toast.makeText(LogInActivity.this, "Please provide valid details", Toast.LENGTH_SHORT).show();
                logInBinding.progressBar.setVisibility(View.GONE);
            } else {
                logIn(logInBinding.email.getText().toString(), logInBinding.password.getText().toString());
            }
        });
    }

    private void logIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent i = new Intent(LogInActivity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        logInBinding.progressBar.setVisibility(View.GONE);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(LogInActivity.this, "Authentication Fails Please Try Again", Toast.LENGTH_SHORT).show();
                        logInBinding.progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
