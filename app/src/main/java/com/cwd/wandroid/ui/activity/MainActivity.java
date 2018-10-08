package com.cwd.wandroid.ui.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.cwd.wandroid.R;
import com.cwd.wandroid.base.BaseActivity;
import com.cwd.wandroid.ui.fragment.ArticleFragment;
import com.cwd.wandroid.ui.fragment.NavFragment;
import com.cwd.wandroid.ui.fragment.ProjectFragment;
import com.cwd.wandroid.ui.fragment.SystemFragment;
import com.cwd.wandroid.utils.BottomNavigationViewHelper;
import com.cwd.wandroid.utils.ToastUtils;

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
                    toolbar.setTitle("Google Android");
                    addFragment(fragmentList.get(0));
                    showFragment(0);
                    return true;
                case R.id.navigation_project:
                    toolbar.setTitle("项目");
                    addFragment(fragmentList.get(1));
                    showFragment(1);
                    return true;
                case R.id.navigation_system:
                    toolbar.setTitle("体系");
                    addFragment(fragmentList.get(2));
                    showFragment(2);
                    return true;
                case R.id.navigation_nav:
                    toolbar.setTitle("导航");
                    addFragment(fragmentList.get(3));
                    showFragment(3);
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
        BottomNavigationViewHelper.disableShiftMode(navigation);
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
        fragmentList.add(SystemFragment.newInstance());
        fragmentList.add(NavFragment.newInstance());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Intent intent = new Intent(mContext,SearchActivity.class);
                mContext.startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
