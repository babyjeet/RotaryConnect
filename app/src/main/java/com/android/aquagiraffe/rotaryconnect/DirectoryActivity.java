package com.android.aquagiraffe.rotaryconnect;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.aquagiraffe.rotaryconnect.adapter.CurrentListAdapter;
import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DirectoryActivity extends AppCompatActivity
{
    ProgressDialog progressDialog;
    Button button;
    SessionManager session;
    String result,CLASSIFICATION,NAME,PROFILEPHOTO;
    ListView listView;
    ImageView back;
    int RCTP_ID;
    Toolbar toolbar;
    ArrayList<HashMap<String, String>> transactList;
    CurrentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        session = new SessionManager(getApplicationContext());

        listView = findViewById(R.id.listview);

        listView.setFooterDividersEnabled(true);
        listView.setHeaderDividersEnabled(true);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        back = findViewById(R.id.imgback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DirectoryActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });


        SharedPreferences preferences = getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        result = preferences.getString("Result", "empty");
        transactList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("DirectoryMembersList");

            for (int i =0;i<jsonArray.length();i++)
            {
                JSONObject object = jsonArray.getJSONObject(i);
                RCTP_ID = object.getInt("RCTP_ID");
                NAME = object.getString("NAME");
                CLASSIFICATION = object.getString("CLASSIFICATION");
                PROFILEPHOTO = object.getString("PROFILEPHOTO");

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("RCTP_ID", String.valueOf(RCTP_ID));
                hashMap.put("NAME", String.valueOf(NAME));
                hashMap.put("CLASSIFICATION", String.valueOf(CLASSIFICATION));
                hashMap.put("PROFILEPHOTO",String.valueOf(PROFILEPHOTO));
                transactList.add(hashMap);

                adapter = new CurrentListAdapter(this,hashMap,transactList, new String[]{"RCTP_ID","NAME","CLASSIFICATION","PROFILEPHOTO"},new int[]{R.id.RCTP_ID,R.id.NAME,R.id.CLASSIFICATION,R.id.PROFILEPHOTO});
                listView.setAdapter(adapter);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DirectoryActivity.this,DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
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
