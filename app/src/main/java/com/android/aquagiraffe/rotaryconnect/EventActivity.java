package com.android.aquagiraffe.rotaryconnect;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class EventActivity extends AppCompatActivity {
    SessionManager session;
    Button btnNotification, btnSubmit;
    String[] listitem;
    boolean[] checkItem;
    ArrayList<Integer> mSelectedlist = new ArrayList<>();
    java.util.ArrayList<String> nameList;
    String strResult, getText, strName, strTitleEvent, strDateTime, strEndDate, strEndDateTime, strTravelTime, strMessage, strLocation, strNotification;
    ArrayList<String> list;
    ProgressDialog progressDialog;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm");
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;
    private LocationAddressResultReceiver addressResultReceiver;
    private TextView txtStartDate, txtStartTime, txtEndDate, txtEndTime, txtTravelTime;
    private Location currentLocation;
    private LocationCallback locationCallback;
    private EditText edtTitle, edtMessage;
    Spinner spnList;
    ImageView back;
    LinearLayout imgStartDatePicker, imgStartTimePicker, imgEndDatePicker, imgEndTimePicker;
    ImageView imgGps;
    private int StartmYear, StartmMonth, StartmDay, StartmHour, StartmMinute;
    private int EndmYear, EndmMonth, EndmDay, EndmHour, EndmMinute;
    String startDate, endDate, startTime, strRCTPID;
    long elapsedDays;
    EditText edtOptional;
    HashMap<String, String> hashMap = new HashMap<>();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));

        session = new SessionManager(getApplicationContext());

        hashMap = session.getUserDetails();
        strRCTPID = hashMap.get(SessionManager.KEY_EMAIL);

        btnNotification = findViewById(R.id.btnAlert);
        btnSubmit = findViewById(R.id.btnSubmit);
        spnList = findViewById(R.id.txtLocation1);
        edtTitle = findViewById(R.id.edtTitle);
        edtMessage = findViewById(R.id.edtmsg);
        edtOptional = findViewById(R.id.edtOptional);

        imgStartDatePicker = findViewById(R.id.linear);
        imgStartTimePicker = findViewById(R.id.linear2);
        imgEndDatePicker = findViewById(R.id.linear3);
        imgEndTimePicker = findViewById(R.id.linear4);

        txtStartDate = findViewById(R.id.txtDate);
        txtStartTime = findViewById(R.id.txtTime);
        txtEndDate = findViewById(R.id.txtDate2);
        txtEndTime = findViewById(R.id.txtTime2);
        txtTravelTime = findViewById(R.id.traveltime);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        back = findViewById(R.id.imgback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, ViewEvenetList.class);
                startActivity(intent);
            }
        });


        imgGps = findViewById(R.id.imgGps);

        imgGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addressResultReceiver = new LocationAddressResultReceiver(new Handler());


                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(EventActivity.this);

                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        currentLocation = locationResult.getLocations().get(0);
                        getAddress();
                    }

                };
                startLocationUpdates();

            }

        });

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(EventActivity.this, R.array.spnMaplist, android.R.layout.simple_list_item_1);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnList.setAdapter(arrayAdapter);
        spnList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    edtOptional.setVisibility(View.GONE);
                } else if (position == 1) {
                    strLocation = "TMA, Hall, Near Dwarka Hotel, Wagle Estate, Thane West";
                    edtOptional.setVisibility(View.GONE);
                } else if (position == 2) {
                    strLocation = "Conference Hall,Smt.Savitridevi Thirani Vidyamandir Vartak Nagar Pokharan Road 1, Thane West";
                    edtOptional.setVisibility(View.GONE);
                } else if (position == 3) {
                    strLocation = "All Heavens Banquet Hall Flower Valley, Eastern Express Highway,Runwal Nagar, Thane West";
                    edtOptional.setVisibility(View.GONE);

                } else if (position == 4) {
                    edtOptional.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strTitleEvent = edtTitle.getText().toString();
                strMessage = edtMessage.getText().toString();
                strTravelTime = txtTravelTime.getText().toString();
                strDateTime = txtStartDate.getText().toString();
                strEndDate = txtEndTime.getText().toString();
                if (strLocation == null) {
                    strLocation = edtOptional.getText().toString();
                }

                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    if (!strTitleEvent.equalsIgnoreCase("") && strDateTime != null && strEndDateTime != null && strTravelTime != null && !strMessage.equalsIgnoreCase("") && !strLocation.equalsIgnoreCase("") /*&& strNotification != null*/) {
                       /* DataAsync dataAsync = new DataAsync();
                        dataAsync.execute();*/

                        DataAsync2 dataAsync2 = new DataAsync2();
                        dataAsync2.execute();

                    } else {
                        Toast.makeText(EventActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        imgStartDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                StartmYear = c.get(Calendar.YEAR);
                StartmMonth = c.get(Calendar.MONTH);
                StartmDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EventActivity.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfmonth1) {

                        txtStartDate.setText(dayOfmonth1 + "/" + (monthOfYear + 1) + "/" + year);
                        startDate = txtStartDate.getText().toString();

                        Toast.makeText(EventActivity.this, startDate, Toast.LENGTH_SHORT).show();
                    }
                }, StartmYear, StartmMonth, StartmDay);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }


        });

        imgStartTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                StartmHour = calendar.get(Calendar.HOUR);
                StartmMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(EventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        String AM_PM;
                        if (hourOfDay < 12) {
                            AM_PM = "am";
                        } else {
                            AM_PM = "pm";
                        }
                        int hour = hourOfDay % 12;
                        txtStartTime.setText(String.format("%02d:%02d", hour == 0 ? 12 : hour,
                                minute, hourOfDay < 12 ? "am" : "pm") + AM_PM);

                        startTime = txtStartTime.getText().toString();
                    }
                }, StartmHour, StartmMinute, false);
                timePickerDialog.show();
            }
        });

        imgEndDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                EndmYear = c.get(Calendar.YEAR);
                EndmMonth = c.get(Calendar.MONTH);
                EndmDay = c.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog datePickerDialog = new DatePickerDialog(EventActivity.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfmonth) {
                        txtEndDate.setText(dayOfmonth + "/" + (monthOfYear + 1) + "/" + year);
                        endDate = txtEndDate.getText().toString().trim();
                        Toast.makeText(EventActivity.this, endDate, Toast.LENGTH_SHORT).show();

                        if (startDate != null && endDate != null)

                        {
                            try {
                                Date date1 = simpleDateFormat.parse(startDate);
                                Date date2 = simpleDateFormat.parse(endDate);

                                if (date2.getTime() > date1.getTime()) {
                                    printDifference(date1, date2);
                                }


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(EventActivity.this, "select start date first", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, EndmYear, EndmMonth, EndmDay);


                if (startDate != null) {
                    try {
                        Date date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(startDate);
                        long milliseconds = date.getTime();
                        datePickerDialog.getDatePicker().setMinDate(milliseconds);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    datePickerDialog.show();

                } else {
                    Toast.makeText(EventActivity.this, "select start date first", Toast.LENGTH_SHORT).show();
                }

            }


        });

        imgEndTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar calendar = Calendar.getInstance();
                EndmHour = calendar.get(Calendar.HOUR);
                EndmMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(EventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String AM_PM;

                        if (hourOfDay < 12) {
                            AM_PM = "am";
                        } else {
                            AM_PM = "pm";
                        }

                        int hour = hourOfDay % 12;

                        txtEndTime.setText(String.format("%02d:%02d", hour == 0 ? 12 : hour,
                                minute, hourOfDay < 12 ? "am" : "pm") + AM_PM);

                        strEndDateTime = txtEndTime.getText().toString();

                        Log.d("strEndDateTime", strEndDateTime);
                        if (startTime != null) {
                            try {
                                Date time1 = simpleDateFormat2.parse(startTime);
                                Date time2 = simpleDateFormat2.parse(strEndDateTime);

                                printTimeDifference(time1, time2);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(EventActivity.this, "Select Starting time first", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, EndmHour, EndmMinute, false);
                timePickerDialog.show();
            }
        });


        nameList = new ArrayList<>();
        list = new ArrayList<>();


        SharedPreferences preferences = getApplicationContext().getSharedPreferences("result", Context.MODE_PRIVATE);
        getText = preferences.getString("names", "empty");
        String[] parts = getText.split(","); // escape

        for (int i = 1; i < parts.length; i++) {
            nameList.add(parts[i]);
        }


        listitem = nameList.toArray(new String[nameList.size()]);
        checkItem = new boolean[listitem.length];

        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < checkItem.length; i++) {
                    checkItem[i] = true;
                    mSelectedlist.add(i);
                }

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(EventActivity.this);
                mBuilder.setTitle("Select Members from list");
                mBuilder.setMultiChoiceItems(listitem, checkItem, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                        if (isChecked) {
                            if (!mSelectedlist.contains(position)) {
                                mSelectedlist.add(position);
                            }
                        } else if (mSelectedlist.contains(position)) {
                            mSelectedlist.remove(Integer.valueOf(position));
                        }
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < mSelectedlist.size(); i++) {
                            item = item + listitem[mSelectedlist.get(i)];

                            if (i != mSelectedlist.size() - 1) {
                                item = item + ",";
                            }
                            strNotification = item;
                        }
                        mSelectedlist.clear();
                        //Toast.makeText(EventActivity.this, item, Toast.LENGTH_SHORT).show();
                    }
                });

                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkItem.length; i++) {
                            checkItem[i] = false;
                        }
                        mSelectedlist.clear();
                        dialogInterface.dismiss();
                    }
                });


                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }


    public void printDifference(Date startDate, Date endDate) {
        long different = endDate.getTime() - startDate.getTime();

        Log.d("startDate:", String.valueOf(startDate));
        Log.d("endDate :", String.valueOf(endDate));
        Log.d("different :", String.valueOf(different));

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        txtTravelTime.setText(elapsedDays + " Days ");
    }

    public void printTimeDifference(Date startTime, Date endTime) {
        //  DateFormat df = DateFormat.getInstance();
        long difference = endTime.getTime() - startTime.getTime();

        long diffMinutes = difference / (60 * 1000) % 60;
        long diffHours = difference / (60 * 60 * 1000) % 24;
        long diffDays = difference / (24 * 60 * 60 * 1000);

        txtTravelTime.setText(elapsedDays + " Day " + diffHours + " hours " + diffMinutes + " Minute");
        strTravelTime = txtTravelTime.getText().toString();
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(2000);
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void getAddress() {
        if (!Geocoder.isPresent()) {
            Toast.makeText(this, "Can't find current address,", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, GetAddressIntentServices.class);
        intent.putExtra("add_receiver", addressResultReceiver);
        intent.putExtra("add_location", currentLocation);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(this, "Location permission not granted, " +
                                    "restart the app if you want the feature",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }

    }


    private class LocationAddressResultReceiver extends ResultReceiver {
        public LocationAddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == 0) {
                Log.d("Address", "Location null retrying");
                getAddress();

            }
            if (resultCode == 1) {
                Toast.makeText(EventActivity.this, "Address not found,", Toast.LENGTH_SHORT).show();
            }

        }
    }


    class DataAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(EventActivity.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.setTitle("Getting things ready for you...");
                progressDialog.setCancelable(false);
            }

            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection conn;
            URL url;
            try {
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx/WS_Event_Management");

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
                        .appendQueryParameter("RCTP_APIkey", Constants.P_KEY)
                        .appendQueryParameter("RCTP_ID", strRCTPID)
                        .appendQueryParameter("TittleOfEvent", strTitleEvent)
                        .appendQueryParameter("EventDateTime", strDateTime)
                        .appendQueryParameter("EndDateTime", endDate)
                        .appendQueryParameter("TravelTime", strTravelTime)
                        .appendQueryParameter("champion_Id", "20")
                        .appendQueryParameter("message", strMessage)
                        .appendQueryParameter("Location", strLocation)
                        .appendQueryParameter("Notification_Data", strNotification);

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

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }


            try {
                JSONObject jsonObject = new JSONObject(result);
                String strStatus = jsonObject.getString("Status");

                if (strStatus.equalsIgnoreCase("1")) {

                    Intent intent = new Intent(EventActivity.this, ViewEvenetList.class);
                    startActivity(intent);

                } else if (strStatus.equalsIgnoreCase("0")) {
                    Toast.makeText(EventActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    class DataAsync2 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(EventActivity.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.setTitle("Getting things ready for you...");
                progressDialog.setCancelable(false);
            }

            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection conn;
            URL url;
            try {
                url = new URL("http://192.168.0.102/RCTP/v1/eventmanagement.php");

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
                        .appendQueryParameter("RCTP_APIkey", Constants.P_KEY)
                        .appendQueryParameter("RCTP_ID", strRCTPID)
                        .appendQueryParameter("TittleOfEvent", strTitleEvent)
                        .appendQueryParameter("EventDateTime", strDateTime)
                        .appendQueryParameter("EndDateTime", endDate)
                        .appendQueryParameter("TravelTime", strTravelTime)
                        .appendQueryParameter("message", strMessage)
                        .appendQueryParameter("Location", strLocation)
                        .appendQueryParameter("Notification_Data", strNotification);

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

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }


            try {
                JSONObject jsonObject = new JSONObject(result);
                String strStatus = jsonObject.getString("Status");

                if (strStatus.equalsIgnoreCase("1")) {

                 /*   Intent intent = new Intent(EventActivity.this, ViewEvenetList.class);
                    startActivity(intent);
                    */
                    Toast.makeText(EventActivity.this, "event details added successfully", Toast.LENGTH_SHORT).show();

                } else if (strStatus.equalsIgnoreCase("0")) {
                    Toast.makeText(EventActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EventActivity.this, ViewEvenetList.class);
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
