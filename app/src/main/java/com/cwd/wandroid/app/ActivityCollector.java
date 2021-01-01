package com.cwd.wandroid.app;

import android.app.Activity;

import com.cwd.wandroid.base.BaseActivity;

import java.util.HashSet;
import java.util.Set;

public class ActivityCollector {

    private static ActivityCollector activityCollector;

    public static ActivityCollector getInstance() {
        if(activityCollector == null){
            synchronized (ActivityCollector.class){
                if(activityCollector == null){
                    activityCollector = new ActivityCollector();
                }
            }
        }
        return activityCollector;
    }

    private Set<Activity> allActivities;

    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    public void finishActivity(Class<? extends BaseActivity> c) {
        for (Activity activity : allActivities) {
            Class<? extends Activity> aClass = activity.getClass();
            if(aClass == c) {
                activity.finish();
            }
        }
    }

    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

}
