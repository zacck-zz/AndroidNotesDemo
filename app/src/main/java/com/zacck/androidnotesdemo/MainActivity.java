package com.zacck.androidnotesdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ListView mNotesList;
    static ArrayList<String> mNotes = new ArrayList<>();
    static ArrayAdapter mArrayAdapter;
    static Set<String> mNotesSet;
    SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //init list
        mNotesList = (ListView)findViewById(R.id.lvNotes);

        mSharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        //try and restore a set
        mNotesSet = mSharedPreferences.getStringSet("notes",null);

        mNotes.clear();

        if(mNotesSet != null)
        {

            mNotes.addAll(mNotesSet);
        }
        else
        {
            //if set is null
            mNotes.add("Example Note");
            //initialize set
            mNotesSet = new HashSet<String>();
            mNotesSet.addAll(mNotes);
            mSharedPreferences.edit().putStringSet("notes", mNotesSet).apply();
        }

        //adapter
        mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mNotes);

        mNotesList.setAdapter(mArrayAdapter);

        //edit note
        mNotesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //move to the other activityu
                Intent editIntent = new Intent(getApplicationContext(), Edit.class);
                //add the note Id
                editIntent.putExtra("noteId", position);
                startActivity(editIntent);
            }
        });

        //delete note
        mNotesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //build in alert Dialog
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are You Sure")
                        .setMessage("Do you want to Delete This note")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mNotes.remove(position);
                                saveSet();

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                //return true so the long click consumes the click and the other click not processed
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
                mNotes.add("");
                saveSet();
                Intent editIntent = new Intent(getApplicationContext(), Edit.class);
                //add the note Id
                editIntent.putExtra("noteId", mNotes.size()-1);
                startActivity(editIntent);


            }
        });
    }

    public void saveSet()
    {
        if(mNotesSet == null)
        {
            mNotesSet = new HashSet<String>();
        }
        else
        {
            mNotesSet.clear();
        }
        mNotesSet.addAll(mNotes);
        //cleare the stringset
        mSharedPreferences.edit().remove("notes").apply();
        //update the string set
        mSharedPreferences.edit().putStringSet("notes",mNotesSet).apply();
        mArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
