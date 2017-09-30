package edu.miami.c10656908.TalkingPicture;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.StaleDataException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SimpleCursorAdapter.ViewBinder, TextToSpeech.OnInitListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, PictureDialog.DialogDismisser {

    //variables used by multiple methods. must be global
    public static final int EDIT_ACTIVITY = 1;
    private Cursor audioCursor;
    private MediaPlayer myPlayer;
    private MediaPlayer myRecordingPlayer;
    private imageNoteDB notesDB;
    private Cursor noteCursor;
    private Cursor imageMediaCursor;
    private TextToSpeech mySpeaker;
    private SimpleCursorAdapter cursorAdapter;

    //fields to be accessed from the image media store
    private String[] queryFields = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.DATA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        int idIndex;
        int dataIndex;
        int audioDataIndex;
        String audioFilename;
        int audioposition;
        int min = 0;
        int audiomax;

        ListView theList;

        theList = (ListView)findViewById(R.id.the_list);

        //fields to be accessed from database to display in listview
        String[] displayFields = {
                "image_id",
                "note",
                "recording"
        };
        //Id of views that will display data from database
        int[] displayViews = {
                R.id.image,
                R.id.note,
                R.id.checkbox
        };


        myPlayer = new MediaPlayer();

        //fields to be accessed from the audio media store
        String[] audioqueryFields = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA     //----Path to file on disk
        };

        //get audio cursor for random song
        audioCursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,audioqueryFields,null,null,
                MediaStore.Audio.Media.TITLE + " ASC");

        //make sure cursor isn't null
        try {
            audioCursor.moveToFirst();
        }
        catch (Exception e) {
            Log.i("error","null cursor");
        };

        //get random song to play from audio cursor
        Random random = new Random();
        audioDataIndex = audioCursor.getColumnIndex(
                MediaStore.Audio.Media.DATA);
        audiomax = audioCursor.getCount() - 1;
        //get a random index between 0 and the last song in the audiocursor
        audioposition = random.nextInt((audiomax - min) + 1) + min;
        audioCursor.moveToPosition(audioposition);
        audioFilename = audioCursor.getString(audioDataIndex);


        //play random song chosen.
        try {
            myPlayer.setDataSource(audioFilename);
            myPlayer.prepare();
            myPlayer.start();
            //close audio cursor because we're done with it to save memory
            audioCursor.close();
        } catch (IOException e) {
            //----Should do something here
        }

        //create new instance of our database class to store image and description data
        notesDB = new imageNoteDB(this);

        //get cursor for images from media store
       imageMediaCursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,queryFields,null,null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER);

        //make sure ImageCursor not null
        if (imageMediaCursor.moveToFirst()) {
            //update our database. Add new pictures. Remove deleted ones.
            updateImageDBFromContent();

            //get cursor to newly updated database
            noteCursor = notesDB.fetchAllNotes();

            //make a cursor adaptor to transfer info from database to listview
            cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_item_layout, noteCursor, displayFields, displayViews, 0);
            //set this class to override setViewValue method to take control of transfer of data from database to listview
            cursorAdapter.setViewBinder(this);

            //give the listview our adapter
            theList.setAdapter(cursorAdapter);

            //set this class to handle clicks and long clicks
            theList.setOnItemClickListener(this);
            theList.setOnItemLongClickListener(this);

        }


        //text to speech to say photo descriptions
        mySpeaker = new TextToSpeech(this,this);



    }

    private void updateImageDBFromContent() {
        Log.i("repeating", "updateImageDBfromContent repeating");


        ContentValues imageData;
        int imageMediaId;

        //Add all new photos to the database
        do {
            //get the image Id for the current photo
            imageMediaId = imageMediaCursor.getInt(
                    imageMediaCursor.getColumnIndex(MediaStore.Images.Media._ID));
            Log.i("imagemediaid ", imageMediaId + "");

            //if a photo is in the MediaStore, but not in the database, add it to the database
            if (notesDB.getNoteByImageMediaId(imageMediaId) == null) {
                imageData = new ContentValues();
                imageData.put("image_id",imageMediaId);
                notesDB.addNote(imageData);
            }
            //move to next image in MediaStore
        } while (imageMediaCursor.moveToNext());

        int databaseImageMediaId;
        int mediastoreImageMediaId;
        ContentValues noteContent;
        ContentValues noteToDeleteContent;
        int noteToDeleteId;
        boolean fileNotDeleted = false;

        //move back to beginning of ImageMediaCursor
        imageMediaCursor.moveToFirst();
        //get an updated noteCursor, and move back to beginning
        noteCursor = notesDB.fetchAllNotes();
        noteCursor.moveToFirst();

        //Remove all deleted photos from the database
        //for each image in the database, compare it to all the images in the MediaStore.
        // If there is a match, the photo has NOT been deleted from the MediaStore. Mark it to NOT be deleted from database.
        // If there is no match, the photo has been deleted from the MediaStore. Delete it from database
        do {

            noteContent = notesDB.noteDataFromCursorsCurrentPosition(noteCursor);
            databaseImageMediaId = (int)noteContent.get("image_id");
            do {
                mediastoreImageMediaId = imageMediaCursor.getInt(imageMediaCursor.getColumnIndex(MediaStore.Images.Media._ID));

                //Log.i("databaseImagemMediaId ", "" + databaseImageMediaId);
                //Log.i("mediastoreimagemediaId ", "" + mediastoreImageMediaId);
                if(databaseImageMediaId == mediastoreImageMediaId) {
                    fileNotDeleted = true;
                }
            }
            while (imageMediaCursor.moveToNext());

            //if the file has NOT been marked as "fileNotDeleted", delete the file from database
            if(!fileNotDeleted) {
                noteToDeleteContent = notesDB.getNoteByImageMediaId(databaseImageMediaId);
                noteToDeleteId = (int) noteToDeleteContent.get("_id");
                notesDB.deleteNote(noteToDeleteId);
            }

            //reset File deletion marker, and bring imageMediaCursor back to first
            fileNotDeleted = false;
            imageMediaCursor.moveToFirst();
        }
        while (noteCursor.moveToNext() );
    }

    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
        Log.i("setViewValue", "entered setViewValue at all");

        //for the ImageView, take the imageId from the database, find it in the MediaStore, get the thumbNailBitmap, assign it to the ImageView
        switch (view.getId()) {
            case R.id.image:
                Log.i("setViewValue", "entered imagecase at all");

                //get this row of the ListView's ImageView
                ImageView image = (ImageView)view;

                int idIndex = 0;

                //get the imageId for this row of our database
                int imageId = cursor.getInt(columnIndex);

                boolean imageFound = false;
                Bitmap thumbnailBitmap;
                if(imageMediaCursor.moveToFirst()) {
                    //get the imageId for this row of the MediaStore
                    idIndex = imageMediaCursor.getColumnIndex(MediaStore.Audio.Media._ID);
                    do {
                        //if our database imageId is equal to the MediaStore's imageId, mark imageFound as true
                        imageFound = imageId == imageMediaCursor.getInt(idIndex);

                        //if the image has not been found, and there are more images to check, continue the loop
                    } while(!imageFound && imageMediaCursor.moveToNext());
                }
                //if the image is found, get the thumbnail from the MediaStore, and assign it to the ImageView
                if(imageFound) {
                    thumbnailBitmap = MediaStore.Images.Thumbnails.getThumbnail(
                            getContentResolver(),imageMediaCursor.getInt(idIndex),
                            MediaStore.Images.Thumbnails.MICRO_KIND,null);

                    image.setImageBitmap(thumbnailBitmap);
                    return true;
                }
                else {
                    Toast.makeText(this, "Can't find image", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.note:
                //if there is no photo description stored in the database, set the textView's text to an empty string, and set it's hint to "No description yet"
                if(cursor.getString(columnIndex) == null) {
                    TextView textView = (TextView)view;
                    textView.setText("");
                    //textView.setText("No description yet");
                    textView.setHint(getResources().getString(R.string.textview_no_description));
                    return true;
                }
                //if there is a photo description in the database, let the adaptor handle it
                else {
                    return false;
                }

            case R.id.checkbox:
                CheckBox checkBox = (CheckBox)view;

                //if there is a recording stored in the database, set check the checkbox
                if(cursor.getBlob(columnIndex) != null) {
                    checkBox.setChecked(true);
                }
                //otherwise, uncheck the checkbox
                else {
                    checkBox.setChecked(false);
                }
                return true;

            default:
                Toast.makeText(this, "Can't find anything", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }



    protected void onPause() {
        Log.i("test", "entered onpause");
        super.onPause();
        //stop all resources not in use in order to save memory
        imageMediaCursor.close();
        noteCursor.close();
        mySpeaker.stop();
        mySpeaker.shutdown();
        //pause the music
        myPlayer.pause();

    }
    protected void onResume() {
        Log.i("test", "entered onresume");
        //resume the music
        myPlayer.start();
        //reset the TextToSpeech, imageMediaCursor, and noteCursor that were closed in onPause
        mySpeaker = new TextToSpeech(this,this);
        imageMediaCursor =  getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,queryFields,null,null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        noteCursor = notesDB.fetchAllNotes();
        //set the cursorAdaptor to the reset noteCursor
        cursorAdapter.changeCursor(noteCursor);
        super.onResume();


    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
        //release all resources when app destroyed
        myPlayer.release();
        audioCursor.close();
        imageMediaCursor.close();
        noteCursor.close();;
        notesDB.close();
        mySpeaker.shutdown();
    }
    //ensure that TextToSpeech started successfully
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Log.i("I", "Text to speech success");
        } else {
            Toast.makeText(this,"You need to install TextToSpeech",
                    Toast.LENGTH_LONG).show();
            Log.i("I", "Text to speech failure");
            finish();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        //get row of database associated with clicked row of ListView
        ContentValues note = notesDB.getNoteById(l);
        //get Id of image from that row
        int pictureId = (int)note.get("image_id");
        int idIndex;
        boolean imageFound = false;
        Uri uri;
        String imageFilename;

        //compare imageId of each row of imageMediaCursor to our row's imageId
        if(imageMediaCursor.moveToFirst()) {
            idIndex = imageMediaCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            do {
                imageFound = pictureId == imageMediaCursor.getInt(idIndex);
            } while(!imageFound && imageMediaCursor.moveToNext());
        }
        //if there is a match, get the filename of the image
        if(imageFound) {
            imageFilename = imageMediaCursor.getString(imageMediaCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //test if we can get uri from the filename. If so, store uri.
            if ((uri = Uri.parse(new File(imageFilename).toString())) != null) {
                myPlayer.pause();


                //Make bundle to send uri to pictureDialog
                Bundle bundleToFragment;
                bundleToFragment = new Bundle();
                bundleToFragment.putString("uri", uri.toString());

                PictureDialog pictureDialog = new PictureDialog();

                //hand bundle to picture diaglog, and start it
                pictureDialog.setArguments(bundleToFragment);
                pictureDialog.show(getFragmentManager(), "my_fragment");



                //get the description and recording from database
                String noteText = (String) note.get("note");
                //retrieve recording as array of bytes
                byte[] recordingBytes = (byte[]) note.get("recording");


                //if there's a description, speak the descriptioin
                if(noteText != null) {
                    HashMap<String, String> speechParameters = new HashMap<String, String>();
                    speechParameters.put(
                            TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "WHAT_I_SAID");
                    mySpeaker.speak(noteText, TextToSpeech.QUEUE_ADD,
                            speechParameters);
                }

                //if there is a recording, speak it
                if(recordingBytes != null) {
                    try {
                        //Make a file
                        String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
                        mFileName += "/audiorecordtest.3gp";
                        File soundDataFile = new File(mFileName);
                        //store recording into file
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(soundDataFile));
                        bos.write(recordingBytes);
                        bos.flush();
                        bos.close();

                        //set datasource of player to the recoring file and play
                        myRecordingPlayer.setDataSource(soundDataFile.getPath());
                        myRecordingPlayer.prepare();
                        myRecordingPlayer.start();
                    }
                    catch (Exception e) {

                    }

                }

            }
            //otherwise, delete row from database
            else {
                notesDB.deleteNote(l);
                cursorAdapter.changeCursor(notesDB.fetchAllNotes());
            }
        }



        Log.i("test", "it worked");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long rowId) {
        //get row of database associated with row of ListView
        ContentValues note = notesDB.getNoteById(rowId);
        //get the imageId
        int pictureId = (int)note.get("image_id");
        //get the pictureDescription
        String pictureDescription = (String)note.get("note");
        int idIndex;
        boolean imageFound = false;
        Uri uri;
        String imageFilename;

        //compare imageId of each row of imageMediaCursor to our row's imageId
        if(imageMediaCursor.moveToFirst()) {
            idIndex = imageMediaCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            do {
                imageFound = pictureId == imageMediaCursor.getInt(idIndex);
            } while(!imageFound && imageMediaCursor.moveToNext());
        }
        //if there is a match, get the filename of the image
        if(imageFound) {
            imageFilename = imageMediaCursor.getString(imageMediaCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //test if we can get uri from the filename. If so, store uri.
            if ((uri = Uri.parse(new File(imageFilename).toString())) != null) {



                //store uri, pictureDescription, and id of the row of the database we're working with
                Intent nextActivity;
                nextActivity = new Intent(this,EditActivity.class);
                nextActivity.putExtra("uri", uri.toString());
                nextActivity.putExtra("note", pictureDescription);
                nextActivity.putExtra("noteId", rowId);

                startActivityForResult(nextActivity,EDIT_ACTIVITY);

            }
            //otherwise, delete row from database
            else {
                notesDB.deleteNote(rowId);
                cursorAdapter.changeCursor(notesDB.fetchAllNotes());
            }
        }




        Log.i("test", "long click worked");
        return true;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //if successful, store new info into database
        if(resultCode == RESULT_OK) {
            //get rowID of database to work with
            long noteId = data.getLongExtra("noteId", 1);
            String note;
            byte[] bytes;
            //get ContentValues of row we're working with
            ContentValues noteContent = notesDB.getNoteById(noteId);


            //get the new image description
            note = data.getStringExtra("note");
            Log.i("test", "Returned note content: " + note);

            //if someone puts an empty string as the description, consider it to be null
            if (note != null) {
                if (note.equals("")) {
                    note = null;
                }
            }

            //get the recording
            bytes = data.getByteArrayExtra("recording");

            //put the image description and recording into the ContentValues
            noteContent.put("note", note);
            noteContent.put("recording", bytes);
            //update the row in the database with new info
            notesDB.updateNote(noteId, noteContent);
            //update the cursorAdapter's cursor with updated database
            cursorAdapter.changeCursor(notesDB.fetchAllNotes());

        }


    }

    //when dialog is dismissed, stop all speaking, and resume music
    public void onDismiss() {
       mySpeaker.stop();
       myPlayer.start();
   }
}
