package edu.miami.c10656908.texttime;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private  timetextDB notesDB;
    private Cursor noteCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleCursorAdapter cursorAdapter;
        ListView theList;


        String[] displayFields = {
                "time",
                "idea"
        };
        int[] displayViews = {
                R.id.time,
                R.id.idea
        };

        notesDB = new timetextDB(this);

        noteCursor = notesDB.fetchAllNotes();
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_item_layout, noteCursor,  displayFields,displayViews, 0);



    }

    public void onDestroy(){
        super.onDestroy();
        noteCursor.close();;
        notesDB.close();
    }

    public void myClickHandler(View view) {

        ContentValues noteData;
        noteData = new ContentValues();

        switch (view.getId()) {
            case R.id.save:
                DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                Date dateobj = new Date();
                String date = df.format(dateobj);
                EditText editText = (EditText)findViewById(R.id.typedIdea);
                String ideaText = editText.getText().toString();

                noteData.put("time", date);
                noteData.put("idea", ideaText);
                editText.setText("");



                notesDB.addNote(noteData);
                break;
            case R.id.view:

                Intent nextActivity;
                nextActivity  = new Intent(this, noteList.class);
                startActivity(nextActivity);



        }
    }
}
