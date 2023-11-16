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
    Button btn_UpdateQualification;
    DatabaseHelper databaseHelper;
    ImageView ivUserUpdate;
    int o_id;
    int o_hid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_observation);

        txt_Title = findViewById(R.id.txt_Title);
        txt_Year = findViewById(R.id.txt_Year);
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
        o_id = bundle.getInt(Observation.O_ID);
        o_hid = bundle.getInt(Observation.O_HIKEID);

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
        startActivity(intent);

    }
}