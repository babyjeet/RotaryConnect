package com.android.aquagiraffe.rotaryconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;

import java.util.ArrayList;

public class FindUsActivity extends AppCompatActivity {

    SessionManager session;
    ListView listView;
    ListViewAdapter adapter;
    ArrayList<ListModel> listModels;
    Toolbar toolbar;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_us);

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
                Intent intent = new Intent(FindUsActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });


        session = new SessionManager(getApplicationContext());

        listView = findViewById(R.id.listOfLocation);

        listModels = new ArrayList<>();
        listModels.add(new ListModel("TMA, Hall, Near Dwarka Hotel, Wagle Estate, Thane West"));
        listModels.add(new ListModel("Conference Hall,Smt.Savitridevi Thirani Vidyamandir Vartak Nagar Pokharan Road 1, Thane West"));
        listModels.add(new ListModel("All Heavens Banquet Hall Flower Valley, Eastern Express Hwy,Runwal Nagar, Thane West, Thane, Maharash 400601"));



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
                }else if (position == 2)
                {
                    Intent intent = new Intent(FindUsActivity.this,MapView3.class);
                    startActivity(intent);
                }
            }
        });

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
