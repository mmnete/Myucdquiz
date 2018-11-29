package com.companywesbite.myucdquiz;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.widget.Toast;

import com.companywesbite.myucdquiz.questionClasses.question;
import com.companywesbite.myucdquiz.questionClasses.quiz;
import com.companywesbite.myucdquiz.utils.DatabaseHelper;

public class SnapQuestionActivity extends AppCompatActivity {


    /*

    ImageView imageView, imageView1;
    Button buttonCamera, buttonGallery,buttonCamera1, buttonGallery1;
    File file;
    Uri uri;
    Intent CamIntent, GalIntent, CropIntent ;

    */

    public  static final int RequestPermissionCode  = 1 ;
    DisplayMetrics displayMetrics ;
    int width, height;

    private static final String TAG = "SnapQuestionActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    int choice = 0;


    private String questionString = "";
    private String answerString = "";

    private long quizId;
    private quiz thisQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_question);


        // get the information about this quiz
        Intent i = getIntent();

        quizId = i.getLongExtra("quizId", 1000);

        // Log.d("TAG","Now "+Long.toString(quizId));

        // now let us get our quiz
        /*
        DatabaseHelper db = new DatabaseHelper(this);
        thisQuiz = db.getQuiz(quizId);

        */

        getSupportActionBar().setTitle("SNAP-N-GO");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        // Remove shadow from Action Bar
        this.getSupportActionBar().setElevation(0);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



       /*
        imageView = (ImageView)findViewById(R.id.imageview);
        buttonCamera = (Button)findViewById(R.id.button2);
        buttonGallery = (Button)findViewById(R.id.button1);

        imageView1 = (ImageView)findViewById(R.id.imageview1);
        buttonCamera1 = (Button)findViewById(R.id.button21);
        buttonGallery1 = (Button)findViewById(R.id.button11);

        EnableRuntimePermission();

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClickImageFromCamera(0) ;

            }
        });

        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetImageFromGallery(0);

            }
        });



        buttonCamera1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClickImageFromCamera(1) ;

            }
        });

        buttonGallery1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetImageFromGallery(1);

            }
        });

        */




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

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new SnapQuestionFragment(), "QUESTION");
        adapter.addFragment(new SnapAnswerFragment(), "ANSWER");
        viewPager.setAdapter(adapter);
    }


    /*
    public void ClickImageFromCamera(int val) {

        choice = val;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        CamIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        file = new File(Environment.getExternalStorageDirectory(),
                "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        uri = Uri.fromFile(file);

        CamIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);

        CamIntent.putExtra("return-data", true);

        startActivityForResult(CamIntent, 0);

    }

    public void GetImageFromGallery(int val){

        choice = val;

        GalIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);

    }


    public void ImageCropFunction() {

        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setDataAndType(uri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 180);
            CropIntent.putExtra("outputY", 180);
            CropIntent.putExtra("aspectX", 3);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);

        } catch (ActivityNotFoundException e) {

        }
    }
    //Image Crop Code End Here

    */

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(SnapQuestionActivity.this,
                Manifest.permission.CAMERA))
        {

            Toast.makeText(SnapQuestionActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(SnapQuestionActivity.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }


    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {

            ImageCropFunction();

        }
        else if (requestCode == 2) {

            if (data != null) {

                uri = data.getData();

                ImageCropFunction();

            }
        }
        else if (requestCode == 1) {

            if (data != null) {

                Bundle bundle = data.getExtras();

                Bitmap bitmap = bundle.getParcelable("data");

                if(choice == 0)
                {
                    imageView.setImageBitmap(bitmap);
                } else
                {
                    imageView1.setImageBitmap(bitmap);
                }


            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(SnapQuestionActivity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(SnapQuestionActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    */

    public void setAnswer(String answer) {
        this.answerString = answer;
    }

    public void setQuestion(String question) {
        this.questionString = question;
    }

    public String getQuestion() {
        return questionString;
    }

    public String getAnswer() {
        return answerString;
    }

    public void saveQuestion()
    {
        String questionValue = this.questionString;
        String answerValue = this.answerString;

        if(questionValue.length() < 1)
        {
            Toast.makeText(getApplicationContext(),"Question too short",Toast.LENGTH_LONG).show();
            return;
        }

        if(answerValue.length() < 1)
        {
            Toast.makeText(getApplicationContext(),"Answer too short",Toast.LENGTH_LONG).show();
            return;
        }

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        question questionObject = new question("SNAPPED",questionValue,answerValue,"default");
        db.createQuestion(questionObject,quizId);
        Toast.makeText(getApplicationContext(),"Question Created :-) ",Toast.LENGTH_LONG).show();
    }
}
