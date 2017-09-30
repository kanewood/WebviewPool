package com.kanewood.myapplication;

import android.app.Application;

/**
 * Created by Admin on 2017/9/30.
 */

public class BaseApplication extends Application {

    private static BaseApplication baseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        WebViewPool.getInstance().init();
    }

    /**
     * 获取application对象
     *
     * @return
     */
    public static BaseApplication getInstance() {
        return baseApplication;
    }
}
