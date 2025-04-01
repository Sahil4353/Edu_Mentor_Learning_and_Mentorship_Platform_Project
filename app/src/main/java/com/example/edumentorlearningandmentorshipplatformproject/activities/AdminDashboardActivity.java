package com.example.edumentorlearningandmentorshipplatformproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.edumentorlearningandmentorshipplatformproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashSet;
import java.util.Set;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView tvAdminTitle;
    private TextView tvTotalUsersValue, tvTotalUsersSubtitle;
    private TextView tvActiveCoursesValue, tvActiveCoursesSubtitle;
    private TextView tvSessionTimeValue, tvSessionTimeSubtitle;
    private ImageView ivChartUsers, ivChartCourses, ivChartTime;
    private BottomNavigationView bottomNavAdmin;
    private Button btnLogout;
    private FirebaseFirestore db;
    private static final int FIXED_SESSION_DURATION = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        tvAdminTitle           = findViewById(R.id.tvAdminTitle);
        tvTotalUsersValue      = findViewById(R.id.tvTotalUsersValue);
        tvTotalUsersSubtitle   = findViewById(R.id.tvTotalUsersSubtitle);
        ivChartUsers           = findViewById(R.id.ivChartUsers);

        tvActiveCoursesValue   = findViewById(R.id.tvActiveCoursesValue);
        tvActiveCoursesSubtitle= findViewById(R.id.tvActiveCoursesSubtitle);
        ivChartCourses         = findViewById(R.id.ivChartCourses);

        tvSessionTimeValue     = findViewById(R.id.tvSessionTimeValue);
        tvSessionTimeSubtitle  = findViewById(R.id.tvSessionTimeSubtitle);
        ivChartTime            = findViewById(R.id.ivChartTime);

        bottomNavAdmin         = findViewById(R.id.bottomNavAdmin);
        btnLogout              = findViewById(R.id.btnLogout);

        db = FirebaseFirestore.getInstance();

        loadTotalUsers();
        loadActiveCourses();
        loadSessionTime();

        bottomNavAdmin.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_dashboard) {
                return true;
            } else if (itemId == R.id.action_courses) {
                startActivity(new Intent(this, AdminCoursesActivity.class));
                return true;
            } else if (itemId == R.id.action_users) {
                startActivity(new Intent(this, AdminUsersActivity.class));
                return true;
            }
            return false;
        });

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardActivity.this, LoginActivity.class));
            finish();
        });
    }
    private void loadTotalUsers() {
        db.collection("users")
                .get()
                .addOnSuccessListener((QuerySnapshot querySnapshot) -> {
                    int count = querySnapshot.size();
                    tvTotalUsersValue.setText(String.valueOf(count));
                    tvTotalUsersSubtitle.setText("Last 30 Days +12%");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminDashboardActivity.this,
                            "Failed to load total users: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void loadActiveCourses() {
        db.collection("payments")
                .get()
                .addOnSuccessListener((QuerySnapshot querySnapshot) -> {
                    Set<String> courseSet = new HashSet<>();
                    querySnapshot.getDocuments().forEach(doc -> {
                        String courseName = doc.getString("courseName");
                        if (courseName != null && !courseName.isEmpty()) {
                            courseSet.add(courseName);
                        }
                    });
                    int count = courseSet.size();
                    tvActiveCoursesValue.setText(String.valueOf(count));
                    tvActiveCoursesSubtitle.setText("Last 30 Days +8%");
                    if (count == 0) {
                        Toast.makeText(AdminDashboardActivity.this, "No active courses found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminDashboardActivity.this,
                            "Failed to load courses: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void loadSessionTime() {
        long thirtyDaysAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);
        db.collection("payments")
                .whereGreaterThan("timestamp", thirtyDaysAgo)
                .get()
                .addOnSuccessListener((QuerySnapshot querySnapshot) -> {
                    int enrollmentCount = querySnapshot.size();
                    int totalMinutes = enrollmentCount * FIXED_SESSION_DURATION;
                    String displayTime = convertMinutesToHHmm(totalMinutes);
                    tvSessionTimeValue.setText(displayTime);
                    tvSessionTimeSubtitle.setText("Last 30 Days +4%");
                    if (enrollmentCount == 0) {
                        Toast.makeText(AdminDashboardActivity.this, "No enrollments found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminDashboardActivity.this,
                            "Failed to load session time: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private String convertMinutesToHHmm(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }
}
