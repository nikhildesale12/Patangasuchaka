package com.gkvk.patangasuchaka.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gkvk.R;
import com.gkvk.patangasuchaka.bean.HistoryData;
import com.gkvk.patangasuchaka.bean.SpeciesData;
import com.gkvk.patangasuchaka.main.LargeImageActivity;

import java.util.ArrayList;
import java.util.List;

public class ButterflyAdapter extends RecyclerView.Adapter<ButterflyAdapter.MyViewHolder> implements Filterable {

    private Context mContext;
    private List<SpeciesData> butterflyList;
    SpeciesFilter speciesFilter ;

    @Override
    public Filter getFilter() {
        if (speciesFilter == null)
            speciesFilter = new SpeciesFilter();
        return speciesFilter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textSpeciesName;
        public TextView textCommonName;

        public MyViewHolder(View view) {
            super(view);
            mContext = view.getContext();
            textSpeciesName = (TextView) view.findViewById(R.id.text_species_list);
            textCommonName = (TextView) view.findViewById(R.id.text_cm_list);
        }
    }


    public ButterflyAdapter(List<SpeciesData> butterflyList,List<SpeciesData> butterflyListAll) {
        this.butterflyList = butterflyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_species_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final SpeciesData history =  butterflyList.get(position);
        if(history.getSpeciesName() != null && history.getSpeciesName().length()>0) {
            holder.textSpeciesName.setText(history.getSpeciesName());
        }else{
            holder.textSpeciesName.setText("NA");
        }
        if(history.getCommonName() != null && history.getCommonName().length()>0) {
            holder.textCommonName.setText(history.getCommonName());
        }else{
            holder.textCommonName.setText("NA");
        }
    }

    @Override
    public int getItemCount() {
        return butterflyList.size();
    }

    private class SpeciesFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = butterflyList;
                results.count = butterflyList.size();
            }
            else {
                // We perform filtering operation
                List<SpeciesData> newButterflyList = new ArrayList<SpeciesData>();

                for (SpeciesData speciesData : butterflyList) {
                    if (speciesData.getSpeciesName().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
                        newButterflyList.add(speciesData);
                    }
                    if (speciesData.getCommonName().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
                        newButterflyList.add(speciesData);
                    }
                }

                results.values = newButterflyList;
                results.count = newButterflyList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,FilterResults results) {
        // Now we have to inform the adapter about the new list filtered
                butterflyList = (List<SpeciesData>) results.values;
                notifyDataSetChanged();
        }
    }
}
