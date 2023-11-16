package com.uog.soemhike.activity;

//import static com.uog.soemhike.EntryActivity.spn_Location_data;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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

import com.uog.soemhike.DatePick;
import com.uog.soemhike.R;
import com.uog.soemhike.database.Hike;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class HikeAdvanceSearchActivity extends AppCompatActivity {

    EditText txtAdvname,txtAdvLength;

    Spinner spnAdvLocation;

    TextView txtAdvdate;
    Button btnAdvdate, btnAdvSearch;

    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_advance_search);

        txtAdvname=findViewById(R.id.txt_AdvNameOfHike);
        txtAdvdate=findViewById(R.id.lbl_AdvDate);
        txtAdvLength=findViewById(R.id.txt_AdvLengthOfHike);
        spnAdvLocation=findViewById(R.id.spn_AdvLocation);
        btnAdvdate=findViewById(R.id.btnAdvShowDateTime);
        btnAdvSearch=findViewById(R.id.btn_AdvSearch);

//        ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spn_Location_data);
//        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnAdvLocation.setAdapter(adapterLocation);
//        spnAdvLocation.setSelection(0);
//        spnAdvLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                location=spn_Location_data[i];
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
        btnAdvdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePick datePick=new DatePick();
                datePick.show(getSupportFragmentManager(),"datePicker");
            }
        });

        btnAdvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=txtAdvname.getText().toString();
                String date=txtAdvdate.getText().toString();
                String length=txtAdvLength.getText().toString();

                Log.i("test",name);
                Log.i("test",date);
                Log.i("test",length);

                try {
                    if (name==null || name.isEmpty()){
                        new AlertDialog.Builder(getBaseContext()).setTitle("Error").setMessage("Please Enter the Name of Hike").show();
                        txtAdvname.requestFocus();
                        return;
                    }
                    if (location==null || location.isEmpty()) {
                        new AlertDialog.Builder(getBaseContext()).setTitle("Error").setMessage("Please Select the Location of Hike").show();
                        return;
                    }

                    if ( (date==null || date.isEmpty()) && (length==null || length.isEmpty()) ){
                        new AlertDialog.Builder(getBaseContext()).setTitle("Error").setMessage("Please Enter the Date or Length of Hike").show();
                        return;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }


                Intent intent=new Intent();
                intent.putExtra(Hike.NAME,name.trim());
                intent.putExtra(Hike.LOCATION,location);
                intent.putExtra(Hike.DATE,date);
                intent.putExtra(Hike.LENGTH,length);
                setResult(RESULT_OK,intent);
                finish();
            }
        });


    }
    public void setDate(LocalDate date){
//        ZonedDateTime zdt = ZonedDateTime.now();
        txtAdvdate.setText(date.toString());
//        txtAdvdate.setText(zdt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm")));
    }
}