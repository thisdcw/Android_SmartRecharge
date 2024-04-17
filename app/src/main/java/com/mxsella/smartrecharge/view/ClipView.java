package com.mxsella.smartrecharge.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ClipView extends FrameLayout {
    private int mHeight = 0;
    private int mWight = 0;
    private int halfH;
    private int halfW;
    private int clipDp;
    private int scanSize = 300;
    private Paint paint;
    Context context;
    public ClipView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWight = w;
    }
    @Override
    protected void dispatchDraw(Canvas canvas) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float density = dm.density;
        int scanFrameSize = (int) (this.scanSize * density);

        halfH = mHeight / 2;
        halfW = mWight / 2;
        clipDp = scanFrameSize / 2;
        int left = halfW - clipDp;
        int top = halfH - clipDp;
        int right = halfW + clipDp;
        int bottom = halfH + clipDp;
        canvas.drawRect(left, top, right, bottom, getPaint());

        canvas.clipRect(new Rect(
                left,
                top,
                right,
                bottom
        ));
        super.dispatchDraw(canvas);
    }

    public void setScanFrameSize(int scanSize) {
        this.scanSize = scanSize;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    private Paint getPaint() {
        if (paint == null) {
            paint = new Paint();
            paint.setColor(Color.WHITE); // 设置边框颜色
            paint.setStyle(Paint.Style.STROKE); // 设置绘制样式为边框
            paint.setStrokeWidth(5); // 设置边框宽度
        }
        return paint;
    }
}
