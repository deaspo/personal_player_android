package com.example.polyc.sounddemo;

import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.MediaController;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //declaration of the variables
    MediaPlayer mPlayer;
    SeekBar skBar, skVol;
    Handler handler = new Handler();
    AudioManager audMgr;
    TextView txtProg, txtVol;
    String msg;
    int curVol;
    double mediaLength = 0, mediaPos = 0;
    int forwardTime = 2000, backwardTime = 2000;

    //thread for checking the seeking bar and the progress of the song



    public void clkPlay(View view) {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        }else{
            mPlayer.start();
            handler.postDelayed(mvSeekBar,100);//sets the 100 ms

        }
    }

    public void clkForward(View view) {
        //check if we can go forward
        if (mediaPos < mediaLength) {
            mediaPos = mediaPos + forwardTime;

            //adjust the seekbar
            mPlayer.seekTo((int) mediaPos);
        }
    }

    public void clkBackward(View view) {
        //check if we can go forward
        if (mediaPos - backwardTime > 0) {
            mediaPos = mediaPos - backwardTime;

            //adjust the seekbar
            mPlayer.seekTo((int) mediaPos);
        }
    }

    public void clkNext(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custometitle);


        mPlayer = MediaPlayer.create(this, R.raw.avicii);
        txtProg = (TextView) findViewById(R.id.btnProgress);
        skVol = (SeekBar) findViewById(R.id.skVol);
        skBar = (SeekBar) findViewById(R.id.skBar);
        txtVol = (TextView) findViewById(R.id.txtVol);
        audMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVol = audMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        curVol = audMgr.getStreamVolume(AudioManager.STREAM_MUSIC);


        //set max volume

        skVol.setMax(maxVol);
        skVol.setProgress(curVol);

        txtProg.setText("0.00");
        txtVol.setText("Volume = " + Integer.toString(curVol));

        //progress bar manenos
        mediaLength = mPlayer.getDuration();
        mediaPos = mPlayer.getCurrentPosition();

        skBar.setMax((int) mediaLength);
        skBar.setProgress((int) mediaPos);

        //check the progress after every 100 miliseconds

        handler.removeCallbacks(mvSeekBar);
        //handler.postDelayed(mvSeekBar,100);//sets the 100 ms



        //adjust the length
        skBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progV = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progV = progress;
                txtProg.setText(Integer.toString(progV) + "/" + seekBar.getMax());
                if (progress == mediaLength) { msg = "End of song";}
                            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {



            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //txtProg.setText(Integer.toString(progV) + "/" + seekBar.getMax());

            }
        });
        //set the volume manenos

        skVol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int volChg = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volChg = progress;
                audMgr.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtVol.setText("Volume = " + Integer.toString(volChg));


            }
        });


    }
    private Runnable mvSeekBar = new Runnable() {
        @Override
        public void run() {
            /*if (mPlayer.isPlaying()) {
                int mPos = mPlayer.getCurrentPosition();
                int mL = mPlayer.getDuration();
                skBar.setMax(mL);
                skBar.setProgress(mPos);

            }*/

            //gets current pos
            mediaPos = mPlayer.getCurrentPosition();
            //sets the progress bar
            skBar.setProgress((int) mediaPos);
            //sets the time remaining
            double timeRemaining = mediaLength - mediaPos;
            txtProg.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
            handler.postDelayed(this, 100);
        }
    };
}
