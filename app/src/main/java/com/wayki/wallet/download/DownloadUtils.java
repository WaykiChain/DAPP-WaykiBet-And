package com.wayki.wallet.download;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.wayki.wallet.R;
import com.wayki.wallet.callback.DownloadCallback;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DownloadUtils {
    public static DownloadUtils utils;

    public static DownloadUtils getIntance() {
        if (utils == null) {
            utils = new DownloadUtils();
        }
        return utils;
    }

    ProgressDialog horizontalProgressDialog;

    public void DownLoad(final Context mContext, final String downurl, final DownloadCallback callback) {

        horizontalProgressDialog = new ProgressDialog(mContext);
        horizontalProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        horizontalProgressDialog.setMessage(mContext.getString(R.string.download));
        horizontalProgressDialog.setCancelable(false);
        horizontalProgressDialog.setMax(10000);
        horizontalProgressDialog.show();
        final String fileName="wayki" + System.currentTimeMillis() + ".apk";
        Observable.create(new ObservableOnSubscribe<Progress>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Progress> e) throws Exception {
                OkGo.<File>get(downurl)//
                        .execute(new FileCallback(Environment.getExternalStorageDirectory().getAbsolutePath(),
                                fileName) {
                            @Override
                            public void onSuccess(Response<File> response) {
                                e.onComplete();
                            }

                            @Override
                            public void onError(Response<File> response) {
                                e.onError(response.getException());
                            }

                            @Override
                            public void downloadProgress(Progress progress) {
                                e.onNext(progress);
                            }
                        });
            }
        }).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable disposable) throws Exception {

            }
        }).observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Observer<Progress>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        callback.addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull Progress progress) {
                        horizontalProgressDialog.setProgress((int) (progress.fraction * 10000));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        callback.downLoadFail();
                        horizontalProgressDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        callback.downLoadComplete(fileName);
                        horizontalProgressDialog.dismiss();
                    }
                });


    }
}
