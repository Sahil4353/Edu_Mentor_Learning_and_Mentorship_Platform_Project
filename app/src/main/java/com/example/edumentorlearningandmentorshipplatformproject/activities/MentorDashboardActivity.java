package com.example.edumentorlearningandmentorshipplatformproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.edumentorlearningandmentorshipplatformproject.R;
import com.example.edumentorlearningandmentorshipplatformproject.models.Payment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Calendar;

public class MentorDashboardActivity extends AppCompatActivity {

    private TextView tvWelcomeText, tvTotalEarnedValue, tvLastMonthValue, tvThisMonthValue;
    private Button btnLogout, btnMyCourses;
    private ImageView ivNotifications;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_dashboard);

        tvWelcomeText       = findViewById(R.id.tvWelcomeText);
        tvTotalEarnedValue  = findViewById(R.id.tvTotalEarnedValue);
        tvLastMonthValue    = findViewById(R.id.tvLastMonthValue);
        tvThisMonthValue    = findViewById(R.id.tvThisMonthValue);
        btnLogout           = findViewById(R.id.btnLogout);
        btnMyCourses        = findViewById(R.id.btnMyCourses);
        ivNotifications     = findViewById(R.id.ivNotification);

        auth = FirebaseAuth.getInstance();
        db   = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String displayName = (currentUser.getDisplayName() != null && !currentUser.getDisplayName().isEmpty())
                    ? currentUser.getDisplayName()
                    : "Mentor";
            tvWelcomeText.setText("Welcome back, " + displayName + "!");

            db.collection("payments")
                    .whereEqualTo("mentorName", displayName)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        double totalIncome = 0.0;
                        double lastMonthIncome = 0.0;
                        double thisMonthIncome = 0.0;

                        Calendar nowCal = Calendar.getInstance();
                        int currentMonth = nowCal.get(Calendar.MONTH);
                        int currentYear  = nowCal.get(Calendar.YEAR);

                        for (QueryDocumentSnapshot doc : querySnapshot) {
                            Payment payment = doc.toObject(Payment.class);
                            double priceVal = parsePrice(payment.getCoursePrice());
                            totalIncome += priceVal;

                            Calendar payCal = Calendar.getInstance();
                            payCal.setTimeInMillis(payment.getTimestamp());
                            int payMonth = payCal.get(Calendar.MONTH);
                            int payYear  = payCal.get(Calendar.YEAR);

                            if (payYear == currentYear && payMonth == currentMonth) {
                                thisMonthIncome += priceVal;
                            } else if (payYear == currentYear && payMonth == (currentMonth - 1)) {
                                lastMonthIncome += priceVal;
                            }
                        }

                        tvTotalEarnedValue.setText("$" + totalIncome);
                        tvLastMonthValue.setText("$" + lastMonthIncome);
                        tvThisMonthValue.setText("$" + thisMonthIncome);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(MentorDashboardActivity.this,
                                "Error fetching payments: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
        }

        // Updated logout functionality: clear SharedPreferences and sign out.
        btnLogout.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            auth.signOut();

            Intent intent = new Intent(MentorDashboardActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btnMyCourses.setOnClickListener(v -> {
            Intent intent = new Intent(MentorDashboardActivity.this, MentorMyCoursesActivity.class);
            startActivity(intent);
        });

        ivNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(MentorDashboardActivity.this, NotificationsActivity.class);
            startActivity(intent);
        });
    }

    private double parsePrice(String priceStr) {
        if (priceStr == null) return 0.0;
        try {
            return Double.parseDouble(priceStr.replace("$", "").trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
