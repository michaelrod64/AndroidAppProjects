package edu.miami.c10656908.TalkingPicture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditActivity extends AppCompatActivity {

    private MediaRecorder recorder;
    private String recordFileName;
    private EditText editDescription;
    private long noteId;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Uri uri;
        String uriString;
        String imageDescription;

        //retrieve image uri as string and parse it back to uri
        uriString = this.getIntent().getStringExtra("uri");
        uri = Uri.parse(uriString);

        //get the imageDescriptoin and id of row of database we're working with
        imageDescription = this.getIntent().getStringExtra("note");
        noteId = this.getIntent().getLongExtra("noteId", 1);

        image = (ImageView) findViewById(R.id.edit_image);
        editDescription = (EditText) findViewById(R.id.edit_text);

        image.setImageURI(uri);

        //if imageDescription exists, set the editText to that description
        if (imageDescription != null) {
            editDescription.setText(imageDescription);
        }
        //otherwise, set text to empty string and put hint telling user to add description
        else {
            editDescription.setText("");
            editDescription.setHint(getResources().getString(R.string.edittext_no_description));
        }
    }

    public void myClickHandler(View view) {
        //create file for recording to be stored
        String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";

        //initialize file
        File soundDataFile = null;
        switch (view.getId()) {

            //user chooses to record
            case R.id.edit_record_button:

                //set up storage file and recorder
                try {
                    soundDataFile = new File(mFileName);
                    recorder = new MediaRecorder();
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    recorder.setOutputFile(mFileName);
                } catch (Exception e) {

                }
                try {
                    recorder.prepare();
                } catch (IOException e) {
                    Log.i("AUDIO ERROR", "PREPARING RECORDER");
                }
                //start recorder
                recorder.start();
                break;
            //user chooses to stop recording. stop recorder and release
            case R.id.edit_stop_button:
                recorder.stop();
                recorder.release();
                break;
            //user chooses to clear recording. Set recording file to null
            case R.id.edit_clear_button:
                soundDataFile = null;
                break;

            //user chooses to save changes.
            case R.id.edit_save_button:

                Intent returnIntent;

                returnIntent = new Intent();
                Log.i("test", "note content before being returned" + editDescription.getText().toString());
                //store image description and id of current database row in return intent
                returnIntent.putExtra("note", editDescription.getText().toString());
                returnIntent.putExtra("noteId", noteId);

                /*if there is a recording to be stored, transfer recording from file to byte array
                and then store array into return intent
                 */
                if (soundDataFile != null) {
                    int size = (int) soundDataFile.length();
                    byte[] bytes = new byte[size];
                    try {
                        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(soundDataFile));
                        buf.read(bytes, 0, bytes.length);
                        buf.close();
                        returnIntent.putExtra("recording", bytes);
                    } catch (Exception e) {

                    }
                }
                //otherwise, return null string
                else {
                    returnIntent.putExtra("recording", (String)null);
                }

                //set result with RESULT_OKAY and return intent
                setResult(RESULT_OK, returnIntent);

                //if recorder was used, release it
                if(recorder != null) {
                    recorder.release();
                }

                finish();
        }
    }
}
