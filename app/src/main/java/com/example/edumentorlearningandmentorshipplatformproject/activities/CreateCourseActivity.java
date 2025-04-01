package com.example.edumentorlearningandmentorshipplatformproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.edumentorlearningandmentorshipplatformproject.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateCourseActivity extends AppCompatActivity {

    private EditText etCourseTitle, etCourseCategory, etCourseType, etCourseRating, etCoursePrice;
    private Button btnSaveCourse;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        etCourseTitle = findViewById(R.id.etCourseTitle);
        etCourseCategory = findViewById(R.id.etCourseCategory);
        etCourseType = findViewById(R.id.etCourseType);
        etCourseRating = findViewById(R.id.etCourseRating);
        etCoursePrice = findViewById(R.id.etCoursePrice);
        btnSaveCourse = findViewById(R.id.btnSaveCourse);

        db = FirebaseFirestore.getInstance();

        btnSaveCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCourse();
            }
        });
    }

    private void saveCourse() {
        String title = etCourseTitle.getText().toString().trim();
        String category = etCourseCategory.getText().toString().trim();
        String courseType = etCourseType.getText().toString().trim();
        String ratingStr = etCourseRating.getText().toString().trim();
        String price = etCoursePrice.getText().toString().trim();

        if (title.isEmpty() || category.isEmpty() || courseType.isEmpty() ||
                ratingStr.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        float rating;
        try {
            rating = Float.parseFloat(ratingStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid rating value", Toast.LENGTH_SHORT).show();
            return;
        }


        Map<String, Object> courseData = new HashMap<>();
        courseData.put("title", title);
        courseData.put("category", category);
        courseData.put("courseType", courseType);
        courseData.put("rating", rating);
        courseData.put("price", price);

        db.collection("courses")
                .add(courseData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(CreateCourseActivity.this, "Course created!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateCourseActivity.this, AdminCoursesActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CreateCourseActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
