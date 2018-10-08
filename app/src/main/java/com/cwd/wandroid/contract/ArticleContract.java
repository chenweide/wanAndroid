package com.cwd.wandroid.contract;

import com.cwd.wandroid.base.BaseContract;
import com.cwd.wandroid.entity.ArticleInfo;
import com.cwd.wandroid.entity.Banner;

import java.util.List;

public interface ArticleContract {

    interface View extends BaseContract.View{
        void showArticleList(List<ArticleInfo> articleInfoList,boolean isEnd);
        void showNoSearchResultView();
        void showBanner(List<Banner> banners);
    }

    interface Presenter extends BaseContract.Presenter<View>{
        void getArticleList(int page);
        void getSearchList(int page,String keyword);
        void getBanner();
    }
}
