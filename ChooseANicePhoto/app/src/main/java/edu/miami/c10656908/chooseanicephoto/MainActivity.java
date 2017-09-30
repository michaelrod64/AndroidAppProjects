package edu.miami.c10656908.chooseanicephoto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int ACTIVITY_SELECT_PICTURE = 1;
    private static final int ACTIVITY_LIKE_DISLIKE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chooseAPicture();


        //setContentView(R.layout.activity_main);
    }
    @Override
    protected void onResume() {
        super.onResume();

        //chooseAPicture();
    }

    public void chooseAPicture() {
        Intent galleryIntent;

        galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,ACTIVITY_SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {

        ImageView pictureView;
        Uri selectedURI;
        String selectedURIString;
        //Bitmap selectedPicture;
        Intent nextActivity;
        super.onActivityResult(requestCode,resultCode,data);

        switch (requestCode) {
            case ACTIVITY_SELECT_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    //pictureView = (ImageView)findViewById(R.id.selected_picture);
                    selectedURI = data.getData();
                    selectedURIString = selectedURI.toString();
                    try {
                        nextActivity = new Intent();
                        nextActivity.setClassName("edu.miami.c10656908.chooseanicephoto",
                                "edu.miami.c10656908.chooseanicephoto.LikeDislike");
                        nextActivity.putExtra("edu.miami.c10656908.chooseanicephoto.selectedURI", selectedURIString);
                        startActivityForResult(nextActivity, ACTIVITY_LIKE_DISLIKE);
                        //selectedPicture = MediaStore.Images.Media.getBitmap(
                               // this.getContentResolver(),selectedURI);
                       // pictureView.setImageBitmap(selectedPicture);
                    } catch (Exception e) {
//----Should do something here
                    }
                }
                else {
                    finish();
                }
                break;
            case ACTIVITY_LIKE_DISLIKE:
                if(resultCode == Activity.RESULT_OK) {
                    int like = data.getIntExtra("edu.miami.c10656908.chooseanicephoto.like_photo", -1);


                    if(like == 1) {
                        Toast.makeText(this,R.string.like_toast_text,Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else if(like == 0) {
                        chooseAPicture();
                    }
                    else {
                        Log.i("IN myClickHandler", "Error recieving like flag");
                    }
                }
                else {
                    chooseAPicture();
                }
        }
    }



}
