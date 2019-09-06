package com.cwd.wandroid.presenter;

import com.cwd.wandroid.base.BasePresenter;
import com.cwd.wandroid.contract.NavContract;
import com.cwd.wandroid.contract.SearchContract;
import com.cwd.wandroid.entity.BaseResponse;
import com.cwd.wandroid.entity.HotKey;
import com.cwd.wandroid.entity.NavTitle;
import com.cwd.wandroid.source.DataManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenter extends BasePresenter<SearchContract.View> implements SearchContract.Presenter {

    private Disposable disposable;
    private DataManager dataManager;

    public SearchPresenter(DataManager dataManager){
        this.dataManager = dataManager;
    }

    @Override
    public void getHotKey() {
        Observable<BaseResponse<List<HotKey>>> observable = dataManager.getHotKey();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<List<HotKey>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse<List<HotKey>> resp) {
                        if(resp.getErrorCode() == 0){
                            getView().showHotKey(resp.getData());
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
        if(!disposable.isDisposed()){
            disposable.dispose();
        }
    }

}
