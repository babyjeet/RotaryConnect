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
import android.widget.TextView;

import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Dues extends AppCompatActivity {
    SessionManager session;
    String result,Semi_Annual_Due_Amount_Cal,Semi_Annual_Due_Amount_Actual,Rotary_Thane_Premium_Trust_Cal,Rotary_Thane_Premium_Trust_Actual,
            Total_Annual_Due_Amount,Spouse_Semi_Annual_Due_Amount_Cal,Spouse_Semi_Annual_Due_Amount_Actual,
            Spouse_Rotary_Thane_Premium_Trust_Cal,Spouse_Rotary_Thane_Premium_Trust_Actual,Spouse_Total_Annual_Due_Amount;
    TextView txtAmount,txtAmount_,txtamount2,txtamount200,txtamount_1,txtamount2_1,txtamount_3,txtamount20,txtamount20_1,txtamount200_;
    Toolbar toolbar;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dues);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        session = new SessionManager(getApplicationContext());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        back = findViewById(R.id.imgback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dues.this, NationalAnthem.class);
                startActivity(intent);
            }
        });

        txtAmount = findViewById(R.id.amount);
        txtAmount_ = findViewById(R.id.amount_);
        txtamount2 = findViewById(R.id.amount2);
        txtamount200 = findViewById(R.id.amount200);
        txtamount_1 = findViewById(R.id.amount_1);
        txtamount2_1 = findViewById(R.id.amount2_1);
        txtamount_3 = findViewById(R.id.amount_3);
        txtamount20 = findViewById(R.id.amount20);
        txtamount20_1 = findViewById(R.id.amount20_1);
        txtamount200_ = findViewById(R.id.amount200_);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("duefee",Context.MODE_PRIVATE);
        result = sharedPreferences.getString("result","empty");

        try {
            JSONObject object = new JSONObject(result);
            JSONArray jsonArray = object.getJSONArray("DueFeeList");


                for (int i =0;i<jsonArray.length();i++) {

                    JSONObject object2 = jsonArray.getJSONObject(i);

                    Semi_Annual_Due_Amount_Cal = object2.getString("Semi_Annual_Due_Amount_Cal");
                    txtAmount.setText(Semi_Annual_Due_Amount_Cal);

                    Semi_Annual_Due_Amount_Actual = object2.getString("Semi_Annual_Due_Amount_Actual");
                    txtamount2.setText(Semi_Annual_Due_Amount_Actual);

                    Rotary_Thane_Premium_Trust_Cal = object2.getString("Rotary_Thane_Premium_Trust_Cal");
                    txtAmount_.setText(Rotary_Thane_Premium_Trust_Cal);

                    Rotary_Thane_Premium_Trust_Actual = object2.getString("Rotary_Thane_Premium_Trust_Actual");
                    txtamount20.setText(Rotary_Thane_Premium_Trust_Actual);

                    Total_Annual_Due_Amount = object2.getString("Total_Annual_Due_Amount");
                    txtamount200.setText(Total_Annual_Due_Amount);

                    Spouse_Semi_Annual_Due_Amount_Cal = object2.getString("Spouse_Semi_Annual_Due_Amount_Cal");
                    txtamount_1.setText(Spouse_Semi_Annual_Due_Amount_Cal);

                    Spouse_Semi_Annual_Due_Amount_Actual = object2.getString("Spouse_Semi_Annual_Due_Amount_Actual");
                    txtamount2_1.setText(Spouse_Semi_Annual_Due_Amount_Actual);

                    Spouse_Rotary_Thane_Premium_Trust_Cal = object2.getString("Spouse_Rotary_Thane_Premium_Trust_Cal");
                    txtamount_3.setText(Spouse_Rotary_Thane_Premium_Trust_Cal);

                    Spouse_Rotary_Thane_Premium_Trust_Actual = object2.getString("Spouse_Rotary_Thane_Premium_Trust_Actual");
                    txtamount20_1.setText(Spouse_Rotary_Thane_Premium_Trust_Actual);

                    Spouse_Total_Annual_Due_Amount = object2.getString("Spouse_Total_Annual_Due_Amount");
                    txtamount200_.setText(Spouse_Total_Annual_Due_Amount);
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Dues.this, NationalAnthem.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                session.logoutUser();
                break;
            case R.id.action_home:
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
