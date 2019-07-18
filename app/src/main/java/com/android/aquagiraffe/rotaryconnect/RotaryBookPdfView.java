package com.android.aquagiraffe.rotaryconnect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.android.aquagiraffe.rotaryconnect.util.FontChangeCrawler;

import java.io.File;

public class RotaryBookPdfView extends AppCompatActivity /*implements Loader.OnLoadCompleteListener ,OnErrorListener*/ {

    WebView webview;
    String pdfName;
    String path;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotary_book_pdf_view);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "RobotoMedium.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        webview = (WebView) findViewById(R.id.webview);

        Bundle extras = getIntent().getExtras();
        pdfName = extras.getString("name");

        path = "https://s3-ap-southeast-1.amazonaws.com/awsaquabucket/RotaryLibrary/"+pdfName;

        file = new File(path);

        webview = new WebView(RotaryBookPdfView.this);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.getSettings().setUseWideViewPort(true);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setSupportZoom(true);


        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.setScrollbarFadingEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                setTitle("Loading...");
                setProgress(progress * 100);

                if(progress == 100)
                    setTitle(R.string.app_name);
            }
        });

        webview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url="+path);
        setContentView(webview);


    }

}
