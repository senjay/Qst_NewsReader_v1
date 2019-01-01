package com.example.qst.qst_newsreader_v1.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.qst.qst_newsreader_v1.R;

import java.text.SimpleDateFormat;
import java.util.Date;

//**
// * Author: 钱苏涛. 
// * Date: 2018/12/30.
// * Description: 下拉上拉刷新 参考https://blog.csdn.net/qq_36699930/article/details/79957397
//**
public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener {
    /**
     * 下拉刷新
     */
    public static final int STATE_PULL_TO_REFRESH = 1;

    /**
     * 释放刷新
     */
    public static final int STATE_RELASE_TO_REFRESH = 2;

    /**
     * 正在刷新
     */
    public static final int STATE_REFRESHING = 3;

    /**
     * 当前状态
     */
    private int mCurrentState = STATE_PULL_TO_REFRESH;

    /**
     * 下拉刷新头布局
     */
    private View mHeadView;

    /**
     * 下拉刷新状态
     */
    private TextView tvState;

    /**
     * 下拉刷新时间
     */
    private TextView tvTime;

    /**
     * 下拉刷新旋转箭头图片
     */
    private ImageView ivArrow;

    /**
     * 下拉刷新ProgressBar
     */
    private ProgressBar pbProgress;

    /**
     * 下拉刷新按下时Y抽坐标
     */
    private float startY;
    //private int startY = -1;

    /**
     * 下拉刷新移动时Y抽坐标
     */
    private float endY;

    /**
     * 下拉刷新头布局的高度
     */
    private int mHeadViewHeight;

    /**
     * 下拉刷新箭头旋转动画
     */
    private RotateAnimation animUp;

    /**
     * 下拉刷新箭头旋转动画
     */
    private RotateAnimation animDown;

    /**
     * 下拉刷新过程中和顶部的padding
     */
    private int paddingTop;

    /**
     * 上拉加载底部布局
     */
    private View mFooterView;

    /**
     * 上拉加载底部布局高度
     */
    private int mFooterViewHeight;

    /**
     * 是否正在加载更多
     */
    private boolean isLoadMore;

    /**
     * 下拉刷新和上拉加载事件监听
     */
    private onRefreshListener onRefreshListener;

    public PullToRefreshListView(Context context) {
        this(context, null);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 初始化头布局，脚布局
     */
    private void init() {

        initHeadView();

        initFoodView();

        initAnimation();

    }

    /**
     * 初始化头布局
     */
    private void initHeadView() {
        mHeadView = View.inflate(getContext(), R.layout.listview_head_layout, null);
        this.addHeaderView(mHeadView);
        ivArrow = (ImageView) mHeadView.findViewById(R.id.iv_arrow);
        tvState = (TextView) mHeadView.findViewById(R.id.tv_state);
        tvTime = (TextView) mHeadView.findViewById(R.id.tv_time);
        pbProgress = (ProgressBar) mHeadView.findViewById(R.id.pd_loading);

        //按照设置的规则测量宽高
        mHeadView.measure(0, 0);
        //控件高度
        mHeadViewHeight = mHeadView.getMeasuredHeight();

        //隐藏头布局
        mHeadView.setPadding(0, -mHeadViewHeight, 0, 0);
    }

    /**
     * 初始化底部，加载更多
     */
    private void initFoodView() {
        mFooterView = View.inflate(getContext(), R.layout.listview_footer_layout, null);
        this.addFooterView(mFooterView);

        //测量控件
        mFooterView.measure(0, 0);
        //控件的高度
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        //隐藏控件
        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);

        //设置监听，加载更多
        this.setOnScrollListener(this);

    }

    /**
     * 初始化头布局箭头动画
     */
    private void initAnimation() {
        animUp = new RotateAnimation(0, -180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        animDown = new RotateAnimation(-180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录起点按下的Y坐标

                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                //添加了广告轮播头布局：
                //当用户按住广告图片进行下拉时ACTION_DOWN会被viewpager消费掉，
                //导致startY没有被赋值，此处需要重新获取一下
                //                if (startY == -1) {
                //                    startY = (int) ev.getY();
                //                }

                //如果此时正在刷新，跳出循环，不让再次刷新
                if (mCurrentState == STATE_REFRESHING) {
                    break;
                    //return super.onTouchEvent(ev); //或者这样写，执行父类的处理
                }

                endY = ev.getY();

                //手指滑动偏移量
                float dy = endY - startY;

                //当前显示第一个条目的位置
                int firstVisiblePosition = getFirstVisiblePosition();
                //往下拉，并且显示的是第一个可见item时第一个的时候，才能划出控件
                if (dy > 0 && firstVisiblePosition == 0) {
                    //计算当前控件的padding
                    paddingTop = (int) (dy - mHeadViewHeight);
                    mHeadView.setPadding(0, paddingTop, 0, 0);

                    //当控件拉至完全显示，改为松开刷新
                    if (paddingTop >= 0 && mCurrentState != STATE_RELASE_TO_REFRESH) {
                        mCurrentState = STATE_RELASE_TO_REFRESH;  //松开刷新状态

                        //刷新头布局
                        refreshState();
                    } else if (paddingTop < 0 && mCurrentState != STATE_PULL_TO_REFRESH) {
                        //不完全显示，下拉刷新
                        mCurrentState = STATE_PULL_TO_REFRESH;//下拉刷新状态

                        //刷新头布局
                        refreshState();
                    }
                    //这里不能返回true 否则下拉会同时触发点击
                    //return true; //当前事件被消费,拦截TouchMove，不让listview处理该次move事件,会造成listview无法滑动
                }
                break;
            case MotionEvent.ACTION_UP:
                //                startY = -1;
                if (mCurrentState == STATE_PULL_TO_REFRESH) { //下拉的时候没完全显示就松开或者回去的时候，就恢复
                    mHeadView.setPadding(0, -mHeadViewHeight, 0, 0);
                } else if (mCurrentState == STATE_RELASE_TO_REFRESH) { //松开刷新状态的时候抬起手要变成正在刷新
                    mCurrentState = STATE_REFRESHING;
                    refreshState();

                    //正在刷新时完整展示头布局
                    mHeadView.setPadding(0, 0, 0, 0);

                    //接口回调，通知加载数据
                    if (onRefreshListener != null) {
                        onRefreshListener.onRefresh();
                    }
                }
                break;

        }
        return super.onTouchEvent(ev);
    }

    /**
     * 根据当前状态刷新控件
     */
    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_TO_REFRESH:
                // 下拉刷新状态
                tvState.setText("下拉刷新");
                pbProgress.setVisibility(INVISIBLE);
                ivArrow.setVisibility(VISIBLE);
                ivArrow.startAnimation(animDown);

                break;
            case STATE_RELASE_TO_REFRESH:
                //  松开刷新状态
                tvState.setText("释放刷新");
                pbProgress.setVisibility(INVISIBLE);
                ivArrow.setVisibility(VISIBLE);
                ivArrow.startAnimation(animUp);
                break;
            case STATE_REFRESHING:
                // 正在刷新状态
                tvState.setText("正在刷新...");
                pbProgress.setVisibility(VISIBLE);
                ivArrow.setVisibility(INVISIBLE);

                ivArrow.clearAnimation();   //清除箭头动画，否则无法隐藏
                break;
        }
    }

    /**
     * 刷新结束，收起控件
     *
     * @param success:是否下拉刷新成功
     */
    public void onRefreshComplete(boolean success) {
        if (isLoadMore) { //加载更多
            //加载更多完成后，收起控件
            mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
            isLoadMore = false;
        } else { //下拉刷新
            mHeadView.setPadding(0, -mHeadViewHeight, 0, 0);
            mCurrentState = STATE_PULL_TO_REFRESH;
            tvState.setText("下拉刷新");
            pbProgress.setVisibility(INVISIBLE);
            ivArrow.setVisibility(VISIBLE);

            //设置刷新时间,只有刷新成功之后才更新时间
            if (success) {
                tvTime.setText("最后刷新时间:" + getCurrentTime());
            }
        }
    }

    /**
     * 设置下拉刷新里面的时间
     */
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        return time;
    }

    /**
     * SCROLL_STATE_IDLE:当屏幕停止滚动时
     * SCROLL_STATE_TOUCH_SCROLL：当屏幕以触屏方式滚动并且手指还在屏幕
     * SCROLL_STATE_FLING：当用户之前滑动屏幕并抬起手指，屏幕以惯性滚动
     *
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 滑动状态发生变化的回调
        if (scrollState == SCROLL_STATE_IDLE) { //空闲状态
            int lastVisiblePosition = getLastVisiblePosition();
            //显示最后一个item并且没有加载更多
            if (lastVisiblePosition == getCount() - 1 && !isLoadMore) {
                isLoadMore = true;
                mFooterView.setPadding(0, 0, 0, 0); //显示

                //setSelection(getCount() - 1); //显示在最后一个item上
                setSelection(getCount()); //显示在最后一个item上

                //通知主界面加载下一页数据
                if (onRefreshListener != null) {
                    onRefreshListener.onLoadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 滑动过程的回调
    }

    /**
     * 下拉刷新的回调接口
     */
    public interface onRefreshListener {
        //通知刷新(正在刷新)
        void onRefresh();

        //通知加载更多
        void onLoadMore();
    }

    /**
     * 暴露接口，设置监听
     *
     * @param onRefreshListener
     */
    public void setOnRefreshListener(PullToRefreshListView.onRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }
}


