package com.android.aquagiraffe.rotaryconnect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileImageActivity extends AppCompatActivity {
    String strMemberID,strName,strMobile,strAge,strDOB,strOrgnName,strClassification,strVocation,strSubVocation,imageString,strSpouseName,strSpouseNumber,path,
            strSpouseBirthday,strAnniversaryDate,strFinalKids,strPassword,strSpouseGender;
    SessionManager session;
    ProgressDialog progressDialog;
    private int GALLERY = 1, CAMERA = 2;
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] byteArray;
    private static final String IMAGE_DIRECTORY = "/ProfileImage";
    Button btnSubmit;
    CircleImageView crclImageUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image);

        session = new SessionManager(getApplicationContext());

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        crclImageUpload = (CircleImageView) findViewById(R.id.imgProfile);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setTitle(" Rotary Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        strMemberID = preferences.getString("Member","empty");
        strName = preferences.getString("Name","empty");
        strMobile = preferences.getString("Mobile","empty");
        strAge = preferences.getString("Age","empty");
        strDOB = preferences.getString("DOB","empty");
        strPassword = preferences.getString("Password","empty");
        strOrgnName = preferences.getString("OrgnName","empty");
        strClassification = preferences.getString("Classification","empty");
        strVocation = preferences.getString("Vocation","empty");
        strSubVocation = preferences.getString("SubVocation","empty");
        strSpouseName = preferences.getString("SpouseName","empty");
        strSpouseNumber = preferences.getString("SpouseNumber","empty");
        strSpouseGender = preferences.getString("SpouseGender","empty");
        strSpouseBirthday = preferences.getString("SpouseBday","empty");
        strAnniversaryDate = preferences.getString("AnniversaryDate","empty");


        crclImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitAsync submit = new submitAsync();
                submit.execute();
            }
        });

    }

   /* class submitAsync extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String date = new SimpleDateFormat("ddMMyyyyhhmmss").format(new Date());

            String url = "http://192.168.1.20:85/RCTP/RCTP.asmx";

            int intErr = 0;
            HttpResponse response;
            BufferedReader inBuffer = null;


            String result = null;

            try {
                HttpClient httpClient = new DefaultHttpClient();
                InputStream is = null;

                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("op", "registration"));
                nameValuePairs.add(new BasicNameValuePair("PKey", "RCTP_sb24_6012*"));
                nameValuePairs.add(new BasicNameValuePair("RID", ""));
                nameValuePairs.add(new BasicNameValuePair("RCTP_ID", "5767958"));
                nameValuePairs.add(new BasicNameValuePair("NAME", "Nihar"));
                nameValuePairs.add(new BasicNameValuePair("MOBILENO", "9821831167"));
                nameValuePairs.add(new BasicNameValuePair("DOB", ""));
                nameValuePairs.add(new BasicNameValuePair("AGE", "28"));
                nameValuePairs.add(new BasicNameValuePair("ORGANIZATION", "Aqua"));

                nameValuePairs.add(new BasicNameValuePair("CLASSIFICATION", "IT"));
                nameValuePairs.add(new BasicNameValuePair("VOCATION", "Software"));
                nameValuePairs.add(new BasicNameValuePair("SUBVOCATION", "Android"));
                nameValuePairs.add(new BasicNameValuePair("SPOUSENAME", ""));
                nameValuePairs.add(new BasicNameValuePair("SPOUSENUMEBR", ""));
                nameValuePairs.add(new BasicNameValuePair("SPOUSEGENDER", ""));
                nameValuePairs.add(new BasicNameValuePair("SPOUSEBIRTHDATE", ""));
                nameValuePairs.add(new BasicNameValuePair("ANNIVERSARYDATE", ""));
                nameValuePairs.add(new BasicNameValuePair("KIDS_DETAILS", ""));
                nameValuePairs.add(new BasicNameValuePair("PROFILEPHOTO", ""));
                nameValuePairs.add(new BasicNameValuePair("THUMBIMPRESSION", "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDABALDA4MChAODQ4SERATGCgaGBYWGDEjJR0oOjM9PDkzODdASFxOQERXRTc4UG1RV19iZ2hnPk1xeXBkeFxlZ2P/2wBDARESEhgVGC8aGi9jQjhCY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2P/wAARCAAyADIDASIAAhEBAxEB/8QAGgABAAMBAQEAAAAAAAAAAAAAAAUGBwQBAv/EADAQAAIBAwMDAQYFBQAAAAAAAAECAwAEEQUhMQYSUUETFCIyYYEjkbHR4jM1obLw/8QAFQEBAQAAAAAAAAAAAAAAAAAAAAH/xAAYEQEBAQEBAAAAAAAAAAAAAAAAASECMf/aAAwDAQACEQMRAD8A0CvKVVdd6ls0E1nDey28qsUeVISxGNiBuN/r+XoQHZqXVFlYyvAivcTJkEJsobwT+2cVES9etEPi0wZ8C4/hVdM2iqP7pP8Aaz/lTVUtY9PsmhX8VmlEj939RcjsYDOwIJxkA7HPGaC+aH1HZayO2LuiuBzE/J23IPqP24qcrEo5ZIJEmhkMbq2VZTuCK1rQL99S0W2vJVCPIp7gPIJBP3xmgk6UpQeVnEUUKXMkvtrc3D3HfGCpdo2R8hcZHzePUDbO+NHrOYpWWWVIy0gWf2zYtywWQP8AApI8lR6nIOBgjeW2eLzJbqK1q2SWGe5lubf3kESPGsfY5djhlOTyuNxzvkjknquLCW40hJolL21vao5kK7q5bBQb8YPdnB+XG29fXVN3FPbHvmLzShJJFEZUJMAVZTttgeh38nipvQT3dHamWOe23dR9B7PP6k0lt1FFMSeBWqdIMrdN2fagTAYYB8Owz9+fvVGvLTS00OOaK4U3JVCAMksx+ZT6AAHn6DzveOjCD0vZkHI+P/dqonKUpQKznWtJutJuZZfe4Yre4kPb3h2DYOVDYUjI5wfB2IzWjVz3VtDeW0lvcRiSKQYZT6/95oMlubee7Ajk1a2aPvL4PtMAn1+TPpx6Vbcpp3Rc7RyJIk0YgQHK+0J2ZgCAc4LbY4XPFRuqdH6hazO1mnvVvklSrDvUDGxG2TzxnOPTOKjmt9aazjtjZX7QREskXuzAAk5J435/XzQREiEAYdsknIxx4wfz/IVsGj2nuGk2tsVCtHGA4U5Hdy3+c1U+l+l7gXUd9qMTRLGQ8UZOGJ2ILeB9Ocjfbm90ClKUClKUClKUClKUClKUH//Z"));
                nameValuePairs.add(new BasicNameValuePair("PASSWORD", "nihar"));
                String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
                if (!url.endsWith("?"))
                    url += "?";
                url += paramString;
                Log.d("URL", url.toString());
                HttpPost request = new HttpPost(url);
                response = httpClient.execute(request);

                StatusLine statusLine = response.getStatusLine();

                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    result = out.toString();
                    out.close();
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }

            } catch (IOException e) {
                result = e.getMessage();
            } finally {
                if (inBuffer != null) {
                    try {
                        inBuffer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("Res",result);
        }
    }*/

    class submitAsync extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(ProfileImageActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setTitle("Registering You...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection conn ;
            URL url;
            try {
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx?op=RNEW");

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            }
            try {
                conn = (HttpURLConnection)url.openConnection();

                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("connection","close");
                conn.setConnectTimeout(60000);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("PKey", "RCTP_sb24_6012");
                        /*.appendQueryParameter("RID", "")
                        .appendQueryParameter("RCTP_ID", "8956745")
                        .appendQueryParameter("NAME", "Nihar")
                        .appendQueryParameter("MOBILENO", "9821831167");*/
                        /*.appendQueryParameter("DOB", "23/03/1990")
                        .appendQueryParameter("AGE", strAge)
                        .appendQueryParameter("ORGANIZATION", strOrgnName)
                        .appendQueryParameter("CLASSIFICATION", strClassification)
                        .appendQueryParameter("VOCATION", strVocation)
                        .appendQueryParameter("SUBVOCATION", strSubVocation)
                        .appendQueryParameter("SPOUSENAME", strSpouseName)
                        .appendQueryParameter("SPOUSENUMEBR", strSpouseNumber)
                        .appendQueryParameter("SPOUSEGENDER",strSpouseGender)
                        .appendQueryParameter("SPOUSEBIRTHDATE", strSpouseBirthday)
                        .appendQueryParameter("ANNIVERSARYDATE", strAnniversaryDate)
                        .appendQueryParameter("KIDS_DETAILS", strFinalKids)
                        .appendQueryParameter("PROFILEPHOTO", "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDABALDA4MChAODQ4SERATGCgaGBYWGDEjJR0oOjM9PDkzODdASFxOQERXRTc4UG1RV19iZ2hnPk1xeXBkeFxlZ2P/2wBDARESEhgVGC8aGi9jQjhCY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2P/wAARCAAyADIDASIAAhEBAxEB/8QAGgABAAMBAQEAAAAAAAAAAAAAAAUGBwQBAv/EADAQAAIBAwMDAQYFBQAAAAAAAAECAwAEEQUhMQYSUUETFCIyYYEjkbHR4jM1obLw/8QAFQEBAQAAAAAAAAAAAAAAAAAAAAH/xAAYEQEBAQEBAAAAAAAAAAAAAAAAASECMf/aAAwDAQACEQMRAD8A0CvKVVdd6ls0E1nDey28qsUeVISxGNiBuN/r+XoQHZqXVFlYyvAivcTJkEJsobwT+2cVES9etEPi0wZ8C4/hVdM2iqP7pP8Aaz/lTVUtY9PsmhX8VmlEj939RcjsYDOwIJxkA7HPGaC+aH1HZayO2LuiuBzE/J23IPqP24qcrEo5ZIJEmhkMbq2VZTuCK1rQL99S0W2vJVCPIp7gPIJBP3xmgk6UpQeVnEUUKXMkvtrc3D3HfGCpdo2R8hcZHzePUDbO+NHrOYpWWWVIy0gWf2zYtywWQP8AApI8lR6nIOBgjeW2eLzJbqK1q2SWGe5lubf3kESPGsfY5djhlOTyuNxzvkjknquLCW40hJolL21vao5kK7q5bBQb8YPdnB+XG29fXVN3FPbHvmLzShJJFEZUJMAVZTttgeh38nipvQT3dHamWOe23dR9B7PP6k0lt1FFMSeBWqdIMrdN2fagTAYYB8Owz9+fvVGvLTS00OOaK4U3JVCAMksx+ZT6AAHn6DzveOjCD0vZkHI+P/dqonKUpQKznWtJutJuZZfe4Yre4kPb3h2DYOVDYUjI5wfB2IzWjVz3VtDeW0lvcRiSKQYZT6/95oMlubee7Ajk1a2aPvL4PtMAn1+TPpx6Vbcpp3Rc7RyJIk0YgQHK+0J2ZgCAc4LbY4XPFRuqdH6hazO1mnvVvklSrDvUDGxG2TzxnOPTOKjmt9aazjtjZX7QREskXuzAAk5J435/XzQREiEAYdsknIxx4wfz/IVsGj2nuGk2tsVCtHGA4U5Hdy3+c1U+l+l7gXUd9qMTRLGQ8UZOGJ2ILeB9Ocjfbm90ClKUClKUClKUClKUClKUH//Z")
                        .appendQueryParameter("THUMBIMPRESSION", "")
                        .appendQueryParameter("PASSWORD", strPassword);
*/
                String query = builder.build().getEncodedQuery();
                Log.d("Query",query);
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
                Log.d("Resp", String.valueOf(response_code));


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

            Log.d("Data",result);

            Toast.makeText(ProfileImageActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }

    private void showPictureDialog() {
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
                        choosePhotoFromGallary();
                        break;
                    case 1:
                        takePhotoFromCamera();
                        break;
                }
            }
        });

        pictureDialog.show();
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, path);
        startActivityForResult(intent, CAMERA);
    }

    private void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    public void onActivityResult(int RC,int RQC, Intent I)
    {
        super.onActivityResult(RC, RQC, I);
        if (RC == this.RESULT_CANCELED) {
            return;
        }
        if (RC == GALLERY) {

            if (I != null) {
                Uri contentURI = I.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(ProfileImageActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();

                    Uri imageUri = I.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    crclImageUpload.setImageBitmap(selectedImage);

                    BitmapDrawable drawable = (BitmapDrawable) crclImageUpload.getDrawable();
                    Bitmap bmp = drawable.getBitmap();
                    Bitmap b = Bitmap.createScaledBitmap(bmp, 250, 250, false);
                    selectedImage = getResizedBitmap(selectedImage, 2000);

                    crclImageUpload.setImageBitmap(selectedImage);
                    FileOutputStream out = new FileOutputStream(path);
                    b.compress(Bitmap.CompressFormat.JPEG, 90, out);

                    File sd = Environment.getExternalStorageDirectory();
                    File image = new File(path);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bit = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);


                    byteArrayOutputStream = new ByteArrayOutputStream();
                    bit.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                    byteArray = byteArrayOutputStream.toByteArray();
                    String imageString1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    Log.d("Base", imageString1);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileImageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (RC == CAMERA && I != null) {

            Bitmap thumbnail = (Bitmap) I.getExtras().get("data");
            path = saveImage(thumbnail);
            thumbnail = Bitmap.createScaledBitmap(thumbnail, 650, 650, false);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

            File sd = Environment.getExternalStorageDirectory();
            File image = new File(path);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            thumbnail = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);


            byte[] byteArray;
            byteArray = byteArrayOutputStream.toByteArray();
            String imageString2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.d("TAGCAM", imageString2);


            Toast.makeText(ProfileImageActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap = Bitmap.createScaledBitmap(myBitmap, 650, 650, false);
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        byte[] imageBytes = bytes.toByteArray();


        imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);  // Compressed image Base64 String

        imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        Log.d("BASE", imageString);
        Log.d("length", String.valueOf(imageString.length()));
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        crclImageUpload.setImageBitmap(decodedImage);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();

            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }



}
