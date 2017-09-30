package edu.miami.c10656908.picturelist;

import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView pictureNamesList;
    String[] pictureNames = {"Happy", "Sad", "Crazy", "Angry", "Crying"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pictureNamesList = (ListView)findViewById(R.id.pictureNamesList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pictureNames);
        pictureNamesList.setAdapter(adapter);
        pictureNamesList.setOnItemClickListener(this);
        registerForContextMenu(findViewById(R.id.face_ImageView));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        ImageView face = (ImageView)findViewById(R.id.face_ImageView);

        TextView toggledTextView;

        switch (item.getItemId()) {
            case R.id.show_face:
                face.setVisibility(View.VISIBLE);
                break;

            case R.id.hide_face:
                face.setVisibility(View.INVISIBLE);
                break;
            case R.id.toggle_happy:
                item.setChecked(!(item.isChecked()));

                toggledTextView = (TextView)pictureNamesList.getChildAt(0);
                if(item.isChecked()) {
                    toggledTextView.setEnabled(true);
                }
                else {
                    toggledTextView.setEnabled(false);

                }
                break;
            case R.id.toggle_sad:
                item.setChecked(!(item.isChecked()));

                toggledTextView = (TextView)pictureNamesList.getChildAt(1);
                if(item.isChecked()) {
                    toggledTextView.setEnabled(true);
                }
                else {
                    toggledTextView.setEnabled(false);

                }
                break;
            case R.id.toggle_crazy:
                item.setChecked(!(item.isChecked()));

                toggledTextView = (TextView)pictureNamesList.getChildAt(2);
                if(item.isChecked()) {
                    toggledTextView.setEnabled(true);
                }
                else {
                    toggledTextView.setEnabled(false);

                }
                break;
            case R.id.toggle_angry:
                item.setChecked(!(item.isChecked()));

                toggledTextView = (TextView)pictureNamesList.getChildAt(3);
                if(item.isChecked()) {
                    toggledTextView.setEnabled(true);
                }
                else {
                    toggledTextView.setEnabled(false);

                }
                break;
            case R.id.toggle_crying:
                item.setChecked(!(item.isChecked()));

                toggledTextView = (TextView)pictureNamesList.getChildAt(4);
                if(item.isChecked()) {
                    toggledTextView.setEnabled(true);
                }
                else {
                    toggledTextView.setEnabled(false);

                }
                break;
        }
        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        getMenuInflater().inflate(R.menu.face_context_menu, menu);
    }
    public boolean onContextItemSelected(MenuItem item) {
        LinearLayout faceLinearLayout = (LinearLayout)findViewById(R.id.face_LinearLayout);

        switch (item.getItemId()) {
            case R.id.context_menu_black:
                faceLinearLayout.setBackgroundColor(Color.BLACK);
                break;
            case R.id.context_menu_red:
                faceLinearLayout.setBackgroundColor(Color.RED);
                break;
            case R.id.context_menu_green:
                faceLinearLayout.setBackgroundColor(Color.GREEN);
                break;
        }
        return true;
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        TextView clickedTextView = (TextView)view;
        if(clickedTextView.isEnabled()) {


            String text = clickedTextView.getText().toString();
            ImageView faceImageView = (ImageView) findViewById(R.id.face_ImageView);


            if (text.equals("Happy")) {
                faceImageView.setImageDrawable(getResources().getDrawable(R.drawable.happyface));
            }
            if (text.equals("Sad")) {
                faceImageView.setImageDrawable(getResources().getDrawable(R.drawable.sadface));
            }
            if (text.equals("Crazy")) {
                faceImageView.setImageDrawable(getResources().getDrawable(R.drawable.crazyface));
            }
            if (text.equals("Angry")) {
                faceImageView.setImageDrawable(getResources().getDrawable(R.drawable.angryface));
            }
            if (text.equals("Crying")) {
                faceImageView.setImageDrawable(getResources().getDrawable(R.drawable.cryingface));
            }
        }
    }
}
