package com.android.aquagiraffe.rotaryconnect;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;

import java.util.HashMap;

public class BirthDay_Activity extends AppCompatActivity {


    TextView txtview, txtname;
    ImageView imgview ;
    String strName;
    private static int SPLASH_TIME_OUT = 4000;
    Boolean Flag = false;
    HashMap<String,String> hashMap = new HashMap<>();
    SessionManager sessionManager;
    ImageView back;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birth_day_);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        txtview = findViewById(R.id.txt_birth_day);
        txtname = findViewById(R.id.txtname);
        sessionManager = new SessionManager(getApplicationContext());
        hashMap = sessionManager.getUserDetails();
        strName = hashMap.get(SessionManager.KEY_NAME);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        back = findViewById(R.id.imgback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BirthDay_Activity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });




        txtview.setText("Wish You Happy \n birth day \n to you");
        txtview.setSelected(true);

        txtname.setText(strName);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Flag = true;

                Intent i = new Intent(BirthDay_Activity.this, DashboardActivity.class);
                i.putExtra("Flag",Flag);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);

        final Animation animation_4 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.anim_shake);
//        final Animation animation_3 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.abc_fade_out);
        final Animation animation_bounce = AnimationUtils.loadAnimation(getBaseContext(),R.anim.bounce);
        final Animation aimation_blink = AnimationUtils.loadAnimation(getBaseContext(),R.anim.blink);


        txtname.setAnimation(aimation_blink);
        aimation_blink.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                txtname.startAnimation(aimation_blink);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });

        imageView.startAnimation(animation_bounce);

        animation_bounce.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                imageView.startAnimation(animation_bounce);
                imageView.startAnimation(animation_4);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
    }

}
