package com.cwd.wandroid.contract;

import com.cwd.wandroid.base.BaseContract;
import com.cwd.wandroid.entity.ProjectCategory;
import com.cwd.wandroid.entity.System;

import java.util.List;

public interface SystemContract {

    interface View extends BaseContract.View{
        void showSystemList(List<System> systemList);
    }

    interface Presenter extends BaseContract.Presenter<View>{
        void getSystemList();
    }
}
