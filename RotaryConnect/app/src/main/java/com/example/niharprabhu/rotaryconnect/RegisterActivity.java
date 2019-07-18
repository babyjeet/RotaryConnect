package com.android.aquagiraffe.rotaryconnect;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtMobile,edtOTP;
    private Button btnOTP,btnConfirm;
    String strMobile,strOTP,strRandom;
    URL url = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setTitle(" Rotary Dashboard");
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        edtMobile = (EditText) findViewById(R.id.edtMobile);
        edtOTP = (EditText) findViewById(R.id.edtOTP);
        btnOTP = (Button) findViewById(R.id.btnOTP);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);

        btnOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strMobile = edtMobile.getText().toString().trim();

                if (strMobile.length() == 10) {

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

                            int randomPIN = (int)(Math.random()*9000)+1000;
                            strRandom = String.valueOf(randomPIN);

                            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)){
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
                                            .setContentText("Your OTP for Registration is "+strRandom)// message for notification
                                            .setChannelId("default")     // set alarm sound for notification
                                            .setAutoCancel(true);
                                }

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                PendingIntent pi = PendingIntent.getActivity(RegisterActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                notification.setContentIntent(pi);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    mNotificationManager.notify(0, notification.build());
                                }
                            }else {
                                NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(RegisterActivity.this)
                                        .setSmallIcon(R.drawable.logo)
                                        .setContentTitle("Your OTP")
                                        .setContentText("Your OTP for Registration is "+strRandom);

                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(1, mBuilder.build());

                            }


                        }
                    }, delayInMillis);
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Please re-check the mobile number", Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strMobile = edtMobile.getText().toString().trim();
                strOTP = edtOTP.getText().toString().trim();

                if (strMobile.length() > 0 && strOTP.length() > 0){

                    if (strMobile.length() == 10 && strOTP.length() == 4 && strOTP.equalsIgnoreCase(strRandom)){
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

                                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                                startActivity(intent);

                            }
                        }, delayInMillis);

                    }
                }
            }
        });
    }
}
