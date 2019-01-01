package com.example.qst.qst_newsreader_v1.Adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.example.qst.qst_newsreader_v1.R;
import com.example.qst.qst_newsreader_v1.Utlis.TextScaleUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

//**
// * Author: 钱苏涛.
// * Date:2018/12/27.
// * Description:
//**
public class MySlidingMenu implements View.OnClickListener {
    private Activity activity;
    private SlidingMenu slidingMenu;
    Button bt;
    Switch daynightSwitch;


    public MySlidingMenu(Activity activity) {
        this.activity = activity;
    }
    public SlidingMenu initSlidingMenu() {
        slidingMenu = new SlidingMenu(activity);
        slidingMenu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setShadowDrawable(R.color.colorAccent);

        // 设置滑动菜单视图的宽度
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        slidingMenu.setFadeDegree(0.35f);

        slidingMenu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        slidingMenu.setMenu(R.layout.menu_layout);

        slidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            public void onOpened() {

            }
        });

        //各种点击事件
        ViewListener();

        return slidingMenu;
    }

    private void ViewListener() {
        bt=slidingMenu.findViewById(R.id.button);
        bt.setOnClickListener(this);
        daynightSwitch=slidingMenu.findViewById(R.id.daynightswitch);
        daynightSwitch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                AlertDialog.Builder builder=new AlertDialog.Builder(activity);
                builder.setTitle("修改字体");
                View view=LayoutInflater.from(activity).inflate(R.layout.dialog_fontsize_layout,null,false);

                builder.setView(view);
                builder.setNegativeButton("取消",null);
                final AlertDialog dlg=builder.create();
                dlg.show();
                dlg.setCanceledOnTouchOutside(true);

                RadioGroup radioGroup=view.findViewById(R.id.font_size);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId)
                        {
                            case R.id.font_small:
                                TextScaleUtils.scaleTextSize(activity,TextScaleUtils.SMALL);
                                break;
                            case R.id.font_middle:
                                TextScaleUtils.scaleTextSize(activity,TextScaleUtils.Middle);
                                break;
                            case R.id.font_big:
                                TextScaleUtils.scaleTextSize(activity,TextScaleUtils.Big);
                                break;
                        }
                        activity.recreate();
                        dlg.cancel();
                    }
                });
                break;

            case R.id.daynightswitch:
                if(daynightSwitch.isChecked())
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    activity.recreate();
                }
                else
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    activity.recreate();
                }
                break;
            default:
                break;
        }
    }

}
