package com.companywesbite.myucdquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.companywesbite.myucdquiz.questionClasses.note;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;
import com.companywesbite.myucdquiz.utils.NoteListViewAdapter;

import java.util.List;

public class SnapNotesFragment extends Fragment {
    private static final String TAG = "SnapNotesFragment";


    private Button newSnapButton;
    private ListView lstview;
    private static final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 1000;


    Context context;
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.activity_snap_notes_fragment,container,false);


        context=getContext();
        lstview=(ListView)view.findViewById(R.id.notesList);

        // creating a new quiz.
        newSnapButton = (Button) view.findViewById(R.id.snapNotesButton);
        newSnapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCamera();
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateQuizListView();
    }

    private void updateQuizListView()
    {
        DatabaseHelper db = new DatabaseHelper(getContext());
        final List<note> notes = db.getAllNotes();


        NoteListViewAdapter adapter=new NoteListViewAdapter(getContext(),R.layout.note_item,R.id.noteName,notes);
        // Bind data to the ListView

        lstview.setAdapter(adapter);
        lstview.setEmptyView((TextView) view.findViewById(R.id.emptySnapList));
    }

    public void clickMe(long quizId){
        // when a user clicks a note...

    }


    private void loadCamera()
    {
        // here is where we will load the camera activity....
        Intent i = new Intent(getContext(), CameraActivity.class);
        startActivity(i);
    }

}
