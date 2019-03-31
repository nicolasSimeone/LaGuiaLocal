package com.cabureweb.laguialocal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.w3c.dom.Text;

import java.net.URLClassLoader;

public class MainActivity extends AppCompatActivity {

    private WebView mywebView;

    private ProgressDialog progDailog;

    private SwipeRefreshLayout mySwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySwipeRefreshLayout = findViewById(R.id.swipeContainer);

        progDailog = ProgressDialog.show(MainActivity.this, "Cargando","Espere por favor...", true);
        progDailog.setCancelable(false);
        progDailog.setCanceledOnTouchOutside(false);

        mywebView = findViewById(R.id.webview);
        mywebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progDailog.show();
                if (url == null || url.startsWith("http://") || url.startsWith("https://")) return false;
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(intent);
                    progDailog.dismiss();
                    return true;
                } catch (Exception e) {

                    progDailog.dismiss();
                    return true;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progDailog.dismiss();
                super.onPageFinished(view, url);
            }
        });
        mywebView.getSettings().setJavaScriptEnabled(true);
        mywebView.loadUrl("https://laguialocal.com.ar");
        mywebView.clearCache(true);

        WebSettings webSettings = mywebView.getSettings();
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        webSettings.setTextZoom(100);
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);


        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(null != mySwipeRefreshLayout) {
                    mySwipeRefreshLayout.setRefreshing(false);
                }
                mywebView.reload();
                mywebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                mywebView.loadUrl("https://laguialocal.com.ar");
            }
        });


    }

    @Override
    public void onBackPressed() {
        if(mywebView.canGoBack())
        {
            mywebView.goBack();
        }
        else{
            super.onBackPressed();
        }

    }
}
