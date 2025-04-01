package com.example.edumentorlearningandmentorshipplatformproject.activities;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                                                        Toast.makeText(LoginActivity.this,
                                                                "Login Successful!",
                                                                Toast.LENGTH_SHORT).show();

                                                        String role = document.getString("role");
                                                        if (role == null) role = "student";

                                                        switch (role.toLowerCase()) {
                                                            case "admin":
                                                                startActivity(new Intent(LoginActivity.this,
                                                                        AdminDashboardActivity.class));
                                                                finish();
                                                                break;
                                                            case "mentor":
                                                                startActivity(new Intent(LoginActivity.this,
                                                                        MentorDashboardActivity.class));
                                                                finish();
                                                                break;
                                                            default:
                                                                Intent dashboardIntent = new Intent(LoginActivity.this,
                                                                        DashboardActivity.class);
                                                                dashboardIntent.putExtra("USER_NAME", userName);
                                                                startActivity(dashboardIntent);
                                                                finish();
                                                                break;
                                                        }
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
