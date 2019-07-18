package com.android.aquagiraffe.rotaryconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FindUsActivity extends AppCompatActivity {

    SessionManager session;
    ListView listView;
    ListViewAdapter adapter;
    ArrayList<ListModel> listModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_us);

        session = new SessionManager(getApplicationContext());

        listView = (ListView) findViewById(R.id.listOfLocation);

        listModels = new ArrayList<>();
        listModels.add(new ListModel("TMA, Hall, Near Dwarka Hotel, Wagle Estate, Thane West"));
        listModels.add(new ListModel("Conference Hall,Smt.Savitridevi Thirani Vidyamandir Vartak Nagar Pokharan Road 1, Thane West"));


        adapter = new ListViewAdapter(listModels,FindUsActivity.this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                {
                    Intent intent = new Intent(FindUsActivity.this,MapView.class);
                    startActivity(intent);
                }else if(position == 1)
                {
                    Intent intent = new Intent(FindUsActivity.this,MapView2.class);
                    startActivity(intent);
                }
            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setTitle(" Find Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
