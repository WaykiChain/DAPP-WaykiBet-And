package com.wayki.wallet.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;

import com.wayki.wallet.R;
import com.wayki.wallet.callback.Wallet_Callback;
import com.wayki.wallet.utils.SPConstants;
import com.wayki.wallet.utils.SPUtils;
import com.wayki.wallet.utils.UIUtils;
import com.wayki.wallet.utils.WalletUtils;

public class BackupWallet extends WalletDailog {
    public TextView tv_words;
    private int layout;
    public BackupWallet(@NonNull Context context, int theme, int layoutid) {
        super(context, theme, layoutid);
        this.layout=layoutid;
    }

    @Override
    public void initUI() {
        tv_words = findViewById(R.id.tv_words);
    }


    /*
    * 导出私钥
    *
    * */

    public void exportPK(){
        if(editText!=null){
            String pwd=editText.getText().toString().trim();

            if(TextUtils.isEmpty(pwd)){
                UIUtils.showToast(UIUtils.getString(R.string.pwd_empty));
                return;
            }

            WalletUtils.getInstance().getPrivateKey(getContext(), pwd, new Wallet_Callback() {
                @Override
                public void Success(Object str) {
                    showPK(str.toString());
                    dismiss();
                }

                @Override
                public void Failure(Object err) {
                    UIUtils.showToast(err.toString());
                    dismiss();
                }
            });
        }
    }


    public void backupWords(){
        if(editText!=null){
            String pwd=editText.getText().toString().trim();

            if(TextUtils.isEmpty(pwd)){
                UIUtils.showToast(UIUtils.getString(R.string.pwd_empty));
                return;
            }

            WalletUtils.getInstance().getWords(getContext(), pwd, new Wallet_Callback() {
                @Override
                public void Success(Object str) {
                BackUpDialog backUpDialog=new BackUpDialog(getContext(),str.toString());
                backUpDialog.setCancelable(false);
                backUpDialog.show();
                SPUtils.put(getContext(), SPConstants.NEEDBACKUP,false);
                    dismiss();
                }

                @Override
                public void Failure(Object err) {
                    UIUtils.showToast(err.toString());
                    dismiss();
                }
            });
        }
    }

    /**
    *
    * 导出助记词
    * */

    public void exportWords(){
        if(editText!=null){
            String pwd=editText.getText().toString().trim();

            if(TextUtils.isEmpty(pwd)){
                UIUtils.showToast(UIUtils.getString(R.string.pwd_empty));
                return;
            }

            WalletUtils.getInstance().getWords(getContext(), pwd, new Wallet_Callback() {
                @Override
                public void Success(Object str) {
                    showWords(str.toString());
                    dismiss();
                }

                @Override
                public void Failure(Object err) {
                  UIUtils.showToast(err.toString());
                  dismiss();
                }
            });
        }
    }

    public void showWords(final String words){
        BackupWallet backupWallet1 = new BackupWallet(getContext(),
                R.style.DialogStyle,R.layout.dialog_backup_completed);
        backupWallet1.addOnConfirmListener(new WalletDailog.onComfirmListener() {
            @Override
            public void onConfirm(WalletDailog dialog) {
                dialog.dismiss();
               UIUtils.toClipboardManager(words);
            }
        });
        backupWallet1.show();
        backupWallet1.setWords(words);
    }

    public void showPK(final String words){
        BackupWallet backupWallet1 = new BackupWallet(getContext(),
                R.style.DialogStyle,R.layout.dialog_backup_privatekey);
        backupWallet1.addOnConfirmListener(new WalletDailog.onComfirmListener() {
            @Override
            public void onConfirm(WalletDailog dialog) {
                dialog.dismiss();
                UIUtils.toClipboardManager(words);
            }
        });
        backupWallet1.show();
        backupWallet1.setWords(words);
    }

    public void setWords(String words) {
        if (tv_words != null) {
            tv_words.setText(words);
        }
    }
}
