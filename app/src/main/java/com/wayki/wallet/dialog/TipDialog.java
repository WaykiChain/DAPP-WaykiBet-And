package com.wayki.wallet.dialog;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class TipDialog extends WalletDailog {
    private Double height= 0.28;

    public TipDialog(@NonNull Context context, int theme, int layoutid,double heignt) {
        super(context, theme, layoutid);
        this.height=heignt;
    }

    public TipDialog(@NonNull Context context, int theme, int layoutid) {
        super(context, theme, layoutid);
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
        layoutParams.height = (int) (point.y * height); //高度设置为屏幕高度的0.5
//        layoutParams.width = (int) (display.getWidth() * 0.5);
//        layoutParams.height = (int) (display.getHeight() * 0.5);
        window.setGravity(Gravity.CENTER);
        window.setAttributes(layoutParams);
    }
}
