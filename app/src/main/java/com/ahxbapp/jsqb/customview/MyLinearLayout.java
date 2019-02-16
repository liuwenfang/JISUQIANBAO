package com.ahxbapp.jsqb.customview;

/**
 * Created by 学祺 on 2015/12/29.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Created by wyq on 2015/12/16.
 */
public class MyLinearLayout extends LinearLayout {
    private static final String TAG ="CustomShareBoard";
    private Context context;
    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context= context;

    }

    public MyLinearLayout(Context context) {
        super(context);
        this.context= context;
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context= context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(!isOffset()) {
            Log.i(TAG, "不需要偏移");
            if (this.getPaddingBottom()!=0) {
                this.setPadding(0, 0, 0, 0);
            }
        } else{
            Log.i(TAG, "需要偏移");
            this.setPadding(0, 0, 0, getNavigationBarHeight());
            invalidate();//这个一定要加上 不然会出错 我也不知道为什么如果有人找到原因请回复我 在此先谢过了
        }
        super.onLayout(true, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private int getNavigationBarHeight() {
        Resources resources = this.context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
    public boolean isOffset() {
        return getDecorViewHeight() > getScreenHeight();
    }


    public int getDecorViewHeight(){
        return ((Activity)this.context).getWindow().getDecorView().getHeight();
    }

    public int getScreenHeight(){
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)this.context).getWindowManager().getDefaultDisplay().getMetrics(dm);//当前activity
        return dm.heightPixels;
    }
}