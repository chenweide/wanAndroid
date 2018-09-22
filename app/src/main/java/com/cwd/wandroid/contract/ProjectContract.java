package com.cwd.wandroid.contract;

import com.cwd.wandroid.base.BaseContract;
import com.cwd.wandroid.entity.ArticleInfo;
import com.cwd.wandroid.entity.ProjectCategory;

import java.util.List;

public interface ProjectContract {

    interface View extends BaseContract.View{
        void showProjectCategory(List<ProjectCategory> categoryList);
    }

    interface Presenter extends BaseContract.Presenter<View>{
        void getProjectCategory();
    }
}
