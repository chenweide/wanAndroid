package com.cwd.wandroid.source;


import com.cwd.wandroid.api.ApiService;
import com.cwd.wandroid.entity.Article;
import com.cwd.wandroid.entity.BaseResponse;

import io.reactivex.Observable;

public class DataManager {

    private ApiService apiService;

    public DataManager(ApiService apiService){
        this.apiService = apiService;
    }

    public Observable<BaseResponse<Article>> getArticleList(int page){
        return apiService.getArticleList(page);
    }
}
