package com.loslink.slingshot.adapter;

import android.app.Activity;

import com.loslink.slingshot.R;
import com.loslink.slingshot.base.BaseViewHolder;
import com.loslink.slingshot.bean.HistoryBean;

import java.util.List;


public class RecyclerViewGameHistoryQuickAdapter extends BaseQuickAdapter<HistoryBean> {

    private Activity context;
    private List<HistoryBean> list;

    public RecyclerViewGameHistoryQuickAdapter(List<HistoryBean> list, Activity context) {
        super(R.layout.item_history_game, list);
        this.context = context;
        this.list = list;
    }

    @Override
    protected void convert(final BaseViewHolder helper, HistoryBean item) {
//        helper.setText(R.id.tv_road, travelMainAddrs);
    }

    

}
