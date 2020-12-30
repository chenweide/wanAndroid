package com.cwd.wandroid.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.cwd.wandroid.R;
import com.cwd.wandroid.utils.DensityUtil;
import com.cwd.wandroid.utils.LogUtils;

public class DynamicNumberTextView extends View implements Runnable {

    private Paint mPaint;
    private int textColor;
    private float textSize;
    private String text;

    private Context context;
    private boolean isEnd = false;
    private int curNum = 0;
    private int finalNum = 0;

    private static final int UPDATE_UI = 0x11;
    private WorkHandler workHandler;

    private class WorkHandler extends Handler {

        public WorkHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_UI) {
                requestLayout();
                postInvalidate();
            }
        }
    };

    public DynamicNumberTextView(Context context) {
        this(context,null);
    }

    public DynamicNumberTextView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public DynamicNumberTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DynamicNumberTextView);
        textColor = ta.getColor(R.styleable.DynamicNumberTextView_textColor, Color.BLACK);
        textSize = ta.getDimension(R.styleable.DynamicNumberTextView_textSize, DensityUtil.sp2px(context,16));
        text = ta.getString(R.styleable.DynamicNumberTextView_text);
        ta.recycle();
        this.context = context;
        init();
    }

    private void init() {
        checkText();
        initPaint();
        new Thread(this).start();
    }

    private void checkText() {
        if(!TextUtils.isEmpty(text)) {
            try {
                Integer.parseInt(text);
            } catch (NumberFormatException e) {
                LogUtils.e("只能设置数字");
            }
            finalNum = Integer.parseInt(text);
        }
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
    }

    private void logic() {

    }

    private void drawView(Canvas canvas) {
        float textWidth = getTextWidthHeight().width();
        float textHeight = getTextWidthHeight().height();
        canvas.drawText(text,(getMeasuredWidth() - textWidth) / 2,(getMeasuredHeight() - textHeight) / 2 + textHeight,mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        logic();
        drawView(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(resolveSize(getTextWidthHeight().width() + DensityUtil.dip2px(context,5),widthMeasureSpec),
                resolveSize(getTextWidthHeight().height() + DensityUtil.dip2px(context,5),heightMeasureSpec));
    }

    private Rect getTextWidthHeight() {
        Rect rect = new Rect();
        mPaint.getTextBounds(text,0,text.length(),rect);
        return rect;
    }

    public void setText(String text) {
        this.text = text;
        isEnd = false;
        curNum = 0;
        finalNum = Integer.parseInt(text);
        requestLayout();
        postInvalidate();
    }

    @Override
    public void run() {
        Looper.prepare();
        while (!isEnd) {
            int step = finalNum / 50;
            curNum += step;
            int dx = finalNum - curNum;
            if (dx < step) {
                curNum = finalNum;
            }
            LogUtils.d(curNum + "---");
            if (curNum == finalNum && finalNum != 0) {
                isEnd = true;
                LogUtils.d(curNum + "---over");
            }
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            text = String.valueOf(curNum);
//            requestLayout();
//            postInvalidate();
            if(workHandler == null) {
                workHandler = new WorkHandler(Looper.getMainLooper());
            }
            workHandler.sendEmptyMessage(UPDATE_UI);

        }
        Looper.loop();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (workHandler != null) {
            workHandler.removeCallbacksAndMessages(null);
            workHandler = null;
        }
        isEnd = true;
        this.context = null;
    }
}
