package com.android.aquagiraffe.rotaryconnect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.aquagiraffe.rotaryconnect.adapter.ViewEventAdapter;
import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.HashMap;

public class ViewEvenetList extends AppCompatActivity {

    ImageView imgAdd, back;
    SessionManager session;
    ListView listView;
    String strResult = "", strTitleOfEvent = "", strStartDateTime = "", strEndDateTime = "", strTravelTime = "", strMessage = "", strLocation = "", strUserType, strName;
    ProgressDialog progressDialog;
    ArrayList<HashMap<String, String>> arrayList;
    ViewEventAdapter viewEventAdapter;
    HashMap<String, String> hashMap = new HashMap<>();
    Toolbar toolbar;
    private static final String JSON_URL = "http://192.168.0.102/RCTP/v1/showEvent.php";
    private static final String URL = "http://192.168.0.102/RCTP/v1/showNames.php";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_evenet_list);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));

     //   loadNames();
  /*      PersonalAsync personalAsync = new PersonalAsync();
        personalAsync.execute();*/
  
        loadList();

        listView = findViewById(R.id.listview);
        imgAdd = findViewById(R.id.addImg);

        back = findViewById(R.id.imgback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewEvenetList.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

       /* EventViewAsync eventViewAsync = new EventViewAsync();
        eventViewAsync.execute();*/

        session = new SessionManager(getApplicationContext());
        arrayList = new ArrayList<>();


        hashMap = session.getUserDetails();

       // strUserType = hashMap.get(SessionManager.USER_TYPE);
        /*
        if (strUserType.equalsIgnoreCase("0")) {
            imgAdd.setVisibility(View.GONE);
        }*/


        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    Intent intent = new Intent(ViewEvenetList.this, EventActivity.class);
                    startActivity(intent);
                } else {

                    Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void loadList() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ViewEvenetList.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setTitle("Getting things ready for you...");
            progressDialog.setCancelable(false);
        }

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                getData(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ViewEvenetList.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void loadNames()
    {
        if(progressDialog == null)
        {
           progressDialog = new ProgressDialog(ViewEvenetList.this);
           progressDialog.setMessage("Please wait...");
           progressDialog.setTitle("Getting things ready for you");
           progressDialog.setCancelable(false);
        }
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String strResult) {
                try {
                    JSONObject jsonObject = new JSONObject(strResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("NotificationList");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        strName = strName + "," + object.getString("NAME");
                    }

                    SharedPreferences preferences = getSharedPreferences("result", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("names", strName);
                    editor.apply();

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ViewEvenetList.this, "newtwork error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public class EventViewAsync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (progressDialog == null) {
                progressDialog = new ProgressDialog(ViewEvenetList.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.setTitle("Getting things ready for you...");
                progressDialog.setCancelable(false);
            }

            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection conn = null;
            URL url = null;

            try {
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx/WS_Event_List");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("RCTP_APIkey", Constants.P_KEY);


                String query = builder.build().getEncodedQuery();
                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();


            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                int response_code = conn.getResponseCode();

                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
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
                conn.disconnect();
            }

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //  progressDialog.dismiss();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            getData(s);

        }


    }

    public void getData(String s) {

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("EventList");
            HashMap<String, String> hashMap;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                strTitleOfEvent = obj.getString("TittleOfEvent");
                strStartDateTime = obj.getString("EventDateTime");
                strEndDateTime = obj.getString("EndDateTime");
                strTravelTime = obj.getString("TravelTime");
                strMessage = obj.getString("message");
                strLocation = obj.getString("Location");

                hashMap = new HashMap<>();
                hashMap.put("TittleOfEvent", strTitleOfEvent);
                hashMap.put("StartDateTime", strStartDateTime);
                hashMap.put("EndDateTime", strEndDateTime);
                hashMap.put("TravelTime", strTravelTime);
                hashMap.put("message", strMessage);
                hashMap.put("Location", strLocation);

                arrayList.add(hashMap);

                viewEventAdapter = new ViewEventAdapter(this, hashMap, arrayList, new String[]{"TittleOfEvent", "StartDateTime", "EndDateTime", "TravelTime", "message", "Location"}, new int[]{R.id.txtTitleOfEvent, R.id.txtStartDate1, R.id.txtEndDateTime, R.id.txtTravelTime, R.id.txtmessage
                });
                listView.setAdapter(viewEventAdapter);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    class PersonalAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ViewEvenetList.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setTitle("Getting things ready for you...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection conn;
            URL url;
            try {
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx/RCTP_Notification_List");

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            }
            try {
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("RCTP_APIkey", Constants.P_KEY);

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                e1.printStackTrace();
                return "exception";
            }

            try {
                int response_code = conn.getResponseCode();

                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
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
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            strResult = result;
            progressDialog.cancel();
            try {
                JSONObject jsonObject = new JSONObject(strResult);
                JSONArray jsonArray = jsonObject.getJSONArray("NotificationList");


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    strName = strName + "," + object.getString("First_name");
                }

                SharedPreferences preferences = getSharedPreferences("result", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("names", strName);
                editor.apply();

            } catch (JSONException e) {
                e.printStackTrace();

            }
            super.onPostExecute(result);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ViewEvenetList.this, DashboardActivity.class);
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
