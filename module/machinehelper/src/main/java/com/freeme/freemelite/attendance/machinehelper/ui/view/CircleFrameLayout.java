package com.freeme.freemelite.attendance.machinehelper.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.freeme.freemelite.attendance.machinehelper.R;

import static android.graphics.Color.WHITE;

public class CircleFrameLayout extends FrameLayout {
    private Paint roundPaint;
    private float mBorderWidth;
    private int mBorderColor;
    private float mRadius;

    public CircleFrameLayout(Context context) {
        this(context, null);
    }

    public CircleFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleFrameLayout);
            mRadius = ta.getDimension(R.styleable.CircleFrameLayout_radius, 0);
            mBorderWidth = ta.getDimension(R.styleable.CircleFrameLayout_borderwidth, 0);
            mBorderColor = ta.getColor(R.styleable.CircleFrameLayout_borderColor, WHITE);
            ta.recycle();
        }
        roundPaint = new Paint();
        roundPaint.setAntiAlias(true);
        roundPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawSquare(canvas);
        drawBorder(canvas);
    }

    private void drawSquare(Canvas canvas) {
        roundPaint.setColor(getResources().getColor(R.color.background_lq_light));
        canvas.drawRect(0, 0, getWidth(), getHeight(), roundPaint);
    }

    private void drawBorder(Canvas canvas) {
        if (mBorderWidth != 0 && getWidth() == getHeight() && mRadius == getWidth() / 2) {
            roundPaint.setStrokeWidth(mBorderWidth);
            roundPaint.setColor(mBorderColor);
            roundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, roundPaint);
        }

    }
}
