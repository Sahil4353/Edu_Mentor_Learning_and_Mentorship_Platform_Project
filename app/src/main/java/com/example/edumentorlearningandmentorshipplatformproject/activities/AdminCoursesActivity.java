package com.example.edumentorlearningandmentorshipplatformproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class AdminCoursesActivity extends AppCompatActivity {

    private RecyclerView rvEnrolledCourses, rvTrendingCourses, rvRecommendedCourses;
    private EnrolledCoursesAdapter enrolledAdapter;
    private TrendingCoursesAdapter trendingAdapter;
    private RecommendedCoursesAdapter recommendedAdapter;
    private FirebaseFirestore db;

    private List<TrendingCourse> trendingCourses = new ArrayList<>();
    private List<RecommendedCourse> recommendedCourses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_courses);

        Toolbar toolbar = findViewById(R.id.toolbarAdminCourses);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("All Courses");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rvEnrolledCourses = findViewById(R.id.rvEnrolledCourses);
        rvTrendingCourses = findViewById(R.id.rvTrendingCourses);
        rvRecommendedCourses = findViewById(R.id.rvRecommendedCourses);
        rvEnrolledCourses.setLayoutManager(new LinearLayoutManager(this));
        rvTrendingCourses.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRecommendedCourses.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        enrolledAdapter = new EnrolledCoursesAdapter(getEnrolledCourses());
        rvEnrolledCourses.setAdapter(enrolledAdapter);
        trendingAdapter = new TrendingCoursesAdapter(this, trendingCourses);
        rvTrendingCourses.setAdapter(trendingAdapter);
        recommendedAdapter = new RecommendedCoursesAdapter(this, recommendedCourses);
        rvRecommendedCourses.setAdapter(recommendedAdapter);
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
                            updateOrAddTrending(unionTrending, new TrendingCourse(
                                    title,
                                    category,
                                    rating,
                                    price,
                                    imageRes
                            ));
                        } else if (courseType.equalsIgnoreCase("recommended")) {
                            updateOrAddRecommended(unionRecommended, new RecommendedCourse(
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

            trendingCourses.clear();
            trendingCourses.addAll(unionTrending);
            recommendedCourses.clear();
            recommendedCourses.addAll(unionRecommended);
            trendingAdapter.notifyDataSetChanged();
            recommendedAdapter.notifyDataSetChanged();
        });
    }

    private void updateDefaultCourses() {
        trendingCourses.clear();
        trendingCourses.addAll(getDefaultTrendingCourses());
        recommendedCourses.clear();
        recommendedCourses.addAll(getDefaultRecommendedCourses());
    }

    private void updateOrAddTrending(List<TrendingCourse> list, TrendingCourse newCourse) {
        boolean updated = false;
        String newTitle = newCourse.getTitle().trim();
        for (int i = 0; i < list.size(); i++) {
            TrendingCourse course = list.get(i);
            if (course.getTitle().trim().equalsIgnoreCase(newTitle)) {
                list.set(i, newCourse);
                updated = true;
                break;
            }
        }
        if (!updated) {
            list.add(newCourse);
        }
    }

    private void updateOrAddRecommended(List<RecommendedCourse> list, RecommendedCourse newCourse) {
        boolean updated = false;
        String newTitle = newCourse.getTitle().trim();
        for (int i = 0; i < list.size(); i++) {
            RecommendedCourse course = list.get(i);
            if (course.getTitle().trim().equalsIgnoreCase(newTitle)) {
                list.set(i, newCourse);
                updated = true;
                break;
            }
        }
        if (!updated) {
            list.add(newCourse);
        }
    }

    private int getImageResourceForCourse(String courseTitle) {
        if (courseTitle == null) return R.drawable.ic_default_course;
        switch (courseTitle.toLowerCase().trim()) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin_courses, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            startActivity(new Intent(AdminCoursesActivity.this, AdminDashboardActivity.class));
            finish();
            return true;
        } else if (itemId == R.id.action_add_course) {
            startActivity(new Intent(AdminCoursesActivity.this, CreateCourseActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Static default enrolled courses
    private List<EnrolledCourse> getEnrolledCourses() {
        List<EnrolledCourse> list = new ArrayList<>();
        list.add(new EnrolledCourse("Python", "Angela White", 75, 1, R.drawable.ic_python_course));
        list.add(new EnrolledCourse("Flutter", "Harry Wilson", 60, 2, R.drawable.ic_flutter_course));
        return list;
    }

    // Default trending courses
    private List<TrendingCourse> getDefaultTrendingCourses() {
        List<TrendingCourse> list = new ArrayList<>();
        list.add(new TrendingCourse("UI UX Designing", "Beginners Level | 25 Videos", 4.9f, "$200", R.drawable.ic_ui_ux_banner));
        list.add(new TrendingCourse("Digital Marketing", "Advanced Level | 30 Videos", 5.0f, "Free", R.drawable.ic_digital_marketing));
        return list;
    }

    // Default recommended courses
    private List<RecommendedCourse> getDefaultRecommendedCourses() {
        List<RecommendedCourse> list = new ArrayList<>();
        list.add(new RecommendedCourse("DevOps", "Intermediate | 20 Videos", 4.8f, "$150", R.drawable.ic_devops_banner));
        list.add(new RecommendedCourse("Advanced SEO", "Advanced | 20 Videos", 4.8f, "$350", R.drawable.ic_advanced_seo_banner));
        return list;
    }
}
