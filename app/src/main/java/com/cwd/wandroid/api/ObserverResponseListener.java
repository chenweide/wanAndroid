package com.cwd.wandroid.api;

public interface ObserverResponseListener<T> {
    void onNext(T o);
    void onError(Throwable e);
}
