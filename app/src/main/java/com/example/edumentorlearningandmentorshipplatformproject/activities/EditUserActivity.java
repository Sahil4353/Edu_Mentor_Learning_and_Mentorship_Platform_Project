package com.example.edumentorlearningandmentorshipplatformproject.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edumentorlearningandmentorshipplatformproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditUserActivity extends AppCompatActivity {

    private EditText etUserName, etUserEmail, etUserPassword;
    private Button btnSaveUser;
    private String userId;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private boolean isCurrentUser = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        etUserName     = findViewById(R.id.etUserName);
        etUserEmail    = findViewById(R.id.etUserEmail);
        etUserPassword = findViewById(R.id.etUserPassword);
        btnSaveUser    = findViewById(R.id.btnSaveUser);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        userId = getIntent().getStringExtra("USER_ID");
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "No user ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null && currentUser.getUid().equals(userId)) {
            isCurrentUser = true;
        }

        loadUserData();

        btnSaveUser.setOnClickListener(v -> saveUserData());
    }

    private void loadUserData() {
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String name     = doc.getString("name");
                        String email    = doc.getString("email");
                        String password = doc.getString("password");

                        if (name != null) etUserName.setText(name);
                        if (email != null) etUserEmail.setText(email);
                        if (password != null) etUserPassword.setText(password);
                    } else {
                        Toast.makeText(EditUserActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditUserActivity.this, "Failed to load user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void saveUserData() {
        String newName = etUserName.getText().toString().trim();
        String newEmail = etUserEmail.getText().toString().trim();
        String newPassword = etUserPassword.getText().toString().trim();

        if (newName.isEmpty() || newEmail.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isCurrentUser) {
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null && !newEmail.equals(currentUser.getEmail())) {
                currentUser.updateEmail(newEmail)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                updateFirestoreUser(newName, newEmail, newPassword);
                            } else {
                                Toast.makeText(EditUserActivity.this,
                                        "Failed to update auth email: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                updateFirestoreUser(newName, newEmail, newPassword);
            }
        } else {
            updateFirestoreUser(newName, newEmail, newPassword);
            Toast.makeText(this, "Firestore updated. To change login email for other users, use admin tools.", Toast.LENGTH_LONG).show();
        }
    }

    private void updateFirestoreUser(String newName, String newEmail, String newPassword) {
        db.collection("users")
                .document(userId)
                .update("name", newName,
                        "email", newEmail,
                        "password", newPassword)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditUserActivity.this, "User updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditUserActivity.this, "Failed to update user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
