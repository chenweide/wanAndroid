package com.cwd.wandroid.source;


import com.cwd.wandroid.api.ApiService;
import com.cwd.wandroid.entity.Article;
import com.cwd.wandroid.entity.Banner;
import com.cwd.wandroid.entity.BaseResponse;
import com.cwd.wandroid.entity.NavTitle;
import com.cwd.wandroid.entity.ProjectCategory;
import com.cwd.wandroid.entity.System;

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

    public Observable<BaseResponse<List<System>>> getSystemInfo(){
        return apiService.getSystemInfo();
    }

    public Observable<BaseResponse<Article>> getSystemArticleList(int page,int cid){
        return apiService.getSystemArticleList(page,cid);
    }

    public Observable<BaseResponse<List<NavTitle>>> getNavInfo(){
        return apiService.getNavInfo();
    }

    public Observable<BaseResponse<Article>> getSearchList(int page,String keyword){
        return apiService.getSearchList(page,keyword);
    }

    public Observable<BaseResponse<List<Banner>>> getBanner(){
        return apiService.getBanner();
    }
}
