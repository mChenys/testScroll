package mchenys.net.csdn.blog.testscroll.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

public abstract class FragmentPagerAdapterCompat extends FragmentPagerAdapter {

    private SparseArray<Fragment> fragments;

    public FragmentPagerAdapterCompat(FragmentManager fm) {
        super(fm);
        fragments = new SparseArray<>(getCount());
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        fragments.remove(position);
    }

    public Fragment getFragment(int position) {
        return fragments.get(position);
    }

}
