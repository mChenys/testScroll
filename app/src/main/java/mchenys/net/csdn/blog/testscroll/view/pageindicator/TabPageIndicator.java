package mchenys.net.csdn.blog.testscroll.view.pageindicator;

/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import mchenys.net.csdn.blog.testscroll.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class TabPageIndicator extends HorizontalScrollView implements PageIndicator {
    /**
     * Title text used when no title is provided by the adapter.
     */
    private static final CharSequence EMPTY_TITLE = "";

    /**
     * Interface for a callback when the selected tab has been reselected.
     */
    public interface OnTabReselectedListener {
        /**
         * Callback when the selected tab has been reselected.
         *
         * @param position Position of the current center item.
         */
        void onTabReselected(int position, View view);
    }

    private Runnable mTabSelector;

    private final OnClickListener mTabClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            TabLinearLayout tabLinearLayout = (TabLinearLayout) view;
            final int oldSelected = mViewPager.getCurrentItem();
            final int newSelected = tabLinearLayout.getIndex();
            mViewPager.setCurrentItem(newSelected, false);
            if (oldSelected == newSelected && mTabReselectedListener != null) {
                mTabReselectedListener.onTabReselected(newSelected, view);
            }
        }
    };

    private final LinearLayout mTabLayout;

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mListener;

    private int mMaxTabWidth;
    private int mSelectedTabIndex;

    private OnTabReselectedListener mTabReselectedListener;
    private boolean isDividerVisible = true;

    public void setDividerVisible(boolean dividerVisible) {
        isDividerVisible = dividerVisible;
    }

    public TabPageIndicator(Context context) {
        this(context, null);
    }

    @SuppressLint("NewApi")
    public TabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (Integer.parseInt(Build.VERSION.SDK) >= 9) {
            this.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }

        setHorizontalScrollBarEnabled(false);

        mTabLayout = new LinearLayout(context); /*new IcsLinearLayout(context, R.attr.vpiTabPageIndicatorStyle)*/;
        addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
    }

    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
        mTabReselectedListener = listener;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
        setFillViewport(lockedExpanded);

        final int childCount = mTabLayout.getChildCount();

        if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
            if (childCount > 2) {
                mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
            } else {
                mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
            }
        } else {
            mMaxTabWidth = -1;
        }

        final int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newWidth = getMeasuredWidth();

        if (lockedExpanded && oldWidth != newWidth) {
            // Recenter the tab display if we're at a new (scrollable) size.
            setCurrentItem(mSelectedTabIndex);
        }
    }

    private void animateToTab(final int position) {
        final View tabLinearLayout = mTabLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {
            @Override
            public void run() {
                final int scrollPos = tabLinearLayout.getLeft() - (getWidth() - tabLinearLayout.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            // Re-post the selector we saved
            post(mTabSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }

    private void addTab(int index, CharSequence text, int iconResId) {
        final TabLinearLayout tabLinearLayout = new TabLinearLayout(getContext());
        tabLinearLayout.mIndex = index;
        tabLinearLayout.setFocusable(true);
        tabLinearLayout.setOnClickListener(mTabClickListener);
        tabLinearLayout.setText(text);
        tabLinearLayout.setDividerVisible(isDividerVisible);

        if (iconResId != 0) {
            tabLinearLayout.setIcon(iconResId);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT, 1);
//        params.leftMargin = 15;
//        params.rightMargin = 15;
        mTabLayout.addView(tabLinearLayout, params);

        //mTabLayout.addView(tabView, new LinearLayout.LayoutParams(0, MATCH_PARENT, 1));
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(arg0);
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (mListener != null) {
            mListener.onPageScrolled(arg0, arg1, arg2);
        }
    }

    @Override
    public void onPageSelected(int arg0) {
        setCurrentItem(arg0);
        if (mListener != null) {
            mListener.onPageSelected(arg0);
        }
    }

    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        view.setOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        mTabLayout.removeAllViews();
        PagerAdapter adapter = mViewPager.getAdapter();
        IconPagerAdapter iconAdapter = null;
        if (adapter instanceof IconPagerAdapter) {
            iconAdapter = (IconPagerAdapter) adapter;
        }
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            CharSequence title = adapter.getPageTitle(i);
            if (title == null) {
                title = EMPTY_TITLE;
            }
            int iconResId = 0;
            if (iconAdapter != null) {
                iconResId = iconAdapter.getIconResId(i);
            }
            addTab(i, title, iconResId);
        }
        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mSelectedTabIndex = item;
        mViewPager.setCurrentItem(item, false);

        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
            if (isSelected) {
                animateToTab(item);
            }
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }



    private class TabLinearLayout extends LinearLayout {
        private int mIndex;
        private TextView mTabView;
        private ImageView mRedPoint;
        private View mVDivider;

        public TabLinearLayout(Context context) {
            super(context);
            setGravity(Gravity.CENTER_HORIZONTAL); // 设置gravity水平居中
            LayoutInflater.from(context).inflate(R.layout.tab_page_indicator_layout, this);
            mTabView = (TextView) findViewById(R.id.indicator_title);
            mRedPoint = (ImageView) findViewById(R.id.indicator_point);
            mVDivider = findViewById(R.id.v_divider);
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            // Re-measure if we went beyond our maximum size.
            if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
                //为了解决字符宽度比较大的，将测量出来的值直接付给最大宽度
                mMaxTabWidth = getMeasuredWidth();
                super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth, MeasureSpec.EXACTLY),
                        heightMeasureSpec);
            }
        }

        public int getIndex() {
            return mIndex;
        }

        public void setIcon(int iconResId) {
            if (null != mTabView && iconResId != 0) {
                mTabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
            }
        }

        public void setPointVisitable(boolean visitable) {
            mRedPoint.setVisibility(visitable ? VISIBLE : GONE);
        }

        public void setText(CharSequence text) {
            if (null != mTabView)
                mTabView.setText(text);
        }

        public String getText() {
            if (null != mTabView)
                return mTabView.getText().toString();
            return null;
        }

        public void setDividerVisible(boolean visible) {
            mVDivider.setVisibility(visible ? VISIBLE : GONE);
        }
    }

    /**
     * 若TabView中的Text为"xxx\n[num]"格式，可调用此方法进行设置TabView的数字
     *
     * @param num 所设置数字串  e.g. "1" or "(1)" or "[1]"
     */
    public void setTabViewTextNum(int index, String num) {
        if (null == mTabLayout || 0 >= mTabLayout.getChildCount())
            return;
        TabLinearLayout tabLinearLayout = (TabLinearLayout) mTabLayout.getChildAt(index);
        String text = tabLinearLayout.getText();
        if (null != text && !text.isEmpty()) {
            tabLinearLayout.setText(text.split("\n")[0] + "\n" + num);
        }
    }

    /****
     * 用于设置红点提示的显示与隐藏
     * @param index
     * @param visitable
     */
    public void setPointVisitable(int index, boolean visitable) {
        if (null == mTabLayout || 0 >= mTabLayout.getChildCount())
            return;
        TabLinearLayout tabLinearLayout = (TabLinearLayout) mTabLayout.getChildAt(index);
        tabLinearLayout.setPointVisitable(visitable);
    }
}
