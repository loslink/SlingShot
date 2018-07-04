package com.loslink.jni.slingshot.widget;

import android.animation.ValueAnimator;
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

import com.loslink.jni.slingshot.DelerAlerInterploator;
import com.loslink.jni.slingshot.R;

public class SlingShotView extends View {

    private Paint mPaint, baselinePaint, rubberPaint, centerPiPaint, stonePiPaint;
    private float canvasWidth, canvasHeight;
    private Matrix matrixSling, matrixTarget;
    private Bitmap circleSling, circleTarget;
    private float centerPiH = 80;
    private float leftCenterPiStartX, leftCenterPiStartY, rightCenterPiEndX, rightCenterPiEndY;
    private float paramA = 300,paramB = 300;//范围0-
    private float xPercent=0.8f;
    private float touchCenterX,touchCenterY;
    private BunblePoint point;
    private float touchX, touchY;
    private float stoneRadius=20f;
    private float slingShotTopY=0;
    private long duration=3000;

    public SlingShotView(Context context) {
        this(context, null);
    }


    public SlingShotView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlingShotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        baselinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        baselinePaint.setColor(Color.RED);
        baselinePaint.setStyle(Paint.Style.FILL);
        baselinePaint.setStrokeWidth(2);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);

        rubberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rubberPaint.setColor(Color.YELLOW);
        rubberPaint.setStyle(Paint.Style.STROKE);
        rubberPaint.setStrokeCap(Paint.Cap.ROUND);
        rubberPaint.setStrokeWidth(20);

        stonePiPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
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

        point=new BunblePoint(0,0);
        point.radius=stoneRadius;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasWidth = w;
        canvasHeight = h;

        slingShotTopY=(canvasHeight / 2 - circleSling.getHeight());
        touchX = 0;
        touchY = slingShotTopY;
        leftCenterPiStartX = -centerPiW / 2;
        leftCenterPiStartY = slingShotTopY;

        rightCenterPiEndX = centerPiW / 2;
        rightCenterPiEndY = slingShotTopY;

        paramB =canvasHeight/4;

    }

    private int centerPiW = 200;

    @Override
    protected void onDraw(Canvas canvas) {//更新画布

        canvas.translate(canvasWidth / 2, canvasHeight / 2);

//        canvas.drawLine(-canvasWidth/2,0,canvasWidth/2,0,baselinePaint);
//        canvas.drawLine(0,-canvasHeight/2,0,canvasHeight/2,baselinePaint);

        matrixSling.reset();
        float sx = ((float) canvas.getWidth() / circleSling.getWidth()) * 0.6f;
        matrixSling.setScale(sx, sx);
        matrixSling.postTranslate(-circleSling.getWidth() * sx / 2, slingShotTopY);
        canvas.drawBitmap(circleSling, matrixSling, mPaint);

        matrixTarget.reset();
        float sxTarget = ((float) canvas.getWidth() / circleTarget.getWidth()) * 0.1f;
        matrixTarget.setScale(sxTarget, sxTarget);
        matrixTarget.postTranslate(-circleTarget.getWidth() * sxTarget / 2, -circleTarget.getWidth() * sxTarget / 2);
        canvas.drawBitmap(circleTarget, matrixTarget, mPaint);

        drawRubber(canvas);

        if(isShot){
            drawPoint(canvas);
        }

//        canvas.translate(touchCenterX, touchCenterY);
//        canvas.scale(1,-1);
//        for(float i=0;i<canvasWidth;i++){
//            float y = (300) * (float) Math.sin(i / (300));
//            canvas.drawCircle(i, y, 2, baselinePaint);
//        }
    }

    private void drawPoint(Canvas canvas){
        canvas.translate(touchX, touchY);
        canvas.scale(1,-1);
        //石头
        canvas.drawCircle(point.x, point.y, point.radius, stonePiPaint);
    }

    /**
     * 原点后第二个与X轴交叉的点X
     * @return
     */
    private float getSecondZeroX(){
        return paramA*(float) Math.PI;
    }

    private boolean isShot=false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        isShot=false;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchX = event.getX() - canvasWidth / 2;
            touchY = event.getY() - canvasHeight / 2;
            getleftCenterPiStartPoint();

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            touchX = event.getX() - canvasWidth / 2;
            touchY = event.getY() - canvasHeight / 2;
            Log.v("SlingShotView", " touchX:" + touchX + "  touchY:" + touchY);
            getleftCenterPiStartPoint();

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            touchX = 0;
            touchY = slingShotTopY;
            leftCenterPiStartX = -centerPiW / 2;
            leftCenterPiStartY = slingShotTopY;

            rightCenterPiEndX = centerPiW / 2;
            rightCenterPiEndY = slingShotTopY;

            touchCenterX = event.getX() - canvasWidth / 2;
            touchCenterY = event.getY() - canvasHeight / 2;

            isShot=true;
            point.x=0;
            point.y=0;
            calcuParams();
            startShot();
        }
        postInvalidate();
        return true;
    }

    private void calcuParams(){
        float b=touchCenterY-slingShotTopY;
        float a=Math.abs(touchCenterX);
        float c=(float) Math.sqrt(a*a+b*b);
        paramA=(a/b)*paramB;

        float d=canvasHeight/2-slingShotTopY;
        paramB =(canvasHeight*0.28f)*(b/d);
    }

    private void startShot(){

        final ValueAnimator animatorBg=ValueAnimator.ofFloat(1,0f);

        animatorBg.setDuration(duration);
        animatorBg.setInterpolator(new DelerAlerInterploator());
        animatorBg.setRepeatCount(0);
        animatorBg.setRepeatMode(ValueAnimator.RESTART);
        animatorBg.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                calcuPoints(animation);
                postInvalidate();
            }
        });

        animatorBg.start();

    }

    private void getleftCenterPiStartPoint() {
        float A = (circleSling.getWidth() / 2) + touchX;
        float B = touchY - slingShotTopY;
        float C = centerPiW / 2;
        leftCenterPiStartX = -(A * C) / (float) Math.sqrt(A * A + B * B) + touchX;
        leftCenterPiStartY = touchY - (B * C) / (float) Math.sqrt(A * A + B * B);

        float A2 = (circleSling.getWidth() / 2) - touchX;
        rightCenterPiEndX = (A2 * C) / (float) Math.sqrt(A2 * A2 + B * B) + touchX;
        rightCenterPiEndY = touchY - (B * C) / (float) Math.sqrt(A2 * A2 + B * B);
    }

    private void drawRubber(Canvas canvas) {

        //前橡胶
        canvas.drawLine(-(canvasWidth / 2 - circleSling.getWidth() / 2),
                slingShotTopY,
                touchX,
                touchY,
                rubberPaint);

        //后橡胶
        canvas.drawLine(touchX,
                touchY,
                circleSling.getWidth() / 2,
                slingShotTopY,
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
        canvas.drawCircle(touchX, touchY, stoneRadius, stonePiPaint);
    }

    private void calcuPoints(ValueAnimator animation) {

//        step = (((float) animation.getCurrentPlayTime() - lastAnimTime) / (float) animation.getDuration())
//                * (canvasWidth / 2 + pointAreaW);
//        lastAnimTime = (float) animation.getCurrentPlayTime();
        point.radius=stoneRadius*(float)animation.getAnimatedValue();

        float step = 5;
        if (touchCenterX >= 0 && touchCenterY <= 0) {//第一象限

        } else if (touchCenterX < 0 && touchCenterY < 0) {//第二象限

        } else if (touchCenterX <= 0 && touchCenterY >= 0) {//第三象限
            getNextPoint(point, step, paramA, paramB);
        } else {//第四象限
            getNextPoint(point, -step ,-paramA, paramB);
        }


    }

    private BunblePoint getNextPoint(BunblePoint point, float step ,float paramA,float paramB) {

        float x = point.x;
        float y = point.y;
        if(Math.abs(point.x) > Math.abs(getSecondZeroX()*xPercent)){

        }else {
            point.x = x + step;
            point.y = paramB * (float) Math.sin(point.x/paramA);
        }

        return point;
    }


}
