package com.android.aquagiraffe.rotaryconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.aquagiraffe.rotaryconnect.AnnouncementsActivity;
import com.android.aquagiraffe.rotaryconnect.R;


import java.util.ArrayList;
import java.util.HashMap;

public class AnnounsmentPdfDownloadAdapter extends BaseAdapter
{

    Context c;

    private static final String strUri = "https://s3-ap-southeast-1.amazonaws.com/awsaquabucket/pdfs/RCTP+Bulletin-2018-19.pdf";
    ArrayList<HashMap<String, String>> datalist;
    HashMap<String, String> data = new HashMap<>();

    public String Pdf_Name = "Pdf_Name";
    public String Pdf_info = "pdf_info";

    int[] integer;
    String[] string;

    String[] placeArray =
            {
                    "Bhagwati School,Thane",
                    "Ghodbunder Road,Thane",
                    "KBP Degree College",
                    "Wagle Estate & Shree Nagar",
                    "Hypercity Mall,Thane",
                    "Kopri,Thane East",
                    "Bhagwati School,Thane",
                    "Ghodbunder Road,Thane",
                    "KBP Degree College",
                    "Wagle Estate & Shree Nagar",
                    "Hypercity Mall,Thane",
                    "Kopri,Thane East",
                    "Hypercity Mall,Thane",
                    "Kopri,Thane East",
                    "Bhagwati School,Thane",
                    "Ghodbunder Road,Thane",
                    "KBP Degree College",
                    "Wagle Estate & Shree Nagar",
                    "Hypercity Mall,Thane",
                    "Kopri,Thane East",
                    "Hypercity Mall,Thane",
                    "Kopri,Thane East",
                    "Bhagwati School,Thane",
                    "Ghodbunder Road,Thane",
                    "KBP Degree College",
                    "Wagle Estate & Shree Nagar",
                    "Hypercity Mall,Thane",
                    "Kopri,Thane East"
            } ;

    LayoutInflater inflater;

    public AnnounsmentPdfDownloadAdapter(AnnouncementsActivity announcementsActivity, HashMap<String ,String> hashMap, ArrayList<HashMap<String,String>> pdfUploadList, String[] strings, int[] ints)
    {
        this.c = announcementsActivity;
        this.data = hashMap;
        this.datalist = pdfUploadList;
        this.integer = ints;
        this.string = strings;
    }

    @Override
    public int getCount()
    {
        return datalist.size();
    }

    @Override
    public Object getItem(int position)
    {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final AnnounsmentPdfDownloadAdapter.TransactionHolder holder;

        if (convertView == null)
        {
            convertView = inflater.from(c).inflate(com.android.aquagiraffe.rotaryconnect.R.layout.announce_list, parent, false);

            data = datalist.get(position);
            holder = new AnnounsmentPdfDownloadAdapter.TransactionHolder();
            assert convertView != null;
            holder.txtTitleView = convertView.findViewById(R.id.txtTitleView);
            holder.txtInfo = convertView.findViewById(com.android.aquagiraffe.rotaryconnect.R.id.txtTitle);
            holder.txtName = convertView.findViewById(com.android.aquagiraffe.rotaryconnect.R.id.txtPdfname);
            holder.place = convertView.findViewById(R.id.place);
            holder.txtplace = convertView.findViewById(R.id.txtplace);
            holder.txtplace.setVisibility(View.INVISIBLE);
            holder.place.setVisibility(View.INVISIBLE);

            holder.txtName.setText(data.get(Pdf_Name));
            holder.txtInfo.setText(data.get(Pdf_info));
            // holder.txtplace.setText(placeArray[position]);
//            holder.txtplace.setVisibility(View.INVISIBLE);
//            holder.place.setVisibility(View.INVISIBLE);


        }
        else
        {
            convertView = inflater.from(c).inflate(com.android.aquagiraffe.rotaryconnect.R.layout.announce_list, parent, false);

            data = datalist.get(position);
            holder = new AnnounsmentPdfDownloadAdapter.TransactionHolder();
            assert convertView != null;
            holder.txtTitleView = convertView.findViewById(R.id.txtTitleView);
            holder.txtInfo = convertView.findViewById(com.android.aquagiraffe.rotaryconnect.R.id.txtTitle);
            holder.txtName = convertView.findViewById(com.android.aquagiraffe.rotaryconnect.R.id.txtPdfname);
            holder.place = convertView.findViewById(R.id.place);
            holder.txtplace = convertView.findViewById(R.id.txtplace);

            holder.txtplace.setVisibility(View.INVISIBLE);
            holder.place.setVisibility(View.INVISIBLE);

            holder.txtName.setText(data.get(Pdf_Name));
            holder.txtInfo.setText(data.get(Pdf_info));
//            holder.txtplace.setText(placeArray[position]);
//            holder.txtplace.setVisibility(View.INVISIBLE);
//            holder.place.setVisibility(View.INVISIBLE);

        }

        final String path2 = "https://s3-ap-southeast-1.amazonaws.com/awsaquabucket/P_Rotary/PDF/Announcement/" + data.get(Pdf_Name);
        Log.d("path2", path2);
        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                displayPdf(path2);
                //openPDf(path2);
            }
        });
        return convertView;
    }

//    public void openPDf(String path)
//    {
//        Intent intent = new Intent(c, DisplayPDF.class);
//
//        intent.putExtra("PATH", path);
//        c.startActivity(intent);
//    }

    public void displayPdf(String path)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(path)));
        c.startActivity(browserIntent);
    }

    class TransactionHolder
    {
        TextView txtName , txtInfo , txtTitleView , place , txtplace;
    }
}
