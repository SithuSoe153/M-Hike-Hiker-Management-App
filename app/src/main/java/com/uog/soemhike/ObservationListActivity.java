package com.uog.soemhike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.uog.soemhike.activity.DatabaseListActivity;
import com.uog.soemhike.adpater.ObservationAdapter;
import com.uog.soemhike.database.DatabaseHelper;
import com.uog.soemhike.database.Hike;
import com.uog.soemhike.database.Observation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
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

    TextView lbl_diff,lbl_Id, lbl_Name, lbl_W1, lbl_W2, lbl_W3, lbl_W4, lbl_W5, lbl_Location, lbl_Date, lbl_Parking, lbl_Length, lbl_Weather, lbl_difficulty, lbl_Description;


    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appid = "1aecce68e2aa97be4985f8d8cdc072c5";
    DecimalFormat df = new DecimalFormat("#.##");
    private String LOCATION;


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
        LOCATION = bundle.getString(Hike.LOCATION);
        String DATE = bundle.getString(Hike.DATE);
        String PARKING = bundle.getString(Hike.PARKING);
        Double LENGTH = bundle.getDouble(Hike.LENGTH);
        String WEATHER = bundle.getString(Hike.WEATHER);
        String DIFFICULTY = bundle.getString(Hike.DIFFICULTY);
        String DESCRIPTION = bundle.getString(Hike.DESCRIPTION);


        Log.i("bb5", LOCATION);

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
                intent.putExtra(Hike.NAME, Name);
                intent.putExtra(Hike.LOCATION, LOCATION);
                intent.putExtra(Hike.DATE, DATE);
                intent.putExtra(Hike.PARKING, PARKING);
                intent.putExtra(Hike.LENGTH, LENGTH);
                intent.putExtra(Hike.WEATHER, WEATHER);
                intent.putExtra(Hike.DIFFICULTY, DIFFICULTY);
                intent.putExtra(Hike.DESCRIPTION, DESCRIPTION);

                startActivity(intent);

            }
        });

        //




//        lbl_Id = findViewById(R.id.lbl_Id1);
//        lbl_diff = findViewById(R.id.lbl_diff1);
        lbl_Name = findViewById(R.id.lbl_Name1);
        lbl_W1 = findViewById(R.id.lbl_W1);
        lbl_W2 = findViewById(R.id.lbl_W2);
        lbl_W3 = findViewById(R.id.lbl_W3);
        lbl_W4 = findViewById(R.id.lbl_W4);
        lbl_W5 = findViewById(R.id.lbl_W5);
        lbl_Location = findViewById(R.id.lbl_Location1);
        lbl_Date = findViewById(R.id.lbl_Date1);
        lbl_Parking = findViewById(R.id.lbl_Parking1);
        lbl_Length = findViewById(R.id.lbl_Length1);
        lbl_Weather = findViewById(R.id.lbl_Weather1);
        lbl_difficulty = findViewById(R.id.lbl_difficulty1);
        lbl_Description = findViewById(R.id.lbl_Description1);

        lbl_Name.setText(Name);
        lbl_Location.setText(LOCATION);
        lbl_Date.setText(DATE);
        lbl_Parking.setText(PARKING);
        lbl_Length.setText(String.valueOf(LENGTH));
        lbl_Weather.setText(WEATHER);
        lbl_difficulty.setText(DIFFICULTY);
        lbl_Description.setText(DESCRIPTION);
        getWeatherDetails();



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

//

    private void getWeatherDetails() {
        String tempUrl = url + "?q=" + LOCATION + "&appid=" + appid;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;

                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");

                    int humidity = jsonObjectMain.getInt("humidity");

                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    String wind = jsonObjectWind.getString("speed");

                    float pressure = jsonObjectMain.getInt("pressure");



                    // Set the temperature to lbl_W1
                    lbl_W1.setText(df.format(temp) + " °C");
                    lbl_W2.setText(humidity + " %");
                    lbl_W3.setText(description);
                    lbl_W4.setText(wind + " m/s");
                    lbl_W5.setText(pressure + " hPa");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Toast.makeText(getApplicationContext(), "Error fetching weather data", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


//
}
//