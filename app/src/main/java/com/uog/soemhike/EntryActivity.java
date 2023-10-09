package com.uog.soemhike;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.uog.soemhike.database.DatabaseHelper;
import com.uog.soemhike.database.Hike;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EntryActivity extends AppCompatActivity {

    private TextView lbl_Date;
    private TextView lbl_h1, lbl_h2, lbl_h3, lbl_h4, lbl_h5, lbl_h6;
    private EditText txt_NameOfHike, txt_LengthOfHike, txt_Description;
    private RadioButton rdo_Yes, rdo_No;

    Spinner spn_Location, spn_Difficulty;

    private Button btn_ShowDateTime, btn_Next, btn_Back;

    String location, difficulty, errorRequired;
    private Integer id;
    private Integer locationIndex = 0;
    private Integer difficultyIndex = 0;
    private String[] spn_Location_data = {"Select a location","M1", "M2", "M3"};
    private String[] spn_Difficulty_data = {"Select a difficulty","H1", "H2", "H3"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        lbl_Date=findViewById(R.id.lbl_Date);

        lbl_h1 =findViewById(R.id.lbl_h1);
        lbl_h2 =findViewById(R.id.lbl_h2);
        lbl_h3 =findViewById(R.id.lbl_h3);
        lbl_h4 =findViewById(R.id.lbl_h4);
        lbl_h5 =findViewById(R.id.lbl_h5);
        lbl_h6 =findViewById(R.id.lbl_h6);

        txt_NameOfHike = findViewById(R.id.txt_NameOfHike);
        txt_LengthOfHike = findViewById(R.id.txt_LengthOfHike);
        txt_Description = findViewById(R.id.txt_Description);

        rdo_Yes = findViewById(R.id.rdo_Yes);
        rdo_No = findViewById(R.id.rdo_No);

        btn_ShowDateTime = findViewById(R.id.btnShowDateTime);
        btn_Next = findViewById(R.id.btn_Next);
        btn_Back = findViewById(R.id.btn_Back);

        spn_Location = (Spinner) findViewById(R.id.spn_Location);
        spn_Difficulty = (Spinner) findViewById(R.id.spn_Difficulty);

        errorRequired = "Required field";

        ArrayAdapter<String> ad_location = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spn_Location_data);
        ad_location.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_Location.setAdapter(ad_location);
        spn_Location.setSelection(locationIndex);
        spn_Location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                location = spn_Location_data[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spn_Difficulty_data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_Difficulty.setAdapter(adapter);
        spn_Difficulty.setSelection(difficultyIndex);
        spn_Difficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                difficulty = spn_Difficulty_data[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btn_ShowDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePick datePick = new DatePick();
                datePick.show(getSupportFragmentManager(),"datePicker");
            }
        });

        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNext();
            }
        });
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        Bundle bundle = getIntent().getExtras();
        if (bundle !=null){
            id=bundle.getInt(Hike.ID);
            txt_NameOfHike.setText(bundle.getString(Hike.NAME));
//            spn_Location.setText(bundle.getString(Hike.ADDRESS));
            location = bundle.getString(Hike.LOCATION);
            for (int i=0; i< spn_Location_data.length; i++){
                if (location.equals(spn_Location_data[i])){
                    locationIndex = i;
                    spn_Location.setSelection(locationIndex);
                    break;
                }
            }

            difficulty = bundle.getString(Hike.DIFFICULTY);
            for (int i=0; i< spn_Difficulty_data.length; i++){
                if (difficulty.equals(spn_Difficulty_data[i])){
                    difficultyIndex = i;
                    spn_Difficulty.setSelection(difficultyIndex);
                    break;
                }
            }

            lbl_Date.setText(bundle.getString(Hike.DATE));
//            rdo_Yes.setText(bundle.getInt(Hike.PARKING) + "");
//          Check the appropriate radio button based on the data
            if ((bundle.getString(Hike.PARKING)).equals("Yes")) {
                rdo_Yes.setChecked(true);
            } else {
                rdo_No.setChecked(true);
            }
//            double length = Double.parseDouble(bundle.getString(Hike.LENGTH));
            txt_LengthOfHike.setText(bundle.getDouble(Hike.LENGTH)+"");

//            Log.i("test", bundle.getString(Hike.LENGTH));

//            spn_Difficulty.setText(bundle.getString(Hike.PHONE));
            txt_Description.setText(bundle.getString(Hike.DESCRIPTION));

        }



    }

//    Functions

    public void setDate(LocalDate date){
        ZonedDateTime zdt = ZonedDateTime.now();

        lbl_Date.setText(zdt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm")));
    }

    private void goToNext(){
        String name = txt_NameOfHike.getText().toString();
        String date = lbl_Date.getText().toString();
        String parking = rdo_Yes.isChecked()?"Yes" : "No";
        String length = txt_LengthOfHike.getText().toString();
        String description = txt_Description.getText().toString(); //Optional
        if (description == null || description.isEmpty()) {
            description = "Default description";
        }

        if(name==null || name.trim().isEmpty()){
            new AlertDialog.Builder(this).setTitle(errorRequired).setMessage("Please Enter the Name of Hike").show();
            txt_NameOfHike.requestFocus();

            lbl_h1.setVisibility(View.VISIBLE);

            return;
        }else{
            lbl_h1.setVisibility(View.INVISIBLE);

        }

        if (location=="Select a location" || location.trim().isEmpty()) {
            new AlertDialog.Builder(this).setTitle(errorRequired).setMessage("Please Enter the Location of Hike").show();
            lbl_h2.setVisibility(View.VISIBLE);

            return;
        }else{
            lbl_h2.setVisibility(View.INVISIBLE);

        }

        if (date==null || date.trim().isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle(errorRequired)
                    .setMessage("Please Enter the Date of Hike")
                    .setPositiveButton("Select Date", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            DatePick datePick = new DatePick();
                            datePick.show(getSupportFragmentManager(),"datePicker");
                        }
                    })
                    .show();
            lbl_h3.setVisibility(View.VISIBLE);

            return;
        }else{
            lbl_h3.setVisibility(View.INVISIBLE);

        }

        if (!rdo_Yes.isChecked() && !rdo_No.isChecked()){
            new AlertDialog.Builder(this).setTitle(errorRequired).setMessage("Please Choose the Parking Available").show();
            lbl_h4.setVisibility(View.VISIBLE);

            return;
        }else{
            lbl_h4.setVisibility(View.INVISIBLE);

        }

        if (length==null || length.trim().isEmpty()) {
            new AlertDialog.Builder(this).setTitle(errorRequired).setMessage("Please Enter the Length of Hike").show();
            txt_LengthOfHike.requestFocus();
            lbl_h5.setVisibility(View.VISIBLE);

            return;
        }else{
            lbl_h5.setVisibility(View.INVISIBLE);

        }

        if (difficulty=="Select a difficulty" || difficulty.trim().isEmpty()) {
            new AlertDialog.Builder(this).setTitle(errorRequired).setMessage("Please Enter the Difficulty of Hike").show();
            lbl_h6.setVisibility(View.VISIBLE);

            return;
        }else{
            lbl_h6.setVisibility(View.INVISIBLE);

        }

        Log.i("test1",length);
        Log.i("test", id + " This is ID");

        Intent intent = new Intent(this, HikeDetailActivity.class);
        intent.putExtra(Hike.ID, id);
        intent.putExtra(Hike.NAME, name);
        intent.putExtra(Hike.LOCATION, location);
        intent.putExtra(Hike.DATE, date);
        intent.putExtra(Hike.PARKING, parking);
        intent.putExtra(Hike.LENGTH, length);
        intent.putExtra(Hike.DIFFICULTY, difficulty);
        intent.putExtra(Hike.DESCRIPTION, description);
        startActivity(intent);


    }



}