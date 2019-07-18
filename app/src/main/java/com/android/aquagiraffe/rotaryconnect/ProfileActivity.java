package com.android.aquagiraffe.rotaryconnect;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {
    private EditText edtName, edtAge, edtDOB, edtMobile, edtMemberID, edtOrgnName, edtClassification, edtVocation, edtSubVocation, edtPassword, edtEmail;
    private TextInputLayout inptLayoutMemberID, inptLayoutName, inptLayoutMobile, inptLayoutDOB, inptLayoutAge, inptLayoutEmail, inptLayoutPassword, inptLayoutOrgnName, inptLayoutClassification, inptLayoutVocation, inptLayoutSubVocation;
    String strName, strAge, strMobile, strMemberID, strOrgnName, strClassification, strVocation, strSubVocation, strPassword, strEmail;
    String strYear, strMonth, strDay, strCalAge;
    int errorColor;
    final int version = Build.VERSION.SDK_INT;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Button btnSubmit;
    private ImageView back;
    ProgressDialog progressDialog;
    private TextView edtDob;
    String startDate, strStatus;
    int StartmYear, StartmMonth, StartmDay;
    SessionManager session;
    Toolbar toolbar;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));

        back = findViewById(R.id.imgback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        session = new SessionManager(getApplicationContext());

        edtName = (EditText) findViewById(R.id.input_name);
        edtAge = (EditText) findViewById(R.id.input_age);
        edtEmail = (EditText) findViewById(R.id.input_email);
        edtDob = (TextView) findViewById(R.id.input_dob);
        edtMobile = (EditText) findViewById(R.id.input_mobile);
        edtMemberID = (EditText) findViewById(R.id.input_memberid);
        edtPassword = (EditText) findViewById(R.id.input_password);
        edtOrgnName = (EditText) findViewById(R.id.input_orgnname);
        edtClassification = (EditText) findViewById(R.id.input_classification);
        edtVocation = (EditText) findViewById(R.id.input_vocation);
        edtSubVocation = (EditText) findViewById(R.id.input_subvocation);

        edtDob = findViewById(R.id.input_dob);


        if (version >= 23) {
            errorColor = ContextCompat.getColor(getApplicationContext(), R.color.textColor);
        } else {
            errorColor = getResources().getColor(R.color.errorColor);
        }


        edtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                StartmYear = c.get(Calendar.YEAR);
                StartmMonth = c.get(Calendar.MONTH);
                StartmDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfmonth1) {

                        edtDob.setText(dayOfmonth1 + "/" + (monthOfYear + 1) + "/" + year);
                        startDate = edtDob.getText().toString();

                        Toast.makeText(ProfileActivity.this, startDate, Toast.LENGTH_SHORT).show();
                    }
                }, StartmYear, StartmMonth, StartmDay);

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        edtAge.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (startDate != null) {
                    final ProgressDialog dialog = new ProgressDialog(ProfileActivity.this);
                    dialog.setIndeterminate(true);
                    dialog.setCancelable(false);
                    dialog.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void run() {
                            dialog.dismiss();

                            String[] strDate = startDate.split("/");
                            Log.d("strDate", String.valueOf(strDate));

                            strDay = strDate[0];
                            strMonth = strDate[1];
                            strYear = strDate[2];


                            int intAge = getAge(Integer.parseInt(strYear), Integer.parseInt(strMonth), Integer.parseInt(strDay));
                            strCalAge = String.valueOf(intAge);
                            edtAge.setText(strCalAge);

                        }
                    }, 500);
                } else {
                    Toast.makeText(ProfileActivity.this, "Please provide the date of birth", Toast.LENGTH_SHORT).show();
                }
            }
        });

        inptLayoutMemberID = (TextInputLayout) findViewById(R.id.input_layout_memberid);
        inptLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inptLayoutMobile = (TextInputLayout) findViewById(R.id.input_layout_mobile);
        // inptLayoutDOB = (TextInputLayout) findViewById(R.id.input_layout_dob);
        inptLayoutAge = (TextInputLayout) findViewById(R.id.input_layout_age);
        inptLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inptLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inptLayoutOrgnName = (TextInputLayout) findViewById(R.id.input_layout_orgnname);
        inptLayoutClassification = (TextInputLayout) findViewById(R.id.input_layout_classification);
        inptLayoutVocation = (TextInputLayout) findViewById(R.id.input_layout_vocation);
        inptLayoutSubVocation = (TextInputLayout) findViewById(R.id.input_layout_subvocation);


        edtName.addTextChangedListener(new MyTextWatcher(edtName));
        edtAge.addTextChangedListener(new MyTextWatcher(edtAge));
        edtEmail.addTextChangedListener(new MyTextWatcher(edtEmail));
        edtMobile.addTextChangedListener(new MyTextWatcher(edtMobile));
        edtMemberID.addTextChangedListener(new MyTextWatcher(edtMemberID));
        edtPassword.addTextChangedListener(new MyTextWatcher(edtPassword));
        edtOrgnName.addTextChangedListener(new MyTextWatcher(edtOrgnName));
        edtClassification.addTextChangedListener(new MyTextWatcher(edtClassification));
        edtVocation.addTextChangedListener(new MyTextWatcher(edtVocation));
        edtSubVocation.addTextChangedListener(new MyTextWatcher(edtSubVocation));


        btnSubmit = (Button) findViewById(R.id.btn_next);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strMemberID = edtMemberID.getText().toString().trim();
                strName = edtName.getText().toString().trim();
                strMobile = edtMobile.getText().toString().trim();
                strAge = edtAge.getText().toString().trim();
                strEmail = edtEmail.getText().toString().trim();
                //        strDOB = edtDOB.getText().toString().trim();
                strOrgnName = edtOrgnName.getText().toString().trim();
                strPassword = edtPassword.getText().toString().trim();
                strClassification = edtClassification.getText().toString().trim();
                strVocation = edtVocation.getText().toString().trim();
                strSubVocation = edtSubVocation.getText().toString().trim();


                if (validateName() && validateMemberID() && validateMobile() /*&& validateDOB()*/ && validateAge() && validatePassword() && validateOrgnName() && validateClassification() && validateVocation() && validateSubVocation()) {
                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString("Member", strMemberID);
                    editor.putString("Name", strName);
                    editor.putString("Mobile", strMobile);
                    editor.putString("Age", strAge);
                    editor.putString("Email", strEmail);
                    editor.putString("DOB", startDate);
                    editor.putString("Password", strPassword);
                    editor.putString("OrgnName", strOrgnName);
                    editor.putString("Classification", strClassification);
                    editor.putString("Vocation", strVocation);
                    editor.putString("SubVocation", strSubVocation);
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), PersonalInfoActivity.class);
                    startActivity(intent);

              /*      if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                        memberAsync memberAsync = new memberAsync();
                        memberAsync.execute();

                    } else {

                        Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                    }*/

                } else {
                    Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    class memberAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(ProfileActivity.this);
            progressDialog.setMessage("Please wait...");
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
                Log.d("Resp", String.valueOf(response_code));


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
                Toast.makeText(ProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
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
                strStatus = jsonObject.getString("Status");

                if (strStatus.equalsIgnoreCase("3")) {
                    Intent intent = new Intent(getApplicationContext(), PersonalInfoActivity.class);
                    startActivity(intent);
                } else if (strStatus.equalsIgnoreCase("1")) {
                    buildDialog("Member id is already exist");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void buildDialog(String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage(type);
        builder.setNegativeButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        dialog.show();
    }


    private boolean validateName() {
        if (edtName.getText().toString().trim().length() == 0) {
            String errorString = "Enter your full name";
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
            edtName.setError(spannableStringBuilder);
            return false;
        }

        return true;
    }

    private boolean validateMemberID() {
        if (edtMemberID.getText().toString().trim().length() != 7) {

            String errorString = "Please enter valid email id";
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
            edtMemberID.setError(spannableStringBuilder);
            return false;
        }

        return true;
    }

    private boolean validateMobile() {
        if (edtMobile.getText().toString().trim().length() != 10) {

            String errorString = "Please enter valid mobile number";
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
            edtMobile.setError(spannableStringBuilder);
            return false;
        }

        return true;
    }

    private boolean validateDOB() {
        if (edtDob.getText().toString().trim().length() < 10) {

            String errorString = "Please enter valid Date of birth";
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
            edtDob.setError(spannableStringBuilder);
            return false;
        }
        return true;
    }

    private boolean validateEmail() {
        if (edtEmail.getText().toString().trim().length() == 0 && edtEmail.getText().toString().trim().matches(emailPattern)) {

            String errorString = "Please enter email id";
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
            edtEmail.setError(spannableStringBuilder);
            return false;
        }
        return true;
    }

    private boolean validateAge() {
        if (edtAge.getText().toString().trim().isEmpty()) {

            String errorString = "Please enter valid age";
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
            edtAge.setError(spannableStringBuilder);
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        if (edtPassword.getText().toString().trim().length() < 6) {

            String errorString = "Please enter valid password";
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
            edtPassword.setError(spannableStringBuilder);
            return false;
        }
        return true;
    }

    private boolean validateOrgnName() {
        if (edtOrgnName.getText().toString().trim().length() == 0) {

            String errorString = "Please enter Organization name";
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
            edtOrgnName.setError(spannableStringBuilder);
            return false;
        }
        return true;
    }

    private boolean validateClassification() {
        if (edtClassification.getText().toString().trim().length() == 0) {


            String errorString = "Please enter classification";
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
            edtClassification.setError(spannableStringBuilder);
            return false;
        }
        return true;
    }

    private boolean validateVocation() {
        if (edtVocation.getText().toString().trim().length() == 0) {

            String errorString = "Please enter your Vocation";
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
            edtVocation.setError(spannableStringBuilder);
            return false;
        }
        return true;
    }

    private boolean validateSubVocation() {
        if (edtSubVocation.getText().toString().trim().length() == 0) {

            String errorString = "Please enter your SubVocation";
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
            edtSubVocation.setError(spannableStringBuilder);
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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


    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_memberid:
                    validateMemberID();
                    break;
                case R.id.input_mobile:
                    validateMobile();
                    break;
                case R.id.input_dob:
                    validateDOB();
                    break;
                case R.id.input_age:
                    validateAge();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_orgnname:
                    validateOrgnName();
                    break;
                case R.id.input_classification:
                    validateClassification();
                    break;
                case R.id.input_vocation:
                    validateVocation();
                    break;
                case R.id.input_subvocation:
                    validateSubVocation();
                    break;
                case R.id.input_password:
                    validatePassword();
                default:
                    break;
            }
        }
    }
}
