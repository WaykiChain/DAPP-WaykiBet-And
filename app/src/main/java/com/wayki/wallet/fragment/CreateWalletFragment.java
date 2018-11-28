package com.wayki.wallet.fragment;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wayki.wallet.R;
import com.wayki.wallet.activity.CommonActivity;
import com.wayki.wallet.application.ActivityManager;
import com.wayki.wallet.dialog.TipDialog;
import com.wayki.wallet.dialog.WalletDailog;
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

import static com.wayki.wallet.utils.Stringconstant.FRAGMENT_VALUE;
import static com.wayki.wallet.utils.Stringconstant.REGISTE_FRAGMENT_KEY;

public class CreateWalletFragment extends BaseFragment<WalletView, WalletPresenter> implements WalletView {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ll_common_title)
    PercentLinearLayout llCommonTitle;
    @Bind(R.id.iv_important)
    ImageView ivImportant;
    @Bind(R.id.tv_tip1)
    TextView tvTip1;
    @Bind(R.id.acb_service)
    CheckBox acbService;
    @Bind(R.id.btn_create)
    Button btnCreate;
    @Bind(R.id.pll_bg)
    PercentLinearLayout pllBg;
    @Bind(R.id.pll_tip)
    PercentRelativeLayout pllTip;
    @Bind(R.id.pll_input)
    EditText pllInput;
    @Bind(R.id.et_input1)
    ImageEditText etInput1;
    @Bind(R.id.et_input2)
    ImageEditText etInput2;
    String pageValue;
    @Bind(R.id.tv_import)
    TextView tvImport;
    @Bind(R.id.tv_tiaoguo)
    TextView tvTiaoguo;
    private String register = "";

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_createwallet;
    }

    @Override
    public String getEmptyText() {
        return null;
    }

    @Override
    public void initUI() {
        llCommonTitle.setBackgroundColor(Color.parseColor("#00000000"));
        pageValue = this.getArguments().getString(FRAGMENT_VALUE);
        register = this.getArguments().getString(REGISTE_FRAGMENT_KEY);

        if ("1".equals(register)) {
            tvTiaoguo.setVisibility(View.VISIBLE);
        }

        changePage();
    }

    @OnClick({R.id.iv_back, R.id.btn_create, R.id.tv_import,R.id.tv_tiaoguo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tiaoguo:
                getActivity().finish();
                break;
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.tv_import:
                pageValue = pageValue.equals("1") ? "2" : "1";
                changePage();
                break;
            case R.id.btn_create:
               // if (acbService.isChecked()) {
                    if (NetworkUtil.isConnected(getActivity())) {
                        if ("1".equals(pageValue)) {
                            presenter.createWallet(getPwd1(), getPwd2(), null);
                        } else if ("2".equals(pageValue)) {
                            presenter.createWallet(getPwd1(), getPwd2(), getWords());
                        }
                    } else {
                        showTip(getString(R.string.network_error));
                    }
                /*} else {
                    showTip(getString(R.string.services_agree));
                }*/
                break;
        }
    }

    private void changePage() {
        if (pageValue != null) {
            switch (pageValue) {
                case "1":
                    tvTitle.setText(getString(R.string.create_wallet));
                    btnCreate.setText(getString(R.string.create_wallet));
                    tvImport.setText(getString(R.string.import_wallet));
                    pllTip.setVisibility(View.VISIBLE);
                    pllInput.setVisibility(View.GONE);
                    break;
                case "2":
                    tvTitle.setText(getString(R.string.import_wallet));
                    pllTip.setVisibility(View.GONE);
                    pllInput.setVisibility(View.VISIBLE);
                    tvImport.setText(getString(R.string.create_wallet));
                    btnCreate.setText(getString(R.string.import_wallet));
                    break;
            }
        }
    }

    @Override
    public WalletPresenter createPresenter() {
        presenter = new WalletPresenter(getContext(), this);
        return presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClickLoadFailText() {

    }

    @Override
    public void showNoNetError() {

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
        TipDialog tipDialog = new TipDialog(getActivity(), R.style.DialogStyle
                , R.layout.dialog_signal_tip);
        tipDialog.show();
        tipDialog.addOnConfirmListener(new WalletDailog.onComfirmListener() {
            @Override
            public void onConfirm(WalletDailog dialog) {
                ActivityManager.create().finishActivity(CommonActivity.class);
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
        pllInput.setText("");
    }

}
