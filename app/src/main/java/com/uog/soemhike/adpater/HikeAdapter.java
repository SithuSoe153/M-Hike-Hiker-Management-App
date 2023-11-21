package com.uog.soemhike.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.uog.soemhike.R;
import com.uog.soemhike.database.Hike;

import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.ViewHolder> {

public interface ClickListener{
    void onItemClick(int position, View v, long id);
}

    public void setListener (ClickListener listener){
        this.listener = listener;
    }

    private static ClickListener listener;

    private List<Hike> hikesList;
    public void setHikesList(List<Hike> hikesList){
        this.hikesList = hikesList;
    }

    public HikeAdapter(List<Hike> personList){
        this.hikesList =personList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hike_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Hike hike = hikesList.get(position);
        holder.lbl_diff.setText(hike.getTimeDifference());
//        holder.lbl_Id.setText(hike.getId() + "");
        holder.lbl_Name.setText(hike.getName());
        holder.lbl_Location.setText(hike.getLocation());
        holder.lbl_Date.setText(hike.getDate());
//        holder.lbl_Parking.setText(hike.getParking() + "");
//        holder.lbl_Length.setText(hike.getLength() + "");
//        holder.lbl_difficulty.setText(hike.getDifficulty() + "");
//        holder.lbl_Description.setText(hike.getDescription() + "");


    }

    @Override
    public int getItemCount() {
        return hikesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

    TextView lbl_diff,lbl_Id, lbl_Name, lbl_Location, lbl_Date, lbl_Parking, lbl_Length, lbl_difficulty, lbl_Description;
    Button btn_Remove, btn_Edit;
    ConstraintLayout l_Item;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        lbl_diff = itemView.findViewById(R.id.lbl_diff);
//        lbl_Id = itemView.findViewById(R.id.lbl_Id);
        lbl_Name = itemView.findViewById(R.id.lbl_Name);
        lbl_Location = itemView.findViewById(R.id.lbl_Location);
        lbl_Date = itemView.findViewById(R.id.lbl_Date);
//        lbl_Parking = itemView.findViewById(R.id.lbl_Parking);
//        lbl_Length = itemView.findViewById(R.id.lbl_Length);
//        lbl_difficulty = itemView.findViewById(R.id.lbl_difficulty);
//        lbl_Description = itemView.findViewById(R.id.lbl_Description);

        btn_Remove = itemView.findViewById(R.id.btn_Remove);
        btn_Edit = itemView.findViewById(R.id.btn_Edit);

        l_Item = itemView.findViewById(R.id.l_Item);


        l_Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(getAdapterPosition(),view,R.id.l_Item);
            }
        });

        btn_Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onItemClick(getAdapterPosition(),view,R.id.btn_Remove);

            }
        });

        btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onItemClick(getAdapterPosition(),view,R.id.btn_Edit);

            }
        });

    }
}

}
