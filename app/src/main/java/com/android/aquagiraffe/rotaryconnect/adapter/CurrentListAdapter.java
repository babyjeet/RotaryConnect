package com.android.aquagiraffe.rotaryconnect.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.aquagiraffe.rotaryconnect.DirectoryActivity;
import com.android.aquagiraffe.rotaryconnect.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;

public class CurrentListAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> datalist;
    HashMap<String, String> data = new HashMap<>();


    public String strRCTP_ID = "RCTP_ID";
    public String strNAME = "NAME";
    public String strCLASSIFICATION = "CLASSIFICATION";
    public String strPROFILEPHOTO = "PROFILEPHOTO";

    int[] integer;
    String[] string;
    Context c;
    LayoutInflater inflater;

    public CurrentListAdapter(DirectoryActivity mainActivity, HashMap<String, String> transaction, ArrayList<HashMap<String, String>> transactionList, String[] string, int[] integer) {
        inflater = LayoutInflater.from(mainActivity);
        this.data = transaction;
        this.datalist = transactionList;
        this.integer = integer;
        this.string = string;
        this.c = mainActivity;

    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int i) {
        return datalist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final TransactionHolder holder;
        View v = view;
        if (v == null) {

            v = inflater.inflate(R.layout.single_directory_item, viewGroup, false);
            data = datalist.get(i);
            holder = new TransactionHolder();
            assert v != null;
            holder.txtRctp = v.findViewById(R.id.RCTP_ID);
            holder.txtName = v.findViewById(R.id.NAME);
            holder.txtClassification = v.findViewById(R.id.CLASSIFICATION);
            holder.imgProfile = v.findViewById(R.id.PROFILEPHOTO);

            holder.txtRctp.setText(data.get(strRCTP_ID));
            holder.txtName.setText(data.get(strNAME));
            holder.txtClassification.setText(data.get(strCLASSIFICATION));

            holder.txtRctp.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoMedium.ttf"));
            holder.txtName.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoMedium.ttf"));
            holder.txtClassification.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoMedium.ttf"));


            String url = "https://s3-ap-southeast-1.amazonaws.com/awsaquabucket/P_Rotary/Profile/" + data.get(strPROFILEPHOTO);

            Picasso.with(c)
                    .load(url)
                    .into(holder.imgProfile, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
            v.setTag(holder);

        } else {
            v = inflater.inflate(R.layout.single_directory_item, viewGroup, false);
            data = datalist.get(i);
            holder = new TransactionHolder();
            assert v != null;
            holder.txtRctp = v.findViewById(R.id.RCTP_ID);
            holder.txtName = v.findViewById(R.id.NAME);
            holder.txtClassification = v.findViewById(R.id.CLASSIFICATION);
            holder.imgProfile = v.findViewById(R.id.PROFILEPHOTO);

            holder.txtRctp.setText(data.get(strRCTP_ID));
            holder.txtName.setText(data.get(strNAME));
            holder.txtClassification.setText(data.get(strCLASSIFICATION));

            holder.txtRctp.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoMedium.ttf"));
            holder.txtName.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoMedium.ttf"));
            holder.txtClassification.setTypeface(Typeface.createFromAsset(c.getAssets(), "RobotoMedium.ttf"));


            String url = "https://s3-ap-southeast-1.amazonaws.com/awsaquabucket/P_Rotary/Profile/" + data.get(strPROFILEPHOTO);

            Picasso.with(c)
                    .load(url)
                    .into(holder.imgProfile, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
            v.setTag(holder);
        }


        return v;
    }

}

class TransactionHolder {
    TextView txtRctp, txtName, txtClassification;
    ImageView imgProfile;
}

