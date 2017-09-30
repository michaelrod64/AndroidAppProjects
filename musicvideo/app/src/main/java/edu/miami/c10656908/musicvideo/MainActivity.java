package edu.miami.c10656908.musicvideo;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import java.io.IOException;

public class MainActivity extends Activity implements
        MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {
    //-----------------------------------------------------------------------------
    private MediaPlayer myPlayer;
    private VideoView videoScreen;
    //-----------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myPlayer = MediaPlayer.create(this,R.raw.pancakes);
        myPlayer.setLooping(true);
        myPlayer.start();


    }
    //-----------------------------------------------------------------------------
    public void myVideoClickListener(View view) {

        Uri videoURI;
        Button playButton = (Button)findViewById(R.id.play);
        Button pauseButton = (Button)findViewById(R.id.pause);
        Button resumeButton = (Button)findViewById(R.id.resume);
        Button stopButton = (Button)findViewById(R.id.pause);

        switch (view.getId()) {
            case R.id.play:
                playButton.setClickable(false);
                resumeButton.setClickable(false);
                pauseButton.setClickable(true);
                stopButton.setClickable(true);
                myPlayer.pause();

                videoScreen = (VideoView)findViewById(R.id.video);
                videoURI = Uri.parse(getString(R.string.mp4_url));

                String path = "android.resource://" + getPackageName() + "/" + R.raw.warming;
                videoScreen.setVideoURI(Uri.parse(path));
                videoScreen.setOnCompletionListener(this);
                videoScreen.setVisibility(View.VISIBLE);
                videoScreen.start();
                break;


            case R.id.pause:
                playButton.setClickable(false);
                resumeButton.setClickable(true);
                pauseButton.setClickable(false);
                stopButton.setClickable(true);

                myPlayer.start();
                videoScreen.pause();
                break;
            case R.id.resume:
                playButton.setClickable(false);
                resumeButton.setClickable(false);
                pauseButton.setClickable(true);
                stopButton.setClickable(true);

                myPlayer.pause();
                videoScreen.start();
                break;
            case R.id.stop:
                myPlayer.stop();
                myPlayer.release();
                videoScreen.stopPlayback();
                finish();
                break;
            default:
                break;
        }
    }
    //-----------------------------------------------------------------------------
    public void onPrepared(MediaPlayer mediaPlayer) {

        mediaPlayer.start();
    }
    //-----------------------------------------------------------------------------
    public void onCompletion(MediaPlayer mediaPlayer) {

        mediaPlayer.release();
        finish();
    }
    //-----------------------------------------------------------------------------
    public boolean onError(MediaPlayer mediaPlayer,int whatHappened,int extra) {

        mediaPlayer.stop();
        mediaPlayer.release();
        return(true);
    }
//-----------------------------------------------------------------------------
}
//=============================================================================
