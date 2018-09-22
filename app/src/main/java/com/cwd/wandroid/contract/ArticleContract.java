package com.cwd.wandroid.contract;

import com.cwd.wandroid.base.BaseContract;
import com.cwd.wandroid.entity.ArticleInfo;

import java.util.List;

public interface ArticleContract {

    interface View extends BaseContract.View{
        void showArticleList(List<ArticleInfo> articleInfoList,boolean isEnd);
    }

    interface Presenter extends BaseContract.Presenter<View>{
        void getArticleList(int page);
    }
}
