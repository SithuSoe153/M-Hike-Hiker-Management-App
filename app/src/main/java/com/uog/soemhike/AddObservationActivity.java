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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uog.soemhike.activity.DatabaseListActivity;
import com.uog.soemhike.database.DatabaseHelper;
import com.uog.soemhike.database.Hike;
import com.uog.soemhike.database.Observation;
import com.uog.soemhike.databinding.ActivityAddObservationBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddObservationActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 1;
    ActivityAddObservationBinding mainBinding;
    ActivityResultLauncher<Uri> takePictureLauncher;
    Uri imageUri;


//

    EditText txt_title;
    EditText txt_year, txt_CurrentTime;
    Button btn_SaveQualification;
    DatabaseHelper databaseHelper;

    private ImageView avatarImageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    public String imagePath;
//    public String avatarFilePath;
    Button btnTakePicture;

    int hike_Id = 0;
    String hike_Name,hike_Location,hike_Date, hike_Parking, hike_Length, hike_Weather,hike_Difficulty,hike_Description;



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
        txt_CurrentTime = findViewById(R.id.txt_CurrentTime);
        txt_CurrentTime.setText(getCurrentTime());

        btn_SaveQualification = findViewById(R.id.btn_SaveQualification);

        databaseHelper = new DatabaseHelper(this);

//        pic
//        avatarImageView = findViewById(R.id.avatarImageView);

//        pic

        Bundle bundle = getIntent().getExtras();
        hike_Id = bundle.getInt(Observation.O_HIKEID);
        hike_Name = bundle.getString(Hike.NAME);
        hike_Location = bundle.getString(Hike.LOCATION);
        hike_Date = bundle.getString(Hike.DATE);
        hike_Parking = bundle.getString(Hike.PARKING);
        hike_Length = bundle.getString(Hike.LENGTH);
        hike_Weather = bundle.getString(Hike.WEATHER);
        hike_Difficulty = bundle.getString(Hike.DIFFICULTY);
        hike_Description = bundle.getString(Hike.DESCRIPTION);

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



    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    //    Save
    private void saveQualification() {
        String qTitle = txt_title.getText().toString();
        String qYear = txt_year.getText().toString();
        String currentTime = getCurrentTime();
        Log.i("time", currentTime);
//        imagePath;


        Observation observation = new Observation(qTitle,qYear, currentTime, imagePath, hike_Id);

        long qid = databaseHelper.addObservation(observation);

        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Data Saved")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked OK button, navigate to the new activity
                        Intent intent = new Intent(getBaseContext(), ObservationListActivity.class);
                        intent.putExtra(Observation.O_HIKEID, hike_Id);
                        intent.putExtra(Hike.NAME, hike_Name);
                        intent.putExtra(Hike.LOCATION, hike_Location);
                        intent.putExtra(Hike.DATE, hike_Date);
                        intent.putExtra(Hike.PARKING, hike_Parking);
                        intent.putExtra(Hike.LENGTH, hike_Length);
                        intent.putExtra(Hike.WEATHER, hike_Weather);
                        intent.putExtra(Hike.DIFFICULTY, hike_Difficulty);
                        intent.putExtra(Hike.DESCRIPTION, hike_Description);
                        startActivity(intent);
                    }
                })
                .show();
    }

}