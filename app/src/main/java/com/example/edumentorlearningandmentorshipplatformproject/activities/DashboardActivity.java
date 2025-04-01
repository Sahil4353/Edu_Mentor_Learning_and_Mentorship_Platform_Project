package com.example.edumentorlearningandmentorshipplatformproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edumentorlearningandmentorshipplatformproject.R;
import com.example.edumentorlearningandmentorshipplatformproject.adapters.EnrolledCoursesAdapter;
import com.example.edumentorlearningandmentorshipplatformproject.adapters.RecommendedCoursesAdapter;
import com.example.edumentorlearningandmentorshipplatformproject.adapters.TrendingCoursesAdapter;
import com.example.edumentorlearningandmentorshipplatformproject.models.EnrolledCourse;
import com.example.edumentorlearningandmentorshipplatformproject.models.RecommendedCourse;
import com.example.edumentorlearningandmentorshipplatformproject.models.TrendingCourse;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvGreeting;
    private RecyclerView rvEnrolledCourses, rvTrendingCourses, rvRecommendedCourses;
    private EnrolledCoursesAdapter enrolledAdapter;
    private TrendingCoursesAdapter trendingAdapter;
    private RecommendedCoursesAdapter recommendedAdapter;
    private Button btnLogout;
    private FirebaseFirestore db;

    private List<TrendingCourse> trendingCourses = new ArrayList<>();
    private List<RecommendedCourse> recommendedCourses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvGreeting = findViewById(R.id.tvGreeting);
        String userName = getIntent().getStringExtra("USER_NAME");
        if (userName != null && !userName.isEmpty()) {
            tvGreeting.setText("Hello, " + userName + "!");
        }

        rvEnrolledCourses = findViewById(R.id.rvEnrolledCourses);
        rvTrendingCourses = findViewById(R.id.rvTrendingCourses);
        rvRecommendedCourses = findViewById(R.id.rvRecommendedCourses);
        btnLogout = findViewById(R.id.btnLogout);

        rvEnrolledCourses.setLayoutManager(new LinearLayoutManager(this));
        rvTrendingCourses.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRecommendedCourses.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        enrolledAdapter = new EnrolledCoursesAdapter(getEnrolledCourses());
        rvEnrolledCourses.setAdapter(enrolledAdapter);
        trendingAdapter = new TrendingCoursesAdapter(this, trendingCourses);
        rvTrendingCourses.setAdapter(trendingAdapter);
        recommendedAdapter = new RecommendedCoursesAdapter(this, recommendedCourses);
        rvRecommendedCourses.setAdapter(recommendedAdapter);

        String newCourseTitle = getIntent().getStringExtra("ENROLLED_COURSE_TITLE");
        if (newCourseTitle != null && !newCourseTitle.isEmpty()) {
            int imageRes = getImageResourceForCourse(newCourseTitle);
            String newInstructor = getIntent().getStringExtra("ENROLLED_INSTRUCTOR");
            if(newInstructor == null || newInstructor.isEmpty()){
                newInstructor = "New Instructor";
            }
            EnrolledCourse newCourse = new EnrolledCourse(newCourseTitle, newInstructor, 0, 10, imageRes);
            enrolledAdapter.addCourse(newCourse);
        }

        updateDefaultCourses();

        db = FirebaseFirestore.getInstance();
        db.collection("courses").addSnapshotListener((snapshots, e) -> {
            if (e != null) return;
            List<TrendingCourse> unionTrending = new ArrayList<>(getDefaultTrendingCourses());
            List<RecommendedCourse> unionRecommended = new ArrayList<>(getDefaultRecommendedCourses());
            if (snapshots != null) {
                for (DocumentSnapshot doc : snapshots.getDocuments()) {
                    String courseType = doc.getString("courseType");
                    String title = doc.getString("title");
                    String price = doc.getString("price");
                    String category = doc.getString("category");
                    Double ratingDouble = doc.getDouble("rating");
                    float rating = (ratingDouble != null) ? ratingDouble.floatValue() : 0f;
                    int imageRes = getImageResourceForCourse(title);

                    if (courseType != null && title != null) {
                        if (category == null || category.isEmpty()) {
                            category = "Basic Level | 20 Videos";
                        }
                        if (courseType.equalsIgnoreCase("trending")) {
                            if (!containsTrending(unionTrending, title)) {
                                unionTrending.add(new TrendingCourse(
                                        title,
                                        category,
                                        rating,
                                        price,
                                        imageRes
                                ));
                            }
                        } else if (courseType.equalsIgnoreCase("recommended")) {
                            if (!containsRecommended(unionRecommended, title)) {
                                unionRecommended.add(new RecommendedCourse(
                                        title,
                                        category,
                                        rating,
                                        price,
                                        imageRes
                                ));
                            }
                        }
                    }
                }
            }
            trendingCourses.clear();
            trendingCourses.addAll(unionTrending);
            recommendedCourses.clear();
            recommendedCourses.addAll(unionRecommended);
            trendingAdapter.notifyDataSetChanged();
            recommendedAdapter.notifyDataSetChanged();
        });

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void updateDefaultCourses() {
        trendingCourses.clear();
        trendingCourses.addAll(getDefaultTrendingCourses());
        recommendedCourses.clear();
        recommendedCourses.addAll(getDefaultRecommendedCourses());
    }

    private boolean containsTrending(List<TrendingCourse> list, String title) {
        for (TrendingCourse course : list) {
            if (course.getTitle().equalsIgnoreCase(title)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsRecommended(List<RecommendedCourse> list, String title) {
        for (RecommendedCourse course : list) {
            if (course.getTitle().equalsIgnoreCase(title)) {
                return true;
            }
        }
        return false;
    }

    private int getImageResourceForCourse(String courseTitle) {
        if (courseTitle == null) return R.drawable.ic_default_course;
        switch (courseTitle.toLowerCase()) {
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

    private List<EnrolledCourse> getEnrolledCourses() {
        List<EnrolledCourse> list = new ArrayList<>();

        list.add(new EnrolledCourse("Python", "Angela White", 75, 1, R.drawable.ic_python_course));
        list.add(new EnrolledCourse("Flutter", "Harry Wilson", 60, 2, R.drawable.ic_flutter_course));
        return list;
    }

    private List<TrendingCourse> getDefaultTrendingCourses() {
        List<TrendingCourse> list = new ArrayList<>();
        list.add(new TrendingCourse("UI UX Designing", "Beginners Level | 25 Videos", 4.9f, "$200", R.drawable.ic_ui_ux_banner));
        list.add(new TrendingCourse("Digital Marketing", "Advanced Level | 30 Videos", 5.0f, "Free", R.drawable.ic_digital_marketing));
        return list;
    }

    private List<RecommendedCourse> getDefaultRecommendedCourses() {
        List<RecommendedCourse> list = new ArrayList<>();
        list.add(new RecommendedCourse("DevOps", "Intermediate | 20 Videos", 4.8f, "$150", R.drawable.ic_devops_banner));
        list.add(new RecommendedCourse("Advanced SEO", "Advanced | 20 Videos", 4.8f, "$350", R.drawable.ic_advanced_seo_banner));
        return list;
    }
}
