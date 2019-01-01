package com.example.qst.qst_newsreader_v1.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.qst.qst_newsreader_v1.View.NewsFragment;

import java.util.List;

public class NewsAdapter extends FragmentPagerAdapter {

    private List<NewsFragment> fragmentList;
    public NewsAdapter(FragmentManager fm,List<NewsFragment> fragmentList) {
        super(fm);
        this.fragmentList=fragmentList;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragmentList.get(arg0);
    }

    @Override
    public int getCount() {
        return fragmentList==null?0:fragmentList.size();
    }
}
