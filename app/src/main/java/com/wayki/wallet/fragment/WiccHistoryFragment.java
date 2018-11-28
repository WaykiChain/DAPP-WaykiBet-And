package com.wayki.wallet.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import com.wayki.wallet.bean.HistoryListBean;
import com.wayki.wallet.bean.TradeHistoryBean;
import com.wayki.wallet.bean.entity.AccountInfoEntity;
import com.wayki.wallet.bean.entity.TradeDetailEntity;
import com.wayki.wallet.bean.entity.TradeHistoryEntity;
import com.wayki.wallet.dialog.Dialog_full_qrcode;
import com.wayki.wallet.dialog.Tradedetail_Dialog;
import com.wayki.wallet.mvp.historypresenter.HistoryPresenter;
import com.wayki.wallet.mvp.historypresenter.HistoryView;
import com.wayki.wallet.ui.ArrayPopuWindows;
import com.wayki.wallet.utils.SPConstants;
import com.wayki.wallet.utils.SPUtils;
import com.wayki.wallet.utils.UIUtils;
import com.wayki.wallet.utils.WalletUtils;
import com.zhy.android.percent.support.PercentLinearLayout;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.wayki.wallet.utils.Stringconstant.FRAGMENT_KEY;
import static com.wayki.wallet.utils.Stringconstant.FRAGMENT_VALUE;

public class WiccHistoryFragment extends BaseFragment<HistoryView, HistoryPresenter> implements
        HistoryView, View.OnClickListener {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rv_main)
    RecyclerView rvMain;
    @Bind(R.id.swprl)
    SwipeRefreshLayout swprl;
    @Bind(R.id.ll_common_title)
    PercentLinearLayout llCommonTitle;
    @Bind(R.id.btn_qrcode)
    PercentLinearLayout btnQrcode;
    @Bind(R.id.btn_send)
    PercentLinearLayout btnSend;
    ArrayList<TradeHistoryBean> lists = new ArrayList<TradeHistoryBean>();
    BaseQuickAdapter quickAdapter;
    @Bind(R.id.pll_history_bottom)
    PercentRelativeLayout pllHistoryBottom;
    @Bind(R.id.pll_nodata_tip)
    PercentLinearLayout tvNodataTip;
    private int type = 0;
    TextView textlist;
    private TextView tv_amount;
    private TextView tv_amount_cny;
    private int pageNum = 1;
    private ArrayList<String> arrs;
    private String fragment_value;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_wicc;
    }

    @Override
    public String getEmptyText() {
        return null;
    }

    @Override
    public void initUI() {
        fragment_value = getArguments().getString(FRAGMENT_VALUE);
        if ("0".equals(fragment_value)) {
            tvTitle.setText(getString(R.string.wicc));
            arrs = new ArrayList<String>(Arrays.asList(getString(R.string.all),
                    getString(R.string.transfer),
                    getString(R.string.exchange),
                    getString(R.string.activation)));
            //   getString(R.string.lock_position),
        } else if ("1".equals(fragment_value)) {
            tvTitle.setText(getString(R.string.wusd));
            arrs = new ArrayList<String>(Arrays.asList(getString(R.string.all),
                    getString(R.string.betted),
                    getString(R.string.kaizhuang),
                    getString(R.string.exchange),
                    getString(R.string.system_reward)));
            pllHistoryBottom.setVisibility(View.GONE);
        }

        llCommonTitle.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

        rvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        quickAdapter = new BaseQuickAdapter(R.layout.item_wicc_record, lists) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                final TradeHistoryBean dataBean = (TradeHistoryBean) item;
                helper.setText(R.id.tv_amount, "0".equals(fragment_value) ?
                        UIUtils.setTextDecimal8(dataBean.getAmount() + "") :
                        UIUtils.setTextDecimal2(dataBean.getAmount() + "")
                );

                helper.setTextColor(R.id.tv_amount,
                        Double.parseDouble(dataBean.getAmount())>=0?
                                getContext().getResources().getColor(R.color.textcolor_green):
                                getContext().getResources().getColor(R.color.textcolor_gray1)
                );

                if (dataBean.getImage() != null) {
                    helper.setImageDrawable(R.id.iv_icon, dataBean.getImage());
                } else {
                    helper.setImageDrawable(R.id.iv_icon, getContext().getResources()
                            .getDrawable(R.drawable.trans));
                }
                helper.setText(R.id.tv_type, dataBean.getType());
                helper.setText(R.id.tv_status, dataBean.getConfirmTime() == null ? "" : dataBean.getConfirmTime());


                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (presenter != null) {
                            presenter.getTradeDatail(dataBean.getRefOrderId());
                        }
                    }
                });
            }
        };

        rvMain.setAdapter(quickAdapter);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.wicc_history_header,
                rvMain, false);
        quickAdapter.addHeaderView(view);
        textlist = view.findViewById(R.id.tv_kinds);
        tv_amount = view.findViewById(R.id.tv_amount);
        tv_amount_cny = view.findViewById(R.id.tv_amount_cny);
        textlist.setOnClickListener(WiccHistoryFragment.this);

        swprl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                // type = 0;
                // textlist.setText(arrs.get(0));

                HistoryListBean listBean = new HistoryListBean();
                if (fragment_value != null) {
                    if ("0".equals(fragment_value)) {
                        listBean.setCoinSymbol(getString(R.string.wicc));
                    } else if ("1".equals(fragment_value)) {
                        listBean.setCoinSymbol(getString(R.string.wusd));
                    }
                }
                listBean.setPageNumber(pageNum);
                listBean.setPageSize(10);
                listBean.setWalletAddress(WalletUtils.getInstance().getAddress(getContext()));

                if (type != 0) {
                    ArrayList<Integer> tyList = new ArrayList<>();
                    tyList.add(type);
                    switch (type) {
                        case 210:/*投注 投注派奖*/
                            tyList.add(300);
                            break;
                        case 220:/*开庄 开庄派奖*/
                            tyList.add(310);
                            break;
                    }
                    listBean.setType(tyList);
                }

                presenter.getAllHistory(listBean);
            }
        });

        quickAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                HistoryListBean listBean = new HistoryListBean();
                if (fragment_value != null) {
                    if ("0".equals(fragment_value)) {
                        listBean.setCoinSymbol(getString(R.string.wicc));
                    } else if ("1".equals(fragment_value)) {
                        listBean.setCoinSymbol(getString(R.string.wusd));
                    }
                }
                listBean.setPageNumber(pageNum);
                listBean.setPageSize(10);
                listBean.setWalletAddress(WalletUtils.getInstance().getAddress(getContext()));
                if (type != 0) {
                    listBean.setType(new ArrayList<Integer>(Arrays.asList(type)));
                }

                presenter.getAllHistory(listBean);
            }
        }, rvMain);
    }


    @Override
    public HistoryPresenter createPresenter() {
        presenter = new HistoryPresenter(this, getContext());

        String addr = SPUtils.get(getContext(), SPConstants.WALLET_ADDRESS, "").toString();
        HistoryListBean listBean = new HistoryListBean();
        if (fragment_value != null) {
            if ("0".equals(fragment_value)) {
                listBean.setCoinSymbol(getString(R.string.wicc));
            } else if ("1".equals(fragment_value)) {
                listBean.setCoinSymbol(getString(R.string.wusd));
            }
        }
        listBean.setPageNumber(pageNum);
        listBean.setPageSize(10);
        listBean.setWalletAddress(addr);


        presenter.getAllHistory(listBean);
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
    public void loadingDialog(String tip) {
        super.loadingDialog(getString(R.string.loading_data));
    }

    @Override
    public void dismissDialog() {
        super.dismissDialog();
    }

    @Override
    public void showTip(String str) {

    }

    @Override
    @OnClick({R.id.iv_back, R.id.btn_send, R.id.btn_qrcode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_qrcode:
                Dialog_full_qrcode dialog_full_qrcode = new Dialog_full_qrcode(getContext(), 1);
                dialog_full_qrcode.show();
                break;
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.tv_kinds:
                final ArrayPopuWindows popupWindow = new ArrayPopuWindows(getContext(),
                        arrs);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.popu_bg));

                 popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setWidth(view.getWidth());
                popupWindow.setBackgroundDrawable(getContext().getResources().
                        getDrawable(R.drawable.popu_bg));
                popupWindow.showAsDropDown(view);

                popupWindow.setitemListener(new ArrayPopuWindows.onItemSelect() {
                    @Override
                    public void itemOnClick(ArrayPopuWindows popuWindows, int position) {
                        pageNum = 1;
                        HistoryListBean listBean = new HistoryListBean();
                        if (fragment_value != null) {
                            if ("0".equals(fragment_value)) {
                                listBean.setCoinSymbol(getString(R.string.wicc));
                            } else if ("1".equals(fragment_value)) {
                                listBean.setCoinSymbol(getString(R.string.wusd));
                            }
                        }
                        listBean.setPageNumber(pageNum);
                        listBean.setPageSize(10);
                        listBean.setWalletAddress(WalletUtils.getInstance().getAddress(getContext()));
                        ArrayList<Integer> list = new ArrayList<>();
                        if (fragment_value != null) {
                            if ("0".equals(fragment_value)) {
                                switch (position) {
                                    case 0:
                                        type = 0;
                                        //listBean.setType(0);
                                        presenter.getAllHistory(listBean);
                                        break;
                                    case 1:
                                        type = 110;
                                        list.add(110);
                                        listBean.setType(list);
                                        presenter.getAllHistory(listBean);
                                        break;
                                    case 2:
                                        type = 120;
                                        list.add(120);
                                        listBean.setType(list);
                                        presenter.getAllHistory(listBean);
                                        break;
                                   /* case 3:
                                        type = 130;
                                        list.add(130);
                                        listBean.setType(list);
                                        presenter.getAllHistory(listBean);
                                        break;*/
                                    case 3:
                                        type = 140;
                                        list.add(140);
                                        listBean.setType(list);
                                        presenter.getAllHistory(listBean);
                                        break;
                                }
                            } else if ("1".equals(fragment_value)) {
                                switch (position) {
                                    case 0:
                                        type = 0;
                                        //listBean.setType(0);
                                        presenter.getAllHistory(listBean);
                                        break;
                                    case 1:
                                        type = 210;
                                        list.add(210);
                                        list.add(300);
                                        listBean.setType(list);
                                        presenter.getAllHistory(listBean);
                                        break;
                                    case 2:
                                        type = 220;
                                        list.add(220);
                                        list.add(310);
                                        listBean.setType(list);
                                        presenter.getAllHistory(listBean);
                                        break;
                                    case 3:
                                        type = 120;
                                        list.add(120);
                                        listBean.setType(list);
                                        presenter.getAllHistory(listBean);
                                        break;
                                    case 4:
                                        type = 600;
                                        list.add(600);
                                        listBean.setType(list);
                                        presenter.getAllHistory(listBean);
                                        break;
                                }
                            }
                        }
                        textlist.setText(arrs.get(position));
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.id.btn_send:
                if (UIUtils.checkWallet(getContext())) {
                    Intent intent1 = new Intent(getActivity(), CommonActivity.class);
                    intent1.putExtra(FRAGMENT_KEY, 2);
                    startActivity(intent1);
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void loadDataSuccess(Object o) {
        ArrayList<TradeHistoryEntity.DataBean> rvList = (ArrayList<TradeHistoryEntity.DataBean>) o;
        ArrayList<TradeHistoryBean> dlists = new ArrayList<>();

        for (int i = 0; i < rvList.size(); i++) {
            TradeHistoryBean tradeHistoryBean = new TradeHistoryBean();
            tradeHistoryBean.setAmount(rvList.get(i).getAmount());
            tradeHistoryBean.setConfirmTime(rvList.get(i).getConfirmTime());
            tradeHistoryBean.setSymbol(rvList.get(i).getCoinSymbol());
            tradeHistoryBean.setStatus(rvList.get(i).getStatus() + "");
            tradeHistoryBean.setImage(getImgType(rvList.get(i).getType(), rvList.get(i).getAmount()));
            tradeHistoryBean.setType(getType(rvList.get(i).getType()));
            tradeHistoryBean.setRefOrderId(rvList.get(i).getId());
            dlists.add(tradeHistoryBean);
        }

        if (pageNum == 1) {
            lists.clear();
        }
        if (swprl != null & swprl.isRefreshing()) {
            swprl.setRefreshing(false);
        }
        lists.addAll(dlists);
        if (dlists.size() == 10) {
            pageNum++;
        }

        if (dlists.size() < 10) {
            quickAdapter.loadMoreEnd(false);
        } else {
            quickAdapter.loadMoreComplete();
        }

        quickAdapter.notifyDataSetChanged();

        if(lists.size()==0){
            tvNodataTip.setVisibility(View.VISIBLE);
        }else{
            tvNodataTip.setVisibility(View.INVISIBLE);
        }

    }

    private String getType(int id) {
        String type = "";
        switch (id) {
            case 110:
                type = getString(R.string.transfer);
                break;
            case 120:
                type = getString(R.string.exchange);
                break;
            case 130:
                type = getString(R.string.lock_position);
                break;
            case 140:
                type = getString(R.string.activation);
                break;
            case 180:
                type = getString(R.string.change_rate);
                break;
            case 210:
                type = getString(R.string.betted);
                break;
            case 300:
                type = getString(R.string.betted);
                break;
            case 220:
                type = getString(R.string.kaizhuang);
                break;
            case 310:
                type = getString(R.string.kaizhuang);
                break;
            case 600:
                type = getString(R.string.system_reward);
                break;
        }
        return type;
    }

    private Drawable getImgType(int type, String amount) {
        Drawable imageid = null;
        switch (type) {
            case 110:
                if (Double.parseDouble(amount) >= 0) {
                    imageid = getContext()
                            .getResources().getDrawable(R.drawable.rece_success);
                } else {
                    imageid = getContext()
                            .getResources().getDrawable(R.drawable.send_amount);
                }
                break;
            case 120:
                if (Double.parseDouble(amount) >= 0) {
                    imageid = getContext()
                            .getResources().getDrawable(R.drawable.rece_success);
                } else {
                    imageid = getContext()
                            .getResources().getDrawable(R.drawable.send_amount);
                }
                break;
            case 130:
                if (Double.parseDouble(amount) >= 0) {
                    imageid = getContext()
                            .getResources().getDrawable(R.drawable.rece_success);
                } else {
                    imageid = getContext()
                            .getResources().getDrawable(R.drawable.send_amount);
                }
                break;
            case 140:
                imageid = getContext()
                        .getResources().getDrawable(R.drawable.activition);
                break;
            case 180:
                if (Double.parseDouble(amount) >= 0) {
                    imageid = getContext()
                            .getResources().getDrawable(R.drawable.rece_success);
                } else {
                    imageid = getContext()
                            .getResources().getDrawable(R.drawable.send_amount);
                }
                break;
            case 210:
                if (Double.parseDouble(amount) >= 0) {
                    imageid = getContext()
                            .getResources().getDrawable(R.drawable.rece_success);
                } else {
                    imageid = getContext()
                            .getResources().getDrawable(R.drawable.send_amount);
                }
                break;
            case 300:
                if (Double.parseDouble(amount) >= 0) {
                    imageid = getContext()
                            .getResources().getDrawable(R.drawable.rece_success);
                } else {
                    imageid = getContext()
                            .getResources().getDrawable(R.drawable.send_amount);
                }
                break;
            case 220:
                if (Double.parseDouble(amount) >= 0) {
                    imageid = getContext()
                            .getResources().getDrawable(R.drawable.rece_success);
                } else {
                    imageid = getContext()
                            .getResources().getDrawable(R.drawable.send_amount);
                }
                break;
            case 310:
                if (Double.parseDouble(amount) >= 0) {
                    imageid = getContext()
                            .getResources().getDrawable(R.drawable.rece_success);
                } else {
                    imageid = getContext()
                            .getResources().getDrawable(R.drawable.send_amount);
                }
                break;
            case 600:
                if (Double.parseDouble(amount) >= 0) {
                    imageid = getContext()
                            .getResources().getDrawable(R.drawable.rece_success);
                } else {
                    imageid = getContext()
                            .getResources().getDrawable(R.drawable.send_amount);
                }
                break;
        }
        return imageid;
    }

    @Override
    public void loadDataFail(String failcode) {
        if (quickAdapter != null) {
            quickAdapter.loadMoreFail();
        }

        if (swprl != null && swprl.isRefreshing()) {
            swprl.setRefreshing(false);
        }
    }

    @Override
    public void setAmount(AccountInfoEntity.DataBean amount) {
        if(fragment_value!=null){
        if("0".equals(fragment_value)){
        tv_amount.setText(UIUtils.getMoney8(amount.getCustomerAccountList().get(0).getBalanceAvailable() + ""));
        tv_amount_cny.setText(" ≈ " + UIUtils.setTextDecimal2(amount.getCustomerAccountList().get(0)
                .getBalanceAvailable() * amount.getWiccPriceCNY()
                + "") + "(CNY)");
        }else if("1".equals(fragment_value)){
            tv_amount.setText(UIUtils.getMoney2(amount.getCustomerAccountList().get(2).getBalanceAvailable() + ""));
            tv_amount_cny.setText(" ");
        }
        }
    }

    @Override
    public void tradeDetail(TradeDetailEntity detailEntity) {
        detailEntity.getData().setType(getType(Integer.parseInt(detailEntity.getData().getType())));
        Tradedetail_Dialog alertDialog = new Tradedetail_Dialog(getContext(), detailEntity);
        alertDialog.getWindow().setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.trans));
        alertDialog.show();
    }

}
