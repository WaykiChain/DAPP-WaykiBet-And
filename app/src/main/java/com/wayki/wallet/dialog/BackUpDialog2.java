package com.wayki.wallet.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wayki.wallet.R;
import com.wayki.wallet.bean.BackupWordsBean;
import com.wayki.wallet.utils.UIUtils;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BackUpDialog2 extends AlertDialog {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_title_right)
    ImageView ivTitleRight;
    @Bind(R.id.ll_common_title)
    PercentLinearLayout llCommonTitle;
    @Bind(R.id.pll_input)
    TextView pllInput;
    @Bind(R.id.btn_create)
    Button btnCreate;
    @Bind(R.id.pll_bg)
    PercentLinearLayout pllBg;
    @Bind(R.id.rv_dialog_main2)
    RecyclerView rvDialogMain2;
    private String words;
    StringBuilder sb;

    public BackUpDialog2(@NonNull Context context, String lists) {
        super(context, R.style.DialogFullscreen);
        this.words = lists;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_force_backup2);
        ButterKnife.bind(this, this.getWindow().getDecorView());
        String[] word = words.split(" ");
        llCommonTitle.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
        final ArrayList<BackupWordsBean> lists = new ArrayList<>();
        for (int i = 0; i < word.length; i++) {
            lists.add(new BackupWordsBean(word[i], false));
        }
        Collections.shuffle(lists);
        sb = new StringBuilder();
        BaseQuickAdapter baseQuickAdapter = new BaseQuickAdapter(R.layout.item_words, lists) {
            @Override
            protected void convert(final BaseViewHolder helper, final Object item) {
                final BackupWordsBean wordsBean = (BackupWordsBean) item;
                helper.setText(R.id.checkbox, wordsBean.getWords().toString());
                helper.getView(R.id.pll_backup_bg).setSelected(wordsBean.isSelected());
                helper.setOnClickListener(R.id.pll_backup_bg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < lists.size(); i++) {
                            if (i == helper.getAdapterPosition()) {
                                if (!wordsBean.isSelected()) {
                                    helper.getView(R.id.pll_backup_bg).setSelected(true);
                                    lists.get(i).setSelected(true);
                                    sb.append(lists.get(i).getWords() + " ");
                                    pllInput.setText(sb.toString());
                                } else {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    if (!TextUtils.isEmpty(pllInput.getText().toString())) {
                                        String[] aa = pllInput.getText().toString().split(" ");
                                        if (aa.length > 0) {
                                            for (int j = 0; j < aa.length; j++) {
                                                String curword = lists.get(helper.getAdapterPosition()).getWords();
                                                if (!curword.equals(aa[j])) {
                                                    stringBuilder.append(aa[j] + " ");
                                                }
                                            }
                                        }
                                    }
                                    sb.delete(0, sb.toString().length());
                                    sb.append(stringBuilder);
                                    pllInput.setText(sb.toString());
                                    lists.get(i).setSelected(false);
                                }
                            }
                        }
                        notifyDataSetChanged();
                    }
                });
            }

        };
        rvDialogMain2.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvDialogMain2.setAdapter(baseQuickAdapter);
        tvTitle.setText(getContext().getString(R.string.backup_words));
    }

    @OnClick({R.id.iv_back, R.id.btn_create})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                dismiss();
                break;
            case R.id.btn_create:
                String sbwords=pllInput.getText().toString().trim();
                if(sbwords.equals(words)){
                    UIUtils.showToast(UIUtils.getString(R.string.backup_success));
                    dismiss();
                }else{
                    UIUtils.showToast(UIUtils.getString(R.string.backup_failer));
                }
                break;
        }
    }
}
