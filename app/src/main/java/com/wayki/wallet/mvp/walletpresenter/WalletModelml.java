package com.wayki.wallet.mvp.walletpresenter;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.wayki.wallet.application.App;
import com.wayki.wallet.bean.BindWalletBean;
import com.wayki.wallet.bean.RewardBean;
import com.wayki.wallet.bean.entity.BindWalletEntity;
import com.wayki.wallet.bean.entity.RewardEntity;
import com.wayki.wallet.mvp.base.ModelContract;
import com.wayki.wallet.utils.ApiConstants;
import com.wayki.wallet.utils.SPConstants;
import com.wayki.wallet.utils.SPUtils;
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

public class WalletModelml implements ModelContract.WalletMode {
    @Override
    public void bindWallet(String hash, String address, final ModelContract.MvpCallBack listener) {
        BindWalletBean walletBean=new BindWalletBean();
        walletBean.setWalletMnecode(hash);
        walletBean.setWiccAddress(address);

        String uuid=UUID.randomUUID().toString().replace("-", "").toLowerCase();
        OkGo.<BindWalletEntity>post(ApiConstants.bindwallet)//
                .headers(Stringconstant.ACCESS_TOKEN, App.getApplication().getToken())
                .headers(Stringconstant.REQUEST_UUID, uuid)
                .upJson(new Gson().toJson(walletBean))
                .converter(new JsonConvert<BindWalletEntity>() {})//
                .adapt(new ObservableBody<BindWalletEntity>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        listener.accept(disposable);
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Observer<BindWalletEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        listener.onSubscribe(d);
                    }

                    @Override
                    public void onNext(@NonNull BindWalletEntity bean) {
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
    public void queryRewardWallet(final ModelContract.MvpCallBack listener) {
        String addr= SPUtils.get(UIUtils.getContext(), SPConstants.WALLET_ADDRESS,"").toString();
        String uuid=UUID.randomUUID().toString().replace("-", "").toLowerCase();
        RewardBean bean=new RewardBean();
        bean.setAddress(addr);
        bean.setSysCoinRewardType(100);
        OkGo.<RewardEntity>post(ApiConstants.reward)//
                .headers(Stringconstant.ACCESS_TOKEN, App.getApplication().getToken())
                .headers(Stringconstant.REQUEST_UUID, uuid)
                .upJson(new Gson().toJson(bean))
                .converter(new JsonConvert<RewardEntity>() {})//
                .adapt(new ObservableBody<RewardEntity>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        listener.accept(disposable);
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Observer<RewardEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        listener.onSubscribe(d);
                    }

                    @Override
                    public void onNext(@NonNull RewardEntity bean) {
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
