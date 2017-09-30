package edu.miami.c10656908.dialogs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ListDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder dialogBuilder;

        dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(R.string.list_dialog_title);
        dialogBuilder.setItems(R.array.tv_shows,listListener);
        return (dialogBuilder.create());
    }
    private DialogInterface.OnClickListener listListener =
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int indexClicked) {
                    StartPictureDialog activity = (StartPictureDialog)getActivity();
                    activity.startPictureDialog(indexClicked);
                    }
            };
    public interface StartPictureDialog {
        public void startPictureDialog(int index);
    }

}
