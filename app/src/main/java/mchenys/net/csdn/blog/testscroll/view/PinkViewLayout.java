package mchenys.net.csdn.blog.testscroll.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.OverScroller;

/**
 * Created by mChenys on 2018/6/4.
 */

public class PinkViewLayout extends LinearLayout {

    private View mTopView;
    private View mPinkView;
    private View mContentView;
    private int mTopHeight;
    private int lastX;
    private int lastY;
    private OverScroller mScroller;


    public PinkViewLayout(Context context) {
        this(context,null);
    }

    public PinkViewLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PinkViewLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new OverScroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTopView = getChildAt(0);
        mPinkView = getChildAt(1);
        mContentView =  getChildAt(2);
        mTopView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mTopHeight <= 0) {
                    mTopHeight = mTopView.getMeasuredHeight();
                    LinearLayout.LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
                    lp.height = getHeight() - mPinkView.getHeight();
                    mContentView.setLayoutParams(lp);
                }
            }
        });
    }

    //下拉的时候是否要向下滚动以显示TopView
    public boolean showTopView(int dy) {
        if (dy > 0) {
            if (getScrollY() > 0) {
                return true;
            }
        }

        return false;
    }

    //上拉的时候，是否要向上滚动，隐藏TopView
    public boolean hideTopView(int dy) {
        if (dy < 0) {
            if (getScrollY() < mTopHeight) {
                return true;
            }
        }
        return false;
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
            shouldIntercept = (showTopView(dy) || hideTopView(dy)) && Math.abs(dy) > Math.abs(dx);
            lastX = x;
            lastY = y;
        }
        return shouldIntercept;
    }


    //scrollBy内部会调用scrollTo
    //限制滚动范围
    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopHeight) {
            y = mTopHeight;
        }
        super.scrollTo(x, y);
    }

    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mTopHeight);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }

    private VelocityTracker mVelocityTracker;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = (int) event.getRawY();
                if (!mScroller.isFinished()) { //fling
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) (event.getRawY());
                int dy = y - lastY;
                lastY = y;
                if (showTopView(dy) || hideTopView(dy)) {
                    scrollBy(0, -dy);//滚动
                }
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                int vy = (int) mVelocityTracker.getYVelocity();
                fling(-vy);
                break;
        }

        return true;
    }
}


