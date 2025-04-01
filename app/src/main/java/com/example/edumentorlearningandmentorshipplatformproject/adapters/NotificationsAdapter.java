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
import com.example.edumentorlearningandmentorshipplatformproject.models.NotificationItem;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private final List<NotificationItem> notificationsList;

    public NotificationsAdapter(List<NotificationItem> notificationsList) {
        this.notificationsList = notificationsList;
    }

    @NonNull
    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ViewHolder holder, int position) {
        NotificationItem notification = notificationsList.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivStudentImage, ivChatIcon;
        TextView tvNotificationMessage, tvNotificationSubtext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivStudentImage = itemView.findViewById(R.id.ivStudentImage);
            ivChatIcon = itemView.findViewById(R.id.ivChatIcon);
            tvNotificationMessage = itemView.findViewById(R.id.tvNotificationMessage);
            tvNotificationSubtext = itemView.findViewById(R.id.tvNotificationSubtext);
        }

        public void bind(NotificationItem notification) {
            tvNotificationMessage.setText(notification.getMessage());

            String relativeTime = getRelativeTime(notification.getTimestamp());
            String subtext = notification.getStudentName() + ", " + relativeTime;
            tvNotificationSubtext.setText(subtext);

            Context context = itemView.getContext();
            if (notification.getStudentPhotoUrl() != null && !notification.getStudentPhotoUrl().isEmpty()) {
                Glide.with(context)
                        .load(notification.getStudentPhotoUrl())
                        .circleCrop()
                        .placeholder(R.drawable.ic_profile_placeholder)
                        .into(ivStudentImage);
            } else {
                ivStudentImage.setImageResource(R.drawable.ic_profile_placeholder);
            }
            ivChatIcon.setOnClickListener(v -> {

            });
        }


        private String getRelativeTime(long timestamp) {
            long now = System.currentTimeMillis();
            long diff = now - timestamp;

            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            if (minutes < 60) {
                return minutes + "m ago";
            }
            long hours = TimeUnit.MILLISECONDS.toHours(diff);
            if (hours < 24) {
                return hours + "h ago";
            }
            long days = TimeUnit.MILLISECONDS.toDays(diff);
            return days + "d ago";
        }
    }
}
