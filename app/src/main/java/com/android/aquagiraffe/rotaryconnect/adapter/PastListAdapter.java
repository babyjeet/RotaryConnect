package com.android.aquagiraffe.rotaryconnect.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.aquagiraffe.rotaryconnect.PastPresidentsActivity;
import com.android.aquagiraffe.rotaryconnect.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by NiharPrabhu on 7/26/2018.
 */

public class PastListAdapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> arrayList;
    HashMap<String, String> hashMap = new HashMap<>();
    String Name = "Name",Title = "Title",RCTP_ID = "RCTP_ID",RCTP_Year = "RCTP_Year",RCTP_Year_Name = "RCTP_Year_Name",President_Photo = "President_Photo",President_logo = "President_logo" ;
    int[] ints;
    String[] strings;
    Context c;
    LayoutInflater inflater;


    public PastListAdapter(PastPresidentsActivity presidentsActivity, HashMap<String,String> hashMap, ArrayList<HashMap<String,String>> arrayList, String[] strings, int[] ints) {

        inflater = LayoutInflater.from(presidentsActivity);
        this.arrayList = arrayList;
        this.hashMap = hashMap;
        this.ints = ints;
        this.strings = strings;
        this.c = presidentsActivity;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TransactionViewHolder holder;
        View view = convertView;

        if (view == null)
        {
            view = inflater.inflate(R.layout.past_row,parent,false);
            hashMap = arrayList.get(position);
            holder = new TransactionViewHolder();
            assert view != null;


            holder.txtName = view.findViewById(R.id.member);
            holder.txtTitle = view.findViewById(R.id.title);
            holder.txtRctp = view.findViewById(R.id.rctpid);
            holder.txtRctpyear = view.findViewById(R.id.year);
            holder.txtRctpYearName = view.findViewById(R.id.rctpyearname);
            holder.President_Photo = view.findViewById(R.id.thumbnail);
            holder.President_logo = view.findViewById(R.id.yearImg);


            holder.txtName.setText(hashMap.get(Name));
            holder.txtTitle.setText(hashMap.get(Title));
            holder.txtRctp.setText(hashMap.get(RCTP_ID));
            holder.txtRctpyear.setText(hashMap.get(RCTP_Year));
            holder.txtRctpYearName.setText(hashMap.get(RCTP_Year_Name));

            holder.txtName.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));
            holder.txtTitle.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));
            holder.txtRctp.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));
            holder.txtRctpyear.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));
            holder.txtRctpYearName.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));


            Picasso.with(c)
                    .load("http://106.201.232.22:85/RCTP/Profile/"+hashMap.get(President_Photo))
                    .into(holder.President_Photo, new Callback() {
                        @Override
                        public void onSuccess() {
                            //     Toast.makeText(context, "Image uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError() {

                        }
                    });



            Picasso.with(c)
                    .load("http://106.201.232.22:85/RCTP/Profile/"+hashMap.get(President_logo))
                    .into(holder.President_logo, new Callback() {
                        @Override
                        public void onSuccess() {
                            //     Toast.makeText(context, "Image uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError() {

                        }
                    });


        }

        else {
            hashMap = arrayList.get(position);
            holder = new TransactionViewHolder();
            assert view != null;

            holder.txtName = view.findViewById(R.id.member);
            holder.txtTitle = view.findViewById(R.id.title);
            holder.txtRctp = view.findViewById(R.id.rctpid);
            holder.txtRctpyear = view.findViewById(R.id.year);
            holder.txtRctpYearName = view.findViewById(R.id.rctpyearname);
            holder.President_Photo = view.findViewById(R.id.thumbnail);
            holder.President_logo = view.findViewById(R.id.yearImg);

            holder.txtName.setText(hashMap.get(Name));
            holder.txtTitle.setText(hashMap.get(Title));
            holder.txtRctp.setText(hashMap.get(RCTP_ID));
            holder.txtRctpyear.setText(hashMap.get(RCTP_Year));
            holder.txtRctpYearName.setText(hashMap.get(RCTP_Year_Name));


            holder.txtName.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));
            holder.txtTitle.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));
            holder.txtRctp.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));
            holder.txtRctpyear.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));
            holder.txtRctpYearName.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));


            Picasso.with(c)
                    .load("http://106.201.232.22:85/RCTP/Profile/"+hashMap.get(President_Photo))
                    .into(holder.President_Photo, new Callback() {
                        @Override
                        public void onSuccess() {
                            //     Toast.makeText(context, "Image uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError() {

                        }
                    });



            Picasso.with(c)
                    .load("http://106.201.232.22:85/RCTP/Profile/"+hashMap.get(President_logo))
                    .into(holder.President_logo, new Callback() {
                        @Override
                        public void onSuccess() {
                            //     Toast.makeText(context, "Image uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError() {

                        }
                    });

        }


        return view;
    }

    class TransactionViewHolder
    {
        TextView txtName,txtTitle,txtRctp,txtRctpyear,txtRctpYearName;
        ImageView President_Photo,President_logo;
    }

}
