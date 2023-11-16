
package com.uog.soemhike.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uog.soemhike.AddObservationActivity;
import com.uog.soemhike.DateUtils;
import com.uog.soemhike.EntryActivity;
import com.uog.soemhike.HikeDetailActivity;
import com.uog.soemhike.MainActivity;
import com.uog.soemhike.ObservationListActivity;
import com.uog.soemhike.R;
import com.uog.soemhike.adpater.HikeAdapter;
import com.uog.soemhike.adpater.ObservationAdapter;
import com.uog.soemhike.database.DatabaseHelper;
import com.uog.soemhike.database.Hike;
import com.uog.soemhike.database.Observation;

import java.util.ArrayList;
import java.util.List;

public class DatabaseListActivity extends AppCompatActivity {

    private HikeAdapter hikeAdapter;
    private List<Hike> hikeList=new ArrayList<>();
    private DatabaseHelper databaseHelper;

    private RecyclerView recyclerView;
    EditText txtsearch;
    FloatingActionButton fab_btn, fab_btn1;

    TextView tv_press;


    public static final int UPDATE_REQUEST=1;
    public static final int SEARCH_REQUEST=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_list);

        fab_btn = findViewById(R.id.fab_btn);
        fab_btn1 = findViewById(R.id.fab_btn1);

        recyclerView=findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseHelper=new DatabaseHelper(getBaseContext());
        hikeAdapter=new HikeAdapter(hikeList);
        tv_press = findViewById(R.id.tv_press);




        try {
            hikeList = databaseHelper.search("");

            if (hikeList.size()!=0){
                tv_press.setVisibility(View.GONE);
//            rec_ObservationList.setLayoutManager(new LinearLayoutManager(this));
//            rec_ObservationList.setHasFixedSize(true);
//            rec_ObservationList.setAdapter(new ObservationAdapter(this,o_List));

            }else{
                tv_press.setVisibility(View.VISIBLE);
//            rec_ObservationList.setVisibility(View.GONE);
//            tv_press.setText("No Record Found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }





        hikeAdapter.setListener(new HikeAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v, long id) {

                Hike hike=hikeList.get(position);
                if(id==R.id.btn_Detail){
                    gotoDetailView(hike);
                }else if(id==R.id.btn_Edit){
                    gotoEntry(hike);
                } else if (id==R.id.l_Item) {
                    Log.i("key", String.valueOf(hike.getId()));
                    Intent intent = new Intent(getBaseContext(), ObservationListActivity.class);
                    intent.putExtra(Observation.O_HIKEID, hike.getId());
                    intent.putExtra(Hike.NAME, hike.getName());
                    intent.putExtra(Hike.LOCATION, hike.getLocation());
                    intent.putExtra(Hike.DATE, hike.getDate());
                    intent.putExtra(Hike.PARKING, hike.getParking());
                    intent.putExtra(Hike.LENGTH, hike.getLength());
                    intent.putExtra(Hike.DIFFICULTY, hike.getDifficulty());
                    intent.putExtra(Hike.DESCRIPTION, hike.getDescription());
                    startActivity(intent);
                } else if (id == R.id.btn_Remove) {

                    new AlertDialog.Builder(DatabaseListActivity.this)
                            .setTitle("Delete")
                            .setMessage("This is a sample alert message.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Code to be executed when the "OK" button is clicked
                                    // You can leave this empty or add functionality here
                                    long result = databaseHelper.delete(hike.getId());
                                    if (result != 1) {
                                        new AlertDialog.Builder(getBaseContext()).setTitle("Error").setMessage("Still can't delete this Hike").show();
                                    } else {
                                        search("");
                                    }

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Code to be executed when the "Cancel" button is clicked
                                    // You can leave this empty or add functionality here
                                }
                            })
                            .show();




                }

            }
        });


        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), EntryActivity.class);
                startActivity(intent);
                Log.i("cc", "cc");
            }
        });
        fab_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), GPTActivity.class);
                startActivity(intent);
                Log.i("cc", "cc");
            }
        });


        recyclerView.setAdapter(hikeAdapter);
        search("");
//        FloatingActionButton fab=findViewById(R.id.fabAddHike);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(getBaseContext(), EntryActivity.class);
//                startActivityForResult(intent,UPDATE_REQUEST);
//            }
//        });

        txtsearch=findViewById(R.id.textView);
        Button btnsearch=findViewById(R.id.button);
        Button btnAdvancedSearch=findViewById(R.id.btn_AdvSearch);

        txtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("test", charSequence.toString());

                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                hikeList = databaseHelper.search(charSequence.toString());
                                hikeAdapter.setHikesList(hikeList);
                                hikeAdapter.notifyDataSetChanged(); // refresh the data
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("test", charSequence.toString());

                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                hikeList = databaseHelper.search(charSequence.toString());
                                hikeAdapter.setHikesList(hikeList);
                                hikeAdapter.notifyDataSetChanged(); // refresh the data
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(txtsearch.getText().toString());
            }
        });

        btnAdvancedSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(),HikeAdvanceSearchActivity.class);
                startActivityForResult(intent,SEARCH_REQUEST);
            }
        });
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        //  search();
    }

    private void search(String keyword){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    hikeList=databaseHelper.searchHike(keyword);
//                    Log.i("MyName", hikeList.size() + "");
                    for (Hike hike : hikeList) {
                        hike.setTimeDifference(DateUtils.getTimeDifference(hike.getDate()));
                    }

                    hikeAdapter.setHikesList(hikeList);
                    hikeAdapter.notifyDataSetChanged(); //refresh the data
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    //// pass the data to the HikeEntryActivity
    private void gotoEntry(Hike hike){
        Intent intent=new Intent(this, EntryActivity.class);
        fillIntentData(intent,hike);
        startActivityForResult(intent,UPDATE_REQUEST);
    }

    private void gotoDetailView(Hike hike){
        Intent intent=new Intent(this, HikeDetailActivity.class);
        fillIntentData(intent,hike);
        startActivity(intent);
    }

    private void fillIntentData(Intent intent, Hike hike){

        intent.putExtra(Hike.ID,hike.getId());
        intent.putExtra(Hike.NAME,hike.getName());
        intent.putExtra(Hike.DATE,hike.getDate());
        intent.putExtra(Hike.LOCATION,hike.getLocation());
        intent.putExtra(Hike.DIFFICULTY,hike.getDifficulty());
        intent.putExtra(Hike.LENGTH,hike.getLength());
        intent.putExtra(Hike.PARKING,hike.getParking());
        intent.putExtra (Hike.DESCRIPTION,hike.getDescription());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==UPDATE_REQUEST && resultCode==RESULT_OK){
            search("");
        }
        else if (requestCode==SEARCH_REQUEST && resultCode==RESULT_OK){
            String name= data.getStringExtra(Hike.NAME);
            String location=data.getStringExtra(Hike.LOCATION);
            String date=data.getStringExtra(Hike.DATE);
            String length=data.getStringExtra(Hike.LENGTH);
            txtsearch.setText(name);

            Log.i("search",date );
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    try {
//                        hikeList=databaseHelper.searchHike(name,location,date,(length !=null && !length.isEmpty()?Double.parseDouble(length):null));
//                        Log.i("MyName", hikeList.size() + "");
//                        hikeAdapter.setHikeList(hikeList);
//                        hikeAdapter.notifyDataSetChanged(); //refresh the data
//                    }

                    try {
                        hikeList=databaseHelper.searchHike(name,location, date);
                        Log.i("adv", hikeList.size() + "");
                        hikeAdapter.setHikesList(hikeList);
                        Log.i("search",date );


                        hikeAdapter.notifyDataSetChanged(); //refresh the data
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
