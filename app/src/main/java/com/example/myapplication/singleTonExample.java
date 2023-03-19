package com.example.myapplication;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class singleTonExample {

    static MediaPlayer instance;
    private static singleTonExample ourInstance = new singleTonExample();
    private Context context;

    private singleTonExample() {

    }

    private static Context get() {
        return getInstance().getContext();
    }

    public static synchronized singleTonExample getInstance() {
        return ourInstance;
    }

    public void init(Context context) {
        if (context == null) {
            this.context = context;
        }
    }

    private Context getContext() {
        return context;
    }


    public static MediaPlayer getSingletonMedia() {
        if (instance == null)
            // it's ok, we can call this constructor
            instance = MediaPlayer.create(get(), Uri.parse(""));
        return instance;
    }


    /*public static MediaPlayer getInstance() {
        if (instance != null) {
            instance = new MediaPlayer();
        }
        return instance;
    }

    public static int currentIndex = -1;*/


}
