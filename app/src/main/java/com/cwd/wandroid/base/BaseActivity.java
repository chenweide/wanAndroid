package com.cwd.wandroid.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.cwd.wandroid.app.ActivityCollector;
import com.cwd.wandroid.utils.StatusBarUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity implements BaseContract.View {

    private Unbinder unbinder;
    private BaseActivity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //7.0只用NoTitle无法去掉标题栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        StatusBarUtils.StatusBarLightMode(this);
        unbinder = ButterKnife.bind(this);
        mContext = this;
        ActivityCollector.getInstance().addActivity(this);
        createPresenter();
        init();
    }

    /**
     * 获取布局 LayoutId
     * @return
     */
    public abstract int getLayoutId();

    public abstract void createPresenter();
    public abstract void init();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.getInstance().removeActivity(this);
        if(unbinder != null && unbinder != Unbinder.EMPTY){
            unbinder.unbind();
            unbinder = null;
        }
    }

    @Override
    public void showError(String message) {
        Log.e(getClass().getName(), message);
        Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
    }
}
