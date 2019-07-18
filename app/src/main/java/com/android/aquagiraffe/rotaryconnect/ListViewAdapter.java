package com.android.aquagiraffe.rotaryconnect;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<ListModel> {
    private ArrayList<ListModel> dataset;
    Context mContext;

    public ListViewAdapter(ArrayList<ListModel> datamodel, Context applicationContext)
    {
        super(applicationContext,R.layout.single_list,datamodel);
        this.dataset = datamodel;
        this.mContext = applicationContext;
    }
    public static class ViewHolder
    {
        TextView txtLocation;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ListModel listModel = getItem(position);
        ListViewAdapter.ViewHolder holder;

        if (convertView==null)
        {
            holder = new ListViewAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.single_list,parent,false);
            holder.txtLocation = convertView.findViewById(R.id.txtLocation);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtLocation.setText(listModel.getStrLocation());

        return convertView;
    }
}
