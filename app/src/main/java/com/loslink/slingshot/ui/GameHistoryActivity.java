package com.loslink.slingshot.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.loslink.slingshot.R;
import com.loslink.slingshot.adapter.RecyclerViewGameHistoryQuickAdapter;
import com.loslink.slingshot.bean.HistoryBean;
import com.loslink.slingshot.utils.GameHistoryUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GameHistoryActivity extends Activity {

    @Bind(R.id.rv_history)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_news)
    TextView tv_news;
    private RecyclerViewGameHistoryQuickAdapter adapter;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_history_list);
        ButterKnife.bind(this);
        initAdapter();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void initAdapter() {

        List<HistoryBean> list=GameHistoryUtil.getList();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (list.size() == 0) {
            tv_news.setVisibility(View.VISIBLE);
        } else {
            Collections.reverse(list);
            if(adapter==null){
                adapter = new RecyclerViewGameHistoryQuickAdapter(list, GameHistoryActivity.this);
                adapter.openLoadAnimation();
                mRecyclerView.setAdapter(adapter);

            }else{
                adapter.setNewData(list);
            }

        }

    }


}
