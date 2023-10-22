package com.uog.soemhike;

import androidx.appcompat.app.AppCompatActivity;

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

        Log.i("key1", String.valueOf(hike_Id));
        btn_SaveQualification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveQualification();
            }
        });
    }

    private void saveQualification() {
        String qTitle = txt_title.getText().toString();

        Observation observation = new Observation(qTitle, hike_Id);

        long qid = databaseHelper.addObservation(observation);

        Toast.makeText(this,"saved" + hike_Id, Toast.LENGTH_LONG).show();
    }
}