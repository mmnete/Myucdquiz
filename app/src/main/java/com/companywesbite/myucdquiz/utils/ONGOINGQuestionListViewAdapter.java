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
import android.widget.TextView;

import com.companywesbite.myucdquiz.R;
import com.companywesbite.myucdquiz.questionClasses.question;
import com.companywesbite.myucdquiz.utilUI.AnswerQuestionDialogBox;

import java.util.List;

public class ONGOINGQuestionListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<question> arraylist;
    Activity activity;
    private  AnswerQuestionDialogBox dialogBox;
    private long quizId;

    public ONGOINGQuestionListViewAdapter(Context context,Activity activity, List<question> arraylist, long quizId) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = arraylist;
        this.activity = activity;
        this.quizId = quizId;
    }

    public class ViewHolder {
        TextView name;

        TextView score;
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

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.ongoing_question_item, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.questionName);
            holder.score = (TextView) view.findViewById(R.id.questionScore);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(arraylist.get(position).getTitle());
        if (arraylist.get(position).getCorrect()==1)
        {
            holder.score.setText("Accepted");
        } else {
            holder.score.setText("Not Accepted");
        }

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dialogBox = new AnswerQuestionDialogBox(arraylist.get(position),arraylist,position,mContext);
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
                                DatabaseHelper db = new DatabaseHelper(activity);
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


}