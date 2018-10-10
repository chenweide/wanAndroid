package com.cwd.wandroid.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cwd.wandroid.R;
import com.cwd.wandroid.adapter.NavAdapter;
import com.cwd.wandroid.api.ApiService;
import com.cwd.wandroid.api.RetrofitUtils;
import com.cwd.wandroid.base.BaseFragment;
import com.cwd.wandroid.constants.Constants;
import com.cwd.wandroid.contract.LoginContract;
import com.cwd.wandroid.contract.NavContract;
import com.cwd.wandroid.entity.Login;
import com.cwd.wandroid.presenter.LoginPresenter;
import com.cwd.wandroid.source.DataManager;
import com.cwd.wandroid.ui.activity.CollectActivity;
import com.cwd.wandroid.utils.SPUtils;


import butterknife.BindView;
import butterknife.OnClick;

public class MineFragment extends BaseFragment implements LoginContract.View{

    @BindView(R.id.ll_user)
    LinearLayout llUser;
    @BindView(R.id.ll_collect)
    LinearLayout llCollect;
    @BindView(R.id.tv_username)
    TextView tvUsername;

    private AlertDialog loginDialog;

    private DataManager dataManager;
    private LoginPresenter loginPresenter;

    public MineFragment() {

    }

    public static MineFragment newInstance() {
        return new MineFragment();
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
        return R.layout.fragment_mine;
    }

    @Override
    public void createPresenter() {
        dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));
        loginPresenter = new LoginPresenter(dataManager);
        loginPresenter.attachView(this);
    }

    @Override
    public void init() {

    }

    @Override
    public void onResume() {
        super.onResume();
        String username = (String) SPUtils.get(context, Constants.USERNAME,"");
        String password = (String) SPUtils.get(context, Constants.PASSWORD,"");
        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
            tvUsername.setText(username);
        }
    }

    @OnClick(R.id.ll_user)
    public void userClick(){
        String username = (String) SPUtils.get(context, Constants.USERNAME,"");
        String password = (String) SPUtils.get(context, Constants.PASSWORD,"");
        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
            showLogoutDialog();
        }else{
            showLoginDialog();
        }
    }

    @OnClick(R.id.ll_collect)
    public void collectClick(){
        Intent intent = new Intent(context, CollectActivity.class);
        startActivity(intent);
    }

    private void showLogoutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("注销")
                .setMessage("确认退出登录吗？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loginPresenter.logout();
                    }
                });
        builder.create().show();
    }

    private void showLoginDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("登录");
        View view = LayoutInflater.from(context).inflate(R.layout.login_layout,null);
        final TextInputLayout tUsername = view.findViewById(R.id.til_username);
        final TextInputLayout tPassword = view.findViewById(R.id.til_password);
        final TextInputEditText etUsername = view.findViewById(R.id.et_username);
        final TextInputEditText etPassword = view.findViewById(R.id.et_password);
        TextView tvRegister = view.findViewById(R.id.tv_register);
        Button bnLogin = view.findViewById(R.id.bn_login);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if(isValidate(tUsername,tPassword,username,password)){
                    loginPresenter.register(username,password,password);
                }
            }
        });
        bnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if(isValidate(tUsername,tPassword,username,password)){
                    loginPresenter.login(username,password);
                }
            }
        });
        builder.setView(view);
        loginDialog = builder.create();
        loginDialog.show();
    }

    private boolean isValidate(TextInputLayout tUsername,TextInputLayout tPassword,String username,String password){
        boolean flag = true;
        if(TextUtils.isEmpty(username)){
            tUsername.setError("请输入用户名");
            flag = false;
        }
        if(TextUtils.isEmpty(password)){
            tPassword.setError("请输入密码");
            flag = false;
        }
        return flag;
    }

    @Override
    public void showUsername(Login login) {
        if(login != null){
            tvUsername.setText(login.getUsername());
            if(loginDialog != null && loginDialog.isShowing()){
                loginDialog.dismiss();
            }
        }
    }

    @Override
    public void logoutSuccess() {
        tvUsername.setText("未登录");
    }
}
