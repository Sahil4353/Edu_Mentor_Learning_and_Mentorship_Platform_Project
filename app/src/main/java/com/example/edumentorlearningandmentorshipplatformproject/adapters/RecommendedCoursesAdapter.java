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
import com.example.edumentorlearningandmentorshipplatformproject.models.RecommendedCourse;
import java.util.List;

public class RecommendedCoursesAdapter extends RecyclerView.Adapter<RecommendedCoursesAdapter.ViewHolder> {

    private final List<RecommendedCourse> recommendedCourses;
    private final Context context;

    public RecommendedCoursesAdapter(Context context, List<RecommendedCourse> recommendedCourses) {
        this.context = context;
        this.recommendedCourses = recommendedCourses;
    }

    public void addCourse(RecommendedCourse course) {
        recommendedCourses.add(course);
        notifyItemInserted(recommendedCourses.size() - 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_recommended_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecommendedCourse course = recommendedCourses.get(position);
        holder.ivCourseBanner.setImageResource(course.getImageRes());
        holder.tvCourseTitle.setText(course.getTitle());
        holder.tvCourseSubtitle.setText(course.getSubtitle());
        holder.tvPrice.setText(course.getPrice());
        holder.tvRating.setText(String.valueOf(course.getRating()));
        holder.tvPrice.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookSessionActivity.class);
            intent.putExtra("COURSE_NAME", course.getTitle());
            intent.putExtra("COURSE_PRICE", course.getPrice());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recommendedCourses.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCourseBanner;
        TextView tvCourseTitle, tvCourseSubtitle, tvPrice, tvRating;
        ImageView imgBookmark;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCourseBanner = itemView.findViewById(R.id.imgCourseBannerDevOps);
            tvCourseTitle = itemView.findViewById(R.id.tvCourseTitleDevOps);
            tvCourseSubtitle = itemView.findViewById(R.id.tvCourseSubtitleDevOps);
            tvPrice = itemView.findViewById(R.id.tvPriceDevOps);
            tvRating = itemView.findViewById(R.id.tvRatingDevOps);
            imgBookmark = itemView.findViewById(R.id.imgBookmarkDevOps);
        }
    }
}
