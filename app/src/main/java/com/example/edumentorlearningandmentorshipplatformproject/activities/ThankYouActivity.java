package com.example.edumentorlearningandmentorshipplatformproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.edumentorlearningandmentorshipplatformproject.R;

public class ThankYouActivity extends AppCompatActivity {

    private ImageView ivBack, ivGraduationHat;
    private TextView tvThanksTitle, tvCongrats, tvEnrolledInfo;
    private Button btnStartLearning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        ivBack = findViewById(R.id.ivBack);
        ivGraduationHat = findViewById(R.id.ivGraduationHat);
        tvThanksTitle = findViewById(R.id.tvThanksTitle);
        tvCongrats = findViewById(R.id.tvCongrats);
        tvEnrolledInfo = findViewById(R.id.tvEnrolledInfo);
        btnStartLearning = findViewById(R.id.btnStartLearning);

        String courseNameFromIntent = getIntent().getStringExtra("COURSE_NAME");
        String coursePriceFromIntent = getIntent().getStringExtra("COURSE_PRICE");

        final String courseName = (courseNameFromIntent == null || courseNameFromIntent.isEmpty())
                ? "Default Course" : courseNameFromIntent;
        final String coursePrice = (coursePriceFromIntent == null || coursePriceFromIntent.isEmpty())
                ? "$0" : coursePriceFromIntent;

        String info = "You’ve successfully enrolled in the course: " + courseName +
                ". Your order total was " + coursePrice + ".";
        tvEnrolledInfo.setText(info);

        ivBack.setOnClickListener(v -> {
            Intent intent = new Intent(ThankYouActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });

        btnStartLearning.setOnClickListener(v -> {
            Intent intent = new Intent(ThankYouActivity.this, DashboardActivity.class);
            intent.putExtra("ENROLLED_COURSE_TITLE", courseName);
            startActivity(intent);
            finish();
        });
    }
}
