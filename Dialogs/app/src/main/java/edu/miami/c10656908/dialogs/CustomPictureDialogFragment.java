package edu.miami.c10656908.dialogs;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class CustomPictureDialogFragment extends DialogFragment {

    View dialogView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle(R.string.picture_dialog_title);
        dialogView = inflater.inflate(
                R.layout.activity_custom_picture_dialog_fragment,container);
        ((Button)dialogView.findViewById(R.id.dismiss_button)).setOnClickListener(myClickHandler);
        ImageView tvPicture = (ImageView)dialogView.findViewById(R.id.tv_picture);

        switch (this.getArguments().getInt("Picture_Index")) {
            case 0:
               tvPicture.setImageResource(R.drawable.deadliestcatch);
                break;
            case 1:
                tvPicture.setImageResource(R.drawable.archer);
                break;
            case 2:
                tvPicture.setImageResource(R.drawable.topgear);
                break;
            case 3:
                tvPicture.setImageResource(R.drawable.mash);
                break;
            case 4:
                tvPicture.setImageResource(R.drawable.twoandahalfmen);
                break;
        }



        return(dialogView);

    }
    private View.OnClickListener myClickHandler = new View.OnClickListener() {
        public void onClick(View view) {
            dismiss();
        }
    };
}
