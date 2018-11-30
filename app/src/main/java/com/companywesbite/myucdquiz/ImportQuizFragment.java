package com.companywesbite.myucdquiz;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import android.widget.TextView;

import com.companywesbite.myucdquiz.questionClasses.question;
import com.companywesbite.myucdquiz.questionClasses.quiz;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ImportQuizFragment extends Fragment {
    private static final String TAG = "ImportQuizFragment";

    private FloatingActionButton importQuizButton;
    private TextView importStatus;

    private static final int PICKFILE_RESULT_CODE = 1000;
    private static final int MY_PERMISSIONS_REQUEST_GET_IMAGE = 2000;



    Context context;
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.activity_snap_notes_fragment,container,false);


        context=getContext();


        // creating a new quiz.
        importQuizButton = (FloatingActionButton) view.findViewById(R.id.importQuizButton);
        importStatus = (TextView) view.findViewById(R.id.importStatus);
        importQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importQuiz();
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void importQuiz()
    {
         // just say that the quiz has been imported..
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
        } else
        {
            // we are good to go
            chooseFile();
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
                    chooseFile();
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



    private void chooseFile()
    {
        String[] mimeTypes =
                {"text/plain"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent,"ChooseFile"), PICKFILE_RESULT_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == PICKFILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i(TAG, "Uri: " + uri.toString());
                importData(uri);
            }
        }
    }

    private void importData(Uri data) {
        final String scheme = data.getScheme();

        if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            try {
                ContentResolver cr = getActivity().getContentResolver();
                InputStream is = cr.openInputStream(data);
                if (is == null) return;

                StringBuffer buf = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String str;
                if (is != null) {
                    while ((str = reader.readLine()) != null) {
                        buf.append(str + "\n");
                    }
                }
                is.close();


                String jsonString = buf.toString();
                JSONObject jsonObject = new JSONObject(jsonString);
                // perform your data import here…
                quiz newQuiz = new quiz(jsonString);

                // time to save the questions
                if(!newQuiz.isNull)
                {
                    DatabaseHelper db = new DatabaseHelper(getActivity().getApplicationContext());
                    newQuiz.setId(db.createQuiz(newQuiz));
                    JSONArray questionJSON = jsonObject.getJSONArray("questions");
                    for (int i = 0; i < questionJSON.length(); i++) {
                        JSONObject obj = questionJSON.getJSONObject(i);
                        Log.d("TAG",Long.toString(newQuiz.getId()));
                        String title = obj.getString("title");
                        String description = obj.getString("description");
                        String answer = obj.getString("answer");
                        question temp = new question(title,description,answer,"default");
                        temp.setId(db.createQuestion(temp, newQuiz.getId()));
                        newQuiz = db.getQuiz(newQuiz.getId());
                        newQuiz.addQuestion(temp);
                        db.updateQuiz(newQuiz);
                    }
                    importStatus.setText("Quiz received.");
                } else
                {
                    importStatus.setText("Error. Couldn't load file.");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }




}
