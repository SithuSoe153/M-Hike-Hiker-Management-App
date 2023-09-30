package com.uog.soemhike.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.uog.soemhike.EntryActivity;
import com.uog.soemhike.HikeDetailActivity;
import com.uog.soemhike.R;
import com.uog.soemhike.adpater.HikeAdapter;
import com.uog.soemhike.database.DatabaseHelper;
import com.uog.soemhike.database.Hike;

import java.util.ArrayList;
import java.util.List;

public class DatabaseListActivity extends AppCompatActivity {
    private List<Hike> hikeList = new ArrayList<>();

    private  DatabaseHelper databaseHelper;
    private HikeAdapter hikeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_list);

        databaseHelper = new DatabaseHelper(getBaseContext());

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        hikeAdapter = new HikeAdapter(hikeList);
        //
        hikeAdapter.setListener(new HikeAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v, long id) {
                if (id==R.id.btn_Remove){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                Hike hike = hikeList.get(position);
//                               remove
                                databaseHelper.delete(hike.getId());
//                               List
                                hikeList = databaseHelper.search("");
                                hikeAdapter.setHikesList(hikeList);
                                hikeAdapter.notifyDataSetChanged(); // refresh the data
                            }catch (Exception e){

                            }

                        }
                    });


                } else if (id == R.id.btn_Edit) {
                    Hike hike = hikeList.get(position);
                    Intent intent = new Intent(getBaseContext(), EntryActivity.class);
                    intent.putExtra(Hike.ID,hike.getId());
                    intent.putExtra(Hike.NAME,hike.getName());
                    intent.putExtra(Hike.LOCATION,hike.getLocation());
                    intent.putExtra(Hike.DATE,hike.getDate());
                    intent.putExtra(Hike.PARKING,hike.getParking());
                    intent.putExtra(Hike.LENGTH,hike.getLength());
                    intent.putExtra(Hike.DIFFICULTY,hike.getDifficulty());
                    intent.putExtra(Hike.DESCRIPTION,hike.getDescription());

                    startActivityForResult(intent,UPDATE_RESULT);

                }
            }
        });
        recyclerView.setAdapter(hikeAdapter);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {

                    hikeList = databaseHelper.search("");
                    hikeAdapter.setHikesList(hikeList);
                    hikeAdapter.notifyDataSetChanged(); // refresh the data

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });




    }

    public static final int UPDATE_RESULT = 123;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == UPDATE_RESULT && resultCode == RESULT_OK){

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {

                        hikeList = databaseHelper.search("");
                        hikeAdapter.setHikesList(hikeList);
                        hikeAdapter.notifyDataSetChanged(); // refresh the data

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
            });

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}