package com.cwd.wandroid.app;

import android.app.Application;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class App extends Application {

    private static App mContext;
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        refWatcher = LeakCanary.install(this);
    }

    public static synchronized App getContext(){
        return mContext;
    }

    public static RefWatcher getRefWatcher(Context context) {
        App application = (App) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }
}
