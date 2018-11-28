package com.wayki.wallet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wayki.wallet.R;
import com.wayki.wallet.activity.CommonActivity;
import com.wayki.wallet.bean.CommonBean;
import com.wayki.wallet.dialog.BackupWallet;
import com.wayki.wallet.dialog.CreateWalletTipDialog;
import com.wayki.wallet.dialog.WalletDailog;
import com.wayki.wallet.ui.DividerGridItemDecoration;
import com.wayki.wallet.utils.SPConstants;
import com.wayki.wallet.utils.SPUtils;
import com.wayki.wallet.utils.UIUtils;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.wayki.wallet.utils.Stringconstant.FRAGMENT_KEY;

public class WalletManagerFragment extends Fragment {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ll_common_title)
    PercentLinearLayout llCommonTitle;
    @Bind(R.id.rv_main)
    RecyclerView rvMain;
    @Bind(R.id.swprl)
    SwipeRefreshLayout swprl;
    @Bind(R.id.tv_bottom)
    TextView tvBottom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_common_rv, null, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tvTitle.setText(getString(R.string.management_wallet));
        tvBottom.setVisibility(View.INVISIBLE);
        swprl.setRefreshing(false);
        swprl.setEnabled(false);
        llCommonTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        ArrayList<CommonBean> lists = new ArrayList<CommonBean>(Arrays.asList(
                new CommonBean(getContext().getResources().getDrawable(R.drawable.backup_wallet), getString(R.string.backup_words)),
                new CommonBean(getContext().getResources().getDrawable(R.drawable.export_wallet), getString(R.string.export_private_key)),
                new CommonBean(getContext().getResources().getDrawable(R.drawable.import_wallet), getString(R.string.import_wallet)),
                new CommonBean(getContext().getResources().getDrawable(R.drawable.create_wallet), getString(R.string.create_wallet)),
                new CommonBean(getContext().getResources().getDrawable(R.drawable.change_pwd), getString(R.string.change_password))
        ));
        rvMain.setLayoutManager(new GridLayoutManager(getContext(), 2));
        BaseQuickAdapter quickAdapter = new BaseQuickAdapter(R.layout.item_walletmanager, lists) {
            @Override
            protected void convert(final BaseViewHolder helper, Object o) {
                CommonBean item = (CommonBean) o;
                helper.setText(R.id.textView, item.getName());
                helper.setImageDrawable(R.id.imageView, item.getImage());
                helper.setOnClickListener(R.id.pll_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (helper.getAdapterPosition()) {
                            case 3:
                                final Intent intent4 = new Intent(getActivity(), CommonActivity.class);
                                intent4.putExtra(FRAGMENT_KEY, 4);
                                String words = SPUtils.get(mContext, SPConstants.WALLET_WORDS, "").toString();
                                if (!"".equals(words)) {
                                    CreateWalletTipDialog walletDailog = new CreateWalletTipDialog(getActivity(),
                                            R.style.DialogStyle, R.layout.dialog_createwallet_tip);
                                    walletDailog.addOnConfirmListener(new WalletDailog.onComfirmListener() {
                                        @Override
                                        public void onConfirm(WalletDailog dialog) {

                                            startActivity(intent4);
                                            dialog.dismiss();
                                        }
                                    });

                                    walletDailog.show();
                                } else {
                                    startActivity(intent4);
                                }
                                break;
                            case 1:
                                if(!UIUtils.checkWallet(getContext())){
                                    return;
                                }

                                final BackupWallet backupWallet1 = new BackupWallet(getActivity(),
                                        R.style.DialogStyle, R.layout.dialog_input_privatekey);
                                backupWallet1.addOnConfirmListener(new WalletDailog.onComfirmListener() {
                                    @Override
                                    public void onConfirm(WalletDailog dialog) {
                                        backupWallet1.exportPK();
                                    }
                                });

                                backupWallet1.show();
                                break;
                            case 2:
                                final Intent intent2 = new Intent(getActivity(), CommonActivity.class);
                                intent2.putExtra(FRAGMENT_KEY, 5);
                                String wordss = SPUtils.get(mContext, SPConstants.WALLET_WORDS, "").toString();
                                if (!"".equals(wordss)) {
                                    CreateWalletTipDialog walletDailog = new CreateWalletTipDialog(getActivity(),
                                            R.style.DialogStyle, R.layout.dialog_createwallet_tip);
                                    walletDailog.addOnConfirmListener(new WalletDailog.onComfirmListener() {
                                        @Override
                                        public void onConfirm(WalletDailog dialog) {

                                            startActivity(intent2);
                                            dialog.dismiss();
                                        }
                                    });

                                    walletDailog.show();
                                    walletDailog.setCoverWalletText();
                                } else {
                                    startActivity(intent2);
                                }


                                break;
                            case 0:
                                if(!UIUtils.checkWallet(getContext())){
                                    return;
                                }
                                final BackupWallet backupWallet = new BackupWallet(getActivity(),
                                        R.style.DialogStyle, R.layout.dialog_backup);
                                backupWallet.addOnConfirmListener(new WalletDailog.onComfirmListener() {
                                    @Override
                                    public void onConfirm(WalletDailog dialog) {
                                        backupWallet.exportWords();
                                    }
                                });

                                backupWallet.show();
                                break;
                            case 4:
                                if(!UIUtils.checkWallet(getContext())){
                                    return;
                                }
                                Intent intent6= new Intent(getActivity(), CommonActivity.class);
                                intent6.putExtra(FRAGMENT_KEY, 6);
                                startActivity(intent6);
                                break;
                        }
                    }
                });
            }

        };
        rvMain.addItemDecoration(new DividerGridItemDecoration(
                getContext()));
//        rvMain.addItemDecoration(new DividerItemDecoration(
//                getContext(), DividerItemDecoration.VERTICAL));
        rvMain.setAdapter(quickAdapter);
    }


    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
