package com.wayki.wallet.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wayki.wallet.R;
import com.zhy.android.percent.support.PercentLinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BackUpDialog extends AlertDialog {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_title_right)
    ImageView ivTitleRight;
    @Bind(R.id.ll_common_title)
    PercentLinearLayout llCommonTitle;
    @Bind(R.id.pll_input)
    TextView pllInput;
    @Bind(R.id.btn_create)
    Button btnCreate;
    @Bind(R.id.pll_bg)
    PercentLinearLayout pllBg;
    private String words;
    public BackUpDialog(@NonNull Context context,String lists) {
        super(context, R.style.DialogFullscreen);
        this.words=lists;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_force_backup);
        ButterKnife.bind(this, this.getWindow().getDecorView());
        llCommonTitle.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
        pllInput.setText(words+"");
        tvTitle.setText(getContext().getString(R.string.backup_words));
    }

    @OnClick({R.id.iv_back,R.id.btn_create})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                dismiss();
                break;
            case R.id.btn_create:
                dismiss();
                BackUpDialog2 backUpDialog=new BackUpDialog2(getContext(),words+"");
                backUpDialog.setCancelable(false);
                backUpDialog.show();
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        ButterKnife.unbind( this.getWindow().getDecorView());
    }
}
