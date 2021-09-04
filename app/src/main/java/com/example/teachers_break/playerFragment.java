package com.example.teachers_break;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.io.IOException;

public class playerFragment extends Fragment {

    FloatingActionButton btn_play;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;
    MediaPlayer mediaPlayer;
    private boolean playing;
    TextView tv_songTitle;
    private SeekBar seekBar;
    private Handler handler=new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_player, container, false);

        btn_play=view.findViewById(R.id.btn_play);
        tv_songTitle=view.findViewById(R.id.tv_songTitle);
        seekBar=view.findViewById(R.id.seekBar);
        playing=false;

        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playing==false){
                    playAudio(1);
                    playing=true;
//                    seekBar.setMax(mediaPlayer.getDuration()/1000);
//                    seekBar.setProgress(mediaPlayer.getDuration()/1000);
//                    Toast.makeText(getContext(), mediaPlayer.getDuration()/1000+"", Toast.LENGTH_SHORT).show();
                }
                else{
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer=null;
                    playing=false;
//                    Toast.makeText(getContext(), "audio paused", Toast.LENGTH_SHORT).show();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress*1000 );
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer!=null){
                        int mCurrentPosition=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });


        return view;
    }

    private String audioUrl;

    private void playAudio(int i) {
        mediaPlayer =new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mUser=mAuth.getCurrentUser();
            String uid=mUser.getUid();
            db.collection("songs").document(Integer.toString(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot doc=task.getResult();
                        if (doc.exists()){
                            audioUrl =doc.get("songUrl").toString();
                            tv_songTitle.setText(doc.get("songTitle").toString());
                            try {
                                mediaPlayer.setDataSource(audioUrl);
                                mediaPlayer.prepare();
                                mediaPlayer.start();

                            }catch (IOException e){
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    }

//    public void updateProgressBar(){
//        handler.postDelayed(mUpdateTimeTask,100);
//    }
//
//    private Runnable mUpdateTimeTask=new Runnable() {
//        @Override
//        public void run() {
//
//        }
//    };
}