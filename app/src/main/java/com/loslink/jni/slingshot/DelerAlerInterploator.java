package com.loslink.jni.slingshot;

import android.animation.TimeInterpolator;
import android.util.Log;

/**
 * 先减速后加速
 * @author loslink
 * @time 2018/7/2 15:24
 */

public class DelerAlerInterploator implements TimeInterpolator {

    @Override
    public float getInterpolation(float input) {

//        -(sin((x + 1) * 3.14))
        return -(float)(Math.sin((input + 1) * Math.PI));
    }
}
