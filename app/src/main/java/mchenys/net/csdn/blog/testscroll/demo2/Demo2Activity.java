package mchenys.net.csdn.blog.testscroll.demo2;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mchenys.net.csdn.blog.testscroll.R;
import mchenys.net.csdn.blog.testscroll.fragment.TabFrament;
import mchenys.net.csdn.blog.testscroll.view.FocusCircleView;
import mchenys.net.csdn.blog.testscroll.view.FragmentPagerAdapterCompat;
import mchenys.net.csdn.blog.testscroll.view.ImageViewPager;
import mchenys.net.csdn.blog.testscroll.view.RefreshLayout;
import mchenys.net.csdn.blog.testscroll.view.pageindicator.TabPageIndicator;

/**
 * 嵌套滑动
 * Created by mChenys on 2018/6/5.
 */

public class Demo2Activity extends AppCompatActivity {
    private ViewPager mVpContent;
    private ImageViewPager mFocusViewPager;
    private List<String> mData1 = new ArrayList<>();
    private List<String> mData2 = new ArrayList<>();
    private FocusCircleView mFocusCircleView;
    private ImageViewPager.BasePagerAdapter<String> mFocusAdapter;
    private FragmentPagerAdapterCompat mPagerAdapterCompat;
    private TabPageIndicator mTabIndicator;
    private RefreshLayout mRefreshLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo2);
        initData();
        initView();
        initListener();
    }

    private void initData() {
        for (int i = 1; i <= 8; i++) {
            mData1.add("第" + i + "页");
        }
        for (int i = 1; i <= 5; i++) {
            mData2.add("焦点图第" + i + "页");
        }
    }



    private void initView() {

        //焦点图和指示器
        mFocusCircleView =  findViewById(R.id.FocusCircleView);
        mFocusViewPager = findViewById(R.id.imageview);
        mFocusViewPager.setAdapter(mFocusAdapter=new ImageViewPager.BasePagerAdapter<String>(this, mData2) {
            @Override
            public View getItemView(Context context, int position, String s) {
                TextView view = (TextView) View.inflate(context, android.R.layout.simple_list_item_1, null);
                view.setText(s);
                return view;
            }
        });
        mFocusCircleView.setCount(mFocusAdapter.getDataCount());
        mFocusCircleView.setCurrentFocus(mFocusViewPager.getCurrentItem() % mData2.size());

        //内容视图
        mVpContent = findViewById(R.id.vp);
        mVpContent.setAdapter(mPagerAdapterCompat=new FragmentPagerAdapterCompat(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return TabFrament.newInstance(position);
            }

            @Override
            public int getCount() {
                return mData1.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mData1.get(position);
            }
        });
        mTabIndicator = findViewById(R.id.tab_indicator);
        mTabIndicator.setViewPager(mVpContent);
        mTabIndicator.notifyDataSetChanged();
        mTabIndicator.setCurrentItem(0);

        //刷新view
        mRefreshLayout = findViewById(R.id.refreshLayout);
    }


    private void initListener() {
        mFocusViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mData2 != null && !mData2.isEmpty()) {
                    position = position % mData2.size();
                    mFocusCircleView.setCurrentFocus(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.onComplete();
                        Toast.makeText(Demo2Activity.this, "刷新完毕", Toast.LENGTH_SHORT).show();
                        TabFrament frament = (TabFrament) mPagerAdapterCompat.getFragment(mVpContent.getCurrentItem());
                        frament.refresh();
                    }
                },500);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        mFocusViewPager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFocusViewPager.onPause();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

}
