package com.android.aquagiraffe.rotaryconnect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;
import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;
import com.nhaarman.supertooltips.ToolTipView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NationalAnthem extends AppCompatActivity {

    Button payfee;
    Button btnNext;
    ToolTipView myToolTipView;
    Toolbar toolbar;
    ProgressDialog progressDialog;
    ImageView anthem,play,pause;
    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_national_anthem);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        anthem = findViewById(R.id.anthem);
        payfee = findViewById(R.id.dues);
        btnNext = findViewById(R.id.btnNext);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);

        mediaPlayer= MediaPlayer.create(NationalAnthem.this,R.raw.jan);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mediaPlayer.start();

                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();

                pause.setVisibility(View.INVISIBLE);
                play.setVisibility(View.VISIBLE);
            }
        });


        ToolTipRelativeLayout toolTipRelativeLayout = (ToolTipRelativeLayout) findViewById(R.id.activity_main_tooltipRelativeLayout);

        ToolTip toolTip = new ToolTip()
                .withText("Members fees")
                .withColor(Color.WHITE)
                .withShadow();

        myToolTipView  = toolTipRelativeLayout.showToolTipForView(toolTip,findViewById(R.id.dues));

        btnNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mediaPlayer.stop();
                Intent intent = new Intent(NationalAnthem.this,DashboardActivity.class);
                startActivity(intent);

            }
        });

        payfee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    PaymentDues paymentDues = new PaymentDues();
                    paymentDues.execute();

                } else {

                    Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent a = new Intent(NationalAnthem.this, MainActivity.class);
        mediaPlayer.stop();
        startActivity(a);
    }

    class PaymentDues extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NationalAnthem.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setTitle("Getting things ready for you...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection connection;
            URL url;

            try {
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx/WS_Due_List");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            }

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("RCTP_APIkey", Constants.P_KEY);
                String query = builder.build().getEncodedQuery();
                // Open connection for sending data
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                connection.connect();

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            }


            try {
                int response_code = connection.getResponseCode();

                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return (result.toString());
                } else {

                    return ("unsuccessful");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                connection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("duefee",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("result",result);
            editor.apply();

            Intent intent = new Intent(NationalAnthem.this,Dues.class);
            startActivity(intent);

        }
    }
}
