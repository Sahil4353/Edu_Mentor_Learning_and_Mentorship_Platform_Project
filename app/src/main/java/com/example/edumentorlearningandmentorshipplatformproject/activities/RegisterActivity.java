package com.example.edumentorlearningandmentorshipplatformproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etConfirmPassword;
    private Button btnContinue;
    private TextView tvError, tvLogin;
    private RadioGroup rgUserType;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnContinue = findViewById(R.id.btnContinue);
        tvError = findViewById(R.id.tvError);
        tvLogin = findViewById(R.id.tvLogin);
        rgUserType = findViewById(R.id.rgUserType);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = etName.getText().toString().trim();
                final String email = etEmail.getText().toString().trim();
                final String password = etPassword.getText().toString().trim();
                final String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    if (tvError != null) {
                        tvError.setText("Please fill in all fields.");
                        tvError.setVisibility(View.VISIBLE);
                    }
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    if (tvError != null) {
                        tvError.setText("Passwords do not match.");
                        tvError.setVisibility(View.VISIBLE);
                    }
                    return;
                }

                if (tvError != null) {
                    tvError.setVisibility(View.GONE);
                }

                final String role;
                int selectedId = rgUserType.getCheckedRadioButtonId();
                if (selectedId == R.id.rbStudent) {
                    role = "student";
                } else if (selectedId == R.id.rbMentor) {
                    role = "mentor";
                } else if (selectedId == R.id.rbAdmin) {
                    role = "admin";
                } else {
                    role = "student";
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name)
                                                .build();
                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> updateTask) {
                                                        if (updateTask.isSuccessful()) {
                                                            Map<String, Object> userMap = new HashMap<>();
                                                            userMap.put("name", name);
                                                            userMap.put("email", email);
                                                            userMap.put("role", role);

                                                            db.collection("users").document(user.getUid())
                                                                    .set(userMap)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> firestoreTask) {
                                                                            if (firestoreTask.isSuccessful()) {
                                                                                Toast.makeText(RegisterActivity.this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();
                                                                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                                                finish();
                                                                            } else {
                                                                                if (tvError != null) {
                                                                                    tvError.setText("Failed to store user data: " + firestoreTask.getException().getMessage());
                                                                                    tvError.setVisibility(View.VISIBLE);
                                                                                }
                                                                            }
                                                                        }
                                                                    });
                                                        } else {
                                                            if (tvError != null) {
                                                                tvError.setText("Profile update failed: " + updateTask.getException().getMessage());
                                                                tvError.setVisibility(View.VISIBLE);
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                } else {
                                    if (tvError != null) {
                                        tvError.setText("Registration failed: " + task.getException().getMessage());
                                        tvError.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        });
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}
