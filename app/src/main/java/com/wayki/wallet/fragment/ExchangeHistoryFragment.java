package com.wayki.wallet.fragment;

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
import com.wayki.wallet.bean.entity.AccountInfoEntity;
import com.wayki.wallet.bean.entity.ExchangeHistoryEntity;
import com.wayki.wallet.bean.entity.TradeDetailEntity;
import com.wayki.wallet.mvp.historypresenter.HistoryPresenter;
import com.wayki.wallet.mvp.historypresenter.HistoryView;
import com.wayki.wallet.utils.UIUtils;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExchangeHistoryFragment extends BaseFragment<HistoryView, HistoryPresenter> implements HistoryView {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rv_main)
    RecyclerView rvMain;
    @Bind(R.id.swprl)
    SwipeRefreshLayout swprl;
    ArrayList<ExchangeHistoryEntity.DataBean> lists = new ArrayList<>();
    BaseQuickAdapter quickAdapter;
    int pagenum = 1;
    int loadPagesize = 0;
    @Bind(R.id.pll_nodata_tip)
    PercentLinearLayout tvNodataTip;
    @Bind(R.id.tv_bottom)
    TextView tvBottom;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_common_rv;
    }

    @Override
    public String getEmptyText() {
        return null;
    }

    @Override
    public void initUI() {
        tvTitle.setText(getString(R.string.wicc_wkd_record));
        swprl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (presenter != null) {
                    pagenum = 1;
                    presenter.getHistoryRecord(1);
                }
            }
        });

        rvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        quickAdapter = new BaseQuickAdapter(R.layout.item_wicc_wkd_record, lists) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                ExchangeHistoryEntity.DataBean dataBean = (ExchangeHistoryEntity.DataBean) item;
                if (dataBean.getDirection() != null) {
                    switch (dataBean.getDirection()) {
                        case "100":
                            helper.setImageDrawable(R.id.iv_icon1, getActivity().getResources()
                                    .getDrawable(R.drawable.iv_wkd));
                            helper.setImageDrawable(R.id.iv_icon2, getActivity().getResources()
                                    .getDrawable(R.drawable.iv_wicc));
                            helper.setText(R.id.tv_amount1, UIUtils.getMoney4(dataBean.getWusdAmount() + ""));
                            helper.setText(R.id.tv_amount2, dataBean.getWiccAmount());
                            break;
                        case "200":
                            helper.setImageDrawable(R.id.iv_icon1, getActivity().getResources()
                                    .getDrawable(R.drawable.iv_wicc));
                            helper.setImageDrawable(R.id.iv_icon2, getActivity().getResources()
                                    .getDrawable(R.drawable.iv_wkd));
                            helper.setText(R.id.tv_amount1, dataBean.getWiccAmount());
                            helper.setText(R.id.tv_amount2, UIUtils.getMoney4(dataBean.getWusdAmount() + ""));
                            break;
                    }
                    helper.setText(R.id.tv_exchange_number, getString(R.string.exchange_rate) +
                            UIUtils.getMoney4(dataBean.getExchangeRate()));
                    helper.setText(R.id.tv_exchange_time, getString(R.string.exchange_time) +
                            dataBean.getExchangeTime());
                }

                switch (dataBean.getStatus()) {
                    case 100:
                        helper.setText(R.id.tv_exchange_status, getString(R.string.waiting_confirmation));
                        helper.setTextColor(R.id.tv_exchange_status, getContext().getResources().getColor(R.color.textcolor_white_blue));
                        break;
                    case 400:
                        helper.setText(R.id.tv_exchange_status, getString(R.string.exchangeing));
                        helper.setTextColor(R.id.tv_exchange_status, getContext().getResources().getColor(R.color.textcolor_white_blue));
                        break;
                    case 800:
                        helper.setText(R.id.tv_exchange_status, getString(R.string.exchange_success));
                        helper.setTextColor(R.id.tv_exchange_status, getContext().getResources().getColor(R.color.textcolor_white_blue));
                        break;
                    case 900:
                        helper.setText(R.id.tv_exchange_status, getString(R.string.exchange_failure));
                        helper.setTextColor(R.id.tv_exchange_status, getContext().getResources().getColor(R.color.textcolor_red));
                        break;
                }
            }
        };

        rvMain.setAdapter(quickAdapter);

        quickAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (presenter != null && pagenum > 1) {
                    presenter.getHistoryRecord(pagenum);
                }
            }
        }, rvMain);
    }

    @Override
    public HistoryPresenter createPresenter() {
        presenter = new HistoryPresenter(this, getContext());
        presenter.getHistoryRecord(pagenum);
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

    @OnClick({R.id.iv_back})
    public void OnClick(View view) {
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

    @Override
    public void loadDataSuccess(Object obj) {
        if (swprl.isRefreshing()) {
            swprl.setRefreshing(false);
        }
        if (pagenum == 1) {
            lists.clear();
        }
        ArrayList<ExchangeHistoryEntity.DataBean> datalist = (ArrayList<ExchangeHistoryEntity.DataBean>) obj;
        lists.addAll(datalist);
        if (datalist.size() == 10) {
            pagenum++;
        }
        if (datalist.size() < 10) {
            quickAdapter.loadMoreEnd(false);
        } else {
            quickAdapter.loadMoreComplete();
        }
        quickAdapter.notifyDataSetChanged();

        if (lists.size() == 0) {
            tvNodataTip.setVisibility(View.VISIBLE);
        } else {
            tvNodataTip.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void loadDataFail(String failcode) {

    }

    @Override
    public void setAmount(AccountInfoEntity.DataBean amount) {

    }

    @Override
    public void tradeDetail(TradeDetailEntity detailEntity) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
