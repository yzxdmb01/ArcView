package com.arc.view.arcview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.arc.view.arcview.R;

/**
 * Created by XIAS on 2017/12/25.
 */

public class ArcView extends View {

    private boolean mBump = false;//弧形是凸还是凹，默认是凸
    private int mArcHeight = 80;//弧形高度
    private int mBgColor = Color.WHITE;//弧形背景颜色

    private Paint mPaint;//画笔
    private Path mPath;//贝塞尔曲线路径
    private PointF mSPointF, mCPointF, mEPointF;//贝塞尔曲线关键点 分别为起始点，控制点，终止点
    private int w, h;

    public ArcView(Context context) {
        this(context, null);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs){
        //是否设置弧形高度
        TypedArray ta = attrs == null ? null : context.obtainStyledAttributes(attrs, R.styleable.arc);
        if(ta != null){
            mBump = ta.getBoolean(R.styleable.arc_arcbump, mBump);
            mArcHeight = dip2px(context, ta.getInteger(R.styleable.arc_archeight, mArcHeight));
            mBgColor = ta.getInteger(R.styleable.arc_arcbgcolor, mBgColor);
        }
        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBgColor);

        mPath = new Path();
        mSPointF = new PointF();
        mCPointF = new PointF();
        mEPointF = new PointF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
    }

    private void drawArc(){
        mPath.reset();
        mPath.moveTo(0, 0);
        if(mBump)
            mPath.addRect(0, 0, w, h, Path.Direction.CCW);
        else
            mPath.addRect(0, 0, w, h - mArcHeight, Path.Direction.CCW);
        mSPointF.x = 0;
        if(mBump)
            mSPointF.y = h;
        else
            mSPointF.y = h - mArcHeight;

        mEPointF.x = w;
        if(mBump)
            mEPointF.y = h;
        else
            mEPointF.y = h - mArcHeight;

        mCPointF.x = w / 2;
        if(mBump)
            mCPointF.y = h - mArcHeight;
        else
            mCPointF.y = h + mArcHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawArc();
        mPaint.setColor(mBgColor);
        mPath.moveTo(mSPointF.x, mSPointF.y);
        mPath.quadTo(mCPointF.x, mCPointF.y, mEPointF.x, mEPointF.y);
        canvas.drawPath(mPath, mPaint);
        super.onDraw(canvas);
    }

    private int dip2px(Context context, float dpValue) {
        if (context == null) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
