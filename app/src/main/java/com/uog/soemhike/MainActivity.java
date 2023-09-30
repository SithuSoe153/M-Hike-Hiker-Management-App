package com.uog.soemhike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.uog.soemhike.activity.DatabaseListActivity;
import com.uog.soemhike.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

Button btn_Entry,btn_Detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Entry = findViewById(R.id.btn_Entry);
        btn_Detail = findViewById(R.id.btn_Detail);

        btn_Entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), EntryActivity.class);
                startActivity(intent);

            }
        });

        btn_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), DatabaseListActivity.class);
                startActivity(intent);

//                DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
//                databaseHelper.deleteDatabase(getBaseContext());


            }
        });

    }
}