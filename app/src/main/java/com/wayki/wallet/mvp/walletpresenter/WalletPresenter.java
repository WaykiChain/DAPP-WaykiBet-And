package com.wayki.wallet.mvp.walletpresenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.wayki.wallet.R;
import com.wayki.wallet.application.App;
import com.wayki.wallet.bean.CreateWalletBean;
import com.wayki.wallet.bean.entity.BindWalletEntity;
import com.wayki.wallet.bean.entity.RewardEntity;
import com.wayki.wallet.callback.Wallet_Callback;
import com.wayki.wallet.fragment.CreateWalletFragment;
import com.wayki.wallet.mvp.base.BasePresenter;
import com.wayki.wallet.mvp.base.ModelContract;
import com.wayki.wallet.utils.SPConstants;
import com.wayki.wallet.utils.SPUtils;
import com.wayki.wallet.utils.Stringconstant;
import com.wayki.wallet.utils.UIUtils;
import com.wayki.wallet.utils.WalletUtils;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class WalletPresenter extends BasePresenter<WalletView> {
    private WalletView walletView;
    private WalletModelml model;
    private Context mContext;

    public WalletPresenter(Context context, WalletView mView) {
        this.mContext = context;
        walletView = mView;
        model = new WalletModelml();
        mCompositeSubscription=new CompositeDisposable();
    }


    /**
     * 创建钱包
     */
    public void createWallet(String pwd1, String pwd2, ArrayList<String> input_words) {

        if (input_words != null && input_words.size() < 12) {
            walletView.showTip(UIUtils.getString(R.string.invalid_mnemonic));
            return;
        } else if (TextUtils.isEmpty(pwd1) || TextUtils.isEmpty(pwd2)) {
            walletView.showTip(UIUtils.getString(R.string.pwd_empty));
            return;
        } else if (!pwd1.equals(pwd2)) {
            walletView.showTip(UIUtils.getString(R.string.pwd_match));
            return;
        } else if (pwd1.length() < 8 || pwd1.length() > 16) {
            walletView.showTip(UIUtils.getString(R.string.pwd_length));
            return;
        } else {
            walletView.loadingDialog(input_words==null?UIUtils.getString(R.string.wallet_creating):
                    UIUtils.getString(R.string.importing));
            WalletUtils.getInstance().createWallet(mContext, pwd1, input_words, new Wallet_Callback() {
                @Override
                public void Success(Object str) {
                    CreateWalletBean entity=(CreateWalletBean)str;
                       // String address= SPUtils.get(mContext, SPConstants.WALLET_ADDRESS,"").toString();
                       // String hash= SPUtils.get(mContext, SPConstants.WALLET_MD5,"").toString();
                        bindWallet(entity);

                }
                @Override
                public void Failure(Object err) {
                    walletView.showTip(err.toString());
                    walletView.dismissDialog();
                }
            });
        }
    }

    /**
     * 修改密码
     */
    public void changePassword(String pwd1, String pwd2,String pwd3) {

         if (TextUtils.isEmpty(pwd1) || TextUtils.isEmpty(pwd2)||TextUtils.isEmpty(pwd3)) {
            walletView.showTip(UIUtils.getString(R.string.pwd_empty));
            return;
        } else if (!pwd2.equals(pwd3)) {
            walletView.showTip(UIUtils.getString(R.string.pwd_match));
            return;
        } else if (pwd2.length() < 8 || pwd2.length() > 16) {
            walletView.showTip(UIUtils.getString(R.string.pwd_length));
            return;
        } else {
          WalletUtils.getInstance().changePassword(mContext, pwd1, pwd2, new Wallet_Callback() {
              @Override
              public void Success(Object str) {
                walletView.showTip(str.toString());
                walletView.clearText();
              }

              @Override
              public void Failure(Object err) {
                  walletView.showTip(err.toString());
              }
          });
        }
    }

    /**
     * 绑定钱包
     */
    public void bindWallet(final CreateWalletBean entity) {
        model.bindWallet(entity.getHash(),entity.getAddress(),new ModelContract.MvpCallBack() {

            @Override
            public void accept(@NonNull Disposable d) {

            }
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeSubscription.add(d);
            }
            @Override
            public void success(Object obj) {
                BindWalletEntity bindWalletEntity=(BindWalletEntity)obj;
                if(obj!=null) {
                    if(bindWalletEntity.getCode()==0){
                       // walletView.showTip(entity.getTip().toString());
                        SPUtils.put(mContext, SPConstants.WALLET_WORDS, entity.getWords());
                        SPUtils.put(mContext, SPConstants.WALLET_MD5, entity.getHash());
                        SPUtils.put(mContext, SPConstants.WALLET_PRIVATE_KEY, entity.getPrivatekey());
                        SPUtils.put(mContext, SPConstants.WALLET_ADDRESS, entity.getAddress());
                        SPUtils.put(mContext, SPConstants.REGID, "");
                        App.getApplication().setjBtWallet(entity.getjBtWallet());
                        if(Stringconstant.IMPORT.equals(entity.getCreata_import())){
                           SPUtils.put(mContext,SPConstants.NEEDBACKUP,false);
                        }else  if(Stringconstant.CREATE.equals(entity.getCreata_import())){
                            SPUtils.put(mContext,SPConstants.NEEDBACKUP,true);/*创建的钱包需要备份*/
                        }
                         walletView.clearText();
                        if(walletView instanceof CreateWalletFragment){
                            walletView.getDataSuccess(entity.getTip().toString());
                            walletView.dismissDialog();
                        }else{
                            rewardSearch(entity.getTip().toString());
                        }
                        //
                    }else {
                        UIUtils.showToast(UIUtils.getErrorCode(bindWalletEntity.getCode()+"",
                                mContext));
                        walletView.dismissDialog();
                    }
                }
            }
            @Override
            public void error(Object code) {
                walletView.dismissDialog();
                walletView.clearText();
            }

            @Override
            public void onComplete() {
               // walletView.dismissDialog();
            }
        });
    }

    public void rewardSearch(final String txt) {
        model.queryRewardWallet(new ModelContract.MvpCallBack() {

            @Override
            public void accept(@NonNull Disposable d) {

            }
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeSubscription.add(d);
            }
            @Override
            public void success(Object obj) {
                RewardEntity entity=(RewardEntity) obj;
                if(entity!=null&&entity.getCode()==0){
                    if(entity.getData()!=null&&entity.getData().size()>0){
                        walletView.getDataSuccess(txt+mContext.getString(R.string.reward_tip));
                    }else {
                        walletView.getDataSuccess(txt);
                      //  walletView.dismissDialog();
                    }
                }
            }
            @Override
            public void error(Object code) {
                walletView.getDataSuccess(txt);
                walletView.dismissDialog();
                walletView.clearText();
            }

            @Override
            public void onComplete() {
                walletView.dismissDialog();
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
