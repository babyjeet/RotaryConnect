package com.android.aquagiraffe.rotaryconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.aquagiraffe.rotaryconnect.adapter.MyCustomPagerAdapter;
import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ViewerPager extends AppCompatActivity{

    ViewPager viewPager;
    MyCustomPagerAdapter myCustomPagerAdapter;
    String[] imagenameArray;
    Toolbar toolbar;
    ImageView back,imgShare;
    DotsIndicator dotsIndicator;
    SessionManager session;
    TextView txtAppbar;
    String name,currentname;
    String imageURLOri ="http://velmm.com/images/home/velmm_icon.png";
    ProgressDialog progressDialog;
    String imageURL ="https://s3-ap-southeast-1.amazonaws.com/awsaquabucket/images/";
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer_pager);

        imgShare = findViewById(R.id.imgShare);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        Fresco.initialize(this);
        txtAppbar = findViewById(R.id.txtAppbar);

        if (ActivityCompat.checkSelfPermission(ViewerPager.this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ViewerPager.this, new String[]{WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
        }

        session = new SessionManager(getApplicationContext());

        back = findViewById(R.id.imgback);

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ViewerPager.this, GalleryActivity.class);
                startActivity(intent);
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        viewPager = (ViewPager)findViewById(R.id.viewPager);

        dotsIndicator = (DotsIndicator) findViewById(R.id.dots_indicator);

        Intent intent = getIntent();
        String data=  intent.getStringExtra("data");

        String strprofileName = intent.getStringExtra("profileName");
        Log.d("strprofileName",strprofileName);

        String title = intent.getStringExtra("title");
        txtAppbar.setText(title);

        imagenameArray = data.split(",");

        String i = intent.getStringExtra("p");

        for (int j=0; j<imagenameArray.length ; j++ )
        { name =  imagenameArray[j];
        }

        currentname = imagenameArray[Integer.parseInt(i)];


        myCustomPagerAdapter = new MyCustomPagerAdapter(ViewerPager.this, imagenameArray, strprofileName);
        viewPager.setAdapter(myCustomPagerAdapter);
        viewPager.setCurrentItem(Integer.parseInt(i));
        dotsIndicator.setViewPager(viewPager);



        final int[] maintain = {0};
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                maintain[0] = i;
                currentname = Arrays.toString(new String[]{imagenameArray[i]});

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    shareImage();
            }
        });

    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        String root = Environment.getExternalStorageDirectory().toString();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(ViewerPager.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.setTitle("Downloading image for you...");
                progressDialog.setCancelable(false);
            }

            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {

                System.out.println("Downloading");
                URL url = new URL(f_url[0]);

                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                root = Environment.getExternalStorageDirectory() + File.separator + "DownloadedImages/";

                File directory = new File(root);

                if (!directory.exists())
                {
                    directory.mkdirs();
                }

                // Output stream to write file
                currentname = currentname.replaceAll("\\[", "").replaceAll("\\]","");
                String[] parts = currentname.split("\\.");
                String part1 = parts[0];
                OutputStream output = new FileOutputStream(root+"/"+part1+".jpg");
                byte data[] = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // writing data to file
                    output.write(data, 0, count);

                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());

            }

            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {

            Toast.makeText(ViewerPager.this, "Image downloaded", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }
    private void shareImage() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        currentname = currentname.replaceAll("\\[", "").replaceAll("\\]","");
        String imagePath = imageURL+currentname;
        share.putExtra(android.content.Intent.EXTRA_TEXT, imagePath);
        startActivity(Intent.createChooser(share, "Share image to..."));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.download_image).setVisible(true);
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

            case R.id.download_image:
                if (ActivityCompat.checkSelfPermission(ViewerPager.this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"No Permission",Toast.LENGTH_SHORT).show();
                } else {
                    currentname = currentname.replaceAll("\\[", "").replaceAll("\\]","");
                    if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                        new DownloadFileFromURL().execute(imageURL+currentname);
                    } else {
                        Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
