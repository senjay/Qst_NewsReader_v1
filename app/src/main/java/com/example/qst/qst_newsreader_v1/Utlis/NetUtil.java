package com.example.qst.qst_newsreader_v1.Utlis;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//**
// * Author: 钱苏涛.
// * Date: 2018/12/31.
// * Description: 这个是测试的手机的联网状态 而不是应用的
//**
public class NetUtil {

    //true有网
    public static boolean isNetConnected(Context context) {
        boolean isNetConnected;
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            isNetConnected = true;
        } else {
            isNetConnected = false;
        }
        return isNetConnected;
    }
}