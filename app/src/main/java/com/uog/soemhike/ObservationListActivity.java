package com.uog.soemhike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.uog.soemhike.adpater.ObservationAdapter;
import com.uog.soemhike.database.DatabaseHelper;
import com.uog.soemhike.database.Observation;

import java.util.ArrayList;
import java.util.List;

public class ObservationListActivity extends AppCompatActivity {

    TextView txt_NoRecord;
    DatabaseHelper databaseHelper;
    RecyclerView rec_ObservationList;
    public String hike_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_list);

        txt_NoRecord = findViewById(R.id.txt_NoRecord);
        databaseHelper = new DatabaseHelper(this);
        rec_ObservationList = findViewById(R.id.rec_ObservationList);

        Bundle bundle = getIntent().getExtras();
        hike_Id = String.valueOf(bundle.getInt("user_id"));

        Log.i("key1", hike_Id);

//        txt_Otitle.setText(hike_Id);


        try {
            List<Observation> o_List = databaseHelper.searchObservation(hike_Id);

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


//        List<Observation> o_arrayList = null;
//        try {
//            o_arrayList = databaseHelper.searchObservation(hike_Id);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        if (o_arrayList.size()==0) txt_Otitle.setText("No record");
//            else {
//                txt_Otitle.setText("Qlsit\n");
//                for (int i = 0;i < o_arrayList.size();i++){
////                    txt_Otitle.append((CharSequence) o_arrayList.get(i)+"\n");
//                    txt_Otitle.append(o_arrayList.get(i).getTitle() + "\n");
//
//                }
//
//            }


    }
}