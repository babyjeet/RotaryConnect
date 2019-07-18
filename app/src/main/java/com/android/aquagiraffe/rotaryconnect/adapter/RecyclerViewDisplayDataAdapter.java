package com.android.aquagiraffe.rotaryconnect.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.aquagiraffe.rotaryconnect.AnnouncementsActivity;
import com.android.aquagiraffe.rotaryconnect.NewsletterActivity;
import com.android.aquagiraffe.rotaryconnect.R;
import com.android.aquagiraffe.rotaryconnect.RotaryLibraryActivity;
import com.android.aquagiraffe.rotaryconnect.model.PdfDetailsWithImage;
import com.android.aquagiraffe.rotaryconnect.util.DisplayPDF;
import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;

import java.util.ArrayList;

public class RecyclerViewDisplayDataAdapter extends RecyclerView.Adapter<RecyclerViewDisplayDataAdapter.SingleItemRowHolder>
{
    private ArrayList<PdfDetailsWithImage> itemModels;
    private Context mContext;
    Typeface typeface;
    String strMessage;


    public RecyclerViewDisplayDataAdapter(ArrayList<PdfDetailsWithImage> itemModels, Context mContext, String sectionMessages)
    {
        this.itemModels = itemModels;
        this.mContext = mContext;
        this.strMessage = sectionMessages;
    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pdf_with_text_display, null);
        SingleItemRowHolder singleItemRowHolder = new SingleItemRowHolder(v);

        return singleItemRowHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder singleItemRowHolder, int position)
    {
        FontChangeCrawler fontChanger = new FontChangeCrawler(mContext.getAssets(), "RobotoMedium.ttf");

        typeface = Typeface.createFromAsset(mContext.getAssets(),"RobotoMedium.ttf");

        final PdfDetailsWithImage itemModel = itemModels.get(position);
        if(itemModel.getPdf_Name().equals(""))
        {
            singleItemRowHolder.itemImage.setVisibility(View.INVISIBLE);
            singleItemRowHolder.pdfName.setVisibility(View.INVISIBLE);
            if(mContext instanceof AnnouncementsActivity)
            {
                singleItemRowHolder.layoutParams = (RelativeLayout.LayoutParams) singleItemRowHolder.linearLayout.getLayoutParams();
                singleItemRowHolder.layoutParams.setMargins(0, -50, 0, -100);
                singleItemRowHolder.linearLayout.setLayoutParams(singleItemRowHolder.layoutParams);
            }
        }
        else
        {
            singleItemRowHolder.pdfName.setText(itemModel.getPdf_Name());
            singleItemRowHolder.pdfName.setTypeface(typeface);
        }

        Log.d("PDf_name_of_list",itemModel.getPdf_Name());

        singleItemRowHolder.itemImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mContext instanceof NewsletterActivity)
                {
                    final String path2 = "https://s3-ap-southeast-1.amazonaws.com/awsaquabucket/P_Rotary/PDF/NewsLatter/" +itemModel.getPdf_Name();
                    openPDf(path2);
                }
                if( mContext instanceof AnnouncementsActivity)
                {
                    final String path2 = "https://s3-ap-southeast-1.amazonaws.com/awsaquabucket/P_Rotary/PDF/Announcement/" +itemModel.getPdf_Name();
                    openPDf(path2);
                }
                if(mContext instanceof RotaryLibraryActivity)
                {
                    final String path2 = "https://s3-ap-southeast-1.amazonaws.com/awsaquabucket/P_Rotary/PDF/RotaryLibrary/" +itemModel.getPdf_Name();
                    openPDf(path2);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return (null != itemModels ? itemModels.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder
    {
        protected TextView pdfName, txtMessage;
        protected ImageView itemImage;
        RelativeLayout linearLayout;
        RelativeLayout.LayoutParams layoutParams;
        Activity activity = (Activity) mContext;

        public SingleItemRowHolder(View itemView)
        {
            super(itemView);
            this.pdfName = itemView.findViewById(R.id.PdfnameTxt);
            this.itemImage = itemView.findViewById(R.id.PdfImage);
            this.linearLayout = itemView.findViewById(R.id.linear1);
            //this.txtMessage = itemView.findViewById(R.id.txt_message_announse);
        }
    }

    public void openPDf(String path)
    {
        Intent intent = new Intent(mContext, DisplayPDF.class);

        intent.putExtra("PATH", path);
        mContext.startActivity(intent);
    }

}
