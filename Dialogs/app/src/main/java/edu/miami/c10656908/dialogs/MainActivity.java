package edu.miami.c10656908.dialogs;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements ListDialogFragment.StartPictureDialog {

    CustomPictureDialogFragment pictureDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void myClickListener(View view){
        ListDialogFragment theDialogFragment;
        theDialogFragment = new ListDialogFragment();
        theDialogFragment.show(getFragmentManager(),"my_fragment");
    }
    public void startPictureDialog(int index) {
        Bundle bundleToFragment;

        bundleToFragment = new Bundle();

        bundleToFragment.putInt("Picture_Index", index);
        pictureDialog = new CustomPictureDialogFragment();
        pictureDialog.setArguments(bundleToFragment);
        pictureDialog.show(getFragmentManager(), "my_picture_fragment");

        }

}
