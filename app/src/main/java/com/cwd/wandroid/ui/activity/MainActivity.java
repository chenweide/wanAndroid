package com.cwd.wandroid.ui.activity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.cwd.wandroid.R;
import com.cwd.wandroid.base.BaseActivity;
import com.cwd.wandroid.ui.fragment.ArticleFragment;
import com.cwd.wandroid.ui.fragment.ProjectFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    addFragment(fragmentList.get(0));
                    showFragment(0);
                    return true;
                case R.id.navigation_dashboard:
                    addFragment(fragmentList.get(1));
                    showFragment(1);
                    return true;
                case R.id.navigation_notifications:

                    return true;
                default:
                    return false;
            }
        }
    };

    private FragmentManager fm;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void createPresenter() {

    }

    @Override
    public void init() {
        setSupportActionBar(toolbar);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        ColorStateList colorStateList = getResources().getColorStateList(R.color.navigation_menu_item_color);
        navigation.setItemTextColor(colorStateList);
        navigation.setItemIconTintList(colorStateList);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fm = getSupportFragmentManager();
        initFragments();
        addFragment(fragmentList.get(0));
        showFragment(0);
    }

    private void initFragments(){
        fragmentList.add(ArticleFragment.newInstance());
        fragmentList.add(ProjectFragment.newInstance());
    }

    private void addFragment(Fragment fragment){
        if(!fragment.isAdded()){
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }
    }

    private void showFragment(int index){
        for (int i = 0; i < fragmentList.size(); i++) {
            if(index == i){
                fm.beginTransaction().show(fragmentList.get(index)).commit();
            }else{
                fm.beginTransaction().hide(fragmentList.get(i)).commit();
            }
        }
    }

}
