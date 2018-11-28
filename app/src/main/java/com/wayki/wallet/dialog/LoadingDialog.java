package com.wayki.wallet.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.wayki.wallet.R;
import com.wayki.wallet.ui.loading.AVLoadingIndicatorView;
import com.wayki.wallet.utils.UIUtils;


/**
 * Created by Simon on 7/14/2018.
 */

public class LoadingDialog extends Dialog {

    private Context context;
    private static LoadingDialog dialog;

    public LoadingDialog(Context context) {
        super(context);
        this.context = context;
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;

    }
    //显示dialog的方法
    public static LoadingDialog showDialog(Context context){
        dialog = new LoadingDialog(context, R.style.LoadingDialog);//dialog样式
        dialog.setContentView(R.layout.dialog_loading);//dialog布局文件
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        AVLoadingIndicatorView indicatorView = (AVLoadingIndicatorView) dialog.findViewById(R.id.indicator);
        indicatorView.setIndicator("BallClipRotateMultipleIndicator");
        indicatorView.getIndicator().setColor(UIUtils.getColor(R.color.colorBluePrimaryDark));
        return dialog;
    }

    public   void setTip(String str){
        TextView tip = (TextView) dialog.findViewById(R.id.tv_tip);
        tip.setText(str);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && dialog != null){
            //AVLoadingIndicatorView indicatorView = (AVLoadingIndicatorView) dialog.findViewById(R.id.indicator);
            // indicatorView.setIndicator("LineSpinFadeLoaderIndicator");
        }
    }
}

