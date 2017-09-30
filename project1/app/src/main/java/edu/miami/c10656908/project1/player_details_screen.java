package edu.miami.c10656908.project1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;

public class player_details_screen extends AppCompatActivity {

    private static final int ACTIVITY_SELECT_PICTURE_P1 = 1;
    private static final int ACTIVITY_SELECT_PICTURE_P2 = 2;
    private static final int ACTIVITY_SCORING_SCRREN = 3;


    public String player1SelectedURIString;
    public String player2SelectedURIString;

    public ImageView player1ImageView;
    public ImageView player2ImageView;
    public EditText player1EditText;
    public EditText player2EditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_player_details_screen);

        player1SelectedURIString = null;
        player2SelectedURIString = null;
        player1EditText = (EditText)findViewById(R.id.player1_enter_name);
        player2EditText = (EditText)findViewById(R.id.player2_enter_name);
        player1ImageView = (ImageView)findViewById(R.id.player1_icon);
        player2ImageView = (ImageView)findViewById(R.id.player2_icon);


    }

    public void myClickHandler(View view) {
        Intent galleryIntent;
        String player1Name = "";
        String player2Name = "";




        switch (view.getId()) {

            //if player1's icon is clicked, go to the gallery to get him a new icon
            case R.id.player1_icon:

                galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, ACTIVITY_SELECT_PICTURE_P1);
                break;
            //same as player1
            case R.id.player2_icon:
                galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, ACTIVITY_SELECT_PICTURE_P2);
                break;
            //Upon clicking submit button, store data to be sent to new activity. Create new intent to start the scoring screen
            case R.id.submit_button:
                Intent nextActivity;
                nextActivity = new Intent();
                nextActivity.setClassName("edu.miami.c10656908.project1", "edu.miami.c10656908.project1.scoring_screen");
                nextActivity.putExtra("edu.miami.c10656908.player_details_screen.player1selectedURIString", player1SelectedURIString);
                nextActivity.putExtra("edu.miami.c10656908.player_details_screen.player2selectedURIString", player2SelectedURIString);

                //get player's selected names from the edit text
                player1Name = player1EditText.getText().toString();
                player2Name = player2EditText.getText().toString();

                nextActivity.putExtra("edu.miami.c10656908.player_details_screen.player1Name", player1Name);
                nextActivity.putExtra("edu.miami.c10656908.player_details_screen.player2Name", player2Name);


                startActivityForResult(nextActivity, ACTIVITY_SCORING_SCRREN);
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        Uri selectedURI;
        Bitmap selectedPicture;


        super.onActivityResult(requestCode,resultCode,data);

        switch (requestCode) {
            //take URI from gallery. Assign it to player1's imageview
            case ACTIVITY_SELECT_PICTURE_P1:
                if(resultCode == Activity.RESULT_OK) {
                    selectedURI = data.getData();

                    player1SelectedURIString = selectedURI.toString();
                    try {
                        selectedPicture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedURI);
                        if(selectedPicture == null) {
                            Log.i("IN ONACTIVITY RESULT", "selected picture is null");
                        }
                        player1ImageView.setImageBitmap(selectedPicture);

                    }
                    catch(Exception e) {
                        Log.e("IN ONACTIVITY RESULT", "Error with selected picture");
                    }
                }
                break;
            //same as player 1
            case ACTIVITY_SELECT_PICTURE_P2:
                if(resultCode == Activity.RESULT_OK) {
                    selectedURI = data.getData();
                    player2SelectedURIString = selectedURI.toString();

                    try {
                        selectedPicture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedURI);
                        if(selectedPicture == null) {
                            Log.i("IN ONACTIVITY RESULT", "selected picture is null");
                        }
                        player2ImageView.setImageBitmap(selectedPicture);
                    }
                    catch(Exception e) {
                        Log.e("IN ONACTIVITY RESULT", "Error with selected picture");
                    }
                }
                break;
            //if you press the back button on the scoring_screen activity, reset to default EditText text, and default ImageView Image on the player_details activty
            case ACTIVITY_SCORING_SCRREN:


                player1EditText.setText("");
                player2EditText.setText("");
                player1SelectedURIString = null;
                player2SelectedURIString = null;
                //int P1_Picture_id = getResources().getIdentifier("edu.miami.c10656908.project1:drawable/smilyface.jpg", null, null);
                //player1ImageView.setImageResource(P1_Picture_id);
                //int P2_Picture_id = getResources().getIdentifier("edu.miami.c10656908.project1:drawable/sadface.jpg", null, null);
                //player2ImageView.setImageResource(P2_Picture_id);

                Drawable P1Drawable = getResources().getDrawable(R.drawable.smilyface);
                player1ImageView.setImageDrawable(P1Drawable);
                Drawable P2Drawable = getResources().getDrawable(R.drawable.sadface);
                player2ImageView.setImageDrawable(P2Drawable);

        }
    }
}
