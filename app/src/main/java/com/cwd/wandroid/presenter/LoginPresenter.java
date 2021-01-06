package com.cwd.wandroid.presenter;

import com.cwd.wandroid.app.App;
import com.cwd.wandroid.base.BasePresenter;
import com.cwd.wandroid.constants.Constants;
import com.cwd.wandroid.contract.LoginContract;
import com.cwd.wandroid.contract.SystemContract;
import com.cwd.wandroid.entity.BaseResponse;
import com.cwd.wandroid.entity.Login;
import com.cwd.wandroid.entity.System;
import com.cwd.wandroid.source.DataManager;
import com.cwd.wandroid.utils.SPUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private Disposable disposable;
    private DataManager dataManager;

    public LoginPresenter(DataManager dataManager){
        this.dataManager = dataManager;
    }

    @Override
    public void login(final String username, final String password) {
        Observable<BaseResponse<Login>> observable = dataManager.login(username,password);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<Login>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse<Login> resp) {
                        if(resp.getErrorCode() == 0){
                            getView().showUsername(resp.getData());
                            //保存用户名和密码，以便下次自动登录
                            SPUtils.put(App.getContext(), Constants.USERNAME,username);
                            SPUtils.put(App.getContext(), Constants.PASSWORD,password);
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
    public void register(final String username, final String password, String repassword) {
        Observable<BaseResponse<Login>> observable = dataManager.register(username,password,repassword);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<Login>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse<Login> resp) {
                        if(resp.getErrorCode() == 0){
                            getView().showUsername(resp.getData());
                            //保存用户名和密码，以便下次自动登录
                            SPUtils.put(App.getContext(), Constants.USERNAME,username);
                            SPUtils.put(App.getContext(), Constants.PASSWORD,password);
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
    public void logout() {
        Observable<BaseResponse> observable = dataManager.logout();
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
                            getView().logoutSuccess();
                            //清除缓存的用户名和密码
                            SPUtils.remove(App.getContext(),Constants.USERNAME);
                            SPUtils.remove(App.getContext(),Constants.PASSWORD);
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
