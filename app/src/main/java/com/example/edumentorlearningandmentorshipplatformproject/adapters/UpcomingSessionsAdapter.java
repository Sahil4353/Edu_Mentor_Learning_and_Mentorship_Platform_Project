package com.example.edumentorlearningandmentorshipplatformproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.edumentorlearningandmentorshipplatformproject.R;
import com.example.edumentorlearningandmentorshipplatformproject.models.UpcomingSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.List;

public class UpcomingSessionsAdapter extends RecyclerView.Adapter<UpcomingSessionsAdapter.ViewHolder> {

    private final List<UpcomingSession> sessionList;

    public UpcomingSessionsAdapter(List<UpcomingSession> sessionList) {
        this.sessionList = sessionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_upcoming_session, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UpcomingSession session = sessionList.get(position);
        holder.bind(session);
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        TextView tvUserName, tvSessionDateTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvSessionDateTime = itemView.findViewById(R.id.tvSessionDateTime);
        }

        public void bind(UpcomingSession session) {
            tvUserName.setText(session.getUserName());
            String dateTimeLine = formatDateTime(session.getDate(), session.getTime());
            tvSessionDateTime.setText(dateTimeLine);
            Context context = itemView.getContext();
            if (session.getImageUrl() != null && !session.getImageUrl().isEmpty()) {
                Glide.with(context)
                        .load(session.getImageUrl())
                        .circleCrop()
                        .placeholder(R.drawable.ic_profile_placeholder)
                        .into(ivProfileImage);
            } else {
                ivProfileImage.setImageResource(R.drawable.ic_profile_placeholder);
            }
        }

        private String formatDateTime(String dateStr, String timeStr) {
            String finalDate = "";
            SimpleDateFormat dateIn = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            SimpleDateFormat dateOut = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
            try {
                Date parsedDate = dateIn.parse(dateStr);
                if (parsedDate != null) {
                    finalDate = dateOut.format(parsedDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String finalTime = timeStr;
            SimpleDateFormat timeIn = new SimpleDateFormat("HH:mm", Locale.getDefault());
            SimpleDateFormat timeOut = new SimpleDateFormat("h:mm a", Locale.getDefault());
            try {
                Date parsedTime = timeIn.parse(timeStr);
                if (parsedTime != null) {
                    finalTime = timeOut.format(parsedTime);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return finalDate + " at " + finalTime;
        }
    }
}
