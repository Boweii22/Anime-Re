package com.smartherd.manga2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText, userNameEditText;
    private Button registerButton;
    private FirebaseAuth auth;
    private TextView loginTextView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerBtn);
        userNameEditText = findViewById(R.id.usernameEditText);
        loginTextView = findViewById(R.id.loginTextView);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        registerButton.setOnClickListener(v -> registerUser());
        loginTextView.setOnClickListener(v-> loginUser());
    }

    private void loginUser() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String username = userNameEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration successful, now save user data
                        FirebaseUser user = auth.getCurrentUser();
                        saveUserData(user.getUid(), email, username);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserData(String userId, String email, String username) {
        User user = new User(userId, email, username);

        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    // Redirect to another activity if needed
                })
                .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Error saving user data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}