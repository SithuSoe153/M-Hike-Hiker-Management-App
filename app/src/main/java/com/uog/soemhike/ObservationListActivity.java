package com.uog.soemhike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.List;

public class ObservationListActivity extends AppCompatActivity {


    private ObservationAdapter observationAdapter;
    private List<Observation> observationList=new ArrayList<>();
    private DatabaseHelper databaseHelper;
    RecyclerView rec_ObservationList;
    TextView txt_NoRecord;
    Button btn_AddNewObservation;
    public int hike_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_list);

        txt_NoRecord = findViewById(R.id.txt_NoRecord);
        databaseHelper = new DatabaseHelper(this);
        rec_ObservationList = findViewById(R.id.rec_ObservationList);
        btn_AddNewObservation = findViewById(R.id.btn_AddNewObservation);

        Bundle bundle = getIntent().getExtras();
        hike_Id = bundle.getInt("user_id");

//       New


        btn_AddNewObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddObservationActivity.class);
                intent.putExtra("user_id", hike_Id);
                startActivity(intent);

            }
        });

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