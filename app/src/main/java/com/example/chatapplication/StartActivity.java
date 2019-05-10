package com.example.chatapplication;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.chatapplication.databinding.ActivityStartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class StartActivity extends AppCompatActivity {

    ActivityStartBinding startBinding;
    Intent i;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startBinding = DataBindingUtil.setContentView(this, R.layout.activity_start);
        setUp();
    }

    private void setUp() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }

        startBinding.btnLogin.setOnClickListener(v -> {
            i = new Intent(this, LogInActivity.class);
            startActivity(i);
        });
        startBinding.btnRegister.setOnClickListener(v -> {
            i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });
    }
}
