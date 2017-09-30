package edu.miami.c10656908.songpicture;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Cursor audioCursor;
    private Cursor imagesCursor;
    private MediaPlayer myPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ImageView picture;
        int idIndex;
        int dataIndex;
        Uri fullImageURI;
        int audioDataIndex;
        String audioFilename;
        int audioposition;
        int imageposition;
        int min = 0;
        int imagemax;
        int audiomax;



        picture = (ImageView)findViewById(R.id.image);

        //picture.setImageResource(R.drawable.ic_action_name);
        myPlayer = new MediaPlayer();


        String[] audioqueryFields = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA     //----Path to file on disk
        };
        String[] imagequeryFields = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA
        };



        audioCursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,audioqueryFields,null,null,
                MediaStore.Audio.Media.TITLE + " ASC");

        imagesCursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,imagequeryFields,null,null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER);

        try {
            audioCursor.moveToFirst();
            imagesCursor.moveToFirst();
        }
        catch (Exception e) {
            Log.i("error","null cursor");
        };



        Random random = new Random();

        imagemax = imagesCursor.getCount() - 1;
        imageposition = random.nextInt((imagemax - min) + 1) + min;
        imagesCursor.moveToPosition(imageposition);

        idIndex = imagesCursor.getColumnIndex(MediaStore.Images.Media._ID);

        dataIndex = imagesCursor.getColumnIndex(MediaStore.Images.Media.DATA);



        idIndex = imagesCursor.getColumnIndex(MediaStore.Images.Media._ID);

        Log.i("imagesCurser.getString:", imagesCursor.getString(dataIndex));
        Log.i("URI", Uri.fromFile(new File(imagesCursor.getString(dataIndex))).toString());

        File imgFile = new  File(imagesCursor.getString(dataIndex));
        if(imgFile.exists()){
            Uri thefileuri = Uri.fromFile(imgFile);
            Drawable imagedrawable = Drawable.createFromPath(imagesCursor.getString(dataIndex));

           ///picture.setImageDrawable(imagedrawable);
            Log.i("It exists", "the file exists");
        }
        else {
            Log.i("It does not exist", "the file does not exist");
        }

        //Bitmap thumbImage = BitmapFactory.decodeFile(imagesCursor.getString(dataIndex));
        //picture.setImageBitmap(thumbImage);

        if ((fullImageURI = Uri.fromFile(new File(imagesCursor.getString(
                dataIndex)))) != null) {
           picture.setImageURI(fullImageURI);
        }
        myPlayer.reset();
        audioDataIndex = audioCursor.getColumnIndex(
                MediaStore.Audio.Media.DATA);


        audiomax = audioCursor.getCount() - 1;



        audioposition = random.nextInt((audiomax - min) + 1) + min;

        audioCursor.moveToPosition(audioposition);
        audioFilename = audioCursor.getString(audioDataIndex);
        try {
            myPlayer.setDataSource(audioFilename);
            myPlayer.prepare();
            myPlayer.start();
        } catch (IOException e) {
            //----Should do something here
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myPlayer.release();
        audioCursor.close();
        imagesCursor.close();
    }
}

