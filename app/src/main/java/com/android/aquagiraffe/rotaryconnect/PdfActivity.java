package com.android.aquagiraffe.rotaryconnect;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.ScrollBar;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;

import java.io.File;

public class PdfActivity extends AppCompatActivity
{
    PDFView pdfView;
    ScrollBar scrollBar;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        pdfView = findViewById(R.id.pdfview);
        scrollBar = findViewById(R.id.scrollbar);

        scrollBar.setHorizontal(false);
        Intent i = this.getIntent();
        String path = i.getExtras().getString("PATH");
        File file = new File(path);
        if (file.canRead())
        {
            pdfView.fromFile(file).defaultPage(1).onLoad(new OnLoadCompleteListener()
            {
                @Override
                public void loadComplete(int nbPages)
                {
                    Toast.makeText(PdfActivity.this, String.valueOf(nbPages), Toast.LENGTH_LONG).show();
                }
            }).load();

        }



    }
}
