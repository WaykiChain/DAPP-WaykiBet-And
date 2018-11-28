package com.wayki.wallet.mvp.historypresenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.wayki.wallet.R;
import com.wayki.wallet.bean.HistoryListBean;
import com.wayki.wallet.bean.entity.AccountInfoEntity;
import com.wayki.wallet.bean.entity.ExchangeHistoryEntity;
import com.wayki.wallet.bean.entity.TradeDetailEntity;
import com.wayki.wallet.bean.entity.TradeHistoryEntity;
import com.wayki.wallet.mvp.base.BasePresenter;
import com.wayki.wallet.mvp.base.ModelContract;
import com.wayki.wallet.utils.SPConstants;
import com.wayki.wallet.utils.SPUtils;
import com.wayki.wallet.utils.UIUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class HistoryPresenter extends BasePresenter<HistoryView> {
    private HistoryView historyView;
    private HistoryModelml model;
    private Context mContext;

    public HistoryPresenter(HistoryView mView, Context context) {
        historyView = mView;
        model = new HistoryModelml();
        this.mContext = context;
        mCompositeSubscription = new CompositeDisposable();
    }

    public void getHistoryRecord(int pageNum) {
        String addr= SPUtils.get(mContext, SPConstants.WALLET_ADDRESS,"").toString();
        HistoryListBean listBean=new HistoryListBean();
        listBean.setPageNumber(pageNum);
        listBean.setPageSize(10);
        listBean.setWalletAddress(addr);

        model.getExhangeList(listBean,new ModelContract.MvpCallBack() {
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
                ExchangeHistoryEntity historyEntity=(ExchangeHistoryEntity)str;
                if(historyEntity.getCode()==0){
                    if(historyEntity.getData()!=null){
                        historyView.loadDataSuccess(historyEntity.getData());

                    }
                }else{
                    historyView.showTip(UIUtils.getErrorCode(historyEntity.getCode()+"",mContext));
                    historyView.loadDataFail("");
                }
            }

            @Override
            public void error(Object code) {
                historyView.dismissDialog();
                historyView.loadDataFail("");
            }

            @Override
            public void onComplete() {
                historyView.dismissDialog();
            }
        });

    }


    public void getAllHistory(HistoryListBean listBean) {


        model.getTradeList(listBean,new ModelContract.MvpCallBack() {
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
                TradeHistoryEntity historyEntity=(TradeHistoryEntity)str;
                if(historyEntity.getCode()==0){
                    if(historyEntity.getData()!=null){
                        historyView.loadDataSuccess(historyEntity.getData());
                    }
                }else{
                    historyView.showTip(UIUtils.getErrorCode(historyEntity.getCode()+"",mContext));
                    historyView.loadDataFail("");
                }
            }

            @Override
            public void error(Object code) {
                historyView.dismissDialog();
            }

            @Override
            public void onComplete() {
                historyView.dismissDialog();
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
                        historyView.loadDataFail("");
                    }
                }
            }

            @Override
            public void error(Object code) {
                historyView.dismissDialog();
                historyView.showTip(mContext.getString(R.string.network_error));
                historyView.loadDataFail("");
            }

            @Override
            public void onComplete() {
                historyView.dismissDialog();
            }
        });
    }

    public void getTradeDatail(Long id) {
        model.tradeDatail(id,new ModelContract.MvpCallBack() {
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
                    TradeDetailEntity entity = (TradeDetailEntity) str;
                    if (entity.getCode() == 0) {
                        if (entity.getData() != null) {
                            historyView.tradeDetail(entity);
                        }
                    } else {
                        UIUtils.showToast(UIUtils.getErrorCode(entity.getCode() + "", mContext));
                        historyView.loadDataFail("");
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
