package com.android.aquagiraffe.rotaryconnect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;
import com.github.barteksc.pdfviewer.PDFView;

public class PdfNewsLetterActivity extends AppCompatActivity
{
    PDFView pdfView;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_news_letter);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        Intent i = this.getIntent();
        path = i.getExtras().getString("PATH");

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(path)));
        startActivity(browserIntent);

    }
}

