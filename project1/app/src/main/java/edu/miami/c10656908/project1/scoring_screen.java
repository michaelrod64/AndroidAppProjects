package edu.miami.c10656908.project1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

public class scoring_screen extends AppCompatActivity {

    private static final int ACTIVITY_PLAY_SCREEN = 1;

    String P1TransferedURIString;
    String P2TransferedURIString;

    TextView player1TextView;
    TextView player2TextView;

    RatingBar player1RatingBar;
    RatingBar player2RatingBar;

    int roundTime;
    double dividingLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {




        String P1TransferedName;
        String P2TransferedName;


        Uri P1TransferedURI;
        Uri P2TransferedURI;
        Bitmap P1SelectedPicture;
        Bitmap P2SelectedPicture;

        ImageView player1ImageView;
        ImageView player2ImageView;

        //set default round time to 5 seconds
        roundTime = 5;
        //variable used to make it very unlikely that the same player will start first every round
        dividingLine = .5;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoring_screen);

        player1RatingBar = (RatingBar)findViewById(R.id.player1_ratingbar);
        player2RatingBar = (RatingBar)findViewById(R.id.player2_ratingbar);

        //get names that player chose in player_details activity
        P1TransferedName = this.getIntent().getStringExtra("edu.miami.c10656908.player_details_screen.player1Name");
        P2TransferedName = this.getIntent().getStringExtra("edu.miami.c10656908.player_details_screen.player2Name");


        player1TextView = (TextView)findViewById(R.id.player1_textview);
        player2TextView = (TextView)findViewById(R.id.player2_textview);

        //if player did not choose a name, make their name "no name"
        if(P1TransferedName.trim().length() == 0) {
            player1TextView.setText(this.getResources().getString(R.string.no_name_selected));
        }
        else {
            player1TextView.setText(P1TransferedName.trim());
        }
        if(P2TransferedName.trim().length() == 0) {
            player2TextView.setText(this.getResources().getString(R.string.no_name_selected));
        }
        else {
            player2TextView.setText(P2TransferedName.trim());
        }

        player1ImageView = (ImageView)findViewById(R.id.player1_imageview);
        player2ImageView = (ImageView)findViewById(R.id.player2_imageview);

        //get URI string from photo chosen in player_details activity
        P1TransferedURIString = this.getIntent().getStringExtra("edu.miami.c10656908.player_details_screen.player1selectedURIString");
        P2TransferedURIString = this.getIntent().getStringExtra("edu.miami.c10656908.player_details_screen.player2selectedURIString");

        //if the player chose a photo, assign the photo to the scoring_screen's imageview. If no photo was chosen, the default smily or frowny face is the default
        //(hardcoded into xml)
        if(P1TransferedURIString != null) {
            P1TransferedURI = Uri.parse(P1TransferedURIString);

            try {
                P1SelectedPicture = MediaStore.Images.Media.getBitmap(
                        this.getContentResolver(), P1TransferedURI);
                if(P1SelectedPicture == null) {
                    Log.i("IN LIKEDISLIKE", "Selected Picture is null");
                }
                player1ImageView.setImageBitmap(P1SelectedPicture);
            }
            catch(Exception e) {
                Log.e("test","test"+e);
            }
        }
        if(P2TransferedURIString != null) {
            P2TransferedURI = Uri.parse(P2TransferedURIString);

            try {
                P2SelectedPicture = MediaStore.Images.Media.getBitmap(
                        this.getContentResolver(), P2TransferedURI);
                if(P2SelectedPicture == null) {
                    Log.i("IN LIKEDISLIKE", "Selected Picture is null");
                }
                player2ImageView.setImageBitmap(P2SelectedPicture);
            }
            catch(Exception e) {
                Log.e("test","test"+e);
            }
        }
    }

    //inflates option menu to choose roundtime or reset scores
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scoring_screen, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        //switch statement to confirm that hello_world_menu_item was pressed
        switch (item.getItemId()) {
            //resets score and makes it equally likely that either player will play first (by resetting the dividing line variable)
            case R.id.reset:
                dividingLine = .5;
                player1RatingBar.setRating(0f);
                player2RatingBar.setRating(0f);
                findViewById(R.id.play_button).setVisibility(View.VISIBLE);
                return true;

            //all other cases simply adjust the round time of the game
            case R.id.set_one_second:
                roundTime = 1;
                break;

            case R.id.set_two_seconds:
                roundTime = 2;
                break;

            case R.id.set_five_seconds:
                roundTime = 5;
                break;

            case R.id.set_ten_seconds:
                roundTime = 10;
                break;



            default:
                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);
    }

    public void myClickHandler(View view) {




        switch(view.getId()){
            //When play button is pressed. Program stores picture URIs, playernames, roundtime, and the player who will have the first turn. Data stored in intent to
            //be accessed in the play_screen activity
            case R.id.play_button:
                boolean player1First;
                //decides who is to play first
                player1First = chooseWhoPlays();

                Intent nextActivity;
                nextActivity = new Intent();
                nextActivity.setClassName("edu.miami.c10656908.project1", "edu.miami.c10656908.project1.play_screen");

                nextActivity.putExtra("edu.miami.c10656908.scoring_screen.P1TransferedURIString", P1TransferedURIString);
                nextActivity.putExtra("edu.miami.c10656908.scoring_screen.P2TransferedURIString", P2TransferedURIString);

                nextActivity.putExtra("edu.miami.c10656908.scoring_screen.P1TransferedName", player1TextView.getText().toString() );
                nextActivity.putExtra("edu.miami.c10656908.scoring_screen.P2TransferedName", player2TextView.getText().toString());

                nextActivity.putExtra("edu.miami.c10656908.scoring_screen.roundTime", (int)roundTime);

                nextActivity.putExtra("edu.miami.c10656908.scoring_screen.player1First", player1First);

                startActivityForResult(nextActivity, ACTIVITY_PLAY_SCREEN);


        }

    }
    //calculates who is to play first. Makes person chosen less likely to be chosen next time by adjusting deviding line variable
    public boolean chooseWhoPlays() {
        if(Math.random() >= dividingLine) {
            dividingLine += .1;
            return true;
        }
        else {
            dividingLine -= .1;
            return false;
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int gameResult;
        Log.i("In ONACTIVITYRESULT", "Entered onactivity result");
        switch (requestCode) {
            case ACTIVITY_PLAY_SCREEN:
                if(resultCode == Activity.RESULT_OK){
                    //retrieve integer from play_screen activity saying who won
                    gameResult = data.getIntExtra("edu.miami.c10656908.project1.play_screen.whoWon", 0);
                    Log.i("In ONACTIVITYRESULT", "GAMERESULT= " + gameResult);
                    //increment winner's rating bar. If it was a tie, no one's bar is incremented.
                    if(gameResult == 1) {
                        player1RatingBar.setRating((player1RatingBar.getRating() + 1));
                    }
                    else if(gameResult == 2) {
                        player2RatingBar.setRating(player2RatingBar.getRating() + 1);
                    }
            }
        }
        //if either player reaches 5 stars, the game is over and the play button disappears
        if((player1RatingBar.getRating() >= 5) || (player2RatingBar.getRating() >= 5)) {
            findViewById(R.id.play_button).setVisibility(View.INVISIBLE);
        }


    }

}
