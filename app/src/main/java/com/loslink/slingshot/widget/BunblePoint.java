package com.loslink.slingshot.widget;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * @author loslink
 * @time 2018/6/28 15:45
 */

public class BunblePoint {

    public float x;
    public float y;
    public int alpha;
    public float aParam;
    public float bParam;
    public Bitmap logo;
    public float radius;
    public Matrix matrix;
    public float degree;

    public BunblePoint(){
    }

    public BunblePoint(float x,float y){
        this.x=x;
        this.y=y;
    }

}
