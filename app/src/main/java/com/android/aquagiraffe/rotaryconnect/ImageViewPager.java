package com.android.aquagiraffe.rotaryconnect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.aquagiraffe.rotaryconnect.adapter.SectionListDataAdapter;
import com.android.aquagiraffe.rotaryconnect.model.SingleItemModel;
import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;


import java.util.ArrayList;

public class ImageViewPager extends Activity {

    int position;
    String imagename;
    SectionListDataAdapter sectionListDataAdapter;
    ArrayList<SingleItemModel> itemModels;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view_pager);

        imageView = findViewById(R.id.imageview);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        Intent p = getIntent();
        position = p.getExtras().getInt("id");

        imagename = (String) p.getExtras().get("imageName");

    }
}
