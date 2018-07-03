package com.loslink.jni.slingshot.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.loslink.jni.slingshot.R;

public class SlingShotView extends View {

    private Paint mPaint,baselinePaint,rubberPaint,centerPiPaint,stonePiPaint;
    private float canvasWidth,canvasHeight;
    private Matrix matrixSling,matrixTarget;
    private Bitmap circleSling,circleTarget;
    private float centerPiH=80;
    private float leftCenterPiStartX,leftCenterPiStartY,rightCenterPiEndX,rightCenterPiEndY;

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

        rubberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rubberPaint.setColor(Color.YELLOW);
        rubberPaint.setStyle(Paint.Style.STROKE);
        rubberPaint.setStrokeCap(Paint.Cap.ROUND);
        rubberPaint.setStrokeWidth(20);

        stonePiPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        stonePiPaint.setColor(Color.YELLOW);
        stonePiPaint.setStyle(Paint.Style.FILL);
        stonePiPaint.setStrokeCap(Paint.Cap.ROUND);
        stonePiPaint.setStrokeWidth(20);

        centerPiPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerPiPaint.setColor(Color.parseColor("#191818"));
        centerPiPaint.setStyle(Paint.Style.STROKE);
        centerPiPaint.setStrokeCap(Paint.Cap.ROUND);
        centerPiPaint.setStrokeWidth(centerPiH);

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

        touchX=0;
        touchY=(canvasHeight/2-circleSling.getHeight());
        leftCenterPiStartX=-centerPiW/2;
        leftCenterPiStartY=(canvasHeight/2-circleSling.getHeight());

        rightCenterPiEndX=centerPiW/2;
        rightCenterPiEndY=(canvasHeight/2-circleSling.getHeight());
    }

    private int centerPiW=200;

    @Override
    protected void onDraw(Canvas canvas) {//更新画布

        canvas.translate(canvasWidth/2,canvasHeight/2);

//        canvas.drawLine(-canvasWidth/2,0,canvasWidth/2,0,baselinePaint);
//        canvas.drawLine(0,-canvasHeight/2,0,canvasHeight/2,baselinePaint);

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

        drawRubber(canvas);

    }

    private float touchX,touchY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction()==MotionEvent.ACTION_DOWN){
            touchX=event.getX()-canvasWidth/2;
            touchY=event.getY()-canvasHeight/2;
            getleftCenterPiStartPoint();

        }else if(event.getAction()==MotionEvent.ACTION_MOVE){
            touchX=event.getX()-canvasWidth/2;
            touchY=event.getY()-canvasHeight/2;
            Log.v("SlingShotView"," touchX:"+touchX+"  touchY:"+touchY);
            getleftCenterPiStartPoint();

        }else if (event.getAction()==MotionEvent.ACTION_UP){
            touchX=0;
            touchY=(canvasHeight/2-circleSling.getHeight());
            leftCenterPiStartX=-centerPiW/2;
            leftCenterPiStartY=(canvasHeight/2-circleSling.getHeight());

            rightCenterPiEndX=centerPiW/2;
            rightCenterPiEndY=(canvasHeight/2-circleSling.getHeight());
        }
        postInvalidate();
        return true;
    }

    private void getleftCenterPiStartPoint(){
        float A=(circleSling.getWidth()/2)+touchX;
        float B=touchY-(canvasHeight/2-circleSling.getHeight());
        float C=centerPiW/2;
        leftCenterPiStartX=-(A*C)/(float) Math.sqrt(A*A+B*B)+touchX;
        leftCenterPiStartY=touchY-(B*C)/(float) Math.sqrt(A*A+B*B);

        float A2=(circleSling.getWidth()/2)-touchX;
        rightCenterPiEndX=(A2*C)/(float) Math.sqrt(A2*A2+B*B)+touchX;
        rightCenterPiEndY=touchY-(B*C)/(float) Math.sqrt(A2*A2+B*B);
    }

    private void drawRubber(Canvas canvas){

        //前橡胶
        canvas.drawLine(-(canvasWidth/2-circleSling.getWidth()/2),
                (canvasHeight/2-circleSling.getHeight()),
                touchX,
                touchY,
                rubberPaint);

        //后橡胶
        canvas.drawLine(touchX,
                touchY,
                circleSling.getWidth()/2,
                (canvasHeight/2-circleSling.getHeight()),
                rubberPaint);

        //前皮革
        canvas.drawLine(leftCenterPiStartX,
                leftCenterPiStartY,
                touchX,
                touchY,
                centerPiPaint);

        //后皮革
        canvas.drawLine(touchX,
                touchY,
                rightCenterPiEndX,
                rightCenterPiEndY,
                centerPiPaint);

        //石头
        canvas.drawCircle(touchX,touchY,20,stonePiPaint);
    }


}
