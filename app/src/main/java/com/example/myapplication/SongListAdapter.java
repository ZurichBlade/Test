package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {

    //    ArrayList mSongList;
    ArrayList<HashMap<String, String>> mSongList;
    MediaPlayer music;

    public SongListAdapter(ArrayList<HashMap<String, String>> songList) {

        this.mSongList = songList;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvName.setText(mSongList.get(position).get("file_name"));

        Uri uri = Uri.fromFile(new File(mSongList.get(position).get("file_path")));

        holder.tvName.setOnClickListener(view -> {
            Intent intent = new Intent(holder.btnStop.getContext(), PlayerActivity.class);
            intent.putExtra("SONG_LIST",mSongList);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            holder.btnStop.getContext().startActivity(intent);

        });


    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageButton btnPlay, btnStop;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.txtName);
            btnPlay = itemView.findViewById(R.id.btnPlay);
            btnStop = itemView.findViewById(R.id.btnStop);

        }
    }
}
