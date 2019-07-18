package com.android.aquagiraffe.rotaryconnect;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.android.aquagiraffe.rotaryconnect.adapter.BookAdapter;
import com.android.aquagiraffe.rotaryconnect.adapter.RecyclerViewPDFAdapter;
import com.android.aquagiraffe.rotaryconnect.adapter.RotaryPdfViewAdapter;
import com.android.aquagiraffe.rotaryconnect.model.BookModel;
import com.android.aquagiraffe.rotaryconnect.model.PdfDetailsWithImage;
import com.android.aquagiraffe.rotaryconnect.model.PdfTitleModel;
import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.util.Arrays;
import java.util.HashMap;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class RotaryLibraryActivity extends AppCompatActivity
{
    SessionManager session;
    private ListView listDialog;
    ArrayList<String> filePaths = new ArrayList<>();
    ArrayList<String> NAMESARRAY = new ArrayList<>();
    ImageView imageView, img_pdf_upload;
    String pdfName, pdf_info, pdf_Name2;
    ProgressDialog progressDialog;
    private final String KEY = "AKIAIHL5HIJLUSY7QQSQ";
    private final String SECRET = "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";
    private AmazonS3Client s3Client;
    private BasicAWSCredentials credentials;
    TransferObserver uploadServer;
    Toolbar toolbar;
    final Context context = this;
    ImageView imgClose,back,img_alert;
    Button btnSubmit;
    EditText edtBookname;
    String strBookName, list;
    String[] pdf_nameArray;
    String pdf_Name, p_name;
    RotaryPdfViewAdapter adapter;
    GridView gridView;
    String strResult,strUserType,strMessage = "", strFetchMessage;
    HashMap<String, String> hashMap = new HashMap<>();
    ProgressBar myProgressBar;
    TextView txtBook, txt_upload;
    RecyclerView recyclerView;
    private Typeface typeface;
    RecyclerViewPDFAdapter recyclerViewPDFAdapter;
    private ArrayList<PdfTitleModel> allSampleData;
    ArrayList<BookModel> bookModelArrayList;
    AlertDialogManager alert;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotary_library);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
        typeface = Typeface.createFromAsset(getAssets(), "RobotoBold.ttf");

        session = new SessionManager(getApplicationContext());
        AWSMobileClient.getInstance().initialize(this).execute();

        credentials = new BasicAWSCredentials(KEY, SECRET);
        s3Client = new AmazonS3Client(credentials);
        txtBook = findViewById(R.id.txtBook);
        txtBook.setTypeface(typeface);
        myProgressBar = findViewById(R.id.myProgressBar);
        imageView = findViewById(R.id.ImgBook);

        hashMap = session.getUserDetails();
        strUserType = hashMap.get(SessionManager.USER_TYPE);
        RelativeLayout relativeLayout = findViewById(R.id.grid_relative);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();

        if (strUserType.equalsIgnoreCase("0"))
        {
            txtBook.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            params.setMargins(0,10,0,0);
            relativeLayout.setLayoutParams(params);
        }
        back = findViewById(R.id.imgback);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RotaryLibraryActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        PdfAsyncFetchShow pdfAsyncFetchShow = new PdfAsyncFetchShow();
        pdfAsyncFetchShow.execute();

        recyclerView = findViewById(R.id.my_recycler_library_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        allSampleData = new ArrayList<>();
        recyclerViewPDFAdapter = new RecyclerViewPDFAdapter(allSampleData,this);

        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.pdf_dialog);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setTitle("");
                dialog.getWindow().getAttributes().windowAnimations = R.style.popup_window_animation_phone;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                edtBookname = dialog.findViewById(R.id.edtPdfName);
                edtBookname.setTypeface(typeface);
                //progressBar = dialog.findViewById(R.id.progressbar);

                imgClose = dialog.findViewById(R.id.imgCross);

                img_alert = dialog.findViewById(R.id.img_alert);
                listDialog = dialog.findViewById(R.id.list);

                btnSubmit = dialog.findViewById(R.id.btnSubmit);

                txt_upload = dialog.findViewById(R.id.txt_upload);
                txt_upload.setTypeface(typeface);
                imgClose.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                    }
                });
                img_pdf_upload = dialog.findViewById(R.id.pdf_upload);

                img_pdf_upload.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            FilePickerBuilder.getInstance().setMaxCount(15)
                                    .setSelectedFiles(filePaths)
                                    .setActivityTheme(R.style.LibAppTheme)
                                    .pickFile(RotaryLibraryActivity.this);
                        }
                        catch (Exception e)
                        {
                            Log.d("Onselect_Exception",String.valueOf(e));
                        }
                    }
                });

                btnSubmit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        strBookName = edtBookname.getText().toString();
                        if(AppStatus.getInstance(getApplicationContext()).isOnline()) {
                            if (filePaths.size() != 0)
                            {
                                img_alert.setVisibility(View.INVISIBLE);
                                if (strBookName.equals(""))
                                {
                                    img_alert.setVisibility(View.VISIBLE);
                                    Toast.makeText(RotaryLibraryActivity.this, "Please  enter the book name", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    img_alert.setVisibility(View.INVISIBLE);
                                    for (int i = 0; i < filePaths.size(); i++)
                                    {
                                        uploadFile(String.format(filePaths.get(i)));
                                        File f = new File(filePaths.get(i));
                                        String pdfName = f.getName();
                                        String PdfName = pdfName.replaceAll("[^a-zA-Z0-9]", "_");
                                        NAMESARRAY.add(PdfName);

                                        list = Arrays.toString(NAMESARRAY.toArray()).replace("[", "").replace("]", "");
                                    }

                                    PdfAsync pdfAsync = new PdfAsync();
                                    pdfAsync.execute();

                                    updateListener();
                                    NAMESARRAY.clear();

                                    allSampleData.clear();

                                    PdfAsyncFetchShow pdfAsyncFetchShow = new PdfAsyncFetchShow();
                                    pdfAsyncFetchShow.execute();
                                    dialog.dismiss();
                                }

                            } else
                            {
                                img_alert.setVisibility(View.VISIBLE);
                                Toast.makeText(RotaryLibraryActivity.this, "No such Pdf selected", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            alert = new AlertDialogManager();
                            alert.InternateConnection(RotaryLibraryActivity.this,"Internet Connection Lost","Please check your internate connection",false);
                        }
                    }
                });
                dialog.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case FilePickerConst.REQUEST_CODE_DOC:
                if (resultCode == RESULT_OK && data != null)
                {
                    filePaths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS);
                    BookModel bookModel;
                    String path = null;

                    bookModelArrayList = new ArrayList<>();

                    for (int i = 0; i < filePaths.size(); i++)
                    {
                        bookModel = new BookModel();
                        path = filePaths.get(i);
                        path.substring(path.lastIndexOf("/"));
                        bookModel.setUri(Uri.fromFile(new File(path)));
                        bookModel.setPath(path);

                        File file = new File(path);
                        pdfName = file.getName();
                        pdfName = pdfName.replace(" ", "");

                        bookModel.setName(file.getName().replace(" ", ""));
                        bookModel.setPath(file.getPath());
                        bookModelArrayList.add(bookModel);
                    }

                    listDialog.setAdapter(new BookAdapter(this, bookModelArrayList,filePaths));
                    recyclerView.setAdapter(recyclerViewPDFAdapter);
                }
        }
    }

    private void uploadFile(String PdfPath)
    {
        String pdfName = PdfPath.substring(PdfPath.lastIndexOf("/") + 1);

        pdfName = pdfName.replaceAll("[^a-zA-Z0-9]", "_");

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();

        uploadServer = transferUtility.upload("P_Rotary/PDF/RotaryLibrary/" + pdfName, new File(PdfPath), CannedAccessControlList.PublicRead);


    }

    private void updateListener()
    {
        uploadServer.setTransferListener(new TransferListener()
        {
            @Override
            public void onStateChanged(int id, TransferState state)
            {
                if (TransferState.COMPLETED == state)
                {

                    Toast.makeText(RotaryLibraryActivity.this, "pdf upload", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

            }

            @Override
            public void onError(int id, Exception ex)
            {
                Toast.makeText(RotaryLibraryActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });

    }


    class PdfAsync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection conn = null;
            URL url = null;

            try {
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx/WS_Pdf_Upload");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);


                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("RCTP_APIkey", Constants.P_KEY)
                        .appendQueryParameter("Pdf_Name", list)
                        .appendQueryParameter("pdf_info", strBookName)
                        .appendQueryParameter("pdf_type", "RotaryLibrary")
                        .appendQueryParameter("pdf_Message",strMessage);


                String query = builder.build().getEncodedQuery();
                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();


            } catch (IOException e) {
                e.printStackTrace();
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
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            progressDialog.dismiss();
        }
    }

    public class PdfAsyncFetchShow extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RotaryLibraryActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setTitle("Getting things ready for you...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection conn = null;
            URL url = null;

            try {
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx/WS_Get_PDF");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);


                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("RCTP_APIkey", Constants.P_KEY)
                        .appendQueryParameter("pdf_type", Constants.ROTARYBOOK);


                String query = builder.build().getEncodedQuery();
                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();


            } catch (IOException e) {
                e.printStackTrace();
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
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Runnable progressRunnable = new Runnable()
            {

                @Override
                public void run() {
                    progressDialog.cancel();
                }
            };

            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 1000);

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("pref2", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("s", s);
            editor.apply();

            getPdf();

        }
    }

    public void getPdf()
    {
        try {
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("pref2", Context.MODE_PRIVATE);
            strResult = preferences.getString("s", "empty");

            JSONObject jsonObject = new JSONObject(strResult);
            JSONArray jsonArray = jsonObject.getJSONArray("PDFList");

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject object = (JSONObject) jsonArray.get(i);

                PdfTitleModel pdfTitleModel = new PdfTitleModel();
                PdfDetailsWithImage pdfDetailsWithImage = new PdfDetailsWithImage();
                ArrayList<PdfDetailsWithImage> arrayItemModels = new ArrayList<>();

                pdf_info = object.getString("pdf_info");
                pdfTitleModel.setHeaderTitle(pdf_info);

                pdf_Name = object.getString("Pdf_Name");

                strFetchMessage = object.getString("pdf_Message");

                pdf_Name2 = pdf_Name.replaceAll("\\s+", "");

                pdf_nameArray = pdf_Name2.split(",");

                for (int k = 0; k < pdf_nameArray.length; k++)
                {

                    p_name = pdf_nameArray[k];

                    pdfDetailsWithImage = new PdfDetailsWithImage();
                    pdfDetailsWithImage.setPdf_Name(p_name);
                    arrayItemModels.add(pdfDetailsWithImage);
                }
                pdfTitleModel.setAllItemInSection(arrayItemModels);
                allSampleData.add(pdfTitleModel);
                recyclerViewPDFAdapter.notifyDataSetChanged();
            }
            recyclerView.setAdapter(recyclerViewPDFAdapter);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
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
