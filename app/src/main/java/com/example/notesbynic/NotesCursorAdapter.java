package com.example.notesbynic;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NotesCursorAdapter extends CursorAdapter{
    public NotesCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //Pass in layout from custom layout note_list_item and pass it back whenever the newView method is called
        return LayoutInflater.from(context).inflate(R.layout.note_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //Get text of the note
        String noteText = cursor.getString(
                //Which column
                cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
        int pos = noteText.indexOf(10);
        if (pos != -1){
            noteText = noteText.substring(0, pos) + " ...";
        }

        TextView tv = (TextView) view.findViewById(R.id.tvNote);
        tv.setText(noteText);
    }
}
