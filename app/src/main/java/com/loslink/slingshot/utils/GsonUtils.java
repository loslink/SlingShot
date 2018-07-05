package com.loslink.slingshot.utils;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2018/7/5.
 */

public class GsonUtils {

    public static <T> T getObjFromJson(String value, Class<T> clazz) {
        T result = new Gson().fromJson(value, clazz);
        return  result;
    }

    public static String getJsonFromObj(Object obj) {
        String result = new Gson().toJson(obj);
        return  result;
    }

}
