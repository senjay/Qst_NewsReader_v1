package com.example.qst.qst_newsreader_v1.Activity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.example.qst.qst_newsreader_v1.R;
import com.example.qst.qst_newsreader_v1.Sql.Sqlhelp;
import com.example.qst.qst_newsreader_v1.Sql.Sqlmanager;
import com.example.qst.qst_newsreader_v1.Utlis.NetUtil;

public class WebActivity extends AppCompatActivity {
    private Bundle bundle;
    private WebView webView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);
        frameLayout = findViewById(R.id.web_frame);
        bundle = getIntent().getExtras();
        initView();
    }

    private void initView() {
        String url = bundle.getString("URL");
        webView = new WebView(this);
        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true);

        settings.setJavaScriptEnabled(false);//关闭js可以略去好多广告啥的

        //离线默认为无图模式
        settings.setBlockNetworkImage(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }
        });


        frameLayout.addView(webView);

        if(!NetUtil.isNetConnected(this))
        {
            Sqlmanager sqlmanager=new Sqlmanager(this);
            Cursor cursor=sqlmanager.findbyurl(url);
            cursor.moveToFirst();
            String body=cursor.getString(cursor.getColumnIndex(Sqlhelp.KEY_CONTENT));
            webView.loadDataWithBaseURL(null,body,"text/html" , "utf-8", null);
        }
        else
        {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            settings.setBlockNetworkImage(false);
            webView.loadUrl(url);
        }

    }

    //监听BACK按键，有可以返回的页面时返回页面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.setTag(null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

}

