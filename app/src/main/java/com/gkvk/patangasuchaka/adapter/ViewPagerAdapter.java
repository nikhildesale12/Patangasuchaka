package com.gkvk.patangasuchaka.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gkvk.R;
import com.gkvk.patangasuchaka.bean.DashboardViewpagerBean;
import com.gkvk.patangasuchaka.main.DistributionSearchActivity;
import com.gkvk.patangasuchaka.main.SpeciesSearchActivity;
import com.gkvk.patangasuchaka.main.UploadActivity;
import com.gkvk.patangasuchaka.util.ApplicationConstant;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewPagerAdapter extends PagerAdapter {

    private List<DashboardViewpagerBean> models;
    private LayoutInflater layoutInflater;
    //Activity activity;
    private Context context;

    public ViewPagerAdapter(List<DashboardViewpagerBean> models, Context context) {
        this.models = models;
        this.context = context;
        //this.activity = activity;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        layoutInflater= LayoutInflater.from(context);

        final View view=layoutInflater.inflate(R.layout.layout_item_dashboard,container,false);

        ImageView imageView;
        TextView title;
        Button buttonModule;

        imageView=view.findViewById(R.id.image);
        title=view.findViewById(R.id.title);
        buttonModule=view.findViewById(R.id.buttonModule);

        imageView.setImageResource(models.get(position).getImage());
        title.setText(models.get(position).getTitle());
        buttonModule.setText(models.get(position).getModule());

        buttonModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(models.get(position).getModule().equals(ApplicationConstant.MODULE_CAPTURE)){
                    Intent i = new Intent(view.getContext(), UploadActivity.class);
                    i.putExtra(ApplicationConstant.FROM_MODULE,ApplicationConstant.MODULE_CAPTURE);
                    view.getContext().startActivity(i);
                }else if(models.get(position).getModule().equals(ApplicationConstant.MODULE_GALLERY)){
                    Intent i = new Intent(view.getContext(), UploadActivity.class);
                    i.putExtra(ApplicationConstant.FROM_MODULE,ApplicationConstant.MODULE_GALLERY);
                    view.getContext().startActivity(i);
                }else if(models.get(position).getModule().equals(ApplicationConstant.MODULE_SPECIES)){
                    Toast.makeText(v.getContext(),"Species Under Development",Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(view.getContext(), SpeciesSearchActivity.class);
//                    i.putExtra(ApplicationConstant.FROM_MODULE,ApplicationConstant.MODULE_SPECIES);
//                    view.getContext().startActivity(i);

                }else if(models.get(position).getModule().equals(ApplicationConstant.MODULE_DISTRIBUTION)){
                    Toast.makeText(v.getContext(),"Distribution Under Development",Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(view.getContext(), DistributionSearchActivity.class);
//                    i.putExtra(ApplicationConstant.FROM_MODULE,ApplicationConstant.MODULE_DISTRIBUTION);
//                    view.getContext().startActivity(i);
                }
            }
        });
        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
