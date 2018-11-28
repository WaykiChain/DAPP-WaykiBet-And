package com.wayki.wallet.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.orhanobut.logger.Logger;
import com.wayki.wallet.R;
import com.wayki.wallet.application.ActivityManager;
import com.wayki.wallet.dialog.LoadingDialog;
import com.wayki.wallet.dialog.TipDialog;
import com.wayki.wallet.dialog.WalletDailog;
import com.wayki.wallet.mvp.LoadLayoutHelper;
import com.wayki.wallet.mvp.base.BasePresenter;
import com.wayki.wallet.mvp.base.BaseView;
import com.wayki.wallet.utils.SystemBarTintManager;
import com.wayki.wallet.utils.UIUtils;


/**
 * 基础activity
 */
public abstract class BaseMvpActivity<V extends BaseView, P extends BasePresenter<V>>
        extends MvpActivity<V, P> implements BaseView, LoadLayoutHelper.LoadFailClickListener {
    protected LoadLayoutHelper helper;//布局帮助类，将我们的布局和空页面合并在一起

    protected String emptyMsg;
    public LoadingDialog dialog;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        ActivityManager.create ().addActivity (this);
        Logger.i( getClass ().getSimpleName ());
        initSystemBarTint();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (Build.VERSION.SDK_INT >= 21) {
            /*  View decorView = getWindow().getDecorView();
             int  uiOption = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_IMMERSIVE
                   ;

             *//* |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN*//*

            decorView.setSystemUiVisibility(uiOption);
            getWindow().setStatusBarColor(Color.TRANSPARENT);*/
        }
    }
    /**
     * @param layoutResID
     *     将我们自定义的布局和helper的布局合并在一起显示
     */
    @Override
    public void setContentView (@LayoutRes int layoutResID) {
        helper = new LoadLayoutHelper (this, layoutResID);
        setContentView (helper.getContentView ());
        helper.setLoadViewVisible (View.GONE);
        helper.setClickListener (this);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        ActivityManager.create ().removeActivity (this);
    }

   /* @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);

        getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                       );
        //   View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        //  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
         // | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //  | View.SYSTEM_UI_FLAG_FULLSCREEN
    }*/

    /**
     * 显示helper加载进度的方法
     */
    @Override
    public void showLoadProgress () {
        helper.setProBarVisi (View.VISIBLE);
        helper.setLoadIconIvVisi (View.GONE);
        helper.setLoadViewVisible (View.VISIBLE);
        helper.setLoadMsgText (getString(R.string.loading_data));
    }

    /**
     * 隐藏helper的页面的方法
     */
    @Override
    public void hideLoadProgress () {
        helper.setLoadViewVisible (View.GONE);
    }

    /**
     * 自定义加载失败页面的方法
     */
    @Override
    public void showLoadFail (String errorMsg, @IdRes int failId , int isVisible) {
        helper.setLoadIconIv (failId);
        helper.setLoadMsgText (errorMsg);
        helper.setLoadViewVisible (isVisible);
    }

    /**
     * 默认加载失败页面的方法
     */
    @Override
    public void normalLoadFail () {
        helper.setLoadIconIv (R.mipmap.icon_load_fail);
        helper.setLoadMsgText (getString(R.string.network_error));
        helper.setLoadViewVisible (View.VISIBLE);
    }

    /**
     * 显示加载成功的方法，其关闭了helper的显示页面
     * 如果网络加载成功后，需要携带数据返回，请自定义方法返回
     */
    @Override
    public void showLoadSuccess () {
        helper.setLoadViewVisible (View.GONE);
    }

    /**
     * 显示helper空页面的方法
     */
    @Override
    public void showEmptyPager () {
        helper.setLoadIconIv (R.mipmap.icon_no_data);
        helper.setLoadMsgText (getEmptyText ());
        helper.setLoadViewVisible (View.VISIBLE);
    }

    /**
     * 提示没有网络的方法
     */
    @Override
    public void showNoNetError () {
        UIUtils.showToast(getString(R.string.pls_check_net));
    }

    /**
     * @return 加载失败的提示语
     */
    public abstract String getEmptyText ();

    @Override
    public void setEmptyMsg (String msg) {
        emptyMsg = msg;
    }


    @Override
    public  void loadingDialog(String tip){
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = LoadingDialog.showDialog(this);
        dialog.setTip(tip);
        dialog.show();
    }

    @Override
    public  void dismissDialog(){
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public  void showTip(String msg){
    /*    new AlertDialog.Builder(this)
               // .setTitle(UIUtils.getString(R.string.tip))
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();*/

        TipDialog tipDialog=new TipDialog(this, R.style.DialogStyle
                , R.layout.dialog_signal_tip);
        tipDialog.setCancelable(false);
        tipDialog.addOnConfirmListener(new WalletDailog.onComfirmListener() {
            @Override
            public void onConfirm(WalletDailog dialog) {
                dialog.dismiss();
            }
        });
        tipDialog.show();
        tipDialog.setMessage(msg);

    }



    /** 子类可以重写改变状态栏颜色 */
    protected int setStatusBarColor() {
        return getColorPrimary();
    }

    /** 子类可以重写决定是否使用透明状态栏 */
    protected boolean translucentStatusBar() {
        return false;
    }



    /** 设置状态栏颜色 */

   protected void initSystemBarTint() {
        Window window = getWindow();
        if (translucentStatusBar()) {
            // 设置状态栏全透明
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            return;
        }

       // 沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上使用原生方法
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(setStatusBarColor());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4-5.0使用三方工具类，有些4.4的手机有问题，这里为演示方便，不使用沉浸式
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(setStatusBarColor());
        }
    }

    /** 获取主题色 */
    public int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /** 获取深主题色 */
    public int getDarkColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }

    /** 初始化 Toolbar */
    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, int resTitle) {
        initToolBar(toolbar, homeAsUpEnabled, getString(resTitle));
    }




}
