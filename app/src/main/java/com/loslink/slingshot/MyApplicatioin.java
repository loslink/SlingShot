package com.loslink.slingshot;

import android.app.Application;
import com.umeng.commonsdk.UMConfigure;

public class MyApplicatioin extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "5b3d8082b27b0a0f5d000053");
    }



}
