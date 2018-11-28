package com.wayki.wallet.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wayki.wallet.R;
import com.wayki.wallet.activity.CommonActivity;
import com.wayki.wallet.bean.ExchangeBean;
import com.wayki.wallet.bean.Token2WiccBean;
import com.wayki.wallet.bean.WalletBalanceBean;
import com.wayki.wallet.bean.entity.GetContractEntity;
import com.wayki.wallet.bean.entity.RateEntity;
import com.wayki.wallet.dialog.ActiveDialog;
import com.wayki.wallet.dialog.BackupWallet;
import com.wayki.wallet.dialog.CreateWalletTipDialog;
import com.wayki.wallet.dialog.Dialog_full_qrcode;
import com.wayki.wallet.dialog.WalletDailog;
import com.wayki.wallet.dialog.Wicc_wkd_Dialog;
import com.wayki.wallet.dialog.Wkd_wicc_Dialog;
import com.wayki.wallet.mvp.exchangePresenter.ExchangePresenter;
import com.wayki.wallet.mvp.exchangePresenter.ExchangeView;
import com.wayki.wallet.utils.PermissionUtils;
import com.wayki.wallet.utils.SPConstants;
import com.wayki.wallet.utils.SPUtils;
import com.wayki.wallet.utils.UIUtils;
import com.wayki.wallet.utils.WalletUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bertsir.zbar.QrConfig;
import cn.bertsir.zbar.QrManager;

import static com.wayki.wallet.utils.Stringconstant.FRAGMENT_HISTORY_KEY;
import static com.wayki.wallet.utils.Stringconstant.FRAGMENT_KEY;
import static com.wayki.wallet.utils.Stringconstant.TRANSFER_ADDRESS;

public class WalletTabFragment extends BaseFragment<ExchangeView, ExchangePresenter> implements ExchangeView {
    @Bind(R.id.iv_wallet_detail)
    ImageView ivWalletDetail;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.iv_scan)
    ImageView ivScan;
    @Bind(R.id.iv_qrcode)
    ImageView ivQrcode;
    @Bind(R.id.iv_copy)
    ImageView ivCopy;
    @Bind(R.id.tv_input_title)
    TextView tvInputTitle;
    @Bind(R.id.et_input_amount1)
    EditText etInputAmount1;
    @Bind(R.id.tv_amount_select1)
    TextView tvAmountSelect1;
    @Bind(R.id.tv_input_title2)
    TextView tvInputTitle2;
    @Bind(R.id.et_input_amount2)
    TextView etInputAmount2;
    @Bind(R.id.tv_amount_select2)
    TextView tvAmountSelect2;
    @Bind(R.id.iv_exchange)
    ImageView ivExchange;
    @Bind(R.id.btn_exchange)
    Button btnExchange;
    @Bind(R.id.srl_main)
    SwipeRefreshLayout srlMain;
    @Bind(R.id.tv_rate)
    TextView tvRate;
    @Bind(R.id.tv_activity)
    TextView tvActivity;
    @Bind(R.id.tv_cny_num)
    TextView tvCnyNum;
    @Bind(R.id.tv_wicc_num)
    TextView tvWiccNum;
    @Bind(R.id.tv_wusd_num)
    TextView tvWusdNum;
    @Bind(R.id.tv_token_rate)
    TextView tvToken_rate;
    private RateEntity.DataBean rateEntity;
    private WalletBalanceBean walletBalanceBean;
    private int changeType = 0;
    private final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    // 打开相机请求Code
    private final int REQUEST_CODE_CAMERA = 1;
    private ArrayList<GetContractEntity.DataBean> dataBean;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_wallet_tab3;
    }

    @Override
    public String getEmptyText() {
        return null;
    }

    @Override
    public void initUI() {
        tvAddress.setText(WalletUtils.getInstance().getAddress(getContext()));
        String words = SPUtils.get(getContext(), SPConstants.WALLET_WORDS, "").toString();
        if ("".equals(words)) {
            tvActivity.setVisibility(View.INVISIBLE);
        } else {
            String regid = SPUtils.get(getContext(), SPConstants.REGID, "").toString();
            if ("".equals(regid)) {
                tvActivity.setText(Html.fromHtml("<u>" + getString(R.string.activation) + "</u>"));
            } else {
                tvActivity.setVisibility(View.INVISIBLE);
            }
        }

        //设置Input的类型两种都要
        etInputAmount1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        //设置字符过滤
        etInputAmount1.setFilters(new InputFilter[]{new InputFilter() {
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
                    if (changeType == 0) {
                        int demcal = 9;
                        if (length == demcal) {
                            return "";
                        }
                    } else if (changeType == 1) {
                        int demcal = 3;
                        if (length == demcal) {
                            return "";
                        }
                    }
                }
                return null;
            }
        }, new MyLengthFilter(8, getContext())});

        etInputAmount1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String am1 = etInputAmount1.getText().toString();
                if (!TextUtils.isEmpty(am1) && rateEntity != null) {
                    if (changeType == 0) {
                        Double amu1 = Double.parseDouble(am1);
                        etInputAmount2.setText(UIUtils.setTextDecimal2(amu1 *
                                rateEntity.getWicc2TokenRate() + ""));
                    } else if (changeType == 1) {
                        Double amu1 = Double.parseDouble(am1);
                        etInputAmount2.setText(UIUtils.setTextDecimal8(amu1 *
                                (1 / rateEntity.getWicc2TokenRate()) + ""));
                    }
                } else if (TextUtils.isEmpty(am1)) {
                    etInputAmount2.setText("");
                }
            }
        });

        srlMain.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (presenter != null) {
                    presenter.getAccountInfo();
                    presenter.getRate();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        tvAddress.setText(WalletUtils.getInstance().getAddress(getContext()));
        String words = SPUtils.get(getContext(), SPConstants.WALLET_WORDS, "").toString();
        if ("".equals(words)) {
            tvActivity.setVisibility(View.INVISIBLE);
        } else {
            String regid = SPUtils.get(getContext(), SPConstants.REGID, "").toString();
            if ("".equals(regid)) {
                tvActivity.setVisibility(View.VISIBLE);
                presenter.activeStatus();
                //tvActivity.setText(Html.fromHtml("<u>" + getString(R.string.activation) + "</u>"));
            } else {
                tvActivity.setVisibility(View.INVISIBLE);
            }
        }

        if (presenter != null) {
            presenter.getAccountInfo();
            presenter.getRate();
        }
    }

    @Override
    public ExchangePresenter createPresenter() {
        presenter = new ExchangePresenter(this, getContext());
        String regid = SPUtils.get(getContext(), SPConstants.REGID, "").toString();
        String addr = SPUtils.get(getContext(), SPConstants.WALLET_ADDRESS, "").toString();
        if (!"".equals(addr)) {
            if ("".equals(regid)) {
                presenter.activeStatus();
            }
        }
        presenter.getRate();
        presenter.getAccountInfo();
        presenter.getContractid();
        return presenter;
    }

    @Override
    public void onClickLoadFailText() {

    }

    @Override
    public void showNoNetError() {

    }

    @Override
    public void loadingDialog(String tip) {
        super.loadingDialog(tip);
    }

    @Override
    public void dismissDialog() {
        super.dismissDialog();
    }

    @OnClick({R.id.iv_copy, R.id.iv_wallet_detail, R.id.iv_scan, R.id.pll_wicc_record, R.id.pll_wusd_record,
            R.id.btn_exchange, R.id.iv_exchange, R.id.tv_history, R.id.iv_qrcode, R.id.tv_activity, R.id.tv_wallet_detail})
    public void onCick(View view) {
        int id = view.getId();
        if (UIUtils.checkBackup(id)) {
            /**
             * 判断是否备份
             * */
            final BackupWallet backupWallet1 = new BackupWallet(getActivity(),
                    R.style.DialogStyle, R.layout.dialog_backup);
            backupWallet1.addOnConfirmListener(new WalletDailog.onComfirmListener() {
                @Override
                public void onConfirm(WalletDailog dialog) {
                    backupWallet1.backupWords();
                }
            });
            backupWallet1.show();
            return;
        }

        /**检查是否导入助记词
         * */
        if (!checkWallet(id)) {
            return;
        }
        switch (id) {
            case R.id.iv_copy:
                String address = SPUtils.get(getContext(), SPConstants.WALLET_ADDRESS, "").toString();
                UIUtils.toClipboardManager(address);
                break;
            case R.id.iv_wallet_detail:
                Intent intent = new Intent(getActivity(), CommonActivity.class);
                intent.putExtra(FRAGMENT_KEY, 3);
                startActivity(intent);
                break;
            case R.id.iv_scan:
                PermissionUtils.checkAndRequestPermission(getContext(), WalletTabFragment.this, PERMISSION_CAMERA,
                        REQUEST_CODE_CAMERA,
                        new PermissionUtils.PermissionRequestSuccessCallBack() {
                            @Override
                            public void onHasPermission() {
                                // 权限已被授予
//                                Intent intentsacn=new Intent(getActivity(), ScanActivity.class);
//                                intentsacn.putExtra(Stringconstant.SCAN_VALUE,"transfer");
//                                startActivity(intentsacn);

                                start();
                            }
                        });

                break;
            case R.id.tv_history:
                Intent intent2 = new Intent(getActivity(), CommonActivity.class);
                intent2.putExtra(FRAGMENT_KEY, 0);
                startActivity(intent2);
                break;
            case R.id.btn_exchange:
                if (TextUtils.isEmpty(getAmount1()) || Double.parseDouble(getAmount1()) == 0) {
                    showTip(getString(R.string.amount_valid));
                    return;
                }
                if (walletBalanceBean != null) {
                    if (dataBean != null) {
                        if (changeType == 0) {
                            if (!UIUtils.checkWallet(getContext())) {
                                return;
                            }
                            String regid = SPUtils.get(getContext(), SPConstants.REGID, "").toString();
                            if ("".equals(regid)) {
                                presenter.activeStatus();
                                UIUtils.showToast(getString(R.string.wait_acti));
                                return;
                            }

                            if (Double.parseDouble(walletBalanceBean.getB_wicc()) >=
                                    Double.parseDouble(getAmount1())) {
                                // if (!"".equals(regid)) {
                                confirmDialog1();
                               /* } else {
                                    presenter.activeStatus();
                                    UIUtils.showToast(getString(R.string.wait_acti));
                                }*/
                            } else {
                                showTip(getString(R.string.amount_not_enough));
                            }
                        } else {
                            if (Double.parseDouble(walletBalanceBean.getB_wusd()) >= Double.parseDouble(getAmount1())) {
                                confirmDialog2();
                            } else {
                                showTip(getString(R.string.amount_not_enough));
                            }
                        }
                    } else {
                        presenter.getContractid();
                        showTip(getString(R.string.network_error));
                    }
                } else {
                    presenter.getAccountInfo();
                    showTip(getString(R.string.amount_not_enough));
                }
                break;

            case R.id.pll_wicc_record:
                Intent intent1 = new Intent(getActivity(), CommonActivity.class);
                intent1.putExtra(FRAGMENT_KEY, 1);
                intent1.putExtra(FRAGMENT_HISTORY_KEY, 0);
                startActivity(intent1);
                break;
            case R.id.pll_wusd_record:
                Intent inten = new Intent(getActivity(), CommonActivity.class);
                inten.putExtra(FRAGMENT_KEY, 1);
                inten.putExtra(FRAGMENT_HISTORY_KEY, 1);
                startActivity(inten);
                break;
            case R.id.iv_exchange:
                // changeType = (changeType == 0 ? 1 : 0);
                // Animation circle_anim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_round_rotate);
                // LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
                // circle_anim.setInterpolator(interpolator);
                // if (circle_anim != null) {
                //     ivExchange.startAnimation(circle_anim);  //开始动画
                // }

                tvAmountSelect1.setText(changeType == 0 ? getString(R.string.wicc) : getString(R.string.wusd));
                // tvAmountSelect2.setText(changeType == 0 ? getString(R.string.wusd) : getString(R.string.wicc));
                if (rateEntity != null) {
                    setRateText(rateEntity);
                } else {
                    presenter.getRate();
                }
                break;
            case R.id.iv_qrcode:
                Dialog_full_qrcode dialog_full_qrcode = new Dialog_full_qrcode(getContext());
                dialog_full_qrcode.show();
                break;
            case R.id.tv_activity:/*激活*/
                if (walletBalanceBean != null) {
                    if (Double.parseDouble(walletBalanceBean.getB_wicc()) >=
                            0.0001) {
                        ActiveDialog activeDialog = new ActiveDialog(getActivity(),
                                R.style.DialogStyle, R.layout.dialog_active);
                        activeDialog.addOnConfirmListener(new WalletDailog.onComfirmListener() {
                            @Override
                            public void onConfirm(WalletDailog dialog) {
                                if (presenter != null) {
                                    ActiveDialog activeDialog = (ActiveDialog) dialog;
                                    ExchangeBean exchangeBean = new ExchangeBean();
                                    exchangeBean.setPwd(activeDialog.getPwd());
                                    exchangeBean.setType("100");

                                    presenter.transExchange(exchangeBean);

                                }
                                dialog.dismiss();
                            }
                        });
                        activeDialog.show();
                    } else {
                        presenter.getAccountInfo();
                        showTip(getString(R.string.amount_not_enough));
                    }
                } else {
                    presenter.getAccountInfo();
                    showTip(getString(R.string.amount_not_enough));
                }
                break;
            case R.id.tv_wallet_detail:
                presenter.getWicc();
                break;
        }
    }

    public boolean checkWallet(int id) {

        if (id == R.id.iv_wallet_detail || id == R.id.tv_history
                || id == R.id.iv_exchange || id == R.id.btn_exchange) {
            return true;
        }

        String words = SPUtils.get(getContext(), SPConstants.WALLET_WORDS, "").toString();
        if ("".equals(words)) {
            // showTip(getString(R.string.pls_import_create));
          /*  CreateWalletTipDialog walletDailog = new CreateWalletTipDialog(getContext(),
                    R.style.DialogStyle, R.layout.dialog_createwallet_tip);
            walletDailog.addOnConfirmListener(new WalletDailog.onComfirmListener() {
                @Override
                public void onConfirm(WalletDailog dialog) {
                    dialog.dismiss();
                    Intent intent = new Intent(getContext(), CommonActivity.class);
                    intent.putExtra(FRAGMENT_KEY, 4);
                    getContext().startActivity(intent);
                }
            });

            walletDailog.addOnCancelListener(new WalletDailog.onCancelListener() {
                @Override
                public void onCancel(WalletDailog dialog) {
                    dialog.dismiss();
                    Intent intent = new Intent(getContext(), CommonActivity.class);
                    intent.putExtra(FRAGMENT_KEY, 5);
                    getContext().startActivity(intent);
                }
            });

            walletDailog.show();
            walletDailog.setCreateWalletTip();*/

            CreateWalletTipDialog walletDailog = new CreateWalletTipDialog(getContext(),
                    R.style.DialogStyle, R.layout.dialog_createwallet_tip);
            walletDailog.addOnConfirmListener(new WalletDailog.onComfirmListener() {
                @Override
                public void onConfirm(WalletDailog dialog) {
                    dialog.dismiss();
                    Intent intent = new Intent(getContext(), CommonActivity.class);
                    intent.putExtra(FRAGMENT_KEY, 5);
                    getActivity().startActivity(intent);
                }
            });

            walletDailog.addOnCancelListener(new WalletDailog.onCancelListener() {
                @Override
                public void onCancel(WalletDailog dialog) {

                    dialog.dismiss();
                    Intent intent = new Intent(getContext(), CommonActivity.class);
                    intent.putExtra(FRAGMENT_KEY, 4);
                    getActivity().startActivity(intent);

                }
            });

            walletDailog.show();
            walletDailog.setCreateWalletTip2();

            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (PermissionUtils.isPermissionRequestSuccess(grantResults)) {
                    // 权限已被授予
//                    Intent intentsacn=new Intent(getActivity(), ScanActivity.class);
//                    intentsacn.putExtra(Stringconstant.SCAN_VALUE,"transfer");
//                    startActivity(intentsacn);

                    start();
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
                .setDingPath(R.raw.qrcode)//设置提示音(不设置为默认的Ding~)
                .setIsOnlyCenter(true)//是否只识别框中内容(默认为全屏识别)
                .setTitleText(getString(R.string.qrcode))//设置Tilte文字
                .setTitleBackgroudColor(getContext().getResources().getColor(R.color.colorPrimary))//设置状态栏颜色
                .setTitleTextColor(Color.WHITE)//设置Title文字颜色
                .create();
        QrManager.getInstance().init(qrConfig).startScan(getActivity(), new QrManager.OnScanResultCallback() {
            @Override
            public void onScanSuccess(String result) {
                Intent intent1 = new Intent(getContext(), CommonActivity.class);
                intent1.putExtra(FRAGMENT_KEY, 2);
                intent1.putExtra(TRANSFER_ADDRESS, result);
                startActivity(intent1);
            }
        });
    }

    /**
     * WICC 兑换 WKD
     */
    private void confirmDialog1() {
        Wicc_wkd_Dialog walletDialog = new Wicc_wkd_Dialog(getActivity(),
                R.style.DialogStyle, R.layout.dialog_wicc_wkd);
        walletDialog.addOnConfirmListener(new WalletDailog.onComfirmListener() {
            @Override
            public void onConfirm(WalletDailog dialog) {
                Wicc_wkd_Dialog wkdDialog = (Wicc_wkd_Dialog) dialog;
                wkdDialog.setAmount(getAmount1());
                wkdDialog.setAccount(walletBalanceBean.getB_wicc());
                ExchangeBean exchangeBean = new ExchangeBean();
                exchangeBean.setAmount1(getAmount1());
                exchangeBean.setAmount2(UIUtils.getMoney4("" + Double.parseDouble(getAmount1()) *
                        Double.parseDouble(UIUtils.getMoney4(rateEntity.getWicc2TokenRate() + ""))));
                int progress = wkdDialog.getProgress();

                double min = 0.00000001;//最小值
                double max = 0.00001;
                int scl = 8;//小数最大位数
                int pow = (int) Math.pow(10, scl);//指定小数位
                double one = Math.floor((Math.random() * (max - min) + min) * pow) / pow;

                double mTips = (0.05000000 - 0.0010000) * progress / 100 + 0.0010000 + one;
                exchangeBean.setFee(UIUtils.setTextDecimal8(mTips + ""));
                exchangeBean.setPwd(wkdDialog.getPwd());
                exchangeBean.setType("200");
                exchangeBean.setRate(UIUtils.getMoney4(rateEntity.getWicc2TokenRate() + ""));
                exchangeBean.setDestAddr(dataBean.get(0).getContractAddress());
                exchangeBean.setDestAddrid(dataBean.get(0).getContractAddressRegId());

                double acco = Double.parseDouble(walletBalanceBean.getB_wicc());
                if (acco >= mTips + Double.parseDouble(getAmount1())) {
                    if (presenter != null) {
                        presenter.transExchange(exchangeBean);
                    }
                    dialog.dismiss();
                } else {
                    showTip(getString(R.string.amount_not_enough));
                }
            }
        });

        walletDialog.show();
        walletDialog.setTips(getAmount1() + getString(R.string.wicc), getAmount2() + getString(R.string.wusd));
    }

    /**
     * WKD 兑换 WICC
     */
    private void confirmDialog2() {
        String addr = SPUtils.get(getContext(), SPConstants.WALLET_ADDRESS, "").toString();
        final Token2WiccBean token2WiccBean = new Token2WiccBean();
        token2WiccBean.setContractAddress(dataBean.get(0).getContractAddress());
        token2WiccBean.setToken(getContext().getString(R.string.wusd));
        token2WiccBean.setPrice(1 / (1 / rateEntity.getWicc2TokenRate()));
        token2WiccBean.setTokenAmount(Double.parseDouble(getAmount1()));
        token2WiccBean.setTxTokenFee(0);
        token2WiccBean.setWiccAddress(addr);
        token2WiccBean.setWiccCount(Double.parseDouble(getAmount1()) * (1 / rateEntity.getWicc2TokenRate()));
        token2WiccBean.setDirection(100);
        Wkd_wicc_Dialog walletDialog = new Wkd_wicc_Dialog(getActivity(), R.style.DialogStyle
                , R.layout.dialog_wkd_wicc);
        walletDialog.addOnConfirmListener(new WalletDailog.onComfirmListener() {
            @Override
            public void onConfirm(WalletDailog dialog) {
                presenter.token2Wicc(token2WiccBean);
                dialog.dismiss();
            }
        });

        walletDialog.show();
        walletDialog.setTips(getAmount1() + getString(R.string.wusd), getAmount2() + getString(R.string.wicc));
    }


    @Override
    public void onDestroyView() {
        if (presenter != null) {
            presenter.detachView(true);
        }
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void setRateText(RateEntity.DataBean entity) {
        rateEntity = entity;
        tvRate.setText(changeType == 0 ? ("1 " + getString(R.string.wicc) + " = " + UIUtils.getMoney2(entity.getWicc2TokenRate() + "") + " " + getString(R.string.wusd)) :
                ("1 " + getString(R.string.wusd)) + " = " + UIUtils.setTextDecimal8((1 / entity.getWicc2TokenRate()) + "") + " " + getString(R.string.wicc));
        etInputAmount1.setText(etInputAmount1.getText().toString());
    }

    @Override
    public void setActive(boolean isac) {
        String words = SPUtils.get(getContext(), SPConstants.WALLET_WORDS, "").toString();
        if (isac || "".equals(words)) {
            tvActivity.setVisibility(View.INVISIBLE);
        } else {
            tvActivity.setVisibility(View.VISIBLE);
            tvActivity.setText(Html.fromHtml("<u>" + getString(R.string.activation) + "</u>"));
        }
    }

    @Override
    public void setBalance(WalletBalanceBean balance) {
        walletBalanceBean = balance;
        tvToken_rate.setText(" = "+UIUtils.setTextDecimal2(balance.getWusd_price())+"CNY");
        tvCnyNum.setText("≈ " + UIUtils.setTextDecimal2(balance.getB_amount() + "") + "CNY");
        tvWiccNum.setText(UIUtils.setTextDecimal8(balance.getB_wicc()) + " ");
        tvWusdNum.setText(UIUtils.setTextDecimal2(balance.getB_wusd()) + " ");
    }

    @Override
    public void setContract(ArrayList<GetContractEntity.DataBean> databean) {
        this.dataBean = databean;
    }

    @Override
    public String getAmount1() {
        return etInputAmount1.getText().toString().trim();
    }

    @Override
    public String getAmount2() {
        return etInputAmount2.getText().toString().trim();
    }

    @Override
    public void dismissLoading() {
        if (srlMain != null && srlMain.isRefreshing()) {
            srlMain.setRefreshing(false);
        }
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
