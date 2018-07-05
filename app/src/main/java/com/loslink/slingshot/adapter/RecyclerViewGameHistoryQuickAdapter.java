package com.loslink.slingshot.adapter;

import android.app.Activity;
import android.text.TextUtils;

import com.loslink.slingshot.R;
import com.loslink.slingshot.base.BaseViewHolder;
import com.loslink.slingshot.bean.HistoryData;

import java.util.List;


public class RecyclerViewGameHistoryQuickAdapter extends BaseQuickAdapter<HistoryData> {

    private Activity context;
    private List<HistoryData> list;

    public RecyclerViewGameHistoryQuickAdapter(List<HistoryData> list, Activity context) {
        super(R.layout.item_history_game, list);
        this.context = context;
        this.list = list;
    }

    @Override
    protected void convert(final BaseViewHolder helper, HistoryData item) {
//        helper.setText(R.id.tv_road, travelMainAddrs);
    }

    

}
