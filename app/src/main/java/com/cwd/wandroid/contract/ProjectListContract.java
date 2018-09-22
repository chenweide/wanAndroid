package com.cwd.wandroid.contract;

import com.cwd.wandroid.base.BaseContract;
import com.cwd.wandroid.entity.ArticleInfo;
import com.cwd.wandroid.entity.ProjectCategory;

import java.util.List;

public interface ProjectListContract {

    interface View extends BaseContract.View{
        void showProjectList(List<ArticleInfo> projectList,boolean isEnd);
    }

    interface Presenter extends BaseContract.Presenter<View>{
        void getProjectList(int page,int cid);
    }
}
