package com.example.qst.qst_newsreader_v1.Entity;

import java.util.HashMap;
import java.util.Map;

public class NewsType {
    public static final  HashMap<String,String> TYPEMAP;
     static {
         TYPEMAP=new HashMap<String, String>();
         TYPEMAP.put("头条","top");
         TYPEMAP.put("社会","shehui");
         TYPEMAP.put("国内","guonei");
         TYPEMAP.put("国际","guoji");
         TYPEMAP.put("娱乐","yule");
         TYPEMAP.put("体育","tiyu");
         TYPEMAP.put("军事","junshi");
         TYPEMAP.put("科技","keji");
         TYPEMAP.put("财经","caijing");
         TYPEMAP.put("时尚","shishang");
     }
    public static  String getCn(String Zn)
    {
        return  TYPEMAP.get(Zn);
    }

    public  static  String  getZn(String Cn) {

        for (Map.Entry<String, String> entry : TYPEMAP.entrySet()) {
            if (entry.getValue() == Cn)
                return entry.getKey().toString();
        }
        return "";
    }
}
