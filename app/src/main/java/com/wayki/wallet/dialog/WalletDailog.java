package com.wayki.wallet.dialog;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wayki.wallet.R;

public abstract class WalletDailog extends AlertDialog implements View.OnClickListener {

    private WalletDailog.onComfirmListener comfirmListener;
    private WalletDailog.onCancelListener cancelListener;
    public Context mContext;
    private int layoutID;
    public Button btn_cancel;
    public Button btn_confirm;
    public EditText editText;
    public TextView tv_message;

    protected WalletDailog(@NonNull Context context, int theme, int layoutid) {
        super(context, theme);
        this.mContext = context;
        this.layoutID = layoutid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay(); //获取屏幕宽高
        Point point = new Point();
        display.getSize(point);

        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes(); //获取当前对话框的参数值
        layoutParams.width = (int) (point.x * 0.9); //宽度设置为屏幕宽度的0.9
        layoutParams.height = (int) (point.y * 0.45); //高度设置为屏幕高度的0.5
//        layoutParams.width = (int) (display.getWidth() * 0.5);
//        layoutParams.height = (int) (display.getHeight() * 0.5);
        window.setGravity(Gravity.CENTER);
        window.setAttributes(layoutParams);

        window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
        window.setBackgroundDrawableResource(android.R.color.transparent); //设置对话框背景为透明


        initUI();

        if (findViewById(R.id.btn_cancel) != null) {
            btn_cancel = findViewById(R.id.btn_cancel);
            btn_cancel.setOnClickListener(this);
        }
        if (findViewById(R.id.btn_confirm) != null) {
            btn_confirm = findViewById(R.id.btn_confirm);
            btn_confirm.setOnClickListener(this);
        }

        if (findViewById(R.id.tv_message) != null) {
            tv_message = findViewById(R.id.tv_message);
        }

        if (findViewById(R.id.dialog_pwd) != null) {
            editText = findViewById(R.id.dialog_pwd);
            this.getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            showKeyboard();
        }




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                if (cancelListener != null) {
                    cancelListener.onCancel(this);
                }else {
                    dismiss();
                    hideKeyboard(view);
                }
                break;
            case R.id.btn_confirm:
                if (comfirmListener != null) {
                    comfirmListener.onConfirm(this);
                }
                break;
        }
    }

    public void setMessage(String message){
        if(tv_message!=null){
            tv_message.setText(message);
        }
    }

    public abstract void initUI();

    public void addOnConfirmListener(WalletDailog.onComfirmListener listener) {
        comfirmListener = listener;
    }
    public void addOnCancelListener(WalletDailog.onCancelListener listener) {
        cancelListener = listener;
    }

    public void addOnConfirmListener(String str1,WalletDailog.onComfirmListener listener) {
        if(btn_confirm!=null){
            btn_confirm.setText(str1);
        }
        comfirmListener = listener;
    }
    public void addOnCancelListener(String str2,WalletDailog.onCancelListener listener) {
        if(btn_cancel!=null){
            btn_cancel.setText(str2);
        }
        cancelListener = listener;
    }

    public interface onComfirmListener {
        void onConfirm(WalletDailog dialog);
    }

    public interface onCancelListener {
        void onCancel(WalletDailog dialog);
    }

    public void showKeyboard() {
        if (editText != null) {
            //设置可获得焦点
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            //请求获得焦点
            editText.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) editText
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(editText, 0);
        }
    }


    public void hideKeyboard(View v) {
        if(editText!=null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            //if (imm.isActive()) {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
          //  }
        }
    }

}
