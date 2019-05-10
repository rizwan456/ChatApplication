package com.example.chatapplication;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.chatapplication.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding registerBinding;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        setSupportActionBar(registerBinding.toolbar);
        getSupportActionBar().setTitle("Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUp();
    }

    private void setUp() {
        auth = FirebaseAuth.getInstance();
        registerBinding.btnRegister.setOnClickListener(v -> {
            registerBinding.progressBar.setVisibility(View.VISIBLE);
            if (registerBinding.userName.getText().toString().isEmpty()
                    || registerBinding.password.getText().toString().isEmpty()
                    || registerBinding.email.getText().toString().isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please provide valid details", Toast.LENGTH_SHORT).show();
                registerBinding.progressBar.setVisibility(View.GONE);
            } else if (registerBinding.password.getText().toString().length() < 6) {
                Toast.makeText(RegisterActivity.this, "Password must be more than 6 characters", Toast.LENGTH_SHORT).show();
                registerBinding.progressBar.setVisibility(View.GONE);
            } else {
                register(registerBinding.userName.getText().toString(),
                        registerBinding.email.getText().toString(),
                        registerBinding.password.getText().toString());
            }
        });
    }

    private void register(final String username, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        assert firebaseUser != null;
                        String userid = firebaseUser.getUid();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userid);
                        hashMap.put("username", username);
                        hashMap.put("imageURL", "defalut");

                        reference.setValue(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Intent i = new Intent(RegisterActivity.this, LogInActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                registerBinding.progressBar.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, "Registration Success!!!!!", Toast.LENGTH_SHORT).show();
                                startActivity(i);
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "You cant register with this email or password", Toast.LENGTH_SHORT).show();
                        registerBinding.progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
