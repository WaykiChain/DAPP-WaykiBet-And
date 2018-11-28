package com.wayki.wallet.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.wayki.wallet.R;
import com.wayki.wallet.activity.CommonActivity;
import com.wayki.wallet.activity.WebComonActivity;
import com.wayki.wallet.application.App;
import com.wayki.wallet.bean.entity.GetWiccEntity;
import com.wayki.wallet.callback.Wallet_Callback;
import com.wayki.wallet.utils.ApiConstants;
import com.wayki.wallet.utils.Stringconstant;
import com.wayki.wallet.utils.UIUtils;
import com.wayki.wallet.utils.WalletUtils;
import com.wayki.wallet.utils.network.JsonConvert;
import com.wayki.wallet.utils.network.ObservableBody;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.wayki.wallet.utils.Stringconstant.FRAGMENT_KEY;

public class Dialog_full_qrcode extends AlertDialog {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ll_common_title)
    PercentLinearLayout llCommonTitle;
    @Bind(R.id.btn_copy)
    Button btnSend;
    @Bind(R.id.pll_bg)
    PercentLinearLayout pllBg;
    @Bind(R.id.iv_title_right)
    ImageView ivTitleRight;
    @Bind(R.id.btn_getwicc)
    TextView btnGetwicc;
    @Bind(R.id.iv_adderss_qrcode)
    ImageView ivAdderssQrcode;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    private  int id=0;
    public LoadingDialog dialog;
    public Dialog_full_qrcode(@NonNull Context context,int id) {
        super(context, R.style.DialogFullscreen);
        this.id=id;
    }

    public Dialog_full_qrcode(@NonNull Context context) {
        super(context, R.style.DialogFullscreen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_qrcode);
        ButterKnife.bind(this, this.getWindow().getDecorView());
        llCommonTitle.setBackground(getContext().getResources().getDrawable(R.drawable.trans));
        ivTitleRight.setImageDrawable(getContext().getResources().getDrawable(R.drawable.iv_history));
        tvTitle.setText(getContext().getString(R.string.receivables_code));
        btnGetwicc.setText(getContext().getString(R.string.getwicc));
        btnSend.setText(getContext().getString(R.string.copy_address));
        pllBg.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        tvAddress.setText(WalletUtils.getInstance().getAddress(getContext()));

        WalletUtils.getInstance().getBitmap(getContext(), new Wallet_Callback() {
            @Override
            public void Success(Object str) {
                ivAdderssQrcode.setImageBitmap((Bitmap) str);
            }

            @Override
            public void Failure(Object err) {

            }
        });

    }

    @OnClick({R.id.iv_back, R.id.iv_title_right, R.id.btn_copy,R.id.btn_getwicc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                dismiss();
                break;

            case R.id.iv_title_right:
                if(id==1){
                    dismiss();
                }else {
                    Intent intent = new Intent(getContext(), CommonActivity.class);
                    intent.putExtra(FRAGMENT_KEY, 1);
                    getContext().startActivity(intent);
                }
                break;

            case R.id.btn_copy:
                UIUtils.toClipboardManager(WalletUtils.getInstance().getAddress(getContext()));
                break;
            case R.id.btn_getwicc:
                getWicc();
                break;
        }
    }

    private void getWicc(){
        String uuid= UUID.randomUUID().toString().replace("-", "").toLowerCase();
        OkGo.<GetWiccEntity>get(ApiConstants.getwicc)
                .headers(Stringconstant.ACCESS_TOKEN, App.getApplication().getToken())
                .headers(Stringconstant.REQUEST_UUID, uuid)
                .converter(new JsonConvert<GetWiccEntity>() {})//
                .adapt(new ObservableBody<GetWiccEntity>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        loadingDialog("");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetWiccEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull GetWiccEntity str) {
                        GetWiccEntity entity=(GetWiccEntity)str;
                        if(entity!=null){
                            if(entity.getCode()==0){
                                if(entity.getData()!=null&&entity.getData().getLinkUrl()!=null){
                                    Intent intent = new Intent(getContext(), WebComonActivity.class);
                                    intent.putExtra(Stringconstant.WEB_URL_VALUE, entity.getData().getLinkUrl());
                                    intent.putExtra(Stringconstant.WEB_HAD_LOGIN, "true");
                                    getContext().startActivity(intent);
                                }
                            }else{
                                UIUtils.showToast(UIUtils.getErrorCode(entity.getCode()+"",getContext()));
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                         dismissDialog();
                    }

                    @Override
                    public void onComplete() {
                        dismissDialog();
                    }
                });
    }


    private   void loadingDialog(String tip){
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = LoadingDialog.showDialog(getContext());
        dialog.setTip(tip);
        dialog.show();
    }

    public  void dismissDialog(){
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        ButterKnife.unbind(this.getWindow().getDecorView());
    }
}
