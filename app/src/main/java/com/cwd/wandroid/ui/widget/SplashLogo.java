package com.cwd.wandroid.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.cwd.wandroid.R;
import com.cwd.wandroid.utils.DensityUtil;

public class SplashLogo extends View implements Runnable{

    private static final int UPDATE_UI = 1;

    private Context context;

    private Paint textPaint;
    private Paint circlePaint;

    private String text = "悟";

    private int arcAngle = 0;

    private boolean isAnimFinish;

    private UIHandler uiHandler;

    public OnAnimationFinishedListener listener;

    public interface OnAnimationFinishedListener {
        void onAnimationFinished();
    }

    public void setOnAnimationFinishedListener(OnAnimationFinishedListener listener) {
        this.listener = listener;
    }

    public SplashLogo(Context context) {
        super(context);
        initPaint(context);
    }

    public SplashLogo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    public SplashLogo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);
    }

    private void initPaint(Context context) {
        this.context = context;
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(context.getResources().getColor(R.color.textColor));
        circlePaint.setColor(context.getResources().getColor(R.color.textColor));
        circlePaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(DensityUtil.sp2px(context,80));
        circlePaint.setStrokeWidth(10);
        new Thread(this).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x = (float) (getMeasuredWidth() - getTextWidthHeight().width()) / 2;
        float y = (float) (getMeasuredHeight() - getTextWidthHeight().height()) / 2 + getTextWidthHeight().height();
        canvas.drawText(text,x,y,textPaint);
        RectF rect = new RectF(DensityUtil.dip2px(context,5),DensityUtil.dip2px(context,5),getMeasuredWidth() - DensityUtil.dip2px(context,5),getMeasuredHeight() - DensityUtil.dip2px(context,5));
        canvas.drawArc(rect,0,arcAngle,false,circlePaint);
        canvas.drawArc(rect,90,arcAngle,false,circlePaint);
        canvas.drawArc(rect,180,arcAngle,false,circlePaint);
        canvas.drawArc(rect,270,arcAngle,false,circlePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(resolveSize(getTextWidthHeight().width() + DensityUtil.dip2px(context,60),widthMeasureSpec),
                resolveSize(getTextWidthHeight().height() + DensityUtil.dip2px(context,60),heightMeasureSpec));
    }

    private Rect getTextWidthHeight() {
        Rect rect = new Rect();
        textPaint.getTextBounds(text,0,text.length(),rect);
        return rect;
    }

    @Override
    public void run() {
        Looper.prepare();
        if (uiHandler == null) {
            uiHandler = new UIHandler(Looper.getMainLooper());
        }
        while (!isAnimFinish) {
            if (arcAngle < 90) {
                arcAngle++;
            } else {
                isAnimFinish = true;
                uiHandler.sendEmptyMessage(UPDATE_UI);
            }
            postInvalidate();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Looper.loop();
    }

    private class UIHandler extends Handler {

        public UIHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_UI) {
                animate().scaleX(2f).scaleY(2f).alpha(0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (listener != null) {
                            listener.onAnimationFinished();
                        }
                    }
                }).setStartDelay(800).setDuration(800).start();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (uiHandler != null) {
            uiHandler.removeCallbacksAndMessages(null);
            uiHandler = null;
        }
        isAnimFinish = true;
        context = null;
    }
}
