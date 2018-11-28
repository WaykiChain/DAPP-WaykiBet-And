package com.wayki.wallet.mvp.exchangePresenter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.wayki.wallet.R;
import com.wayki.wallet.activity.WebComonActivity;
import com.wayki.wallet.application.App;
import com.wayki.wallet.bean.ExchangeBean;
import com.wayki.wallet.bean.SignTXBean;
import com.wayki.wallet.bean.Token2WiccBean;
import com.wayki.wallet.bean.TransferBean;
import com.wayki.wallet.bean.WalletBalanceBean;
import com.wayki.wallet.bean.Wicc2TokenBean;
import com.wayki.wallet.bean.entity.AccountInfoEntity;
import com.wayki.wallet.bean.entity.BlockHeightEntity;
import com.wayki.wallet.bean.entity.GetContractEntity;
import com.wayki.wallet.bean.entity.GetWiccEntity;
import com.wayki.wallet.bean.entity.RateEntity;
import com.wayki.wallet.bean.entity.RegisterStatusEntity;
import com.wayki.wallet.bean.entity.TransactionEntity;
import com.wayki.wallet.mvp.base.BasePresenter;
import com.wayki.wallet.mvp.base.ModelContract;
import com.wayki.wallet.utils.MD5Util;
import com.wayki.wallet.utils.SPConstants;
import com.wayki.wallet.utils.SPUtils;
import com.wayki.wallet.utils.Stringconstant;
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


public class ExchangePresenter extends BasePresenter<ExchangeView> {
    private ExchangeView exchangeView;
    private ExchangeModeIml model;
    private Context mContext;

    public ExchangePresenter(ExchangeView mView, Context context) {
        exchangeView = mView;
        this.mContext = context;
        model = new ExchangeModeIml();
        mCompositeSubscription = new CompositeDisposable();
    }
    /**
     * 获得汇率
     * */
    public void getRate() {
        model.getRate(new ModelContract.MvpCallBack() {
            @Override
            public void accept(@NonNull Disposable d) {
                exchangeView.loadingDialog(mContext.getString(R.string.loading_data));
            }

            @Override
            public void onSubscribe(Disposable d) {
                mCompositeSubscription.add(d);
            }

            @Override
            public void success(Object str) {
                if (str != null) {
                    RateEntity entity = (RateEntity) str;
                    if (entity.getCode() == 0) {
                        if (entity.getData() != null) {
                            exchangeView.setRateText(entity.getData());
                        }
                    } else {
                        UIUtils.showToast(UIUtils.getErrorCode(entity.getCode() + "", mContext));
                    }
                }
            }

            @Override
            public void error(Object code) {
                exchangeView.dismissDialog();
            }

            @Override
            public void onComplete() {
                exchangeView.dismissDialog();
            }
        });
    }

    /**
     * 获得合约地址
     * */
    public void getContractid() {
        model.getContract(new ModelContract.MvpCallBack() {
            @Override
            public void accept(@NonNull Disposable d) {
                exchangeView.loadingDialog(mContext.getString(R.string.loading_data));
            }

            @Override
            public void onSubscribe(Disposable d) {
                mCompositeSubscription.add(d);
            }

            @Override
            public void success(Object str) {
                if (str != null) {
                    GetContractEntity entity = (GetContractEntity) str;
                    if (entity.getCode() == 0) {
                        if (entity.getData() != null) {
                            exchangeView.setContract(entity.getData());
                        }
                    } else {
                        UIUtils.showToast(UIUtils.getErrorCode(entity.getCode() + "", mContext));
                    }
                }
            }

            @Override
            public void error(Object code) {
                exchangeView.dismissDialog();
            }

            @Override
            public void onComplete() {
                exchangeView.dismissDialog();
            }
        });
    }
      /**
       *
       * 查询激活状态
       * */
      public void activeStatus(){
          model.getActivsatus(new ModelContract.MvpCallBack() {
              @Override
              public void accept(@NonNull Disposable d) {
                  exchangeView.loadingDialog(mContext.getString(R.string.loading_data));
              }

              @Override
              public void onSubscribe(Disposable d) {
                  mCompositeSubscription.add(d);
              }

              @Override
              public void success(Object str) {
                  if (str != null) {
                      RegisterStatusEntity entity = (RegisterStatusEntity) str;
                      if (entity.getCode() == 0) {
                         if(entity.getData()!=null){
                             if(entity.getData().isActive()) {//已经激活的
                                 SPUtils.put(mContext, SPConstants.REGID, entity.getData().getRegId());
                                 exchangeView.setActive(true);
                             }else{
                                 exchangeView.setActive(false);
                             }
                         }
                      } else {
                          UIUtils.showToast(UIUtils.getErrorCode(entity.getCode() + "", mContext));
                      }
                  }
              }

              @Override
              public void error(Object code) {
                  exchangeView.dismissDialog();
              }

              @Override
              public void onComplete() {
                  exchangeView.dismissDialog();
              }
          });

      }


    public void transExchange(final ExchangeBean exchangeBean){
          final String pwd=exchangeBean.getPwd();
        if(TextUtils.isEmpty(pwd)){
            exchangeView.showTip(mContext.getString(R.string.pwd_empty));
            return;
        }
        final String md5_pwd = MD5Util.getMD5Str(pwd);
        String dwords = SPUtils.get(mContext, SPConstants.WALLET_WORDS, "").toString();
        final String words = AESUtils2.decrypt(md5_pwd, dwords);
        if(words!=null){
            model.getBlockNumber(new ModelContract.MvpCallBack() {
                @Override
                public void accept(@NonNull Disposable d) {
                    exchangeView.loadingDialog("");
                }

                @Override
                public void onSubscribe(Disposable d) {
                    mCompositeSubscription.add(d);
                }

                @Override
                public void success(Object str) {
                    BlockHeightEntity entity=(BlockHeightEntity)str;
                  if(entity!=null&&entity.getCode()==0){
                      int number=entity.getData();
                      signTXData(number,words,exchangeBean);
                  }
                }

                @Override
                public void error(Object code) {
                    exchangeView.showTip(mContext.getString(R.string.network_error));
                    exchangeView.dismissDialog();
                }

                @Override
                public void onComplete() {

                }
            });
        }else{
            exchangeView.showTip(mContext.getString(R.string.pwd_error));
        }

    }

    /**
     * 进行签名
    * */
    private void signTXData(final int blocknum, final String words, final ExchangeBean exchangeBean) {
        final String regid=SPUtils.get(mContext,SPConstants.REGID,"").toString();
        Observable.create(new ObservableOnSubscribe<SignTXBean>() {
            @Override
            public void subscribe(ObservableEmitter<SignTXBean> e) throws Exception {
                SignTXBean signTXBean=new SignTXBean();
                CoinApi coinApi = App.getApplication().getCoinApi();
                JNetParams jNetParams = App.getApplication().getjNetParams();
                JBtWallet jBtWallet=App.getApplication().getjBtWallet();
                if(jBtWallet==null){
                    jBtWallet = coinApi.createWallet(words, exchangeBean.getPwd(), jNetParams);
                    App.getApplication().setjBtWallet(jBtWallet);
                }
                //JBtWallet jBtWallet = coinApi.createWallet(words, password, jNetParams);
                JBtSeed jbs = jBtWallet.getBtSeed();
                JSmcContractTxParams jSmcContractTxParams2 = new JSmcContractTxParams();
                jSmcContractTxParams2.setPassword(exchangeBean.getPwd());
                jSmcContractTxParams2.setBtSeed(jbs);
                jSmcContractTxParams2.setValidHeight(blocknum);
                if("200".equals(exchangeBean.getType())) {
                    double feevalue = Double.parseDouble(exchangeBean.getFee()) * 100000000;
                    String feem = UIUtils.toBigDecimal2toBigInteger(feevalue);//
                    jSmcContractTxParams2.setFees(new BigInteger(feem));
                    jSmcContractTxParams2.setSrcRegId(regid);
                    jSmcContractTxParams2.setDestAddr(exchangeBean.getDestAddrid());
                    jSmcContractTxParams2.setTxType(Constants.TX_WICC_BET);
                    double value = Double.parseDouble(exchangeBean.getAmount1()) * 100000000;
                    String m = UIUtils.toBigDecimal2toBigInteger(value);//
                    /*汇率*/
                    double rate= Double.parseDouble(exchangeBean.getRate()) * 10000;
                    String m_rate = UIUtils.toBigDecimal2toBigInteger(rate);//
                    String rate_num = UIUtils.toHexString(m_rate);//汇率转16进制

                    /*WUSD*/
                    double wusd= Double.parseDouble(exchangeBean.getAmount2())*100000000;
                    String m_wusd = UIUtils.toBigDecimal2toBigInteger(wusd);//
                    String wusd_num = UIUtils.toHexString(m_wusd);//wusd转16进制


                    String bbbb = "f0040000"+UIUtils.valueSort(rate_num)+UIUtils.valueSort(wusd_num);//转网络序
                    byte[] bin2 = UIUtils.hexString2binaryString(bbbb);
                    jSmcContractTxParams2.setContract(bin2);
                    signTXBean.setContractData(bbbb);
                jSmcContractTxParams2.setValue(new BigInteger(m));
                }else if("100".equals(exchangeBean.getType())){
                    jSmcContractTxParams2.setFees(new BigInteger("10000"));
                    jSmcContractTxParams2.setTxType(Constants.TX_WICC_REGISTERACCOUNT);
                    jSmcContractTxParams2.setValue(new BigInteger("0"));
                }

                Map resultMap = coinApi.createSignTransaction(jSmcContractTxParams2, jNetParams);
                String signHex = resultMap.get("hex").toString();
                signTXBean.setSignTX(signHex);
                e.onNext(signTXBean);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SignTXBean>() {
                    @Override
                    public void accept(SignTXBean signTXBean) throws Exception {
                        if("100".equals(exchangeBean.getType())){
                           sendTransaction(signTXBean.getSignTX());
                        }else if("200".equals(exchangeBean.getType())){
                            wicc2Token(signTXBean,exchangeBean.getDestAddr());
                        }
                    }
                });
    }

    /**
     * wicc兑换 Token
    * */
    private void wicc2Token(SignTXBean signTXBean,String desAddr) {

        String addr = SPUtils.get(mContext, SPConstants.WALLET_ADDRESS, "").toString();
        Wicc2TokenBean wicc2TokenBean = new Wicc2TokenBean();
        wicc2TokenBean.setCoinSymbol(mContext.getString(R.string.wusd));
        wicc2TokenBean.setContractAddress(desAddr);
        wicc2TokenBean.setRawtx(signTXBean.getSignTX());
        wicc2TokenBean.setWiccAddress(addr);
        wicc2TokenBean.setRequestContract(signTXBean.getContractData());
        wicc2TokenBean.setTxRemark("");
        model.wicc2Token(wicc2TokenBean, new ModelContract.MvpCallBack() {
            @Override
            public void accept(@NonNull Disposable d) {

            }

            @Override
            public void onSubscribe(Disposable d) {
                mCompositeSubscription.add(d);
            }

            @Override
            public void success(Object str) {
                TransactionEntity entity = (TransactionEntity) str;
                if (entity != null) {
                    if (entity.getCode() == 0) {
                        exchangeView.showTip(mContext.getString(R.string.exchange_success)+
                                ","+mContext.getString(R.string.waiting_confirmation));
                    } else {
                        UIUtils.showToast(UIUtils.getErrorCode(entity.getCode() + "", mContext));
                    }
                }
            }

            @Override
            public void error(Object code) {
                exchangeView.showTip(mContext.getString(R.string.network_error));
                exchangeView.dismissDialog();
            }

            @Override
            public void onComplete() {
                exchangeView.dismissDialog();
            }
        });
    }

    /**
     * wicc兑换 Token
     * */
    public void token2Wicc(Token2WiccBean token2WiccBean) {
        model.token2wicc(token2WiccBean, new ModelContract.MvpCallBack() {
            @Override
            public void accept(@NonNull Disposable d) {

            }

            @Override
            public void onSubscribe(Disposable d) {
                mCompositeSubscription.add(d);
            }

            @Override
            public void success(Object str) {
                TransactionEntity entity = (TransactionEntity) str;
                if (entity != null) {
                    if (entity.getCode() == 0) {
                        exchangeView.showTip(mContext.getString(R.string.transfer_success));
                    } else {
                        UIUtils.showToast(UIUtils.getErrorCode(entity.getCode() + "", mContext));
                    }
                }
            }

            @Override
            public void error(Object code) {
                exchangeView.showTip(mContext.getString(R.string.network_error));
                exchangeView.dismissDialog();
            }

            @Override
            public void onComplete() {
                exchangeView.dismissDialog();
            }
        });
    }


    /**
     * 获得账户信息
     * */
    public void getAccountInfo() {
        model.accountInfo(new ModelContract.MvpCallBack() {
            @Override
            public void accept(@NonNull Disposable d) {
                exchangeView.loadingDialog("");
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
                        if (entity.getData() != null&&entity.getData().getCustomerAccountList()!=null&&entity.getData().getCustomerAccountList().size()>0) {
                            WalletBalanceBean balance = new WalletBalanceBean();
                            balance.setB_amount(entity.getData().getWiccPriceCNY() + "");
                            balance.setB_wicc(entity.getData().getCustomerAccountList().get(0).getBalanceAvailable() + "");
                            balance.setB_wusd(entity.getData().getCustomerAccountList().get(2).getBalanceAvailable() + "");
                            balance.setWusd_price(entity.getData().getCustomerAccountList().get(2).getPriceCNY()+"");
                            exchangeView.setBalance(balance);
                        }
                    } else {
                        UIUtils.showToast(UIUtils.getErrorCode(entity.getCode() + "", mContext));
                    }
                }
                exchangeView.dismissLoading();
            }

            @Override
            public void error(Object code) {
                exchangeView.dismissDialog();
                exchangeView.showTip(mContext.getString(R.string.network_error));
            }

            @Override
            public void onComplete() {
                exchangeView.dismissDialog();
            }
        });
    }


    /**
     * 发送交易
     * */
    private void sendTransaction(final String signhash){
        TransferBean transferBean=new TransferBean();
        transferBean.setRawtx(signhash);
        transferBean.setTxRemark("");
        transferBean.setType(100);
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
                TransactionEntity entity=(TransactionEntity)str;
                if(entity!=null){
                    if(entity.getCode()==0){
                         exchangeView.showTip(mContext.getString(R.string.activation_successful));
                         exchangeView.setActive(true);
                    }else{
                       UIUtils.showToast(UIUtils.getErrorCode(entity.getCode()+"",mContext));
                    }
                }
            }

            @Override
            public void error(Object code) {
                exchangeView.showTip(mContext.getString(R.string.network_error));
                exchangeView.dismissDialog();
            }

            @Override
            public void onComplete() {
                exchangeView.dismissDialog();

                getAccountInfo();
            }
        });
    }

/**
* 获得WICC链接
* */
    public void getWicc(){
        model.getWicc( new ModelContract.MvpCallBack() {
            @Override
            public void accept(@NonNull Disposable d) {
              exchangeView.loadingDialog("");
            }

            @Override
            public void onSubscribe(Disposable d) {
                mCompositeSubscription.add(d);
            }

            @Override
            public void success(Object str) {
                GetWiccEntity entity=(GetWiccEntity)str;
                if(entity!=null){
                    if(entity.getCode()==0){
                      if(entity.getData()!=null&&entity.getData().getLinkUrl()!=null){
                          Intent intent = new Intent(mContext, WebComonActivity.class);
                          intent.putExtra(Stringconstant.WEB_URL_VALUE, entity.getData().getLinkUrl());
                          intent.putExtra(Stringconstant.WEB_HAD_LOGIN, "true");
                          mContext.startActivity(intent);
                      }
                    }else{
                        UIUtils.showToast(UIUtils.getErrorCode(entity.getCode()+"",mContext));
                    }
                }
            }

            @Override
            public void error(Object code) {
                exchangeView.showTip(mContext.getString(R.string.network_error));
                exchangeView.dismissDialog();
            }

            @Override
            public void onComplete() {
                exchangeView.dismissDialog();
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