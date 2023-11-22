package com.uog.soemhike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.uog.soemhike.database.DatabaseHelper;
import com.uog.soemhike.database.Hike;
import com.uog.soemhike.database.Observation;

public class UpdateObservationActivity extends AppCompatActivity {

    EditText txt_Title, txt_Year;
    TextView txt_Date;

    Button btn_UpdateQualification;
    DatabaseHelper databaseHelper;
    ImageView ivUserUpdate;
    int o_id;
    int o_hid;

    String hike_Name,hike_Location,hike_Date, hike_Parking, hike_Length, hike_Weather,hike_Difficulty,hike_Description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_observation);

        txt_Title = findViewById(R.id.txt_Title);
        txt_Year = findViewById(R.id.txt_Year);
        txt_Date = findViewById(R.id.txt_Date);
        btn_UpdateQualification = findViewById(R.id.btn_UpdateQualification);
        databaseHelper = new DatabaseHelper(this);
        ivUserUpdate = findViewById(R.id.ivUserUpdate);

        Bundle bundle = getIntent().getExtras();

        String avatarFilePath = bundle.getString(Observation.AVATAR_FILE_PATH);

        // Load and display avatar image if available
        if (avatarFilePath != null) {

            // Handle the image file path, for example, set it to an ImageView
            Bitmap bitmap = BitmapFactory.decodeFile(avatarFilePath);
            if (bitmap != null) {
                ivUserUpdate.setImageBitmap(bitmap);
            } else {
                // Handle the case where the image couldn't be loaded
                ivUserUpdate.setImageResource(R.drawable.default_avatar); // Set a default image
            }

        }

        txt_Title.setText(bundle.getString(Observation.O_TITLE));
        txt_Year.setText(bundle.getString(Observation.O_YEAR));
        txt_Date.setText(bundle.getString(Observation.O_CURRENT_TIME));
//        Log.i("bb23", bundle.getString(Observation.O_CURRENT_TIME));
        o_id = bundle.getInt(Observation.O_ID);
        o_hid = bundle.getInt(Observation.O_HIKEID);

        hike_Name = bundle.getString(Hike.NAME);
        hike_Location = bundle.getString(Hike.LOCATION);
        hike_Date = bundle.getString(Hike.DATE);
        hike_Parking = bundle.getString(Hike.PARKING);
        hike_Length = bundle.getString(Hike.LENGTH);
        hike_Weather = bundle.getString(Hike.WEATHER);
        hike_Difficulty = bundle.getString(Hike.DIFFICULTY);
        hike_Description = bundle.getString(Hike.DESCRIPTION);

//        Log.i("bb123", hike_Name);

        btn_UpdateQualification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i("oid" , String.valueOf(o_id));
                UpdateObservation();
            }
        });

    }

    private void UpdateObservation(){
        String o_title = txt_Title.getText().toString();
        String o_year = txt_Year.getText().toString();
        String o_image = String.valueOf(R.drawable.ic_launcher_background);

        Observation observation = new Observation(o_id, o_title, o_year, o_image);
        Log.i("FK", String.valueOf(o_hid));

//        databaseHelper = new DatabaseHelper(this);
        databaseHelper.update_Observation(observation);

        Intent intent = new Intent(UpdateObservationActivity.this, ObservationListActivity.class);
        intent.putExtra(Observation.O_HIKEID, o_hid);

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
}