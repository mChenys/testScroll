package mchenys.net.csdn.blog.testscroll.view;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Scroller;


/**
 * 下拉刷新控件
 * Created by mChenys on 2018/6/5.
 */

public class RefreshLayout extends LinearLayout {

    private int lastX, lastY;
    private View mRefreshView; //刷新头
    private View mScrollView; //可滚动的View
    private int refreshHeight;
    private Scroller mScroller;
    private Handler mHandler = new Handler();
    private boolean isRefreshing;

    public RefreshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRefreshView =  getChildAt(0);
        mScrollView = getChildAt(1);
        mRefreshView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (refreshHeight <= 0) {
                    refreshHeight = mRefreshView.getHeight();

                }
            }
        });

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean shouldIntercept = false;
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            lastX = (int) ev.getRawX();
            lastY = (int) ev.getRawY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            int y = (int) ev.getRawY();
            int x = (int) ev.getRawX();
            int dx = x - lastX;
            int dy = y - lastY;
            //向下拦截

            shouldIntercept = (mScrollView.getScrollY() <=0 && dy > 0 &&
                    (Math.abs(dy) > Math.abs(dx))) || (getScrollY() < 0);
            lastX = x;
            lastY = y;
        }
        return shouldIntercept || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("cys", "RefreshLayout->onTouchEvent");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) (event.getRawY());
                int dy = y - lastY;
                lastY = y;
                int ratioDy = (int) (dy * 0.5f + 0.5f);
                scrollBy(0, -ratioDy);
                break;
            case MotionEvent.ACTION_UP:
                //完全划出后松手
                if (Math.abs(getScrollY()) >= refreshHeight) {
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY() - refreshHeight, 1000);
                    if (null != mOnRefreshListener && !isRefreshing) {
                        //startScroll未完成时scrollTo调用是无效的
                        mHandler.removeCallbacksAndMessages(null);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!mScroller.computeScrollOffset()) {
                                    Log.e("cys", "开始刷新");
                                    mOnRefreshListener.onRefresh();
                                    isRefreshing = true;
                                }

                            }
                        }, 1010);

                    }
                } else {
                    //未完全划出松手
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), 1000);
                }
                invalidate();
                break;
        }

        return true;
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public OnRefreshListener mOnRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    public void onComplete() {
        scrollTo(0, 0);
        Log.e("cys", "刷新完毕");
        isRefreshing = false;
    }
}
