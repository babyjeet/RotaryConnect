package com.android.aquagiraffe.rotaryconnect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.aquagiraffe.rotaryconnect.adapter.KidAdapter;
import com.android.aquagiraffe.rotaryconnect.model.KidModel;
import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class DashProfileActivity extends AppCompatActivity {
    SessionManager session;
    ProgressDialog progressDialog;
    String strResult = "", strStatus = "", strError = "", strRCTP_ID = "", strMOBILENO = "", strNAME = "", strDOB = "", strAGE = "", strORGANIZATION = "", strCLASSIFICATION = "", strVOCATION = "", strSUBVOCATION = "", strSPOUSENAME = "", strSPOUSEGENDER = "", strSPOUSENUMEBR = "", strSPOUSEBIRTHDATE = "", strANNIVERSARYDATE = "", strKIDS_DETAILS = "", strPROFILEPHOTOUPDATE = "", strPASSWORD = "", strEmail_Id = "";
    int RCTP_ID, MOBILENO;
    TextView txtView, txtName, txtDOB, txtAge, txtORGANIZATION, txtRCTPID, txtMobileNo, txtClassification, txtVocation, txtSubVocation, txtSpouseName, txtSPOUSEGENDER, txtSPOUSENUMEBR, txtSPOUSEBIRTHDATE, txtANNIVERSARYDATE, txtKIDS_DETAILS, PROFILEPHOTOUPDATE, txtEmail;
    CircleImageView circleImageView;
    Context context;
    Typeface typeface;
    TextView address, blood, contact, doj, txtAge0, email, designation, manager, txtVocation0, txtSubVocation0, txtSpouseName0, txtSpouseGender, txtSpouseContact, txtSpouseBirthday, txtAnniversaryDate, txtKidsDetails;
    ImageView imgEdit, back;
    String strProfileStatus, strName, strRID, strRCTPID, strMobile;
    ListView listView;
    KidAdapter kidAdapter;
    KidModel kidModel;
    String Name, Contact, DOB2, Gend, id, TempKID_DETAILS, strUserType;
    ArrayList<KidModel> stringArrayList = new ArrayList<>();
    HashMap<String, String> hashMap = new HashMap<>();
    Toolbar toolbar;
    String profile_url = "http://192.168.0.102/RCTP/v1/profiledetails.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_profile);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));

        back = findViewById(R.id.imgback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashProfileActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        listView = findViewById(R.id.listView);
        txtName = (TextView) findViewById(R.id.txtProfilename);
        txtRCTPID = (TextView) findViewById(R.id.txtRctpid);
        txtMobileNo = (TextView) findViewById(R.id.txtContact);
        txtDOB = (TextView) findViewById(R.id.txtDOB);
        txtAge = (TextView) findViewById(R.id.txtAge);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtORGANIZATION = (TextView) findViewById(R.id.txtOrnName);
        txtClassification = (TextView) findViewById(R.id.txtClassification1);
        txtVocation = (TextView) findViewById(R.id.txtVocation1);
        txtSubVocation = (TextView) findViewById(R.id.txtSubVocation1);
        txtSpouseName = (TextView) findViewById(R.id.txtSpouseName1);
        txtSPOUSEGENDER = (TextView) findViewById(R.id.txtSpouseGender1);
        txtSPOUSENUMEBR = (TextView) findViewById(R.id.txtSpouseContact1);
        txtSPOUSEBIRTHDATE = (TextView) findViewById(R.id.txtSpouseBirthday1);
        txtANNIVERSARYDATE = (TextView) findViewById(R.id.txtAnniversaryDate1);
        txtKIDS_DETAILS = findViewById(R.id.txtKidsDetails1);
        circleImageView = findViewById(R.id.imgProfile);
        imgEdit = findViewById(R.id.imgEdit);
        txtSpouseContact = findViewById(R.id.txtSpouseContact);
        address = findViewById(R.id.address);
        blood = findViewById(R.id.blood);
        contact = findViewById(R.id.contact);
        doj = findViewById(R.id.doj);
        txtAge0 = findViewById(R.id.txtAge);
        email = findViewById(R.id.email);
        designation = findViewById(R.id.designation);
        manager = findViewById(R.id.manager);
        txtVocation0 = findViewById(R.id.txtVocation);
        txtSubVocation0 = findViewById(R.id.txtSubVocation);
        txtSpouseName0 = findViewById(R.id.txtSpouseName);
        txtSpouseGender = findViewById(R.id.txtSpouseGender);
        txtSpouseBirthday = findViewById(R.id.txtSpouseBirthday);
        txtAnniversaryDate = findViewById(R.id.txtAnniversaryDate);
        txtKidsDetails = findViewById(R.id.txtKidsDetails);

        session = new SessionManager(getApplicationContext());

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        strResult = preferences.getString("Result", "empty");

        try {
            JSONObject jsonObject = new JSONObject(strResult);
            /*strStatus = jsonObject.getString("Status");*/
          //  strError = jsonObject.getString("error");
            strRCTP_ID = jsonObject.getString("RCTP_ID");
            strMOBILENO = jsonObject.getString("MOBILENO");
            strNAME = jsonObject.getString("NAME");
            strDOB = jsonObject.getString("DOB");
            strAGE = jsonObject.getString("AGE");
            strORGANIZATION = jsonObject.getString("ORGANIZATION");
            strCLASSIFICATION = jsonObject.getString("CLASSIFICATION");
            strVOCATION = jsonObject.getString("VOCATION");
            strSUBVOCATION = jsonObject.getString("SUBVOCATION");
            strSPOUSENAME = jsonObject.getString("SPOUSENAME");
            strSPOUSEGENDER = jsonObject.getString("SPOUSEGENDER");
            strSPOUSENUMEBR = jsonObject.getString("SPOUSENUMBER");
            strSPOUSEBIRTHDATE = jsonObject.getString("SPOUSEBIRTHDATE");
            strANNIVERSARYDATE = jsonObject.getString("ANNIVERSARYDATE");
            strKIDS_DETAILS = jsonObject.getString("KIDS_DETAILS");
            strPROFILEPHOTOUPDATE = jsonObject.getString("PROFILEPHOTO");
            strEmail_Id = jsonObject.getString("Email_Id");


            HashMap<String, String> hashMap = new HashMap<>();

            hashMap = session.getUserDetails();
            strName = hashMap.get(SessionManager.KEY_NAME);
            strRCTPID = hashMap.get(SessionManager.KEY_EMAIL);
            strMobile = hashMap.get(SessionManager.KEY_MOBILE);
            strRID = hashMap.get(SessionManager.KEY_RID);
            strUserType = hashMap.get(SessionManager.USER_TYPE);



            String kidArray[] = strKIDS_DETAILS.split("##");


            for (int u = 0; u < kidArray.length; u++) {
                String temp = kidArray[u];
                String pop = temp.replace("##", " ").trim();
                String kidArray2[] = pop.split(",");


                try {

                    for (int o = 0; o < kidArray2.length; o++) {

                        kidModel = new KidModel();

                        Name = kidArray2[0];
                        Contact = kidArray2[1];
                        DOB2 = kidArray2[2];
                        Gend = kidArray2[3];


                        kidModel.setId(id);
                        kidModel.setName(Name);
                        kidModel.setContact(Contact);
                        kidModel.setDOB(DOB2);
                        kidModel.setGender(Gend);
                    }

                    if (TempKID_DETAILS == null) {
                        TempKID_DETAILS = id + "," + Name + "," + Contact + "," + DOB2 + "," + Gend;
                    } else {
                        TempKID_DETAILS = TempKID_DETAILS + "##" + id + "," + Name + "," + Contact + "," + DOB2 + "," + Gend;
                    }

                    stringArrayList.add(kidModel);
                    kidAdapter = new KidAdapter(this, stringArrayList);


                    listView.setAdapter(kidAdapter);
                } catch (ArrayIndexOutOfBoundsException exception) {
                } catch (Exception exception) {
                }
            }


            typeface = Typeface.createFromAsset(getAssets(), "Sans.ttf");


            txtName.setText(strNAME);
            txtRCTPID.setText(strRCTPID);
            txtMobileNo.setText(strMOBILENO);
            txtDOB.setText(strDOB);
            txtAge.setText(strAGE + " Years");
            txtEmail.setText(strEmail_Id);
            txtORGANIZATION.setText(strORGANIZATION);
            txtClassification.setText(strCLASSIFICATION);
            txtVocation.setText(strVOCATION);
            txtSubVocation.setText(strSUBVOCATION);
            txtSpouseName.setText(strSPOUSENAME);
            txtSPOUSEGENDER.setText(strSPOUSEGENDER);
            txtSPOUSENUMEBR.setText(strSPOUSENUMEBR);
            txtSPOUSEBIRTHDATE.setText(strSPOUSEBIRTHDATE);
            txtANNIVERSARYDATE.setText(strANNIVERSARYDATE);

            //  txtKIDS_DETAILS.setText(strKid);
            String originalString = txtKIDS_DETAILS.getText().toString();

            originalString = originalString.replaceAll("Name", " <font color='#3F51B5'>Name<</font>");
            originalString = originalString.replaceAll("Contact", "<font color='#3F51B5'>Contact</font>");
            originalString = originalString.replaceAll("Gender", "<font color='#3F51B5'>Gender</font>");
            originalString = originalString.replaceAll("DOB", "<font color='#3F51B5'>DOB</font>");
            originalString = originalString.replaceAll(",", "<br>");


            //txtView.setTypeface(typeface);
            txtKIDS_DETAILS.setText(Html.fromHtml(originalString));

            String url = "https://s3-ap-southeast-1.amazonaws.com/awsaquabucket/P_Rotary/Profile/" + strPROFILEPHOTOUPDATE;
            Log.d("url", url);

            Picasso.with(this)
                    .load(url)
                    .into(circleImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {

                        }
                    });


        } catch (JSONException e) {
            e.printStackTrace();
        }

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                   /* editAsync editAsync = new editAsync();
                    editAsync.execute();*/

                    profileAsync2();

                } else {
                    Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void profileAsync2() {
        StringRequest request = new StringRequest(Request.Method.POST, profile_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    strProfileStatus = jsonObject.getString("Status");
                    if (strProfileStatus.equalsIgnoreCase("1")) {
                        SharedPreferences preferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = preferences.edit();

                        edit.putString("NAME", jsonObject.getString("NAME"));
                        edit.putString("RCTP_ID", jsonObject.getString("RCTP_ID"));
                        edit.putString("MOBILENO", jsonObject.getString("MOBILENO"));
                        edit.putString("DOB", jsonObject.getString("DOB"));
                        edit.putString("AGE", jsonObject.getString("AGE"));
                        edit.putString("Email_Id", jsonObject.getString("Email_Id"));
                        edit.putString("ORGANIZATION", jsonObject.getString("ORGANIZATION"));
                        edit.putString("CLASSIFICATION", jsonObject.getString("CLASSIFICATION"));
                        edit.putString("VOCATION", jsonObject.getString("VOCATION"));
                        edit.putString("SUBVOCATION", jsonObject.getString("SUBVOCATION"));
                        edit.putString("SPOUSENAME", jsonObject.getString("SPOUSENAME"));
                        edit.putString("SPOUSEGENDER", jsonObject.getString("SPOUSEGENDER"));
                        edit.putString("SPOUSENUMEBR", jsonObject.getString("SPOUSENUMBER"));
                        edit.putString("SPOUSEBIRTHDATE", jsonObject.getString("SPOUSEBIRTHDATE"));
                        edit.putString("ANNIVERSARYDATE", jsonObject.getString("ANNIVERSARYDATE"));
                        edit.putString("KIDS_DETAILS", jsonObject.getString("KIDS_DETAILS"));
                        edit.putString("PROFILEPHOTOUPDATE", jsonObject.getString("PROFILEPHOTO"));
                        edit.apply();

                        Intent intent = new Intent(DashProfileActivity.this, ProfileEditActivity.class);
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


    class editAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(DashProfileActivity.this);
            progressDialog.setCancelable(false);
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
                conn.setRequestProperty("connection", "close");
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
                strProfileStatus = jsonObject.getString("Status");
                if (strProfileStatus.equalsIgnoreCase("1")) {
                    SharedPreferences preferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = preferences.edit();

                    edit.putString("NAME", jsonObject.getString("NAME"));
                    edit.putString("RCTP_ID", jsonObject.getString("RCTP_ID"));
                    edit.putString("MOBILENO", jsonObject.getString("MOBILENO"));
                    edit.putString("DOB", jsonObject.getString("DOB"));
                    edit.putString("AGE", jsonObject.getString("AGE"));
                    edit.putString("Email_Id", jsonObject.getString("Email_Id"));
                    edit.putString("ORGANIZATION", jsonObject.getString("ORGANIZATION"));
                    edit.putString("CLASSIFICATION", jsonObject.getString("CLASSIFICATION"));
                    edit.putString("VOCATION", jsonObject.getString("VOCATION"));
                    edit.putString("SUBVOCATION", jsonObject.getString("SUBVOCATION"));
                    edit.putString("SPOUSENAME", jsonObject.getString("SPOUSENAME"));
                    edit.putString("SPOUSEGENDER", jsonObject.getString("SPOUSEGENDER"));
                    edit.putString("SPOUSENUMEBR", jsonObject.getString("SPOUSENUMEBR"));
                    edit.putString("SPOUSEBIRTHDATE", jsonObject.getString("SPOUSEBIRTHDATE"));
                    edit.putString("ANNIVERSARYDATE", jsonObject.getString("ANNIVERSARYDATE"));
                    edit.putString("KIDS_DETAILS", jsonObject.getString("KIDS_DETAILS"));
                    edit.putString("PROFILEPHOTOUPDATE", jsonObject.getString("PROFILEPHOTOUPDATE"));
                    edit.apply();

                    Intent intent = new Intent(DashProfileActivity.this, ProfileEditActivity.class);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(DashProfileActivity.this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

