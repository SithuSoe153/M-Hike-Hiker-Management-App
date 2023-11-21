package com.uog.soemhike;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.uog.soemhike.activity.DatabaseListActivity;
import com.uog.soemhike.database.DatabaseHelper;
import com.uog.soemhike.database.Hike;

public class HikeDetailActivity extends AppCompatActivity {

    TextView lbl_Name, lbl_Location, lbl_Date, lbl_Parking, lbl_Length, lbl_Difficulty, lbl_Weather, lbl_Description;
    Button btn_Back,btn_Save;
    String name,location,date,parking,difficulty, weather, description;

    private DatabaseHelper databaseHelper;
    private Integer id;
    private double length;
    private Hike hike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_detail);

        Log.i("test", id + " This is new ID");

        lbl_Name = findViewById(R.id.txt1);
        lbl_Location = findViewById(R.id.txt2);
        lbl_Date = findViewById(R.id.txt3);
        lbl_Parking = findViewById(R.id.txt4);
        lbl_Length = findViewById(R.id.txt5);
        lbl_Difficulty = findViewById(R.id.txt6);
        lbl_Weather = findViewById(R.id.txt8);
        lbl_Description = findViewById(R.id.txt7);

        btn_Back = findViewById(R.id.btn_Back);
        btn_Save = findViewById(R.id.btn_Save);

        databaseHelper = new DatabaseHelper(getBaseContext());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            id=bundle.getInt(Hike.ID, 0);
            Log.i("test", id + " This is new ID");

            name = bundle.getString(Hike.NAME);
            location= bundle.getString(Hike.LOCATION);
            date = bundle.getString(Hike.DATE);
            parking = bundle.getString(Hike.PARKING);
            length = Double.parseDouble(bundle.getString(Hike.LENGTH));
            difficulty = bundle.getString(Hike.DIFFICULTY);
            weather = bundle.getString(Hike.WEATHER);
            description = bundle.getString(Hike.DESCRIPTION);

            lbl_Name.setText(name);
            lbl_Location.setText(location);
            lbl_Date.setText(date);
            lbl_Parking.setText(parking);
            lbl_Length.setText(length+"");
            lbl_Difficulty.setText(difficulty);
            lbl_Weather.setText(weather);
            lbl_Description.setText(description);

            hike = new Hike(
                   id,name,location,date,parking,length,difficulty, weather,description
            );

        }

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long result = 0;
                if (id == 0){
                    result = databaseHelper.saveHike(hike);
                    new AlertDialog.Builder(HikeDetailActivity.this)
                            .setTitle("Success")
                            .setMessage("Data Saved")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Add any actions you want to perform when the "OK" button is clicked
                                    Intent intent = new Intent(getBaseContext(), DatabaseListActivity.class);
                                    startActivity(intent);
                                    dialog.dismiss(); // Close the dialog if needed
                                }
                            })
                            .show();



                } else {
                    result =databaseHelper.updateHike(hike);
                    new AlertDialog.Builder(HikeDetailActivity.this)
                            .setTitle("Success")
                            .setMessage("Data Update")
                            .show();

                    Intent intent = new Intent(getBaseContext(), DatabaseListActivity.class);
                    startActivity(intent);

                }


//                if (result>0){
//
//                    new AlertDialog.Builder(HikeDetailActivity.this)
//                            .setTitle("Success")
//                            .setMessage("Data Saved")
//                            .show();
//
//                }else{
//                    new AlertDialog.Builder(HikeDetailActivity.this)
//                            .setTitle("Success")
//                            .setMessage("Data Saved")
//                            .show();
//
//                }

//                txt_Name.setText("");
//                txt_Address.setText("");
//                txt_Phone.setText("");
//                txt_Age.setText("");

            }
        });

    }
}