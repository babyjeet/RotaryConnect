package com.android.aquagiraffe.rotaryconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.aquagiraffe.rotaryconnect.R;
import com.android.aquagiraffe.rotaryconnect.RotaryBookPdfView;

import com.android.aquagiraffe.rotaryconnect.model.PDFDoc;

import java.util.ArrayList;

public class RotaryPdfViewAdapter extends BaseAdapter {

    Context c;
    ArrayList<PDFDoc> pdfDocuments;
    PDFDoc pdf;
    String pdfName,pdfPlace;


    public RotaryPdfViewAdapter(Context c, ArrayList<PDFDoc> pdfDocuments) {
        this.c = c;
        this.pdfDocuments = pdfDocuments;
    }

    @Override
    public int getCount() {
        return pdfDocuments.size();
    }

    @Override
    public Object getItem(int position) {
        return pdfDocuments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(c).inflate(R.layout.row_model,parent,false);

            TextView txtName = convertView.findViewById(R.id.pdfNameTxt);
            TextView txtInfo = convertView.findViewById(R.id.pdfInfo);


            pdf= (PDFDoc) this.getItem(position);
            txtName.setText(pdf.getName());
            pdfName = txtName.getText().toString();
            txtInfo.setText(pdf.getPdf_info());
            pdfPlace = txtInfo.getText().toString();

            final String name = pdf.getName();

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(c, name, Toast.LENGTH_SHORT).show();

                    Intent i=new Intent(c,RotaryBookPdfView.class);
                    i.putExtra("name", name);
                    c.startActivity(i);
                }
            });
            
        }

        return convertView;
    }


}
