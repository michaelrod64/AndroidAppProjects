package edu.miami.c10656908.ratingapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;

public class MainActivity extends AppCompatActivity {

    private static final int ACTIVITY_LOADING_BAR_ACTIVITY = 1;
    private RatingBar theRatingBar;
    private int barClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        theRatingBar = (RatingBar)findViewById(R.id.score);

    }

    public void myClickHandler(View view) {
        Intent nextActivity;

        switch (view.getId()) {
            case R.id.start_button:
                nextActivity = new Intent();
                nextActivity.setClassName("edu.miami.c10656908.ratingapp",
                        "edu.miami.c10656908.ratingapp.LoadingBarActivity");
                startActivityForResult(nextActivity, ACTIVITY_LOADING_BAR_ACTIVITY);
                break;
            default:
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode,data);

        switch (requestCode) {
            case ACTIVITY_LOADING_BAR_ACTIVITY:
                if(resultCode == Activity.RESULT_OK) {
                    theRatingBar.setRating(theRatingBar.getRating()+1);
                }
                Log.i("IN ONACTIVITYRESULT", "Rating bar:" + Float.toString(theRatingBar.getRating()));
                Log.i("IN ONACTIVITYREUSLT", "IF STATEMENT: " + Boolean.toString(theRatingBar.getRating() >= R.integer.max_score));
                Log.i("IN ONACTIVITYRESULT", "CONSTANT R.INTEGER.MAX_SCORE: " + Integer.toString(R.integer.max_score));
                Log.i("IN ONACTIVITYREUSLT", "IF STATEMENT: " + Boolean.toString(theRatingBar.getRating() >= 5));

                if(theRatingBar.getRating() >= getResources().getInteger(R.integer.max_score)) {
                    Log.i("IN ONACTIVITYRESULT", "IF-STATEMENT ACTIVATED");
                    finish();

                }
                break;
            default:
                break;
        }

    }
}




