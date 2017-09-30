package edu.miami.c10656908.chooseanicephoto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.net.URI;

public class LikeDislike extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String transferedURIString;
        Uri transferedURI;
        ImageView pictureView;
        Bitmap selectedPicture;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_dislike);

        transferedURIString = this.getIntent().getStringExtra("edu.miami.c10656908.chooseanicephoto.selectedURI");
        if(transferedURIString == null) {
            finish();
        }
        else {
            transferedURI = Uri.parse(transferedURIString);
            pictureView = (ImageView)findViewById(R.id.selected_picture);
            if(pictureView == null) {
                Log.i("IN LIKEDISLIKE", "Picture view is null");
            }
            try {

                selectedPicture = MediaStore.Images.Media.getBitmap(
                        this.getContentResolver(), transferedURI);
                if(selectedPicture == null) {
                    Log.i("IN LIKEDISLIKE", "Selected Picture is null");
                }
                pictureView.setImageBitmap(selectedPicture);
            }
            catch(Exception e) {
                Log.e("test","test"+e);
            }
        }
    }
    public void myClickHandler(View view) {
        Intent returnIntent;
        returnIntent = new Intent();
        switch (view.getId()) {

            case R.id.like_photo:

                returnIntent.putExtra(
                        "edu.miami.c10656908.chooseanicephoto.like_photo",
                        1);
                setResult(RESULT_OK, returnIntent);
                finish();
                break;
            case R.id.dislike_photo:
                returnIntent.putExtra(
                        "edu.miami.c10656908.chooseanicephoto.like_photo",
                        0);
                setResult(RESULT_OK, returnIntent);
                finish();
                break;
            default:
                break;
        }
    }


}
