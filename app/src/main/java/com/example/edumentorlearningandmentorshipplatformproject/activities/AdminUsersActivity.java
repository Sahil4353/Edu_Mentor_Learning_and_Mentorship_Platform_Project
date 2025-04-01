package com.example.edumentorlearningandmentorshipplatformproject.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edumentorlearningandmentorshipplatformproject.R;
import com.example.edumentorlearningandmentorshipplatformproject.adapters.AdminUsersAdapter;
import com.example.edumentorlearningandmentorshipplatformproject.models.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminUsersActivity extends AppCompatActivity {

    private ImageView ivBackArrow;
    private EditText etSearchUsers;
    private RecyclerView rvAdminUsers;

    private AdminUsersAdapter adapter;
    private List<User> userList;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users);

        ivBackArrow   = findViewById(R.id.ivBackArrow);
        etSearchUsers = findViewById(R.id.etSearchUsers);
        rvAdminUsers  = findViewById(R.id.rvAdminUsers);
        ivBackArrow.setOnClickListener(v -> finish());

        userList = new ArrayList<>();
        adapter = new AdminUsersAdapter(this, userList);

        rvAdminUsers.setLayoutManager(new LinearLayoutManager(this));
        rvAdminUsers.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadAllUsersFromFirestore();

        etSearchUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllUsersFromFirestore();
    }

    private void loadAllUsersFromFirestore() {
        db.collection("users")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    userList.clear();
                    for (DocumentSnapshot doc : querySnapshot) {
                        if (doc.exists()) {
                            User user = doc.toObject(User.class);
                            if (user != null) {
                                user.setId(doc.getId());
                                userList.add(user);
                            }
                        }
                    }
                    adapter.setFullUserList(userList);

                    if (userList.isEmpty()) {
                        Toast.makeText(AdminUsersActivity.this,
                                "No users found",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminUsersActivity.this,
                            "Failed to load users: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}
