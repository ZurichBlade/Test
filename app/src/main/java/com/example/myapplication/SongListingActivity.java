package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class SongListingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SongListAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    TextView txtNoFileFound;
    ArrayList<HashMap<String, String>> songList = getPlayList(Environment.getExternalStorageDirectory().getAbsolutePath());


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_listing);

        recyclerView = findViewById(R.id.rvSongListing);
        txtNoFileFound = findViewById(R.id.txtNoFileFound);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SongListAdapter(songList);
        recyclerView.setAdapter(adapter);


        if (songList.size() == 0) {
            txtNoFileFound.setVisibility(View.VISIBLE);
        } else {
            txtNoFileFound.setVisibility(View.GONE);
        }

        if (songList != null) {
            for (int i = 0; i < songList.size(); i++) {
                String fileName = songList.get(i).get("file_name");
                String filePath = songList.get(i).get("file_path");

                Log.d("file details ", " name =" + fileName + " path = " + filePath);
            }
        }


    }


    // Adding Songs from local storage to Arraylist
    ArrayList<HashMap<String, String>> getPlayList(String rootPath) {
        ArrayList<HashMap<String, String>> fileList = new ArrayList<>();


        try {
            File rootFolder = new File(rootPath);
            File[] files = rootFolder.listFiles(); //here you will get NPE if directory doesn't contains  any file,handle it like this.
            for (File file : files) {
                if (file.isDirectory()) {
                    if (getPlayList(file.getAbsolutePath()) != null) {
                        fileList.addAll(getPlayList(file.getAbsolutePath()));
                    } else {
                        break;
                    }
                } else if (file.getName().endsWith(".mp3")) {
                    HashMap<String, String> song = new HashMap<>();
                    song.put("file_path", file.getAbsolutePath());
                    song.put("file_name", file.getName());
                    fileList.add(song);
                }
            }
            return fileList;
        } catch (Exception e) {
            return null;
        }
    }

}