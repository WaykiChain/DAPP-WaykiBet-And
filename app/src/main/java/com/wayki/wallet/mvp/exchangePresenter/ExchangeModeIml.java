package com.wayki.wallet.mvp.exchangePresenter;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.wayki.wallet.application.App;
import com.wayki.wallet.bean.Token2WiccBean;
import com.wayki.wallet.bean.TransferBean;
import com.wayki.wallet.bean.Wicc2TokenBean;
import com.wayki.wallet.bean.entity.AccountInfoEntity;
import com.wayki.wallet.bean.entity.BlockHeightEntity;
import com.wayki.wallet.bean.entity.GetContractEntity;
import com.wayki.wallet.bean.entity.GetWiccEntity;
import com.wayki.wallet.bean.entity.RateEntity;
import com.wayki.wallet.bean.entity.RegisterStatusEntity;
import com.wayki.wallet.bean.entity.TransactionEntity;
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

public class ExchangeModeIml implements ModelContract.ExchangeMode {

    @Override
    public void getRate(final ModelContract.MvpCallBack listener) {
        String uuid=UUID.randomUUID().toString().replace("-", "").toLowerCase();
        OkGo.<RateEntity>get(ApiConstants.wtestrate)
                .headers(Stringconstant.ACCESS_TOKEN, App.getApplication().getToken())
                .headers(Stringconstant.REQUEST_UUID, uuid)
                .converter(new JsonConvert<RateEntity>() {})//
                .adapt(new ObservableBody<RateEntity>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                     listener.accept(disposable);
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RateEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        listener.onSubscribe(d);
                    }

                    @Override
                    public void onNext(@NonNull RateEntity bean) {
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
    public void transfer(TransferBean transferBean,final ModelContract.MvpCallBack listener) {
        String uuid=UUID.randomUUID().toString().replace("-", "").toLowerCase();
        OkGo.<TransactionEntity>post(ApiConstants.transaction_wicc)
                .upJson(new Gson().toJson(transferBean))
                .headers(Stringconstant.REQUEST_UUID, uuid)
                .headers(Stringconstant.ACCESS_TOKEN, App.getApplication().getToken())
                .converter(new JsonConvert<TransactionEntity>(){})//
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
                    public void onSubscribe(@NonNull Disposable d) {
                        listener.onSubscribe(d);
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
    public void wicc2Token(Wicc2TokenBean wicc2TokenBean, final ModelContract.MvpCallBack listener) {
        String uuid= UUID.randomUUID().toString().replace("-", "").toLowerCase();
        OkGo.<TransactionEntity>post(ApiConstants.wicc2token)
                .headers(Stringconstant.ACCESS_TOKEN, App.getApplication().getToken())
                .headers(Stringconstant.REQUEST_UUID, uuid)
                .upJson(new Gson().toJson(wicc2TokenBean))
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
                        listener.onSubscribe(disposable);
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
                      listener.onSubscribe(d);
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
    public void token2wicc(final Token2WiccBean token2WiccBean, final ModelContract.MvpCallBack listener) {
        String uuid= UUID.randomUUID().toString().replace("-", "").toLowerCase();
        OkGo.<TransactionEntity>post(ApiConstants.token2wicc2)
                .headers(Stringconstant.ACCESS_TOKEN, App.getApplication().getToken())
                .headers(Stringconstant.REQUEST_UUID, uuid)
                .upJson(new Gson().toJson(token2WiccBean))
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
                        listener.onSubscribe(disposable);
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
    public void getContract(final ModelContract.MvpCallBack listener) {
        String uuid=UUID.randomUUID().toString().replace("-", "").toLowerCase();
        OkGo.<GetContractEntity>get(ApiConstants.getcontract)
                .headers(Stringconstant.ACCESS_TOKEN, App.getApplication().getToken())
                .headers(Stringconstant.REQUEST_UUID, uuid)
                .converter(new JsonConvert<GetContractEntity>() {})//
                .adapt(new ObservableBody<GetContractEntity>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        listener.accept(disposable);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetContractEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        listener.onSubscribe(d);
                    }

                    @Override
                    public void onNext(@NonNull GetContractEntity bean) {
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
    public void getWicc(final ModelContract.MvpCallBack listener) {
        String uuid=UUID.randomUUID().toString().replace("-", "").toLowerCase();
        OkGo.<GetWiccEntity>get(ApiConstants.getwicc)
                .headers(Stringconstant.ACCESS_TOKEN, App.getApplication().getToken())
                .headers(Stringconstant.REQUEST_UUID, uuid)
                .converter(new JsonConvert<GetWiccEntity>() {})//
                .adapt(new ObservableBody<GetWiccEntity>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        listener.accept(disposable);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetWiccEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        listener.onSubscribe(d);
                    }

                    @Override
                    public void onNext(@NonNull GetWiccEntity bean) {
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

    /**
     * 获得激活状态
     * */
    @Override
    public void getActivsatus(final ModelContract.MvpCallBack listener) {
        String addr= SPUtils.get(UIUtils.getContext(), SPConstants.WALLET_ADDRESS,"").toString();
        String uuid=UUID.randomUUID().toString().replace("-", "").toLowerCase();
        OkGo.<RegisterStatusEntity>get(ApiConstants.isactive+"/"+addr)
                .headers(Stringconstant.ACCESS_TOKEN, App.getApplication().getToken())
                .headers(Stringconstant.REQUEST_UUID, uuid)
                //.params(Stringconstant.ADDRESS,addr)
                .converter(new JsonConvert<RegisterStatusEntity>() {})//
                .adapt(new ObservableBody<RegisterStatusEntity>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                       listener.accept(disposable);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RegisterStatusEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        listener.onSubscribe(d);
                    }

                    @Override
                    public void onNext(@NonNull RegisterStatusEntity bean) {
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
    public void getBlockNumber(final ModelContract.MvpCallBack listener) {
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
                        listener.onSubscribe(d);
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
