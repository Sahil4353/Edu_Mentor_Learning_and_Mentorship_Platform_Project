package com.example.edumentorlearningandmentorshipplatformproject.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edumentorlearningandmentorshipplatformproject.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoPlayerActivity extends AppCompatActivity {

    private YouTubePlayerView youtubePlayerView;
    private Button btnStart, btnPause;
    private YouTubePlayer youTubePlayer;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        youtubePlayerView = findViewById(R.id.youtubePlayerView);
        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(view -> {
            Toast.makeText(this, "Back Button Clicked", Toast.LENGTH_SHORT).show();
            finish();
        });

        getLifecycle().addObserver(youtubePlayerView);
        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer player) {
                youTubePlayer = player;
                String videoId = getIntent().getStringExtra("VIDEO_ID");
                if (videoId != null && !videoId.isEmpty()) {
                    youTubePlayer.loadVideo(videoId, 0);
                }
            }
        });

        btnStart.setOnClickListener(v -> {
            if (youTubePlayer != null) youTubePlayer.play();
        });

        btnPause.setOnClickListener(v -> {
            if (youTubePlayer != null) youTubePlayer.pause();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        youtubePlayerView.release();
    }
}
