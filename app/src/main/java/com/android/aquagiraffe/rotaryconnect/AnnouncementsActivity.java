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
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
import com.android.aquagiraffe.rotaryconnect.adapter.AnnounsmentAdapter;
import com.android.aquagiraffe.rotaryconnect.adapter.RecyclerViewPDFAdapter;
import com.android.aquagiraffe.rotaryconnect.model.PdfDetailsWithImage;
import com.android.aquagiraffe.rotaryconnect.model.PdfModel;
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

public class AnnouncementsActivity extends AppCompatActivity
{
    SessionManager session;
    ArrayList<String> filePaths = new ArrayList();
    ArrayList<PdfModel> pdfModelArrayList;
    ArrayList<String> NAMESARRAY = new ArrayList<>();
    HashMap<String,String> hashMap;
    String Pdf_Name_List [];
    String name[];
    String pdf_info,pdf_Name,strUserType , strMessage, strFetchMessage;
    String strResult;
    ListView listview;
    ImageView imageView, img_pdf_upload,img_cross, img_alert, back;
    EditText edit_Announcement,edt_Message;
    GridView grid;
    Toolbar toolbar;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    Button btnUpload, btn_cross_upload;
    TextView txt_upload,txtTitle, txt_title;
    TransferObserver uploadObserver;
    String strpdfname, list,pdfname;
    private final String KEY = "AKIAIHL5HIJLUSY7QQSQ";
    private final String SECRET = "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";
    private AmazonS3Client s3Client;
    private BasicAWSCredentials credentials;
    File file;
    final Context context = this;
    Boolean Flag , UploadPDF = false , ClickPDf=false;
    private Typeface typeface;
    RecyclerView recyclerView;
    RecyclerViewPDFAdapter recyclerViewPDFAdapter;
    private ArrayList<PdfTitleModel> allSampleData;
    AlertDialogManager alert;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        typeface = Typeface.createFromAsset(getAssets(), "RobotoBold.ttf");
        imageView = findViewById(R.id.Img);

        session = new SessionManager(getApplicationContext());

        AWSMobileClient.getInstance().initialize(this).execute();

        credentials = new BasicAWSCredentials(KEY, SECRET);
        s3Client = new AmazonS3Client(credentials);

        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setTypeface(typeface);
        hashMap = session.getUserDetails();
        strUserType = hashMap.get(SessionManager.USER_TYPE);

        if(strUserType.equalsIgnoreCase("0"))
        {
            txtTitle.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        }
        back = findViewById(R.id.imgback);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AnnouncementsActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = findViewById(R.id.my_recycler_announs_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        allSampleData = new ArrayList<>();
        pdfModelArrayList = new ArrayList<>();

        recyclerViewPDFAdapter = new RecyclerViewPDFAdapter(allSampleData,this);

        callFetchPdf();

        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.popup_newsletter);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setTitle("");
                dialog.getWindow().getAttributes().windowAnimations = R.style.popup_window_animation_phone;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                img_cross = dialog.findViewById(R.id.img_cross);
                txt_title = dialog.findViewById(R.id.txt_alertName);
                txt_title.setTypeface(typeface);
                edit_Announcement = dialog.findViewById(R.id.edt_NewsLetter);
                edit_Announcement.setTypeface(typeface);
                edt_Message = dialog.findViewById(R.id.edt_message);
                edt_Message.setTypeface(typeface);
                txt_upload = dialog.findViewById(R.id.txt_upload);
                txt_upload.setTypeface(typeface);
                img_pdf_upload = dialog.findViewById(R.id.img_pdf_upload);
                img_alert = dialog.findViewById(R.id.img_alert);
                img_cross.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                    }
                });

                listview = dialog.findViewById(R.id.list);

                progressBar = dialog.findViewById(R.id.progressbar);

                img_pdf_upload.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        FilePickerBuilder.getInstance()
                                .setMaxCount(15)
                                .setSelectedFiles(filePaths)
                                .setActivityTheme(R.style.AppTheme)
                                .pickFile(AnnouncementsActivity.this);

                        UploadPDF = true;
                    }
                });

                btnUpload = dialog.findViewById(R.id.upload);
                btn_cross_upload = dialog.findViewById(R.id.upload);
                btnUpload.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = getIntent();
                        Flag = intent.getBooleanExtra("Flag", false);

                        strpdfname = edit_Announcement.getText().toString();
                        strMessage = edt_Message.getText().toString();
                        if(AppStatus.getInstance(getApplicationContext()).isOnline())
                        {
                            img_alert.setVisibility(View.INVISIBLE);
                            if (strpdfname.equals(""))
                            {
                                img_alert.setVisibility(View.VISIBLE);
                                Toast.makeText(AnnouncementsActivity.this, "Please enter the pdf name", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                if(UploadPDF == false)
                                {
                                    NAMESARRAY.clear();
                                    list = Arrays.toString(NAMESARRAY.toArray()).replace("[", "").replace("]", "");
                                }
                                if(UploadPDF == true)
                                {
                                    UploadPDF = false;

                                    for (int i = 0; i < filePaths.size(); i++)
                                    {
                                        img_alert.setVisibility(View.INVISIBLE);

                                        if (filePaths.get(i).endsWith(".pdf"))
                                        {
                                            File file = new File(filePaths.get(i));
                                            String pdfName = file.getName();
                                            String PdfName = pdfName.replaceAll("[^a-zA-Z0-9]", "_");
                                            NAMESARRAY.add(PdfName);
                                            Log.d("NameArray", String.valueOf(NAMESARRAY));
                                            list = Arrays.toString(NAMESARRAY.toArray()).replace("[", "").replace("]", "");

                                            uploadFile(String.format(filePaths.get(i)));
                                        }

                                        updateListener();
                                    }
                                }

                                AnnouncementsActivity.PdfNewsAsync pdfAsync = new AnnouncementsActivity.PdfNewsAsync();
                                pdfAsync.execute();

                                NAMESARRAY.clear();
                                filePaths.clear();
                                pdfModelArrayList.clear();
                                allSampleData.clear();
                                callFetchPdf();
                                dialog.dismiss();
                            }
                        }
                        else
                        {
                            alert = new AlertDialogManager();
                            alert.InternateConnection(AnnouncementsActivity.this,"Internet Connection Lost","Please check your internate connection",false);
                        }
                    }
                });

                dialog.show();
            }
        });

    }

    public void callFetchPdf()
    {
        PdfAsyncShow3 pdfAsyncShow3 = new PdfAsyncShow3();
        pdfAsyncShow3.execute();
    }

    public void getPdf()
    {
        try
        {
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("pref2", Context.MODE_PRIVATE);
            strResult = preferences.getString("s", "empty");

            JSONObject jsonObject = new JSONObject(strResult);
            JSONArray jsonArray;

            jsonArray = jsonObject.getJSONArray("PDFList");

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject object = (JSONObject) jsonArray.get(i);

                PdfTitleModel pdfTitleModel = new PdfTitleModel();
                PdfDetailsWithImage pdfDetailsWithImage = new PdfDetailsWithImage();
                ArrayList<PdfDetailsWithImage> arrayItemModels = new ArrayList<>();

                pdf_Name = object.getString("Pdf_Name");
                pdf_info = object.getString("pdf_info");
                strFetchMessage = object.getString("pdf_Message");
                pdfTitleModel.setHeaderTitle(pdf_info);
                pdfTitleModel.setStrMessageAnouns(strFetchMessage);

                Pdf_Name_List = pdf_Name.split(",");

                for (int j = 0; j < Pdf_Name_List.length; j++)
                {
                    pdf_Name = Pdf_Name_List[j];

                    pdf_Name = pdf_Name.replaceAll("\\s", "");

                    pdfDetailsWithImage = new PdfDetailsWithImage();
                    pdfDetailsWithImage.setPdf_Name(pdf_Name);
                    arrayItemModels.add(pdfDetailsWithImage);
                }

                pdfTitleModel.setAllItemInSection(arrayItemModels);
                allSampleData.add(pdfTitleModel);
            }

            recyclerView.setAdapter(recyclerViewPDFAdapter);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
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

                    PdfModel pdfModel;
                    String path = null;

                    //pdfModelArrayList = new ArrayList<>();

                    for (int i = 0; i < filePaths.size(); i++)
                    {
                        if(filePaths.get(i).endsWith(".pdf") && !filePaths.isEmpty())
                        {
                            pdfModel = new PdfModel();
                            path = filePaths.get(i);

                            path.substring(path.lastIndexOf("/"));

                            pdfModel.setUri(Uri.fromFile(new File(path)));
                            pdfModel.setPath(path);

                            File file = new File(path);
                            pdfname = file.getName();

                            pdfModel.setName(file.getName());
                            pdfModel.setPath(file.getPath());
                            pdfModelArrayList.add(pdfModel);
                        }
                        else
                        {
                            Toast.makeText(context, "Please Select Any PDF File ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    for(int i=0;i<filePaths.size();i++)
                    {
                        File file = new File(filePaths.get(i));
                        String pdfName = file.getName();
                        String PdfName = pdfName.replaceAll("[^a-zA-Z0-9]","_");
                    }

                    listview.setVisibility(View.VISIBLE);

                    img_pdf_upload.setVisibility(View.INVISIBLE);

                    listview.setAdapter(new AnnounsmentAdapter(this, pdfModelArrayList,filePaths));

                    recyclerView.setAdapter(recyclerViewPDFAdapter);
                }
        }
    }

    public void uploadFile(String PdfPath)
    {
        String pdfName = PdfPath.substring(PdfPath.lastIndexOf("/") + 1);

        StringBuilder removeSpace = new StringBuilder();

        pdfName = pdfName.replaceAll("[^a-zA-Z0-9]","_");

        for (int i = 0; i<pdfName.length();i++)
        {
            if(!Character.isWhitespace(pdfName.charAt(i)))
            {
                removeSpace=removeSpace.append(pdfName.charAt(i));
            }
        }
        int j = 0;
        for(int i = 0; i < removeSpace.length(); i++)
        {
            if (!Character.isWhitespace(removeSpace.charAt(i)))
            {
                removeSpace.setCharAt(j++, removeSpace.charAt(i));
            }
        }
        removeSpace.delete(j, removeSpace.length());


        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();

        if(Flag == false)
        {
            uploadObserver = transferUtility.upload("P_Rotary/PDF/Announcement/" + removeSpace, new File(PdfPath), CannedAccessControlList.PublicRead);
        }
        else
        {
            try
            {
                uploadObserver = transferUtility.upload("P_Rotary/PDF/Announcement/" + removeSpace, new File(PdfPath), CannedAccessControlList.PublicRead);
            }
            catch (Exception e)
            {
                Log.d("e", String.valueOf(e));
            }
        }
    }

    private void updateListener()
    {
        uploadObserver.setTransferListener(new TransferListener()
        {
            @Override
            public void onStateChanged(int id, TransferState state)
            {
                if (TransferState.COMPLETED == state)
                {
                    progressBar.setVisibility(View.INVISIBLE);

                    Toast.makeText(AnnouncementsActivity.this, "Pdfs upload Succesfully", Toast.LENGTH_SHORT).show();

                    Dialog dialog2 = new Dialog(context);
                    dialog2.setContentView(R.layout.popup_newsletter);
                    dialog2.setCanceledOnTouchOutside(false);
                    dialog2.dismiss();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal)
            {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(int id, Exception ex)
            {
                Toast.makeText(AnnouncementsActivity.this, "error"+ex, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    class PdfNewsAsync extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings)
        {
            HttpURLConnection conn = null;
            URL url = null;

            try
            {
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx/WS_Pdf_Upload");
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            try
            {
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("RCTP_APIkey", Constants.P_KEY)
                        .appendQueryParameter("Pdf_Name", list)
                        .appendQueryParameter("pdf_info", strpdfname)
                        .appendQueryParameter("pdf_type", "announcement")
                        .appendQueryParameter("pdf_Message",strMessage);

                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            try
            {
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK)
                {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null)
                    {
                        result.append(line);
                    }
                    return (result.toString());
                }
                else
                {
                    return ("unsuccessful");
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return "exception";
            }
            finally
            {
                conn.disconnect();
            }
        }

        protected void onPostExecute(String s)
        {
            //  progressDialog.dismiss();

        }
    }

    public class PdfAsyncShow3 extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AnnouncementsActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setTitle("Getting things ready for you...");

            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings)
        {

            HttpURLConnection conn = null;
            URL url = null;

            try
            {
                url = new URL("http://106.201.232.22:85/RCTP/RCTP.asmx/WS_Get_PDF");

            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            try
            {
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("RCTP_APIkey", Constants.P_KEY)
                        .appendQueryParameter("pdf_type",Constants.ANNOUNSMENT);


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
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            try
            {
                int response_code = conn.getResponseCode();

                if (response_code == HttpURLConnection.HTTP_OK)
                {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null)
                    {
                        result.append(line);
                    }
                    return (result.toString());
                }
                else
                {
                    return ("unsuccessful");
                }
            } catch (IOException e)
            {
                e.printStackTrace();
                return "exception";
            } finally
            {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            // progressDialog.dismiss();
            Runnable progressRunnable = new Runnable()
            {

                @Override
                public void run()
                {
                    progressDialog.cancel();
                }
            };

            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 1000);

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("pref2", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("s",s);
            editor.apply();

            getPdf();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);
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
                Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}



