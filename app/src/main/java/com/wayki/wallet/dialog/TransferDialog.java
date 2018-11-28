package com.wayki.wallet.dialog;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.wayki.wallet.R;

public class TransferDialog extends WalletDailog {
    private EditText et_pwd;
    private TextView tv_address;
    private TextView tv_amount;
    private TextView tv_remarkes;
    public TransferDialog(@NonNull Context context, int theme, int layout) {
        super(context, theme, layout);
    }

    @Override
    public void initUI() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay(); //获取屏幕宽高
        Point point = new Point();
        display.getSize(point);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes(); //获取当前对话框的参数值
        layoutParams.width = (int) (point.x * 0.9); //宽度设置为屏幕宽度的0.9
        layoutParams.height = (int) (point.y * 0.6); //高度设置为屏幕高度的0.6
        window.setGravity(Gravity.CENTER);
        window.setAttributes(layoutParams);

        et_pwd=findViewById(R.id.dialog_pwd);
        tv_address=findViewById(R.id.tv_re_address);
        tv_amount=findViewById(R.id.tv_send_amount);
        tv_remarkes=findViewById(R.id.tv_send_remarkes);
    }

    public String getEt_pwd(){
      return   et_pwd.getText().toString();
    }

    public void setTv_address(String tv_address) {
        this.tv_address.setText(tv_address);
    }

    public void setTv_amount(String tv_amount) {
        this.tv_amount.setText(tv_amount);
    }

    public void setTv_remarkes(String tv_remarkes) {
        this.tv_remarkes.setText(tv_remarkes);
    }
}
