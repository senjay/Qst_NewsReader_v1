package com.example.qst.qst_newsreader_v1.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.qst.qst_newsreader_v1.Adapter.MyListViewAdapter;
import com.example.qst.qst_newsreader_v1.Interface.GetListviewdataListener;
import com.example.qst.qst_newsreader_v1.R;
import com.example.qst.qst_newsreader_v1.Thread.NewsgetByDatabaseThread;
import com.example.qst.qst_newsreader_v1.Sql.Sqlhelp;
import com.example.qst.qst_newsreader_v1.Sql.Sqlmanager;
import com.example.qst.qst_newsreader_v1.Thread.NewsgetByInternetThread;
import com.example.qst.qst_newsreader_v1.Utlis.NetUtil;
import com.example.qst.qst_newsreader_v1.Activity.WebActivity;

//**
// * Author: 钱苏涛.
// * Date: 2018/12/28.
// * Description:
//**
@SuppressLint("ValidFragment")
public class NewsFragment extends Fragment {

    String type;
    Context context;
    View v;
    SearchView searchView;
    MyListViewAdapter adapter;
    int itemcount;
    PullToRefreshListView refreshListView;
    Sqlmanager sqlmanager;
    Boolean searchstatus=true;//false无法下拉
    @SuppressLint("ValidFragment")
    public NewsFragment(Context context,String type) {
        this.type = type;
        this.context=context;
        sqlmanager=new Sqlmanager(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v =inflater .inflate(R.layout.fragment_layout, container, false);
        searchView=v.findViewById(R.id.searchview);
        refreshListView= v.findViewById(R.id.listview);
        sqlmanager =new Sqlmanager(context);
        itemcount=10;
        Cursor cursor=sqlmanager.findall(type,itemcount);
        adapter=new MyListViewAdapter(context,cursor);
        refreshListView.setAdapter(adapter);
        searchView.setVisibility(View.GONE);//想了很久 还是决定把searview移到顶部这里就先隐藏掉吧
        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor temp=adapter.getCursor();
                temp.moveToPosition(position-1);
                String url=temp.getString(temp.getColumnIndex(Sqlhelp.KEY_URL));
                Log.d("test",url);
                Bundle bundle = new Bundle();
                bundle.putString("URL",url);
                Intent intent = new Intent(getActivity(),WebActivity.class);
                intent.putExtras(bundle);
                startActivity(intent,bundle);
            }
        });
        refreshListView.setOnRefreshListener(new PullToRefreshListView.onRefreshListener() {
            @Override
            public void onRefresh() {

                if(searchstatus&& NetUtil.isNetConnected(context))
                {
                    new NewsgetByInternetThread(type, context, new GetListviewdataListener() {

                        @Override
                        public void Getdata(Cursor cursor) {

                        }
                        @Override
                        public void FlushdataBynew() {
                            Cursor cursor=sqlmanager.findall(type,itemcount);
                            adapter.changeCursor(cursor);
                            refreshListView.onRefreshComplete(true);

                        }
                    }).start();
                }
                else
                    refreshListView.onRefreshComplete(true);

            }

            @Override
            public void onLoadMore() {
                if(searchstatus)
                {
                    itemcount+=10;
                    new NewsgetByDatabaseThread(context, type, new GetListviewdataListener() {
                        @Override
                        public void Getdata(Cursor cursor) {
                            adapter.changeCursor(sqlmanager.findall(type,itemcount));
                            refreshListView.onRefreshComplete(true);

                        }

                        @Override
                        public void FlushdataBynew() {

                        }
                    }).start();
                }
                else
                    refreshListView.onRefreshComplete(true);

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
                    Cursor cursor=sqlmanager.findfuzzy(newText);
                    adapter.changeCursor(cursor);//这里要用swap 用change 原来的cursor tm给你关了
                    searchstatus=false;         //还是用change 后面直接查 原来的cursor保存临时的话烦
                    Toast.makeText(context,"查询出"+ sqlmanager.findfuzzycount(newText)+"条结果",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    adapter.changeCursor(sqlmanager.findall(type,itemcount));
                    searchstatus=true;

                }
                return  false;
            }
        });

    }

    public void flush()
    {
        new NewsgetByDatabaseThread(context, type, new GetListviewdataListener() {
            @Override
            public void Getdata(Cursor cursor) {

                adapter.changeCursor(cursor);
                adapter.getCursor().requery();
            }

            @Override
            public void FlushdataBynew() {

            }
        }).start();
    }
}
