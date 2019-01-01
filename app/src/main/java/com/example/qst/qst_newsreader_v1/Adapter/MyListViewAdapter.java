package com.example.qst.qst_newsreader_v1.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qst.qst_newsreader_v1.R;
import com.example.qst.qst_newsreader_v1.Sql.Sqlhelp;

//**
// * Author: 钱苏涛.
// * Date: 2018/12/28.
// * Description:
//**
public class MyListViewAdapter extends CursorAdapter {
    Context context;
    public MyListViewAdapter(Context context, Cursor c) {
        super(context, c);
        this.context=context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v=LayoutInflater.from(context).inflate(R.layout.listview_item_layout,parent,false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView iv=view.findViewById(R.id.imageView);
        TextView  txtitle=view.findViewById(R.id.lvtitle);
        // 跑马灯 https://blog.csdn.net/mashang123456789/article/details/79353591
        txtitle.setSelected(true);
        TextView  txdate=view.findViewById(R.id.lvdate);
        TextView  txauthor=view.findViewById(R.id.lvauthor);
        String title=cursor.getString(cursor.getColumnIndex(Sqlhelp.KEY_TITLE));
        String date=cursor.getString(cursor.getColumnIndex(Sqlhelp.KEY_DATE));
        String author=cursor.getString(cursor.getColumnIndex(Sqlhelp.KEY_AUTHOR));
        txtitle.setText(title);
        txdate.setText(date);
        txauthor.setText(author);
        String picurl=cursor.getString(cursor.getColumnIndex(Sqlhelp.KEY_PICURL));
        Glide.with(context).load(picurl).centerCrop().into(iv);

    }
}
