package mchenys.net.csdn.blog.testscroll.demo1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import mchenys.net.csdn.blog.testscroll.R;
import mchenys.net.csdn.blog.testscroll.fragment.TabFrament;

/**
 * 非嵌套滑动
 * Created by mChenys on 2018/6/4.
 */

public class Demo1Activity extends AppCompatActivity {
    private ViewPager vp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo1);
        initView();
    }

    private void initView() {
        vp = findViewById(R.id.vp);
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return TabFrament.newInstance(position);
            }

            @Override
            public int getCount() {
                return 4;
            }
        });
    }
}
