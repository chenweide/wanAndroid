package com.cwd.wandroid.api;


/**
 *
 */

public class Api {

    private static final String BASE_URL = "http://www.wanandroid.com";

    private volatile static ApiService apiService;

    public static ApiService getApiService() {
        if (apiService == null) {
            synchronized (Api.class) {
                if (apiService == null) {
                    new Api();
                }
            }
        }
        return apiService;
    }

    private Api() {
//        BaseApi baseApi = new BaseApi();
//        apiService = baseApi.getRetrofit(BASE_URL).create(ApiService.class);
    }
}
