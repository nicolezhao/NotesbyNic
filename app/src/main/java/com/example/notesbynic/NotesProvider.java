package com.example.notesbynic;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class NotesProvider extends ContentProvider{

    private static final String AUTHORITY = "com.example.plainolnotes.notesprovider";
    private static final String BASE_PATH = "notes";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    // Constant to identify the requested operation
    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);
            //Parse a Uri and tell you which operation has been requested

    //Will execute the first time anything is called from this class
    static{
        //Register operations
        uriMatcher.addURI(AUTHORITY, BASE_PATH, NOTES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTES_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        //Instance of DBOpenHelper class
        DBOpenHelper helper = new DBOpenHelper(getContext());
        //call member of the superclass SQLiteOpenHelper, creates db
        database = helper.getWritableDatabase();
        return true;
    }

    //Will get data from the database table: notes
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return database.query(DBOpenHelper.TABLE_NOTES, DBOpenHelper.ALL_COLUMNS, selection, null,
                null, null, DBOpenHelper.NOTE_CREATED + "DESC");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    //Returns URI
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //get primary key value
        // 3rd argument the pass on ContentValues object that's passed into this method
        long id = database.insert(DBOpenHelper.TABLE_NOTES, null, )
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
