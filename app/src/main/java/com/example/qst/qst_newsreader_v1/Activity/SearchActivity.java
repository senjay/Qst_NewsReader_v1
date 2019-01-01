package com.example.qst.qst_newsreader_v1.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.qst.qst_newsreader_v1.Adapter.MyListViewAdapter;
import com.example.qst.qst_newsreader_v1.Interface.GetListviewdataListener;
import com.example.qst.qst_newsreader_v1.R;
import com.example.qst.qst_newsreader_v1.Sql.Sqlhelp;
import com.example.qst.qst_newsreader_v1.Sql.Sqlmanager;
import com.example.qst.qst_newsreader_v1.Thread.NewsgetByDatabaseThread;
import com.example.qst.qst_newsreader_v1.View.PullToRefreshListView;

//**
// * Author: 钱苏涛.
// * Date: 2018/01/01.
// * Description:manin
//**
public class SearchActivity extends AppCompatActivity {

    SearchView searchView;
    MyListViewAdapter adapter;
    PullToRefreshListView refreshListView;
    Sqlmanager sqlmanager;
    int fuzzycount=10;
    String keyText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        searchView=findViewById(R.id.search_search);
        refreshListView= findViewById(R.id.search_list);
        sqlmanager =new Sqlmanager(this);
        refreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor temp=adapter.getCursor();
                temp.moveToPosition(position-1);
                String url=temp.getString(temp.getColumnIndex(Sqlhelp.KEY_URL));
                Log.d("test",url);
                Bundle bundle = new Bundle();
                bundle.putString("URL",url);
                Intent intent = new Intent(SearchActivity.this,WebActivity.class);
                intent.putExtras(bundle);
                startActivity(intent,bundle);
            }
        });
        refreshListView.setOnRefreshListener(new PullToRefreshListView.onRefreshListener() {
            @Override
            public void onRefresh() {
                refreshListView.onRefreshComplete(true);
            }

            @Override
            public void onLoadMore() {
                    fuzzycount+=10;
                    new NewsgetByDatabaseThread(SearchActivity.this, "null", new GetListviewdataListener() {
                        @Override
                        public void Getdata(Cursor cursor) {
                            adapter.changeCursor(sqlmanager.findfuzzy(keyText,fuzzycount));
                            refreshListView.onRefreshComplete(true);

                        }

                        @Override
                        public void FlushdataBynew() {

                        }
                    }).start();


            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!TextUtils.isEmpty(newText))
                {
                    keyText=newText;
                    Cursor cursor=sqlmanager.findfuzzy(newText,fuzzycount);
                    if(adapter==null)
                    {
                        adapter=new MyListViewAdapter(SearchActivity.this,cursor);
                        refreshListView.setAdapter(adapter);
                    }
                    else
                        adapter.changeCursor(cursor);//这里要用swap 用change 原来的cursor tm给你关了
                    Toast.makeText(SearchActivity.this,"查询出"+ sqlmanager.findfuzzycount(newText)+"条结果",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //搜索空 给它空的cursor
                    adapter.changeCursor(sqlmanager.findall("null",fuzzycount));
                }
                return  false;
            }
        });
    }
}
