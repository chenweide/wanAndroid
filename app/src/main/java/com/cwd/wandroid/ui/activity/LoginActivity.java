package com.cwd.wandroid.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.cwd.wandroid.R;
import com.cwd.wandroid.api.ApiService;
import com.cwd.wandroid.api.RetrofitUtils;
import com.cwd.wandroid.base.BaseActivity;
import com.cwd.wandroid.presenter.ArticlePresenter;
import com.cwd.wandroid.source.DataManager;


import butterknife.BindView;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;


    private ArticlePresenter articlePresenter;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void createPresenter() {
        dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));

    }

    @Override
    public void init() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("登录");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
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


}
