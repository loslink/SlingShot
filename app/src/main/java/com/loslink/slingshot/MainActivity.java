package com.loslink.slingshot;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.loslink.slingshot.R;
import com.loslink.slingshot.utils.SoundManager;
import com.loslink.slingshot.widget.SlingShotView;
import com.umeng.analytics.MobclickAgent;

import static android.animation.ValueAnimator.REVERSE;

public class MainActivity extends Activity {

    private final String mPageName = "MainActivity";
    private SlingShotView slingShotView;
    private TextView tv_score;
    private int score=0;
    private int currStreamId;// 当前正播放的streamId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 移除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        slingShotView= (SlingShotView) findViewById(R.id.ssv_main);
        tv_score= (TextView) findViewById(R.id.tv_score);

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

}
