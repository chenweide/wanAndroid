package com.cwd.wandroid.contract;

import com.cwd.wandroid.base.BaseContract;
import com.cwd.wandroid.entity.ArticleInfo;
import com.cwd.wandroid.entity.MyIntegral;

import java.util.List;

public interface IntegralContract {

    interface View extends BaseContract.View{
        void showMyIntegral(MyIntegral myIntegral);
    }

    interface Presenter extends BaseContract.Presenter<View>{
        void getMyIntegral();
    }
}
