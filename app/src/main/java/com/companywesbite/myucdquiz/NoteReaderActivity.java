package com.companywesbite.myucdquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.companywesbite.myucdquiz.questionClasses.note;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;

public class NoteReaderActivity extends AppCompatActivity {


    private long noteId;
    private note currNote;
    private String currNoteText;
    private String noteTitle;

    private TextView textDisplayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_reader);

        Intent i = getIntent();

        noteId = i.getLongExtra("noteId", 100);

        DatabaseHelper db = new DatabaseHelper(getBaseContext());

        currNote = db.getNote(noteId);

        currNoteText = currNote.getNotes();
        noteTitle = currNote.getTitle();

        getSupportActionBar().setTitle(noteTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        textDisplayer = (TextView) findViewById(R.id.noteText);

        textDisplayer.setText(currNoteText);

    }


    // when the user clicks the back button .. we cannot let the user come back here
    // This is for the back button
    public boolean onOptionsItemSelected(MenuItem item){
       /* Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(myIntent);
        */
        finish();
        return true;
    }

}
