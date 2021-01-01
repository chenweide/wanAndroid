package com.cwd.wandroid.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.cwd.wandroid.R;
import com.cwd.wandroid.api.ApiService;
import com.cwd.wandroid.api.RetrofitUtils;
import com.cwd.wandroid.app.ActivityCollector;
import com.cwd.wandroid.base.BaseActivity;
import com.cwd.wandroid.constants.Constants;
import com.cwd.wandroid.contract.LoginContract;
import com.cwd.wandroid.entity.Login;
import com.cwd.wandroid.presenter.LoginPresenter;
import com.cwd.wandroid.source.DataManager;
import com.cwd.wandroid.ui.fragment.ArticleFragment;
import com.cwd.wandroid.ui.fragment.MineFragment;
import com.cwd.wandroid.ui.fragment.NavFragment;
import com.cwd.wandroid.ui.fragment.ProjectFragment;
import com.cwd.wandroid.ui.fragment.SystemFragment;
import com.cwd.wandroid.utils.BottomNavigationViewHelper;
import com.cwd.wandroid.utils.SPUtils;
import com.cwd.wandroid.utils.ToastUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements LoginContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    private ActionMenuView menuView;

    private DataManager dataManager;
    private LoginPresenter loginPresenter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle("悟Android");
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
                case R.id.navigation_mine:
                    toolbar.setTitle("我的");
                    addFragment(fragmentList.get(4));
                    showFragment(4);
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
        dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));
        loginPresenter = new LoginPresenter(dataManager);
        loginPresenter.attachView(this);
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

        try {
            TextView tvLogo = reflectToolbarTitleTextView();
            if (tvLogo != null) {
                tvLogo.setTransitionName("logo");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        String username = (String) SPUtils.get(context, Constants.USERNAME,"");
        String password = (String) SPUtils.get(context, Constants.PASSWORD,"");
        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
            loginPresenter.login(username,password);
        }
    }

    private TextView reflectToolbarTitleTextView() throws NoSuchFieldException, IllegalAccessException {
        if(toolbar != null){
            Class<? extends Toolbar> aClass = toolbar.getClass();
            Field mTitleTextView = aClass.getDeclaredField("mTitleTextView");
            mTitleTextView.setAccessible(true);
            TextView tvTitle = (TextView) mTitleTextView.get(toolbar);
            return tvTitle;
        }
        return null;
    }

    private void initFragments(){
        fragmentList.add(ArticleFragment.newInstance());
        fragmentList.add(ProjectFragment.newInstance());
        fragmentList.add(SystemFragment.newInstance());
        fragmentList.add(NavFragment.newInstance());
        fragmentList.add(MineFragment.newInstance());
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
                try {
                    menuView = reflectToolbarMenuView();
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }

//                Intent intent = new Intent(context,SearchActivity.class);
//                context.startActivity(intent);
                if(menuView != null){
                    Intent intent = new Intent(this,SearchActivity.class);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this,menuView,"search").toBundle());
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private ActionMenuView reflectToolbarMenuView() throws NoSuchFieldException, IllegalAccessException {
        if(toolbar != null){
            Class<? extends Toolbar> aClass = toolbar.getClass();
            Field mTitleTextView = aClass.getDeclaredField("mMenuView");
            mTitleTextView.setAccessible(true);
            return (ActionMenuView) mTitleTextView.get(toolbar);
        }
        return null;
    }

    @Override
    public void showUsername(Login login) {

    }

    @Override
    public void logoutSuccess() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCollector.getInstance().exitApp();
    }
}
