package com.android.aquagiraffe.rotaryconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.aquagiraffe.rotaryconnect.adapter.PastListAdapter;
import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PastPresidentsActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    SessionManager session;
    Toolbar toolbar;
    private ListView listView;
    private PastListAdapter adapter;
    String result,Name,Title,RCTP_ID,RCTP_Year,RCTP_Year_Name,President_Photo,President_logo;
    ArrayList<HashMap<String, String>> arrayList;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_presidents);

        session = new SessionManager(getApplicationContext());

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        back = findViewById(R.id.imgback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PastPresidentsActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        listView = findViewById(R.id.list);

        session = new SessionManager(getApplicationContext());

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("past", Context.MODE_PRIVATE);
        result = preferences.getString("s","empty");
        arrayList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("PresidentList");

            for (int i=0;i<jsonArray.length();i++)
            {
                JSONObject object = jsonArray.getJSONObject(i);
                Name = object.getString("Name");
                Title = object.getString("Title");
                RCTP_ID = object.getString("RCTP_ID");
                RCTP_Year = object.getString("RCTP_Year");
                RCTP_Year_Name = object.getString("RCTP_Year_Name");
                President_Photo = object.getString("President_Photo");
                President_logo = object.getString("President_logo");

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Name",Name);
                hashMap.put("Title",Title);
                hashMap.put("RCTP_ID",RCTP_ID);
                hashMap.put("RCTP_Year",RCTP_Year);
                hashMap.put("RCTP_Year_Name",RCTP_Year_Name);
                hashMap.put("President_Photo",President_Photo);
                hashMap.put("President_logo",President_logo);

                arrayList.add(hashMap);

                adapter = new PastListAdapter(this,hashMap,arrayList,new String[]{"Name","Title","RCTP_ID","RCTP_Year","RCTP_Year_Name","President_Photo","President_logo"},new int[]{R.id.thumbnail,R.id.title,R.id.member,R.id.year,R.id.yearImg});
                listView.setAdapter(adapter);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                session.logoutUser();
                break;
            case R.id.action_home:
                Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
