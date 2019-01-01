package com.example.qst.qst_newsreader_v1.Thread;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.qst.qst_newsreader_v1.Interface.GetListviewdataListener;
import com.example.qst.qst_newsreader_v1.Sql.Sqlmanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class NewsgetByInternetThread extends Thread {
    String type;
    String mainurl;
    GetListviewdataListener getListviewdataListener;
    Context context;

    public NewsgetByInternetThread(String type,Context context,GetListviewdataListener getListviewdataListener) {
        this.type = type;
        this.mainurl=String.format("http://v.juhe.cn/toutiao/index?type=%s&key=c97f7d475b93a1ac7e512b2ea1cbc30a",type);
        this.context=context;
        this.getListviewdataListener=getListviewdataListener;
    }

    @Override
    public void run() {
        //            Connection.Response res=Jsoup.connect(mainurl).timeout(1000).ignoreContentType(true).execute();
//            String body=res.body();
    try {
            Sqlmanager sqlmanager=new Sqlmanager(context);
            URL url = new URL(mainurl);
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
            String body = sb.toString();
            JSONObject json1 = new JSONObject(body);
            JSONArray data  =json1.getJSONObject("result").getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject value = data.getJSONObject(i);
                String uniquekey = value.getString("uniquekey");
                String title = value.getString("title");
                String date = value.getString("date");
                String category = value.getString("category");
                String author = value.getString("author_name");
                String weburl = value.getString("url");
                String imageurl = value.getString("thumbnail_pic_s");
                String [] datas= new String[]{uniquekey,title,date,category,author,weburl,imageurl,""};//在线不加载content 所以为空
                Boolean ishave=sqlmanager.isHave(uniquekey);
                if(!ishave)
                    sqlmanager.insert(datas);
                //离线下载
                new NewsContentgetByInternetThred(context,weburl,uniquekey).start();
            }


        Activity activity= (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getListviewdataListener.FlushdataBynew();
            }
        });

        sqlmanager.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
