package com.android.aquagiraffe.rotaryconnect;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;

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
import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtMobile, edtOTP;
    private Button btnOTP, btnConfirm;
    String strMobile, strOTP, strRandom;
    ProgressDialog progressDialog;
    Toolbar toolbar;
    ImageView back;
    URL url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));

        edtMobile = (EditText) findViewById(R.id.edtMobile);
        edtOTP = (EditText) findViewById(R.id.edtOTP);
        btnOTP = (Button) findViewById(R.id.btnOTP);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);

        back = findViewById(R.id.imgback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btnOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strMobile = edtMobile.getText().toString().trim();

                if (strMobile.length() == 10) {

                    if (AppStatus.getInstance(getApplicationContext()).isOnline()) {

                    /*    validateAsync validate = new validateAsync();
                        validate.execute();*/

                        validateAsync2 validateAsyny2 = new validateAsync2();
                        validateAsyny2.execute();

                    } else {

                        Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Please re-check the mobile number", Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strMobile = edtMobile.getText().toString().trim();
                strOTP = edtOTP.getText().toString().trim();

                if (strMobile.length() > 0 && strOTP.length() > 0) {

                    if (strMobile.length() == 10 && strOTP.length() == 4 && strOTP.equalsIgnoreCase(strRandom)) {
                        final ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
                        dialog.setTitle("Loading...");
                        dialog.setMessage("Please wait.");
                        dialog.setIndeterminate(true);
                        dialog.setCancelable(false);
                        dialog.show();

                        long delayInMillis = 1000;
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                dialog.dismiss();

                                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                startActivity(intent);

                            }
                        }, delayInMillis);

                    }
                }
            }
        });
    }


    class validateAsync2 extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection conn;
            URL url;
            try {
                url = new URL("http://192.168.0.102/RCTP/v1/Api.php");

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            }
            try {
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("connection", "close");
                conn.setConnectTimeout(60000);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("RCTP_APIkey", Constants.P_KEY)
                        .appendQueryParameter("MOBILENO", strMobile)
                        .appendQueryParameter("PASSWORD", "123456");

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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                String strStatus = jsonObject.getString("Status");

                if (strStatus.equalsIgnoreCase("1")) {

                    Toast.makeText(RegisterActivity.this, "You are already a member of RCTP, kindly change your password and proceed", Toast.LENGTH_SHORT).show();

                } else {
                    final ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
                    dialog.setTitle("Processing...");
                    dialog.setMessage("Please wait.");
                    dialog.setIndeterminate(true);
                    dialog.setCancelable(false);
                    dialog.show();

                    long delayInMillis = 1000;
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            dialog.dismiss();

                            int randomPIN = (int) (Math.random() * 9000) + 1000;
                            strRandom = String.valueOf(randomPIN);

                            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)) {
                                NotificationManager mNotificationManager =
                                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    NotificationChannel channel = new NotificationChannel("default",
                                            "YOUR_CHANNEL_NAME",
                                            NotificationManager.IMPORTANCE_DEFAULT);
                                    channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
                                    mNotificationManager.createNotificationChannel(channel);
                                }
                                Notification.Builder notification = null; // clear notification after click
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    notification = new Notification.Builder(RegisterActivity.this)
                                            .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                                            .setContentTitle("Your OTP") // title for notification
                                            .setContentText("Your OTP for Registration is " + strRandom)// message for notification
                                            .setChannelId("default")     // set alarm sound for notification
                                            .setAutoCancel(true);
                                }

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                PendingIntent pi = PendingIntent.getActivity(RegisterActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                notification.setContentIntent(pi);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    mNotificationManager.notify(0, notification.build());
                                }
                            } else {
                                NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(RegisterActivity.this)
                                        .setSmallIcon(R.drawable.logo)
                                        .setContentTitle("Your OTP")
                                        .setContentText("Your OTP for Registration is " + strRandom);

                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(1, mBuilder.build());

                            }


                        }
                    }, delayInMillis);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class validateAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection conn;
            URL url;
            try {
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx/WS_Change_Password");

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            }
            try {
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("connection", "close");
                conn.setConnectTimeout(60000);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("RCTP_APIkey", Constants.P_KEY)
                        .appendQueryParameter("MOBILENO", strMobile)
                        .appendQueryParameter("PASSWORD", "123456");

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
            progressDialog.dismiss();

            try {
                JSONObject jsonObject = new JSONObject(result);
                String strStatus = jsonObject.getString("Status");

                if (strStatus.equalsIgnoreCase("1")) {

                    Toast.makeText(RegisterActivity.this, "You are already a member of RCTP, kindly change your password and proceed", Toast.LENGTH_SHORT).show();

                } else {
                    final ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
                    dialog.setTitle("Processing...");
                    dialog.setMessage("Please wait.");
                    dialog.setIndeterminate(true);
                    dialog.setCancelable(false);
                    dialog.show();

                    long delayInMillis = 1000;
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            dialog.dismiss();

                            int randomPIN = (int) (Math.random() * 9000) + 1000;
                            strRandom = String.valueOf(randomPIN);

                            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)) {
                                NotificationManager mNotificationManager =
                                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    NotificationChannel channel = new NotificationChannel("default",
                                            "YOUR_CHANNEL_NAME",
                                            NotificationManager.IMPORTANCE_DEFAULT);
                                    channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
                                    mNotificationManager.createNotificationChannel(channel);
                                }
                                Notification.Builder notification = null; // clear notification after click
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    notification = new Notification.Builder(RegisterActivity.this)
                                            .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                                            .setContentTitle("Your OTP") // title for notification
                                            .setContentText("Your OTP for Registration is " + strRandom)// message for notification
                                            .setChannelId("default")     // set alarm sound for notification
                                            .setAutoCancel(true);
                                }

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                PendingIntent pi = PendingIntent.getActivity(RegisterActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                notification.setContentIntent(pi);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    mNotificationManager.notify(0, notification.build());
                                }
                            } else {
                                NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(RegisterActivity.this)
                                        .setSmallIcon(R.drawable.logo)
                                        .setContentTitle("Your OTP")
                                        .setContentText("Your OTP for Registration is " + strRandom);

                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(1, mBuilder.build());

                            }


                        }
                    }, delayInMillis);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
