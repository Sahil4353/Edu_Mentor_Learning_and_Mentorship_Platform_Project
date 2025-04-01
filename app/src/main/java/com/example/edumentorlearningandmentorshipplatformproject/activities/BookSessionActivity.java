package com.example.edumentorlearningandmentorshipplatformproject.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edumentorlearningandmentorshipplatformproject.R;

public class BookSessionActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PAYMENT = 1002;

    private CalendarView calendarView;
    private TextView tvSelectTime;
    private EditText etNotes;
    private Button btnScheduleSession;

    private String selectedDate = "";
    private String selectedTime = "";
    private String courseName, coursePrice;
    private String mentorName = "";
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_session);

        // Retrieve the logged in user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPref", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", "");

        calendarView = findViewById(R.id.calendarView);
        tvSelectTime = findViewById(R.id.tvSelectTime);
        etNotes = findViewById(R.id.etNotes);
        btnScheduleSession = findViewById(R.id.btnScheduleSession);
        courseName = getIntent().getStringExtra("COURSE_NAME");
        coursePrice = getIntent().getStringExtra("COURSE_PRICE");

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = (month + 1) + "/" + dayOfMonth + "/" + year;
        });

        tvSelectTime.setOnClickListener(v -> {
            TimePickerDialog timePicker = new TimePickerDialog(
                    this,
                    (picker, hourOfDay, minute) -> {
                        selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        tvSelectTime.setText(selectedTime);
                    },
                    12, 0, false
            );
            timePicker.show();
        });

        btnScheduleSession.setOnClickListener(v -> {
            if (selectedDate.isEmpty()) {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedTime.isEmpty()) {
                Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show();
                return;
            }
            mentorName = etNotes.getText().toString().trim();
            if (mentorName.isEmpty()) {
                Toast.makeText(this, "Please enter the mentor's name in the notes", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Session scheduled on " + selectedDate + " at " + selectedTime, Toast.LENGTH_LONG).show();

            // Pass the user ID along with other details to PaymentActivity
            Intent payIntent = new Intent(BookSessionActivity.this, PaymentActivity.class);
            payIntent.putExtra("COURSE_NAME", courseName);
            payIntent.putExtra("COURSE_PRICE", coursePrice);
            payIntent.putExtra("MENTOR_NAME", mentorName);
            payIntent.putExtra("SESSION_DATE", selectedDate);
            payIntent.putExtra("SESSION_TIME", selectedTime);
            payIntent.putExtra("USER_ID", userId);
            startActivityForResult(payIntent, REQUEST_CODE_PAYMENT);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("ENROLLED_COURSE_TITLE", courseName);
                resultIntent.putExtra("ENROLLED_INSTRUCTOR", mentorName);
                resultIntent.putExtra("USER_ID", userId);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Payment was not successful.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}