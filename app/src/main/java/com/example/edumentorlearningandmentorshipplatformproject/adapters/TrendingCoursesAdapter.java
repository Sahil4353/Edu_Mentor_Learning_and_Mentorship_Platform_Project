package com.example.edumentorlearningandmentorshipplatformproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edumentorlearningandmentorshipplatformproject.R;
import com.example.edumentorlearningandmentorshipplatformproject.activities.BookSessionActivity;
import com.example.edumentorlearningandmentorshipplatformproject.models.TrendingCourse;
import java.util.List;

public class TrendingCoursesAdapter extends RecyclerView.Adapter<TrendingCoursesAdapter.ViewHolder> {

    private final List<TrendingCourse> trendingCourses;
    private final Context context;

    public TrendingCoursesAdapter(Context context, List<TrendingCourse> trendingCourses) {
        this.context = context;
        this.trendingCourses = trendingCourses;
    }

    public void addCourse(TrendingCourse course) {
        trendingCourses.add(course);
        notifyItemInserted(trendingCourses.size() - 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_trending_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrendingCourse course = trendingCourses.get(position);
        holder.ivCourseBanner.setImageResource(course.getImageRes());
        holder.tvCourseTitle.setText(course.getTitle());
        holder.tvCourseSubtitle.setText(course.getSubtitle());
        holder.tvRating.setText(String.valueOf(course.getRating()));
        holder.tvPrice.setText(course.getPrice());
        holder.tvPrice.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookSessionActivity.class);
            intent.putExtra("COURSE_NAME", course.getTitle());
            intent.putExtra("COURSE_PRICE", course.getPrice());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return trendingCourses.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCourseBanner;
        TextView tvCourseTitle, tvCourseSubtitle, tvRating, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCourseBanner = itemView.findViewById(R.id.ivCourseBanner);
            tvCourseTitle = itemView.findViewById(R.id.tvCourseTitle);
            tvCourseSubtitle = itemView.findViewById(R.id.tvCourseSubtitle);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
