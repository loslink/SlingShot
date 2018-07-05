package com.loslink.slingshot.utils;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.DisplayMetrics;

import com.loslink.slingshot.R;

/**
 * 声音管理器
 * @author loslink
 * @time 2018/7/5 15:05
 */
public class SoundManager {

    public static SoundManager dataStoreController;
    private SoundPool soundPool;
    private Context context;

    public static SoundManager getInstance(Context context) {
        if (dataStoreController == null) {
            synchronized (SoundManager.class) {
                if (dataStoreController == null) {
                    dataStoreController = new SoundManager(context);
                }
            }
        }
        return dataStoreController;
    }

    private SoundManager(Context context){
        this.context=context;
        initSoundPool();
    }

    // 初始化声音池的方法
    private void initSoundPool() {
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0); // 创建SoundPool对象

    }

    // 播放声音的方法
    public int playSound(int sound) { // 获取AudioManager引用
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        // 获取当前音量
        float streamVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        // 获取系统最大音量
        float streamVolumeMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 计算得到播放音量
        float volume = streamVolumeCurrent / streamVolumeMax;
        // 调用SoundPool的play方法来播放声音文件
        int currStreamId = soundPool.play(sound, volume, volume, 1, 1, 1.0f);
        return currStreamId;
    }

}
