package com.cwd.wandroid.presenter;

import com.cwd.wandroid.base.BasePresenter;
import com.cwd.wandroid.contract.ProjectContract;
import com.cwd.wandroid.entity.BaseResponse;
import com.cwd.wandroid.entity.ProjectCategory;
import com.cwd.wandroid.source.DataManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProjectPresenter extends BasePresenter<ProjectContract.View> implements ProjectContract.Presenter {

    private Disposable disposable;
    private DataManager dataManager;

    public ProjectPresenter(DataManager dataManager){
        this.dataManager = dataManager;
    }

    @Override
    public void getProjectCategory() {
        Observable<BaseResponse<List<ProjectCategory>>> observable = dataManager.getProjectCategory();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<List<ProjectCategory>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse<List<ProjectCategory>> categoryResp) {
                        if(categoryResp.getErrorCode() == 0){
                            getView().showProjectCategory(categoryResp.getData());
                        }else{
                            getView().showError(categoryResp.getErrorMsg());
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
