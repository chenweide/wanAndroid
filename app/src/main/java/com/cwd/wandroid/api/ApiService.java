package com.cwd.wandroid.api;

import com.cwd.wandroid.entity.Article;
import com.cwd.wandroid.entity.Banner;
import com.cwd.wandroid.entity.BaseResponse;
import com.cwd.wandroid.entity.Login;
import com.cwd.wandroid.entity.NavTitle;
import com.cwd.wandroid.entity.ProjectCategory;
import com.cwd.wandroid.entity.System;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * api service
 */
public interface ApiService {

    @POST("query")
    Observable<BaseResponse<List<Login>>> login(@QueryMap Map<String, String> map);

    @POST("query")
    Observable<BaseResponse<List<Login>>> logout(@QueryMap Map<String, String> map);

    /**
     * 获取首页文章列表
     * @param page
     * @return
     */
    @GET("/article/list/{page}/json")
    Observable<BaseResponse<Article>> getArticleList(@Path("page") int page);

    /**
     * 项目分类
     * @return
     */
    @GET("/project/tree/json")
    Observable<BaseResponse<List<ProjectCategory>>> getProjectCategory();

    /**
     * 项目列表数据
     * @return
     */
    @GET("/project/list/{page}/json")
    Observable<BaseResponse<Article>> getProjectList(@Path("page") int page,@Query("cid") int cid);

    /**
     * 体系数据
     * @return
     */
    @GET("/tree/json")
    Observable<BaseResponse<List<System>>> getSystemInfo();

    /**
     * 获取体系下的文章
     * @param page
     * @param cid
     * @return
     */
    @GET("/article/list/{page}/json")
    Observable<BaseResponse<Article>> getSystemArticleList(@Path("page") int page,@Query("cid") int cid);

    /**
     * 导航数据
     * @return
     */
    @GET("/navi/json")
    Observable<BaseResponse<List<NavTitle>>> getNavInfo();

    /**
     * 搜索
     * @param page
     * @param keyword
     * @return
     */
    @POST("/article/query/{page}/json")
    Observable<BaseResponse<Article>> getSearchList(@Path("page") int page,@Query("k") String keyword);

    /**
     * banner
     * @return
     */
    @GET("/banner/json")
    Observable<BaseResponse<List<Banner>>> getBanner();
}
