package com.cwd.wandroid.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.cwd.wandroid.R;
import com.cwd.wandroid.api.ApiService;
import com.cwd.wandroid.api.RetrofitUtils;
import com.cwd.wandroid.base.BaseActivity;
import com.cwd.wandroid.contract.IntegralContract;
import com.cwd.wandroid.entity.MyIntegral;
import com.cwd.wandroid.presenter.CollectPresenter;
import com.cwd.wandroid.presenter.IntegralPresenter;
import com.cwd.wandroid.source.DataManager;
import com.cwd.wandroid.ui.widget.DynamicNumberTextView;
import com.cwd.wandroid.utils.DensityUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class IntegralActivity extends BaseActivity implements IntegralContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.tv_integral)
    DynamicNumberTextView tvIntegral;
    @BindView(R.id.tv_rank)
    DynamicNumberTextView tvRank;
    @BindView(R.id.iv_integral)
    ImageView ivIntegral;
    @BindView(R.id.iv_rank)
    ImageView ivRank;

    private IntegralPresenter integralPresenter;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_myintegral;
    }

    @Override
    public void createPresenter() {
        dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));
        integralPresenter = new IntegralPresenter(dataManager);
        integralPresenter.attachView(this);
    }

    @Override
    public void init() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("我的积分");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        integralPresenter.getMyIntegral();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishAfterTransition();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showError(String message) {
        super.showError(message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        execAnim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        integralPresenter.detachView();
    }

    @Override
    public void showMyIntegral(MyIntegral myIntegral) {
        tvIntegral.setText(String.valueOf(myIntegral.getCoinCount()));
        tvRank.setText(String.valueOf(myIntegral.getRank()));
    }

    private void execAnim() {
        List<View> views = animViewList();

        for (int i = 0; i < views.size(); i++) {
            View view = views.get(i);
            view.setTranslationY(DensityUtil.dip2px(IntegralActivity.this,20));
            view.setAlpha(0);
        }

        for (int i = 0; i < views.size(); i++) {
            final View view = views.get(i);
//            view.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    view.animate().translationY(0).alpha(1).setDuration(500).start();
//                }
//            },100 + (i * 100));
            view.animate().translationY(0).alpha(1).setStartDelay(50 + (i * 100)).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        }
    }

    private List<View> animViewList() {
        List<View> views = new ArrayList<>();
        views.add(ivIntegral);
        views.add(tvIntegral);
        views.add(ivRank);
        views.add(tvRank);
        return views;
    }
}
