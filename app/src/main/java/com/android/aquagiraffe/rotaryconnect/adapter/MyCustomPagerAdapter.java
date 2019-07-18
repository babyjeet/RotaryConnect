package com.android.aquagiraffe.rotaryconnect.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.aquagiraffe.rotaryconnect.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class MyCustomPagerAdapter extends PagerAdapter {
    Context context;
    String[] images;
    LayoutInflater inflater;
    String strProfilename;


    public MyCustomPagerAdapter(Context viewerPager, String[] images, String strprofileName) {
        this.context = viewerPager  ;
        this.images = images;
        strProfilename = strprofileName;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == ((LinearLayout)o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull View container, int position) {

        View itemView = inflater.inflate(R.layout.single_imageviewer, (ViewGroup) container, false);
        SimpleDraweeView imageView = (SimpleDraweeView) itemView.findViewById(R.id.imageView);
        TextView textView = itemView.findViewById(R.id.textView);
        textView.setText(strProfilename);


        Glide.with(context).load("https://s3-ap-southeast-1.amazonaws.com/awsaquabucket/images/"+images[position]).into(imageView);
        ((ViewGroup) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }


}
