package mchenys.net.csdn.blog.testscroll.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import mchenys.net.csdn.blog.testscroll.R;

/**
 * Created by mChenys on 2018/6/6.
 */

public class LoaderFooterView extends FrameLayout {
    public static final int STATE_IDE = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_NO_MORE = 2;
    public static final int STATE_DONE = 3;

    private int mState;

    private TextView mInfoTv;
    private ProgressBar mLoading;

    public LoaderFooterView(@NonNull Context context) {
        this(context, null);
    }

    public LoaderFooterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoaderFooterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.layout_loader, this);
        mInfoTv = findViewById(R.id.tv_info);
        mLoading = findViewById(R.id.pb_loading);
        setState(STATE_DONE);
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    public void setState(int state) {
        mState = state;
        switch (state) {
            case STATE_IDE:
                mInfoTv.setText("松手加载更多");
                show(true, true, false);
                break;
            case STATE_LOADING:
                mInfoTv.setText("正在加载中...");
                show(true, true, true);
                break;
            case STATE_NO_MORE:
                mInfoTv.setText("没有更多了");
                show(true, true, false);
                break;
            case STATE_DONE:
                mInfoTv.setText("松手加载更多");
                show(false, false, false);
                break;
        }
    }

    public int getState() {
        return mState;
    }

    private void show(boolean parent, boolean info, boolean loading) {
        setVisibility(parent ? VISIBLE : GONE);
        mInfoTv.setVisibility(info ? VISIBLE : GONE);
        mLoading.setVisibility(loading ? VISIBLE : GONE);
    }
}
