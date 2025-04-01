package com.example.edumentorlearningandmentorshipplatformproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.edumentorlearningandmentorshipplatformproject.R;
import com.example.edumentorlearningandmentorshipplatformproject.activities.EditUserActivity;
import com.example.edumentorlearningandmentorshipplatformproject.models.User;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class AdminUsersAdapter extends RecyclerView.Adapter<AdminUsersAdapter.ViewHolder> {

    private final List<User> originalList;
    private final List<User> filteredList;
    private final Context context;
    private final FirebaseFirestore db;

    public AdminUsersAdapter(Context context, List<User> userList) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.originalList = new ArrayList<>(userList);
        this.filteredList = new ArrayList<>(userList);
    }

    public void setFullUserList(List<User> newList) {
        originalList.clear();
        originalList.addAll(newList);
        filteredList.clear();
        filteredList.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdminUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_admin_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminUsersAdapter.ViewHolder holder, int position) {
        holder.bind(filteredList.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            String lower = query.toLowerCase();
            for (User user : originalList) {
                if ((user.getName() != null && user.getName().toLowerCase().contains(lower))
                        || (user.getEmail() != null && user.getEmail().toLowerCase().contains(lower))) {
                    filteredList.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserEmail, tvUserRole;
        SwitchCompat switchSuspend;
        Button btnEditUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvUserRole = itemView.findViewById(R.id.tvUserRole);
            switchSuspend = itemView.findViewById(R.id.switchSuspend);
            btnEditUser = itemView.findViewById(R.id.btnEditUser);
        }

        public void bind(User user) {
            tvUserName.setText(user.getName());
            tvUserEmail.setText(user.getEmail());
            tvUserRole.setText(user.getRole());
            switchSuspend.setOnCheckedChangeListener(null);
            boolean isActive = "active".equalsIgnoreCase(user.getStatus());
            switchSuspend.setChecked(isActive);
            switchSuspend.setThumbTintList(ContextCompat.getColorStateList(context, R.color.switch_thumb_tint));
            switchSuspend.setTrackTintList(ContextCompat.getColorStateList(context, R.color.switch_track_tint));
            switchSuspend.setOnCheckedChangeListener((buttonView, isChecked) -> {
                String newStatus = isChecked ? "active" : "suspended";
                db.collection("users")
                        .document(user.getId())
                        .update("status", newStatus)
                        .addOnSuccessListener(aVoid -> {
                            user.setStatus(newStatus);
                            Toast.makeText(context,
                                    "User " + user.getName() + " is now " + newStatus,
                                    Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(context,
                                    "Failed to update status: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            switchSuspend.setChecked(!isChecked);
                        });
            });
            btnEditUser.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditUserActivity.class);
                intent.putExtra("USER_ID", user.getId());
                context.startActivity(intent);
            });
        }
    }
}
