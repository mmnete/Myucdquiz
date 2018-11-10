package com.companywesbite.myucdquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.companywesbite.myucdquiz.questionClasses.note;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;

public class NoteReaderActivity extends AppCompatActivity {


    private long noteId;
    private note currNote;
    private String currNoteText;
    private String noteTitle;

    private Button saveButton;
    private EditText textDisplayer;



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

        saveButton = (Button) findViewById(R.id.saveButton);
        textDisplayer = (EditText) findViewById(R.id.noteText);

        textDisplayer.setText(currNoteText);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(getBaseContext());
                currNote.setNotes(textDisplayer.getText().toString().trim());
                db.updateNote(currNote);
                Toast.makeText(getBaseContext(),"Changes Made",Toast.LENGTH_LONG).show();
            }
        });
        

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
