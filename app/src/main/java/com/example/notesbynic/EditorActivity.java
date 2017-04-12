package com.example.notesbynic;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity {
    //Remember what I'm doing (insert, delete, etc)
    private String action;
    //Represent the edit text object, the editor control the user is typing inti
    private EditText editor;
    //Where clause for sql statement
    private String noteFilter;
    //Existing text of selected note
    private String oldText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editor = (EditText) findViewById(R.id.editText2);

        Intent intent = getIntent();

        //Implements parcelable interface, makes it possible to pass complex object as an intent extra
        Uri uri = intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);

        //If user ur is passed in it wont be null but if user pressed insert button it will be null
        if (uri == null){
            //Insert new not
            action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.new_note));
        } else {
            //If uri has been passed in, means user wants to edit note
            action = Intent.ACTION_EDIT;
            noteFilter = DBOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, DBOpenHelper.ALL_COLUMNS, noteFilter, null, null);
            //Retrieve data: move to first and only row
            cursor.moveToFirst();
            oldText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
            editor.setText(oldText);
            editor.requestFocus(); //Move cursor to end of existing text

            //Get text of the note
            String noteText = cursor.getString(
                    //Which column
                    cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
            int pos = noteText.indexOf(10);
            if (pos != -1){
                noteText = noteText.substring(0, pos);
            }
            setTitle(noteText);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)){
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(item.getItemId()){
            case android.R.id.home:
                finishEditing();
                break;
            case R.id.action_delete:
                deleteNote();
                break;
        }

        return true;
    }

    private void deleteNote() {
        //Add user confirmation
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            //Insert Data management code
                            getContentResolver().delete(NotesProvider.CONTENT_URI, noteFilter, null);

                            //Toast msg to show user what happened
                            Toast.makeText(EditorActivity.this, R.string.note_deleted, Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }

    private void finishEditing(){
        //First find what the user tyoed in
        String newText = editor.getText().toString().trim();
        //Evaluate current action
        switch (action) {
            case Intent.ACTION_INSERT:
                if (newText.length() == 0){
                    setResult(RESULT_CANCELED); //Send message back to main activity to cancel whatever action was requested
                } else {
                    insertNote(newText);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newText.length() == 0){
                    deleteNote();
                } else if (oldText.equals(newText)){
                    setResult(RESULT_CANCELED);
                } else {
                    //Update note in database
                    updateNote(newText);
                }

        }
        finish();
    }

    private void updateNote(String noteText) {
        //Create values object
        ContentValues values = new ContentValues();
        //Put text into values object
        values.put(DBOpenHelper.NOTE_TEXT, noteText);
        getContentResolver().update(NotesProvider.CONTENT_URI, values, noteFilter, null);
        //(noteFilter makes sure we're only updating selected row)
        Toast.makeText(this, R.string.note_updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertNote(String noteText) {
        //Create values object
        ContentValues values = new ContentValues();
        //Put text into values object
        values.put(DBOpenHelper.NOTE_TEXT, noteText);
        //Insert data into db table
        getContentResolver().insert(NotesProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }
}
