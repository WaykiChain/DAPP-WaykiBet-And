package com.wayki.wallet.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wayki.wallet.R;
import com.wayki.wallet.utils.UIUtils;

import java.util.ArrayList;

public class ArrayPopuWindows extends PopupWindow {
    private ArrayList<String> lists;
    private Context mContext;
    private onItemSelect itemSelect;

    public ArrayPopuWindows(@NonNull Context context, ArrayList<String> lists) {
        super(context);
        this.mContext = context;
        this.lists = lists;
        View mContentView = LayoutInflater.from(context).inflate(R.layout.layout_popu, null);
        setContentView(mContentView);
      //  int screenHeigh = context.getResources().getDisplayMetrics().heightPixels;
        //this.setHeight(Math.round(screenHeigh * 0.4f));
        RecyclerView recyclerView = mContentView.findViewById(R.id.rv_main);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        BaseQuickAdapter quickAdapter = new BaseQuickAdapter(R.layout.item_popu, lists) {
            @Override
            protected void convert(final BaseViewHolder helper, Object item) {

                RecyclerView.LayoutParams param = new RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        UIUtils.dip2px(40));
                helper.getConvertView().setLayoutParams(param);


                helper.setText(R.id.tv_popu, item.toString());
                helper.setOnClickListener(R.id.tv_popu, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      if(itemSelect!=null){
                          itemSelect.itemOnClick(ArrayPopuWindows.this,
                                  helper.getAdapterPosition());
                      }
                    }
                });
            }

        };
        recyclerView.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(quickAdapter);
    }


 /*   @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT == 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (Build.VERSION.SDK_INT == 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor, xoff, yoff);
    }*/

    public void setitemListener(onItemSelect itemSelect){
       this.itemSelect=itemSelect;
    }

    public interface onItemSelect{
        void itemOnClick(ArrayPopuWindows popuWindows,int position);
    }
/*
    @Override
    public void showAsDropDown(View anchor) {
        if(Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if(Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor, xoff, yoff);
    }*/

}
