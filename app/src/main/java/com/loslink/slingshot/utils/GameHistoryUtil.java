package com.loslink.slingshot.utils;

import android.text.TextUtils;
import android.util.Log;

import com.loslink.slingshot.bean.HistoryBean;
import com.loslink.slingshot.bean.HistoryData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/5.
 */

public class GameHistoryUtil {

    public static void addData(long score, long time){
        String hisString=PreferenceManager.getInstance().getHistoryList();
        HistoryData historyData = null;
        if(!TextUtils.isEmpty(hisString)){
            historyData=GsonUtils.getObjFromJson(hisString,HistoryData.class);
        }
        if(historyData!=null && historyData.getList()!=null){
            HistoryBean bean=new HistoryBean();
            bean.setScore(score);
            bean.setTime(System.currentTimeMillis());
            historyData.getList().add(bean);
        }else {
            historyData=new HistoryData();
            historyData.setList(new ArrayList<HistoryBean>());
            HistoryBean bean=new HistoryBean();
            bean.setScore(score);
            bean.setTime(time);
            historyData.getList().add(bean);
        }
        String value=GsonUtils.getJsonFromObj(historyData);
        PreferenceManager.getInstance().setHistoryList(value);
    }

    public static List<HistoryBean> getList(){
        String hisString=PreferenceManager.getInstance().getHistoryList();
        HistoryData historyData = new HistoryData();
        if(!TextUtils.isEmpty(hisString)){
            historyData=GsonUtils.getObjFromJson(hisString,HistoryData.class);
        }else {
            historyData.setList(new ArrayList<HistoryBean>());
        }
        return historyData.getList();
    }
}
