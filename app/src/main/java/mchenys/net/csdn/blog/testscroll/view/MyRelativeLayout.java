package mchenys.net.csdn.blog.testscroll.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.RelativeLayout;

/**
 * 处理生活研究院向上滑动焦点图时MyNestedScrollParent的onTouchEvent不回调的bug
 */
public class MyRelativeLayout extends RelativeLayout implements NestedScrollingChild {

    public MyRelativeLayout(Context context) {
        this(context, null);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int lastX, lastY;
    private VelocityTracker mVelocityTracker;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            lastX = (int) ev.getRawX();
            lastY = (int) ev.getRawY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            int dx = (int) (ev.getRawX() - lastX);
            int dy = (int) (ev.getRawY() - lastY);
            lastX = (int) ev.getRawX();
            lastY = (int) ev.getRawY();
            if (Math.abs(dy) > Math.abs(dx)) {
                getParent().requestDisallowInterceptTouchEvent(false);
            }
        }

        Log.e("cys", "MyRelativeLayout->dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

   /* @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("cys", "MyRelativeLayout->onInterceptTouchEvent");
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            lastX = (int) ev.getRawX();
            lastY = (int) ev.getRawY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            int dx = (int) (ev.getRawX() - lastX);
            int dy = (int) (ev.getRawY() - lastY);
            lastX = (int) ev.getRawX();
            lastY = (int) ev.getRawY();
            return Math.abs(dy) > Math.abs(dx);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e("cys", "MyRelativeLayout->onTouchEvent");
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            lastX = (int) ev.getRawX();
            lastY = (int) ev.getRawY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            int dx = (int) (ev.getRawX() - lastX);
            int dy = (int) (ev.getRawY() - lastY);
            lastX = (int) ev.getRawX();
            lastY = (int) ev.getRawY();
            if (Math.abs(dy) > Math.abs(dx)) {
                ViewGroup parent = (ViewGroup) getParent();
                if (parent instanceof MyNestedScrollParent) {
                    parent.scrollBy(0, -dy);
                    return true;
                }

            }

        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            mVelocityTracker.computeCurrentVelocity(1000);
            int vy = (int) mVelocityTracker.getYVelocity();
            ViewGroup parent = (ViewGroup) getParent();
            if (parent instanceof MyNestedScrollParent) {
                MyNestedScrollParent nestedScrollParent = (MyNestedScrollParent) parent;
                nestedScrollParent.fling(-vy);
                return true;
            }

        }
        return super.onTouchEvent(ev);
    }
*/
}
