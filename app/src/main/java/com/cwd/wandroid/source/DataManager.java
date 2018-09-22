package com.cwd.wandroid.source;


import com.cwd.wandroid.api.ApiService;
import com.cwd.wandroid.entity.Article;
import com.cwd.wandroid.entity.BaseResponse;
import com.cwd.wandroid.entity.ProjectCategory;

import java.util.List;

import io.reactivex.Observable;

public class DataManager {

    private ApiService apiService;

    public DataManager(ApiService apiService){
        this.apiService = apiService;
    }

    public Observable<BaseResponse<Article>> getArticleList(int page){
        return apiService.getArticleList(page);
    }

    public Observable<BaseResponse<List<ProjectCategory>>> getProjectCategory(){
        return apiService.getProjectCategory();
    }

    public Observable<BaseResponse<Article>> getProjectList(int page,int cid){
        return apiService.getProjectList(page,cid);
    }
}
