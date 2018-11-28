package com.wayki.wallet.callback;

import io.reactivex.disposables.Disposable;

public interface DownloadCallback {
    void addDispose(Disposable disposable);
    void downLoadComplete(String fileName);
    void downLoadFail();
}
