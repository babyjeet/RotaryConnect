package com.android.aquagiraffe.rotaryconnect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.aquagiraffe.rotaryconnect.ImagesUpload;
import com.android.aquagiraffe.rotaryconnect.R;
import com.android.aquagiraffe.rotaryconnect.model.ImageModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridImageAdapter extends BaseAdapter {

    Context c;
    ArrayList<ImageModel> imageModelArrayList;
    ArrayList<String> filePaths;


    public GridImageAdapter(ImagesUpload imagesUpload, ArrayList<ImageModel> spacecrafts, ArrayList<String> filePaths) {

        this.c = imagesUpload;
        this.imageModelArrayList = spacecrafts;
        this.filePaths = filePaths;

    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(c).inflate(R.layout.image_model, parent, false);
        }

        final ImageModel imageModel = (ImageModel) this.getItem(position);

        ImageView imgPhoto = convertView.findViewById(R.id.spacecraftImg);
        ImageView imageView = convertView.findViewById(R.id.imgcross);


        Picasso.with(c).load(imageModel.getUri()).placeholder(R.drawable.placeholder).into(imgPhoto);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageModelArrayList.remove(position);
                filePaths.remove(position);

                notifyDataSetChanged();
                notifyDataSetInvalidated();

            }
        });

        return convertView;
    }


}
