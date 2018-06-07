package mchenys.net.csdn.blog.testscroll.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by user on 2017/8/28.
 */

public class ImageViewPager extends ViewPager {

    private final static int AUTO_SWITCH_TIME = 5000;
    private static OnItemClickListener onItemClickListener;

    private Handler mHandler = new Handler();

    private Runnable mScrollRunable = new Runnable() {

        @Override
        public void run() {
            mHandler.removeCallbacks(this);
            int currentItem = getCurrentItem();
            currentItem++;
            if (currentItem >= Integer.MAX_VALUE) {
                currentItem = 0;
            } else if (currentItem <= 0) {
                currentItem = Integer.MAX_VALUE;
            }
            setCurrentItem(currentItem, true);
            mHandler.postDelayed(this, AUTO_SWITCH_TIME);
        }
    };

    public ImageViewPager(Context context) {
        super(context);
    }

    public ImageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private int lastX, lastY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            onPause();
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            onResume();
        } else if (ev.getAction() == MotionEvent.ACTION_CANCEL) {
            onResume();
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {

            for (int i = 0; i < getChildCount(); i++) {
                MotionEvent childEvent = MotionEvent.obtain(ev);
                childEvent.setAction(MotionEvent.ACTION_CANCEL);
                getChildAt(i).dispatchTouchEvent(ev);
            }
        }
//        ViewGroup group = (ViewGroup) getParent();
//        group = (ViewGroup) group.getParent();
//        group.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    //暂停自动切换
    public void onPause() {
        mHandler.removeCallbacks(mScrollRunable);
    }

    //重新开启自动切换
    public void onResume() {
        mHandler.removeCallbacks(mScrollRunable);
        mHandler.postDelayed(mScrollRunable, AUTO_SWITCH_TIME);
    }

    public void onDestory() {
        mHandler.removeCallbacksAndMessages(null);
    }


    //基类适配器
    public abstract static class BasePagerAdapter<T> extends PagerAdapter implements OnClickListener {
        private Context mContext;
        private List<T> mData;

        public BasePagerAdapter(Context ctx, List<T> data) {
            this.mContext = ctx;
            this.mData = data;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            T t = null;
            if (null != mData && !mData.isEmpty()) {
                position = position % mData.size();
                t = mData.get(position);
            }
            View view = getItemView(mContext, position, t);
            if (null != view) {
                ViewGroup parent = (ViewGroup) view.getParent();
                if (parent != null) {
                    parent.removeView(view);
                }
                view.setOnClickListener(this);
            }
            container.addView(view);
            return view;
        }

        public abstract View getItemView(Context context, int position, T t);


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            if (mData.size() > 0) {
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick();
            }
        }

        public int getDataCount() {
            return mData.size();
        }
    }

    public interface OnItemClickListener {
        void onItemClick();
    }


}
