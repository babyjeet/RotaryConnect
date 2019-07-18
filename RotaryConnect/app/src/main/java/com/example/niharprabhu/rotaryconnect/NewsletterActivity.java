package com.android.aquagiraffe.rotaryconnect;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class NewsletterActivity extends AppCompatActivity {
    SessionManager session;
    EditText edtPopNewsTitle;
    Spinner spnGender;
    ImageView add,next;
    ListView listView;
    public static final String UPLOAD_URL = "http://internetfaqs.net/AndroidPdfUpload/upload.php";
    private int PICK_PDF_REQUEST = 1;
    ArrayList<DataModel> dataModels;
    String strPopNewsTitle;
    ImageButton imgChoose;
    String FilePath;
    final Context context = this;
    private Uri filePath;
    TextView txtPdfname;
    Button btnUpload;
    Uri path;

    String[] web = {
            "Drawing competition",
            "Visit to Old Age Home",
            "Bag Distribution",
            "Polio Sunday",
            "Blood Donation","Sapling Bank plus Tree Plantation"
    } ;
    Integer[] imageId = {
            R.drawable.pdf,
            R.drawable.pdf,
            R.drawable.pdf,
            R.drawable.pdf,
            R.drawable.pdf,
            R.drawable.pdf

    };
    String[] place = {
            "Bhagwati School,Thane",
            "Ghodbunder Road,Thane",
            "KBP Degree College",
            "Wagle Estate & Shree Nagar",
            "Hypercity Mall,Thane",
            "Kopri,Thane East"
    } ;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsletter);

        session = new SessionManager(getApplicationContext());
        listView = (ListView) findViewById(R.id.listview);
        add = (ImageView) findViewById(R.id.Img);


        CustomList adapter = new CustomList(NewsletterActivity.this, web, imageId,place);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(NewsletterActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View view) {
                final Dialog dialog;
                dialog = new Dialog(NewsletterActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().getAttributes().windowAnimations = R.style.popup_window_animation_phone;
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(
                                android.graphics.Color.TRANSPARENT
                        )

                );
                dialog.setContentView(R.layout.popup_newsletter);

                edtPopNewsTitle = (EditText) dialog.findViewById(R.id.edtNewsName);
                imgChoose = (ImageButton) dialog.findViewById(R.id.imgUpload);
                btnUpload = (Button) dialog.findViewById(R.id.btnSave);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                imgChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showFileChooser();
                    }
                });


                btnUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        strPopNewsTitle = edtPopNewsTitle.getText().toString().trim();


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

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setTitle(" Newsletter");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK)
        {
            if (requestCode == 1)
            {
                path = data.getData();
                FilePath = data.getData().getPath();

                String  resultName = FilePath.substring(FilePath.lastIndexOf("."));
                txtPdfname.setText(resultName);

            }
        }
    }


    private ArrayList<DataModel> getPDFs()

    {
        ArrayList<DataModel> pdfDocs=new ArrayList<>();
        //TARGET FOLDER
        File downloadsFolder= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        DataModel pdfDoc;

        if(downloadsFolder.exists())
        {
            //GET ALL FILES IN DOWNLOAD FOLDER
            File[] files=downloadsFolder.listFiles();

            //LOOP THRU THOSE FILES GETTING NAME AND URI
            for (int i=0;i<files.length;i++)
            {
                File file=files[i];
/*
                if(file.getPath().endsWith("pdf")) {
                }*/
                pdfDoc=new DataModel();
                pdfDoc.setPdfname(file.getName());
                pdfDoc.setPdfPath(file.getAbsolutePath());

                pdfDocs.add(pdfDoc);


            }
        }

        return pdfDocs;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                session.logoutUser();
                break;
            case R.id.action_home:
                Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
