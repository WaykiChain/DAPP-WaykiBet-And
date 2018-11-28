package com.wayki.wallet.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wayki.wallet.R;
import com.wayki.wallet.utils.UIUtils;

public class Wicc_wkd_Dialog extends WalletDailog {
    SeekBar seekBar;
    TextView tv_fee;

    TextView tv_amount;

    private String amount;//输入余额
    private String account;//账户余额

    public Wicc_wkd_Dialog(@NonNull Context context, int theme, int layout) {
        super(context, theme, layout);
    }

    @Override
    public void initUI() {
        seekBar = findViewById(R.id.sb_fee);
        tv_fee = findViewById(R.id.tv_fee);
        tv_fee.setText(getContext().getString(R.string.kgfy)+":"+"0.001"+getContext().getString(R.string.wicc));

        tv_amount = findViewById(R.id.tv_ec_amount);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double mTips = (0.05000000 - 0.0010000) * progress / 100 + 0.0010000;
                tv_fee.setText(getContext().getString(R.string.kgfy)+":" + UIUtils.setTextDecimal4((mTips) + "")+getContext().getString(R.string.wicc));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public String getPwd() {
        return editText.getText().toString().trim();
    }

    public int getProgress() {
        return seekBar.getProgress();
    }

    public void setTips(String wicc, String wusd){
        String tips =   "<font color='#475A82'>"+ mContext.getString(R.string.you_will_pay)
                + "</font>" +  "<font color='#2E86FF'>" + wicc + "</font>"
                +  "<font color='#475A82'>"+ mContext.getString(R.string.exchange)  + wusd  + "</font>";
        tv_amount.setText(Html.fromHtml(tips));
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
