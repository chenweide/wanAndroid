package com.cwd.wandroid.api;

import com.cwd.wandroid.entity.Article;
import com.cwd.wandroid.entity.BaseResponse;
import com.cwd.wandroid.entity.Login;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * api service
 */
public interface ApiService {

    @POST("query")
    Observable<BaseResponse<List<Login>>> login(@QueryMap Map<String, String> map);

    @POST("query")
    Observable<BaseResponse<List<Login>>> logout(@QueryMap Map<String, String> map);

    @GET("/article/list/{page}/json")
    Observable<BaseResponse<Article>> getArticleList(@Path("page") int page);
}
