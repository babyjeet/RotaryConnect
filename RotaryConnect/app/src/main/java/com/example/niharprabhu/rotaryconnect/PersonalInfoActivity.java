package com.android.aquagiraffe.rotaryconnect;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class PersonalInfoActivity extends AppCompatActivity {
    EditText edtSpouseName,edtSpouseNumber,edtSpouseBirthday,edtAnniversaryDate,edtSpouseGender;
    String strSpouseName,strSpouseNumber,strSpouseBirthday,strAnniversaryDate,strSpGender;
    TextInputLayout inptSpouseName,inptSpouseNumber,inptSpouseBirthday,inptAnniversaryDate,inptKid1Name,inptKid1Number,inptKid1Birthday,
            inptKid2Name,inptKid2Number,inptKid2Birthday,inptKid3Name,inptKid3Number,inptKid3Bday;
    String strName,strAge,strDOB,strMobile,strMemberID,strOrgnName,strPassword,strClassification,strVocation,strSubVocation,strGender1,strGender2,strKid = null;
    Button btnNext,btnSkip,btnAddKid;
    String strPopName,strPopDOB,strPopMobile,strPopGender;
    Spinner spnGender1,spnGender2;
    EditText edtPopName,edtPopMobile,edtPopDOB,edtPopGender;
    Spinner spnGender,spnSpGender;
    TextView txtPopGender;
    Button btnSave;
    int mYear,mMonth,mDay;
    String strFinalKids,strPoGender;
    private Context mContext;
    private Activity mActivity;
    private RelativeLayout mRelativeLayout;
    private PopupWindow mPopupWindow;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.rlMain);
        spnSpGender = (Spinner) findViewById(R.id.spnSpGender);

        mContext = getApplicationContext();
        mActivity = PersonalInfoActivity.this;


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setTitle(" Rotary Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        strName = preferences.getString("Number","empty");
        strAge = preferences.getString("Age","empty");
        strDOB = preferences.getString("DOB","empty");
        strMobile = preferences.getString("Mobile","empty");
        strPassword = preferences.getString("Password","empty");
        strMemberID = preferences.getString("Member","empty");
        strOrgnName = preferences.getString("OrgnName","empty");
        strClassification = preferences.getString("Classification","empty");
        strVocation = preferences.getString("Vocation","empty");
        strSubVocation = preferences.getString("SubVocation","empty");

        edtSpouseName = (EditText) findViewById(R.id.input_spouseName);
        edtSpouseNumber = (EditText) findViewById(R.id.input_spouseNumber);
        edtSpouseBirthday = (EditText) findViewById(R.id.input_spouseBday);
        edtAnniversaryDate = (EditText) findViewById(R.id.input_anniversaryDate);


        inptSpouseName = (TextInputLayout) findViewById(R.id.input_layout_spouseName);
        inptSpouseNumber = (TextInputLayout) findViewById(R.id.input_layout_spouseNumber);
        inptSpouseBirthday = (TextInputLayout) findViewById(R.id.input_layout_spouseBday);
        inptAnniversaryDate = (TextInputLayout) findViewById(R.id.input_layout_anniversaryDate);


        edtSpouseName.addTextChangedListener(new MyTextWatcher(edtSpouseName));
        edtSpouseNumber.addTextChangedListener(new MyTextWatcher(edtSpouseNumber));
        edtSpouseBirthday.addTextChangedListener(new MyTextWatcher(edtSpouseBirthday));
        edtAnniversaryDate.addTextChangedListener(new MyTextWatcher(edtAnniversaryDate));


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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        edtSpouseBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                {

                    inptSpouseBirthday.setHint("Spouse Birthday");
                }
            }
        });

        edtAnniversaryDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                {
                    inptAnniversaryDate.setHint("Wedding Anniversary");
                }
            }
        });


        btnNext = (Button) findViewById(R.id.btn_next);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnAddKid = (Button) findViewById(R.id.btnAddKid);
        //btnAddKid.setActivated(false);

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
                edtPopDOB = (EditText) dialog.findViewById(R.id.edtKidDOB);
                spnGender = (Spinner) dialog.findViewById(R.id.spnGender);
                txtPopGender = (TextView) dialog.findViewById(R.id.txtGender);
                btnSave = (Button) dialog.findViewById(R.id.btnSave);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


                edtPopDOB.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        final int DRAWABLE_RIGHT = 2;

                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (event.getRawX() >= (edtPopDOB.getRight() - edtPopDOB.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                // your action here
                                final Calendar mcurrentDate = Calendar.getInstance();
                                mYear = mcurrentDate.get(Calendar.YEAR);
                                mMonth = mcurrentDate.get(Calendar.MONTH);
                                String month = String.valueOf(mMonth);

                                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                                DatePickerDialog mDatePicker = new DatePickerDialog(PersonalInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                        selectedmonth = selectedmonth + 1;
                                        if (selectedmonth <= 9 && selectedday <= 9) {
                                            edtPopDOB.setText("" + "0" + selectedday + "0" + selectedmonth + +selectedyear);
                                        } else if (selectedmonth > 9 && selectedmonth <= 12) {
                                            edtPopDOB.setText("" + selectedday + +selectedmonth + +selectedyear);
                                        }
                                    }
                                }, mYear, mMonth, mDay);

                                mDatePicker.setTitle("Select date");
                                mDatePicker.show();
                                return true;
                            }
                        }
                        return false;
                    }
                });


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
                        strPopDOB = edtPopDOB.getText().toString().trim();
                        strPopMobile = edtPopMobile.getText().toString().trim();


                        strFinalKids  =  "Kid :" + strPopName +"," + strPopMobile + "," + strPopDOB + "," + strPopGender + "##";
                        strFinalKids  =  strFinalKids + "Kid :" + strPopName +"," + strPopMobile + "," + strPopDOB + "," + strPopGender + "##";

                        //Toast.makeText(mContext, strPopName+" Successfully Added", Toast.LENGTH_SHORT).show();
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
                strSpouseBirthday = edtSpouseBirthday.getText().toString().trim();
                strAnniversaryDate = edtAnniversaryDate.getText().toString().trim();


               if (strMemberID != null){
                    SharedPreferences preferences1 = getApplicationContext().getSharedPreferences("pref1",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences1.edit();
                    editor.putString("RCTP_ID",strMemberID);
                    editor.putString("NAME",strName);
                    editor.putString("MOBILENO",strMobile);
                    editor.putString("AGE",strAge);
                    editor.putString("DOB",strDOB);
                    editor.putString("Password",strPassword);
                    editor.putString("ORGANIZATION",strOrgnName);
                    editor.putString("CLASSIFICATION",strClassification);
                    editor.putString("VOCATION",strVocation);
                    editor.putString("SUBVOCATION",strSubVocation);
                    editor.putString("SPOUSENAME",strSpouseName);
                    editor.putString("SPOUSENUMEBR",strSpouseNumber);
                    editor.putString("SPOUSEBIRTHDATE",strSpouseBirthday);
                    editor.putString("SPOUSEGENDER",strSpGender);
                    editor.putString("ANNIVERSARYDATE",strAnniversaryDate);
                    editor.putString("KIDS_DETAILS",strFinalKids);
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(),ProfileImageActivity.class);
                    startActivity(intent);

                }else {
                    Toast.makeText(PersonalInfoActivity.this, "Please Enter Correct Data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences1 = getApplicationContext().getSharedPreferences("pref1",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences1.edit();
                editor.putString("Member",strMemberID);
                editor.putString("Name",strName);
                editor.putString("Mobile",strMobile);
                editor.putString("Age",strAge);
                editor.putString("DOB",strDOB);
                editor.putString("OrgnName",strOrgnName);
                editor.putString("Classification",strClassification);
                editor.putString("Vocation",strVocation);
                editor.putString("SubVocation",strSubVocation);
                editor.apply();

                Intent intent = new Intent(getApplicationContext(),ProfileImageActivity.class);
                startActivity(intent);
            }
        });

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
                case R.id.input_spouseBday:
                    //validateSpouseBday();
                    break;
                case R.id.input_anniversaryDate:
                    //validateAnniversaryDate();
                    break;
            }
        }
    }
}
