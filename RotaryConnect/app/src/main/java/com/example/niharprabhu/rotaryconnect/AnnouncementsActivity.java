package com.android.aquagiraffe.rotaryconnect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class AnnouncementsActivity extends AppCompatActivity {
    SessionManager session;
    ImageView add;
    ListView listView;
    ArrayList<DataModel> dataModels;
    PdfAdapter adapter;
    String FilePath,strTitle;
    final Context context = this;
    TextView txtPdfname,txtPdfPlace;
    EditText edtTitle,edtPlace,edtDescription;
    Button btnBrowse,btnUpload;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        session = new SessionManager(getApplicationContext());

        add = (ImageView) findViewById(R.id.Img);
        listView = (ListView) findViewById(R.id.listview);


        CustomList adapter = new CustomList(AnnouncementsActivity.this, web, imageId,place);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(AnnouncementsActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setTitle(" Announcements");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
