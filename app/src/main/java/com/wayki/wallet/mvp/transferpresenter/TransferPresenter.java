package com.wayki.wallet.mvp.transferpresenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.wayki.wallet.R;
import com.wayki.wallet.application.App;
import com.wayki.wallet.bean.TransferBean;
import com.wayki.wallet.bean.entity.AccountInfoEntity;
import com.wayki.wallet.bean.entity.BlockHeightEntity;
import com.wayki.wallet.bean.entity.TransactionEntity;
import com.wayki.wallet.mvp.base.BasePresenter;
import com.wayki.wallet.mvp.base.ModelContract;
import com.wayki.wallet.utils.MD5Util;
import com.wayki.wallet.utils.SPConstants;
import com.wayki.wallet.utils.SPUtils;
import com.wayki.wallet.utils.UIUtils;
import com.wayki.wallet.utils.WalletUtils;
import com.wayki.wallet.utils.encryption.AESUtils2;

import java.math.BigInteger;
import java.util.Map;

import core.CoinApi;
import core.Constants;
import core.JBtSeed;
import core.JBtWallet;
import core.JNetParams;
import core.JSmcContractTxParams;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TransferPresenter extends BasePresenter<TransferView> {

    private TransferView historyView;
    private TransferModelml model;
    private Context mContext;

    public TransferPresenter(TransferView mView, Context context) {
        historyView = mView;
        model = new TransferModelml();
        this.mContext = context;
        mCompositeSubscription = new CompositeDisposable();
    }

    public void checkMsg() {
        CoinApi coinApi = App.getApplication().getCoinApi();
        JNetParams params = App.getApplication().getjNetParams();
        String add = historyView.getAddress();
        String amount = historyView.getAmount();
        String remarke = historyView.getRemarkr();
        String fee = historyView.getFee();
        if (TextUtils.isEmpty(add)||!coinApi.validateAddress(add.trim(), params)) {
            historyView.showTip(mContext.getString(R.string.address_valid));
        } else if (TextUtils.isEmpty(amount)) {
            historyView.showTip(mContext.getString(R.string.amount_valid));
        } else {
            historyView.showTransDialog();
        }
    }

    public void sendTXdata(final String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            historyView.showTip(mContext.getString(R.string.pwd_empty));
            return;
        }

        String md5_pwd = MD5Util.getMD5Str(pwd);
        String dwords = SPUtils.get(mContext, SPConstants.WALLET_WORDS, "").toString();
        final String words = AESUtils2.decrypt(md5_pwd, dwords);
        if (words != null) {
            model.getblockNumber(new ModelContract.MvpCallBack() {
                @Override
                public void accept(@NonNull Disposable d) {
                    historyView.loadingDialog("");
                }

                @Override
                public void onSubscribe(Disposable d) {
                    mCompositeSubscription.add(d);
                }

                @Override
                public void success(Object str) {
                    BlockHeightEntity heightEntity = (BlockHeightEntity) str;
                    if (heightEntity != null && heightEntity.getCode() == 0) {
                        signTXdata(pwd, heightEntity.getData(),words);
                    }
                }

                @Override
                public void error(Object code) {
                    historyView.dismissDialog();
                    historyView.showTip(mContext.getString(R.string.network_error));
                }

                @Override
                public void onComplete() {

                }
            });
        } else {
            historyView.showTip(mContext.getString(R.string.pwd_error));
        }
    }

    private void signTXdata(final String pwd, final int heigh,final String words) {

        final String add = historyView.getAddress();
        final String amount = historyView.getAmount();
        final String fee = historyView.getFee();
       final String regid=SPUtils.get(mContext,SPConstants.REGID,"").toString();


        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                CoinApi coinApi = App.getApplication().getCoinApi();
                JNetParams jNetParams = App.getApplication().getjNetParams();
                JBtWallet jBtWallet=App.getApplication().getjBtWallet();
                if(jBtWallet==null){
                    jBtWallet = coinApi.createWallet(words, pwd, jNetParams);
                    App.getApplication().setjBtWallet(jBtWallet);
                }
                //JBtWallet jBtWallet = coinApi.createWallet(words, password, jNetParams);
                JBtSeed jbs = jBtWallet.getBtSeed();
                JSmcContractTxParams jSmcContractTxParams2 = new JSmcContractTxParams();
                jSmcContractTxParams2.setPassword(pwd);
                jSmcContractTxParams2.setBtSeed(jbs);
                String fee_input=UIUtils.toBigDecimal2toBigInteger(Double.parseDouble(fee)*100000000);
                jSmcContractTxParams2.setFees(new BigInteger(fee_input));
                jSmcContractTxParams2.setValidHeight(heigh);

                jSmcContractTxParams2.setSrcRegId(regid.trim());
                jSmcContractTxParams2.setDestAddr(add);
                jSmcContractTxParams2.setTxType(Constants.TX_WICC_COMMON);
                double value = Double.parseDouble(amount) * 100000000;
                String m = UIUtils.toBigDecimal2toBigInteger(value);//
                jSmcContractTxParams2.setValue(new BigInteger(m));
                Map resultMap = coinApi.createSignTransaction(jSmcContractTxParams2, jNetParams);
                String signHex = resultMap.get("hex").toString();

                e.onNext(signHex);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String tip) throws Exception {
                        sendTransaction(tip);
                    }
                });
    }


    private void sendTransaction(final String signhash){
        String remarke = historyView.getRemarkr();
        TransferBean transferBean=new TransferBean();
        transferBean.setRawtx(signhash);
        transferBean.setTxRemark(remarke);
        transferBean.setType(200);
        transferBean.setWiccAddress(WalletUtils.getInstance().getAddress(UIUtils.getContext()));


        model.transfer(transferBean, new ModelContract.MvpCallBack() {
            @Override
            public void accept(@NonNull Disposable d) {

            }

            @Override
            public void onSubscribe(Disposable d) {
                mCompositeSubscription.add(d);
            }

            @Override
            public void success(Object str) {
                TransactionEntity entity=(TransactionEntity) str;
                if(entity!=null){
                    if(entity.getCode()==0){
                    historyView.showTip(mContext.getString(R.string.transfer_success));
                    }else{
                        UIUtils.showToast(UIUtils.getErrorCode(entity.getCode() + "", mContext));
                    }
                }
                getAccountInfo();
                historyView.dismissDialog();
            }

            @Override
            public void error(Object code) {
                getAccountInfo();
                historyView.dismissDialog();
                historyView.showTip(mContext.getString(R.string.network_error));
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void getAccountInfo() {
        model.accountInfo(new ModelContract.MvpCallBack() {
            @Override
            public void accept(@NonNull Disposable d) {
                historyView.loadingDialog("");
            }

            @Override
            public void onSubscribe(Disposable d) {
                mCompositeSubscription.add(d);
            }

            @Override
            public void success(Object str) {
                if (str != null) {
                    AccountInfoEntity entity = (AccountInfoEntity) str;
                    if (entity.getCode() == 0) {
                        if (entity.getData() != null&&entity.getData().getCustomerAccountList()!=null
                                &&entity.getData().getCustomerAccountList().size()>0) {
                            historyView.setAmount(entity.getData());
                        }
                    } else {
                        UIUtils.showToast(UIUtils.getErrorCode(entity.getCode() + "", mContext));
                    }
                }
            }

            @Override
            public void error(Object code) {
                historyView.dismissDialog();
                historyView.showTip(mContext.getString(R.string.network_error));
            }

            @Override
            public void onComplete() {
                historyView.dismissDialog();
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
