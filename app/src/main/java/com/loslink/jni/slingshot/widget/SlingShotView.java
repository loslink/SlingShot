package com.loslink.jni.slingshot.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.loslink.jni.slingshot.R;

public class SlingShotView extends View {

    private Paint mPaint,baselinePaint;
    private float canvasWidth,canvasHeight;
    private Matrix matrixSling,matrixTarget;
    private Bitmap circleSling,circleTarget;

    public SlingShotView(Context context) {
        this(context,null);
    }


    public SlingShotView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlingShotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        baselinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        baselinePaint.setColor(Color.RED);
        baselinePaint.setStyle(Paint.Style.STROKE);
        baselinePaint.setStrokeWidth(2);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);

        matrixSling = new Matrix();
        circleSling = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_slingshot)).getBitmap();

        matrixTarget = new Matrix();
        circleTarget = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_ba)).getBitmap();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasWidth=w;
        canvasHeight=h;

    }

    @Override
    protected void onDraw(Canvas canvas) {//更新画布

        canvas.translate(canvasWidth/2,canvasHeight/2);

        canvas.drawLine(-canvasWidth/2,0,canvasWidth/2,0,baselinePaint);
        canvas.drawLine(0,-canvasHeight/2,0,canvasHeight/2,baselinePaint);

        matrixSling.reset();
        float sx=((float) canvas.getWidth()/ circleSling.getWidth())*0.6f;
        matrixSling.setScale(sx,sx);
        matrixSling.postTranslate(-circleSling.getWidth()*sx/2,canvas.getHeight()/2- circleSling.getHeight());
        canvas.drawBitmap(circleSling, matrixSling,mPaint);

        matrixTarget.reset();
        float sxTarget=((float) canvas.getWidth()/ circleTarget.getWidth())*0.1f;
        matrixTarget.setScale(sxTarget,sxTarget);
        matrixTarget.postTranslate(-circleTarget.getWidth()*sxTarget/2,-circleTarget.getWidth()*sxTarget/2);
        canvas.drawBitmap(circleTarget, matrixTarget,mPaint);

    }


}
