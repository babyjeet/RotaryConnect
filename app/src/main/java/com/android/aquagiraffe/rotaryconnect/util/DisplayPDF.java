package com.android.aquagiraffe.rotaryconnect.util;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.aquagiraffe.rotaryconnect.CheckForSDCard;
import com.android.aquagiraffe.rotaryconnect.PdfNewsLetterActivity;
import com.android.aquagiraffe.rotaryconnect.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class DisplayPDF extends AppCompatActivity implements EasyPermissions.PermissionCallbacks
{

    WebView webView;
    ProgressBar progressBar;
    Button btn_download;
    String path;
    private static final int WRITE_REQUEST_CODE = 300;

    private static final String TAG = PdfNewsLetterActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pdf);
        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progress_bar);

        Intent i = this.getIntent();
        path = i.getExtras().getString("PATH");

        webView = new WebView(DisplayPDF.this);


        btn_download = findViewById(R.id.btn_download);



        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);


        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        webView.setWebChromeClient(new WebChromeClient()
        {
            public void onProgressChanged(WebView view, int progress)
            {
                setTitle("Loading...");
                setProgress(progress * 100);

                if (progress == 100)
                    setTitle(R.string.app_name);
            }
        });

        webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + path);
        setContentView(webView);

    }

    public void pdfDownload()
    {
        if (CheckForSDCard.isSDCardPresent())
        {
            //check if app has permission to write to the external storage.
            if (EasyPermissions.hasPermissions(DisplayPDF.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                //Get the URL entered
                new DownloadFile().execute(path);
                // showPdf();

            }
            else
            {
                //If permission is not present request for the same.
                EasyPermissions.requestPermissions(DisplayPDF.this, getString(R.string.write_file), WRITE_REQUEST_CODE, android.Manifest.permission.READ_EXTERNAL_STORAGE);
            }

        }
        else
        {
            Toast.makeText(getApplicationContext(),"SD Card not found", Toast.LENGTH_LONG).show();

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, DisplayPDF.this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms)
    {
        String path2 = "https://s3-ap-southeast-1.amazonaws.com/awsaquabucket/P_Rotary/PDF/NewsLatter/"+path;
        new DownloadFile().execute(path);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms)
    {

    }

    private class DownloadFile extends AsyncTask<String, String, String>
    {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;
        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(DisplayPDF.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url)
        {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());
                //Append timestamp to file name
                fileName = timestamp + "_" + fileName;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "DownloadPDf/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists())
                {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1)
                {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    // Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;

            } catch (Exception e)
            {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress)
        {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message)
        {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();
            // ViewPDf(strUri2);

            // Display File path after downloading
            Toast.makeText(getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();
        }
    }

//    @Override
//    public void onBackPressed()
//    {
//        super.onBackPressed();
//        Intent intent = new Intent(DisplayPDF.this, NewsletterActivity.class);
//        startActivity(intent);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);
        menu.findItem(R.id.action_download).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_download :

                pdfDownload();
        }
        return super.onOptionsItemSelected(item);
    }
}





