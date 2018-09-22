package com.cwd.wandroid.base;

public interface BaseContract {
    interface View {
        void showError(String message);
    }

    interface Presenter<V extends BaseContract.View> {
        void attachView(V view);
        void detachView();
    }
}
