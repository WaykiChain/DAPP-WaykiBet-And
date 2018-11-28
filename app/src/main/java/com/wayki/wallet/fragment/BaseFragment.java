package com.wayki.wallet.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.wayki.wallet.R;
import com.wayki.wallet.dialog.LoadingDialog;
import com.wayki.wallet.dialog.TipDialog;
import com.wayki.wallet.dialog.WalletDailog;
import com.wayki.wallet.mvp.LoadLayoutHelper;
import com.wayki.wallet.mvp.base.BasePresenter;
import com.wayki.wallet.mvp.base.BaseView;

import butterknife.ButterKnife;

public abstract class BaseFragment<V extends BaseView, P extends BasePresenter<V>>
        extends MvpFragment<V, P> implements BaseView, LoadLayoutHelper.LoadFailClickListener {
    private LoadLayoutHelper helper;
    protected String emptyMsg;
    public LoadingDialog dialog;
    /**
     * @return 将自定义的布局和helper的布局合并在一起显示
     */
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater,
                              @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView (inflater, container, savedInstanceState);
        helper = new LoadLayoutHelper (getContext (), getLayoutResId ());
        helper.setLoadViewVisible (View.GONE);
        helper.setClickListener (this);
        ButterKnife.bind(this,helper.getContentView());
        initUI();
        return helper.getContentView ();

    }

    /**
     * @return 返回自定义fragment的方法
     */
    public abstract int getLayoutResId ();

    /**
     * @return 加载失败的提示语
     */
    public abstract String getEmptyText ();
    public abstract void initUI ();

    /**
     * 显示helper加载进度的方法
     */
    @Override
    public void showLoadProgress () {
        helper.setLoadViewVisible (View.VISIBLE);
        helper.setProBarVisi (View.VISIBLE);
        helper.setLoadIconIvVisi (View.GONE);
        helper.setLoadMsgText (getString(R.string.loading_data));
    }

    /**
     * 隐藏helper的页面的方法
     */
    @Override
    public void hideLoadProgress () {
        helper.setLoadViewVisible (View.GONE);
    }

    /**
     * 自定义加载失败页面的方法
     */
    @Override
    public void showLoadFail (String errorMsg, @IdRes int failId , int isVisible) {
        helper.setLoadIconIv (failId);
        helper.setLoadMsgText (errorMsg);
        helper.setLoadViewVisible (isVisible);
    }

    /**
     * 默认加载失败页面的方法
     */
    @Override
    public void normalLoadFail () {
        helper.setLoadIconIv (R.mipmap.icon_load_fail);
        helper.setLoadMsgText (getString(R.string.network_error));
        helper.setLoadViewVisible (View.VISIBLE);
    }

    /**
     * 显示加载成功的方法，其关闭了helper的显示页面
     * 如果网络加载成功后，需要携带数据返回，请自定义方法返回
     */
    @Override
    public void showLoadSuccess () {
        helper.setLoadViewVisible (View.GONE);
    }

    /**
     * 显示helper空页面的方法
     */
    @Override
    public void showEmptyPager () {
        helper.setLoadIconIv (R.mipmap.icon_no_data);
        helper.setLoadMsgText (getEmptyText ());
        helper.setLoadViewVisible (View.VISIBLE);
    }


    @Override
    public void setEmptyMsg (String msg) {
        this.emptyMsg=msg;
    }


    @Override
    public  void loadingDialog(String tip){
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = LoadingDialog.showDialog(getContext());
        dialog.setTip(tip);
        dialog.show();
    }

    @Override
    public  void dismissDialog(){
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public  void showTip(String msg){
        TipDialog tipDialog=new TipDialog(getActivity(), R.style.DialogStyle
                , R.layout.dialog_signal_tip);
        tipDialog.show();
        tipDialog.addOnConfirmListener(new WalletDailog.onComfirmListener() {
            @Override
            public void onConfirm(WalletDailog dialog) {
                dialog.dismiss();
            }
        });
        tipDialog.setMessage(msg);
    }
}
