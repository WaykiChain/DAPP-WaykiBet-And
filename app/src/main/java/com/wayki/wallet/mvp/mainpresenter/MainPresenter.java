package com.wayki.wallet.mvp.mainpresenter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.wayki.wallet.R;
import com.wayki.wallet.activity.CommonActivity;
import com.wayki.wallet.bean.entity.BindInfoEntity;
import com.wayki.wallet.bean.entity.BottomBarEntity;
import com.wayki.wallet.dialog.CreateWalletTipDialog;
import com.wayki.wallet.dialog.WalletDailog;
import com.wayki.wallet.mvp.base.BasePresenter;
import com.wayki.wallet.mvp.base.ModelContract;
import com.wayki.wallet.utils.SPConstants;
import com.wayki.wallet.utils.SPUtils;
import com.wayki.wallet.utils.UIUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.wayki.wallet.utils.Stringconstant.FRAGMENT_KEY;

public class MainPresenter extends BasePresenter<MainView> {
    private MainView mainView;
    private MainModelml model;
    private Context mContext;
    public MainPresenter(MainView mView,Context context) {
        mainView = mView;
        this.mContext=context;
        model = new MainModelml();
        mCompositeSubscription = new CompositeDisposable();
    }



    /**
     * 查询绑定信息
     * */
    public void bindInfo() {
        model.walletInfo(new ModelContract.MvpCallBack() {

            @Override
            public void accept(@NonNull Disposable d) {

            }

            @Override
            public void onSubscribe(Disposable d) {
                mCompositeSubscription.add(d);
            }

            @Override
            public void success(Object obj) {
                if(obj!=null) {
                    BindInfoEntity infoEntity = (BindInfoEntity) obj;
                    if (infoEntity.getCode() == 0) {
                        String words= SPUtils.get(mContext, SPConstants.WALLET_WORDS,"").toString();
                        if (infoEntity.getData() != null) {
                            String addr= SPUtils.get(mContext, SPConstants.WALLET_ADDRESS,"").toString();
                            if("".equals(words)){
                                SPUtils.put(mContext, SPConstants.WALLET_ADDRESS, infoEntity.getData()+"");
                                SPUtils.put(mContext, SPConstants.WALLET_PRIVATE_KEY, "");
                                SPUtils.put(mContext, SPConstants.WALLET_MD5, "");
                                //showTip();
                            }else if(!addr.equals(infoEntity.getData()+"")){
                                SPUtils.put(mContext, SPConstants.WALLET_ADDRESS, infoEntity.getData()+"");
                                SPUtils.put(mContext, SPConstants.WALLET_PRIVATE_KEY, "");
                                SPUtils.put(mContext, SPConstants.WALLET_MD5, "");
                                SPUtils.put(mContext, SPConstants.WALLET_WORDS, "");
                            }
                        }else{
                         // showTip();
                        }
                    }else {
                        UIUtils.showToast(UIUtils.getErrorCode(infoEntity.getCode()+"",mContext));
                    }
                }
            }

            @Override
            public void error(Object code) {
                String addr= SPUtils.get(mContext, SPConstants.WALLET_ADDRESS,"").toString();
                if("".equals(addr)){
                   // showTip();
                }
            }

            @Override
            public void onComplete() {
                mainView.dismissDialog();
            }
        });
    }


    /**
     * 底部Button
     * */
    public void bottomBar() {
        model.bottomItem(new ModelContract.MvpCallBack() {

            @Override
            public void accept(@NonNull Disposable d) {

            }

            @Override
            public void onSubscribe(Disposable d) {
                mCompositeSubscription.add(d);
            }

            @Override
            public void success(Object obj) {
                if(obj!=null) {
                    BottomBarEntity infoEntity = (BottomBarEntity) obj;
                    if (infoEntity.getCode() == 0) {
                        if(infoEntity.getData()!=null&&infoEntity.getData().size()>0) {
                            mainView.genBottomItem(infoEntity);
                        }
                    }else {
                        UIUtils.showToast(UIUtils.getErrorCode(infoEntity.getCode()+"",mContext));
                    }
                }
            }

            @Override
            public void error(Object code) {

            }

            @Override
            public void onComplete() {
                mainView.dismissDialog();
            }
        });
    }

    private void showTip(){
        CreateWalletTipDialog walletDailog = new CreateWalletTipDialog(mContext,
                R.style.DialogStyle, R.layout.dialog_createwallet_tip);
        walletDailog.addOnConfirmListener(new WalletDailog.onComfirmListener() {
            @Override
            public void onConfirm(WalletDailog dialog) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, CommonActivity.class);
                intent.putExtra(FRAGMENT_KEY, 4);
                mContext.startActivity(intent);
            }
        });

        walletDailog.addOnCancelListener(new WalletDailog.onCancelListener() {
            @Override
            public void onCancel(WalletDailog dialog) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, CommonActivity.class);
                intent.putExtra(FRAGMENT_KEY, 5);
                mContext.startActivity(intent);
            }
        });

        walletDailog.show();
        walletDailog.setCreateWalletTip();
    }


    private void showTip2(){
        CreateWalletTipDialog walletDailog = new CreateWalletTipDialog(mContext,
                R.style.DialogStyle, R.layout.dialog_createwallet_tip);
        walletDailog.addOnConfirmListener(new WalletDailog.onComfirmListener() {
            @Override
            public void onConfirm(WalletDailog dialog) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, CommonActivity.class);
                intent.putExtra(FRAGMENT_KEY, 5);
                mContext.startActivity(intent);
            }
        });

        walletDailog.addOnCancelListener(new WalletDailog.onCancelListener() {
            @Override
            public void onCancel(WalletDailog dialog) {

                dialog.dismiss();
                Intent intent = new Intent(mContext, CommonActivity.class);
                intent.putExtra(FRAGMENT_KEY, 4);
                mContext.startActivity(intent);

            }
        });

        walletDailog.show();
        walletDailog.setCreateWalletTip2();
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
