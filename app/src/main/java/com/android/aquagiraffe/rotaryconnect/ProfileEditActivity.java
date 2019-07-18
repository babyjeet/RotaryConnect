package com.android.aquagiraffe.rotaryconnect;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEditActivity extends AppCompatActivity {
    EditText edtName, edtOrgnName, edtClassfication, edtVocation, edtSubVocation, edtMemberId,
            edtSpouseName, /*edtSpouseGender,*/
            edtSpouseContact, edtPopName, edtPopMobile;
    ProgressDialog progressDialog;
    SessionManager session;
    private int GALLERY = 1, CAMERA = 2;
    byte[] byteArray;
    int StartmYear3, StartmDay3, StartmMonth3, StartmYear4, StartmDay4, StartmMonth4, StartmYear5, StartmDay5, StartmMonth5;
    Spinner spnGender, spnGenderEdit;
    EditText edtPopKID;
    String originalString, strKidInfo, startDate3;
    Button btnPopKidUpdate, btnUpdate, btnSave;
    CircleImageView circleImageView;
    String strUpName, TempKID_DETAILS, strUpMemberId, strUpContact, strUpDOB, strUpAge, strUpEmail, strUpOrgnName, strUpClassfication, strUpVocation, strUpSubVocation, strUpSpouseName, strUpSpouseGender, strUpSpouseContact, strUpSpouseBirthday, strUpAnniversaryDate, strUpKID_DETAILS, strUpProfilePhoto;
    TextView txtKIDS_DETAILS, txtAdd, txtDOB, txtAge, txtSpouseDOB, txtAnniversary, txtPopGender, txtTemp, edtDob3, txtContact, txtEmail;
    String strName, strMemberId, strContact, strDOB, strAge, strEmail, strOrgnName, strClassfication, strVocation, strSubVocation, strSpouseName, strSpouseGender, strSpouseContact, strSpouseBirthday, strAnniversaryDate, strKID_DETAILS, strProfilePhoto, strGender;
    String strYear, strMonth, strDay, strCalAge, strPopGender, strPopName, strPopMobile, strFinalKids, strID;
    private static final int CHOOSING_IMAGE_REQUEST = 1234;
    private Bitmap bitmap;
    Uri fileUri;
    String getname;
    TransferObserver uploadObserver;
    private final String KEY = "AKIAIHL5HIJLUSY7QQSQ";
    private final String SECRET = "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";
    private AmazonS3Client s3Client;
    private BasicAWSCredentials credentials;
    private ProgressBar progressBar;
    private String imagename, strKID_DETAILS2;
    DatePickerDialog datePickerDialog;
    ImageButton imgbtnClose;
    Toolbar toolbar;
    String firstName, contact, DOB, strgen, optionStrSpouseGender, optionalgen, strStatus, strRID, strRCTPID, strMobile;
    HashMap<String, String> hashMap = new HashMap<>();
    ImageView back;
    KidAdapter kidAdapter;
    KidModel kidModel;
    ListView listView;
    String Name, Contact, DOB2, Gend, id, strEditfinal, strKidGender, strKidgenderOptional;
    ArrayList<KidModel> stringArrayList = new ArrayList<>();
    boolean ischanged = false;
    String NewKidData;
    boolean clicked = false;
    String profile_url = "http://192.168.0.102/RCTP/v1/profiledetails.php";


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));

        AWSMobileClient.getInstance().initialize(this).execute();
        credentials = new BasicAWSCredentials(KEY, SECRET);
        s3Client = new AmazonS3Client(credentials);

        progressBar = findViewById(R.id.pbLoading);
        spnGenderEdit = findViewById(R.id.edtSpouseGender1);
        session = new SessionManager(getApplicationContext());
        listView = findViewById(R.id.listView);

        hashMap = session.getUserDetails();
        strName = hashMap.get(SessionManager.KEY_NAME);
        strRCTPID = hashMap.get(SessionManager.KEY_EMAIL);
        strRID = hashMap.get(SessionManager.KEY_RID);
        strMobile = hashMap.get(SessionManager.KEY_MOBILE);

        back = findViewById(R.id.imgback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(ProfileEditActivity.this, DashProfileActivity.class);
                startActivity(intent);

            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        txtTemp = findViewById(R.id.temp);

        session = new SessionManager(getApplicationContext());

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        strName = preferences.getString("NAME", "empty");
        strMemberId = preferences.getString("RCTP_ID", "empty");
        strContact = preferences.getString("MOBILENO", "empty");
        strDOB = preferences.getString("DOB", "empty");
        strAge = preferences.getString("AGE", "empty");
        strEmail = preferences.getString("Email_Id", "empty");
        strOrgnName = preferences.getString("ORGANIZATION", "empty");
        strClassfication = preferences.getString("CLASSIFICATION", "empty");
        strVocation = preferences.getString("VOCATION", "empty");
        strSubVocation = preferences.getString("SUBVOCATION", "empty");
        strSpouseName = preferences.getString("SPOUSENAME", "empty");
        strSpouseGender = preferences.getString("SPOUSEGENDER", "empty");
        strSpouseContact = preferences.getString("SPOUSENUMEBR", "empty");
        strSpouseBirthday = preferences.getString("SPOUSEBIRTHDATE", "empty");
        strAnniversaryDate = preferences.getString("ANNIVERSARYDATE", "empty");
        strKID_DETAILS = preferences.getString("KIDS_DETAILS", "empty");
        strProfilePhoto = preferences.getString("PROFILEPHOTOUPDATE", "empty");

        edtName = (EditText) findViewById(R.id.edtName);
        edtOrgnName = (EditText) findViewById(R.id.edtOrnName);
        edtClassfication = (EditText) findViewById(R.id.edtClassification1);
        edtVocation = (EditText) findViewById(R.id.edtVocation1);
        edtSubVocation = (EditText) findViewById(R.id.edtSubVocation1);
        circleImageView = (CircleImageView) findViewById(R.id.imgProfile);
        txtAdd = (TextView) findViewById(R.id.txtEditKid);
        txtKIDS_DETAILS = (TextView) findViewById(R.id.txtKidsDetails1);
        txtDOB = (TextView) findViewById(R.id.txtDOB);
        txtAge = (TextView) findViewById(R.id.txtAge1);
        txtSpouseDOB = (TextView) findViewById(R.id.txtSpouseBirthday1);
        txtAnniversary = (TextView) findViewById(R.id.txtAnniversaryDate1);
        edtSpouseName = (EditText) findViewById(R.id.edtSpouseName1);
        edtSpouseContact = (EditText) findViewById(R.id.edtSpouseContact1);
        edtMemberId = findViewById(R.id.txtRotaryId);
        txtContact = findViewById(R.id.txtContact);
        txtEmail = findViewById(R.id.txtEmail);
        txtTemp = findViewById(R.id.temp);

        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        if (strRCTPID.equalsIgnoreCase("")) {
            edtMemberId.setEnabled(true);
        } else {
            edtMemberId.setEnabled(false);
        }

        if (strRCTPID.equalsIgnoreCase("0")) {

            final Dialog dialog;
            dialog = new Dialog(ProfileEditActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().getAttributes().windowAnimations = R.style.popup_window_animation_phone;
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(
                            android.graphics.Color.TRANSPARENT
                    )

            );
            dialog.setContentView(R.layout.alert_mumberid);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;

            TextView txtAlert = dialog.findViewById(R.id.txtAlert);
            Button close = dialog.findViewById(R.id.btnSave);
            ImageView imageView = dialog.findViewById(R.id.ib_close);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });


            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }


        String url = "https://s3-ap-southeast-1.amazonaws.com/awsaquabucket/P_Rotary/Profile/" + strProfilePhoto;
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

        if (strSpouseGender.equalsIgnoreCase("Female")) {
            optionStrSpouseGender = "Male";
        } else if (strSpouseGender.equalsIgnoreCase("Male")) {
            optionStrSpouseGender = "Female";
        }

        String[] Gender;

        if (strSpouseGender != null && optionStrSpouseGender != null) {
            Gender = new String[]{
                    strSpouseGender,
                    optionStrSpouseGender
            };
        } else {
            Gender = new String[]{
                    "Male",
                    "Female"
            };
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileEditActivity.this, android.R.layout.simple_spinner_item, Gender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGenderEdit.setAdapter(adapter);
        spnGenderEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                    if (strSpouseGender != null) {
                        strGender = strSpouseGender;
                    } else {
                        strGender = "Male";
                    }

                } else if (position == 1) {
                    strGender = optionStrSpouseGender;
                    if (optionStrSpouseGender != null) {
                        strGender = optionStrSpouseGender;
                    } else {
                        strGender = "Female";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        edtName.setText(strName);
        edtMemberId.setText(strRCTPID);
        txtContact.setText(strContact);
        txtEmail.setText(strEmail);
        txtDOB.setText(strDOB);
        txtAge.setText(strAge);
        edtOrgnName.setText(strOrgnName);
        edtClassfication.setText(strClassfication);
        edtVocation.setText(strVocation);
        edtSubVocation.setText(strSubVocation);
        edtSpouseName.setText(strSpouseName);
        //  edtSpouseGender.setText(strSpouseGender);
        edtSpouseContact.setText(strSpouseContact);
        txtSpouseDOB.setText(strSpouseBirthday);
        txtAnniversary.setText(strAnniversaryDate);


        txtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                StartmYear3 = c.get(Calendar.YEAR);
                StartmMonth3 = c.get(Calendar.MONTH);
                StartmDay3 = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(ProfileEditActivity.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfmonth1) {

                        txtDOB.setText(dayOfmonth1 + "/" + (monthOfYear + 1) + "/" + year);
                        startDate3 = txtDOB.getText().toString();

                        Toast.makeText(ProfileEditActivity.this, startDate3, Toast.LENGTH_SHORT).show();


                    }
                }, StartmYear3, StartmMonth3, StartmDay3);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        txtDOB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                final ProgressDialog dialog2 = new ProgressDialog(ProfileEditActivity.this);
                dialog2.setIndeterminate(true);
                dialog2.setCancelable(false);
                dialog2.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        dialog2.dismiss();

                        String[] strDate = startDate3.split("/");

                        strDay = strDate[0];
                        strMonth = strDate[1];
                        strYear = strDate[2];


                        int intAge = getAge(Integer.parseInt(strYear), Integer.parseInt(strMonth), Integer.parseInt(strDay));
                        strCalAge = String.valueOf(intAge);
                        txtAge.setText(strCalAge);

                        boolean isTextchanged = isTextChanged(strCalAge);

                    }
                }, 2000);

            }
        });

        txtAnniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                StartmYear4 = c.get(Calendar.YEAR);
                StartmMonth4 = c.get(Calendar.MONTH);
                StartmDay4 = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(ProfileEditActivity.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfmonth1) {

                        txtAnniversary.setText(dayOfmonth1 + "/" + (monthOfYear + 1) + "/" + year);
                        strAnniversaryDate = txtAnniversary.getText().toString();

                        Toast.makeText(ProfileEditActivity.this, strAnniversaryDate, Toast.LENGTH_SHORT).show();


                    }
                }, StartmYear4, StartmMonth4, StartmDay4);


                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });

        txtSpouseDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                StartmYear5 = c.get(Calendar.YEAR);
                StartmMonth5 = c.get(Calendar.MONTH);
                StartmDay5 = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(ProfileEditActivity.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfmonth1) {

                        txtSpouseDOB.setText(dayOfmonth1 + "/" + (monthOfYear + 1) + "/" + year);
                        strUpSpouseBirthday = txtSpouseDOB.getText().toString();

                        Toast.makeText(ProfileEditActivity.this, strUpSpouseBirthday, Toast.LENGTH_SHORT).show();


                    }
                }, StartmYear5, StartmMonth5, StartmDay5);


                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();


            }
        });


        strKID_DETAILS = strKID_DETAILS.replace("null##", " ");

        strKID_DETAILS2 = strKID_DETAILS;
        String kidArray[] = strKID_DETAILS.split("##");

        for (int u = 0; u < kidArray.length; u++) {
            String temp = kidArray[u];
            String pop = temp.replace("##", " ").trim();

            String kidArray2[] = pop.split(",");

            id = String.valueOf(u + 1);


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

                if (ischanged != true) {
                    if (TempKID_DETAILS == null) {
                        TempKID_DETAILS = id + "," + Name + "," + Contact + "," + DOB2 + "," + Gend;
                    } else {
                        TempKID_DETAILS = TempKID_DETAILS + "##" + id + "," + Name + "," + Contact + "," + DOB2 + "," + Gend;
                    }
                } else {

                    TempKID_DETAILS = strKID_DETAILS;
                }


                stringArrayList.add(kidModel);
                kidAdapter = new KidAdapter(this, stringArrayList, "Long press to edit Kids details");
                listView.setAdapter(kidAdapter);

            } catch (ArrayIndexOutOfBoundsException exception) {
            } catch (Exception exception) {
            }
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ProfileEditActivity.this, "Long press to edit details", Toast.LENGTH_SHORT).show();
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                showEditDialog(position);

                return true;
            }
        });


        strKidInfo = strKID_DETAILS.replaceAll("##", "<br>");


        txtKIDS_DETAILS.setText(strKidInfo);
        originalString = txtKIDS_DETAILS.getText().toString();

        originalString = originalString.replaceAll("Name", " <font color='#3F51B5'>Name<</font>");
        originalString = originalString.replaceAll("Contact", "<font color='#3F51B5'>Contact</font>");
        originalString = originalString.replaceAll("Gender", "<font color='#3F51B5'>Gender</font>");
        originalString = originalString.replaceAll("DOB", "<font color='#3F51B5'>DOB</font>");
        originalString = originalString.replaceAll(",", "<br>");

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });
        txtKIDS_DETAILS.setText(Html.fromHtml(originalString));

        txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog;
                dialog = new Dialog(ProfileEditActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().getAttributes().windowAnimations = R.style.popup_window_animation_phone;
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(
                                android.graphics.Color.TRANSPARENT
                        )

                );
                dialog.setContentView(R.layout.popup_kid);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


                edtPopName = (EditText) dialog.findViewById(R.id.edtKidName);
                edtPopMobile = (EditText) dialog.findViewById(R.id.edtKidMobile);

                spnGender = (Spinner) dialog.findViewById(R.id.spnGender);
                txtPopGender = (TextView) dialog.findViewById(R.id.txtGender);
                btnSave = (Button) dialog.findViewById(R.id.btnSave);

                edtDob3 = dialog.findViewById(R.id.input_dob);


                edtDob3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar c = Calendar.getInstance();
                        StartmYear3 = c.get(Calendar.YEAR);
                        StartmMonth3 = c.get(Calendar.MONTH);
                        StartmDay3 = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileEditActivity.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfmonth1) {

                                edtDob3.setText(dayOfmonth1 + "/" + (monthOfYear + 1) + "/" + year);
                                startDate3 = edtDob3.getText().toString();

                                Toast.makeText(ProfileEditActivity.this, startDate3, Toast.LENGTH_SHORT).show();
                            }
                        }, StartmYear3, StartmMonth3, StartmDay3);

                        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                    }
                });


                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


                final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileEditActivity.this, R.array.Gender, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnGender.setAdapter(adapter);
                spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 1) {
                            strPopGender = "Male";
                        } else if (position == 2) {
                            strPopGender = "Female";
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        strPopName = edtPopName.getText().toString().trim();
                        strPopMobile = edtPopMobile.getText().toString().trim();
                        if (edtPopMobile.length() == 10) {
                            if (strPopName != null && strPopMobile != null && startDate3 != null && strPopGender != null) {

                                TempKID_DETAILS = strPopName + "," + strPopMobile + "," + startDate3 + "," + strPopGender + "##" + strKID_DETAILS;
                                txtTemp.setText(strFinalKids);


                                strUpName = edtName.getText().toString().trim();
                                strUpMemberId = edtMemberId.getText().toString().trim();
                                strUpContact = txtContact.getText().toString().trim();
                                strUpDOB = txtDOB.getText().toString().trim();
                                strUpAge = txtAge.getText().toString().trim();
                                strUpEmail = txtEmail.getText().toString().trim();
                                strUpOrgnName = edtOrgnName.getText().toString().trim();
                                strUpClassfication = edtClassfication.getText().toString().trim();
                                strUpVocation = edtVocation.getText().toString().trim();
                                strUpSubVocation = edtSubVocation.getText().toString().trim();
                                strUpSpouseName = edtSpouseName.getText().toString().trim();
                                strUpSpouseContact = edtSpouseContact.getText().toString().trim();
                                strUpSpouseBirthday = txtSpouseDOB.getText().toString().trim();
                                strUpAnniversaryDate = txtAnniversary.getText().toString().trim();

                                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {

                                    if (strGender != null) {
                                        updateAsync update = new updateAsync();
                                        update.execute();
                                    } else {
                                        Toast.makeText(ProfileEditActivity.this, "select gender first", Toast.LENGTH_SHORT).show();
                                    }

                                } else {

                                    Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                                }


                                dialog.getWindow().getAttributes().windowAnimations = R.style.popup_window_close_animation_phone;
                                dialog.dismiss();

                            } else {
                                Toast.makeText(ProfileEditActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ProfileEditActivity.this, "Please enter 10 digit number", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

                imgbtnClose = dialog.findViewById(R.id.ib_close);

                imgbtnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strUpName = edtName.getText().toString().trim();
                strUpMemberId = edtMemberId.getText().toString().trim();
                strUpContact = txtContact.getText().toString().trim();
                strUpDOB = txtDOB.getText().toString().trim();
                strUpAge = txtAge.getText().toString().trim();
                strUpEmail = txtEmail.getText().toString().trim();
                strUpOrgnName = edtOrgnName.getText().toString().trim();
                strUpClassfication = edtClassfication.getText().toString().trim();
                strUpVocation = edtVocation.getText().toString().trim();
                strUpSubVocation = edtSubVocation.getText().toString().trim();
                strUpSpouseName = edtSpouseName.getText().toString().trim();
                strUpSpouseContact = edtSpouseContact.getText().toString().trim();
                strUpSpouseBirthday = txtSpouseDOB.getText().toString().trim();
                strUpAnniversaryDate = txtAnniversary.getText().toString().trim();

                ischanged = true;


                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    if (strGender != null) {
                        updateAsync2 update = new updateAsync2();
                        update.execute();

                    } else {
                        Toast.makeText(ProfileEditActivity.this, "select gender first", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public static boolean isTextChanged(String strCalAge) {
        Log.d("strCalAge", strCalAge);
        return true;
    }

    private void showEditDialog(final int position) {


        final Dialog dialog;
        dialog = new Dialog(ProfileEditActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.popup_window_animation_phone;
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(
                        android.graphics.Color.TRANSPARENT
                )

        );
        dialog.setContentView(R.layout.popup_kid);


        edtPopName = (EditText) dialog.findViewById(R.id.edtKidName);
        edtPopMobile = (EditText) dialog.findViewById(R.id.edtKidMobile);

        spnGender = (Spinner) dialog.findViewById(R.id.spnGender);
        txtPopGender = (TextView) dialog.findViewById(R.id.txtGender);
        btnSave = (Button) dialog.findViewById(R.id.btnSave);

        edtDob3 = dialog.findViewById(R.id.input_dob);

        edtPopName.setText(stringArrayList.get(position).getName());
        edtPopMobile.setText(stringArrayList.get(position).getContact());
        edtDob3.setText(stringArrayList.get(position).getDOB());
        strKidGender = stringArrayList.get(position).getGender();
        startDate3 = edtDob3.getText().toString();

        edtDob3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                StartmYear3 = c.get(Calendar.YEAR);
                StartmMonth3 = c.get(Calendar.MONTH);
                StartmDay3 = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileEditActivity.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfmonth1) {

                        edtDob3.setText(dayOfmonth1 + "/" + (monthOfYear + 1) + "/" + year);
                        startDate3 = edtDob3.getText().toString();

                        Toast.makeText(ProfileEditActivity.this, startDate3, Toast.LENGTH_SHORT).show();
                    }
                }, StartmYear3, StartmMonth3, StartmDay3);

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });


        if (strKidGender.equalsIgnoreCase("Female")) {
            strKidgenderOptional = "Male";
        } else if (strKidGender.equalsIgnoreCase("Male")) {
            strKidgenderOptional = "Female";
        }

        String[] Gender;

        if (strKidGender != null && strKidgenderOptional != null) {
            Gender = new String[]{
                    strKidGender,
                    strKidgenderOptional
            };
        } else {
            Gender = new String[]{
                    "No kidGender selected",
                    "Male",
                    "Female"
            };
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileEditActivity.this, android.R.layout.simple_spinner_item, Gender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGender.setAdapter(adapter);
        spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    strPopGender = strKidGender;
                } else if (position == 1) {
                    strPopGender = strKidgenderOptional;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strPopName = edtPopName.getText().toString().trim();
                strPopMobile = edtPopMobile.getText().toString().trim();
                clicked = true;

                if (strPopName != null && strPopMobile != null && startDate3 != null && strPopGender != null) {


                    stringArrayList.get(position).setName(edtPopName.getText().toString());
                    stringArrayList.get(position).setContact(edtPopMobile.getText().toString());

                    strID = stringArrayList.get(position).getId();

                    strEditfinal = strPopName + "," + strPopMobile + "," + startDate3 + "," + strPopGender;

                    String OldKidMainData[] = TempKID_DETAILS.split("##");

                    try {

                        for (int u = 0; u < OldKidMainData.length; u++) {

                            String kid[] = OldKidMainData[u].trim().split(",");


                            if (NewKidData == null) {
                                if (kid[0].equalsIgnoreCase(strID)) {
                                    NewKidData = strPopName + "," + strPopMobile + "," + startDate3 + "," + strPopGender + "##";
                                } else {
                                    NewKidData = kid[1] + "," + kid[2] + "," + kid[3] + "," + kid[4] + "##";
                                }

                            } else {
                                if (kid[0].equalsIgnoreCase(strID)) {
                                    NewKidData += strPopName + "," + strPopMobile + "," + startDate3 + "," + strPopGender + "##";
                                } else {
                                    NewKidData += kid[1] + "," + kid[2] + "," + kid[3] + "," + kid[4] + "##";
                                }

                            }

                        }

                    } catch (ArrayIndexOutOfBoundsException exception) {
                    } catch (Exception exception) {
                    }


                    TempKID_DETAILS = NewKidData;
                    kidAdapter.notifyDataSetChanged();


                    strUpName = edtName.getText().toString().trim();
                    strUpMemberId = edtMemberId.getText().toString().trim();
                    strUpContact = txtContact.getText().toString().trim();
                    strUpDOB = txtDOB.getText().toString().trim();
                    strUpAge = txtAge.getText().toString().trim();
                    strUpEmail = txtEmail.getText().toString().trim();
                    strUpOrgnName = edtOrgnName.getText().toString().trim();
                    strUpClassfication = edtClassfication.getText().toString().trim();
                    strUpVocation = edtVocation.getText().toString().trim();
                    strUpSubVocation = edtSubVocation.getText().toString().trim();
                    strUpSpouseName = edtSpouseName.getText().toString().trim();
                    //      strUpSpouseGender = edtSpouseGender.getText().toString().trim();
                    strUpSpouseContact = edtSpouseContact.getText().toString().trim();
                    strUpSpouseBirthday = txtSpouseDOB.getText().toString().trim();
                    strUpAnniversaryDate = txtAnniversary.getText().toString().trim();


                    if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                        if (strGender != null) {
                            updateAsync update = new updateAsync();
                            update.execute();
                        } else {
                            Toast.makeText(ProfileEditActivity.this, "select gender first", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                    }

                    dialog.getWindow().getAttributes().windowAnimations = R.style.popup_window_close_animation_phone;
                    dialog.dismiss();


                } else {
                    Toast.makeText(ProfileEditActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        imgbtnClose = dialog.findViewById(R.id.ib_close);
        imgbtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public int getAge(int DOByear, int DOBmonth, int DOBday) {

        int age, currentYear = 0, currentMonth = 0, todayDay = 0;

        final android.icu.util.Calendar calenderToday;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            calenderToday = android.icu.util.Calendar.getInstance();
            currentYear = calenderToday.get(android.icu.util.Calendar.YEAR);
            currentMonth = 1 + calenderToday.get(android.icu.util.Calendar.MONTH);
            todayDay = calenderToday.get(android.icu.util.Calendar.DAY_OF_MONTH);
        }


        age = currentYear - DOByear;

        if (DOBmonth > currentMonth) {
            --age;
        } else if (DOBmonth == currentMonth) {
            if (DOBday > todayDay) {
                --age;
            }
        }
        return age;

    }


    class updateAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(ProfileEditActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection conn;
            URL url;
            try {
                url = new URL("http://192.168.0.102/RCTP/v1/profileupdate.php");

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


                if (imagename == null) {
                    imagename = strProfilePhoto;
                }

                ischanged = true;

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("RCTP_ID", strUpMemberId)
                        .appendQueryParameter("NAME", strUpName)
                        .appendQueryParameter("MOBILENO", strUpContact)
                        .appendQueryParameter("DOB", strUpDOB)
                        .appendQueryParameter("AGE", strUpAge)
                        .appendQueryParameter("ORGANIZATION", strUpOrgnName)
                        .appendQueryParameter("CLASSIFICATION", strUpClassfication)
                        .appendQueryParameter("VOCATION", strUpVocation)
                        .appendQueryParameter("SUBVOCATION", strUpSubVocation)
                        .appendQueryParameter("SPOUSENAME", strUpSpouseName)
                        .appendQueryParameter("SPOUSEGENDER", strGender)
                        .appendQueryParameter("SPOUSENUMEBR", strUpSpouseContact)
                        .appendQueryParameter("SPOUSEBIRTHDATE", strUpSpouseBirthday)
                        .appendQueryParameter("ANNIVERSARYDATE", strAnniversaryDate)
                        .appendQueryParameter("KIDS_DETAILS", TempKID_DETAILS)
                        .appendQueryParameter("PROFILEPHOTO", imagename);

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

//                    profileAsync profileAsync = new profileAsync();
//                    profileAsync.execute();
                    profileAsync2();

                    Toast.makeText(ProfileEditActivity.this, "Profile has been updated successfully", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class updateAsync2 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(ProfileEditActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection conn;
            URL url;
            try {
                url = new URL("http://192.168.0.102/RCTP/v1/profileupdate.php");

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


                if (imagename == null) {
                    imagename = strProfilePhoto;
                }

                ischanged = true;

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("RCTP_ID", strUpMemberId)
                        .appendQueryParameter("NAME", strUpName)
                        .appendQueryParameter("MOBILENO", strUpContact)
                        .appendQueryParameter("DOB", strUpDOB)
                        .appendQueryParameter("AGE", strUpAge)
                        .appendQueryParameter("ORGANIZATION", strUpOrgnName)
                        .appendQueryParameter("CLASSIFICATION", strUpClassfication)
                        .appendQueryParameter("VOCATION", strUpVocation)
                        .appendQueryParameter("SUBVOCATION", strUpSubVocation)
                        .appendQueryParameter("SPOUSENAME", strUpSpouseName)
                        .appendQueryParameter("SPOUSEGENDER", strGender)
                        .appendQueryParameter("SPOUSENUMEBR", strUpSpouseContact)
                        .appendQueryParameter("SPOUSEBIRTHDATE", strUpSpouseBirthday)
                        .appendQueryParameter("ANNIVERSARYDATE", strAnniversaryDate)
                        .appendQueryParameter("KIDS_DETAILS", strKID_DETAILS2)
                        .appendQueryParameter("PROFILEPHOTO", imagename);

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

//                    profileAsync profileAsync = new profileAsync();
//                    profileAsync.execute();
                    profileAsync2();

                    Toast.makeText(ProfileEditActivity.this, "Profile has been updated successfully", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    private void choosePhotoFromGallary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), CHOOSING_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (bitmap != null) {
            bitmap.recycle();
        }

        if (requestCode == CHOOSING_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            try {
                fileUri = data.getData();

                getname = getFileName(fileUri);
                Uri imageUri = data.getData();

                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                bitmap = BitmapFactory.decodeStream(imageStream);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imageInByte = stream.toByteArray();
                long lengthbmp = imageInByte.length;
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                circleImageView.setImageBitmap(bitmap);

                Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                File finalFile = new File(getRealPathFromURI(tempUri));
                uploadFile(String.valueOf(finalFile));

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == CAMERA && resultCode == RESULT_OK && data != null) {

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            circleImageView.setImageBitmap(thumbnail);
            Uri tempUri = getImageUri(getApplicationContext(), thumbnail);
            File finalFile = new File(getRealPathFromURI(tempUri));
            Log.d("cameraFile", String.valueOf(finalFile));
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
                    Toast.makeText(ProfileEditActivity.this, "image upload", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ProfileEditActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class profileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(ProfileEditActivity.this);
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
                Toast.makeText(ProfileEditActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (ProfileEditActivity.this.isDestroyed()) { // or call isFinishing() if min sdk version < 17
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
                    Toast.makeText(ProfileEditActivity.this, "Sorry! Something went wrong", Toast.LENGTH_SHORT).show();
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (ProfileEditActivity.this.isDestroyed()) { // or call isFinishing() if min sdk version < 17
                        return;
                    }
                }
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    strStatus = jsonObject.getString("Status");

                    if (strStatus.equalsIgnoreCase("1")) {
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Result", s);
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), DashProfileActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ProfileEditActivity.this, "Sorry! Something went wrong", Toast.LENGTH_SHORT).show();
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
