package com.android.aquagiraffe.rotaryconnect;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private final String[] web;
    private final int[] Imageid;

    public ImageAdapter(Context c,String[] web, int[] Imageid)
    {
        mContext = c;
        this.Imageid = Imageid;
        this.web = web;
    }



    @Override
    public int getCount() {
        return web.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
        {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView textView = grid.findViewById(R.id.grid_text);
            TextView imageView = grid.findViewById(R.id.grid_image);

            textView.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "Roboto.ttf"));
            textView.setText(web[position]);
            imageView.setTypeface(Typeface.createFromAsset(mContext.getAssets(),"fontawesome-webfont.ttf"));
            imageView.setText(Imageid[position]);
        }
        else
        {
            grid = convertView;
        }

        return grid;
    }
}
