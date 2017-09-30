package edu.miami.c10656908.project1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class play_screen extends AppCompatActivity {

    String P1TransferedURIString;
    String P2TransferedURIString;

    String P1TransferedName;
    String P2TransferedName;

    Uri P1TransferedURI;
    Uri P2TransferedURI;

    Bitmap P1SelectedPicture;
    Bitmap P2SelectedPicture;

    //1000 (out of 20000) done every increment. Makes 20 increments for smooth motion
    int barClickTime = 1000;
    //amount of time between increments required for 1 second rounds. Scaled appropriately by roundTime variable
    int timeBetweenIncrements = 50;

    //amount of seconds in a round
    int roundTime;
    //True if player1 plays first. False if player 2 plays first
    boolean player1First;

    TextView player1TextView;
    TextView player2TextView;

    ImageView player1ImageView;
    ImageView player2ImageView;

    ProgressBar myProgressBar;

    //who is currently playing
    int whoPlays;
    //amount of moves done by players. Used to calculate when a tie has occurred
    int playerMoves;

    //array tracking moves made
    int[][] ticTacToeArray = new int[3][3];

    //Player who played most recently. Variable used to ensure only one instance of myRunnable is affecting the ProgressBar at one time
    int whoPlayedLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);

        myProgressBar = (ProgressBar)findViewById(R.id.progressBar);

        //recovering information sent over by the scoring_screen activity
        P1TransferedName = this.getIntent().getStringExtra("edu.miami.c10656908.scoring_screen.P1TransferedName");
        P2TransferedName = this.getIntent().getStringExtra("edu.miami.c10656908.scoring_screen.P2TransferedName");

        P1TransferedURIString = this.getIntent().getStringExtra("edu.miami.c10656908.scoring_screen.P1TransferedURIString");
        P2TransferedURIString = this.getIntent().getStringExtra("edu.miami.c10656908.scoring_screen.P2TransferedURIString");

        roundTime = this.getIntent().getIntExtra("edu.miami.c10656908.scoring_screen.roundTime", 5);
        player1First = this.getIntent().getBooleanExtra("edu.miami.c10656908.scoring_screen.player1First", true);

        player1TextView = (TextView) findViewById(R.id.player1_textView);
        player2TextView = (TextView) findViewById(R.id.player2_textView);

        //setting player names
        player1TextView.setText(P1TransferedName);
        player2TextView.setText(P2TransferedName);

        player1ImageView = (ImageView)findViewById(R.id.player1_imageView);
        player2ImageView = (ImageView)findViewById(R.id.player2_imageView);



        //setting player images
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
                if (P2SelectedPicture == null) {
                    Log.i("IN LIKEDISLIKE", "Selected Picture is null");
                }
                player2ImageView.setImageBitmap(P2SelectedPicture);
            } catch (Exception e) {
                Log.e("test", "test" + e);
            }
        }
        //setting time between increments. Example: If round time is 10, the time between increments assigned will cause a round to last 10 seconds
        timeBetweenIncrements = timeBetweenIncrements * roundTime;
        playerMoves = 0;
        //assigning the first move to a player
        if(player1First) {
            whoPlays = 1;
        }
        else {
            whoPlays = 2;
        }
        whoPlayedLast = whoPlays;
        startPlayer();
    }

    public void startPlayer() {


        myProgressBar.setProgress(myProgressBar.getMax());
        //player currently player has their name and icon visible. Other player has theirs invisible
        if(whoPlays == 1) {
            player1TextView.setVisibility(View.VISIBLE);
            player1ImageView.setVisibility(View.VISIBLE);
            player2TextView.setVisibility(View.INVISIBLE);
            player2ImageView.setVisibility(View.INVISIBLE);
        }
        else {
            player1TextView.setVisibility(View.INVISIBLE);
            player1ImageView.setVisibility(View.INVISIBLE);
            player2TextView.setVisibility(View.VISIBLE);
            player2ImageView.setVisibility(View.VISIBLE);
        }

        myProgresser.run();


    }

    public void myPlayClickHandler(View view) {
        //player clicked a square, so move counter is incremented
        playerMoves++;

        ImageView clickedButton = (ImageView) view;
        BitmapDrawable bDrawable;
        //asigns chosen photo to button. If photo was not chosen, assigns default photo
        if(whoPlays == 1) {
            if(P1TransferedURIString != null) {
                bDrawable = new BitmapDrawable(P1SelectedPicture);
                clickedButton.setBackground(bDrawable);
            }
            else {
                Drawable img = this.getResources().getDrawable(R.drawable.smilyface);


                clickedButton.setBackground(img);
            }
        }
        else {
            if(P2TransferedURIString != null) {
                bDrawable = new BitmapDrawable(P2SelectedPicture);
                clickedButton.setBackground(bDrawable);
            }
            else {
                Drawable img = this.getResources().getDrawable(R.drawable.sadface);

                clickedButton.setBackground(img);

            }
        }
        //makes sure button cannot be clicked twice
        clickedButton.setClickable(false);


        Intent returnIntent;
        returnIntent = new Intent();

        //if someone won, store who won in an intent, set result with the intent, and finish activity
        if(updateScore(clickedButton, whoPlays)) {
            returnIntent.putExtra("edu.miami.c10656908.project1.play_screen.whoWon", whoPlays);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
        //if a tie occurs, return 0 to signifify a tie, set result, and finish.
        else if (playerMoves == 9){
            returnIntent.putExtra("edu.miami.c10656908.project1.play_screen.whoWon", 0);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
        //if no one won or tied, switch players and start a new round
        else {
            if(whoPlays == 1) {
                whoPlays = 2;
            }
            else if(whoPlays == 2) {
                whoPlays = 1;
            }
            startPlayer();
        }
    }

    //updates array of moves currently made. Returns if the game has been won.
    public boolean updateScore(ImageView clickedButton, int whoPlays) {
        int x = 0;
        int y = 0;

        switch (clickedButton.getId()) {
            case R.id.Button_0_0:
                ticTacToeArray[0][0] = whoPlays;
                x = 0;
                y = 0;
                break;
            case R.id.Button_1_0:
                ticTacToeArray[1][0] = whoPlays;
                x = 1;
                y = 0;
                break;
            case R.id.Button_2_0:
                ticTacToeArray[2][0] = whoPlays;
                x = 2;
                y = 0;
                break;
            case R.id.Button_0_1:
                ticTacToeArray[0][1] = whoPlays;
                x = 0;
                y = 1;
                break;
            case R.id.Button_1_1:
                ticTacToeArray[1][1] = whoPlays;
                x = 1;
                y = 1;
                break;
            case R.id.Button_2_1:
                ticTacToeArray[2][1] = whoPlays;
                x = 2;
                y = 1;
                break;
            case R.id.Button_0_2:
                ticTacToeArray[0][2] = whoPlays;
                x = 0;
                y = 2;
                break;
            case R.id.Button_1_2:
                ticTacToeArray[1][2] = whoPlays;
                x = 1;
                y = 2;
                break;
            case R.id.Button_2_2:
                ticTacToeArray[2][2] = whoPlays;
                x = 2;
                y = 2;
                break;
            default:
                break;
        }
        //calls method to see if game has been won. Sends the x and y coordinate of move, as well as who made the move
        return gameOver(x, y, whoPlays);
    }
    public boolean gameOver(int x, int y, int whoPlays) {
        int column, row, diagnal, secondDiagnal;
        column = row = diagnal = secondDiagnal = 0;


        for(int i = 0; i <= 2; i++) {
            //checks if an entire column has been filled by the player
            if(ticTacToeArray[x][i] == whoPlays) {
                column++;
            }
            //checks if an entire row has been filled by the player
            if(ticTacToeArray[i][y] == whoPlays) {
                row++;
            }
            //checks if the left to right diagnal has been filled by the player
            if(ticTacToeArray[i][i] == whoPlays) {
                diagnal++;
            }
            //checks if the right to left diagnal has been filled by the player
            if(ticTacToeArray[i][2 - i] == whoPlays) {
                secondDiagnal++;
            }
        }
        //if any column, row, or diagnal has 3 in a row by the player, return true to signify his victory
        return (column == 3 || row == 3 || diagnal == 3 || secondDiagnal == 3);
    }

    private final Runnable myProgresser = new Runnable() {




        private Handler myHandler = new Handler();

        public void run() {
            //if a round has been ended early by someone making a move, remove callbacks and don't affect progressbar
            if (whoPlayedLast != whoPlays) {
                myHandler.removeCallbacksAndMessages(null);
                //reset progress bar for new round
                myProgressBar.setProgress(myProgressBar.getMax());
                //update whoplayedlast to the new person playing
                whoPlayedLast = whoPlays;
                //startPlayer();
                //myProgresser.run();
            }

            else {
                //update progress bar
                myProgressBar.setProgress(myProgressBar.getProgress() - barClickTime);
            }
            //if round runs out of time, switch players and start new round
            if(myProgressBar.getProgress() <= 0) {
                if(whoPlays == 1) {
                    whoPlays = 2;
                }
                else if(whoPlays == 2) {
                    whoPlays = 1;
                }
                myHandler.removeCallbacksAndMessages(null);
                startPlayer();
            }
            //if (myProgressBar.getProgress() <= 0) {

               // if (false) {
                    //finish();
               // } else {
                    //myProgressBar.setProgress(myProgressBar.getMax());
               // }
           // }

            //repeat thread up to 20 times to increment progress bar for roundtime seconds
            else if (!myHandler.postDelayed(myProgresser,timeBetweenIncrements)) {
                Log.e("ERROR","Cannot postDelayed");
            }
        }
    };

}
