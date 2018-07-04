package com.loslink.jni.slingshot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.loslink.jni.slingshot.widget.SlingShotView;

import static android.animation.ValueAnimator.INFINITE;
import static android.animation.ValueAnimator.REVERSE;

public class MainActivity extends Activity {

    private SlingShotView slingShotView;
    private TextView tv_score;
    private int score=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 移除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        slingShotView= (SlingShotView) findViewById(R.id.ssv_main);
        tv_score= (TextView) findViewById(R.id.tv_score);

        tv_score.setText("Score:"+score);
        slingShotView.setOnShotListenr(new SlingShotView.OnShotListenr() {
            @Override
            public void onShotSuccess() {
                score=score+10;
                tv_score.setText("Score:"+score);
                startAnimation();
//                Toast.makeText(MainActivity.this,"fine shot success!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onShotLost() {
                Toast.makeText(MainActivity.this,"shot lost!",Toast.LENGTH_SHORT).show();
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

}
