package com.example.tfg;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoPlayer extends YouTubeBaseActivity {

    YouTubePlayerView youTubePlayerView;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        //Variables
        youTubePlayerView = findViewById(R.id.videoPlayer_youtubeVideo);
        btnBack = findViewById(R.id.videoPlayer_btn_back);

        //Se coge la url del video
        String workout = getIntent().getStringExtra("workoutUrl");

        //Youtube Video listener
        YouTubePlayer.OnInitializedListener listener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                //Carga el vídeo
                youTubePlayer.loadVideo(workout);
                //Reproduce el vídeo
                youTubePlayer.play();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(VideoPlayer.this, R.string.videoPlayer_cantPlayVideo, Toast.LENGTH_SHORT).show();
            }
        };
        //Inicializa el video de youtube pasandole la clave de la API
        youTubePlayerView.initialize("AIzaSyAecRvrqTbmguK_exRmMQBnMcDkppnMGXU", listener);

        //Listener que vuelve a la pantalla de tutoriales
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}