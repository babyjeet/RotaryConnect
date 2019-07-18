package com.android.aquagiraffe.rotaryconnect.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.aquagiraffe.rotaryconnect.DashProfileActivity;
import com.android.aquagiraffe.rotaryconnect.ProfileEditActivity;
import com.android.aquagiraffe.rotaryconnect.R;
import com.android.aquagiraffe.rotaryconnect.model.KidModel;

import java.util.ArrayList;

public class KidAdapter extends BaseAdapter {

    ArrayList<KidModel> arrayList, temp;
    Context context;
    String txtHint;

    public KidAdapter(DashProfileActivity dashProfileActivity, ArrayList<KidModel> stringArrayList) {
        this.arrayList = stringArrayList;
        this.context = dashProfileActivity;
    }

    public KidAdapter(ProfileEditActivity profileEditActivity, ArrayList<KidModel> stringArrayList, String s) {
        this.arrayList = stringArrayList;
        this.context = profileEditActivity;
        this.txtHint = s;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.kid_single_list, parent, false);
        }
       // arrayList.clear();

        final KidModel imageModel = (KidModel) this.getItem(position);

        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtContact = convertView.findViewById(R.id.txtContact);
        TextView txtDob = convertView.findViewById(R.id.txtDob);
        TextView txtGender = convertView.findViewById(R.id.txtGender);
        TextView edtText = convertView.findViewById(R.id.edtText);
        TextView txtid = convertView.findViewById(R.id.id);
        TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        txtTitle.setText(txtHint);

        txtName.setText(imageModel.getName());
        txtContact.setText(imageModel.getContact());
        txtDob.setText(imageModel.getDOB());
        txtGender.setText(imageModel.getGender());
        txtid.setText(imageModel.getId());

        txtName.setTypeface(Typeface.createFromAsset(context.getAssets(), "RobotoBold.ttf"));
        txtContact.setTypeface(Typeface.createFromAsset(context.getAssets(), "RobotoBold.ttf"));
        txtDob.setTypeface(Typeface.createFromAsset(context.getAssets(), "RobotoBold.ttf"));
        txtGender.setTypeface(Typeface.createFromAsset(context.getAssets(), "RobotoBold.ttf"));
        txtid.setTypeface(Typeface.createFromAsset(context.getAssets(), "RobotoBold.ttf"));

        return convertView;
    }
}
