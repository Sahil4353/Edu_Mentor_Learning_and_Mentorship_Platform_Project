package com.example.edumentorlearningandmentorshipplatformproject.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edumentorlearningandmentorshipplatformproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private TextView tvError;
    private EditText etEmail, etPassword;
    private ImageView ivTogglePassword;
    private CheckBox cbRememberMe;
    private TextView tvForgotPassword, tvSignUp;
    private Button btnContinue;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private boolean isPasswordVisible = false;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "UserPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);


        if (sharedPreferences.contains("user_id")) {
            String role = sharedPreferences.getString("role", "student");
            Intent intent;
            if ("admin".equalsIgnoreCase(role)) {
                intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
            } else if ("mentor".equalsIgnoreCase(role)) {
                intent = new Intent(LoginActivity.this, MentorDashboardActivity.class);
            } else {
                intent = new Intent(LoginActivity.this, DashboardActivity.class);
                intent.putExtra("USER_NAME", sharedPreferences.getString("user_name", "Student"));
            }
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        tvError = findViewById(R.id.tvError);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
        btnContinue = findViewById(R.id.btnContinue);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ivTogglePassword.setOnClickListener(view -> {
            if (!isPasswordVisible) {
                etPassword.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                isPasswordVisible = true;
            } else {
                etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT
                        | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                isPasswordVisible = false;
            }
            etPassword.setSelection(etPassword.length());
        });

        tvForgotPassword.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter your email address to reset your password.", Toast.LENGTH_SHORT).show();
                return;
            }
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Password reset email sent! Check your inbox.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Error: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error."), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        btnContinue.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty()) {
                tvError.setText("Please enter your email and password.");
                tvError.setVisibility(View.VISIBLE);
                return;
            }
            tvError.setVisibility(View.GONE);

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser currentUser = auth.getCurrentUser();
                                if (currentUser != null) {
                                    db.collection("users")
                                            .document(currentUser.getUid())
                                            .get()
                                            .addOnCompleteListener(userTask -> {
                                                if (userTask.isSuccessful() && userTask.getResult() != null) {
                                                    DocumentSnapshot document = userTask.getResult();
                                                    if (document.exists()) {

                                                        String status = document.getString("status");
                                                        if ("suspended".equalsIgnoreCase(status)) {
                                                            Toast.makeText(LoginActivity.this,
                                                                    "Your account is suspended.",
                                                                    Toast.LENGTH_SHORT).show();
                                                            auth.signOut();
                                                            return;
                                                        }

                                                        String userName = document.getString("name");
                                                        if (userName == null || userName.isEmpty()) {
                                                            userName = "Student";
                                                        }

                                                        String role = document.getString("role");
                                                        if (role == null) role = "student";


                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putString("user_id", currentUser.getUid());
                                                        editor.putString("user_name", userName);
                                                        editor.putString("email", email);
                                                        editor.putString("role", role);
                                                        if (status != null) {
                                                            editor.putString("status", status);
                                                        }
                                                        editor.apply();

                                                        Toast.makeText(LoginActivity.this,
                                                                "Login Successful!",
                                                                Toast.LENGTH_SHORT).show();


                                                        Intent intent;
                                                        switch (role.toLowerCase()) {
                                                            case "admin":
                                                                intent = new Intent(LoginActivity.this,
                                                                        AdminDashboardActivity.class);
                                                                break;
                                                            case "mentor":
                                                                intent = new Intent(LoginActivity.this,
                                                                        MentorDashboardActivity.class);
                                                                break;
                                                            default:
                                                                intent = new Intent(LoginActivity.this,
                                                                        DashboardActivity.class);
                                                                intent.putExtra("USER_NAME", userName);
                                                                break;
                                                        }
                                                        startActivity(intent);
                                                        finish();
                                                    } else {

                                                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                                        intent.putExtra("USER_NAME", "Student");
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                } else {
                                                    Toast.makeText(LoginActivity.this,
                                                            "Failed to load user data.",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                tvError.setText("Authentication failed: " +
                                        (task.getException() != null ? task.getException().getMessage() : ""));
                                tvError.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        });

        tvSignUp.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}
