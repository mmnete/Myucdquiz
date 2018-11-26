package com.companywesbite.myucdquiz;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.companywesbite.myucdquiz.questionClasses.quiz;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;
import com.companywesbite.myucdquiz.utils.QuizListViewAdapter;

import java.util.List;

public class QuizMainPageFragment extends Fragment {

    private static final String TAG = "QUIZ Menu";

    Context context;

    private FloatingActionButton newQuizButton;
    private ListView lstview;
    private static final int MY_PERMISSIONS_REQUEST_GET_IMAGE = 1000;
    private View view;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_quiz_main_page_fragment, container, false);

        context=getContext();
        lstview=(ListView)view.findViewById(R.id.quizList);

        // creating a new quiz.
        newQuizButton = (FloatingActionButton) view.findViewById(R.id.addQuizButton);
        newQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CreateNewQuiz.class);
                startActivity(i);
            }
        });


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        // if the user does not allow us to read any images we cannot display anything
        newQuizButton.setEnabled(false);
        checkPermission();
    }

    // Ask for permission to read images unless close the application
    private void checkPermission()
    {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Permission is not granted
            // Should we show an explanation?

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_GET_IMAGE);
        }else
        {
            // we are good to go
            newQuizButton.setEnabled(true);
            updateQuizListView();
        }
    }

    // Does the user allow us to check for images
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GET_IMAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    newQuizButton.setEnabled(true);
                    updateQuizListView();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }




    public void clickMe(long quizId){
        DatabaseHelper db = new DatabaseHelper(getContext());
        quiz temp = db.getQuiz(quizId);
        Log.d("TAG",Double.toString(temp.getPercentCorrect()));
        Intent i = new Intent(getContext(), QuizDetailActivity.class);
        i.putExtra("quizId", quizId);
        startActivity(i);
    }

    private void updateQuizListView()
    {
        DatabaseHelper db = new DatabaseHelper(getContext());
        final List<quiz> quizes = db.getAllQuizes();


        QuizListViewAdapter adapter=new QuizListViewAdapter(getContext(),R.layout.quiz_item,R.id.quizName,quizes);
        // Bind data to the ListView

        lstview.setAdapter(adapter);
        lstview.setEmptyView((TextView) view.findViewById(R.id.emptyElement));
        lstview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Log.d("TAG",Integer.toString((int)quizes.get(position).getId()));
                clickMe(quizes.get(position).getId());
            }
        });

    }


}