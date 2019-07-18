package com.android.aquagiraffe.rotaryconnect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] web;
    private final String[] place;
    private final Integer[] imageId;

    public CustomList(Activity context,
                      String[] web, Integer[] imageId,String[]place) {
        super(context, R.layout.single_list, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
        this.place = place;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"})
        View rowView= inflater.inflate(R.layout.announce_list, null, true);
        TextView txtPlace = (TextView) rowView.findViewById(R.id.txtplace);
        TextView txtPdfName = rowView.findViewById(R.id.txtPdfname);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.pdfImage);
        txtPdfName.setText(web[position]);
        txtPlace.setText(place[position]);

        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
