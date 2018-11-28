package com.wayki.wallet.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

public class ActiveDialog extends WalletDailog {
    public ActiveDialog(@NonNull Context context, int theme, int layoutid) {
        super(context, theme, layoutid);
    }

    @Override
    public void initUI() {

    }

    public String getPwd() {
        return editText.getText().toString().trim();
    }
}
