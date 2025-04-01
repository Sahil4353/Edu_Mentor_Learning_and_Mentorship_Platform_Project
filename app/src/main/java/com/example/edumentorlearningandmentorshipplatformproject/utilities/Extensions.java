package com.example.edumentorlearningandmentorshipplatformproject.utilities;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Extensions {
    public static void showToast(AppCompatActivity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }
}
