package com.example.qst.qst_newsreader_v1.Utlis;

import android.app.Activity;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

//**
// * Author: 钱苏涛. 
// * Date: 2018/12/30.
// * Description:   改变字体大小
//**
public class TextScaleUtils {

    public static final float SMALL=0.85f;
    public static final float Middle=1f;
    public static final float Big=1.3f;
    public static void scaleTextSize(Activity activity,float size) {

        Configuration configuration = activity.getResources().getConfiguration();
        configuration.fontScale = size; //0.85 small size, 1 normal size, 1.5 big etc
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        activity.getBaseContext().getResources().updateConfiguration(configuration, metrics);//更新全局的配置
    }
}
