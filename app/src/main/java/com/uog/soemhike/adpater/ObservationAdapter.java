package com.uog.soemhike.adpater;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uog.soemhike.R;
import com.uog.soemhike.database.Observation;

import java.util.ArrayList;
import java.util.List;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ViewHolder> {

    Context context;
    List<Observation> arrayList;
    public ObservationAdapter(Context context, List<Observation> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
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

        Log.i("tt", o_Record.getTitle());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


//=========================
//=========================
//=========================
//=========================


    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_Oimage;
        TextView txt_Otitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_Oimage = itemView.findViewById(R.id.iv_Oimage);
            txt_Otitle = itemView.findViewById(R.id.txt_Otitle);
        }
    }

}


