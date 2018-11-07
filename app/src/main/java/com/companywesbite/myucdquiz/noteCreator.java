package com.companywesbite.myucdquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.companywesbite.myucdquiz.questionClasses.note;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;

public class noteCreator extends AppCompatActivity {


    private EditText noteTitle;
    private Button cancelButton;
    private Button addButton;

    private String noteString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_creator);

        getSupportActionBar().setTitle("New Note");


        // get the information about this quiz
        Intent i = getIntent();

        noteString = i.getStringExtra("noteValue");

        noteTitle = findViewById(R.id.noteTitle);
        cancelButton = findViewById(R.id.cancelButton);
        addButton = findViewById(R.id.addButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creatNewNote();
            }
        });



    }

    private void creatNewNote()
    {
        String noteTitleString = noteTitle.getText().toString().trim();
        if(noteTitleString.length() < 3)
        {
            Toast.makeText(this,"Title too short", Toast.LENGTH_LONG).show();
            return;
        }
        if(noteTitleString.length() > 50)
        {
            Toast.makeText(this,"Title too long", Toast.LENGTH_LONG).show();
            return;
        }
        DatabaseHelper db = new DatabaseHelper(this);
        note note = new note(noteTitleString,noteString);
        db.createNote(note);
        finish();
    }
}
