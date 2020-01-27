package com.gkvk.patangasuchaka.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gkvk.R;
import com.gkvk.patangasuchaka.bean.HistoryData;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {


    private List<HistoryData> historyList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView speciesCircleImageView;
        public TextView textViewSpeciesName;
        public TextView textViewCommonName;
        public TextView textViewPlace;
        public TextView textViewDate;

        public MyViewHolder(View view) {
            super(view);
            speciesCircleImageView = (CircleImageView) view.findViewById(R.id.speciesCircleImageView);
            textViewSpeciesName = (TextView) view.findViewById(R.id.textViewSpeciesName);
            textViewCommonName = (TextView) view.findViewById(R.id.textViewCommonName);
            textViewPlace = (TextView) view.findViewById(R.id.textViewPlace);
            textViewDate = (TextView) view.findViewById(R.id.textViewDate);
        }
    }


    public HistoryAdapter(List<HistoryData> historyList) {
        this.historyList = historyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_history_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HistoryData history = historyList.get(position);
        if(history.getOneScientificName() != null && history.getOneScientificName().length()>0) {
            holder.textViewSpeciesName.setText(history.getOneScientificName());
        }else{
            holder.textViewSpeciesName.setText("NA");
        }
        if(history.getOneCommonName() != null && history.getOneCommonName().length()>0) {
            holder.textViewCommonName.setText(history.getOneCommonName());
        }else{
            holder.textViewCommonName.setText("NA");
        }
        if(history.getDateUploaded() != null && history.getDateUploaded().length()>0) {
            holder.textViewDate.setText(history.getDateUploaded());
        }else{
            holder.textViewDate.setText("NA");
        }
        if(history.getPlaceCap() != null && history.getPlaceCap().length()>0) {
            holder.textViewPlace.setText(history.getPlaceCap());
        }else{
            holder.textViewPlace.setText("NA");
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

}
