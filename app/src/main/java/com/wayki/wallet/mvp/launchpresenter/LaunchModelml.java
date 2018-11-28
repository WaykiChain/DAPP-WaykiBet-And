package com.wayki.wallet.mvp.launchpresenter;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.wayki.wallet.R;
import com.wayki.wallet.bean.CheckUpdateBean;
import com.wayki.wallet.bean.entity.UpGradeEntity;
import com.wayki.wallet.mvp.base.ModelContract;
import com.wayki.wallet.utils.ApiConstants;
import com.wayki.wallet.utils.Stringconstant;
import com.wayki.wallet.utils.UIUtils;
import com.wayki.wallet.utils.network.JsonConvert;
import com.wayki.wallet.utils.network.ObservableBody;

import java.util.UUID;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LaunchModelml implements ModelContract.LaunchMode{
    @Override
    public void checkUpDate(final ModelContract.MvpCallBack listener) {
        CheckUpdateBean updateBean=new CheckUpdateBean();
        updateBean.setAppName("WaykiChain");
        updateBean.setPlatformType("android");
        updateBean.setChannelCode(UIUtils.getChannelData(UIUtils.getContext()));
        updateBean.setVerCode(1);
        updateBean.setVerName(UIUtils.getVersionName(UIUtils.getContext()));

        String uuid=UUID.randomUUID().toString().replace("-", "").toLowerCase();
           OkGo.<UpGradeEntity>post(ApiConstants.upgrade)
                   .headers(Stringconstant.REQUEST_UUID, uuid)
                .upJson(new Gson().toJson(updateBean))
                .converter(new JsonConvert<UpGradeEntity>() {})//
                .adapt(new ObservableBody<UpGradeEntity>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {

                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpGradeEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                         listener.accept(d);
                    }

                    @Override
                    public void onNext(@NonNull UpGradeEntity bean) {
                        listener.success(bean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        listener.error(e);
                    }

                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });



    }
}
