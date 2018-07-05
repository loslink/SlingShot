/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.loslink.slingshot.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    /**
     * 保存Preference的name
     */
    public static final String PREFERENCE_NAME = "saveInfo";
    private static SharedPreferences mSharedPreferences;
    private static PreferenceManager mPreferencemManager;
    private static SharedPreferences.Editor editor;
    private static String GAME_HISTORY = "GAME_HISTORY";

    @SuppressLint("CommitPrefEdits")
    private PreferenceManager(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public static synchronized void init(Context cxt) {
        if (mPreferencemManager == null) {
            mPreferencemManager = new PreferenceManager(cxt);
        }
    }

    /**
     * 单例模式，获取instance实例
     * @return
     */
    public synchronized static PreferenceManager getInstance() {
        if (mPreferencemManager == null) {
            throw new RuntimeException("please init first!");
        }
        return mPreferencemManager;
    }

    public void setHistoryList(String value){
        editor.putString(GAME_HISTORY, value);
        editor.commit();
    }

    public String getHistoryList(){
        return mSharedPreferences.getString(GAME_HISTORY,null);
    }


}
