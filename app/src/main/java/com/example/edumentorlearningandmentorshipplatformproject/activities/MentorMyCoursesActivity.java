package com.example.edumentorlearningandmentorshipplatformproject.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edumentorlearningandmentorshipplatformproject.R;
import com.example.edumentorlearningandmentorshipplatformproject.adapters.MentorCoursesAdapter;
import com.example.edumentorlearningandmentorshipplatformproject.models.Course;
import com.example.edumentorlearningandmentorshipplatformproject.models.Payment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MentorMyCoursesActivity extends AppCompatActivity {

    private ImageView ivBackArrow;
    private TextView tvTitle;
    private RecyclerView rvMyCourses;
    private MentorCoursesAdapter coursesAdapter;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private List<Course> myCoursesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_my_courses);

        ivBackArrow = findViewById(R.id.ivBackArrow);
        tvTitle = findViewById(R.id.tvTitle);
        rvMyCourses = findViewById(R.id.rvMyCourses);

        rvMyCourses.setLayoutManager(new LinearLayoutManager(this));
        myCoursesList = new ArrayList<>();
        coursesAdapter = new MentorCoursesAdapter(myCoursesList);
        rvMyCourses.setAdapter(coursesAdapter);

        tvTitle.setText("My Courses");

        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String displayName = currentUser.getDisplayName();
            if (displayName == null || displayName.isEmpty()) {
                displayName = "Mentor";
            }

            db.collection("payments")
                    .whereEqualTo("mentorName", displayName)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        Set<String> courseTitles = new HashSet<>();

                        for (QueryDocumentSnapshot doc : querySnapshot) {
                            Payment payment = doc.toObject(Payment.class);
                            if (payment.getCourseName() != null) {
                                courseTitles.add(payment.getCourseName());
                            }
                        }

                        for (String title : courseTitles) {
                            int imageRes = getCourseImageRes(title);
                            String courseDesc = getCourseDescription(title);
                            Course course = new Course(title, courseDesc, imageRes);
                            myCoursesList.add(course);
                        }

                        coursesAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(MentorMyCoursesActivity.this,
                                    "Failed to load mentor courses: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show()
                    );
        }
    }

    private int getCourseImageRes(String courseTitle) {
        if (courseTitle == null) return R.drawable.ic_default_course;
        String normalizedTitle = courseTitle.trim().toLowerCase();

        switch (normalizedTitle) {
            case "ui ux designing":
                return R.drawable.ic_ui_ux_banner;
            case "digital marketing":
                return R.drawable.ic_digital_marketing;
            case "devops":
                return R.drawable.ic_devops_banner;
            case "advanced seo":
                return R.drawable.ic_advanced_seo_banner;
            case "python":
                return R.drawable.ic_python_course;
            case "flutter":
                return R.drawable.ic_flutter_course;
            default:
                return R.drawable.ic_default_course;
        }
    }

    private String getCourseDescription(String courseTitle) {
        if (courseTitle == null) return "No description available";
        String normalizedTitle = courseTitle.trim().toLowerCase();

        switch (normalizedTitle) {
            case "ui ux designing":
                return "Beginners Level | 25 Videos";
            case "digital marketing":
                return "Advanced Level | 30 Videos";
            case "devops":
                return "Intermediate Level | 20 Videos";
            case "advanced seo":
                return "Advanced Level | 20 Videos";
            case "python":
                return "Beginner Friendly | 50 Videos";
            case "flutter":
                return "Beginner Friendly | 60 Videos";
            default:
                return "Category or short description here";
        }
    }
}
