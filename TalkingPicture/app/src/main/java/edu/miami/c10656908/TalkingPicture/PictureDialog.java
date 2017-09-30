package edu.miami.c10656908.TalkingPicture;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by Michael on 11/23/2016.
 */
public class PictureDialog extends DialogFragment {

    View dialogView;
    ImageView dialogImage;
    Bitmap bitmap;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflate xml layout
        dialogView = inflater.inflate(R.layout.custom_picture_dialog_layout, container);
        dialogImage = (ImageView) dialogView.findViewById(R.id.dialog_image);

        //get uri string passed to dialog, and parse it back to uri
        String stringUri = this.getArguments().getString("uri");
        Uri uri = Uri.parse(stringUri);

        //set image to uri
        dialogImage.setImageURI(uri);

        //set click listener programatically, because it doesn't work properly if you put it in the xml
        ((Button) dialogView.findViewById(R.id.dialog_button)).
                setOnClickListener(myClickHandler);
        return dialogView;
    }

    //dismiss when you click dismiss button
    private View.OnClickListener myClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            dismiss();

        }
    };

    //interface for main activity to implement so it can resume music when dialog is dismissed
    public interface DialogDismisser {
        public void onDismiss();
    }



    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        //call main activity's dismiss method
        DialogDismisser activity = (DialogDismisser)getActivity();
        activity.onDismiss();

    }
}
