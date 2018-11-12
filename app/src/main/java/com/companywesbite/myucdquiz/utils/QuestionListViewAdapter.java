package com.companywesbite.myucdquiz.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.companywesbite.myucdquiz.R;
import com.companywesbite.myucdquiz.questionClasses.question;
import com.companywesbite.myucdquiz.questionClasses.quiz;

import java.util.ArrayList;
import java.util.List;

public class QuestionListViewAdapter extends ArrayAdapter<question> {

    int groupid;
    List<question> item_list;
    ArrayList<String> desc;
    Context context;
    quiz thisQuiz;

    public QuestionListViewAdapter (Context context, int vg, int id, List<question> item_list, quiz newQuiz){
        super(context, vg, id, item_list);
        this.context=context;
        groupid=vg;
        this.item_list=item_list;
        this.thisQuiz = newQuiz;
    }

    // Hold views of the ListView to improve its scrolling performance
    static class ViewHolder {
        public TextView questionNameView;
        public Button deleteButton;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {

        View rowView = convertView;
        // Inflate the list_item.xml file if convertView is null
        if(rowView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView= inflater.inflate(groupid, parent, false);
            QuestionListViewAdapter.ViewHolder viewHolder = new QuestionListViewAdapter.ViewHolder();
            viewHolder.questionNameView = (TextView) rowView.findViewById(R.id.questionTitle);
            viewHolder.deleteButton = (Button) rowView.findViewById(R.id.deletQuestion);
            rowView.setTag(viewHolder);
        }
        // Set text to each TextView of ListView item
        QuestionListViewAdapter.ViewHolder holder = (QuestionListViewAdapter.ViewHolder) rowView.getTag();
        holder.questionNameView.setText(item_list.get(position).getTitle());
        // for now we just this as the image but later we will display the image the user chose

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(getContext());
                db.deleteQuestion(item_list.get(position).getId(), thisQuiz.getId());
                removeListItem(position);
                thisQuiz.numberOfQuestions--;
                db.updateQuiz(thisQuiz);
                Toast.makeText(getContext(),"Question Deleted",Toast.LENGTH_LONG).show();
            }
        });


        /***
         *
         * Clicking a specific question
         *
         */

        holder.questionNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open the note.
                ((ListView) parent).performItemClick(v, position, 0);
            }
        });


        return rowView;
    }


    // Method for remove an item of ListView inside adapter class
    // you need to pass as an argument the tag you added to the layout of your choice
    private void removeListItem(int position) {
        // lv and the adapter must be public-static in their Activity Class
        this.item_list.remove(position);
        this.notifyDataSetChanged();
    }


}
