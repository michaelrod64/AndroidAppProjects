package edu.miami.c10656908.ratingapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

public class LoadingBarActivity extends AppCompatActivity {

    private ProgressBar myProgressBar;
    private int barClickTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_bar);
        myProgressBar = (ProgressBar)findViewById(R.id.time_left);
        myProgressBar.setProgress(myProgressBar.getMax());

        barClickTime = getResources().getInteger(R.integer.bar_click_time);
        myProgresser.run();
    }


    private final Runnable myProgresser = new Runnable() {



    private Handler myHandler = new Handler();

    public void run() {

        myProgressBar.setProgress(myProgressBar.getProgress()-barClickTime);
        if (myProgressBar.getProgress() <= 0) {
            setResult(RESULT_OK);
            finish();
        }
        if (!myHandler.postDelayed(myProgresser,barClickTime)) {
            Log.e("ERROR","Cannot postDelayed");
        }
    }
};


}
