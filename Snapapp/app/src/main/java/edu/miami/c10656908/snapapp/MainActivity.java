package edu.miami.c10656908.snapapp;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements
SurfaceHolder.Callback, Camera.PictureCallback, MediaPlayer.OnCompletionListener {

    private static final int ACTIVITY_CAMERA_APP = 1;
    private static final int ACTIVITY_VIDEO_APP = 2;

    private static final boolean SAVE_TO_FILE = true;
    private String cameraFileName;


    private SurfaceView cameraPreview;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private boolean cameraInUse;
    private ImageView currentPhoto;
    private boolean cameraIsPreviewing = false;

    Bitmap photoBitmap;
    FileOutputStream photoStream;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraPreview = (SurfaceView)findViewById(R.id.surface_camera);
        surfaceHolder = cameraPreview.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);
        currentPhoto = (ImageView)findViewById(R.id.taken_photo);

        cameraInUse = false;
        //cameraFileName = "/storage/emulated/0/Pictures/" + getString(R.string.camera_file_name);
        cameraFileName = Environment.getExternalStorageDirectory().toString() +
                "/Pictures/" + getString(R.string.camera_file_name);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA}, 0);

    }

    public void onRequestPermissionsResult (int requestCode,
                                     String[] permissions,
                                     int[] grantResults) {
        openCamera();
    }



    protected void onDestroy() {
        super.onDestroy();
        camera.release();
    }
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            cameraIsPreviewing = true;
//----This will make the surface be changed
            camera.startPreview();;
            ((Button)findViewById(R.id.snap)).setClickable(true);
        } catch (Exception e) {
            //----Do something
        }

    }
    public void surfaceChanged(SurfaceHolder holder,int format,int width,
                               int height) {

        Camera.Parameters cameraParameters;
        boolean sizeFound;

        if (cameraIsPreviewing) {
            camera.stopPreview();
        }
        sizeFound = false;
        cameraParameters = camera.getParameters();
        for (Camera.Size size : cameraParameters.getSupportedPreviewSizes()) {
            if (size.width == width || size.height == height) {
                width = size.width;
                height = size.height;
                sizeFound = true;
                break;
            }
        }
        if (sizeFound) {
            cameraParameters.setPreviewSize(width,height);
            camera.setParameters(cameraParameters);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Camera cannot do "+width+"x"+height,Toast.LENGTH_LONG).show();
            finish();
        }
        if (cameraIsPreviewing) {
            camera.startPreview();
        }
    }
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void MyClickHandler(View view) {




        switch (view.getId()) {
            case R.id.snap:
                ((Button)findViewById(R.id.snap)).setClickable(false);
                cameraIsPreviewing = false;
                camera.takePicture(null,null,null,this);
                break;
            case R.id.exit:
                camera.stopPreview();
                try {
                    photoStream = new FileOutputStream(cameraFileName);
                    photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, photoStream);
                    photoStream.close();
                }
                catch(IOException e) {
                    //Toast.makeText(this, getString(R.string.Error), Toast.LENGTH_LONG).show();
                }
                finish();
                break;

        }


    }

    private void openCamera() {



        if ((camera = Camera.open(0)) == null) {
            Toast.makeText(getApplicationContext(),"Camera not available!",
                    Toast.LENGTH_LONG).show();
        } else {

//----This will make the surface be created
            cameraPreview.setVisibility(View.VISIBLE);
            cameraInUse = true;
        }
    }

    public void onPictureTaken(byte[] data, Camera whichCamera) {

        photoBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        currentPhoto.setImageBitmap(photoBitmap);
        camera.startPreview();
        Button button = (Button)findViewById(R.id.snap);
        button.setClickable(true);
    }

    public void onCompletion(MediaPlayer mediaPlayer) {


    }
}