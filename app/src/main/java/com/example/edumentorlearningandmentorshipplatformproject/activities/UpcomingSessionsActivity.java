package com.example.edumentorlearningandmentorshipplatformproject.activities;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.edumentorlearningandmentorshipplatformproject.R;
import com.example.edumentorlearningandmentorshipplatformproject.adapters.UpcomingSessionsAdapter;
import com.example.edumentorlearningandmentorshipplatformproject.models.Payment;
import com.example.edumentorlearningandmentorshipplatformproject.models.UpcomingSession;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UpcomingSessionsActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView tvUpcomingSessionsTitle;
    private CalendarView calendarView;
    private RecyclerView rvUpcomingSessions;
    private FirebaseFirestore db;
    private UpcomingSessionsAdapter adapter;
    private List<UpcomingSession> sessionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_sessions);
        ivBack = findViewById(R.id.ivBack);
        tvUpcomingSessionsTitle = findViewById(R.id.tvUpcomingSessionsTitle);
        calendarView = findViewById(R.id.calendarView);
        rvUpcomingSessions = findViewById(R.id.rvUpcomingSessions);
        sessionList = new ArrayList<>();
        adapter = new UpcomingSessionsAdapter(sessionList);
        rvUpcomingSessions.setLayoutManager(new LinearLayoutManager(this));
        rvUpcomingSessions.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        String courseName = getIntent().getStringExtra("COURSE_NAME");
        if (courseName == null || courseName.isEmpty()) {
            courseName = "Unknown Course";
        }
        tvUpcomingSessionsTitle.setText("Upcoming Sessions - " + courseName);
        db.collection("payments")
                .whereEqualTo("courseName", courseName)
                .whereNotEqualTo("sessionDate", "")
                .addSnapshotListener((QuerySnapshot value, FirebaseFirestoreException error) -> {
                    if (error != null) {
                        Toast.makeText(UpcomingSessionsActivity.this,
                                "Error: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (value == null) return;
                    List<Payment> paymentList = new ArrayList<>();
                    Set<String> userIds = new HashSet<>();
                    value.getDocuments().forEach(doc -> {
                        Payment payment = doc.toObject(Payment.class);
                        if (payment != null) {
                            paymentList.add(payment);
                            userIds.add(payment.getUserId());
                        }
                    });
                    if (!userIds.isEmpty()) {
                        db.collection("users")
                                .whereIn(FieldPath.documentId(), new ArrayList<>(userIds))
                                .get()
                                .addOnSuccessListener(usersSnapshot -> {
                                    Map<String, String> userNamesMap = new HashMap<>();
                                    usersSnapshot.getDocuments().forEach(userDoc -> {
                                        String userId = userDoc.getId();
                                        String name = userDoc.getString("name");
                                        if (userId != null && name != null) {
                                            userNamesMap.put(userId, name);
                                        }
                                    });
                                    sessionList.clear();
                                    for (Payment p : paymentList) {
                                        String userName = userNamesMap.get(p.getUserId());
                                        if (userName == null) {
                                            userName = p.getUserId();
                                        }
                                        UpcomingSession session = new UpcomingSession(userName, p.getSessionDate(), p.getSessionTime(), "");
                                        sessionList.add(session);
                                    }
                                    adapter.notifyDataSetChanged();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(UpcomingSessionsActivity.this, "Error fetching user names: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                });
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
        });
        ivBack.setOnClickListener(v -> finish());
    }
}
