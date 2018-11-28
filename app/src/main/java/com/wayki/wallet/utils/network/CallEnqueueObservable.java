package com.wayki.wallet.utils.network;

import com.lzy.okgo.adapter.Call;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;

public class CallEnqueueObservable<T> extends Observable<Response<T>> {
    private final Call<T> originalCall;

    public CallEnqueueObservable(Call<T> originalCall) {
        this.originalCall = originalCall;
    }

    @Override
    protected void subscribeActual(Observer<? super Response<T>> observer) {
        // Since Call is a one-shot type, clone it for each new observer.
        Call<T> call = originalCall.clone();
        CallCallback<T> callback = new CallCallback<>(call, observer);
        observer.onSubscribe(callback);
        call.execute(callback);
    }

    private static final class CallCallback<T> implements Disposable, Callback<T> {
        private final Call<T> call;
        private final Observer<? super Response<T>> observer;
        boolean terminated = false;

        CallCallback(Call<T> call, Observer<? super Response<T>> observer) {
            this.call = call;
            this.observer = observer;
        }

        @Override
        public void dispose() {
            call.cancel();
        }

        @Override
        public boolean isDisposed() {
            return call.isCanceled();
        }

        @Override
        public T convertResponse(okhttp3.Response response) throws Throwable {
            // okrx 使用converter转换，不需要这个解析方法
            return null;
        }

        @Override
        public void onStart(Request<T, ? extends Request> request) {
        }

        @Override
        public void onSuccess(Response<T> response) {
            if (call.isCanceled()) return;

            try {
                observer.onNext(response);
            } catch (Exception e) {
                if (terminated) {
                    RxJavaPlugins.onError(e);
                } else {
                    onError(response);
                }
            }
        }

        @Override
        public void onCacheSuccess(Response<T> response) {
            onSuccess(response);
        }

        @Override
        public void onError(Response<T> response) {
            if (call.isCanceled()) return;

            Throwable throwable = response.getException();
            try {
                terminated = true;
                observer.onError(throwable);
            } catch (Throwable inner) {
                Exceptions.throwIfFatal(inner);
                RxJavaPlugins.onError(new CompositeException(throwable, inner));
            }
        }

        @Override
        public void onFinish() {
            if (call.isCanceled()) return;

            try {
                terminated = true;
                observer.onComplete();
            } catch (Throwable inner) {
                Exceptions.throwIfFatal(inner);
                RxJavaPlugins.onError(inner);
            }

        }

        @Override
        public void uploadProgress(Progress progress) {
        }

        @Override
        public void downloadProgress(Progress progress) {
        }
    }
}

