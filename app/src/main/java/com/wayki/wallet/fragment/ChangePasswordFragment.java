package com.wayki.wallet.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wayki.wallet.R;
import com.wayki.wallet.mvp.walletpresenter.WalletPresenter;
import com.wayki.wallet.mvp.walletpresenter.WalletView;
import com.wayki.wallet.ui.ImageEditText;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordFragment extends BaseFragment<WalletView, WalletPresenter> implements WalletView {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ll_common_title)
    PercentLinearLayout llCommonTitle;
    @Bind(R.id.btn_send)
    Button btnSend;
    @Bind(R.id.pll_bg)
    PercentLinearLayout pllBg;
    @Bind(R.id.iv_title_right)
    ImageView ivTitleRight;
    @Bind(R.id.iet_input1)
    ImageEditText ietInput1;
    @Bind(R.id.iet_input2)
    ImageEditText ietInput2;
    @Bind(R.id.iet_input3)
    ImageEditText ietInput3;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_change_pwd;
    }

    @Override
    public String getEmptyText() {
        return null;
    }

    @Override
    public void initUI() {
        llCommonTitle.setBackgroundColor(Color.parseColor("#00000000"));
        tvTitle.setText(getString(R.string.change_password));
    }


    @OnClick({R.id.iv_back,R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.btn_send:
                presenter.changePassword(getPwd1(),getPwd2(),getPwd3());
                break;
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
    public List<String> getWords() {
        return null;
    }

    @Override
    public String getPwd1() {
        return ietInput1.getText().toString().trim();
    }

    @Override
    public String getPwd2() {
        return ietInput2.getText().toString().trim();
    }

    @Override
    public String getPwd3() {
        return ietInput3.getText().toString().trim();
    }

    @Override
    public void getDataSuccess(Object obj) {

    }

    @Override
    public void getDataFail(Object fail) {

    }

    @Override
    public void clearText() {
        ietInput1.setText("");
        ietInput2.setText("");
        ietInput3.setText("");
    }

}
