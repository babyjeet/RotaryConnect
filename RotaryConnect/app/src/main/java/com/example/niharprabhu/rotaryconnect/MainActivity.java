package com.android.aquagiraffe.rotaryconnect;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;

public class MainActivity extends AppCompatActivity {
    EditText edtMobile,edtPassword;
    String strMobile,strPassword;
    private TextView txtRegister;
    ProgressDialog progressDialog;
    private Button btnLogin;
    private static final String TAG = MainActivity.class.getSimpleName();
    String strFile,strURL = "https://rotarythanepremium.co.in/clubfinder.apk";
    public static final int RequestPermissionCode = 7;
    AlertDialogManager alert;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (CheckingPermissionIsEnabledOrNot()) {
                //Toast.makeText(this, "All Permission granted ", Toast.LENGTH_SHORT).show();
            } else {
                RequestMultiplePermission();
            }
        }



        boolean isAppInstalled = isAppInstalled(getApplicationContext(),"com.app.clubfinder");

        Toast.makeText(this, String.valueOf(isAppInstalled), Toast.LENGTH_SHORT).show();

       /* if (isAppInstalled == false){
            downloadInstallAsync download = new downloadInstallAsync();
            download.execute();

        }else {
            Toast.makeText(this, "The app is already installed", Toast.LENGTH_SHORT).show();
        }*/

        alert = new AlertDialogManager();

        edtMobile = (EditText) findViewById(R.id.edtMobile);
        edtPassword = (EditText) findViewById(R.id.edtPassword);;
        txtRegister = (TextView) findViewById(R.id.txtRegister1);
        btnLogin = (Button) findViewById(R.id.btnLogin);


        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strMobile = edtMobile.getText().toString().trim();
                strPassword = edtPassword.getText().toString().trim();

                if(strMobile.trim().length() > 0 && strPassword.trim().length() > 0){

                    if(strMobile.length() == 10){

                        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                        dialog.setTitle("Logging You In...");
                        dialog.setMessage("Please wait.");
                        dialog.setIndeterminate(true);
                        dialog.setCancelable(false);
                        dialog.show();

                        long delayInMillis = 2000;
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                dialog.dismiss();

                                session.createLoginSession("Sachin Nerurkar", "sachin5470@gmail.com");

                                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }
                        }, delayInMillis);

                    }else{
                        alert.showAlertDialog(MainActivity.this, "Login failed..", "Username/Password is incorrect", false);
                    }
                }else{
                    alert.showAlertDialog(MainActivity.this, "Login failed..", "Please enter username and password", false);
                }
                /*loginAsync login = new loginAsync();
                login.execute();*/

            }
        });
    }

    class loginAsync extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setTitle("Logging You In...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection conn ;
            URL url;
            try {
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx/Login");

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            }
            try {
                conn = (HttpURLConnection)url.openConnection();

                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("PKey", "RCTP_sb24_6012*")
                        .appendQueryParameter("MOBILENO", strMobile)
                        .appendQueryParameter("PASSWORD",strPassword);

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
                    return(result.toString());
                }else{

                    return("unsuccessful");
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

            Log.d("TAG",result);

        }
    }

    private void RequestMultiplePermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {READ_PHONE_STATE, READ_EXTERNAL_STORAGE, CAMERA}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean ReadphonePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean CameraPermission    = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (ReadphonePermission && ReadExternalStorage && CameraPermission) {
                        Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int ThirdPermissoonResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissoonResult == PackageManager.PERMISSION_GRANTED;
    }



    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @SuppressLint("StaticFieldLeak")
    class downloadInstallAsync extends AsyncTask<String,String,String> {
        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.progressDialog = new ProgressDialog(MainActivity.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            int count;
            try {
                URL url = new URL(strURL);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                fileName = strURL.substring(strURL.lastIndexOf("/") + 1);


                folder = Environment.getExternalStorageDirectory() + "/Download/" + fileName;

                OutputStream output = new FileOutputStream(folder);

                strFile = String.valueOf(output);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

                return "Downloaded at: " + folder;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            super.onProgressUpdate(progress);

            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);

            this.progressDialog.dismiss();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(folder)),
                    "application/vnd.android.package-archive");
            startActivity(intent);
            finish();
            // Display File path after downloading
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

}
