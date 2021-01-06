package com.cwd.wandroid.presenter;

import com.cwd.wandroid.base.BasePresenter;
import com.cwd.wandroid.contract.CollectContract;
import com.cwd.wandroid.contract.IntegralContract;
import com.cwd.wandroid.entity.Article;
import com.cwd.wandroid.entity.ArticleInfo;
import com.cwd.wandroid.entity.BaseResponse;
import com.cwd.wandroid.entity.MyIntegral;
import com.cwd.wandroid.source.DataManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class IntegralPresenter extends BasePresenter<IntegralContract.View> implements IntegralContract.Presenter {

    private Disposable disposable;
    private DataManager dataManager;

    public IntegralPresenter(DataManager dataManager){
        this.dataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        if(disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }

    @Override
    public void getMyIntegral() {
        Observable<BaseResponse<MyIntegral>> observable = dataManager.getMyIntegral();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<MyIntegral>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse<MyIntegral> myIntegralBaseResponse) {
                        if(myIntegralBaseResponse.getErrorCode() != 0){
                            getView().showError(myIntegralBaseResponse.getErrorMsg());
                            return;
                        }
                        MyIntegral myIntegral = myIntegralBaseResponse.getData();
                        if (myIntegral != null) {
                            getView().showMyIntegral(myIntegral);
                        } else {
                            getView().showError("没有积分信息");
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
}
