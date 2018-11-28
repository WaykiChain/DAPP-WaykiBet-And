package com.wayki.wallet.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wayki.wallet.R;
import com.wayki.wallet.activity.CommonActivity;
import com.wayki.wallet.bean.entity.AccountInfoEntity;
import com.wayki.wallet.bean.entity.TradeDetailEntity;
import com.wayki.wallet.dialog.TransferDialog;
import com.wayki.wallet.dialog.WalletDailog;
import com.wayki.wallet.mvp.transferpresenter.TransferPresenter;
import com.wayki.wallet.mvp.transferpresenter.TransferView;
import com.wayki.wallet.ui.ImageEditText;
import com.wayki.wallet.utils.PermissionUtils;
import com.wayki.wallet.utils.Stringconstant;
import com.wayki.wallet.utils.UIUtils;
import com.zhy.android.percent.support.PercentLinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bertsir.zbar.QrConfig;
import cn.bertsir.zbar.QrManager;

import static com.wayki.wallet.utils.Stringconstant.FRAGMENT_KEY;
import static com.wayki.wallet.utils.Stringconstant.FRAGMENT_VALUE;
import static com.wayki.wallet.utils.Stringconstant.TRANSFER_ADDRESS;

public class TransferFragment extends BaseFragment<TransferView, TransferPresenter> implements
        TransferView {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ll_common_title)
    PercentLinearLayout llCommonTitle;
    @Bind(R.id.tv_amuont)
    TextView tvAmuont;
    @Bind(R.id.iv_title_right)
    ImageView ivTitleRight;
    @Bind(R.id.tv_address)
    ImageEditText tvAddress;
    @Bind(R.id.tv_amount)
    ImageEditText tvAmount;
    @Bind(R.id.tv_remarkes)
    ImageEditText tvRamarkes;
    @Bind(R.id.tv_fee)
    TextView tvFee;
    @Bind(R.id.sb_fee)
    AppCompatSeekBar sbFee;
    @Bind(R.id.tv_slow)
    TextView tvSlow;
    @Bind(R.id.btn_send)
    Button btnSend;
    private int  requestCode=1;

    private AccountInfoEntity.DataBean dataBean;

    private final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    // 打开相机请求Code
    private final int REQUEST_CODE_CAMERA = 1;
    private String address="";
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_transfer;
    }

    @Override
    public String getEmptyText() {
        return null;
    }

    @Override
    public void initUI() {
        tvTitle.setText(getString(R.string.wicc_transfer));
        address=this.getArguments().getString(FRAGMENT_VALUE);
        tvAddress.setText(address);

        llCommonTitle.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

        tvFee.setText(getContext().getString(R.string.kgfy)+":" + 0.001+" "+getContext().getString(R.string.wicc));
       sbFee.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               double mTips = (0.05000000 - 0.0010000) * progress / 100 + 0.0010000;
               tvFee.setText(getContext().getString(R.string.kgfy)+":" + UIUtils.setTextDecimal4((mTips)+"")+" "+getContext().getString(R.string.wicc));

           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {

           }
       });


       tvAddress.setRightPicOnclickListener(new ImageEditText.RightPicOnclickListener() {
           @Override
           public void rightPicClick() {

               PermissionUtils.checkAndRequestPermission(getContext(), TransferFragment.this, PERMISSION_CAMERA,
                       REQUEST_CODE_CAMERA,
                       new PermissionUtils.PermissionRequestSuccessCallBack() {
                           @Override
                           public void onHasPermission() {
                               start();
                               // 权限已被授予
                               // Intent intent=new Intent(getActivity(), ScanActivity.class);
                               // TransferFragment.this.startActivityForResult(intent,requestCode);
                           }
                       });
           }
       });

        //设置Input的类型两种都要
        tvAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        //设置字符过滤
        tvAmount.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                if (source.equals(".") && dest.toString().contains(".")) {
                    return "";
                }

                if ((source.equals(".") || source.equals("0")) && dest.toString().length() == 0) {
                    return "0.";
                }

                if (dest.toString().contains(".")) {
                    int index = dest.toString().indexOf(".");
                    int length = dest.toString().substring(index).length();
                        int demcal = 9;
                        if (length == demcal) {
                            return "";
                        }
                }
                return null;
            }
        }, new MyLengthFilter(10, getContext())});

    }

    @Override
    public TransferPresenter createPresenter() {
        presenter = new TransferPresenter(this, getContext());
        presenter.getAccountInfo();
        return presenter;
    }

    @Override
    public void onClickLoadFailText() {

    }

    @Override
    public void showNoNetError() {

    }

    @Override
    public void loadDataSuccess(Object o) {

    }

    @Override
    public void loadDataFail(String failcode) {

    }

    @Override
    public void setAmount(AccountInfoEntity.DataBean amount) {
        dataBean=amount;
        tvAmuont.setText(amount.getCustomerAccountList().get(0)
                .getBalanceAvailable() + " " +
                UIUtils.getContext().getResources().getString(R.string.wicc));
    }

    @Override
    public void tradeDetail(TradeDetailEntity detailEntity) {

    }


    @Override
    public void onDestroyView() {
        presenter.detachView(true);
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @OnClick({R.id.iv_back, R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.btn_send:
                if(presenter!=null){
                    if(dataBean!=null){
                    if(Double.parseDouble(getAmount())>=Double.parseDouble(
                            dataBean.getCustomerAccountList().get(0)
                                    .getBalanceAvailable()+"")){
                        showTip(getString(R.string.amount_not_enough));
                    }else {
                        presenter.checkMsg();

                    }
                    }else{
                        presenter.getAccountInfo();
                    }
                }
                break;

        }
    }


    @Override
    public String getAddress() {
        return tvAddress.getText().toString();
    }

    @Override
    public String getAmount() {
        return tvAmount.getText().toString().trim();
    }

    @Override
    public String getRemarkr() {
      return   tvRamarkes.getText().toString();
    }

    @Override
    public String getFee() {
        double min = 0.00000001;//最小值
        double max = 0.00001;
        int scl = 8;//小数最大位数
        int pow = (int) Math.pow(10, scl);//指定小数位
        double one = Math.floor((Math.random() * (max - min) + min) * pow) / pow;
        double mTips = (0.05000000 - 0.0010000) * sbFee.getProgress() / 100 + 0.0010000+one;
        return UIUtils.setTextDecimal8(mTips+"");
    }

    @Override
    public void showTransDialog() {
        TransferDialog transferDialog = new TransferDialog(getActivity(), R.style.DialogStyle
                , R.layout.dialog_transfer_confirm);
        transferDialog.addOnConfirmListener(new WalletDailog.onComfirmListener() {
            @Override
            public void onConfirm(WalletDailog dialog) {
                TransferDialog transfer=(TransferDialog)dialog;
                if(presenter!=null){
                    presenter.sendTXdata(transfer.getEt_pwd());
                }
                dialog.dismiss();
            }
        });
        transferDialog.show();
        transferDialog.setTv_address(getAddress());
        transferDialog.setTv_amount(getAmount());
        transferDialog.setTv_remarkes(getRemarkr());
    }

  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null) {
            String addr = data.getStringExtra(Stringconstant.QRCODE);
            if (addr != null) {
                tvAddress.setText(addr);
            }
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (PermissionUtils.isPermissionRequestSuccess(grantResults)) {

                    start();
                    // 权限申请成功
                   // Intent intent=new Intent(getActivity(), ScanActivity.class);
                   // TransferFragment.this.startActivityForResult(intent,requestCode);
                } else {
                    UIUtils.showToast(getString(R.string.open_camer));
                }
                break;
        }
    }

    private void start() {
        int scan_type = 0;
        int scan_view_type = 0;
        //if (rb_all.isChecked()) {
        //  scan_type = QrConfig.TYPE_ALL;
        //  scan_view_type = QrConfig.SCANVIEW_TYPE_QRCODE;
        // } else if (rb_qrcode.isChecked()) {
        scan_type = QrConfig.TYPE_QRCODE;
        scan_view_type = QrConfig.SCANVIEW_TYPE_QRCODE;
        //  } else if (rb_bcode.isChecked()) {
        //   scan_type = QrConfig.TYPE_BARCODE;
        //   scan_view_type = QrConfig.SCANVIEW_TYPE_BARCODE;
        //}
        QrConfig qrConfig = new QrConfig.Builder()
                .setDesText("")//扫描框下文字
                .setShowDes(true)//是否显示扫描框下面文字
                .setShowLight(true)//显示手电筒按钮
                .setShowTitle(true)//显示Title
                .setShowAlbum(true)//显示从相册选择按钮
                .setCornerColor(Color.parseColor("#E42E30"))//设置扫描框颜色
                .setLineColor(getContext().getResources().getColor(R.color.colorAccent))//设置扫描线颜色
                .setLineSpeed(QrConfig.LINE_MEDIUM)//设置扫描线速度
                .setScanType(scan_type)//设置扫码类型（二维码，条形码，全部，自定义，默认为二维码）
                .setScanViewType(scan_view_type)//设置扫描框类型（二维码还是条形码，默认为二维码）
                .setCustombarcodeformat(QrConfig.BARCODE_EAN13)//此项只有在扫码类型为TYPE_CUSTOM时才有效
                .setPlaySound(true)//是否扫描成功后bi~的声音
                .setDingPath( R.raw.qrcode)//设置提示音(不设置为默认的Ding~)
                .setIsOnlyCenter(true)//是否只识别框中内容(默认为全屏识别)
                .setTitleText(getString(R.string.qrcode))//设置Tilte文字
                .setTitleBackgroudColor(getContext().getResources().getColor(R.color.colorPrimary))//设置状态栏颜色
                .setTitleTextColor(Color.WHITE)//设置Title文字颜色
                .create();
        QrManager.getInstance().init(qrConfig).startScan(getActivity(), new QrManager.OnScanResultCallback() {
            @Override
            public void onScanSuccess(String result) {
                tvAddress.setText(result);
            }
        });
    }

    class MyLengthFilter implements InputFilter {

        private final int mMax;
        private Context context;

        public MyLengthFilter(int max, Context context) {
            mMax = max;
            this.context = context;
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart, int dend) {
            int keep = mMax - (dest.length() - (dend - dstart));
            if (keep <= 0) {
                //这里，用来给用户提示
                //  etInputAmount1.setText("最大输入数字不超过" + mMax);
                return "";
            } else if (keep >= end - start) {
                if (keep == 1 && ".".equals(source)) {
                    return "";
                }
                return null; // keep original
            } else {
                keep += start;
                if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                    --keep;
                    if (keep == start) {
                        return "";
                    }
                }
                return source.subSequence(start, keep);
            }
        }

        /**
         * @return the maximum length enforced by this input filter
         */
        public int getMax() {
            return mMax;
        }
    }
}
