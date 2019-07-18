package com.android.aquagiraffe.rotaryconnect.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.aquagiraffe.rotaryconnect.R;
import com.android.aquagiraffe.rotaryconnect.model.SingleItemModel;

import java.util.ArrayList;
import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {

    private  Context context;
    private List<ImageView> images;
    private ArrayList<SingleItemModel> itemModels;

    public ImagePagerAdapter(List<ImageView> images) {
        this.images = images;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.imagepager_layout,null);
        ((ViewPager) container).addView(view);
        final SingleItemModel itemModel = itemModels.get(position);


        return container;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(images.get(position));
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
