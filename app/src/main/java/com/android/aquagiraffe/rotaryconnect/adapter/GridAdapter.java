package com.android.aquagiraffe.rotaryconnect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.android.aquagiraffe.rotaryconnect.R;
import com.android.aquagiraffe.rotaryconnect.model.GridImage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    Context c;
    ArrayList<GridImage> imageArrayList;
    GridImage image;


    public GridAdapter(Context context, ArrayList<GridImage> imageArrayList) {
        this.c = context;
        this.imageArrayList = imageArrayList;
    }

    @Override
    public int getCount() {
        return imageArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(c).inflate(R.layout.single_grid, parent, false);

            ImageView imageView;
            imageView = convertView.findViewById(R.id.imgView);

            image = (GridImage) this.getItem(position);
            // String strImage = image.getImgName();

            String url = "https://s3-ap-southeast-1.amazonaws.com/awsaquabucket/images/"+image.getImgName();

            Picasso.with(c)
                    .load(url)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });


        }


        return convertView;
    }


}
