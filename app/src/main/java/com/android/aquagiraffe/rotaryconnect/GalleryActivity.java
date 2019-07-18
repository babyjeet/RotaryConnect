package com.android.aquagiraffe.rotaryconnect;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.aquagiraffe.rotaryconnect.adapter.RecyclerViewDataAdapter;
import com.android.aquagiraffe.rotaryconnect.model.SectionDataModel;
import com.android.aquagiraffe.rotaryconnect.model.SingleItemModel;
import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;


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
import java.util.Map;


@TargetApi(Build.VERSION_CODES.M)
@RequiresApi(api = Build.VERSION_CODES.M)

public class GalleryActivity extends AppCompatActivity implements RecyclerView.OnScrollChangeListener {
    SessionManager session;
    private ArrayList<SectionDataModel> allSampleData;
    private ImageView pic, back;
    private Context mContext;
    private Activity mActivity;
    RecyclerView recyclerView;
    String[] imagenameArray;
    private int requestCount = 1;
    RecyclerViewDataAdapter adapter;
    ProgressBar progressBar;
    private RequestQueue requestQueue;
    String strStatus, ISMORE;
    String imgNames, strEvent_Title, strDate, strPROFILEPHOTO, strName, strUserType, strTitleOfEvent;
    HashMap<String, String> hashMap = new HashMap<>();
    Toolbar toolbar;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        pic = findViewById(R.id.pic);
        session = new SessionManager(getApplicationContext());

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));

        back = findViewById(R.id.imgback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        hashMap = session.getUserDetails();

        strUserType = hashMap.get(SessionManager.USER_TYPE);



        if (strUserType.equalsIgnoreCase("0")) {

            pic.setVisibility(View.GONE);
        }

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    EventViewAsync viewAsync = new EventViewAsync();
                    viewAsync.execute();

                } else {
                    Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mContext = getApplicationContext();
        mActivity = GalleryActivity.this;

        allSampleData = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);

        getData();

        recyclerView.setOnScrollChangeListener(this);

        adapter = new RecyclerViewDataAdapter(allSampleData, this);
        recyclerView.setAdapter(adapter);

    }

    private StringRequest getAllPurchaseList(final int requestCount) {

        String GetPurchaseDetails = "http://106.201.232.22:85/RCTP/RCTP.asmx/GetGalleryData";
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);
        final Handler handler = new Handler();

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }


                handler.post(new Runnable() {
                    public void run() {

                        progressBar.setVisibility(View.INVISIBLE);
                        setProgressBarIndeterminateVisibility(true);
                    }
                });
            }
        }).start();

        StringRequest request = new StringRequest(Request.Method.POST, GetPurchaseDetails, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    strStatus = jsonObj.getString("Status");
                    ISMORE = jsonObj.getString("ISMORE");

                    select(response);

                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(GalleryActivity.this, "Please Check internet Connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("RCTP_APIkey", "RCTP_sb24_6012*");
                params.put("PageIndex", "" + requestCount + "");
                params.put("PageSize", "4");
                return params;
            }
        };
        return request;
    }

    private void getData() {
        requestQueue.add(getAllPurchaseList(requestCount));
        requestCount++;
    }

    private void select(String jsonresponse) throws JSONException {

        JSONObject jsonObj = new JSONObject(jsonresponse);
        JSONArray items = jsonObj.getJSONArray("Gallery");

        for (int i = 0; i < items.length(); i++) {
            JSONObject c = items.getJSONObject(i);

            SectionDataModel dm = new SectionDataModel();
            SingleItemModel singleItemModel;
            ArrayList<SingleItemModel> arrayItemModels = new ArrayList<>();

            imgNames = c.getString("Event_Images");
            imgNames = imgNames.replaceAll("\\s+", "");
            imagenameArray = imgNames.split(",");
            strEvent_Title = c.getString("Event_Title");
            strDate = c.getString("Event_Date");
            int index = strDate.indexOf(" ");
            strDate = strDate.substring(0, index);
            strPROFILEPHOTO = c.getString("PROFILEPHOTO");
            strName = c.getString("NAME");

            dm.setHeaderTitle(strEvent_Title);
            dm.setStrdate(strDate);
            dm.setStrImageName(strPROFILEPHOTO);


            for (int k = 0; k < imagenameArray.length; k++) {
                String imgName = imagenameArray[k];
                singleItemModel = new SingleItemModel();

                singleItemModel.setName(imgName);
                singleItemModel.setNameArray(imgNames);
                singleItemModel.setStrProfileName(strName);
                arrayItemModels.add(singleItemModel);
            }
            dm.setAllItemInSection(arrayItemModels);
            allSampleData.add(dm);
            adapter.notifyDataSetChanged();

        }
    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (isLastItemDisplaying(recyclerView)) {

            if (strStatus.equalsIgnoreCase("1") && ISMORE.equalsIgnoreCase("true")) {
                getData();
            } else {
                Toast.makeText(mContext, "all data loaded", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

        }
    }

    public class EventViewAsync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (progressDialog == null) {
                progressDialog = new ProgressDialog(GalleryActivity.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.setTitle("Getting things ready for you...");
                progressDialog.setCancelable(true);
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
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Intent intent = new Intent(GalleryActivity.this, ImagesUpload.class);
            intent.putExtra("strTitleOfEvent", s);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(GalleryActivity.this, DashboardActivity.class);
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
