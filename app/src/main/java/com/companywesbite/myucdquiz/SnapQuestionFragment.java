package com.companywesbite.myucdquiz;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.companywesbite.myucdquiz.utilUI.SnapPreviewDialogBox;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;


/***
 *
 *
 *
 * Team: Flashcards Pro
 * Date: 12/09/2018
 * Name:  SnapQuestionFragment
 * Functionality: This fragment gets called when the user wants to take a picture of the
 *                answer and uses the OCR to fetch text. The save button saves the question and answer
 *                from the snapquestionactivity into the database.
 *
 *
 */



public class SnapQuestionFragment extends Fragment {

    private static final String TAG = "SnapNotesFragment";


    public  static final int RequestPermissionCode  = 1 ;

    public String questionValue = "";

    Context context;
    private View view;

    private ImageView imageView;
    private Button buttonCamera;
    private Button snatchButton, backButton;

    File file;
    Uri uri;
    Intent CamIntent, CropIntent ;

    Bitmap currImage;

    SnapPreviewDialogBox snapPreviewDialogBox;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_snap_question_fragment,container,false);


        context=getContext();

        imageView = (ImageView) view.findViewById(R.id.questionImage);
        buttonCamera = (Button)view.findViewById(R.id.snapQuestionButton);
        snatchButton = (Button) view.findViewById(R.id.snapQuestionPreviewButton);

        EnableRuntimePermission();

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClickImageFromCamera() ;

            }
        });

        snatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textFromImage();
            }
        });




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // update the view
    }


    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA))
        {

            Toast.makeText(getContext(),"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }


    public void ClickImageFromCamera() {

        snatchButton.setText("SNATCH TEXT");

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            /*
            ImageCropFunction();
            */
            try {
                Bitmap bitmap = handleSamplingAndRotationBitmap(getContext(),uri);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                Toast.makeText(getContext(),"Could not load image",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            if (data != null) {
                uri = data.getData();

            } else
            {
                Log.d("TAG","NULL");
            }
        }

        else if (requestCode == 2) {

            /*
            if (data != null) {

                uri = data.getData();

                ImageCropFunction();

            }
            */


            if (data != null) {
                uri = data.getData();
                imageView.setImageURI(uri);
                imageView.setVisibility(View.VISIBLE);
            }


        }
        else if (requestCode == 1) {

            if (data != null) {

                Bundle bundle = data.getExtras();



                Bitmap bitmap = bundle.getParcelable("data");


                currImage = bitmap;

                imageView.setImageBitmap(bitmap);



            }
        }
    }


    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(img, selectedImage);
        return img;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }


    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

        private void textFromImage ()
    {
        if(imageView.getDrawable() == null)
        {
            Toast.makeText(getContext(),"Please insert Question Image!",Toast.LENGTH_LONG).show();
            return;
        }

        // the user has inserted the image

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getContext()).build();

        if(!textRecognizer.isOperational())
        {
            Toast.makeText(getContext(),"Text Recognition not operational!",Toast.LENGTH_LONG).show();
            return;
        }


        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        try {
            bitmap = handleSamplingAndRotationBitmap(getContext(),uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

        SparseArray<TextBlock> items = textRecognizer.detect(frame);

        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < items.size(); i++)
        {
            TextBlock myItem = items.valueAt(i);
            sb.append(myItem.getValue());
            sb.append(" ");
        }

        // pull up the alert box
        questionValue = sb.toString();

        if(questionValue.length() > 1)
        {
            snatchButton.setText("PREVIEW");

            snapPreviewDialogBox = new SnapPreviewDialogBox(questionValue);
            snapPreviewDialogBox.showDialog(getActivity());

            ((SnapQuestionActivity)getActivity()).setQuestion(questionValue);


        } else
        {
            Toast.makeText(getContext(),"IMAGE HAS NO TEXT!",Toast.LENGTH_LONG).show();
        }

    }

}

