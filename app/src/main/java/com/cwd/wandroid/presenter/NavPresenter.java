package com.cwd.wandroid.presenter;

import com.cwd.wandroid.base.BasePresenter;
import com.cwd.wandroid.contract.NavContract;
import com.cwd.wandroid.entity.BaseResponse;
import com.cwd.wandroid.entity.NavTitle;
import com.cwd.wandroid.source.DataManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NavPresenter extends BasePresenter<NavContract.View> implements NavContract.Presenter {

    private Disposable disposable;
    private DataManager dataManager;

    public NavPresenter(DataManager dataManager){
        this.dataManager = dataManager;
    }

    @Override
    public void getNavInfo() {
        Observable<BaseResponse<List<NavTitle>>> observable = dataManager.getNavInfo();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<List<NavTitle>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse<List<NavTitle>> navResp) {
                        if(navResp.getErrorCode() == 0){
                            getView().showNavInfo(navResp.getData());
                        }else{
                            getView().showError(navResp.getErrorMsg());
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
