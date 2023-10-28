package com.uog.soemhike;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uog.soemhike.database.DatabaseHelper;
import com.uog.soemhike.database.Observation;

public class AddObservationActivity extends AppCompatActivity {

    EditText txt_title;
    Button btn_SaveQualification;
    DatabaseHelper databaseHelper;
    int hike_Id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);

        txt_title = findViewById(R.id.txt_Title);

        btn_SaveQualification = findViewById(R.id.btn_SaveQualification);

        databaseHelper = new DatabaseHelper(this);

        Bundle bundle = getIntent().getExtras();
        hike_Id = bundle.getInt("user_id");

        Log.i("key111", String.valueOf(hike_Id));
        btn_SaveQualification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveQualification();
            }
        });
    }

    private void saveQualification() {
        String qTitle = txt_title.getText().toString();
        String qYear = "2023";

        Observation observation = new Observation(qTitle,qYear , hike_Id);

        long qid = databaseHelper.addObservation(observation);

        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Data Saved")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked OK button, navigate to the new activity
                        Intent intent = new Intent(getBaseContext(), ObservationListActivity.class);
                        intent.putExtra("user_id", hike_Id);
                        startActivity(intent);
                    }
                })
                .show();
    }
}