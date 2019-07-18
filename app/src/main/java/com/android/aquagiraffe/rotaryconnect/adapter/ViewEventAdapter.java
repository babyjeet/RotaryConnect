package com.android.aquagiraffe.rotaryconnect.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.aquagiraffe.rotaryconnect.R;
import com.android.aquagiraffe.rotaryconnect.ViewEvenetList;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewEventAdapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> arrayList;
    HashMap<String, String> hashMap = new HashMap<>();

    String strTitleOfEvent="TittleOfEvent",strStartDateTime="StartDateTime",strEndDateTime="EndDateTime",strTravelTime="TravelTime",strMessage="message",strLocation="Location";

    int[] ints;
    String[] strings;
    Context c;
    LayoutInflater inflater;


    public ViewEventAdapter(ViewEvenetList viewEvenetList, HashMap<String, String>hashMap,ArrayList<HashMap<String, String>> arrayList, String[] strings, int[] ints) {
        inflater = LayoutInflater.from(viewEvenetList);
        this.c = viewEvenetList;
        this.strings=strings;
        this.hashMap=hashMap;
        this.arrayList=arrayList;
        this.ints=ints;

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
        final TransactionViewHolder holder;
        View view = convertView;

        if (view == null)
        {
            view = inflater.inflate(R.layout.eventview_single,parent,false);
            hashMap = arrayList.get(position);
            holder = new TransactionViewHolder();
            assert view != null;
            holder.txtTitle = view.findViewById(R.id.txtTitleOfEvent);
            holder.txtStartDatetime = view.findViewById(R.id.txtStartDate1);
            holder.txtEndDatetime = view.findViewById(R.id.txtEndDateTime);
            holder.txtTravelTime = view.findViewById(R.id.txtTravelTime);
            holder.txtMessage = view.findViewById(R.id.txtmessage);
            holder.txtLocation = view.findViewById(R.id.txtLocation1);

            holder.txtTitle.setText(hashMap.get(strTitleOfEvent));
            holder.txtStartDatetime.setText(hashMap.get(strStartDateTime));
            holder.txtEndDatetime.setText(hashMap.get(strEndDateTime));
            holder.txtTravelTime.setText(hashMap.get(strTravelTime));
            holder.txtMessage.setText(hashMap.get(strMessage));
            holder.txtLocation.setText(hashMap.get(strLocation));

            holder.txtTitle.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));
            holder.txtStartDatetime.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));
            holder.txtEndDatetime.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));
            holder.txtTravelTime.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));
            holder.txtMessage.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));
            holder.txtLocation.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));


        }else {
            hashMap = arrayList.get(position);
            holder = new TransactionViewHolder();
            assert view != null;
            holder.txtTitle = view.findViewById(R.id.txtTitleOfEvent);
            holder.txtStartDatetime = view.findViewById(R.id.txtStartDate1);
            holder.txtEndDatetime = view.findViewById(R.id.txtEndDateTime);
            holder.txtTravelTime = view.findViewById(R.id.txtTravelTime);
            holder.txtMessage = view.findViewById(R.id.txtmessage);
            holder.txtLocation = view.findViewById(R.id.txtLocation1);

            holder.txtTitle.setText(hashMap.get(strTitleOfEvent));
            holder.txtStartDatetime.setText(hashMap.get(strStartDateTime));
            holder.txtEndDatetime.setText(hashMap.get(strEndDateTime));
            holder.txtTravelTime.setText(hashMap.get(strTravelTime));
            holder.txtMessage.setText(hashMap.get(strMessage));
            holder.txtLocation.setText(hashMap.get(strLocation));

            holder.txtTitle.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));
            holder.txtStartDatetime.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));
            holder.txtEndDatetime.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));
            holder.txtTravelTime.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));
            holder.txtMessage.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));
            holder.txtLocation.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoBold.ttf"));

        }

        return view;
    }

    class TransactionViewHolder
    {
        TextView txtTitle,txtStartDatetime,txtEndDatetime,txtTravelTime,txtMessage,txtLocation;
    }
}
