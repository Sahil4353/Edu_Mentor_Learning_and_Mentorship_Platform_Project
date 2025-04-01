package com.example.edumentorlearningandmentorshipplatformproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.edumentorlearningandmentorshipplatformproject.R;
import com.example.edumentorlearningandmentorshipplatformproject.models.Course;

import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseViewHolder> {

    private List<Course> courses;

    public CoursesAdapter(List<Course> courses) {
        this.courses = courses;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.tvTitle.setText(course.getTitle());
        holder.tvCategory.setText(course.getCategory());
        holder.ivCourseImage.setImageResource(course.getImageResId());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory;
        ImageView ivCourseImage;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvCourseTitle);
            tvCategory = itemView.findViewById(R.id.tvCourseCategory);
            ivCourseImage = itemView.findViewById(R.id.ivCourseImage);
        }
    }
}
