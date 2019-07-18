package com.android.aquagiraffe.rotaryconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.android.aquagiraffe.rotaryconnect.adapter.GridImageAdapter;
import com.android.aquagiraffe.rotaryconnect.model.ImageModel;
import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class ImagesUpload extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SessionManager session;
    ArrayList<String> filePaths = new ArrayList<String>();
    ImageView imgdelete, back;
    TextView image_gallary;
    private Button btnSend;
    Spinner spinner;
    String imagename, strTitleOfEvent,strEventDate,newstrtitle,TempTitleOfEvent ;

    private final String KEY = "AKIAIHL5HIJLUSY7QQSQ";
    private final String SECRET = "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";
    private AmazonS3Client s3Client;
    private BasicAWSCredentials credentials;
    ProgressBar progressBar;

    File file;
    ProgressDialog progressDialog;
    TransferObserver uploadObserver;
    String eventTitle, strResult;

    ArrayList<String> NAMESARRAY = new ArrayList<>();
    private HashMap<String, String> hashMap = new HashMap<>();
    String strRCTPID, imgName ,valueReceived;
    private GridView gridView;

    ArrayList<ImageModel> imageModelArrayList;
    ArrayList<String> titleList,datelist;
    GridImageAdapter adapter;
    ImageModel imageModel;
    String Event_Name, list;
    String Event_Date;
    Toolbar toolbar;
    Typeface font_awsome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_upload);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
        font_awsome =Typeface.createFromAsset(getAssets(),"fontawesome-webfont.ttf");

        spinner = (Spinner) findViewById(R.id.spinner);
        imgdelete = findViewById(R.id.imgdelete);
        image_gallary = findViewById(R.id.imgUpload);
        gridView = findViewById(R.id.gridview);

        progressBar = findViewById(R.id.myProgressBar);
        titleList = new ArrayList<>();


        Bundle bundle = getIntent().getExtras();
        valueReceived = bundle .getString("strTitleOfEvent");


        AWSMobileClient.getInstance().initialize(this).execute();

        credentials = new BasicAWSCredentials(KEY, SECRET);
        s3Client = new AmazonS3Client(credentials);

        btnSend = findViewById(R.id.btnSend);

        session = new SessionManager(getApplicationContext());
        hashMap = session.getUserDetails();
        strRCTPID = hashMap.get(SessionManager.KEY_EMAIL);

        image_gallary.setTypeface(font_awsome);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        back = findViewById(R.id.imgback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImagesUpload.this, GalleryActivity.class);
                startActivity(intent);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                        Event_Name = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                        Toast.makeText(getApplicationContext(), Event_Name, Toast.LENGTH_LONG).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        titleList.add("select event");
        try {
            JSONObject jsonObject = new JSONObject(valueReceived);
            JSONArray jsonArray = jsonObject.getJSONArray("EventList");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                strTitleOfEvent = obj.getString("TittleOfEvent");
                strEventDate = obj.getString("EventDateTime");
                titleList.add(strTitleOfEvent);
            }
            spinner.setAdapter(new ArrayAdapter<String>(ImagesUpload.this, R.layout.support_simple_spinner_dropdown_item, titleList));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (filePaths.size() != 0)
                {
                    for (int i = 0; i < filePaths.size(); i++) {

                        uploadFile(String.valueOf(filePaths.get(i)));

                        File f = new File(filePaths.get(i));
                        imagename = f.getName();
                        NAMESARRAY.add(imagename);
                        list = Arrays.toString(NAMESARRAY.toArray()).replace("[", "").replace("]", "");
                    }

                    if (AppStatus.getInstance(getApplicationContext()).isOnline())
                    {

                        if (Event_Name.equalsIgnoreCase("select event"))
                        {
                            Toast.makeText(ImagesUpload.this, "Please select event", Toast.LENGTH_SHORT).show();
                        }else {
                            GalleryAsync async = new GalleryAsync();
                            async.execute();

                            updateListener();
                        }

                    }
                    else
                    {

                        Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(ImagesUpload.this, "select images first", Toast.LENGTH_SHORT).show();
                }


            }
        });


        credentials = new BasicAWSCredentials(KEY, SECRET);
        s3Client = new AmazonS3Client(credentials);

        session = new SessionManager(getApplicationContext());


        image_gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FilePickerBuilder.getInstance()
                        .setMaxCount(15)
                        .setSelectedFiles(filePaths)
                        .setActivityTheme(R.style.AppTheme)
                        .pickPhoto(ImagesUpload.this);


            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:

                if (resultCode == RESULT_OK && data != null) {
                    filePaths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);

                    Spacecraft s;

                    ArrayList<Spacecraft> spacecrafts = new ArrayList<>();
                    imageModelArrayList = new ArrayList<>();

                    try {
                        for (String path : filePaths) {
                            imageModel = new ImageModel(false);
                            path.substring(path.lastIndexOf("/"));
                            imageModel.setUri(Uri.fromFile(new File(path)));
                            imageModel.setPath(path);

                            File file = new File(path);
                            imgName = file.getName();
                            imgName = imgName.replace(" ", "");


                            s = new Spacecraft();
                            s.setName(path.substring(path.lastIndexOf("/") + 1));
                            s.setUri(Uri.fromFile(new File(path)));
                            spacecrafts.add(s);

                            imageModel.setName(file.getName().replace(" ", ""));
                            imageModel.setPath(file.getPath());
                            imageModelArrayList.add(imageModel);
                            }


                        adapter = new GridImageAdapter(this, imageModelArrayList, filePaths);
                        gridView.setAdapter(adapter);


                        Toast.makeText(ImagesUpload.this, "Total = " + String.valueOf(spacecrafts.size()), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        eventTitle = "Title name";
    }



    class GalleryAsync extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ImagesUpload.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setTitle("Getting things ready for you...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection conn = null;
            URL url = null;

            try {
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx/WS_Set_Gallary_Data");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);


                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("RCTP_APIkey", Constants.P_KEY)
                        .appendQueryParameter("E_ID", "12")
                        .appendQueryParameter("RCTP_ID", strRCTPID)
                        .appendQueryParameter("MOBILENO","")
                        .appendQueryParameter("Event_Title", Event_Name)
                        .appendQueryParameter("Event_Images",list)
                        .appendQueryParameter("Event_Date","");


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

            Log.d("result",s);
            Toast.makeText(ImagesUpload.this, s, Toast.LENGTH_SHORT).show();
        }
    }


    private void uploadFile(String Imagepath) {

        String imagename = Imagepath.substring(Imagepath.lastIndexOf("/") + 1);
        imagename = imagename.replaceAll(" ", "");

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();


        uploadObserver = transferUtility.upload("images/" + imagename, new File(Imagepath), CannedAccessControlList.PublicRead);


    }

    private void updateListener() {
        uploadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {

                if (TransferState.COMPLETED == state) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(ImagesUpload.this, "image upload", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();

                    Intent intent = new Intent(ImagesUpload.this,GalleryActivity.class);
                    startActivity(intent);

                }


            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(int id, Exception ex) {

                Toast.makeText(ImagesUpload.this, "error", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
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
