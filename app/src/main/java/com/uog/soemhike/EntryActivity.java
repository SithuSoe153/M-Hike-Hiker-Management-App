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

import com.uog.soemhike.activity.DatabaseListActivity;
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

    //Spin Group
    private Spinner spinnerHikeNames;
    private Spinner spinnerLocations;
//Spin Group

    private Button btn_ShowDateTime, btn_Next, btn_Back;

    String name, location, difficulty, errorRequired;
    private Integer id;
    private Integer nameIndex = 0;
    private Integer difficultyIndex = 0;
//    public static final String[] spn_Location_data = {"Select a location","M1", "M2", "M3"};

    // Define arrays for hike names and corresponding locations
    private String[] hikeNames = {"Select a Hike Name", "Ben Nevis", "Snowdon", "Lake District National Park", "The Giant's Causeway", "Isle of Skye"};

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

//        txt_NameOfHike = findViewById(R.id.txt_NameOfHike);
        txt_LengthOfHike = findViewById(R.id.txt_LengthOfHike);
        txt_Description = findViewById(R.id.txt_Description);

        rdo_Yes = findViewById(R.id.rdo_Yes);
        rdo_No = findViewById(R.id.rdo_No);

        btn_ShowDateTime = findViewById(R.id.btnShowDateTime);
        btn_Next = findViewById(R.id.btn_Next);
        btn_Back = findViewById(R.id.btn_Back);

//        spn_Location = (Spinner) findViewById(R.id.spn_Location);
        spn_Difficulty = (Spinner) findViewById(R.id.spn_Difficulty);

//        spin group

        // Assuming you have defined your spinners in the layout XML file
        spinnerHikeNames = findViewById(R.id.spinnerHikeNames);
        spinnerLocations = findViewById(R.id.spinnerLocations);


        // Create ArrayAdapter for hike names
        ArrayAdapter<String> hikeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hikeNames);
        hikeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set ArrayAdapter for hike names to Spinner A
        spinnerHikeNames.setAdapter(hikeAdapter);

        // Set listener for Spinner A to update options in Spinner B
        spinnerHikeNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected hike name
                String selectedHike = (String) parentView.getSelectedItem();
                name = hikeNames[position];
                Log.i("item", name);


                // Find the corresponding location based on the selected hike
                String correspondingLocation = getCorrespondingLocation(selectedHike);

                // Update options in Spinner B
                updateLocationSpinner(correspondingLocation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

//        spin group


        errorRequired = "Required field";

//        ArrayAdapter<String> ad_location = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spn_Location_data);
//        ad_location.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spn_Location.setAdapter(ad_location);
//        spn_Location.setSelection(locationIndex);
//        spn_Location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////                location = spn_Location_data[i];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });


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

//                if (id != null){
//                    Intent intent = new Intent(getBaseContext(), DatabaseListActivity.class);
//                    startActivity(intent);
//                }else {
//                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
//                    startActivity(intent);
//                }

                Intent intent = new Intent(getBaseContext(), DatabaseListActivity.class);
                startActivity(intent);

                Log.i("test", id+"This is idid");


            }
        });
        recievedData();
    }


    private void updateLocationSpinner(String location) {
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{location});
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set ArrayAdapter for locations to Spinner B
        spinnerLocations.setAdapter(locationAdapter);
    }


    private String getCorrespondingLocation(String selectedHike) {
        // Implement your logic to map hike names to locations
        // For simplicity, using a hardcoded mapping in this example
        switch (selectedHike) {
            case "Select a Hike Name":
                location = "";
                return "Select Hike Name first";
            case "Ben Nevis":
                location = "Scotland";
                return "Scotland";
            case "Snowdon":
                location = "Scotland";
                return "Wales";
            case "Lake District National Park":
                location = "Scotland";
                return "England";
            case "The Giant's Causeway":
                location = "Scotland";
                return "Northern Ireland";
            case "Isle of Skye":
                location = "Scotland";
                return "Scotland";
            default:
                return "";
        }
    }

//    Functions

    private void recievedData(){
        Bundle bundle = getIntent().getExtras();
        if (bundle !=null){
            id=bundle.getInt(Hike.ID);
//            txt_NameOfHike.setText(bundle.getString(Hike.NAME));


//            spn_Location.setText(bundle.getString(Hike.ADDRESS));


            name = bundle.getString(Hike.NAME);
            for (int i=0; i< hikeNames.length; i++){
                if (name.equals(hikeNames[i])){
                    nameIndex = i;
                    spinnerHikeNames.setSelection(nameIndex);
                    break;
                }
            }

            difficulty = bundle.getString(Hike.DIFFICULTY);
            for (int i=0; i< spn_Difficulty_data.length; i++){
                if (    difficulty.equals(spn_Difficulty_data[i])){
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

    public void setDate(LocalDate date){
        lbl_Date.setText(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

    }

    private void goToNext(){
//        String name = txt_NameOfHike.getText().toString();
        String date = lbl_Date.getText().toString();
        String parking = rdo_Yes.isChecked()?"Yes" : "No";
        String length = txt_LengthOfHike.getText().toString();
        String description = txt_Description.getText().toString(); //Optional
        if (description == null || description.isEmpty()) {
            description = "Default description";
        }

//        if(name==null || name.trim().isEmpty()){
//            new AlertDialog.Builder(this).setTitle(errorRequired).setMessage("Please Enter the Name of Hike").show();
//            txt_NameOfHike.requestFocus();
//
//            lbl_h1.setVisibility(View.VISIBLE);
//
//            return;
//        }else{
//            lbl_h1.setVisibility(View.INVISIBLE);
//
//        }

        if (name=="Select Hike Name first" || location.trim().isEmpty()) {
            new AlertDialog.Builder(this).setTitle(errorRequired).setMessage("Please Enter the Name of Hike").show();
            lbl_h1.setVisibility(View.VISIBLE);

            return;
        }else{
            lbl_h2.setVisibility(View.INVISIBLE);

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