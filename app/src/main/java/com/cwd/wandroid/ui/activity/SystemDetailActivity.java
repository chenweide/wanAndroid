package com.cwd.wandroid.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.cwd.wandroid.R;
import com.cwd.wandroid.adapter.ProjectFragmentAdapter;
import com.cwd.wandroid.adapter.SystemDetailFragmentAdapter;
import com.cwd.wandroid.base.BaseActivity;
import com.cwd.wandroid.entity.ProjectCategory;
import com.cwd.wandroid.entity.System;
import com.cwd.wandroid.entity.SystemDetail;
import com.cwd.wandroid.ui.fragment.ProjectFragment;

import java.util.List;

import butterknife.BindView;

public class SystemDetailActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.vp_project)
    ViewPager vpProject;

    public static final String SYSTEM = "system";
    private System system;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_system_detail;
    }

    @Override
    public void createPresenter() {

    }

    @Override
    public void init() {
        system = (System) getIntent().getSerializableExtra(SYSTEM);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(system.getName());
        initTab(system.getChildren());
        initViewPager(system.getChildren());
        tabLayout.setupWithViewPager(vpProject);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tabLayout != null){
            tabLayout.removeOnTabSelectedListener(tabListener);
        }
    }

    private void addFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        if(!fragment.isAdded()){
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
            fm.beginTransaction().show(fragment).commit();
        }
    }

    private void initTab(List<SystemDetail> systemDetailList){
        for (SystemDetail systemDetail : systemDetailList){
            tabLayout.addTab(tabLayout.newTab().setText(systemDetail.getName()));
        }
        tabLayout.addOnTabSelectedListener(tabListener);
    }

    private void initViewPager(List<SystemDetail> systemDetailList){
        vpProject.setAdapter(new SystemDetailFragmentAdapter(getSupportFragmentManager(),systemDetailList));
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
}
