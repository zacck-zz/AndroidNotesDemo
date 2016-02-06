package com.zacck.androidnotesdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class Edit extends AppCompatActivity implements TextWatcher {
    int noteID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //get intent get data
        EditText mNoteEditor = (EditText)findViewById(R.id.editNoteText);

        Intent mNotesIntent = getIntent();
        noteID = mNotesIntent.getIntExtra("noteId", -1);

        if(noteID != -1)
        {
            //grab content of the notes
            mNoteEditor.setText(MainActivity.mNotes.get(noteID));
        }

        mNoteEditor.addTextChangedListener(this);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        //using a watcher not note when the text has been changed
        //grab content of the notes
        MainActivity.mNotes.set(noteID,s.toString());
        MainActivity.mArrayAdapter.notifyDataSetChanged();
        //setup shared preferences
        SharedPreferences mSharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        if(MainActivity.mNotesSet == null)
        {
            MainActivity.mNotesSet = new HashSet<String>();
        }
        else
        {
            MainActivity.mNotesSet.clear();
        }

        MainActivity.mNotesSet.addAll(MainActivity.mNotes);
        //clear the String set
        mSharedPreferences.edit().remove("notes").apply();
        mSharedPreferences.edit().putStringSet("notes",MainActivity.mNotesSet).apply();

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
