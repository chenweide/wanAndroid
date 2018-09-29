package com.cwd.wandroid.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cwd.wandroid.R;
import com.cwd.wandroid.adapter.ArticleAdapter;
import com.cwd.wandroid.adapter.ProjectFragmentAdapter;
import com.cwd.wandroid.api.ApiService;
import com.cwd.wandroid.api.RetrofitUtils;
import com.cwd.wandroid.base.BaseFragment;
import com.cwd.wandroid.contract.ArticleContract;
import com.cwd.wandroid.contract.ProjectContract;
import com.cwd.wandroid.entity.ArticleInfo;
import com.cwd.wandroid.entity.ProjectCategory;
import com.cwd.wandroid.entity.System;
import com.cwd.wandroid.presenter.ArticlePresenter;
import com.cwd.wandroid.presenter.ProjectPresenter;
import com.cwd.wandroid.source.DataManager;
import com.cwd.wandroid.ui.activity.WebViewActivity;
import com.cwd.wandroid.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ProjectFragment extends BaseFragment implements ProjectContract.View {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.vp_project)
    ViewPager vpProject;

    private ProjectPresenter projectPresenter;
    private DataManager dataManager;

    public ProjectFragment() {

    }

    public static ProjectFragment newInstance() {
        return new ProjectFragment();
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
        return R.layout.fragment_project;
    }

    @Override
    public void createPresenter() {
        dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));
        projectPresenter = new ProjectPresenter(dataManager);
        projectPresenter.attachView(this);
    }

    @Override
    public void init() {
        projectPresenter.getProjectCategory();
    }

    private void initTab(List<ProjectCategory> categoryList){
        for (ProjectCategory category : categoryList){
            tabLayout.addTab(tabLayout.newTab().setText(category.getName()));
        }
        tabLayout.addOnTabSelectedListener(tabListener);
    }

    private void initViewPager(List<ProjectCategory> categoryList){
        FragmentActivity activity = getActivity();
        if(activity != null){
            vpProject.setAdapter(new ProjectFragmentAdapter(activity.getSupportFragmentManager(),categoryList));
        }
    }

    private TabLayout.OnTabSelectedListener tabListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            vpProject.setCurrentItem(tab.getPosition(),true);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(tabLayout != null){
            tabLayout.removeOnTabSelectedListener(tabListener);
        }
    }

    @Override
    public void showProjectCategory(List<ProjectCategory> categoryList) {
        initTab(categoryList);
        initViewPager(categoryList);
        tabLayout.setupWithViewPager(vpProject);
    }

}
