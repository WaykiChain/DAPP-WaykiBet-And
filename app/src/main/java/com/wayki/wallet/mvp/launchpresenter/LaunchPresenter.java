package com.wayki.wallet.mvp.launchpresenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.wayki.wallet.R;
import com.wayki.wallet.bean.entity.UpGradeEntity;
import com.wayki.wallet.callback.DownloadCallback;
import com.wayki.wallet.download.DownloadUtils;
import com.wayki.wallet.mvp.base.BasePresenter;
import com.wayki.wallet.mvp.base.ModelContract;

import java.io.File;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LaunchPresenter extends BasePresenter<LaunchView> {
    private LaunchView launchView;
    private LaunchModelml model;
    private Context mContext;
    public LaunchPresenter(LaunchView mView,Context context) {
        launchView = mView;
        this.mContext=context;
        model = new LaunchModelml();
        mCompositeSubscription = new CompositeDisposable();
    }

    public void checkUpdate(){
        model.checkUpDate( new ModelContract.MvpCallBack() {
            @Override
            public void success(Object object) {
                UpGradeEntity entity=(UpGradeEntity)object;
                if(entity!=null&&entity.getCode()==0){
                 launchView.getDataSuccess(object);
                }else{

                }
            }

            @Override
            public void error(Object code) {
                launchView.dismissDialog();
                launchView.showTip(mContext.getString(R.string.network_error));
            }

            @Override
            public void accept(@NonNull Disposable d) {
               launchView.loadingDialog("");
            }

            @Override
            public void onSubscribe(Disposable d) {
                mCompositeSubscription.add(d);
            }

            @Override
            public void onComplete() {
                launchView.dismissDialog();
            }
        });
    }

    public void downLoad(String url) {
        DownloadUtils.getIntance().DownLoad(mContext, url, new DownloadCallback() {
            @Override
            public void addDispose(Disposable disposable) {
                mCompositeSubscription.add(disposable);
            }

            ///sdcard/com.olz.wicc1_2.1.31534563687133.apk
            @Override
            public void downLoadComplete(String fileName) {
                File apkFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);
                if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
                    String appid=mContext.getPackageName();
                    Uri apkUri = FileProvider.getUriForFile(mContext,  appid+ ".provider", apkFile);//在AndroidManifest中的android:authorities值
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    mContext.startActivity(install);
                } else {
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(install);
                }
            }

            @Override
            public void downLoadFail() {

            }
        });

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
