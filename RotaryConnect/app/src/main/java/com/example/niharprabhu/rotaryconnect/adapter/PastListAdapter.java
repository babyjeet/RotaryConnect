package com.android.aquagiraffe.rotaryconnect.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.aquagiraffe.rotaryconnect.R;
import com.android.aquagiraffe.rotaryconnect.app.AppController;
import com.android.aquagiraffe.rotaryconnect.model.Past;

import java.util.List;

/**
 * Created by NiharPrabhu on 7/26/2018.
 */

public class PastListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Past> pastItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public PastListAdapter(Activity activity, List<Past> pastItems) {
        this.activity = activity;
        this.pastItems = pastItems;
    }

    @Override
    public int getCount() {
        return pastItems.size();
    }

    @Override
    public Object getItem(int location) {
        return pastItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.past_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView member = (TextView) convertView.findViewById(R.id.member);
        TextView year = (TextView) convertView.findViewById(R.id.year);
        NetworkImageView yearImg = (NetworkImageView) convertView.findViewById(R.id.yearImg);

        // getting movie data for the row
        Past p = pastItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(p.getThumbnailUrl(), imageLoader);

        // title
        title.setText(p.getTitle());

        // member ID
        member.setText("RCTP Member ID: " + String.valueOf(p.getRating()));

        year.setText("President Year: " + p.getYear());

        yearImg.setImageUrl(p.getYearImageUrl(), imageLoader);

        return convertView;
    }
}
