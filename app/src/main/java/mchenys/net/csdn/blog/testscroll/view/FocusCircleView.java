package mchenys.net.csdn.blog.testscroll.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import mchenys.net.csdn.blog.testscroll.R;


/**
 * 焦点图圆点
 * Created by user on 2017/8/28.
 */

public class FocusCircleView extends LinearLayout {

    private Context context;
    private int count;

    public FocusCircleView(Context context) {
        super(context);
        init(context);
    }

    public FocusCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FocusCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        this.setOrientation(LinearLayout.HORIZONTAL);
    }

    /***
     * 设置检点图数目
     * @param count
     */
    public void setCount(int count){
        this.count = count;
        this.removeAllViews();
        for(int i = 0;i<count;i++){
            ImageView img = new ImageView(context);
            img.setPadding(8, 0, 8, 0);
            this.addView(img, i);
        }
    }

    public void setCurrentFocus(int current){
        for(int i = 0;i<count;i++){
            ImageView img = (ImageView) this.getChildAt(i);
            if(i==current){
                img.setImageResource(R.drawable.focus_circle_true);
            }else {
                img.setImageResource(R.drawable.focus_circle_false);
            }
        }
    }

}
