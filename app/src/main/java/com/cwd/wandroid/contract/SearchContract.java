package com.cwd.wandroid.contract;

import com.cwd.wandroid.base.BaseContract;
import com.cwd.wandroid.entity.HotKey;

import java.util.List;

public interface SearchContract {

    interface View extends BaseContract.View{
        void showHotKey(List<HotKey> hotKeyList);
    }

    interface Presenter extends BaseContract.Presenter<View>{
        void getHotKey();
    }
}
