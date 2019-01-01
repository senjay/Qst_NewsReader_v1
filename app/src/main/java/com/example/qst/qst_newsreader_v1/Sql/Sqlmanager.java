package com.example.qst.qst_newsreader_v1.Sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.qst.qst_newsreader_v1.Entity.NewsType;
import com.example.qst.qst_newsreader_v1.Sql.Sqlhelp;

import java.util.HashMap;

//**
// * Author: 钱苏涛.
// * Date:2018/12/27.
// * Description:
//**
public class Sqlmanager {
    SQLiteDatabase db;
    Cursor cursor;

    public Sqlmanager(Context context) {
        db=(new Sqlhelp(context)).getWritableDatabase();
    }

    public void insert(String [] data)
    {

        ContentValues cv = new ContentValues();
        cv.put(Sqlhelp.KEY_UNIQUEID,data[0]);
        cv.put(Sqlhelp.KEY_TITLE,data[1]);
        cv.put(Sqlhelp.KEY_DATE,data[2]);
        cv.put(Sqlhelp.KEY_CATEGORY,data[3]);
        cv.put(Sqlhelp.KEY_AUTHOR,data[4]);
        cv.put(Sqlhelp.KEY_URL,data[5]);
        cv.put(Sqlhelp.KEY_PICURL,data[6]);
        cv.put(Sqlhelp.KEY_CONTENT,data[7]);
        db.insert(Sqlhelp.DATABASE_NAME,null,cv);
    }
    public  int  update(String uniqueid,String body)
    {
        ContentValues cv = new ContentValues();
        cv.put(Sqlhelp.KEY_CONTENT,body);
        int ans=db.update(Sqlhelp.DATABASE_NAME,cv,"uniqueid=?",new String []{uniqueid});
        return ans;
    }
    public  int delete(String id)
    {
        int ans=db.delete(Sqlhelp.DATABASE_NAME,"_id=?",new String[]{id});
        return ans;
    }

    public Cursor findall(String type,int count)
    {
        //如果count>数据库中的重置count
        //超过也没事 数据库会自己判断 不用写这个
        type=NewsType.getZn(type);//拼音转中文
//        Cursor cursor=db.rawQuery("select count(*) as count from NEWS where category = '"+type+"'",null);
//        cursor.moveToFirst();
//        if(count>cursor.getInt(cursor.getColumnIndex("count")))
//            count=cursor.getInt(cursor.getColumnIndex("count"));
//        cursor.close();

        String sql=String.format("select * from %s where %s = ? group by (uniqueid) order by datetime(date) desc limit ?",Sqlhelp.DATABASE_NAME,Sqlhelp.KEY_CATEGORY);
        String [] args=new String[] {type,count+""};
        return db.rawQuery(sql,args);

    }
    public  Cursor findbyurl(String url)
    {
        String sql= String.format("select * from %s where %s = ?",Sqlhelp.DATABASE_NAME,Sqlhelp.KEY_URL);
        String []args=new String []{url};
        return db.rawQuery(sql,args);
    }
    public Cursor findfuzzy(String text)
    {
        String sql=String.format("select * from %s where %s like ? or %s like ? or %s like ? group by (uniqueid)",Sqlhelp.DATABASE_NAME,Sqlhelp.KEY_TITLE,Sqlhelp.KEY_AUTHOR,Sqlhelp.KEY_DATE);
        String [] args=new String[] {"%"+text+"%","%"+text+"%","%"+text+"%"};
        return db.rawQuery(sql,args);
    }
    public Cursor findfuzzy(String text,int count)
    {
        String sql=String.format("select * from %s where %s like ? or %s like ? or %s like ? group by (uniqueid) limit ?",Sqlhelp.DATABASE_NAME,Sqlhelp.KEY_TITLE,Sqlhelp.KEY_AUTHOR,Sqlhelp.KEY_DATE);
        String [] args=new String[] {"%"+text+"%","%"+text+"%","%"+text+"%",count+""};
        return db.rawQuery(sql,args);
    }
    public int findfuzzycount(String text)
    {

        String sql=String.format("select count(*) as count from %s where %s like ? or %s like ? or %s like ?",Sqlhelp.DATABASE_NAME,Sqlhelp.KEY_TITLE,Sqlhelp.KEY_AUTHOR,Sqlhelp.KEY_DATE);
        String [] args=new String[] {"%"+text+"%","%"+text+"%","%"+text+"%"};
        Cursor cursor = db.rawQuery(sql,args);
        cursor.moveToFirst();
        return  cursor.getInt(cursor.getColumnIndex("count"));
    }
    //true就是已经有
    public Boolean isHave(String uniqueid)
    {
        String sql=String.format("select count(*) as count from %s where %s = ?",Sqlhelp.DATABASE_NAME,Sqlhelp.KEY_UNIQUEID);
        String [] args=new String[] {uniqueid};
        Cursor cursor=db.rawQuery(sql,args);
        cursor.moveToFirst();
        int count=cursor.getInt(cursor.getColumnIndex("count"));
        if(count>0)
            return true;
        else
            return false;
    }
    public void close()
    {
        db.close();
    }
}
