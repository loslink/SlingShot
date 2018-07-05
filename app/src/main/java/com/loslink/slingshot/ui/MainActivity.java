package com.loslink.slingshot.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.loslink.slingshot.R;
import com.loslink.slingshot.bean.HistoryBean;
import com.loslink.slingshot.bean.HistoryData;
import com.loslink.slingshot.utils.GameHistoryUtil;
import com.loslink.slingshot.utils.GsonUtils;
import com.loslink.slingshot.utils.PreferenceManager;
import com.loslink.slingshot.utils.SoundManager;
import com.loslink.slingshot.widget.SlingShotView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import static android.animation.ValueAnimator.REVERSE;

public class MainActivity extends Activity {

    private final String mPageName = "MainActivity";
    private SlingShotView slingShotView;
    private TextView tv_score;
    private long score=0;
    private int currStreamId;// 当前正播放的streamId
    private TextView tv_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 移除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
//        GameHistoryUtil.addData(1000,System.currentTimeMillis()-30000);
//        GameHistoryUtil.addData(2000,System.currentTimeMillis());
//        GameHistoryUtil.addData(800,System.currentTimeMillis());
        if(savedInstanceState!=null)
        {
            long score=savedInstanceState.getLong("score");//取得保存的值
            long time=savedInstanceState.getLong("time");
            if(score>0){
                Log.v("onSaveInstanceState：","score"+score);
                GameHistoryUtil.addData(score,time);
            }
        }
        slingShotView= (SlingShotView) findViewById(R.id.ssv_main);
        tv_score= (TextView) findViewById(R.id.tv_score);
        tv_history= (TextView) findViewById(R.id.tv_history);

        tv_score.setText(getResources().getString(R.string.main_score)+score);

        SoundManager.getInstance(MainActivity.this).initSoundPool(new int[]{R.raw.shot,R.raw.bomb});
        slingShotView.setOnShotListenr(new SlingShotView.OnShotListenr() {
            @Override
            public void onStartShot() {
                currStreamId=SoundManager.getInstance(MainActivity.this).playSound(0);
            }

            @Override
            public void onShotSuccess() {
                score=score+10;
                tv_score.setText(getResources().getString(R.string.main_score)+score);
                startAnimation();
                currStreamId=SoundManager.getInstance(MainActivity.this).playSound(1);
            }

            @Override
            public void onShotLost() {
                Toast.makeText(MainActivity.this,getResources().getString(R.string.main_shot_lost),Toast.LENGTH_SHORT).show();
            }
        });

        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        tv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,GameHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startAnimation(){

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(tv_score, "scaleX", 1f, 1.3f,1f);
        animatorX.setDuration(200);
        animatorX.setRepeatCount(0);
        animatorX.setRepeatMode(REVERSE);
        animatorX.start();

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(tv_score, "scaleY", 1f, 1.3f,1f);
        animatorY.setDuration(200);
        animatorY.setRepeatCount(0);
        animatorY.setRepeatMode(REVERSE);
        animatorY.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong("score", score);
        outState.putLong("time", System.currentTimeMillis());
        Log.v("onSaveInstanceState：","success"+outState);
        super.onSaveInstanceState(outState);
    }
}
