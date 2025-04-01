package com.example.edumentorlearningandmentorshipplatformproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.edumentorlearningandmentorshipplatformproject.R;
import com.example.edumentorlearningandmentorshipplatformproject.activities.UpcomingSessionsActivity;
import com.example.edumentorlearningandmentorshipplatformproject.models.Course;

import java.util.List;

public class MentorCoursesAdapter extends RecyclerView.Adapter<MentorCoursesAdapter.ViewHolder> {

    private final List<Course> coursesList;

    public MentorCoursesAdapter(List<Course> coursesList) {
        this.coursesList = coursesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mentor_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = coursesList.get(position);
        holder.bind(course);
    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCourseImage;
        TextView tvCourseTitle, tvCourseCategory;
        Button btnUpcomingSession;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCourseImage       = itemView.findViewById(R.id.ivCourseImage);
            tvCourseTitle       = itemView.findViewById(R.id.tvCourseTitle);
            tvCourseCategory    = itemView.findViewById(R.id.tvCourseCategory);
            btnUpcomingSession  = itemView.findViewById(R.id.btnAddUpcomingSession);
        }

        public void bind(Course course) {

            tvCourseTitle.setText(course.getTitle());
            tvCourseCategory.setText(course.getCategory());
            ivCourseImage.setImageResource(course.getImageResId());


            btnUpcomingSession.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, UpcomingSessionsActivity.class);
                intent.putExtra("COURSE_NAME", course.getTitle());
                context.startActivity(intent);
            });
        }
    }
}
