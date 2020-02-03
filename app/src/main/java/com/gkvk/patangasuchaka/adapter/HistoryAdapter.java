package com.gkvk.patangasuchaka.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.gkvk.R;
import com.gkvk.patangasuchaka.bean.HistoryData;
import java.util.List;
import com.bumptech.glide.Glide;
import com.gkvk.patangasuchaka.fragment.LargeImageFragment;
import com.gkvk.patangasuchaka.main.MainActivity;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<HistoryData> historyList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView speciesCircleImageView;
        public TextView textViewSpeciesName;
        public TextView textViewCommonName;
        public TextView textViewCategory;
        public TextView textViewPlace;
        public TextView textViewDate;

        public MyViewHolder(View view) {
            super(view);
            mContext = view.getContext();
            speciesCircleImageView = (CircleImageView) view.findViewById(R.id.speciesCircleImageView);
            textViewSpeciesName = (TextView) view.findViewById(R.id.textViewSpeciesName);
            textViewCommonName = (TextView) view.findViewById(R.id.textViewCommonName);
            textViewCategory = (TextView) view.findViewById(R.id.textViewCategory);
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
        final HistoryData history = historyList.get(position);
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
        if(history.getButtCategory() != null && history.getButtCategory().toString().length()>0) {
            holder.textViewCategory.setText((CharSequence) history.getButtCategory());
        }else{
            holder.textViewCategory.setText("NA");
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

        Glide.with(mContext)
                .load(history.getImage())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(holder.speciesCircleImageView);

        holder.speciesCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialogViewLargeImage(v,history);

                LargeImageFragment fragment = new LargeImageFragment();
                FragmentManager fragmentManager =  ((MainActivity)mContext).getSupportFragmentManager();
                Bundle args = new Bundle();
                args.putString("imagepath", history.getImage());
                fragment.setArguments(args);
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    private void dialogViewLargeImage(View v,HistoryData history) {
        final Dialog dialogLargeImage = new Dialog(v.getContext());
        dialogLargeImage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLargeImage.setContentView(R.layout.dialog_popup_image);
        dialogLargeImage.setCanceledOnTouchOutside(false);
        ImageView imageView = (ImageView) dialogLargeImage.findViewById(R.id.large_image);
        Glide.with(mContext)
                .load(history.getImage())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(imageView);
        Button cancelBtn = (Button) dialogLargeImage.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLargeImage.dismiss();
            }
        });
        dialogLargeImage.show();
    }
}
