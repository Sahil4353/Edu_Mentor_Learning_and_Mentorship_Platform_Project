package com.example.edumentorlearningandmentorshipplatformproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.edumentorlearningandmentorshipplatformproject.activities.DashboardActivity;
import com.example.edumentorlearningandmentorshipplatformproject.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            startActivity(new Intent(this, DashboardActivity.class));
        }
        finish();
    }
}
