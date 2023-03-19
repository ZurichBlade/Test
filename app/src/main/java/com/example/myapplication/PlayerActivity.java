package com.example.myapplication;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerActivity extends AppCompatActivity {

    Button pause, stop, start;
    ImageView imageView;
    ArrayList<HashMap<String, String>> songList;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        pause = findViewById(R.id.pause);
        stop = findViewById(R.id.stop);
        start = findViewById(R.id.start);

        songList = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("SONG_LIST");


        loadMusic();


        start.setOnClickListener(this::startPlayer);

        stop.setOnClickListener(this::stopPlayer);

        pause.setOnClickListener(this::pausePlayer);

    }


    private void loadMusic() {
//        Intent intent = getIntent();
//        String music_data = intent.getStringExtra("music_data");
//


//        Uri uri = Uri.fromFile(new File(music_data));
//        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
//                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                .setUsage(AudioAttributes.USAGE_MEDIA)
//                .build()
//        );

        mediaPlayer = singleTonExample.getSingletonMedia();


        Uri uri = Uri.fromFile(new File(songList.get(0).get("file_path")));

        try {
            mediaPlayer.setDataSource(getApplicationContext(), uri);
            mediaPlayer.prepare();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    // Playing the music
    public void startPlayer(View v) {
        if (mediaPlayer != null /*&& !music.isPlaying()*/) {
            mediaPlayer.start();
        }
    }

    // Pausing the music
    public void pausePlayer(View v) {
        if (mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    // Stopping the music
    public void stopPlayer(View v) {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        onBackPressed();
    }

    public void resetPlayer(MediaPlayer mediaPlayer) {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }


}