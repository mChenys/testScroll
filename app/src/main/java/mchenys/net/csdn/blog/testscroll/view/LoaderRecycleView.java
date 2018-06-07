package mchenys.net.csdn.blog.testscroll.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by mChenys on 2018/6/5.
 */

public class LoaderRecycleView extends RecyclerView {
    private int lastX, lastY;
    private WrapAdapter mWrapAdapter;
    private LoaderFooterView mLoaderFooter;

    public interface OnLoaderMoreListener {
        void onMore(LoaderRecycleView loaderRecycleView);
    }

    public OnLoaderMoreListener mOnLoaderMoreListener;

    public void setOnLoaderMoreListener(OnLoaderMoreListener listener) {
        this.mOnLoaderMoreListener = listener;
    }

    public LoaderRecycleView(Context context) {
        this(context, null);
    }

    public LoaderRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoaderRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mLoaderFooter = new LoaderFooterView(context);
    }

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
            //避免ViewPager拦截了斜方向的滑动事件
            boolean isVerticalScroll = Math.abs(dy) > Math.abs(dx);
            if (isVerticalScroll && !canScrollVertically(-1)) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        }
        return super.dispatchTouchEvent(ev);
    }





    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter, mLoaderFooter);
        super.setAdapter(mWrapAdapter);
    }

    @Override
    public void onScrollStateChanged(int state) {
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            if (getLastVisibleItemPosition() == mWrapAdapter.getItemCount() - 1
                    && mLoaderFooter.getState() == LoaderFooterView.STATE_DONE) {

                mLoaderFooter.setState(LoaderFooterView.STATE_LOADING);
                smoothScrollToPosition(mWrapAdapter.getItemCount());
                if (null != mOnLoaderMoreListener) {
                    mOnLoaderMoreListener.onMore(this);
                }
            }

        }
    }

    /**
     * 没有更多

     */
    public void loadNoMore() {
        mLoaderFooter.setState(LoaderFooterView.STATE_NO_MORE);
        mWrapAdapter.notifyDataSetChanged();

    }

    /**
     * 加载更多完成
     */
    public void loadComplete() {
        mLoaderFooter.setState(LoaderFooterView.STATE_DONE);
        mWrapAdapter.notifyDataSetChanged();
    }
    /**
     * 获取可见列表内最后一个item的位置
     *
     * @return
     */
    public int getLastVisibleItemPosition() {
        int lastVisibleItemPosition;
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
            lastVisibleItemPosition = findMax(into);
        } else {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }
        return lastVisibleItemPosition;
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
   /* @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (mWrapAdapter != null) {
            if (layout instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) layout);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return position==mWrapAdapter.getItemCount()-1
                                ? gridManager.getSpanCount() : 1;
                    }
                });
            }
        }
    }*/

}
