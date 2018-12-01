package com.companywesbite.myucdquiz.utilUI;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.companywesbite.myucdquiz.R;


/***
 *
 *
 * Team: Flashcards Pro
 * Date: 2018-12-01
 * Name: SnapPreviewDialogBox
 * Functionality: This is our own custom dialog box that pops up as an input when the user wants to view a
 *                preview of the question or answer that is obtained from an image taken by the camera
 *                through the text extraction OCR API in the snapquestionactivity
 *
 *
 *
 *
 */




public class SnapPreviewDialogBox {


    private String valueToDisplay = "";

    public SnapPreviewDialogBox(String value)
    {
        this.valueToDisplay = value;
    }

    public void showDialog(final Activity activity){

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.snap_preview_layout);


        final TextView snapValueDisplay = (TextView) dialog.findViewById(R.id.snapValue);
        final Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);


        snapValueDisplay.setText(valueToDisplay);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueToDisplay = snapValueDisplay.getText().toString();
                dialog.cancel();
            }
        });



        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }


    public String getValueToDisplay() {
        return valueToDisplay;
    }

    public void setValueToDisplay(String valueToDisplay) {
        this.valueToDisplay = valueToDisplay;
    }

}
