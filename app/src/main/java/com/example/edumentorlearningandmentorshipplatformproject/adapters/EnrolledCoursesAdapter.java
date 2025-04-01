package com.example.edumentorlearningandmentorshipplatformproject.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.edumentorlearningandmentorshipplatformproject.R;
import com.example.edumentorlearningandmentorshipplatformproject.activities.VideoPlayerActivity;
import com.example.edumentorlearningandmentorshipplatformproject.models.EnrolledCourse;
import java.util.List;

public class EnrolledCoursesAdapter extends RecyclerView.Adapter<EnrolledCoursesAdapter.ViewHolder> {

    private final List<EnrolledCourse> enrolledCourses;

    public EnrolledCoursesAdapter(List<EnrolledCourse> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_combined_enrolled_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EnrolledCourse course = enrolledCourses.get(position);
        holder.imgCourseBanner.setImageResource(course.getImageResId());
        holder.tvCourseTitle.setText(course.getTitle());
        holder.tvInstructor.setText(course.getInstructor());
        holder.tvHoursRemaining.setText(course.getHoursRemaining() + " Hours Remaining");
        holder.progressCourse.setProgress(course.getProgress());
        holder.tvProgressPercent.setText(course.getProgress() + "%");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), VideoPlayerActivity.class);
            if ("Flutter".equals(course.getTitle())) {
                intent.putExtra("VIDEO_ID", "C-fKAzdTrLU");
            } else {
                intent.putExtra("VIDEO_ID", "8DvywoWv6fI");
            }
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return enrolledCourses.size();
    }

    // Method to add a single course to the list.
    public void addCourse(EnrolledCourse course) {
        enrolledCourses.add(course);
        notifyItemInserted(enrolledCourses.size() - 1);
    }

    // New method to update the entire list of courses.
    public void setCourses(List<EnrolledCourse> courses) {
        enrolledCourses.clear();
        enrolledCourses.addAll(courses);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCourseBanner;
        TextView tvCourseTitle, tvInstructor, tvHoursRemaining, tvProgressPercent;
        ProgressBar progressCourse;

        ViewHolder(View itemView) {
            super(itemView);
            imgCourseBanner = itemView.findViewById(R.id.imgCourseBanner);
            tvCourseTitle = itemView.findViewById(R.id.tvCourseTitle);
            tvInstructor = itemView.findViewById(R.id.tvInstructor);
            tvHoursRemaining = itemView.findViewById(R.id.tvHoursRemaining);
            tvProgressPercent = itemView.findViewById(R.id.tvProgressPercent);
            progressCourse = itemView.findViewById(R.id.progressCourse);
        }
    }
}
