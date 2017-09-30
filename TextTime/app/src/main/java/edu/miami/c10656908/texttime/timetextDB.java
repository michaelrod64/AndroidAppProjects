package edu.miami.c10656908.texttime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class timetextDB {

    public static final String DATABASE_NAME = "Notes.db";
    private static final int DATABASE_VERSION = 11;

    private static final String NOTES_TABLE_NAME = "Notes";
    private static final String CREATE_NOTES_TABLE =
            "CREATE TABLE IF NOT EXISTS " + NOTES_TABLE_NAME +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "time TEXT NOT NULL, " +
                    "idea TEXT NOT NULL" +
                    ");";




    private DatabaseHelper dbHelper;
    private SQLiteDatabase theDB;


    public timetextDB(Context theContext) {
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
        String[] fieldNames = {"_id", "time", "idea"};

        return(theDB.query(NOTES_TABLE_NAME, fieldNames, null, null, null, null, "time"));
    }

    public ContentValues getNoteById(long noteId) {
        Cursor cursor;
        ContentValues noteData;

        cursor = theDB.query(NOTES_TABLE_NAME, null, "_id = \"" + noteId + "\"", null, null, null, null);
        noteData = noteDataFromCursor(cursor);
        cursor.close();
        return noteData;
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
                else if(fieldNames[index].equals("time")) {
                    noteData.put("time", cursor.getInt(index));
                }
                else if(fieldNames[index].equals("idea")) {
                    noteData.put("idea", cursor.getString(index));
                }
            }
            return(noteData);
        }
        else {
            return(null);
        }
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