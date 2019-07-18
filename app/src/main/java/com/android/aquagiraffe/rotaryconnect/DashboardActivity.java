package com.android.aquagiraffe.rotaryconnect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.aquagiraffe.rotaryconnect.adapter.ViewerPagerAdapter;
import com.android.aquagiraffe.rotaryconnect.model.NotificationModel;
import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {
    private GridView gridView;
    AlertDialogManager alert = new AlertDialogManager();
    private TextView txtName, txtWelcome;
    DotsIndicator dotsIndicator;
    SessionManager session;
    ProgressDialog progressDialog;
    String strStatus, strName, strRCTPID, strMobile, strRID, strDOB, strResult;
    HashMap<String, String> hashMap = new HashMap<>();
    Toolbar toolbar;
    SharedPreferences prefs = null;
    Boolean Flag = false;
    int count = 0;
    TextView commit_count, txtAppbar;
    String NAME, strDate, PROFILEPHOTO, ANNIVERSARYDATE, StartDateTime, Reason_Celebrate,ReasonCelebrate,TittleOfEvent,EventDateTime,profile_photo;
    ArrayList<HashMap<String, String>> birthdayList;
    private static final String PREF_NAME = "AndroidHivePref";
    ArrayList<NotificationModel> notofication = new ArrayList<>();
    private Typeface typeface, font_awsome;
    ViewPager mViewPager;
    String[] strArr;
    WebView webview;
    String profile_url = "http://192.168.0.102/RCTP/v1/profiledetails.php";

    ArrayList<String> reasonArrayList = new ArrayList<>();
    ArrayList<HashMap<String, String>> list;
    String[] web = {
            "Profile", "Events", "Gallery",
            "Announcements", "Newsletters", "Rotary Library",
            "Find Rotary Club", "Rotary International", "Find Us",
            "Directory", "Past Presidents", "Board of Directors",
    };
    int[] imageId =
            {
                    R.string.icon_user, R.string.icon_event, R.string.icon_gallary,
                    R.string.icon_anounsement, R.string.icon_news, R.string.icon_library_rotary,
                    R.string.icon_find_us, R.string.icon_international, R.string.icon_map,
                    R.string.icon_directory, R.string.icon_past_pre, R.string.icon_director
            };
    String[] sliderStringId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

      /*  birthDayAsync birthDayAsync = new birthDayAsync();
        birthDayAsync.execute();*/

        VolleyBirthdayList();

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));

        typeface = Typeface.createFromAsset(getAssets(), "RobotoBold.ttf");
        webview = (WebView) findViewById(R.id.webview);
        font_awsome = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        NotificationAsync notificationAsync = new NotificationAsync();
        notificationAsync.execute();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        txtWelcome = findViewById(R.id.txtWelcome);
        txtName = findViewById(R.id.txtName);
        txtAppbar = findViewById(R.id.txtAppbar);
        dotsIndicator = (DotsIndicator) findViewById(R.id.dots_indicator);

         mViewPager = (ViewPager) findViewById(R.id.viewPage);

        session = new SessionManager(getApplicationContext());
        txtName = findViewById(R.id.txtUser);

        hashMap = session.getUserDetails();
        strName = hashMap.get(SessionManager.KEY_NAME);
        strRCTPID = hashMap.get(SessionManager.KEY_EMAIL);
        strMobile = hashMap.get(SessionManager.KEY_MOBILE);
        strRID = hashMap.get(SessionManager.KEY_RID);
        strDOB = hashMap.get(SessionManager.KEY_DOB);
      //  strDOB = strDOB.substring(0, Math.min(strDOB.length(), 5));
        Flag = Boolean.valueOf(hashMap.get(SessionManager.KEY_FLAG));
        txtName.setText(strName);
        txtName.setTypeface(typeface);
        txtWelcome.setTypeface(Typeface.createFromAsset(getAssets(), "RobotoMedium.ttf"));
        txtAppbar.setTypeface(Typeface.createFromAsset(getAssets(), "RobotoMedium.ttf"));
        ImageAdapter adapter = new ImageAdapter(DashboardActivity.this, web, imageId);

        gridView = findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:

                        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {

                            /*profileAsync profileAsync = new profileAsync();
                            profileAsync.execute();*/

                            profileAsync2();

                        } else {
                            /*
                             Internet is NOT available, Toast It!
                             */
                            Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case 1:
                        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                            Intent i = new Intent(getApplicationContext(), ViewEvenetList.class);
                            startActivity(i);

                        } else {
                            /**
                             * Internet is NOT available, Toast It!
                             */
                            Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case 2:


                        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                            gallery gallery = new gallery();
                            gallery.execute();

                        } else {

                            Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case 3:

                        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {

                            Intent intent2 = new Intent(getApplicationContext(), AnnouncementsActivity.class);
                            startActivity(intent2);

                        } else {

                            Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case 4:

                        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {

                            Intent intent3 = new Intent(getApplicationContext(), NewsletterActivity.class);
                            startActivity(intent3);

                        } else {

                            Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case 5:

                        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {

                            Intent intent4 = new Intent(getApplicationContext(), RotaryLibraryActivity.class);
                            startActivity(intent4);

                        } else {

                            Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                        }


                        break;

                    case 6:
                        installFindRotaryClub();
                        break;

                    case 7:
                        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {

                            Intent intent = new Intent(DashboardActivity.this, RotaryInternational.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case 8:
                        Intent intent5 = new Intent(getApplicationContext(), FindUsActivity.class);
                        startActivity(intent5);
                        break;

                    case 9:
                        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                            EntriesAsync async = new EntriesAsync();
                            async.execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case 10:


                        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                            PastPresidence pastPresidence = new PastPresidence();
                            pastPresidence.execute();

                        } else {

                            Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case 11:


                        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                            Intent intent6 = new Intent(getApplicationContext(), BoardOfDirectorsActivity.class);
                            startActivity(intent6);

                        } else {

                            Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    default:
                        break;
                }
            }
        });

    }



    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onResume() {
        super.onResume();
        this.doubleBackToExitPressedOnce = false;

    }

    public void installFindRotaryClub() {
        boolean isInstalled = appInstalledOrNOt("com.app.clubfinder");
        if (isInstalled) {
            Intent launchIntent = getPackageManager()
                    .getLaunchIntentForPackage("com.app.clubfinder");
            startActivity(launchIntent);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.app.clubfinder"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private boolean appInstalledOrNOt(String uri) {
        PackageManager pm = getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }

        return installed;
    }

    class EntriesAsync extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(DashboardActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setTitle("Getting things ready for you...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn;
            URL url;
            try {
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx/WS_Directory_Member");

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
                        .appendQueryParameter("RCTP_APIkey", "RCTP_sb24_6012*");
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
        protected void onPostExecute(String s) {
            Log.d("Scene", s);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            SharedPreferences preferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("Result", s);
            edit.apply();

            Intent intent = new Intent(DashboardActivity.this, DirectoryActivity.class);
            startActivity(intent);
        }
    }

    class gallery extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(DashboardActivity.this);
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
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx/WS_Get_Gallry_Data");

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            }
            try {
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setConnectTimeout(60000);
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
                Toast.makeText(DashboardActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("gallery", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("result", s);
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
            startActivity(intent);
        }

    }

    class profileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(DashboardActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection conn;
            URL url;
            try {
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx/WS_Profile");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            }
            try {
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setConnectTimeout(60000);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("RCTP_APIkey", Constants.P_KEY)
                        .appendQueryParameter("MOBILENO", strMobile);

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
                //Toast.makeText(DashboardActivity.this, e1.toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DashboardActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (DashboardActivity.this.isDestroyed()) { // or call isFinishing() if min sdk version < 17
                    return;
                }
            }
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                strStatus = jsonObject.getString("Status");

                if (strStatus.equalsIgnoreCase("1")) {
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Result", result);
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), DashProfileActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(DashboardActivity.this, "Sorry! Something went wrong", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void profileAsync2() {
        StringRequest request = new StringRequest(Request.Method.POST, profile_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    strStatus = jsonObject.getString("Status");

                    if (strStatus.equalsIgnoreCase("1"))
                    {
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Result", s);
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), DashProfileActivity.class);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("MOBILENO",strMobile);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }





    class PastPresidence extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(DashboardActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setTitle("Getting things ready for you...");
            progressDialog.setCancelable(true);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn;
            URL url;
            try {
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx/WS_Past_President_List");

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
                // Open connection for sending data
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
        protected void onPostExecute(String s) {
            Log.d("Scene", s);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("past", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("s", s);
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), PastPresidentsActivity.class);
                startActivity(intent);

            }

        }
    }


    public class NotificationAsync extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection conn = null;
            URL url = null;

            try {
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx/WS_Birthday_List");

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

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("pref2", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("s", s);
            editor.apply();

            getNotification();
        }
    }

    public class birthDayAsync extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection conn = null;
            URL url = null;

            try {
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx/WS_Birthday_List");

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

            Log.d("result", s);
            getBirthdayInfo(s);

        }
    }


    private void VolleyBirthdayList() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(DashboardActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setTitle("Getting things ready for you...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();

        String url = "http://192.168.0.102/RCTP/v1/birthdayList.php";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                getBirthdayInfo(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(DashboardActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }



    private void getBirthdayInfo(String s) {
        list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray;


            jsonArray = jsonObject.getJSONArray("BirthdayListMembers");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                NAME = object.getString("NAME");
                Log.d("name", NAME);
                /*ReasonCelebrate = object.getString("Reason_Celebrate");
                Log.d("ReasonCelebrate",ReasonCelebrate);*/
                TittleOfEvent = object.getString("TittleOfEvent");
                EventDateTime = object.getString("EventDateTime");
                profile_photo = object.getString("PROFILEPHOTO");

                reasonArrayList.add(NAME);

                HashMap<String, String> hashMap = new HashMap<>();
               // hashMap.put("Reason_Celebrate",ReasonCelebrate);
                hashMap.put("NAME", NAME);
                hashMap.put("TittleOfEvent",TittleOfEvent);
                hashMap.put("EventDateTime",EventDateTime);
                hashMap.put("PROFILEPHOTO",profile_photo);

                list.add(hashMap);

                ViewerPagerAdapter viewerPagerAdapter = new ViewerPagerAdapter(this,reasonArrayList,new String[]{"NAME","TittleOfEvent,EventDateTime,PROFILEPHOTO"},hashMap,list);
                mViewPager.setAdapter(viewerPagerAdapter);
                dotsIndicator.setViewPager(mViewPager);
            }
            //notofication.add(notificationModel);


                    
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getNotification() {
        try {
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("pref2", Context.MODE_PRIVATE);
            String strResult = preferences.getString("s", "empty");

            birthdayList = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(strResult);
            JSONArray jsonArray;

            jsonArray = jsonObject.getJSONArray("BirthdayListMembers");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                NAME = object.getString("NAME");

                PROFILEPHOTO = object.getString("PROFILEPHOTO");

                Reason_Celebrate = object.getString("Reason_Celebrate");

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("NAME", String.valueOf(NAME));
                hashMap.put("PROFILEPHOTO", String.valueOf(PROFILEPHOTO));
                hashMap.put("Reason_Celebrate", String.valueOf(Reason_Celebrate));

                Log.d("Hashmap", String.valueOf(hashMap));
                birthdayList.add(hashMap);
//
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();

                String current_date = dateFormat.format(date);

                if (Reason_Celebrate.equals("ANNIVERSARY") || Reason_Celebrate.equals("Birthday") || Reason_Celebrate.equals("Event")) {
                    count++;
                    Log.d("count_of_reason", String.valueOf(count));

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_home).setVisible(false);
        MenuItem item_notification = menu.findItem(R.id.action_notification);
        menu.findItem(R.id.info).setVisible(true);
        menu.findItem(R.id.action_notification).setVisible(false);
        item_notification.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        MenuItemCompat.setActionView(item_notification, (R.layout.action_bar_notifitcation_icon));
        View v = MenuItemCompat.getActionView(item_notification);
        final ImageView imageView = v.findViewById(R.id.hotlist_bell);
        for (int i = 0; i < menu.size(); i++) {
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.GREEN), 0, spanString.length(), 0); //fix the color to white
            item_notification.setTitle(spanString);
        }

        final Animation animation_4 = AnimationUtils.loadAnimation(this, R.anim.anim_shake);

        imageView.setAnimation(animation_4);
        animation_4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imageView.startAnimation(animation_4);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //doIncrease();
        commit_count = v.findViewById(R.id.notification_count);
        commit_count.setText(String.valueOf(count));

        if (count == 0) {
            commit_count.setVisibility(View.INVISIBLE);
        } else {
            commit_count.setVisibility(View.VISIBLE);
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 0) {
                    commit_count.setText(String.valueOf(count));
                    Intent intent = new Intent(DashboardActivity.this, DisplayNotification.class);
                    intent.putExtra("BirthDayListExtra", birthdayList);
                    intent.putExtra("NAME", NAME);
                    intent.putExtra("PROFILEPHOTO", PROFILEPHOTO);
                    intent.putExtra("Reason_Celebrate", Reason_Celebrate);
                    startActivity(intent);
                    commit_count.setVisibility(View.INVISIBLE);
                    Toast.makeText(DashboardActivity.this, "Click " + count, Toast.LENGTH_SHORT).show();
                    // onStop();
                } else {
                    commit_count.setVisibility(View.INVISIBLE);
                    Toast.makeText(DashboardActivity.this, "No such Notifications", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {

            super.onBackPressed();
            Intent a = new Intent(DashboardActivity.this, NationalAnthem.class);
            startActivity(a);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                session.logoutUser();
                finish();
                break;

            case R.id.action_home:
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
                break;

            case R.id.flag:
                Intent Iflag = new Intent(getApplicationContext(), NationalAnthem.class);
                startActivity(Iflag);
        }
        return super.onOptionsItemSelected(item);
    }
}
