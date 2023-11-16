package com.uog.soemhike;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uog.soemhike.activity.DatabaseListActivity;
import com.uog.soemhike.database.DatabaseHelper;
import com.uog.soemhike.database.Observation;
import com.uog.soemhike.databinding.ActivityAddObservationBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddObservationActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 1;
    ActivityAddObservationBinding mainBinding;
    ActivityResultLauncher<Uri> takePictureLauncher;
    Uri imageUri;


//

    EditText txt_title;
    EditText txt_year;
    Button btn_SaveQualification;
    DatabaseHelper databaseHelper;

    private ImageView avatarImageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    public String imagePath;
//    public String avatarFilePath;
    Button btnTakePicture;

    int hike_Id = 0;



//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityAddObservationBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        imageUri = createUri();
        registerPictureLauncher();

        mainBinding.btnTakePicture.setOnClickListener(view -> {
            checkCameraPermissionAndOpenCamera();

        });


//        setContentView(R.layout.activity_add_observation);

        txt_title = findViewById(R.id.txt_Title);
        txt_year = findViewById(R.id.txt_Year);

        btn_SaveQualification = findViewById(R.id.btn_SaveQualification);

        databaseHelper = new DatabaseHelper(this);

//        pic
//        avatarImageView = findViewById(R.id.avatarImageView);

//        pic

        Bundle bundle = getIntent().getExtras();
        hike_Id = bundle.getInt(Observation.O_HIKEID);

        Log.i("key111", String.valueOf(hike_Id));
        btn_SaveQualification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveQualification();
            }
        });





    }
    private Uri createUri(){

        File imageFile = new File(getApplicationContext().getFilesDir(), "camera_photo.jpg");

        return FileProvider.getUriForFile(
                getApplicationContext(),
                "com.uog.soemhike.fileProvider",
                imageFile
        );
    }



//    SAVE INTO LOCAL START


    private void saveImageToLocalStorage(Uri selectedImageUri) {
        try {

            // Generate a unique file name based on the current timestamp
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "IMG_" + timeStamp + ".jpg";

            Log.i("saved",imageFileName );


            // Create a file with the generated name
            File imageFile = new File(getFilesDir(), imageFileName);

            // Open an input stream from the selected image URI
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);

            // Open an output stream to the file
            OutputStream outputStream = new FileOutputStream(imageFile);

            this.imagePath = String.valueOf(imageFile);

            // Copy the image data from the input stream to the output stream
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Close the streams
            inputStream.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    SAVE INTO LOCAL END

    private void registerPictureLauncher(){
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        try {
                            if (result){
                                mainBinding.ivUser.setImageURI(null);
                                mainBinding.ivUser.setImageURI(imageUri);

                                // Get the new image name
                                String imageName = generateImageName();
                                Log.i("image", imageName);
                                // Now imageName contains the new unique image name
                                // You can use this name as needed (e.g., save to database)

                                // Save the selected image to local storage
                                saveImageToLocalStorage(imageUri);

                                // To get the full path including the directory, you can use:
                                String imagePath = new File(getApplicationContext().getFilesDir(), imageName).getAbsolutePath();
                                Log.i("imagePath", imagePath);

                            }
                        } catch (Exception exception){
                            exception.printStackTrace();
                        }
                    }
                }
        );
    }

    // Add this method to generate a unique image name
    private String generateImageName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        return "IMG_" + timeStamp + ".jpg";
    }

    private void checkCameraPermissionAndOpenCamera(){
        if (ActivityCompat.checkSelfPermission(AddObservationActivity.this,
                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddObservationActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }else {
            takePictureLauncher.launch(imageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                takePictureLauncher.launch(imageUri);
            }else{
                Toast.makeText(this,"Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }





    //    Save
    private void saveQualification() {
        String qTitle = txt_title.getText().toString();
        String qYear = txt_year.getText().toString();
//        imagePath;


        Observation observation = new Observation(qTitle,qYear , imagePath, hike_Id);

        long qid = databaseHelper.addObservation(observation);

        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Data Saved")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked OK button, navigate to the new activity
                        Intent intent = new Intent(getBaseContext(), ObservationListActivity.class);
                        intent.putExtra(Observation.O_HIKEID, hike_Id);
                        startActivity(intent);
                    }
                })
                .show();
    }

}