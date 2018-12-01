package com.companywesbite.myucdquiz.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.companywesbite.myucdquiz.R;
import com.companywesbite.myucdquiz.questionClasses.question;
import com.companywesbite.myucdquiz.questionClasses.quiz;
import com.companywesbite.myucdquiz.utilUI.AnswerQuestionDialogBox;

import java.util.Collections;
import java.util.List;


/***
 *
 * Team: Flashcards Pro
 * Date: 2018-11-30
 * Name: ONGOINGQuestionListViewAdapter
 * Functionality: This is a class is the one responsible for displaying the question list in the
 *                quizdetailactivity - this is the activity ran when a user click on a specific
 *                quiz in the first page of the app. It conttrols how each question should be displayed
 *
 *                It displays the question title [if present], otherwise it just shows a snippet of the question
 *                itself, and also shows if a question is answered or not using an awesome image UI
 *
 *                It contains a shuffle method that shuffles questions in a quiz - a feature we added
 *                in our app to enable learners to be able to work with a random order of their questuons
 *
 *
 *
 */




public class ONGOINGQuestionListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<question> arraylist;
    Activity activity;
    private  AnswerQuestionDialogBox dialogBox;
    private long quizId;
    private quiz thisQuiz;
    private DatabaseHelper db;


    public ONGOINGQuestionListViewAdapter(Context context,Activity activity, List<question> arraylist, quiz thisQuiz, DatabaseHelper db) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = arraylist;
        this.activity = activity;
        this.quizId = thisQuiz.getId();
        this.db = db;
        this.thisQuiz = thisQuiz;
    }

    public class ViewHolder {
        TextView name;
        ImageView correctOrNot;
        //TextView score;
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public question getItem(int position) {
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    // All the view displaying operations
    // All the methods that get called when actions are done on list elements like clicking
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.ongoing_question_item, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.questionName);
            holder.correctOrNot = (ImageView) view.findViewById(R.id.correctOrNot);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(arraylist.get(position).getTitle());
        if (arraylist.get(position).getCorrect()==1)
        {
            holder.correctOrNot.setImageResource(R.drawable.ic_check_circle_green_24dp);
        } else {
            holder.correctOrNot.setImageResource(R.drawable.ic_cancel_red_24dp);

        }

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dialogBox = new AnswerQuestionDialogBox(arraylist.get(position),arraylist,position,mContext, thisQuiz);
              dialogBox.showDialog(activity);
            }
        });

        // Delete question with long click
        holder.name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are you sure you want to delete question")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                db.deleteQuestion(arraylist.get(position).getId(),quizId);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE);
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                    }
                });
                alert.show();
                return false;
            }
        });

        return view;
    }

    // Shuffle mode
    public void startRandomQuiz(){
        // Shuffle the arrayList
        Collections.shuffle(arraylist);
        // Hand it over to a newly created dialog box

        dialogBox = new AnswerQuestionDialogBox(arraylist.get(0),arraylist,0,mContext, thisQuiz);
        dialogBox.showDialog(activity);
    }



}