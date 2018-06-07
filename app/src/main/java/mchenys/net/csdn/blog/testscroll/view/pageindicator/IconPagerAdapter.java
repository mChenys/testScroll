package mchenys.net.csdn.blog.testscroll.view.pageindicator;


public interface IconPagerAdapter {
    /**
     * Get icon representing the page at {@code index} in the adapter.
     */
    int getIconResId(int index);

    // From PagerAdapter
    int getCount();
}
