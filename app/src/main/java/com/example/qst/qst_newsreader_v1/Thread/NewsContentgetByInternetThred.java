package com.example.qst.qst_newsreader_v1.Thread;

import android.content.Context;

import com.example.qst.qst_newsreader_v1.Sql.Sqlmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class NewsContentgetByInternetThred extends Thread {

    Context context;
    String url;
    String uniqueid;
    public NewsContentgetByInternetThred(Context context,String url,String uniqueid) {
        this.context = context;
        this.url=url;
        this.uniqueid=uniqueid;
    }

    @Override
    public void run() {
        Sqlmanager sqlmanager= new Sqlmanager(context);
        try {
            URL url = new URL(this.url);
            //找到输入流
            URLConnection conn = url.openConnection();
            //设置从主机读取数据超时（单位：毫秒）
            conn.setConnectTimeout(5000);
            //设置连接主机超时（单位：毫秒）
            conn.setReadTimeout(5000);
            InputStream is = conn.getInputStream();
            //添加到缓冲输入区里
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            //字符串变量
            StringBuffer sb = new StringBuffer();
            String s = "";
            //一行一行读下来
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            br.close();
            is.close();
            final String body = sb.toString();
            sqlmanager.update(uniqueid,body);

        } catch (IOException e) {
            sqlmanager.close();
            e.printStackTrace();
        }

    }
}
