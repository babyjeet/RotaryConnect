package com.android.aquagiraffe.rotaryconnect;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileImageActivity extends AppCompatActivity {

    String strMemberID, strName, strMobile, strAge, strDOB, strOrgnName, strClassification, strVocation, strSubVocation, imageString, strSpouseName, strSpouseNumber, path,
            strSpouseBirthday, strAnniversaryDate, strFinalKids, strPassword, strSpouseGender, strEmail, strStatus;
    SessionManager session;
    ProgressDialog progressDialog;
    private int CAMERA = 2;
    Button btnSubmit;
    CircleImageView crclImageUpload;
    private static final int CHOOSING_IMAGE_REQUEST = 1234;
    private Bitmap bitmap;
    Uri fileUri;
    String getname;
    TransferObserver uploadObserver;
    private final String KEY = "AKIAIHL5HIJLUSY7QQSQ";
    private final String SECRET = "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";
    private AmazonS3Client s3Client;
    private BasicAWSCredentials credentials;
    ProgressBar progressBar;
    String imagename;
    ImageView back;
    Toolbar toolbar;
    TextView txtProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

         AWSMobileClient.getInstance().initialize(this).execute();
        credentials = new BasicAWSCredentials(KEY, SECRET);
        s3Client = new AmazonS3Client(credentials);
        session = new SessionManager(getApplicationContext());

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        crclImageUpload = (CircleImageView) findViewById(R.id.imgProfile);
        progressBar = findViewById(R.id.pbLoading);
        txtProfile = findViewById(R.id.txtProfile);

        txtProfile.setTypeface(Typeface.createFromAsset(getAssets(), "RobotoMedium.ttf"));

        back = findViewById(R.id.imgback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileImageActivity.this, PersonalInfoActivity.class);
                startActivity(intent);
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("pref1", Context.MODE_PRIVATE);
        strMemberID = preferences.getString("RCTP_ID", "empty").trim();
        strName = preferences.getString("NAME", "empty").trim();
        strMobile = preferences.getString("MOBILENO", "empty").trim();
        strAge = preferences.getString("AGE", "empty").trim();
        strEmail = preferences.getString("Email", "empty").trim();
        strDOB = preferences.getString("DOB", "empty").trim();
        strPassword = preferences.getString("Password", "empty").trim();
        strOrgnName = preferences.getString("ORGANIZATION", "empty").trim();
        strClassification = preferences.getString("CLASSIFICATION", "empty").trim();
        strVocation = preferences.getString("VOCATION", "empty").trim();
        strSubVocation = preferences.getString("SUBVOCATION", "empty").trim();
        strSpouseName = preferences.getString("SPOUSENAME", " ").trim();
        strSpouseNumber = preferences.getString("SPOUSENUMEBR", " ").trim();
        strSpouseGender = preferences.getString("SPOUSEGENDER", " ").trim();
        strSpouseBirthday = preferences.getString("SPOUSEBIRTHDATE", " ").trim();
        strAnniversaryDate = preferences.getString("ANNIVERSARYDATE", " ").trim();
        strFinalKids = preferences.getString("KIDS", " ").trim();


        crclImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // showPictureDialog();


                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    showPictureDialog2();
                } else {

                    Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                  /*  submitAsync submit = new submitAsync();
                    submit.execute();*/

                    submitAsync2 submit2 = new submitAsync2();
                    submit2.execute();

                } else {


                    Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }




    private void showPictureDialog2() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogitems = {"Select Photo From Gallery",
                "Capture Photo From Camera"
        };
        pictureDialog.setItems(pictureDialogitems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        showChoosingFile();
                        break;
                    case 1:
                        photofromcamera();
                        break;
                }
            }
        });

        pictureDialog.show();
    }

    private void showChoosingFile() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), CHOOSING_IMAGE_REQUEST);
    }

    private void photofromcamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (bitmap != null)
        {
            bitmap.recycle();
        }

        if (requestCode == CHOOSING_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {

            try {
                fileUri = data.getData();

                getname = getFileName(fileUri);
                Uri imageUri = data.getData();

                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                bitmap = BitmapFactory.decodeStream(imageStream);

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                crclImageUpload.setImageBitmap(bitmap);

                Uri tempUri = getImageUri(getApplicationContext(),bitmap);
                File finalFile = new File(getRealPathFromURI(tempUri));
                uploadFile(String.valueOf(finalFile));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if (requestCode == CAMERA && resultCode == RESULT_OK && data != null)
        {

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            crclImageUpload.setImageBitmap(thumbnail);
            Uri tempUri = getImageUri(getApplicationContext(),thumbnail);
            File finalFile = new File(getRealPathFromURI(tempUri));
            uploadFile(String.valueOf(finalFile));

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }



    private void uploadFile(String Imagepath) {

        imagename = Imagepath.substring(Imagepath.lastIndexOf("/") + 1);

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();


        uploadObserver = transferUtility.upload("P_Rotary/Profile/" + imagename, new File(Imagepath), CannedAccessControlList.PublicRead);
        updateListener();

    }
    private void updateListener() {
        uploadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {

                if (TransferState.COMPLETED == state) {
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    Toast.makeText(ProfileImageActivity.this, "image upload", Toast.LENGTH_SHORT).show();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    Window MyWindow = getWindow();
                    WindowManager.LayoutParams winParams = MyWindow.getAttributes();
                    winParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
                    MyWindow.setAttributes(winParams);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                progressBar.setVisibility(ProgressBar.VISIBLE);
                Window window = getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.screenBrightness = 0.1f;
                getWindow().
                        setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        );


            }

            @Override
            public void onError(int id, Exception ex) {
                Toast.makeText(ProfileImageActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class submitAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(ProfileImageActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setTitle("Registering You...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection conn;
            URL url;
            try {
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx/WB_AWS_Registration");

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
                        .appendQueryParameter("RID", " ")
                        .appendQueryParameter("RCTP_APIkey", Constants.P_KEY)
                        .appendQueryParameter("RCTP_ID", strMemberID)
                        .appendQueryParameter("NAME", strName)
                        .appendQueryParameter("MOBILENO", strMobile)
                        .appendQueryParameter("DOB", strDOB)
                        .appendQueryParameter("AGE", strAge)
                        .appendQueryParameter("ORGANIZATION", strOrgnName)
                        .appendQueryParameter("CLASSIFICATION", strClassification)
                        .appendQueryParameter("VOCATION", strVocation)
                        .appendQueryParameter("SUBVOCATION", strSubVocation)
                        .appendQueryParameter("SPOUSENAME", strSpouseName)
                        .appendQueryParameter("SPOUSEGENDER", strSpouseGender)
                        .appendQueryParameter("SPOUSENUMEBR", strSpouseNumber)
                        .appendQueryParameter("SPOUSEBIRTHDATE", strSpouseBirthday)
                        .appendQueryParameter("ANNIVERSARYDATE", strAnniversaryDate)
                        .appendQueryParameter("KIDS_DETAILS", strFinalKids)
                        .appendQueryParameter("THUMBIMPRESSION", "")
                        .appendQueryParameter("PASSWORD", strPassword)
                        .appendQueryParameter("Email_Id", strEmail)
                        .appendQueryParameter("PROFILEPHOTO",imagename);

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
                Toast.makeText(ProfileImageActivity.this, e1.toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ProfileImageActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ProfileImageActivity.this, strStatus, Toast.LENGTH_SHORT).show();

                if (strStatus.equalsIgnoreCase("1")) {
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
                            notification = new Notification.Builder(ProfileImageActivity.this)
                                    .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                                    .setContentTitle("Registration Successful") // title for notification
                                    .setContentText("You are successfully registered with RCTP Connect. You can login now")// message for notification
                                    .setChannelId("default")     // set alarm sound for notification
                                    .setAutoCancel(true);
                        }

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(ProfileImageActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        notification.setContentIntent(pi);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mNotificationManager.notify(0, notification.build());
                        }
                    } else {
                        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(ProfileImageActivity.this)
                                .setSmallIcon(R.drawable.logo)
                                .setContentTitle("Registration Successful")
                                .setContentText("You are successfully registered with RCTP Connect. You can login now");

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(1, mBuilder.build());

                    }

                    Toast.makeText(ProfileImageActivity.this, "You have successfully registered, please login", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ProfileImageActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

   class submitAsync2 extends AsyncTask<String,String,String>
   {

       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           progressDialog = new ProgressDialog(ProfileImageActivity.this);
           progressDialog.setMessage("Please wait...");
           progressDialog.setTitle("Registering You...");
           progressDialog.setCancelable(false);
           progressDialog.show();

       }



       @Override
       protected String doInBackground(String... strings) {
           HttpURLConnection conn;
           URL url;
           try {
               url = new URL("http://192.168.0.102/RCTP/v1/userDb.php");

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
                       .appendQueryParameter("RCTP_ID", strMemberID)
                       .appendQueryParameter("NAME", strName)
                       .appendQueryParameter("MOBILENO", strMobile)
                       .appendQueryParameter("DOB", strDOB)
                       .appendQueryParameter("AGE", strAge)
                       .appendQueryParameter("ORGANIZATION", strOrgnName)
                       .appendQueryParameter("CLASSIFICATION", strClassification)
                       .appendQueryParameter("VOCATION", strVocation)
                       .appendQueryParameter("SUBVOCATION", strSubVocation)
                       .appendQueryParameter("SPOUSENAME", strSpouseName)
                       .appendQueryParameter("SPOUSEGENDER", strSpouseGender)
                       .appendQueryParameter("SPOUSENUMEBR", strSpouseNumber)
                       .appendQueryParameter("SPOUSEBIRTHDATE", strSpouseBirthday)
                       .appendQueryParameter("ANNIVERSARYDATE", strAnniversaryDate)
                       .appendQueryParameter("KIDS_DETAILS", strFinalKids)
                       .appendQueryParameter("PASSWORD", strPassword)
                       .appendQueryParameter("Email_Id", strEmail)
                       .appendQueryParameter("PROFILEPHOTO",imagename);

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
               Toast.makeText(ProfileImageActivity.this, e1.toString(), Toast.LENGTH_SHORT).show();
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
               Toast.makeText(ProfileImageActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
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
               Toast.makeText(ProfileImageActivity.this, strStatus, Toast.LENGTH_SHORT).show();

               if (strStatus.equalsIgnoreCase("1")) {
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
                           notification = new Notification.Builder(ProfileImageActivity.this)
                                   .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                                   .setContentTitle("Registration Successful") // title for notification
                                   .setContentText("You are successfully registered with RCTP Connect. You can login now")// message for notification
                                   .setChannelId("default")     // set alarm sound for notification
                                   .setAutoCancel(true);
                       }

                       Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                       PendingIntent pi = PendingIntent.getActivity(ProfileImageActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                       notification.setContentIntent(pi);
                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                           mNotificationManager.notify(0, notification.build());
                       }
                   } else {
                       NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(ProfileImageActivity.this)
                               .setSmallIcon(R.drawable.logo)
                               .setContentTitle("Registration Successful")
                               .setContentText("You are successfully registered with RCTP Connect. You can login now");

                       NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                       notificationManager.notify(1, mBuilder.build());

                   }

                   Toast.makeText(ProfileImageActivity.this, "You have successfully registered, please login", Toast.LENGTH_SHORT).show();

                   Intent intent = new Intent(ProfileImageActivity.this, MainActivity.class);
                   startActivity(intent);

               }
           } catch (JSONException e) {
               e.printStackTrace();
           }

       }
   }
}
