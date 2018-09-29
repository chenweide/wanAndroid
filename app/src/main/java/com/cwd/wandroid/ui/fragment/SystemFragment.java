package com.cwd.wandroid.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cwd.wandroid.R;
import com.cwd.wandroid.adapter.SystemAdapter;
import com.cwd.wandroid.api.ApiService;
import com.cwd.wandroid.api.RetrofitUtils;
import com.cwd.wandroid.base.BaseFragment;
import com.cwd.wandroid.contract.SystemContract;
import com.cwd.wandroid.entity.System;
import com.cwd.wandroid.presenter.SystemPresenter;
import com.cwd.wandroid.source.DataManager;
import com.cwd.wandroid.ui.activity.SystemDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SystemFragment extends BaseFragment implements SystemContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_system)
    RecyclerView rvSystem;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;


    private boolean isRefresh;

    private SystemPresenter systemPresenter;
    private DataManager dataManager;
    private SystemAdapter systemAdapter;
    private List<System> systemList = new ArrayList<>();

    public SystemFragment() {

    }

    public static SystemFragment newInstance() {
        return new SystemFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_system;
    }

    @Override
    public void createPresenter() {
        dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));
        systemPresenter = new SystemPresenter(dataManager);
        systemPresenter.attachView(this);
    }

    @Override
    public void init() {
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setOnRefreshListener(this);
        rvSystem.setLayoutManager(new LinearLayoutManager(getContext()));
        systemAdapter = new SystemAdapter(R.layout.item_system, systemList);
        systemAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        systemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), SystemDetailActivity.class);
                intent.putExtra(SystemDetailActivity.SYSTEM, systemList.get(position));
                startActivity(intent);
            }
        });
        rvSystem.setAdapter(systemAdapter);
        refreshLayout.setRefreshing(true);
        systemPresenter.getSystemList();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void showError(String message) {
        super.showError(message);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        isRefresh = true;
        systemPresenter.getSystemList();
    }

    @Override
    public void showSystemList(List<System> systemList) {
        refreshLayout.setRefreshing(false);
        if(isRefresh){
            this.systemList.clear();
        }else{
            systemAdapter.loadMoreComplete();
        }
        this.systemList.addAll(systemList);
        systemAdapter.notifyDataSetChanged();
    }

}
