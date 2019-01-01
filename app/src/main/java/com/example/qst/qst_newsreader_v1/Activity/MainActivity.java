package com.example.qst.qst_newsreader_v1.Activity;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.qst.qst_newsreader_v1.Adapter.MySlidingMenu;
import com.example.qst.qst_newsreader_v1.Adapter.NewsAdapter;
import com.example.qst.qst_newsreader_v1.Adapter.NewsTypeAdapter;
import com.example.qst.qst_newsreader_v1.Entity.NewsType;
import com.example.qst.qst_newsreader_v1.Interface.NewsTypeOnItemClickListener;
import com.example.qst.qst_newsreader_v1.R;
import com.example.qst.qst_newsreader_v1.View.NewsFragment;

import java.util.ArrayList;


//**
// * Author: 钱苏涛.
// * Date: 2018/12/27.
// * Description:manin
//**
public class MainActivity extends AppCompatActivity {//FragmentActivity
    RecyclerView recyclerView;
    ViewPager pager;
    ArrayList <NewsFragment> fragmentList;
    NewsTypeAdapter newsTypeAdapter;
    Toolbar toolbar;
    //recreate 字体设置刷新的时候要调用，但是因为有fragment的原因（生命周期）会炸，所以要重写，这里是最简单暴力的方法直接把原来的fragment移除销毁
    @Override
    public void recreate() {
        try {//避免重启太快 恢复
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            for (Fragment fragment : fragmentList) {
                fragmentTransaction.remove(fragment);
            }
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
        }
        super.recreate();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        //创建侧滑菜单
        MySlidingMenu slidingMenu=new MySlidingMenu(this);
        slidingMenu.initSlidingMenu();
        initToolBar();
        //recyclerview
        recyclerView=findViewById(R.id.recycle);
        String[] datas=new String[]{"头条","社会","国内","国际","娱乐","体育","军事","科技","财经","时尚"};

        newsTypeAdapter =new NewsTypeAdapter(this,datas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(newsTypeAdapter);

        newsTypeAdapter.setOnRecyclerViewItemClickListener(new NewsTypeOnItemClickListener() {
            @Override
            public void onClick(int position) {
                newsTypeAdapter.setThisPosition(position);
                pager.setCurrentItem(position);
                newsTypeAdapter.notifyDataSetChanged();
            }
        });


        //viewpager
        pager=findViewById(R.id.viewpager);
        fragmentList=new ArrayList<NewsFragment>();
        for(int i=0;i<10;i++){
            NewsFragment fragment=new NewsFragment(this,NewsType.getCn(datas[i]));
            fragmentList.add(fragment);
        }



        final NewsAdapter newsAdapter =new NewsAdapter(getSupportFragmentManager(),fragmentList);
        pager.setAdapter(newsAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }
            @Override
            public void onPageSelected(int i) {
                //不需要 它自己会有限制
//                if(i>=newsclassAdapter.getItemCount())
//                    i=newsAdapter.getCount()-1;
                recyclerView.smoothScrollToPosition(i);//若用recyclerView.scrollToPosition(i);会炸
                //newsclassAdapter.selectTab(i);
                newsTypeAdapter.setThisPosition(i);
                newsTypeAdapter.notifyDataSetChanged();
                fragmentList.get(i).flush();

            }
            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


    }
    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.inflateMenu(R.menu.menu);

        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.action_search)
                {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent,bundle);
                }
                return true;
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);//这句代码使启用Activity回退功能，并显示Toolbar上的左侧回退图标
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


}
