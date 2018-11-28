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
import android.widget.ImageView;
import android.widget.TextView;

import com.wayki.wallet.R;
import com.wayki.wallet.bean.entity.TradeDetailEntity;
import com.zhy.android.percent.support.PercentLinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Tradedetail_Dialog extends AlertDialog {
    public Context mContext;
    @Bind(R.id.iv_close)
    ImageView ivClose;
    @Bind(R.id.tv_trade_amount)
    TextView tvTradeAmount;
    @Bind(R.id.tv_trade_status)
    TextView tvTradeStatus;
    @Bind(R.id.tv_trade_type)
    TextView tvTradeType;
    @Bind(R.id.tv_type)
    TextView tvType;
    @Bind(R.id.tv_trade_time)
    TextView tvTradeTime;
    @Bind(R.id.pll_bg)
    PercentLinearLayout pllBg;
    @Bind(R.id.iv_trade_status)
    ImageView ivTradeStatus;
    TradeDetailEntity detailEntity;
    @Bind(R.id.tv_sender)
    TextView tvSender;
    @Bind(R.id.tv_receiver)
    TextView tvReceiver;
    @Bind(R.id.tv_fee)
    TextView tvFee;
    @Bind(R.id.tv_hash)
    TextView tvHash;
    @Bind(R.id.tv_remark)
    TextView tvRemark;

    public Tradedetail_Dialog(@NonNull Context context, TradeDetailEntity detailEntity) {
        super(context);
        this.mContext = context;
        this.detailEntity = detailEntity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_trade_datail);
        ButterKnife.bind(this, getWindow().getDecorView());
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay(); //获取屏幕宽高
        Point point = new Point();
        display.getSize(point);

        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes(); //获取当前对话框的参数值
        layoutParams.width = (int) (point.x * 0.90); //宽度设置为屏幕宽度的0.9
        layoutParams.height = (int) (point.y * 0.80); //高度设置为屏幕高度的0.5
//        layoutParams.width = (int) (display.getWidth() * 0.5);
//        layoutParams.height = (int) (display.getHeight() * 0.5);
        window.setGravity(Gravity.CENTER);
        window.setAttributes(layoutParams);

        tvTradeAmount.setText(detailEntity.getData().getAmount()+"");
        tvSender.setText(detailEntity.getData().getSendAddress()+"");
        tvReceiver.setText(detailEntity.getData().getReceiveAddress()+"");
        tvFee.setText(detailEntity.getData().getMinnerFee()+"");
        tvHash.setText(detailEntity.getData().getTxHash()+"");
        tvRemark.setText(detailEntity.getData().getComments()+"");
        tvType.setText(detailEntity.getData().getType()+"");
        tvTradeTime.setText(detailEntity.getData().getTxTime()+"");
    }

    @OnClick({R.id.iv_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
        }
    }


    @Override
    public void dismiss() {
        super.dismiss();
        ButterKnife.unbind(this);
    }
}
