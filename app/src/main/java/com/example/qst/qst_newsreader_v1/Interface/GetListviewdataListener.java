package com.example.qst.qst_newsreader_v1.Interface;

import android.database.Cursor;

//**
// * Author: 钱苏涛.
// * Date: 2018/12/29.
// * Description:
//**
public interface GetListviewdataListener {
    public void Getdata(Cursor cursor);
    public void FlushdataBynew();
}
