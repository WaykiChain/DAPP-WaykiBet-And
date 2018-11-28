package com.wayki.wallet.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wayki.wallet.R;
import com.wayki.wallet.mvp.walletpresenter.WalletPresenter;
import com.wayki.wallet.mvp.walletpresenter.WalletView;
import com.wayki.wallet.ui.ImageEditText;
import com.wayki.wallet.utils.NetworkUtil;
import com.zhy.android.percent.support.PercentLinearLayout;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateImportDialog extends AlertDialog implements WalletView {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_title_right)
    ImageView ivTitleRight;
    @Bind(R.id.ll_common_title)
    PercentLinearLayout llCommonTitle;
    @Bind(R.id.iv_important)
    ImageView ivImportant;
    @Bind(R.id.tv_tip1)
    TextView tvTip1;
    @Bind(R.id.pll_tip)
    PercentRelativeLayout pllTip;
    @Bind(R.id.pll_input)
    EditText pllInput;
    @Bind(R.id.et_input1)
    ImageEditText etInput1;
    @Bind(R.id.et_input2)
    ImageEditText etInput2;
    @Bind(R.id.acb_service)
    CheckBox acbService;
    @Bind(R.id.btn_create)
    Button btnCreate;
    @Bind(R.id.tv_import)
    TextView tvImport;
    @Bind(R.id.pll_bg)
    PercentLinearLayout pllBg;
    String pageValue = "1";
    WalletPresenter presenter;
    @Bind(R.id.tv_tiaoguo)
    TextView tvTiaoguo;
    public LoadingDialog dialog;
    public CreateImportDialog(@NonNull Context context) {
        super(context, R.style.DialogFullscreen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_createwallet);
        ButterKnife.bind(this, this.getWindow().getDecorView());
        llCommonTitle.setBackgroundColor(Color.parseColor("#00000000"));
        presenter = new WalletPresenter(getContext(), this);
        tvTiaoguo.setVisibility(View.VISIBLE);
        changePage();
        showKeyboard();
    }

    @OnClick({R.id.iv_back, R.id.btn_create, R.id.tv_import,R.id.tv_tiaoguo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                dismiss();
                break;
            case R.id.tv_tiaoguo:
                dismiss();
                break;
            case R.id.tv_import:
                pageValue = pageValue.equals("1") ? "2" : "1";
                changePage();
                break;
            case R.id.btn_create:
                //if (acbService.isChecked()) {
                    if (NetworkUtil.isConnected(getContext())) {
                        if ("1".equals(pageValue)) {
                            presenter.createWallet(getPwd1(), getPwd2(), null);
                        } else if ("2".equals(pageValue)) {
                            presenter.createWallet(getPwd1(), getPwd2(), getWords());
                        }
                    } else {
                        showTip(getContext().getString(R.string.network_error));
                    }
                /*} else {
                    showTip(getContext().getString(R.string.services_agree));
                }*/
                break;
        }
    }

    private void changePage() {
        if (pageValue != null) {
            switch (pageValue) {
                case "1":
                    tvTitle.setText(getContext().getString(R.string.create_wallet));
                    btnCreate.setText(getContext().getString(R.string.create_wallet));
                    tvImport.setText(getContext().getString(R.string.import_wallet));
                    pllTip.setVisibility(View.VISIBLE);
                    pllInput.setVisibility(View.GONE);
                    break;
                case "2":
                    tvTitle.setText(getContext().getString(R.string.import_wallet));
                    pllTip.setVisibility(View.GONE);
                    pllInput.setVisibility(View.VISIBLE);
                    tvImport.setText(getContext().getString(R.string.create_wallet));
                    btnCreate.setText(getContext().getString(R.string.import_wallet));
                    break;
            }
        }
    }

    @Override
    public void dismiss() {
        presenter.detachView(true);
        super.dismiss();
        ButterKnife.unbind(this.getWindow().getDecorView());
    }


    public void showKeyboard() {

        this.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            //设置可获得焦点
            etInput1.setFocusable(true);
            etInput1.setFocusableInTouchMode(true);
            //请求获得焦点
            etInput1.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) etInput1
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(etInput1, 0);

    }

    @Override
    public void showLoadProgress() {

    }

    @Override
    public void hideLoadProgress() {

    }

    @Override
    public void showEmptyPager() {

    }

    @Override
    public void showLoadFail(String errorMsg, int failId, int isVisible) {

    }

    @Override
    public void normalLoadFail() {

    }

    @Override
    public void showLoadSuccess() {

    }

    @Override
    public void showNoNetError() {

    }

    @Override
    public void setEmptyMsg(String msg) {

    }

    @Override
    public void loadingDialog(String tip) {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = LoadingDialog.showDialog(getContext());
        dialog.setTip(tip);
        dialog.show();
    }

    @Override
    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void showTip(String str) {
        TipDialog tipDialog=new TipDialog(getContext(), R.style.DialogStyle
                , R.layout.dialog_signal_tip);
        tipDialog.show();
        tipDialog.addOnConfirmListener(new WalletDailog.onComfirmListener() {
            @Override
            public void onConfirm(WalletDailog dialog) {
                dialog.dismiss();
            }
        });
        tipDialog.setMessage(str);
    }

    @Override
    public ArrayList<String> getWords() {
        String words = pllInput.getText().toString().trim();
        String[] lists = words.split("\\s+");
        ArrayList<String> arr = new ArrayList<>();
        for (String str : lists) {
            if (!TextUtils.isEmpty(str)) {
                arr.add(str);
            }
        }
        return arr;
    }

    @Override
    public String getPwd1() {
        return etInput1.getText().toString().trim();
    }

    @Override
    public String getPwd2() {
        return etInput2.getText().toString().trim();
    }

    @Override
    public String getPwd3() {
        return null;
    }

    @Override
    public void getDataSuccess(Object obj) {
        TipDialog tipDialog = new TipDialog(getContext(), R.style.DialogStyle
                , R.layout.dialog_signal_tip);
        tipDialog.setCancelable(false);
        tipDialog.show();
        tipDialog.addOnConfirmListener(new WalletDailog.onComfirmListener() {
            @Override
            public void onConfirm(WalletDailog dialog) {
                 dismiss();
                dialog.dismiss();
            }
        });
        tipDialog.setMessage(obj.toString());
    }

    @Override
    public void getDataFail(Object fail) {

    }

    @Override
    public void clearText() {
        etInput1.setText("");
        etInput2.setText("");
       if(pllInput.getVisibility()==View.VISIBLE) pllInput.setText("");
    }

}
