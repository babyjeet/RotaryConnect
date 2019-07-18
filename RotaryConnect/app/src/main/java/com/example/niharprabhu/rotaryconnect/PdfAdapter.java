package com.android.aquagiraffe.rotaryconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PdfAdapter extends BaseAdapter {
    private ArrayList<DataModel> dataset;
    Context mContext;

    public PdfAdapter(MainActivity mainActivity, ArrayList<DataModel> pdfs) {
        this.mContext=mainActivity;
        this.dataset = pdfs;
    }


    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public Object getItem(int i) {
        return dataset.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.single_list,viewGroup,false);
        }
        final DataModel pdfdocs = (DataModel) this.getItem(i);
        ImageView img = view.findViewById(R.id.pdfImage);
        TextView nameTxt = view.findViewById(R.id.txtPdfname);

        nameTxt.setText(pdfdocs.getPdfname());
        img.setImageResource(R.drawable.pdf_icon);


        return view;
    }
}
