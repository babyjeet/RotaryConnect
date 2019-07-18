package com.android.aquagiraffe.rotaryconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.aquagiraffe.rotaryconnect.NewsletterActivity;
import com.android.aquagiraffe.rotaryconnect.R;


import java.util.ArrayList;
import java.util.HashMap;

public class NewsletterPdfDownAdapter extends BaseAdapter
{
    Context c;

    ArrayList<HashMap<String, String>> datalist;
    HashMap<String, String> data = new HashMap<>();

    public String Pdf_Name = "Pdf_Name";
    public String Pdf_info = "pdf_info";

    int[] integer;
    String[] string;

    LayoutInflater inflater;

    public NewsletterPdfDownAdapter(NewsletterActivity newsletterPdfDownAdapter, HashMap<String ,String> hashMap, ArrayList<HashMap<String,String>> pdfUploadList, String[] strings, int[] ints)
    {
        this.c = newsletterPdfDownAdapter;
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
        final TransactionHolder holder;

        if (convertView == null)
        {

            convertView = inflater.from(c).inflate(R.layout.row_model2, parent, false);

            data = datalist.get(position);
            holder = new TransactionHolder();
            assert convertView != null;
            holder.txtName = convertView.findViewById(R.id.nameTxt);
            holder.txtInfo = convertView.findViewById(R.id.txt_type_info);

            holder.txtName.setText(data.get(Pdf_Name));
            holder.txtInfo.setText(data.get(Pdf_info));
        }
        else
        {
            convertView = inflater.from(c).inflate(R.layout.row_model2, parent, false);

            data = datalist.get(position);
            holder = new TransactionHolder();
            assert convertView != null;
            holder.txtName = convertView.findViewById(R.id.nameTxt);
            holder.txtInfo = convertView.findViewById(R.id.txt_type_info);

            holder.txtName.setText(data.get(Pdf_Name));
            holder.txtInfo.setText(data.get(Pdf_info));
        }

        final String path2 = "https://s3-ap-southeast-1.amazonaws.com/awsaquabucket/P_Rotary/PDF/NewsLatter/" +data.get(Pdf_Name);

        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                displayPdf(path2);
            }
        });

        return convertView;
    }

    public void displayPdf(String path)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(path)));
        c.startActivity(browserIntent);
    }

    class TransactionHolder
    {
        TextView txtName , txtInfo;
    }
}



