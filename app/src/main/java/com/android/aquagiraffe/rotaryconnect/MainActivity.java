package com.android.aquagiraffe.rotaryconnect;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;


public class MainActivity extends AppCompatActivity {
    EditText edtMobile, edtPassword;
    String strMobile, strPassword, strStatus, strRID, strRCTP_ID, strNAME, strDOB, strUserType;
    private TextView txtRegister;
    private Button btnLogin;
    ProgressDialog progressDialog;
    EditText edtPopPassword, edtPopCnfrmPassword;
    String strPopPassword, strPopCnfrmPassword;
    Button btnChangePwd;
    Dialog dialog;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int RequestPermissionCode = 7;
    AlertDialogManager alert;
    SessionManager session;
    CheckBox mCbShowPwd;
    ImageView imgRotary;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));


        mCbShowPwd = (CheckBox) findViewById(R.id.cbShowPwd);
        session = new SessionManager(getApplicationContext());
        imgRotary = findViewById(R.id.imgRotary);


        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(3000);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        imgRotary.startAnimation(rotate);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(R.color.login);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.login)));


        mCbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mCbShowPwd.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
                }
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (CheckingPermissionIsEnabledOrNot()) {
                //Toast.makeText(this, "All Permission granted ", Toast.LENGTH_SHORT).show();
            } else {
                RequestMultiplePermission();
            }
        }


        boolean isAppInstalled = isAppInstalled(getApplicationContext(), "com.app.clubfinder");

        Toast.makeText(this, String.valueOf(isAppInstalled), Toast.LENGTH_SHORT).show();


        alert = new AlertDialogManager();

        edtMobile = findViewById(R.id.edtMobile);
        edtPassword = findViewById(R.id.edtPassword);
        txtRegister = findViewById(R.id.txtRegister1);
        btnLogin = findViewById(R.id.btnLogin);


        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                strMobile = edtMobile.getText().toString().trim();
                strPassword = edtPassword.getText().toString().trim();

                if (strMobile.trim().length() > 0 && strPassword.trim().length() > 0) {

                    if (strMobile.length() == 10 && strPassword.equalsIgnoreCase("Pass@123")) {
                        validateAsync validate = new validateAsync();
                        validate.execute();
                    } else if (strMobile.length() == 10 && strPassword.length() == 6) { //actual 8 testing 6
                    /*    loginAsync login = new loginAsync();
                        login.execute();*/

                        lofinAsync2 lofin2 = new lofinAsync2();
                        lofin2.execute();


                    } else {
                        Toast.makeText(MainActivity.this, "Please enter correct username & password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    alert.showAlertDialog(MainActivity.this, "Login failed..", "Please enter username and password", false);
                }

                if (isConnected()) {
                    Toast.makeText(getApplicationContext(), "Internet Connected", Toast.LENGTH_LONG).show();
                } else {
                    alert.InternateConnection(MainActivity.this, "Internet Connection Lost", "Please check your internate connection", false);
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
        System.exit(0);

    }


    private void RequestMultiplePermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {READ_PHONE_STATE, READ_EXTERNAL_STORAGE, CAMERA, WRITE_EXTERNAL_STORAGE}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean ReadphonePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean CameraPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadWriteable = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (ReadphonePermission && ReadExternalStorage && CameraPermission && ReadWriteable) {
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
        int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissoonResult == PackageManager.PERMISSION_GRANTED &&
                ForthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    class changeAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
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
                        .appendQueryParameter("PASSWORD", strPopPassword);

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
                    Toast.makeText(MainActivity.this, "Password has been changed, kindly login with new password", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            dialog.getWindow().getAttributes().windowAnimations = R.style.popup_window_close_animation_phone;
            dialog.dismiss();

        }
    }

    class validateAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection conn;
            URL url;
            try {
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx/Login");

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
                        .appendQueryParameter("PASSWORD", strPassword);

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
                strStatus = jsonObject.getString("Status");

                if (strStatus.equalsIgnoreCase("3")) {

                    dialog = new Dialog(MainActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.popup_window_animation_phone;
                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(
                                    android.graphics.Color.TRANSPARENT
                            )

                    );
                    dialog.setContentView(R.layout.popup_change_pwd);
                    edtPopPassword = (EditText) dialog.findViewById(R.id.edtPassword);
                    edtPopCnfrmPassword = (EditText) dialog.findViewById(R.id.edtCnfrmPassword);

                    btnChangePwd = (Button) dialog.findViewById(R.id.btnSave);


                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    btnChangePwd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            strPopPassword = edtPopPassword.getText().toString().trim();
                            strPopCnfrmPassword = edtPopCnfrmPassword.getText().toString().trim();

                            if (strPopPassword.equalsIgnoreCase(strPopCnfrmPassword)) {
                                if (!strPopPassword.contains(" ") && !strPopCnfrmPassword.contains(" ")) {
                                    if (strPopPassword.length() == 8 && strPopCnfrmPassword.length() == 8) {

                                        changeAsync change = new changeAsync();
                                        change.execute();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Password should be of 8 characters", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(MainActivity.this, "Please do not leave any blank space in the password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Your passwords do not match", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    ImageButton closeButton = (ImageButton) dialog.findViewById(R.id.ib_close);
                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.getWindow().getAttributes().windowAnimations = R.style.popup_window_close_animation_phone;
                            dialog.dismiss();

                        }
                    });

                } else {
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }


    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    class loginAsync extends AsyncTask<String, String, String> {

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
            HttpURLConnection conn;
            URL url;
            try {
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx/Login");

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
                        .appendQueryParameter("PASSWORD", strPassword);

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
                strStatus = jsonObject.getString("Status");
                strRID = jsonObject.getString("ID");
                strRCTP_ID = jsonObject.getString("RCTP_ID");
                strNAME = jsonObject.getString("NAME");
                strUserType = jsonObject.getString("UserType");
                strDOB = jsonObject.getString("DOB");


                if (strStatus.equalsIgnoreCase("1")) {

                //    session.createLoginSession(strNAME, strRCTP_ID, strMobile, strRID, strUserType, strDOB);

                    Intent intent = new Intent(getApplicationContext(), NationalAnthem.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

   class lofinAsync2 extends AsyncTask<String,String,String>
   {

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
           HttpURLConnection conn;
           URL url;
           try {
               url = new URL("http://192.168.0.102/RCTP/v1/userLogin.php");

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
                       .appendQueryParameter("PASSWORD", strPassword);

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
               strStatus = jsonObject.getString("Status");
               strRID = jsonObject.getString("id");
               strRCTP_ID = jsonObject.getString("RCTP_ID");
               strNAME = jsonObject.getString("NAME");
       //        strUserType = jsonObject.getString("UserType");
               strDOB = jsonObject.getString("DOB");


               if (strStatus.equalsIgnoreCase("1")) {

                   session.createLoginSession(strNAME, strRCTP_ID, strMobile, strRID, strDOB);

                   Intent intent = new Intent(getApplicationContext(), NationalAnthem.class);
                   startActivity(intent);

               } else {
                   Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
               }


           } catch (JSONException e) {
               e.printStackTrace();
           }
       }
   }


}
