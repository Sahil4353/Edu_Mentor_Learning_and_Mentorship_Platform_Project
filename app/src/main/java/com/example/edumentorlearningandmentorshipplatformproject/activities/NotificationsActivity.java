package com.example.edumentorlearningandmentorshipplatformproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edumentorlearningandmentorshipplatformproject.R;
import com.example.edumentorlearningandmentorshipplatformproject.adapters.NotificationsAdapter;
import com.example.edumentorlearningandmentorshipplatformproject.models.NotificationItem;
import com.example.edumentorlearningandmentorshipplatformproject.models.Payment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NotificationsActivity extends AppCompatActivity {

    private ImageView ivBackArrow;
    private TextView tvNotificationsTitle;
    private RecyclerView rvNotifications;
    private NotificationsAdapter notificationsAdapter;
    private List<NotificationItem> notificationsList;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        ivBackArrow = findViewById(R.id.ivBackArrow);
        tvNotificationsTitle = findViewById(R.id.tvNotificationsTitle);
        rvNotifications = findViewById(R.id.rvNotifications);

        tvNotificationsTitle.setText("Notifications");

        ivBackArrow.setOnClickListener(v -> {
            Intent intent = new Intent(NotificationsActivity.this, MentorDashboardActivity.class);
            startActivity(intent);
            finish();
        });

        notificationsList = new ArrayList<>();
        notificationsAdapter = new NotificationsAdapter(notificationsList);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        rvNotifications.setAdapter(notificationsAdapter);

        auth = FirebaseAuth.getInstance();
        db   = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "No mentor logged in.", Toast.LENGTH_SHORT).show();
            return;
        }
        String mentorName = currentUser.getDisplayName();
        if (mentorName == null || mentorName.isEmpty()) {
            mentorName = "Mentor";
        }

        db.collection("payments")
                .whereEqualTo("mentorName", mentorName)
                .get()
                .addOnSuccessListener((QuerySnapshot paymentsSnapshot) -> {
                    List<Payment> paymentList = new ArrayList<>();
                    Set<String> userIds = new HashSet<>();
                    for (DocumentSnapshot doc : paymentsSnapshot) {
                        Payment payment = doc.toObject(Payment.class);
                        if (payment != null) {
                            paymentList.add(payment);
                            userIds.add(payment.getUserId());
                        }
                    }

                    if (paymentList.isEmpty()) {
                        Toast.makeText(NotificationsActivity.this, "No new enrollments found.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    db.collection("users")
                            .whereIn(FieldPath.documentId(), new ArrayList<>(userIds))
                            .get()
                            .addOnSuccessListener(usersSnapshot -> {
                                final HashMap<String, UserData> userMap = new HashMap<>();
                                for (DocumentSnapshot userDoc : usersSnapshot.getDocuments()) {
                                    String uid = userDoc.getId();
                                    String name = userDoc.getString("name");
                                    String photoUrl = userDoc.getString("photoUrl");
                                    if (name == null) name = uid;
                                    if (photoUrl == null) photoUrl = "";
                                    userMap.put(uid, new UserData(name, photoUrl));
                                }

                                notificationsList.clear();
                                for (Payment p : paymentList) {
                                    UserData ud = userMap.get(p.getUserId());
                                    String studentName = (ud != null) ? ud.name : p.getUserId();
                                    String photoUrl = (ud != null) ? ud.photoUrl : "";
                                    String message = "You have a new student!";

                                    NotificationItem item = new NotificationItem(
                                            studentName,
                                            p.getTimestamp(),
                                            photoUrl,
                                            message
                                    );
                                    notificationsList.add(item);
                                }
                                notificationsAdapter.notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(NotificationsActivity.this, "Error fetching user info: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to fetch payments: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private static class UserData {
        String name;
        String photoUrl;
        UserData(String name, String photoUrl) {
            this.name = name;
            this.photoUrl = photoUrl;
        }
    }
}
