package com.cwd.wandroid.presenter;

import com.cwd.wandroid.base.BasePresenter;
import com.cwd.wandroid.contract.ArticleContract;
import com.cwd.wandroid.entity.Article;
import com.cwd.wandroid.entity.ArticleInfo;
import com.cwd.wandroid.entity.Banner;
import com.cwd.wandroid.entity.BaseResponse;
import com.cwd.wandroid.source.DataManager;
import com.cwd.wandroid.utils.LogUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ArticlePresenter extends BasePresenter<ArticleContract.View> implements ArticleContract.Presenter {

    private Disposable disposable;
    private DataManager dataManager;

    public ArticlePresenter(DataManager dataManager){
        this.dataManager = dataManager;
    }

    @Override
    public void getArticleList(int page) {
        Observable<BaseResponse<Article>> observable = dataManager.getArticleList(page);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<Article>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse<Article> articleBaseResponse) {
                        if(articleBaseResponse.getErrorCode() != 0){
                            getView().showError(articleBaseResponse.getErrorMsg());
                            return;
                        }
                        getView().showArticleList(articleBaseResponse.getData().getDatas(),articleBaseResponse.getData().isOver());
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getTopArticleList() {
        Observable<BaseResponse<List<ArticleInfo>>> observable = dataManager.getTopAticleList();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<List<ArticleInfo>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse<List<ArticleInfo>> articleBaseResponse) {
                        if(articleBaseResponse.getErrorCode() != 0){
                            getView().showError(articleBaseResponse.getErrorMsg());
                            return;
                        }
                        getView().showTopArticleList(articleBaseResponse.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getSearchList(int page, String keyword) {
        Observable<BaseResponse<Article>> observable = dataManager.getSearchList(page,keyword);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<Article>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse<Article> articleBaseResponse) {
                        if(articleBaseResponse.getErrorCode() != 0){
                            getView().showError(articleBaseResponse.getErrorMsg());
                            return;
                        }
                        List<ArticleInfo> articleInfoList = articleBaseResponse.getData().getDatas();
                        if(articleInfoList.isEmpty()){
                            getView().showNoSearchResultView();
                        }else{
                            Logger.d(articleInfoList);
                            getView().showArticleList(articleInfoList,articleBaseResponse.getData().isOver());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getBanner() {
        Observable<BaseResponse<List<Banner>>> observable = dataManager.getBanner();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<List<Banner>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse<List<Banner>> resp) {
                        if(resp.getErrorCode() != 0){
                            getView().showError(resp.getErrorMsg());
                            return;
                        }
                        List<Banner> bannerList = resp.getData();
                        if(!bannerList.isEmpty()){
                            getView().showBanner(bannerList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        if(!disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
