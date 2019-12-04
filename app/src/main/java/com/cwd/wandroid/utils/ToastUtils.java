package com.cwd.wandroid.utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.cwd.wandroid.app.App;


/**
 * Created by drakeet on 9/27/14.
 */
public class ToastUtils {

    private ToastUtils() {
    }


    public static void showShort(int resId) {
        Toast.makeText(App.getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(String message) {
        if(!TextUtils.isEmpty(message)){
            Toast.makeText(App.getContext(), message, Toast.LENGTH_SHORT).show();
        }

    }

    public static void showLong(int resId) {
        Toast.makeText(App.getContext(), resId, Toast.LENGTH_LONG).show();
    }

    public static void showLong(String message) {
        if(!TextUtils.isEmpty(message)){
            Toast.makeText(App.getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

}
