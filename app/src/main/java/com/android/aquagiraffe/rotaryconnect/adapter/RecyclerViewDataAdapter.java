package com.android.aquagiraffe.rotaryconnect.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.aquagiraffe.rotaryconnect.R;
import com.android.aquagiraffe.rotaryconnect.model.SectionDataModel;

import java.util.ArrayList;

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ItemRowHolder> {
    private ArrayList<SectionDataModel> dataList;
    private Context mContext;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public RecyclerViewDataAdapter(ArrayList<SectionDataModel> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null);
        ItemRowHolder rowHolder = new ItemRowHolder(v);
        // snapHelper = new GravitySnapHelper(Gravity.START);
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRowHolder holder, int i) {
        final String sectionName = dataList.get(i).getHeaderTitle();
        final String date = dataList.get(i).getStrdate();
        final String strImgname = dataList.get(i).getStrImageName();
        ArrayList singleSectionItems = dataList.get(i).getAllItemInSection();
        holder.itemTitle.setText(sectionName);
        holder.btnMore.setText(date);
        holder.btnMore.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "RobotoMedium.ttf"));
        holder.itemTitle.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "RobotoMedium.ttf"));
        holder.Title.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "RobotoMedium.ttf"));
        holder.txtDate.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "RobotoMedium.ttf"));

        Glide.with(mContext).load("https://s3-ap-southeast-1.amazonaws.com/awsaquabucket/P_Rotary/Profile/"+strImgname)
                .into(holder.imageview);

        SectionListDataAdapter adapter = new SectionListDataAdapter(singleSectionItems, mContext,sectionName);
        holder.recyclerView.setLayoutManager(new GridLayoutManager(mContext,4));

        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setDrawingCacheEnabled(true);
        holder.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setRecycledViewPool(recycledViewPool);

    }

    @Override
    public int getItemCount() {
        return  (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        protected TextView itemTitle,btnMore,Title,txtDate;
        protected RecyclerView recyclerView;
        protected ImageView imageview;

        public ItemRowHolder(View itemView) {
            super(itemView);
            this.itemTitle = itemView.findViewById(R.id.itemTitle);
            this.recyclerView = itemView.findViewById(R.id.recycler_view_list);
            this.btnMore = itemView.findViewById(R.id.btnMore);
            this.imageview = itemView.findViewById(R.id.imageview);
            this.Title = itemView.findViewById(R.id.Title);
            this.txtDate = itemView.findViewById(R.id.txtDate);
        }
    }
}
