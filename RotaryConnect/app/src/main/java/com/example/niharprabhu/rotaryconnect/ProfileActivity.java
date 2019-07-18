package com.android.aquagiraffe.rotaryconnect;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {
    private EditText edtName,edtAge,edtDOB,edtMobile,edtMemberID,edtOrgnName,edtClassification,edtVocation,edtSubVocation,edtPassword;
    private TextInputLayout inptLayoutMemberID,inptLayoutName,inptLayoutMobile,inptLayoutDOB,inptLayoutAge,inptLayoutPassword,inptLayoutOrgnName,inptLayoutClassification,inptLayoutVocation,inptLayoutSubVocation;
    String strName,strAge,strDOB,strMobile,strMemberID,strOrgnName,strClassification,strVocation,strSubVocation,strPassword;
    int mYear,mMonth,mDay;
    private Button btnSubmit;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setTitle(" Rotary Registration");
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        edtName = (EditText) findViewById(R.id.input_name);
        edtAge = (EditText) findViewById(R.id.input_age);
        edtDOB = (EditText) findViewById(R.id.input_dob);
        edtMobile = (EditText) findViewById(R.id.input_mobile);
        edtMemberID = (EditText) findViewById(R.id.input_memberid);
        edtPassword = (EditText) findViewById(R.id.input_password);
        edtOrgnName = (EditText) findViewById(R.id.input_orgnname);
        edtClassification = (EditText) findViewById(R.id.input_classification);
        edtVocation = (EditText) findViewById(R.id.input_vocation);
        edtSubVocation = (EditText) findViewById(R.id.input_subvocation);

        inptLayoutMemberID = (TextInputLayout) findViewById(R.id.input_layout_memberid);
        inptLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inptLayoutMobile = (TextInputLayout) findViewById(R.id.input_layout_mobile);
        inptLayoutDOB = (TextInputLayout) findViewById(R.id.input_layout_dob);
        inptLayoutAge = (TextInputLayout) findViewById(R.id.input_layout_age);
        inptLayoutPassword  = (TextInputLayout) findViewById(R.id.input_layout_password);
        inptLayoutOrgnName = (TextInputLayout) findViewById(R.id.input_layout_orgnname);
        inptLayoutClassification = (TextInputLayout) findViewById(R.id.input_layout_classification);
        inptLayoutVocation = (TextInputLayout) findViewById(R.id.input_layout_vocation);
        inptLayoutSubVocation = (TextInputLayout) findViewById(R.id.input_layout_subvocation);


        edtName.addTextChangedListener(new MyTextWatcher(edtName));
        edtAge.addTextChangedListener(new MyTextWatcher(edtAge));
        edtDOB.addTextChangedListener(new MyTextWatcher(edtDOB));
        edtMobile.addTextChangedListener(new MyTextWatcher(edtMobile));
        edtMemberID.addTextChangedListener(new MyTextWatcher(edtMemberID));
        edtPassword.addTextChangedListener(new MyTextWatcher(edtPassword));
        edtOrgnName.addTextChangedListener(new MyTextWatcher(edtOrgnName));
        edtClassification.addTextChangedListener(new MyTextWatcher(edtClassification));
        edtVocation.addTextChangedListener(new MyTextWatcher(edtVocation));
        edtSubVocation.addTextChangedListener(new MyTextWatcher(edtSubVocation));

        edtDOB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (edtDOB.getRight() - edtDOB.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        final Calendar mcurrentDate = Calendar.getInstance();
                        mYear = mcurrentDate.get(Calendar.YEAR);
                        mMonth = mcurrentDate.get(Calendar.MONTH);
                        String month = String.valueOf(mMonth);

                        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog mDatePicker=new DatePickerDialog(ProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                selectedmonth = selectedmonth+1;
                                if (selectedmonth <= 9 && selectedday <= 9) {
                                    edtDOB.setText("" + "0" + selectedday + "0" + selectedmonth + +selectedyear);
                                }else if (selectedmonth > 9 && selectedmonth <= 12){
                                    edtDOB.setText("" + selectedday +  + selectedmonth + +selectedyear);
                                }
                            }
                        },mYear, mMonth, mDay);

                        mDatePicker.setTitle("Select date");
                        mDatePicker.show();
                        return true;
                    }
                }
                return false;
            }
        });

        edtDOB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                {
                    inptLayoutDOB.setHint("Date of Birth");
                }
            }
        });

        btnSubmit = (Button) findViewById(R.id.btn_next);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strMemberID = edtMemberID.getText().toString().trim();
                strName = edtName.getText().toString().trim();
                strMobile = edtMobile.getText().toString().trim();
                strAge = edtAge.getText().toString().trim();
                strDOB = edtDOB.getText().toString().trim();
                strOrgnName = edtOrgnName.getText().toString().trim();
                strPassword = edtPassword.getText().toString().trim();
                strClassification = edtClassification.getText().toString().trim();
                strVocation = edtVocation.getText().toString().trim();
                strSubVocation = edtSubVocation.getText().toString().trim();


                if (validateName() && validateMemberID() && validateMobile() && validateDOB() && validateAge() && validateOrgnName() && validateClassification() && validateVocation() && validateSubVocation()){
                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("Member",strMemberID);
                    editor.putString("Name",strName);
                    editor.putString("Mobile",strMobile);
                    editor.putString("Age",strAge);
                    editor.putString("DOB",strDOB);
                    editor.putString("Password",strPassword);
                    editor.putString("OrgnName",strOrgnName);
                    editor.putString("Classification",strClassification);
                    editor.putString("Vocation",strVocation);
                    editor.putString("SubVocation",strSubVocation);
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(),PersonalInfoActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    private boolean validateName() {
        if (edtName.getText().toString().trim().isEmpty()) {
            inptLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(edtName);
            return false;
        } else {
            inptLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateMemberID() {
        if (edtMemberID.getText().toString().trim().length() != 7) {
            inptLayoutMemberID.setError(getString(R.string.err_msg_memberid));
            requestFocus(edtMemberID);
            return false;
        } else {
            inptLayoutMemberID.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateMobile() {
        if (edtMobile.getText().toString().trim().length() != 10) {
            inptLayoutMobile.setError(getString(R.string.err_msg_mobile));
            requestFocus(edtMobile);
            return false;
        } else {
            inptLayoutMobile.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateDOB() {
        if (edtDOB.getText().toString().trim().isEmpty()) {
            inptLayoutDOB.setError(getString(R.string.err_msg_dob));
            requestFocus(edtDOB);
            return false;
        } else {
            inptLayoutDOB.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAge() {
        if (edtAge.getText().toString().trim().isEmpty()) {
            inptLayoutAge.setError(getString(R.string.err_msg_age));
            requestFocus(edtAge);
            return false;
        } else {
            inptLayoutAge.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateOrgnName() {
        if (edtOrgnName.getText().toString().trim().isEmpty()) {
            inptLayoutOrgnName.setError(getString(R.string.err_msg_orgnname));
            requestFocus(edtOrgnName);
            return false;
        } else {
            inptLayoutOrgnName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateClassification() {
        if (edtClassification.getText().toString().trim().isEmpty()) {
            inptLayoutClassification.setError(getString(R.string.err_msg_classification));
            requestFocus(edtClassification);
            return false;
        } else {
            inptLayoutClassification.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateVocation() {
        if (edtVocation.getText().toString().trim().isEmpty()) {
            inptLayoutVocation.setError(getString(R.string.err_msg_vocation));
            requestFocus(inptLayoutVocation);
            return false;
        } else {
            inptLayoutClassification.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateSubVocation() {
        if (edtSubVocation.getText().toString().trim().isEmpty()) {
            inptLayoutSubVocation.setError(getString(R.string.err_msg_subvocation));
            requestFocus(inptLayoutSubVocation);
            return false;
        } else {
            inptLayoutSubVocation.setErrorEnabled(false);
        }

        return true;
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
                default:
                    break;
            }
        }
    }
}
