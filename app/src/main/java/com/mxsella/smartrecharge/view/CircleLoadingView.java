package com.mxsella.smartrecharge.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.mxsella.smartrecharge.R;

public class CircleLoadingView extends View {
    private int mColor = Color.argb(255, 0, 0, 0);
    private int mStartAngle = 0;
    private float mStrokeWidth = 0;
    private boolean isAutoStart;
    private final int mLineCount = 12;
    private final int minAlpha = 0;
    private final int mAngleGradient = 360 / mLineCount;
    private int[] mColors = new int[mLineCount];
    private Paint mPaint;

    private Handler mAnimHandler = new Handler(Looper.getMainLooper());

    private Runnable mAnimRunnable = new Runnable() {
        @Override
        public void run() {
            mStartAngle += mAngleGradient;
            invalidate();
            mAnimHandler.postDelayed(mAnimRunnable, 50);
        }
    };

    public CircleLoadingView(Context context) {
        this(context, null);
    }

    public CircleLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context, attrs, defStyleAttr, 0);
    }

    private void setup(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleLoadingView, defStyleAttr, defStyleRes);
        mColor = typedArray.getColor(R.styleable.CircleLoadingView_clv_color, mColor);
        mStartAngle = typedArray.getInt(R.styleable.CircleLoadingView_clv_startAngle, mStartAngle);
        mStrokeWidth = typedArray.getDimension(R.styleable.CircleLoadingView_clv_strokeWidth, mStrokeWidth);
        isAutoStart = typedArray.getBoolean(R.styleable.CircleLoadingView_clv_auto_start, false);
        typedArray.recycle();
        initialize();
    }

    private void initialize() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int alpha = Color.alpha(mColor);
        int red = Color.red(mColor);
        int green = Color.green(mColor);
        int blue = Color.blue(mColor);
        int alphaGradient = Math.abs(alpha - minAlpha) / mLineCount;
        for (int i = 0; i < mColors.length; i++) {
            mColors[i] = Color.argb(alpha - alphaGradient * i, red, green, blue);
        }
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        float radius = Math.min(getWidth() - getPaddingLeft() - getPaddingRight(), getHeight() - getPaddingTop() - getPaddingBottom()) * 0.5f;
        if (mStrokeWidth == 0) {
            mStrokeWidth = pointX(mAngleGradient / 2, radius / 2) / 2;
        }
        mPaint.setStrokeWidth(mStrokeWidth);
        for (int i = 0; i < mColors.length; i++) {
            mPaint.setColor(mColors[i]);
            canvas.drawLine(
                    centerX + pointX(-mAngleGradient * i + mStartAngle, radius / 2),
                    centerY + pointY(-mAngleGradient * i + mStartAngle, radius / 2),
                    centerX + pointX(-mAngleGradient * i + mStartAngle, radius - mStrokeWidth / 2),
                    centerY + pointY(-mAngleGradient * i + mStartAngle, radius - mStrokeWidth / 2),
                    mPaint);
        }
    }

    private float pointX(int angle, float radius) {
        return (float) (radius * Math.cos(angle * Math.PI / 180));
    }

    private float pointY(int angle, float radius) {
        return (float) (radius * Math.sin(angle * Math.PI / 180));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isAutoStart) {
            start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimHandler != null) {
            stop();
        }
    }

    public void start() {
        mAnimHandler.post(mAnimRunnable);
    }

    public void stop() {
        mAnimHandler.removeCallbacks(mAnimRunnable);
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.mStrokeWidth = strokeWidth;
    }

    public void setStartAngle(int startAngle) {
        this.mStartAngle = startAngle;
    }
}
