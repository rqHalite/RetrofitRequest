package com.rock.retrofitrequest.https;

import io.reactivex.subscribers.DefaultSubscriber;

/**
 * 返回成功和失败的回调
 */

public abstract class RxSubscriber<T> extends DefaultSubscriber<T> {

    @Override
    public void onComplete() {
        cancel();
    }

    @Override
    public void onError(Throwable t) {
        String msg=ExceptionHandle.handleException(t);
        t.printStackTrace();
        _onError(msg);
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    public abstract void _onNext(T t);

    public abstract void _onError(String msg);

}
