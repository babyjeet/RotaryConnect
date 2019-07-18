package com.android.aquagiraffe.rotaryconnect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;

import java.util.Calendar;

public class PersonalInfoActivity extends AppCompatActivity {

    EditText edtSpouseName, edtSpouseNumber;
    String strSpouseName, strSpouseNumber, strSpouseBirthday, strAnniversaryDate, strSpGender;
    TextInputLayout inptSpouseName, inptSpouseNumber, inptSpouseBirthday, inptAnniversaryDate, inptKid1Name, inptKid1Number, inptKid1Birthday,
            inptKid2Name, inptKid2Number, inptKid2Birthday, inptKid3Name, inptKid3Number, inptKid3Bday;
    String strName, strAge, strEmail, strDOB, strMobile, strMemberID, strOrgnName, strPassword, strClassification, strVocation, strSubVocation, strGender1, strGender2, strKid = null;
    Button btnNext, btnSkip, btnAddKid;
    String strPopName, strPopDOB, strPopMobile, strPopGender;
    Spinner spnGender1, spnGender2;
    EditText edtPopName, edtPopMobile, edtPopGender;
    Spinner spnGender, spnSpGender;
    TextView txtPopGender, txtTemp;
    Button btnSave;
    int mYear, mMonth, mDay;
    String strFinalKids, strPoGender;
    private Context mContext;
    private Activity mActivity;
    private RelativeLayout mRelativeLayout;
    private PopupWindow mPopupWindow;

    private TextView edtDob, edtDob2, edtDob3;
    String startDate, startDate2, startDate3;
    int StartmYear, StartmMonth, StartmDay, StartmYear2, StartmMonth2, StartmDay2, StartmYear3, StartmMonth3, StartmDay3;
    ImageView back;
    RadioGroup rg, rg2;
    RadioButton radioYes, radioNo, radioYes2, radioNo2;
    private LinearLayout linearLayout1, linearLayout2;
    Toolbar toolbar;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        back = findViewById(R.id.imgback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalInfoActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.rlMain);
        spnSpGender = (Spinner) findViewById(R.id.spnSpGender);
        txtTemp = (TextView) findViewById(R.id.temp);

        linearLayout1 = findViewById(R.id.linear);
        linearLayout2 = findViewById(R.id.linear2);

        mContext = getApplicationContext();
        mActivity = PersonalInfoActivity.this;

        rg = findViewById(R.id.rg);
        rg2 = findViewById(R.id.rg2);

        radioYes = findViewById(R.id.yes);
        radioNo = findViewById(R.id.No);

        radioYes2 = findViewById(R.id.yes2);
        radioNo2 = findViewById(R.id.No2);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        strName = preferences.getString("Name", "empty");
        strAge = preferences.getString("Age", "empty");
        strEmail = preferences.getString("Email", "empty");
        strDOB = preferences.getString("DOB", "empty");
        strMobile = preferences.getString("Mobile", "empty");
        strPassword = preferences.getString("Password", "empty");
        strMemberID = preferences.getString("Member", "empty");
        strOrgnName = preferences.getString("OrgnName", "empty");
        strClassification = preferences.getString("Classification", "empty");
        strVocation = preferences.getString("Vocation", "empty");
        strSubVocation = preferences.getString("SubVocation", "empty");

        edtSpouseName = (EditText) findViewById(R.id.input_spouseName);
        edtSpouseNumber = (EditText) findViewById(R.id.input_spouseNumber);

        inptSpouseName = (TextInputLayout) findViewById(R.id.input_layout_spouseName);
        inptSpouseNumber = (TextInputLayout) findViewById(R.id.input_layout_spouseNumber);

        edtSpouseName.addTextChangedListener(new MyTextWatcher(edtSpouseName));
        edtSpouseNumber.addTextChangedListener(new MyTextWatcher(edtSpouseNumber));

        edtDob = findViewById(R.id.input_dob);
        edtDob2 = findViewById(R.id.input_dob2);

        edtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                StartmYear = c.get(Calendar.YEAR);
                StartmMonth = c.get(Calendar.MONTH);
                StartmDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(PersonalInfoActivity.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfmonth1) {

                        edtDob.setText(dayOfmonth1 + "/" + (monthOfYear + 1) + "/" + year);
                        startDate = edtDob.getText().toString();

                        Toast.makeText(PersonalInfoActivity.this, startDate, Toast.LENGTH_SHORT).show();
                    }
                }, StartmYear, StartmMonth, StartmDay);

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        edtDob2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                StartmYear2 = c.get(Calendar.YEAR);
                StartmMonth2 = c.get(Calendar.MONTH);
                StartmDay2 = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(PersonalInfoActivity.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfmonth1) {

                        edtDob2.setText(dayOfmonth1 + "/" + (monthOfYear + 1) + "/" + year);
                        startDate2 = edtDob2.getText().toString();

                        Toast.makeText(PersonalInfoActivity.this, startDate2, Toast.LENGTH_SHORT).show();
                    }
                }, StartmYear2, StartmMonth2, StartmDay2);

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(PersonalInfoActivity.this, R.array.Gender1, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSpGender.setAdapter(adapter);
        spnSpGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    strSpGender = "Male";
                } else if (position == 2) {
                    strSpGender = "Female";
                } else if (position == 0) {
                    strSpGender = "Gender";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnNext = (Button) findViewById(R.id.btn_next);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnAddKid = (Button) findViewById(R.id.btnAddKid);
        btnAddKid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog;
                dialog = new Dialog(PersonalInfoActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().getAttributes().windowAnimations = R.style.popup_window_animation_phone;
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(
                                android.graphics.Color.TRANSPARENT
                        )

                );
                dialog.setContentView(R.layout.popup);
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

                        DatePickerDialog datePickerDialog = new DatePickerDialog(PersonalInfoActivity.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfmonth1) {

                                edtDob3.setText(dayOfmonth1 + "/" + (monthOfYear + 1) + "/" + year);
                                startDate3 = edtDob3.getText().toString();

                                Toast.makeText(PersonalInfoActivity.this, startDate3, Toast.LENGTH_SHORT).show();
                            }
                        }, StartmYear3, StartmMonth3, StartmDay3);

                        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                    }
                });


                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(PersonalInfoActivity.this, R.array.Gender, android.R.layout.simple_spinner_item);
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


                        if (strFinalKids == null) {
                            strFinalKids = strPopName + "," + strPopMobile + "," + startDate3 + "," + strPopGender;
                        } else {
                            strFinalKids = strFinalKids + "##" + strPopName + "," + strPopMobile + "," + startDate3 + "," + strPopGender;
                        }

                        txtTemp.setText(strFinalKids);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.popup_window_close_animation_phone;
                        dialog.dismiss();
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

            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strSpouseName = edtSpouseName.getText().toString().trim();
                strSpouseNumber = edtSpouseNumber.getText().toString().trim();

                String strFinalKidsInfo = txtTemp.getText().toString().trim();
                if (rg.getCheckedRadioButtonId() != -1) {

                    if (rg2.getCheckedRadioButtonId() != -1) {
                        if (strMemberID != null) {
                            SharedPreferences preferences1 = getApplicationContext().getSharedPreferences("pref1", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences1.edit();
                            editor.putString("RCTP_ID", strMemberID);
                            editor.putString("NAME", strName);
                            editor.putString("MOBILENO", strMobile);
                            editor.putString("AGE", strAge);
                            editor.putString("Email", strEmail);
                            editor.putString("DOB", strDOB);
                            editor.putString("Password", strPassword);
                            editor.putString("ORGANIZATION", strOrgnName);
                            editor.putString("CLASSIFICATION", strClassification);
                            editor.putString("VOCATION", strVocation);
                            editor.putString("SUBVOCATION", strSubVocation);
                            editor.putString("SPOUSENAME", strSpouseName);
                            editor.putString("SPOUSENUMEBR", strSpouseNumber);
                            editor.putString("SPOUSEBIRTHDATE", startDate);
                            editor.putString("SPOUSEGENDER", strSpGender);
                            editor.putString("ANNIVERSARYDATE", startDate2);
                            editor.putString("KIDS", strFinalKidsInfo);
                            editor.apply();

                            Intent intent = new Intent(getApplicationContext(), ProfileImageActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(PersonalInfoActivity.this, "Please Enter Correct Data", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "you forgot to select child option", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(mContext, "you forgot to select spouse option", Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences1 = getApplicationContext().getSharedPreferences("pref1", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences1.edit();
                editor.putString("Member", strMemberID);
                editor.putString("Name", strName);
                editor.putString("Mobile", strMobile);
                editor.putString("Age", strAge);
                editor.putString("Email", strEmail);
                editor.putString("DOB", strDOB);
                editor.putString("OrgnName", strOrgnName);
                editor.putString("Classification", strClassification);
                editor.putString("Vocation", strVocation);
                editor.putString("SubVocation", strSubVocation);
                editor.apply();


                Intent intent = new Intent(getApplicationContext(), ProfileImageActivity.class);
                startActivity(intent);

            }
        });

    }


    public void onRadioButtonClicked(View v) {


        // Is the current Radio Button checked?
        boolean checked = ((RadioButton) v).isChecked();

        switch (v.getId()) {
            case R.id.yes:
                if (checked) {
                    inptSpouseName.setVisibility(View.VISIBLE);
                    inptSpouseNumber.setVisibility(View.VISIBLE);
                    spnSpGender.setVisibility(View.VISIBLE);
                    linearLayout1.setVisibility(View.VISIBLE);
                    linearLayout2.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.No:
                if (checked) {
                    inptSpouseName.setVisibility(View.GONE);
                    inptSpouseNumber.setVisibility(View.GONE);
                    spnSpGender.setVisibility(View.GONE);
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.GONE);

                    Toast.makeText(mContext, "no", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void onRadioButtonClicked2(View v) {


        // Is the current Radio Button checked?
        boolean checked = ((RadioButton) v).isChecked();

        switch (v.getId()) {
            case R.id.yes2:
                if (checked) {
                    btnAddKid.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.No2:
                if (checked) {
                    btnAddKid.setVisibility(View.GONE);
                }
                break;
        }
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onResume() {
        super.onResume();
        this.doubleBackToExitPressedOnce = false;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "please press back again to exit!", Toast.LENGTH_SHORT).show();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
                case R.id.input_spouseName:
                    //validateSpouseName();
                    break;
                case R.id.input_spouseNumber:
                    //validateSpouseNumber();
                    break;
            }
        }
    }
}
