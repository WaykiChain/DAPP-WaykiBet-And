package com.wayki.wallet.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.Html;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wayki.wallet.R;

public class Wkd_wicc_Dialog extends WalletDailog {
    AppCompatSeekBar seekBar;
    TextView tv_fee;

    TextView tv_amount;

    public Wkd_wicc_Dialog(@NonNull Context context, int theme, int layout) {
        super(context, theme, layout);
    }

    @Override
    public void initUI() {
        seekBar = findViewById(R.id.sb_fee);
        setSeekBarClickable(0);
        tv_fee = findViewById(R.id.tv_fee);

        tv_amount = findViewById(R.id.tv_ec_amount);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_fee.setText(getContext().getString(R.string.kgfy) + (0.01 * progress) + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void setSeekBarClickable(int i){
        if (i==1){
            //启用状态
            seekBar.setClickable(true);
            seekBar.setEnabled(true);
            seekBar.setSelected(true);
            seekBar.setFocusable(true);

        }else {
            //禁用状态
            seekBar.setClickable(false);
            seekBar.setEnabled(false);
            seekBar.setSelected(false);
            seekBar.setFocusable(false);

        }
    }


    public int getProgress() {
        return seekBar.getProgress();
    }

    public void setTips(String wicc, String wusd) {
        String tips = "<font color='#475A82'>" + mContext.getString(R.string.you_will_pay)
                + "</font>" + "<font color='#2E86FF'>" + wicc + "</font>"
                + "<font color='#475A82'>" + mContext.getString(R.string.exchange) + wusd + "</font>";
        tv_amount.setText(Html.fromHtml(tips));
    }
}
