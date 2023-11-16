package com.uog.soemhike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.uog.soemhike.activity.DatabaseListActivity;
import com.uog.soemhike.adpater.HikeAdapter;
import com.uog.soemhike.adpater.ObservationAdapter;
import com.uog.soemhike.database.DatabaseHelper;
import com.uog.soemhike.database.Hike;
import com.uog.soemhike.database.Observation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ObservationListActivity extends AppCompatActivity {


    private ObservationAdapter observationAdapter;
    private List<Observation> observationList=new ArrayList<>();
    private DatabaseHelper databaseHelper;
    RecyclerView rec_ObservationList;
    TextView txt_NoRecord;
    Button btn_AddNewObservation, btn_Back;
    public int hike_Id;

    TextView lbl_diff,lbl_Id, lbl_Name, lbl_Location, lbl_Date, lbl_Parking, lbl_Length, lbl_difficulty, lbl_Description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_list);

        txt_NoRecord = findViewById(R.id.txt_NoRecord);
        databaseHelper = new DatabaseHelper(this);
        rec_ObservationList = findViewById(R.id.rec_ObservationList);
        btn_AddNewObservation = findViewById(R.id.btn_AddNewObservation);
        btn_Back = findViewById(R.id.btn_Back);

        Bundle bundle = getIntent().getExtras();
        hike_Id = bundle.getInt(Observation.O_HIKEID);
        String Name = bundle.getString(Hike.NAME);
        String LOCATION = bundle.getString(Hike.LOCATION);
        String DATE = bundle.getString(Hike.DATE);
        String PARKING = bundle.getString(Hike.PARKING);
        Double LENGTH = bundle.getDouble(Hike.LENGTH);
        String DIFFICULTY = bundle.getString(Hike.DIFFICULTY);
        String DESCRIPTION = bundle.getString(Hike.DESCRIPTION);

        Log.i("test123", Name);


//       New

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getBaseContext(), DatabaseListActivity.class);
                startActivity(intent);
            }
        });

        btn_AddNewObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddObservationActivity.class);
                intent.putExtra(Observation.O_HIKEID, hike_Id);
                startActivity(intent);

            }
        });

        //

        lbl_Id = findViewById(R.id.lbl_Id1);
        lbl_diff = findViewById(R.id.lbl_diff1);
        lbl_Name = findViewById(R.id.lbl_Name1);
        lbl_Location = findViewById(R.id.lbl_Location1);
        lbl_Date = findViewById(R.id.lbl_Date1);
        lbl_Parking = findViewById(R.id.lbl_Parking1);
        lbl_Length = findViewById(R.id.lbl_Length1);
        lbl_difficulty = findViewById(R.id.lbl_difficulty1);
        lbl_Description = findViewById(R.id.lbl_Description1);

        lbl_Name.setText(Name);
        lbl_Location.setText(LOCATION);
        lbl_Date.setText(DATE);
        lbl_Parking.setText(PARKING);
        lbl_Length.setText(String.valueOf(LENGTH));
        lbl_difficulty.setText(DIFFICULTY);
        lbl_Description.setText(DESCRIPTION);



//

        try {
            List<Observation> o_List = databaseHelper.searchObservation(String.valueOf(hike_Id));

//            rSetting
            if (o_List.size()!=0){
                txt_NoRecord.setVisibility(View.GONE);
                rec_ObservationList.setLayoutManager(new LinearLayoutManager(this));
                rec_ObservationList.setHasFixedSize(true);
                rec_ObservationList.setAdapter(new ObservationAdapter(this,o_List));

            }else{
                txt_NoRecord.setVisibility(View.VISIBLE);
                rec_ObservationList.setVisibility(View.GONE);
                txt_NoRecord.setText("No Record Found");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}