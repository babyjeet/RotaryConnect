package com.android.aquagiraffe.rotaryconnect.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.android.aquagiraffe.rotaryconnect.R;
import com.android.aquagiraffe.rotaryconnect.ViewerPager;
import com.android.aquagiraffe.rotaryconnect.model.SingleItemModel;


import java.util.ArrayList;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<SingleItemModel> itemModels;
    private Context mContext;
    private String sectionName;

    public SectionListDataAdapter(ArrayList<SingleItemModel> itemModels, Context mContext, String sectionName)
    {
        this.itemModels = itemModels;
        this.mContext = mContext;
        this.sectionName = sectionName;
    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        SingleItemRowHolder singleItemRowHolder = new SingleItemRowHolder(v);
        return singleItemRowHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder singleItemRowHolder, final int i) {
        final SingleItemModel itemModel = itemModels.get(i);
        singleItemRowHolder.tvTitle.setText(itemModel.getName());

        final String Names = itemModel.getNameArray();
        Glide.with(mContext).load("https://s3-ap-southeast-1.amazonaws.com/awsaquabucket/images/" + itemModel.getName()).into(singleItemRowHolder.itemImage);

        final String data = itemModel.getName();

        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(data);

        final String profileName = itemModel.getStrProfileName();

        singleItemRowHolder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ViewerPager.class);
                intent.putExtra("data", Names);
                intent.putExtra("p",String.valueOf(i));
                intent.putExtra("profileName", profileName);
                intent.putExtra("title",sectionName);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != itemModels ? itemModels.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle;
        protected ImageView itemImage;



        Activity activity = (Activity) mContext;
        public SingleItemRowHolder(View itemView) {
            super(itemView);
            this.tvTitle = itemView.findViewById(R.id.tvTitle);
            this.itemImage = itemView.findViewById(R.id.itemImage);


        }
    }


}
