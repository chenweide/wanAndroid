package com.cwd.wandroid.presenter;

import com.cwd.wandroid.base.BasePresenter;
import com.cwd.wandroid.contract.ArticleContract;
import com.cwd.wandroid.contract.CollectContract;
import com.cwd.wandroid.entity.Article;
import com.cwd.wandroid.entity.ArticleInfo;
import com.cwd.wandroid.entity.Banner;
import com.cwd.wandroid.entity.BaseResponse;
import com.cwd.wandroid.source.DataManager;
import com.cwd.wandroid.utils.LogUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CollectPresenter extends BasePresenter<CollectContract.View> implements CollectContract.Presenter {

    private Disposable disposable;
    private DataManager dataManager;

    public CollectPresenter(DataManager dataManager){
        this.dataManager = dataManager;
    }

    @Override
    public void getCollectList(int page) {
        Observable<BaseResponse<Article>> observable = dataManager.getCollectList(page);
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
                        if(articleInfoList == null){
                            getView().showNoCollectView();
                        }else{
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
    public void collectArticle(int id) {
        Observable<BaseResponse> observable = dataManager.collectArticle(id);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse resp) {
                        if(resp.getErrorCode() == 0){
                            getView().showCollectSuccess();
                        }else{
                            getView().showError(resp.getErrorMsg());
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
    public void cancelCollectArticle(int id,int originId) {
        Observable<BaseResponse> observable = dataManager.cancelCollectArticle(id,originId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse resp) {
                        if(resp.getErrorCode() == 0){
                            getView().showCancelCollectSuccess();
                        }else{
                            getView().showError(resp.getErrorMsg());
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
        if(disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
