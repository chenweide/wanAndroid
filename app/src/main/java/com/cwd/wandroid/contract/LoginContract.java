package com.cwd.wandroid.contract;

import com.cwd.wandroid.base.BaseContract;
import com.cwd.wandroid.entity.Login;
import com.cwd.wandroid.entity.System;

import java.util.List;

public interface LoginContract {

    interface View extends BaseContract.View{
        void showUsername(Login login);
        void logoutSuccess();
    }

    interface Presenter extends BaseContract.Presenter<View>{
        void login(String username,String password);
        void register(String username,String password,String repassword);
        void logout();
    }
}
