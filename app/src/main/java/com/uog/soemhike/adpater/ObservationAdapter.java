package com.uog.soemhike.adpater;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.uog.soemhike.R;
import com.uog.soemhike.UpdateObservationActivity;
import com.uog.soemhike.database.DatabaseHelper;
import com.uog.soemhike.database.Observation;
import com.uog.soemhike.ObservationImageActivity;

import java.util.List;


public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ViewHolder> {

    Context context;
    List<Observation> arrayList;
    DatabaseHelper databaseHelper;

    public String avatarFilePath;


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

        avatarFilePath = o_Record.avatarFilePath;

        // Load and display avatar image if available
        if (avatarFilePath != null) {

            // Handle the image file path, for example, set it to an ImageView
            Bitmap bitmap = BitmapFactory.decodeFile(avatarFilePath);
            if (bitmap != null) {
//                avatarImageView.setImageBitmap(bitmap);
                holder.iv_Oimage.setImageBitmap(bitmap);

            } else {
                // Handle the case where the image couldn't be loaded
                holder.iv_Oimage.setImageResource(R.drawable.default_avatar); // Set a default image
            }

        }


        holder.txt_Otitle.setText(o_Record.getTitle());
        holder.txt_Year.setText(o_Record.getYear());


        holder.btn_OEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                update_Observation(o_Record);
            }
        });

        holder.btn_ODelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_Observation(o_Record);
            }
        });

        holder.l_oitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ObservationImageActivity.class);
                intent.putExtra(Observation.O_ID, o_Record.getId());
                intent.putExtra(Observation.AVATAR_FILE_PATH, o_Record.getAvatarFilePath());
                context.startActivity(intent);
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

    private void delete_Observation(Observation o){

//        long result = databaseHelper.delete_Observation(o.getId());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation")
                .setMessage("Are You Sure")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("test1", String.valueOf(o.getId()));
//
                        databaseHelper.delete_Observation(o.getId());
                        ((Activity)context).finish();
                        context.startActivity(((Activity) context).getIntent());



                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void update_Observation(Observation o){
        Intent intent = new Intent(context, UpdateObservationActivity.class);
        intent.putExtra(Observation.O_ID, o.getId());
        intent.putExtra(Observation.O_TITLE, o.getTitle());
        intent.putExtra(Observation.O_YEAR, o.getYear());
        intent.putExtra(Observation.O_HIKEID, o.getUser_id());
        intent.putExtra(Observation.AVATAR_FILE_PATH, o.getAvatarFilePath());
        context.startActivity(intent);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_Oimage;
        TextView txt_Otitle;
        TextView txt_Year;
        ConstraintLayout l_oitem;
        ImageButton btn_OEdit, btn_ODelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_Oimage = itemView.findViewById(R.id.iv_Oimage);
            txt_Otitle = itemView.findViewById(R.id.txt_Otitle);
            txt_Year = itemView.findViewById(R.id.txt_Year);
            l_oitem = itemView.findViewById(R.id.l_oitem);
            btn_OEdit = itemView.findViewById(R.id.btn_OEdit);
            btn_ODelete = itemView.findViewById(R.id.btn_ODelete);



        }


    }

}


