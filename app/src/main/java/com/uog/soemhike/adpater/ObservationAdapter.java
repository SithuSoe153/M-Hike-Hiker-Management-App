package com.uog.soemhike.adpater;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.uog.soemhike.R;
import com.uog.soemhike.activity.DatabaseListActivity;
import com.uog.soemhike.database.DatabaseHelper;
import com.uog.soemhike.database.Hike;
import com.uog.soemhike.database.Observation;

import java.util.ArrayList;
import java.util.List;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ViewHolder> {

    Context context;
    List<Observation> arrayList;
    DatabaseHelper databaseHelper;

        public ObservationAdapter(Context context, List<Observation> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.databaseHelper = new DatabaseHelper(context);

        }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.observation_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        Observation o_Record = arrayList.get(position);

//        holder.iv_Oimage.setImageResource(R.drawable.ic_launcher_background);
        holder.txt_Otitle.setText(o_Record.getTitle());


        holder.btn_OEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("info", "Clicked");

            }
        });

        holder.btn_ODelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteObservation(o_Record);
            }
        });



//        Log.i("tt", o_Record.getTitle());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


//=========================
//=========================
//=========================
//=========================

    private void deleteObservation(Observation o){

//        long result = databaseHelper.delete_Observation(o.getId());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation")
                .setMessage("Are You Sure")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("test1", String.valueOf(o.getId()));
//
//                        if (result != 1) {
//                            new android.app.AlertDialog.Builder(context).setTitle("Error").setMessage("Still can't delete this Hike").show();
//                        } else {
//                           search("");
//                        }

                        databaseHelper.delete_Observation(o.getId());
                        ((Activity)context).finish();
                        context.startActivity(((Activity) context).getIntent());

//                        notifyDataSetChanged();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_Oimage;
        TextView txt_Otitle;
        ImageButton btn_OEdit, btn_ODelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_Oimage = itemView.findViewById(R.id.iv_Oimage);
            txt_Otitle = itemView.findViewById(R.id.txt_Otitle);
            btn_OEdit = itemView.findViewById(R.id.btn_OEdit);
            btn_ODelete = itemView.findViewById(R.id.btn_ODelete);



        }


    }

}


