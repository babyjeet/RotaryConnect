package com.android.aquagiraffe.rotaryconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.android.aquagiraffe.rotaryconnect.adapter.BirthDayAdapter;
import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayNotification extends AppCompatActivity {
    HashMap<String,String> hashMap = new HashMap<>();
    SharedPreferences prefs = null;
    ArrayList<HashMap<String,String>> arrayList;
    private static final String PREF_NAME = "AndroidHivePref";
    TextView txt_name;
    ImageView img;
    ListView listView;
    String strReason_Celebrate, strName, strProfile;
    SessionManager sessionManager;
    Toolbar toolbar;
    ImageView back;

    BirthDayAdapter birthDayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notification);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        back = findViewById(R.id.imgback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayNotification.this,DashboardActivity.class);
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.list_of_birth_day);
        Intent intent = getIntent();
        arrayList = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("BirthDayListExtra");
        strName = intent.getStringExtra("NAME");
        strReason_Celebrate = intent.getStringExtra("Reason_Celebrate");
        //Log.d("strDate",strDate);
        strProfile = intent.getStringExtra("PROFILE_PHOTO");
        birthDayAdapter = new BirthDayAdapter(this,hashMap,arrayList,new String[]{"NAME","Reason_Celebrate","PROFILE_PHOTO"}, new int[]{R.id.str_name,R.id.txt_dob,R.id.img_bod});
        listView.setAdapter(birthDayAdapter);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.logout:
                sessionManager.logoutUser();
                finish();
                break;
            case R.id.action_home:
                Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
