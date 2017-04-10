package com.example.notesbynic;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditorActivity extends AppCompatActivity {
    //Remember what I'm doing (insert, delete, etc)
    private String action;
    //Represent the edit text object, the editor control the user is typing inti
    private EditText editor;


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
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_editor, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(item.getItemId()){
            case android.R.id.home:
                finishEditing();
                break;
        }

        return true;
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
        }
        finish();
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
