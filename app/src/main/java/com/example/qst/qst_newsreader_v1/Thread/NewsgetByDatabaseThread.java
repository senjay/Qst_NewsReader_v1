package com.example.qst.qst_newsreader_v1.Thread;

//**
// * Author: 钱苏涛. 
// * Date:2018/12/27.
// * Description:从数据库获取新闻
//**

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.qst.qst_newsreader_v1.Entity.NewsType;
import com.example.qst.qst_newsreader_v1.Interface.GetListviewdataListener;
import com.example.qst.qst_newsreader_v1.Sql.Sqlmanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;


public class NewsgetByDatabaseThread extends Thread{

    String key;
    String mainurl;
    GetListviewdataListener getListviewdataListener;
    Context context;


    public NewsgetByDatabaseThread(Context context, String key, GetListviewdataListener getListviewdataListener) {
        this.key = key;
        this.mainurl=String.format("http://v.juhe.cn/toutiao/index?type=%s&key=c97f7d475b93a1ac7e512b2ea1cbc30a",key);
        this.context=context;
        this.getListviewdataListener=getListviewdataListener;

    }

    @Override
    public void run() {

           Sqlmanager sqlmanager=new Sqlmanager(context);
            final Cursor cursor=sqlmanager.findall(key,10);
            //这个sleep是为了在加载下面的时候转圈 ，如果要这个类用在起来地方 注意修改
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        Activity activity= (Activity) context;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getListviewdataListener.Getdata(cursor);
                }
            });


    }

}
