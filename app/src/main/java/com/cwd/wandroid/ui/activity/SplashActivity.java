package com.cwd.wandroid.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.widget.TextView;

import com.cwd.wandroid.R;
import com.cwd.wandroid.app.ActivityCollector;
import com.cwd.wandroid.base.BaseActivity;
import com.cwd.wandroid.ui.widget.SplashLogo;
import com.cwd.wandroid.utils.DensityUtil;

import butterknife.BindView;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.tv_logo)
    TextView tvLogo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void createPresenter() {

    }

    @Override
    public void init() {
        ActivityCollector.getInstance().addActivity(this);
        tvLogo.animate().scaleX(0.5f).scaleY(0.5f).translationY(-DensityUtil.dip2px(this,150)).setStartDelay(1000)
                .setDuration(600)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this,tvLogo,"logo").toBundle());
//                        finish();
                    }
                }).start();
    }
}