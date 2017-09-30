package edu.miami.c10656908.TalkingPicture;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Michael on 11/14/2016.
 */
public class imageNoteDB {


    public static final String DATABASE_NAME = "imageNote.db";
    private static final int DATABASE_VERSION = 21;

    private static final String NOTES_TABLE_NAME = "imageNote";
    private static final String CREATE_NOTES_TABLE =
            "CREATE TABLE IF NOT EXISTS " + NOTES_TABLE_NAME +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "image_id INTEGER NOT NULL, " +
                    "note TEXT, " +
                    "recording BLOB" +
                    ");";




    private DatabaseHelper dbHelper;
    private SQLiteDatabase theDB;

    public imageNoteDB(Context theContext) {
        dbHelper = new DatabaseHelper(theContext);
        theDB = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
        theDB.close();
    }
    public boolean addNote(ContentValues noteData){
        return(theDB.insert(NOTES_TABLE_NAME, null, noteData) >= 0);
    }

    public boolean deleteNote(long noteID) {
        return(theDB.delete(NOTES_TABLE_NAME, "_id = " + noteID, null) > 0);
    }
    public Cursor fetchAllNotes(){
        String[] fieldNames = {"_id", "image_id", "note", "recording"};

        return(theDB.query(NOTES_TABLE_NAME, fieldNames, null, null, null, null, "_id"));
    }

    public ContentValues getNoteById(long noteId) {
        Cursor cursor;
        ContentValues noteData;

        cursor = theDB.query(NOTES_TABLE_NAME, null, "_id = \"" + noteId + "\"", null, null, null, null);
        noteData = noteDataFromCursor(cursor);
        cursor.close();
        return noteData;
    }

    public ContentValues getNoteByImageMediaId(long imageMediaId) {

        Cursor cursor;
        ContentValues imageData;

        cursor = theDB.query(NOTES_TABLE_NAME,null,
                "image_id = " + imageMediaId,null,null,null,null);
        imageData = noteDataFromCursor(cursor);
        cursor.close();
        return(imageData);
    }

    /*
    Added because I needed data from a specific row in a cursor. The existing "noteDataFromCursor"
    would move to the beginning of the cursor that it was given. This method is identical to
    "noteDataFromCursor", except that it does not move to the beginning of the cursor.
     */
    public ContentValues noteDataFromCursorsCurrentPosition(Cursor cursor) {
        String[] fieldNames;
        int index;
        ContentValues noteData;

        if (cursor != null) {
            fieldNames = cursor.getColumnNames();
            noteData = new ContentValues();
            for (index = 0; index < fieldNames.length; index++) {
                Log.i("test ", "fieldnames.length " + fieldNames.length);
                Log.i("test ", "index: " + index);
                if (fieldNames[index].equals("_id")) {
                    Log.i("Index", "" + index);
                    noteData.put("_id", cursor.getInt(index));
                } else if (fieldNames[index].equals("image_id")) {
                    noteData.put("image_id", cursor.getInt(index));
                } else if (fieldNames[index].equals("note")) {
                    noteData.put("note", cursor.getString(index));
                } else if (fieldNames[index].equals("recording")) {
                    noteData.put("recording", cursor.getString(index));
                }
            }
            return (noteData);
        }
        else {
            return  null;
        }
    }


    public ContentValues noteDataFromCursor(Cursor cursor) {

        String[] fieldNames;
        int index;
        ContentValues noteData;

        if(cursor != null && cursor.moveToFirst()) {
            fieldNames = cursor.getColumnNames();
            noteData = new ContentValues();
            for(index = 0; index < fieldNames.length; index++) {
                if(fieldNames[index].equals("_id")) {
                    noteData.put("_id", cursor.getInt(index));
                }
                else if(fieldNames[index].equals("image_id")) {
                    noteData.put("image_id", cursor.getInt(index));
                }
                else if(fieldNames[index].equals("note")) {
                    noteData.put("note", cursor.getString(index));
                }
                else if(fieldNames[index].equals("recording")) {
                    noteData.put("recording", cursor.getString(index));
                }
            }
            return(noteData);
        }
        else {
            return(null);
        }
    }
    public boolean updateNote(long noteID,ContentValues noteData) {

        return(theDB.update(NOTES_TABLE_NAME,noteData,
                "_id =" + noteID,null) > 0);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {

            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_NOTES_TABLE);
        }

        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE_NAME);
            onCreate(db);
        }
    }
    
    
    
}
