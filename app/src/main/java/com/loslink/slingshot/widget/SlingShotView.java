package com.loslink.slingshot.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.loslink.slingshot.DelerAlerInterploator;
import com.loslink.slingshot.R;
import com.loslink.slingshot.utils.DipToPx;

public class SlingShotView extends View {

    private Paint mPaint, baselinePaint, rubberPaint, centerPiPaint, stonePiPaint, bombPiPaint;
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
    private int centerPiW = 200;
    BunblePoint bombPoint;
    private float bombScale=0f;
    private ValueAnimator animatorBomb, animatorShot;
    private boolean isShot=false;
    boolean isArride=false;
    private BunblePoint baPoint;
    private Context mContext;
    private float startX,endX,startY,endY;
    private OnShotListenr onShotListenr;
    private boolean isStop=false;;

    public SlingShotView(Context context) {
        this(context, null);
    }


    public SlingShotView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlingShotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
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

        bombPiPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bombPiPaint.setColor(Color.YELLOW);
        bombPiPaint.setStyle(Paint.Style.FILL);
        bombPiPaint.setStrokeCap(Paint.Cap.ROUND);
        bombPiPaint.setStrokeWidth(20);

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
        baPoint=new BunblePoint(0,0);
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

        startX=-canvasWidth/2+DipToPx.dipToPx(mContext,50);
        endX=canvasWidth/2-DipToPx.dipToPx(mContext,50);
        startY=slingShotTopY-DipToPx.dipToPx(mContext,110);
        endY=slingShotTopY-DipToPx.dipToPx(mContext,50);
    }


    @Override
    protected void onDraw(Canvas canvas) {//更新画布

        canvas.translate(canvasWidth / 2, canvasHeight / 2);

//        canvas.drawLine(-canvasWidth/2,0,canvasWidth/2,0,baselinePaint);
//        canvas.drawLine(0,-canvasHeight/2,0,canvasHeight/2,baselinePaint);

        baPoint.radius=0.1f;//缩放值，即屏幕宽度的0.1倍
        float sxTarget = ((float) canvas.getWidth() / circleTarget.getWidth()) * baPoint.radius;

        matrixTarget.reset();
        matrixTarget.setScale(sxTarget, sxTarget);
        matrixTarget.postTranslate(baPoint.x, baPoint.y);
        canvas.drawBitmap(circleTarget, matrixTarget, mPaint);

        matrixSling.reset();
        float sx = ((float) canvas.getWidth() / circleSling.getWidth()) * 0.6f;
        matrixSling.setScale(sx, sx);
        matrixSling.postTranslate(-circleSling.getWidth() * sx / 2, slingShotTopY);
        canvas.drawBitmap(circleSling, matrixSling, mPaint);

        drawRubber(canvas);

        if(isShot){
            drawPoint(canvas);
        }

        if(isArride){
            bombPiPaint.setAlpha((int)(255/bombScale)-(255/5));
            canvas.drawCircle(bombPoint.x, bombPoint.y, stoneRadius*bombScale, bombPiPaint);
        }

    }


    private void drawPoint(Canvas canvas){
        canvas.translate(touchX, touchY);
        canvas.scale(1,-1);
        stonePiPaint.setAlpha(point.alpha);
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



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(isStop){
            return true;
        }
        isArride=false;
        isShot=false;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchX = event.getX() - canvasWidth / 2;
            touchY = event.getY() - canvasHeight / 2;
            getleftCenterPiStartPoint();

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            touchX = event.getX() - canvasWidth / 2;
            touchY = event.getY() - canvasHeight / 2;
//            Log.v("SlingShotView", " touchX:" + touchX + "  touchY:" + touchY);
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
            point.alpha=255;
            calcuParams();
            startShot();
        }
        postInvalidate();
        return true;
    }

    public void stopGame(boolean stop){
        isStop=stop;
    }

    /**
     * 计算公式a、b参数
     */
    private void calcuParams(){
        float b=touchCenterY-slingShotTopY;
        float a=Math.abs(touchCenterX);
        float c=(float) Math.sqrt(a*a+b*b);
        paramA=(a/b)*paramB;

        float d=canvasHeight/2-slingShotTopY;
        paramB =(canvasHeight*0.33f)*(b/d);
    }

    /**
     * 射击动画
     */
    private void startShot(){

        if(onShotListenr!=null){
            onShotListenr.onStartShot();
        }
        animatorShot=ValueAnimator.ofFloat(1,0.3f);
        animatorShot.setDuration(duration);
        animatorShot.setInterpolator(new DelerAlerInterploator());
        animatorShot.setRepeatCount(0);
        animatorShot.setRepeatMode(ValueAnimator.RESTART);
        animatorShot.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                calcuPoints(animation);
                postInvalidate();
            }
        });

        animatorShot.start();

        animatorBomb=ValueAnimator.ofFloat(0.2f,5f);
        animatorBomb.setDuration(300);
        animatorBomb.setInterpolator(new LinearInterpolator());
        animatorBomb.setRepeatCount(0);
        animatorBomb.setRepeatMode(ValueAnimator.RESTART);
        animatorBomb.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bombScale= (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animatorBomb.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Point targetPoint=getTargetPoint();
                        baPoint.x=targetPoint.x;
                        baPoint.y=targetPoint.y;
                        postInvalidate();
                    }
                },200);

            }
        });
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

    /**
     * 弹弓橡胶绘图
     * @param canvas
     */
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
        stonePiPaint.setAlpha(255);
        canvas.drawCircle(touchX, touchY, stoneRadius, stonePiPaint);
    }

    /**
     * 计算连续点算法
     * @param animation
     */
    private void calcuPoints(ValueAnimator animation) {

        point.radius=stoneRadius*(float)animation.getAnimatedValue();
        float step = 5;
        step = Math.abs(getSecondZeroX())/(canvasWidth/9);
        if (touchCenterX >= 0 && touchCenterY <= 0) {//第一象限
        } else if (touchCenterX < 0 && touchCenterY < 0) {//第二象限
        } else if (touchCenterX <= 0 && touchCenterY >= 0) {//第三象限
            getNextPoint(point, step, paramA, paramB);
        } else {//第四象限
            getNextPoint(point, -step ,-paramA, paramB);
        }

    }


    /**
     * 运行的下一点
     * @param point
     * @param step
     * @param paramA
     * @param paramB
     * @return
     */
    private BunblePoint getNextPoint(BunblePoint point, float step ,float paramA,float paramB) {

        float x = point.x;
        float y = point.y;
        if(Math.abs(point.x) > Math.abs(getSecondZeroX()*xPercent)){
            animatorShot.cancel();
            point.alpha=0;
            isArride=true;
            bombPoint=getShotPoint();
            animatorBomb.start();
            if(isShotSuccess()){
                if(onShotListenr!=null){
                    onShotListenr.onShotSuccess();
                }
            }else{
                if(onShotListenr!=null){
                    onShotListenr.onShotLost();
                }
            }
        }else {
            point.x = x + step;
            point.y = paramB * (float) Math.sin(point.x/paramA);
        }

        return point;
    }

    /**
     * 子弹击中点
     * @return
     */
    private BunblePoint getShotPoint(){
        float x = getSecondZeroX()*xPercent;
        float y = paramB * (float) Math.sin(x/paramA);
        if(touchCenterX>=0){
            x=-x;
        }
        return new BunblePoint(x,y);
    }


    /**
     * 靶的位置
     * @return
     */
    private Point getTargetPoint(){
        int targetX=(int)(Math.random()*(endX-startX)+startX);
        int targetY=(int)(Math.random()*(endY-startY)+startY);
        return new Point(targetX,targetY);
    }

    /**
     * 有没有击中靶
     * @return
     */
    private boolean isShotSuccess(){

        float toFirstZuoBiaoY=slingShotTopY-bombPoint.y;
        float baWid=canvasWidth*baPoint.radius;
//        Log.v("isShotSuccess","baWid:" +baWid);
//        Log.v("isShotSuccess", (baPoint.x-stoneRadius/2)+"  bombPoint.x:" + bombPoint.x +"  "+(baPoint.x+baWid+stoneRadius/2));
//        Log.v("isShotSuccess", (baPoint.y-stoneRadius/2)+"  toFirstZuoBiaoY:" +toFirstZuoBiaoY+ "  "+(baPoint.y+baWid+stoneRadius/2) );
        if(bombPoint.x>=baPoint.x-stoneRadius/2
                && bombPoint.x<=baPoint.x+baWid+stoneRadius/2
                && toFirstZuoBiaoY>=baPoint.y-stoneRadius/2
                && toFirstZuoBiaoY<=baPoint.y+baWid+stoneRadius/2){

            return true;
        }
        return false;
    }


    public OnShotListenr getOnShotListenr() {
        return onShotListenr;
    }

    public void setOnShotListenr(OnShotListenr onShotListenr) {
        this.onShotListenr = onShotListenr;
    }

    public interface OnShotListenr{

        void onStartShot();
        void onShotSuccess();
        void onShotLost();
    }

}
