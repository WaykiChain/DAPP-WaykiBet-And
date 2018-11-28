package com.wayki.wallet.mvp;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wayki.wallet.R;
import com.wayki.wallet.ui.loading.AVLoadingIndicatorView;
import com.wayki.wallet.utils.UIUtils;


/**
 * 加载布局
 */
public class LoadLayoutHelper {
    /*上下文，创建view的时候需要用到*/
    private Context mContext;
    /*base view*/
    private FrameLayout mContentView;
    /*用户定义的view*/
    private View mUserView;

    private LayoutInflater mInflater;
    private ImageView loadIconIv;
    private TextView loadMsgTv;
    private View loadLayout;

    private LoadFailClickListener clickListener;
    private AVLoadingIndicatorView loadingPb;

    public void setClickListener (LoadFailClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public LoadLayoutHelper(Context context, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from (mContext);
        /*初始化整个内容*/
        initContentView ();
        /*初始化用户定义的布局*/
        initUserView (layoutId);
        /*初始化加载布局*/
        initLoadLayout();
    }

    private void initContentView () { /*直接创建一个帧布局，作为视图容器的父容器*/
        mContentView = new FrameLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams (params);
        mContentView.setBackgroundResource (R.color.white);

    }

    private void initLoadLayout() {
        loadLayout = mInflater.inflate (R.layout.layout_load_data, null);
        loadIconIv = loadLayout.findViewById (R.id.iv_load_icon);
        loadMsgTv = loadLayout.findViewById (R.id.tv_load_msg);
        loadingPb = loadLayout.findViewById (R.id.indicator);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        mContentView.addView (loadLayout,params);

        loadLayout.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                clickListener.onClickLoadFailText ();
            }
        });

        loadingPb.setIndicator("LineSpinFadeLoaderIndicator");
        loadingPb.getIndicator().setColor(UIUtils.getColor(R.color.colorDeepOrangePrimary));
    }

    private void initUserView (int id) {
        mUserView = mInflater.inflate (id, null,false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.addView (mUserView, params);
    }

    public FrameLayout getContentView () {
        return mContentView;
    }

    public void setLoadMsgText(String msgText){
        loadMsgTv.setText (msgText);
    }

    public void setLoadIconIv(int resId){
        loadingPb.setVisibility (View.GONE);
        loadIconIv.setVisibility (View.VISIBLE);

        loadIconIv.setImageResource (resId);
    }

    public void setProBarVisi(int visi){
        loadingPb.setVisibility (visi);
    }

    public void setLoadIconIvVisi(int visi){
        loadIconIv.setVisibility (visi);
    }

    public void setLoadViewVisible(int viewVisible){
        loadLayout.setVisibility (viewVisible);
    }

    public interface LoadFailClickListener{
        void onClickLoadFailText();
    }
}
