package com.cwd.wandroid.contract;

import com.cwd.wandroid.base.BaseContract;
import com.cwd.wandroid.entity.ArticleInfo;
import com.cwd.wandroid.entity.NavInfo;
import com.cwd.wandroid.entity.NavTitle;

import java.util.List;

public interface NavContract {

    interface View extends BaseContract.View{
        void showNavInfo(List<NavTitle> navTitleList);
    }

    interface Presenter extends BaseContract.Presenter<NavContract.View>{
        void getNavInfo();
    }
}
