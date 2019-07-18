package com.android.aquagiraffe.rotaryconnect.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.aquagiraffe.rotaryconnect.AnnouncementsActivity;
import com.android.aquagiraffe.rotaryconnect.R;
import com.android.aquagiraffe.rotaryconnect.model.PdfTitleModel;
import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;

import java.util.ArrayList;

public class RecyclerViewPDFAdapter extends RecyclerView.Adapter<RecyclerViewPDFAdapter.PdfRowHolder>
{
    Context c;
    private ArrayList<PdfTitleModel> dataList;
    private Context mContext;
    private RecyclerView.RecycledViewPool recycledViewPool;
    Typeface typeface;
    public RecyclerViewPDFAdapter(ArrayList<PdfTitleModel> dataList, Context mContext)
    {
        this.dataList = dataList;
        this.mContext = mContext;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    @Override
    public PdfRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pdf_title_row, null);
        PdfRowHolder pdfRowHolder = new PdfRowHolder(view);
        return pdfRowHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PdfRowHolder holder, int position)
    {
        FontChangeCrawler changeCrawler = new FontChangeCrawler(mContext.getAssets(),"RobotoMedium.ttf");

        typeface = Typeface.createFromAsset(mContext.getAssets(),"RobotoMedium.ttf");

        PdfTitleModel pdfTitleModel = new PdfTitleModel();

        final String sectionHeaderName = dataList.get(position).getheaderTitle();
        final String sectionMessages = dataList.get(position).getStrMessageAnouns();
        ArrayList singleSectionItems = dataList.get(position).getAllItemInSection();

        if(mContext instanceof AnnouncementsActivity)
        {
            holder.pdf_title.setText(sectionHeaderName);
            holder.pdf_title.setTypeface(typeface);
            if(!sectionMessages.equals("null"))
            {
                holder.pdf_message.setText(sectionMessages);
                holder.pdf_message.setTypeface(typeface);
            }
            else
            {
                holder.layoutParams = (RelativeLayout.LayoutParams)holder.relativeLayout.getLayoutParams();
                holder.layoutParams.setMargins(0,-40,0,-50);
                holder.relativeLayout.setLayoutParams(holder.layoutParams);
            }
        }

        else
        {
            holder.pdf_title.setText(sectionHeaderName);
            holder.pdf_title.setTypeface(typeface);
            if(mContext instanceof AnnouncementsActivity)
            {
                holder.layoutParams = (RelativeLayout.LayoutParams) holder.relativeLayout.getLayoutParams();
                holder.layoutParams.setMargins(0, -30, 0, 0);
                holder.relativeLayout.setLayoutParams(holder.layoutParams);
            }
            else
            {
                holder.layoutParams = (RelativeLayout.LayoutParams) holder.relativeLayout.getLayoutParams();
                holder.layoutParams.setMargins(0, -20, 0, 0);
                holder.relativeLayout.setLayoutParams(holder.layoutParams);
            }
        }

        RecyclerViewDisplayDataAdapter adapter = new RecyclerViewDisplayDataAdapter(singleSectionItems,mContext,sectionMessages);
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new GridLayoutManager(mContext,4));

        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setDrawingCacheEnabled(true);
        holder.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setRecycledViewPool(recycledViewPool);
    }

    @Override
    public int getItemCount()
    {
        return   (null != dataList ? dataList.size() : 0);
    }

    public static class PdfRowHolder extends RecyclerView.ViewHolder
    {
        protected TextView pdf_title, pdf_message;
        protected RecyclerView recyclerView;
        RelativeLayout relativeLayout;
        RelativeLayout.LayoutParams layoutParams;

        public PdfRowHolder(View itemView)
        {
            super(itemView);
            pdf_title = itemView.findViewById(R.id.pdfTitle);
            pdf_title.setAllCaps(true);
            recyclerView = itemView.findViewById(R.id.recycler_view_list);
            pdf_message = itemView.findViewById(R.id.txt_sms);

            relativeLayout = itemView.findViewById(R.id.relative_recycler);
        }
    }
}
