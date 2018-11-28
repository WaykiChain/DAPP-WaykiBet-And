package com.wayki.wallet.mvp.transferpresenter;

import com.wayki.wallet.mvp.historypresenter.HistoryView;

public interface TransferView extends HistoryView {
    String getAddress();
    String getAmount();
    String getRemarkr();
    String getFee();
    void showTransDialog();
}
