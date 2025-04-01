package com.example.edumentorlearningandmentorshipplatformproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.edumentorlearningandmentorshipplatformproject.R;
import com.example.edumentorlearningandmentorshipplatformproject.models.Payment;
import com.example.edumentorlearningandmentorshipplatformproject.models.EnrolledCourse;
import com.example.edumentorlearningandmentorshipplatformproject.room.AppDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PaymentActivity extends AppCompatActivity {

    private EditText etCardNumber, etExpiry, etCvc, etNameOnCard, etZipCode, etCoupon;
    private TextView tvCourseFee, tvTax, tvTotal;
    private Button btnSecurePayment;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private String courseName, coursePrice, mentorName;
    private String sessionDate, sessionTime;
    private String userId; // user id passed from BookSessionActivity or from auth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        etCardNumber = findViewById(R.id.etCardNumber);
        etExpiry = findViewById(R.id.etExpiry);
        etCvc = findViewById(R.id.etCvc);
        etNameOnCard = findViewById(R.id.etNameOnCard);
        etZipCode = findViewById(R.id.etZipCode);
        etCoupon = findViewById(R.id.etCoupon);
        tvCourseFee = findViewById(R.id.tvCourseFee);
        tvTax = findViewById(R.id.tvTax);
        tvTotal = findViewById(R.id.tvTotal);
        btnSecurePayment = findViewById(R.id.btnSecurePayment);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Retrieve extras from intent
        courseName = getIntent().getStringExtra("COURSE_NAME");
        coursePrice = getIntent().getStringExtra("COURSE_PRICE");
        mentorName = getIntent().getStringExtra("MENTOR_NAME");
        sessionDate = getIntent().getStringExtra("SESSION_DATE");
        sessionTime = getIntent().getStringExtra("SESSION_TIME");
        userId = getIntent().getStringExtra("USER_ID");

        if (courseName == null) courseName = "Default Course";
        if (coursePrice == null) coursePrice = "$0";
        if (mentorName == null) mentorName = "";
        if (sessionDate == null) sessionDate = "";
        if (sessionTime == null) sessionTime = "";
        if (userId == null || userId.isEmpty()) {
            userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "UnknownStudent";
        }

        tvCourseFee.setText(coursePrice);
        tvTax.setText("$0");
        tvTotal.setText(coursePrice);

        etExpiry.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if (isFormatting) return;
                isFormatting = true;
                String digits = s.toString().replaceAll("[^\\d]", "");
                String formatted;
                if (digits.length() >= 2) {
                    formatted = digits.substring(0, 2);
                    if (digits.length() > 2) {
                        formatted += "/" + digits.substring(2);
                    }
                } else {
                    formatted = digits;
                }
                if (formatted.length() > 5) {
                    formatted = formatted.substring(0, 5);
                }
                etExpiry.setText(formatted);
                etExpiry.setSelection(formatted.length());
                isFormatting = false;
            }
        });

        btnSecurePayment.setOnClickListener(v -> {
            String cardNumber = etCardNumber.getText().toString().trim();
            String expiry = etExpiry.getText().toString().trim();
            String cvc = etCvc.getText().toString().trim();
            String nameOnCard = etNameOnCard.getText().toString().trim();
            String zipCode = etZipCode.getText().toString().trim();

            if (cardNumber.length() != 16) {
                Toast.makeText(this, "Invalid 16-digit card number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (expiry.length() != 5 || !expiry.contains("/")) {
                Toast.makeText(this, "Enter expiry in MM/YY format", Toast.LENGTH_SHORT).show();
                return;
            }
            if (cvc.length() != 3) {
                Toast.makeText(this, "Invalid 3-digit CVC", Toast.LENGTH_SHORT).show();
                return;
            }
            if (zipCode.length() != 4) {
                Toast.makeText(this, "Invalid 4-digit ZIP code", Toast.LENGTH_SHORT).show();
                return;
            }
            if (nameOnCard.isEmpty()) {
                Toast.makeText(this, "Name on card is required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Use the passed userId (or fallback to auth)
            String studentUid = userId;

            Payment payment = new Payment(
                    studentUid,
                    mentorName,
                    courseName,
                    coursePrice,
                    System.currentTimeMillis(),
                    sessionDate,
                    sessionTime
            );

            db.collection("payments")
                    .add(payment)
                    .addOnSuccessListener(docRef -> {
                        Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show();

                        // Create a new enrolled course record using default values
                        // Here, 0 progress and 10 hoursRemaining are used as defaults.
                        // Adjust imageResId as needed – using a default drawable resource.
                        int imageResId = R.drawable.ic_default_course;
                        EnrolledCourse enrolledCourse = new EnrolledCourse(courseName, mentorName, 0, 10, imageResId, studentUid);

                        // Insert the enrolled course into Room on a background thread
                        new Thread(() -> {
                            AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
                            appDatabase.enrolledCourseDao().insertEnrolledCourse(enrolledCourse);
                        }).start();

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("COURSE_NAME", courseName);
                        resultIntent.putExtra("MENTOR_NAME", mentorName);
                        resultIntent.putExtra("USER_ID", studentUid);
                        setResult(RESULT_OK, resultIntent);

                        Intent intent = new Intent(this, ThankYouActivity.class);
                        intent.putExtra("COURSE_NAME", courseName);
                        intent.putExtra("COURSE_PRICE", coursePrice);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Payment failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
