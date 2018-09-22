package com.cwd.wandroid.presenter;

import com.cwd.wandroid.api.ObserverResponseListener;
import com.cwd.wandroid.base.BaseObserver;
import com.cwd.wandroid.base.BasePresenter;
import com.cwd.wandroid.contract.ArticleContract;
import com.cwd.wandroid.entity.Article;
import com.cwd.wandroid.entity.ArticleInfo;
import com.cwd.wandroid.entity.BaseResponse;
import com.cwd.wandroid.source.DataManager;
import com.cwd.wandroid.utils.LogUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
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
                        LogUtils.d("wade",articleBaseResponse.getErrorCode()+"");
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
    public void detachView() {
        if(!disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
