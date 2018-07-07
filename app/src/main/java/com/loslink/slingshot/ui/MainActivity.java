package com.loslink.slingshot.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
    private TextView tv_history,tv_time,tv_lianji;
    private Button bt_start;
    private long gameTime=600*1000;
    private long oneMaxScore=200;
    private int lianjiCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 移除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
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
        tv_time= (TextView) findViewById(R.id.tv_time);
        tv_lianji= (TextView) findViewById(R.id.tv_lianji);
        bt_start= (Button) findViewById(R.id.bt_start);

        tv_lianji.setVisibility(View.GONE);
        bt_start.setVisibility(View.VISIBLE);
        tv_time.setText((gameTime/1000)+"s");
        tv_score.setText(getResources().getString(R.string.main_score)+score);
        slingShotView.stopGame(true);

        SoundManager.getInstance(MainActivity.this).initSoundPool(new int[]{R.raw.shot,R.raw.bomb});
        slingShotView.setOnShotListenr(new SlingShotView.OnShotListenr() {
            @Override
            public void onStartShot() {
                currStreamId=SoundManager.getInstance(MainActivity.this).playSound(0);
            }

            @Override
            public void onShotSuccess(float shotHuan) {
                lianjiCount++;
                score=score+(long) (oneMaxScore*shotHuan*lianjiCount);
                tv_score.setText(getResources().getString(R.string.main_score)+score);
                startAnimation();
                currStreamId=SoundManager.getInstance(MainActivity.this).playSound(1);
                if(lianjiCount>1){
                    tv_lianji.setVisibility(View.VISIBLE);
                    tv_lianji.setText(lianjiCount+"连击");
                    startLianjiAnimation();
                }
            }

            @Override
            public void onShotLost() {
                lianjiCount=0;
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

        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountTime();
                startButtonAnimation();
                tv_score.setText(getResources().getString(R.string.main_score)+0);
                tv_time.setText((gameTime/1000)+"s");
                score=0;
                slingShotView.stopGame(false);
                currStreamId=SoundManager.getInstance(MainActivity.this).playSound(0);
            }
        });
    }

    private void CountTime() {
        new CountDownTimer(gameTime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                tv_time.setText(((millisUntilFinished)/1000)+"s");
            }

            @Override
            public void onFinish() {
                tv_time.setText("0s");
                bt_start.setVisibility(View.VISIBLE);
                GameHistoryUtil.addData(score,System.currentTimeMillis());
                slingShotView.stopGame(true);
            }
        }.start();
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

    private void startLianjiAnimation(){

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(tv_lianji, "scaleX", 1f, 4f);
        animatorX.setDuration(400);
        animatorX.setRepeatCount(0);
        animatorX.setRepeatMode(REVERSE);
        animatorX.start();

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(tv_lianji, "scaleY", 1f, 4f);
        animatorY.setDuration(400);
        animatorY.setRepeatCount(0);
        animatorY.setRepeatMode(REVERSE);
        animatorY.start();

        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(tv_lianji, "alpha", 1f, 0f);
        animatorAlpha.setDuration(400);
        animatorAlpha.setRepeatCount(0);
        animatorAlpha.setRepeatMode(REVERSE);
        animatorAlpha.start();

        animatorAlpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                tv_lianji.setVisibility(View.GONE);
                tv_lianji.setAlpha(1f);
                tv_lianji.setScaleX(1f);
                tv_lianji.setScaleY(1f);
            }
        });
    }

    private void startButtonAnimation(){

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(bt_start, "scaleX", 1f, 4f);
        animatorX.setDuration(300);
        animatorX.setRepeatCount(0);
        animatorX.setRepeatMode(REVERSE);
        animatorX.start();

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(bt_start, "scaleY", 1f, 4f);
        animatorY.setDuration(300);
        animatorY.setRepeatCount(0);
        animatorY.setRepeatMode(REVERSE);
        animatorY.start();

        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(bt_start, "alpha", 1f, 0f);
        animatorAlpha.setDuration(300);
        animatorAlpha.setRepeatCount(0);
        animatorAlpha.setRepeatMode(REVERSE);
        animatorAlpha.start();

        animatorAlpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                bt_start.setVisibility(View.GONE);
                bt_start.setAlpha(1f);
                bt_start.setScaleX(1f);
                bt_start.setScaleY(1f);
            }
        });
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
