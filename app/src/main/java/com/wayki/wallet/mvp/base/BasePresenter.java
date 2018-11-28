package com.wayki.wallet.mvp.base;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BasePresenter <V extends BaseView> extends MvpBasePresenter<V> {
    public CompositeDisposable mCompositeSubscription;//线程安全，由所有订阅者组成的组，在页面销毁的时候统一取消所有订阅，防止内存泄露

    @Override
    public void attachView(V view) {
        super.attachView(view);
        mCompositeSubscription = new CompositeDisposable();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (mCompositeSubscription != null) {
            mCompositeSubscription.dispose();
            mCompositeSubscription = null;
        }
    }
}
