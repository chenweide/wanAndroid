package com.cwd.wandroid.api;

import com.cwd.wandroid.entity.Article;
import com.cwd.wandroid.entity.ArticleInfo;
import com.cwd.wandroid.entity.Banner;
import com.cwd.wandroid.entity.BaseResponse;
import com.cwd.wandroid.entity.HotKey;
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
     * 获取置顶文章
     * @return
     */
    @GET("/article/top/json")
    Observable<BaseResponse<List<ArticleInfo>>> getTopArticleList();

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

    /**
     *登录
     * @return
     */
    @POST("/user/login")
    Observable<BaseResponse<Login>> login(@Query("username") String username,@Query("password") String password);

    /**
     *注销
     * @return
     */
    @GET("/user/logout/json")
    Observable<BaseResponse> logout();

    /**
     *注册
     * @return
     */
    @POST("/user/register")
    Observable<BaseResponse<Login>> register(@Query("username") String username,@Query("password") String password,@Query("repassword") String repassword);

    /**
     * 获取收藏文章列表
     * @param page
     * @return
     */
    @GET("/lg/collect/list/{page}/json")
    Observable<BaseResponse<Article>> getCollectList(@Path("page") int page);

    /**
     * 收藏文章
     * @param id
     * @return
     */
    @POST("/lg/collect/{id}/json")
    Observable<BaseResponse> collectArticle(@Path("id") int id);

    /**
     * 取消收藏文章
     * @param id
     * @param originId
     * @return
     */
    @POST("/lg/uncollect/{id}/json")
    Observable<BaseResponse> cancelCollectArticle(@Path("id") int id,@Query("originId") int originId);

    /**
     * 搜索热词
     * @return
     */
    @GET("/hotkey/json")
    Observable<BaseResponse<List<HotKey>>> getHotKey();
}
