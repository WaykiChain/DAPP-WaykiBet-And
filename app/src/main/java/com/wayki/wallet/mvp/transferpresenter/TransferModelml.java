package com.wayki.wallet.mvp.transferpresenter;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.wayki.wallet.application.App;
import com.wayki.wallet.bean.TransferBean;
import com.wayki.wallet.bean.entity.AccountInfoEntity;
import com.wayki.wallet.bean.entity.BlockHeightEntity;
import com.wayki.wallet.bean.entity.TransactionEntity;
import com.wayki.wallet.mvp.base.ModelContract;
import com.wayki.wallet.utils.ApiConstants;
import com.wayki.wallet.utils.Stringconstant;
import com.wayki.wallet.utils.network.JsonConvert;
import com.wayki.wallet.utils.network.ObservableBody;

import java.util.UUID;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TransferModelml  implements ModelContract.TransferMode {

    @Override
    public void transfer(TransferBean transferBean,final ModelContract.MvpCallBack listener) {
        String uuid= UUID.randomUUID().toString().replace("-", "").toLowerCase();
        OkGo.<TransactionEntity>post(ApiConstants.transaction_wicc)
                .headers(Stringconstant.ACCESS_TOKEN, App.getApplication().getToken())
                .headers(Stringconstant.REQUEST_UUID, uuid)
                .upJson(new Gson().toJson(transferBean))
                .converter(new JsonConvert<TransactionEntity>() {})//
                .adapt(new ObservableBody<TransactionEntity>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        listener.accept(disposable);
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TransactionEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        listener.accept(disposable);
                    }

                    @Override
                    public void onNext(@NonNull TransactionEntity bean) {
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

    @Override
    public void accountInfo(final ModelContract.MvpCallBack listener) {
        String uuid=UUID.randomUUID().toString().replace("-", "").toLowerCase();
        OkGo.<AccountInfoEntity>get(ApiConstants.getaccountinfo)
                .headers(Stringconstant.ACCESS_TOKEN, App.getApplication().getToken())
                .headers(Stringconstant.REQUEST_UUID, uuid)
                .converter(new JsonConvert<AccountInfoEntity>() {})//
                .adapt(new ObservableBody<AccountInfoEntity>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        listener.accept(disposable);
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AccountInfoEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull AccountInfoEntity bean) {
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

    @Override
    public void getblockNumber(final ModelContract.MvpCallBack listener) {
        String uuid=UUID.randomUUID().toString().replace("-", "").toLowerCase();
        OkGo.<BlockHeightEntity>get(ApiConstants.blocknumber)
                .headers(Stringconstant.ACCESS_TOKEN, App.getApplication().getToken())
                .headers(Stringconstant.REQUEST_UUID, uuid)
                .converter(new JsonConvert<BlockHeightEntity>() {})//
                .adapt(new ObservableBody<BlockHeightEntity>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        listener.accept(disposable);
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BlockHeightEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull BlockHeightEntity bean) {
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
