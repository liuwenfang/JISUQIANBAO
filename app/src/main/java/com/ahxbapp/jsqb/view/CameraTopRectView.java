package com.ahxbapp.jsqb.view;

import android.view.View;

/**
 * Created by Jayzhang on 16/12/27.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.ahxbapp.jsqb.utils.DeviceUtil;

public class CameraTopRectView extends View {

    private int panelWidth;
    private int panelHeght;

    private int viewWidth;
    private int viewHeight;

    private int rectWidth;
    private int rectHeght;

    private int rectTop;
    private int rectLeft;
    private int rectRight;
    private int rectBottom;

    private int lineLen;
    private int lineWidht;
    private static final int LINE_WIDTH = 5;
    private static final int TOP_BAR_HEIGHT = 50;
    private static final int BOTTOM_BTN_HEIGHT = 66;
    private static final int LEFT_PADDING = 10;
    private static final int RIGHT_PADDING = 10;
    private static final String TIPS = "请将身份证放入到方框中,请横屏拍摄";

    private Paint linePaint;
    private Paint wordPaint;
    private Rect rect;
    private int baseline;

    public CameraTopRectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        Activity activity = (Activity) context;
        int[] sh = DeviceUtil.getScreenSize(activity);
        panelWidth = sh[0];
        panelHeght = sh[1];

        viewHeight = panelHeght
                - DeviceUtil.dip2px(activity, TOP_BAR_HEIGHT + BOTTOM_BTN_HEIGHT);
        viewWidth = panelWidth;

//        rectWidth = panelWidth
//                - DeviceUtil.dip2px(activity,
//                LEFT_PADDING + RIGHT_PADDING);
//        rectHeght = (int) (rectWidth * 54 / 85.6);
        rectHeght = viewHeight
                - DeviceUtil.dip2px(activity,
                130);
        rectWidth = (int) (rectHeght / 1.6);

        // 相对于此view
        rectTop = (viewHeight - rectHeght) / 2;
        rectLeft = (viewWidth - rectWidth) / 2;
        rectBottom = rectTop + rectHeght;
        rectRight = rectLeft + rectWidth;

        lineLen = panelWidth / 8;

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.rgb(0xdd, 0x42, 0x2f));
        linePaint.setStyle(Style.STROKE);
        linePaint.setStrokeWidth(LINE_WIDTH);// 设置线宽
        linePaint.setAlpha(255);

        wordPaint = new Paint();
        wordPaint.setAntiAlias(true);
        wordPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wordPaint.setStrokeWidth(3);
        wordPaint.setTextSize(35);

//        rect = new Rect(rectLeft, rectTop - 80, rectRight, rectTop - 10);
        rect = new Rect(rectLeft, rectTop, rectRight, rectBottom);
        FontMetricsInt fontMetrics = wordPaint.getFontMetricsInt();
        baseline = rect.top
                + (rect.bottom - rect.top - fontMetrics.bottom + fontMetrics.top)
                / 2 - fontMetrics.top;
        wordPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        wordPaint.setColor(Color.TRANSPARENT);
        canvas.drawRect(rect, wordPaint);
        wordPaint.setColor(Color.BLACK);
        canvas.drawText(TIPS, rect.centerX(), baseline, wordPaint);
        canvas.drawLine(rectLeft, rectTop, rectLeft + lineLen, rectTop,
                linePaint);
        canvas.drawLine(rectRight - lineLen, rectTop, rectRight, rectTop,
                linePaint);
        canvas.drawLine(rectLeft, rectTop, rectLeft, rectTop + lineLen,
                linePaint);
        canvas.drawLine(rectRight, rectTop, rectRight, rectTop + lineLen,
                linePaint);
        canvas.drawLine(rectLeft, rectBottom, rectLeft + lineLen, rectBottom,
                linePaint);
        canvas.drawLine(rectRight - lineLen, rectBottom, rectRight, rectBottom,
                linePaint);
        canvas.drawLine(rectLeft, rectBottom - lineLen, rectLeft, rectBottom,
                linePaint);
        canvas.drawLine(rectRight, rectBottom - lineLen, rectRight, rectBottom,
                linePaint);
    }

    public int getRectLeft() {
        return rectLeft;
    }

    public int getRectTop() {
        return rectTop;
    }

    public int getRectRight() {
        return rectRight;
    }

    public int getRectBottom() {
        return rectBottom;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }

}
