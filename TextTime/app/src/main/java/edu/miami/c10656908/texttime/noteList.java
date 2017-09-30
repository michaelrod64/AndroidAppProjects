package edu.miami.c10656908.texttime;

import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class noteList extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    timetextDB notesDB;
    CursorAdapter cursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        Cursor noteCursor;



        String[] displayFields = {
                "time",
                "idea"
        };
        int[] displayViews = {
                R.id.time,
                R.id.idea
        };
        ListView theList;

        theList = (ListView)findViewById(R.id.the_list);

        notesDB = new timetextDB(this);

        cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_item_layout, notesDB.fetchAllNotes(),  displayFields,displayViews, 0);


        theList.setAdapter(cursorAdapter);



        theList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                notesDB.deleteNote(arg3);

                cursorAdapter.changeCursor(notesDB.fetchAllNotes());

                Log.i("clicked", "click detected");





            }

        });

    }


    public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                               long rowId) {


        notesDB.deleteNote(rowId);

        cursorAdapter.changeCursor(notesDB.fetchAllNotes());

        Log.i("clicked", "click detected");



        return true;
    }


}
