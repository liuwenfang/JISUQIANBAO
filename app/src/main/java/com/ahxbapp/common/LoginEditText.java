package com.ahxbapp.common;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.ahxbapp.common.enter.SimpleTextWatcher;
import com.ahxbapp.common.util.OnTextChange;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.utils.DeviceUtil;


/**
 * Created by gravel on 15/1/6.
 */
public class LoginEditText extends EditText implements OnTextChange {

    Drawable drawable;
    Drawable leftDraw;
    public LoginEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        boolean useDark = false;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Crossed, 0, 0);


        int crossedRes = useDark ? R.mipmap.delete_edit_login : R.mipmap.delete_edit_login_black;
        drawable = getResources().getDrawable(crossedRes);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                displayDelete(s.length() > 0);
            }
        });
        try {
            useDark = a.getBoolean(R.styleable.Crossed_dark, false);
            int  mIcon= a.getResourceId(R.styleable.Crossed_leftIcon, 0);
            if(mIcon!=0){
                Resources res = getResources();
                leftDraw = res.getDrawable(mIcon);
                leftDraw.setBounds(0, 0, leftDraw.getMinimumWidth(), leftDraw.getMinimumHeight());
            }

            setDrawableLeft(leftDraw);

//            setDrawableRight(drawable);
        } finally {
            a.recycle();
        }
    }

    public void setLeftIcon(int mIcon){
        if(mIcon!=0){
            Resources res = getResources();
            leftDraw = res.getDrawable(mIcon);
            leftDraw.setBounds(0, 0, leftDraw.getMinimumWidth(), leftDraw.getMinimumHeight());
        }
        setDrawableLeft(leftDraw);
    }

    private void displayDelete(boolean show) {
        if (show) {
            //隐藏右边删除图标
//            setDrawableRight(drawable);
        } else {
            setDrawableRight(null);
        }
    }
    private  void setDrawableLeft(Drawable draw){

        setCompoundDrawables(draw, null,null , null);
    }
    private void setDrawableRight(Drawable drawable) {
        setCompoundDrawables(leftDraw, null, drawable, null);
        setCompoundDrawablePadding(DeviceUtil.dip2px(getContext(),15));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    this.setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean isEmpty() {
        return this.getText().length() == 0;
    }
}