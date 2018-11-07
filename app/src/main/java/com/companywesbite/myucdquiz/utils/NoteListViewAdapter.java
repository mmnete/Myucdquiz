package com.companywesbite.myucdquiz.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.companywesbite.myucdquiz.R;
import com.companywesbite.myucdquiz.questionClasses.note;

import java.util.ArrayList;
import java.util.List;

public class NoteListViewAdapter extends ArrayAdapter<note> {

        int groupid;
        List<note> item_list;
        ArrayList<String> desc;
        Context context;

    public NoteListViewAdapter (Context context, int vg, int id, List<note> item_list){
        super(context, vg, id, item_list);
        this.context=context;
        groupid=vg;
        this.item_list=item_list;
        }

    // Hold views of the ListView to improve its scrolling performance
    static class ViewHolder {
        public TextView noteNameView;
        public Button deleteButton;
        public Button viewButton;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {

        View rowView = convertView;
        // Inflate the list_item.xml file if convertView is null
        if(rowView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView= inflater.inflate(groupid, parent, false);
            NoteListViewAdapter.ViewHolder viewHolder = new NoteListViewAdapter.ViewHolder();
            viewHolder.noteNameView = (TextView) rowView.findViewById(R.id.noteName);
            viewHolder.deleteButton = (Button) rowView.findViewById(R.id.deletNote);
            viewHolder.viewButton = (Button) rowView.findViewById(R.id.readNote);
            rowView.setTag(viewHolder);
        }
        // Set text to each TextView of ListView item
        NoteListViewAdapter.ViewHolder holder = (NoteListViewAdapter.ViewHolder) rowView.getTag();
        holder.noteNameView.setText(item_list.get(position).getTitle());
        // for now we just this as the image but later we will display the image the user chose

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(getContext());
                db.deleteNote(item_list.get(position).getId());
                removeView(position);
                Toast.makeText(getContext(),"Notes Deleted",Toast.LENGTH_LONG).show();
            }
        });



        return rowView;
    }


    // Method for remove an item of ListView inside adapter class
    // you need to pass as an argument the tag you added to the layout of your choice
    public void removeView(int position) {
        // lv and the adapter must be public-static in their Activity Class
        this.removeView(position);
        this.notifyDataSetChanged();
    }

}
